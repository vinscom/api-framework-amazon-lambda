package in.erail.amazon.lambda.eventsource;

import in.erail.amazon.lambda.EventSource;
import io.vertx.core.json.JsonObject;
import java.util.Optional;

/**
 *
 * @author vinay
 */
public class APIGatewayProxyEventSource implements EventSource {

  @Override
  public boolean check(JsonObject pEvent) {
    return pEvent.containsKey("httpMethod");
  }

  @Override
  public JsonObject transform(JsonObject pEvent) {

    Boolean isBase64Encoded
            = Optional
                    .ofNullable(pEvent.getBoolean("isBase64Encoded"))
                    .orElse(Boolean.FALSE);

    byte[] body = null;

    if (isBase64Encoded == false && pEvent.containsKey("body")) {
      Optional<String> b = Optional.ofNullable(pEvent.getString("body"));
      if (b.isPresent()) {
        body = b.get().getBytes();
      }
    }

    if (body != null) {
      pEvent.remove("body");
      pEvent.put("body", body);
    }

    return pEvent;
  }

}
