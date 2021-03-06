package in.erail.amazon.lambda.eventsource;

import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;
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
            .put("body", pEvent.toString().getBytes())
            .put("headers", new JsonObject()
                    .put(HttpHeaders.CONTENT_TYPE, MediaType.JSON_UTF_8.toString())
                    .put("aws-event-source", getEventSourceName().toString()));;

    return newRequest;
  }

}
