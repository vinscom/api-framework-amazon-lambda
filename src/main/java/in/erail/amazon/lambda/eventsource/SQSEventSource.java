package in.erail.amazon.lambda.eventsource;

import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;
import in.erail.amazon.lambda.EventSource;
import in.erail.glue.common.Util;
import io.vertx.core.json.JsonObject;
import java.util.Optional;

/**
 *
 * @author vinay
 */
public class SQSEventSource implements EventSource {

  public static final String SQS_URL = "/aws/sqs";
  public static final String SQS_ENV_NAME = "api.framework.lambda.sqs.url";
  private final String url = Util.getEnvironmentValue(SQS_ENV_NAME, SQS_URL);

  @Override
  public boolean check(JsonObject pEvent) {
    return pEvent.containsKey("Records")
            && Optional
                    .ofNullable(pEvent.getJsonArray("Records"))
                    .filter(o -> o.size() >= 1)
                    .map(o -> (JsonObject) o.iterator().next())
                    .filter(o -> o.containsKey("eventSource") && "aws:sqs".equals(o.getString("eventSource")))
                    .isPresent();
  }

  @Override
  public JsonObject transform(JsonObject pEvent) {
    
    JsonObject newRequest = new JsonObject()
            .put("path", url)
            .put("httpMethod", "POST")
            .put("body", pEvent.toString().getBytes())
            .put("headers", new JsonObject().put(HttpHeaders.CONTENT_TYPE, MediaType.JSON_UTF_8.toString()));

    return newRequest;
  }

}
