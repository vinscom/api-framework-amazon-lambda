package in.erail.amazon.lambda.eventsource;

import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;
import in.erail.amazon.lambda.EventSource;
import in.erail.glue.common.Util;
import io.vertx.core.json.JsonObject;

/**
 *
 * @author vinay
 */
public class SNSEventSource implements EventSource {

  public static final String URL = "/aws/sns";
  public static final String ENV_NAME = "api.framework.lambda.sns.url";
  private final String url = Util.getEnvironmentValue(ENV_NAME, URL);

  @Override
  public EventSourceName getEventSourceName() {
    return EventSourceName.SNS;
  }

  @Override
  public JsonObject transform(JsonObject pEvent) {

    JsonObject newRequest = new JsonObject()
            .put("path", url)
            .put("httpMethod", "POST")
            .put("body", pEvent.toString().getBytes())
            .put("headers", new JsonObject()
                    .put(HttpHeaders.CONTENT_TYPE, MediaType.JSON_UTF_8.toString())
                    .put("aws-event-source", getEventSourceName().toString()));

    return newRequest;
  }

}
