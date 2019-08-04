package in.erail.amazon.lambda.eventsource;

import in.erail.amazon.lambda.EventSource;
import io.vertx.core.json.JsonObject;

/**
 *
 * @author vinay
 */
public class DefaultEventSource implements EventSource {

  @Override
  public boolean check(JsonObject pEvent) {
    return true;
  }

  @Override
  public JsonObject transform(JsonObject pEvent) {

    JsonObject newRequest = new JsonObject()
            .put("body", pEvent.toString().getBytes());

    return newRequest;
  }

}
