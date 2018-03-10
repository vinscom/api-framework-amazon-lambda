package in.erail.amazon.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.common.base.Strings;
import com.google.common.io.ByteStreams;
import in.erail.glue.Glue;
import in.erail.service.RESTService;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.eventbus.Message;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static in.erail.common.FrameworkConstants.RoutingContext.Json.*;

/**
 *
 * @author vinay
 */
public class AWSLambda implements RequestStreamHandler {

  private static final String SERVICE_ENV = "SERVICE";
  private static final String SERVICE_SYS_PROP = "service";
  private final RESTService mService;

  public AWSLambda() {
    String component = System.getenv(SERVICE_ENV);
    if (Strings.isNullOrEmpty(component)) {
      component = System.getProperty(SERVICE_SYS_PROP);
    }
    mService = Glue.instance().resolve(component);
  }

  @Override
  public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {

    JsonObject requestJson = new JsonObject(Buffer.buffer(ByteStreams.toByteArray(inputStream)));
    JsonObject responseJson = handleMessage(requestJson);

    try (OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8")) {
      writer.write(responseJson.toString());
    }
  }

  public JsonObject handleMessage(JsonObject pJsonObject) {

    JsonObject msg;

    try {
      CompletableFuture<JsonObject> result = new CompletableFuture<>();
      MessageAWSLambda<JsonObject> message = new MessageAWSLambda<>(result, pJsonObject);

      Single
              .just(message)
              .map(m -> new Message<JsonObject>(message))
              .subscribeOn(Schedulers.computation())
              .subscribe(m -> getService().process(m));

      msg = result.get();
    } catch (InterruptedException | ExecutionException ex) {
      msg = new JsonObject();
      Logger.getLogger(AWSLambda.class.getName()).log(Level.SEVERE, null, ex);
    }

    return transform(msg);
  }

  public JsonObject transform(JsonObject pMsg) {

    if (!pMsg.containsKey(IS_BASE64_ENCODED)) {
      pMsg.put(IS_BASE64_ENCODED, Boolean.FALSE);
    }

    if (!pMsg.containsKey(STATUS_CODE)) {
      pMsg.put(STATUS_CODE, "200");
    }

    if (!pMsg.containsKey(HEADERS)) {
      pMsg.put(HEADERS, new JsonObject());
    }

    if (!pMsg.containsKey(BODY)) {
      pMsg.put(BODY, new JsonObject().toString());
    } else {
      String body = pMsg.getValue(BODY).toString();
      pMsg.put(BODY, body);
    }

    return pMsg;
  }

  public RESTService getService() {
    return mService;
  }

}
