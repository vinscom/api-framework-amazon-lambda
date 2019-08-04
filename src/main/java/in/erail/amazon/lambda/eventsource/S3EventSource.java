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
public class S3EventSource implements EventSource {

  public static final String S3_URL = "/aws/s3";
  public static final String S3_ENV_NAME = "api.framework.lambda.s3.url";
  private final String s3url = Util.getEnvironmentValue(S3_ENV_NAME, S3_URL);

  @Override
  public boolean check(JsonObject pEvent) {
    return pEvent.containsKey("Records")
            && Optional
                    .ofNullable(pEvent.getJsonArray("Records"))
                    .filter(o -> o.size() >= 1)
                    .map(o -> (JsonObject) o.iterator().next())
                    .filter(o -> o.containsKey("s3"))
                    .isPresent();
  }

  @Override
  public JsonObject transform(JsonObject pEvent) {

    JsonObject newRequest = new JsonObject()
            .put("path", s3url)
            .put("httpMethod", "POST")
            .put("body", pEvent.toString().getBytes())
            .put("headers", new JsonObject().put(HttpHeaders.CONTENT_TYPE,MediaType.JSON_UTF_8.toString()));

    return newRequest;
  }

}
