package in.erail.amazon.lambda;

import io.vertx.core.json.JsonObject;

/**
 *
 * @author vinay
 */
public interface EventSource {

  boolean check(JsonObject pEvent);

  JsonObject transform(JsonObject pEvent);
}
