package in.erail.amazon.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.io.ByteStreams;
import in.erail.glue.Glue;
import in.erail.service.RESTService;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import in.erail.model.RequestEvent;
import in.erail.model.ResponseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author vinay
 */
public class AWSLambda implements RequestStreamHandler {

  protected Logger log = LogManager.getLogger(AWSLambda.class.getCanonicalName());
  private static final String SERVICE_ENV = "SERVICE";
  private static final String SERVICE_SYS_PROP = "service";
  private final RESTService mService;
  private final List<String> allowedFields = new ArrayList<>();

  public AWSLambda() {

    allowedFields.add("isBase64Encoded");
    allowedFields.add("statusCode");
    allowedFields.add("headers");
    allowedFields.add("multiValueHeaders");
    allowedFields.add("body");

    String component = System.getenv(SERVICE_ENV);
    if (Strings.isNullOrEmpty(component)) {
      component = System.getProperty(SERVICE_SYS_PROP);
    }

    mService = Glue.instance().resolve(component);
  }

  @Override
  public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
    try (OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8")) {
      JsonObject requestJson = new JsonObject(Buffer.buffer(ByteStreams.toByteArray(inputStream)));
      log.debug(() -> requestJson.toString());
      String resp = handleMessage(requestJson).blockingGet();
      log.debug(() -> resp);
      writer.write(resp);
    }
  }

  public Single<String> handleMessage(JsonObject pRequest) {
    return Single
            .just(pRequest)
            .subscribeOn(Schedulers.computation())
            .map(this::convertBodyToBase64)
            .map(reqJson -> reqJson.mapTo(RequestEvent.class))
            .flatMapMaybe(req -> getService().process(req))
            .toSingle(new ResponseEvent())
            .map(resp -> JsonObject.mapFrom(resp))
            .map(this::sanatizeResponse)
            .map(respJson -> respJson.toString());
  }

  protected JsonObject sanatizeResponse(JsonObject pResp) {
    Preconditions.checkNotNull(pResp);

    Set<String> keys = new HashSet<>(pResp.fieldNames());

    keys
            .stream()
            .filter(key -> !allowedFields.contains(key))
            .forEach(key -> pResp.remove(key));

    return pResp;
  }

  protected JsonObject convertBodyToBase64(JsonObject pRequest) {

    Boolean isBase64Encoded
            = Optional
                    .ofNullable(pRequest.getBoolean("isBase64Encoded"))
                    .orElse(Boolean.FALSE);

    if (isBase64Encoded == false && pRequest.containsKey("body")) {
      Optional<String> body = Optional.ofNullable(pRequest.getString("body"));
      body.ifPresent((t) -> {
        pRequest.remove("body");
        pRequest.put("body", body.get().getBytes());
      });
    }

    return pRequest;
  }

  public RESTService getService() {
    return mService;
  }

}
