package in.erail.service;

import in.erail.glue.Glue;
import in.erail.server.Server;
import io.vertx.core.json.JsonArray;

import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.Timeout;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class HelloServiceTest {

  @Rule
  public Timeout rule = Timeout.seconds(2000);

  @SuppressWarnings("deprecation")
  @Test
  public void testGetRequest(TestContext context) {

    Async async = context.async(2);
    Server server = Glue.instance().<Server>resolve("/in/erail/server/Server");
    server
            .getVertx()
            .createHttpClient()
            .get(server.getHttpServerOptions().getPort(), server.getHttpServerOptions().getHost(), "/v1/hello")
            .handler(response -> {
              context.assertEquals(response.statusCode(), 200, response.statusMessage());
              response.bodyHandler((event) -> {
                JsonArray data = event.toJsonArray();
                context.assertEquals(5, data.size());
                async.countDown();
              });
              async.countDown();
            })
            .end();
  }

}
