package in.erail.amazon.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.io.ByteStreams;
import in.erail.amazon.lambda.eventsource.APIGatewayProxyEventSource;
import in.erail.amazon.lambda.eventsource.DefaultEventSource;
import in.erail.amazon.lambda.eventsource.S3EventSource;
import in.erail.amazon.lambda.eventsource.SQSEventSource;
import in.erail.glue.Glue;
import in.erail.glue.common.Util;
import in.erail.model.Event;
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
import io.reactivex.Observable;
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
  private static final String SERVICE_ENV = "service";
  private final RESTService mService;
  private EventSource[] eventSource;
  private final List<String> allowedFields = new ArrayList<>();

  public AWSLambda() {

    allowedFields.add("isBase64Encoded");
    allowedFields.add("statusCode");
    allowedFields.add("headers");
    allowedFields.add("multiValueHeaders");
    allowedFields.add("body");

    String component = Util.getEnvironmentValue(SERVICE_ENV);

    if (Strings.isNullOrEmpty(component)) {
      throw new RuntimeException("Service not defined in lambda environment");
    }

    mService = Glue.instance().resolve(component);

    eventSource = new EventSource[]{new APIGatewayProxyEventSource(), new S3EventSource(), new SQSEventSource(), new DefaultEventSource()};
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
    return Observable
            .fromArray(eventSource)
            .subscribeOn(Schedulers.computation())
            .filter(e -> e.check(pRequest))
            .firstElement()
            .map(e -> e.transform(pRequest))
            .map(reqJson -> reqJson.mapTo(getService().getRequestEventClass()))
            .doOnSuccess(this::populateSystemProperties)
            .flatMap(req -> getService().handleEvent(getService().createEvent(req)))
            .toSingle(new Event())
            .map(resp -> JsonObject.mapFrom(resp.getResponse()))
            .map(this::sanatizeResponse)
            .map(respJson -> respJson.toString());
  }

  protected void populateSystemProperties(RequestEvent pRequest) {

    if (pRequest.getRequestContext() == null) {
      return;
    }

    Optional
            .ofNullable(pRequest.getRequestContext().get("stage"))
            .ifPresent(s -> System.setProperty("stage", s.toString()));
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

  public RESTService getService() {
    return mService;
  }

}
