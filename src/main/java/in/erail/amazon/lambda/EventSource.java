package in.erail.amazon.lambda;

import in.erail.amazon.lambda.eventsource.EventSourceName;
import io.vertx.core.json.JsonObject;
import java.util.Optional;

/**
 *
 * @author vinay
 */
public interface EventSource {

  default boolean check(JsonObject pEvent) {
    return Optional
            .ofNullable(pEvent.getJsonArray("Records"))
            .filter(o -> o.size() >= 1)
            .map(o -> (JsonObject) o.iterator().next())
            .map(o -> Optional.ofNullable(o.getString("eventSource")).or(() -> Optional.ofNullable(o.getString("EventSource"))))
            .filter(o -> o.isPresent() && getEventSourceName().event().equals(o.get()))
            .isPresent();
  }

  default EventSourceName getEventSourceName() {
    return EventSourceName.DEFAULT;
  }

  JsonObject transform(JsonObject pEvent);
}
