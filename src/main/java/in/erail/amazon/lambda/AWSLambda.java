package in.erail.amazon.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
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

/**
 *
 * @author vinay
 */
public class AWSLambda implements RequestStreamHandler {

  private final RESTService mService;

  public AWSLambda(String pComponent) {
    mService = Glue.instance().resolve(pComponent);
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

    if (!pMsg.containsKey("isBase64Encoded")) {
      pMsg.put("isBase64Encoded", Boolean.FALSE);
    }

    if (!pMsg.containsKey("statusCode")) {
      pMsg.put("statusCode", "200");
    }

    if (!pMsg.containsKey("headers")) {
      pMsg.put("headers", new JsonObject());
    }

    if (!pMsg.containsKey("body")) {
      pMsg.put("body", new JsonObject().toString());
    } else {
      String body = pMsg.getValue("body").toString();
      pMsg.put("body", body);
    }

    return pMsg;
  }

  public RESTService getService() {
    return mService;
  }

}
