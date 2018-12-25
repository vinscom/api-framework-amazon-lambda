package in.erail.service;

import in.erail.common.FrameworkConstants;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.eventbus.Message;

public class SessionGetService extends RESTServiceImpl {

  private JsonArray mSessions = new JsonArray();

  public SessionGetService() {
    mSessions.add("S1");
    mSessions.add("S2");
    mSessions.add("S3");
    mSessions.add("S4");
    mSessions.add("S5");
  }

  @Override
  public void process(Message<JsonObject> pMessage) {
    pMessage
            .reply(new JsonObject().put(
                    FrameworkConstants.RoutingContext.Json.BODY, 
                    getSessions().toString().getBytes()));
  }

  public JsonArray getSessions() {
    return mSessions;
  }

  public void setSessions(JsonArray pSessions) {
    this.mSessions = pSessions;
  }

}
