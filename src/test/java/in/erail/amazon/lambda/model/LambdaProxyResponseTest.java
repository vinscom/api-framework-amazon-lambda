package in.erail.amazon.lambda.model;

import io.vertx.core.json.JsonObject;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author vinay
 */
public class LambdaProxyResponseTest {

  @Test
  public void testMapping() {
    JsonObject result = new JsonObject("{\n"
            + "  \"body\" : \"Testing\",\n"
            + "  \"isBase64Encoded\" : true,\n"
            + "  \"statusCode\" : 200,\n"
            + "  \"headers\" : {\n"
            + "    \"a\" : \"b\"\n"
            + "  }\n"
            + "}");

    LambdaProxyResponse response = new LambdaProxyResponse();

    response.setBody("Testing");
    response.getHeaders().put("a", "b");

    JsonObject json = JsonObject.mapFrom(response);
    Assert.assertEquals(result, json);
  }

}
