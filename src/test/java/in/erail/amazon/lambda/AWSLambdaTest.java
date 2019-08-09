package in.erail.amazon.lambda;

import in.erail.model.ResponseEvent;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author vinay
 */
@ExtendWith(VertxExtension.class)
public class AWSLambdaTest {

  public static String EVENT_MSG_1 = "{\n"
          + "  \"body\": \"eyJ0ZXN0IjoiYm9keSJ9\",\n"
          + "  \"resource\": \"/{proxy+}\",\n"
          + "  \"path\": \"/path/to/resource\",\n"
          + "  \"httpMethod\": \"POST\",\n"
          + "  \"isBase64Encoded\": true,\n"
          + "  \"queryStringParameters\": {\n"
          + "    \"foo\": \"bar\"\n"
          + "  },\n"
          + "  \"pathParameters\": {\n"
          + "    \"proxy\": \"/path/to/resource\"\n"
          + "  },\n"
          + "  \"stageVariables\": {\n"
          + "    \"baz\": \"qux\"\n"
          + "  },\n"
          + "  \"headers\": {\n"
          + "    \"Accept\": \"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\",\n"
          + "    \"Accept-Encoding\": \"gzip, deflate, sdch\",\n"
          + "    \"Accept-Language\": \"en-US,en;q=0.8\",\n"
          + "    \"Cache-Control\": \"max-age=0\",\n"
          + "    \"CloudFront-Forwarded-Proto\": \"https\",\n"
          + "    \"CloudFront-Is-Desktop-Viewer\": \"true\",\n"
          + "    \"CloudFront-Is-Mobile-Viewer\": \"false\",\n"
          + "    \"CloudFront-Is-SmartTV-Viewer\": \"false\",\n"
          + "    \"CloudFront-Is-Tablet-Viewer\": \"false\",\n"
          + "    \"CloudFront-Viewer-Country\": \"US\",\n"
          + "    \"Host\": \"1234567890.execute-api.eu-west-2.amazonaws.com\",\n"
          + "    \"Upgrade-Insecure-Requests\": \"1\",\n"
          + "    \"User-Agent\": \"Custom User Agent String\",\n"
          + "    \"Via\": \"1.1 08f323deadbeefa7af34d5feb414ce27.cloudfront.net (CloudFront)\",\n"
          + "    \"X-Amz-Cf-Id\": \"cDehVQoZnx43VYQb9j2-nvCh-9z396Uhbp027Y2JvkCPNLmGJHqlaA==\",\n"
          + "    \"X-Forwarded-For\": \"127.0.0.1, 127.0.0.2\",\n"
          + "    \"X-Forwarded-Port\": \"443\",\n"
          + "    \"X-Forwarded-Proto\": \"https\"\n"
          + "  },\n"
          + "  \"requestContext\": {\n"
          + "    \"accountId\": \"123456789012\",\n"
          + "    \"resourceId\": \"123456\",\n"
          + "    \"stage\": \"prod\",\n"
          + "    \"requestId\": \"c6af9ac6-7b61-11e6-9a41-93e8deadbeef\",\n"
          + "    \"requestTime\": \"09/Apr/2015:12:34:56 +0000\",\n"
          + "    \"requestTimeEpoch\": 1428582896000,\n"
          + "    \"identity\": {\n"
          + "      \"cognitoIdentityPoolId\": null,\n"
          + "      \"accountId\": null,\n"
          + "      \"cognitoIdentityId\": null,\n"
          + "      \"caller\": null,\n"
          + "      \"accessKey\": null,\n"
          + "      \"sourceIp\": \"127.0.0.1\",\n"
          + "      \"cognitoAuthenticationType\": null,\n"
          + "      \"cognitoAuthenticationProvider\": null,\n"
          + "      \"userArn\": null,\n"
          + "      \"userAgent\": \"Custom User Agent String\",\n"
          + "      \"user\": null\n"
          + "    },\n"
          + "    \"path\": \"/prod/path/to/resource\",\n"
          + "    \"resourcePath\": \"/{proxy+}\",\n"
          + "    \"httpMethod\": \"POST\",\n"
          + "    \"apiId\": \"1234567890\",\n"
          + "    \"protocol\": \"HTTP/1.1\"\n"
          + "  }\n"
          + "}";

  public static String EVENT_MSG_2 = "{\n"
          + "  \"body\": \"\",\n"
          + "  \"resource\": \"/{proxy+}\",\n"
          + "  \"path\": \"/path/to/resource\",\n"
          + "  \"httpMethod\": \"POST\",\n"
          + "  \"isBase64Encoded\": true,\n"
          + "  \"queryStringParameters\": {\n"
          + "    \"foo\": \"bar\"\n"
          + "  },\n"
          + "  \"pathParameters\": {\n"
          + "    \"proxy\": \"/path/to/resource\"\n"
          + "  },\n"
          + "  \"stageVariables\": {\n"
          + "    \"baz\": \"qux\"\n"
          + "  },\n"
          + "  \"headers\": {\n"
          + "    \"Accept\": \"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\",\n"
          + "    \"Accept-Encoding\": \"gzip, deflate, sdch\",\n"
          + "    \"Accept-Language\": \"en-US,en;q=0.8\",\n"
          + "    \"Cache-Control\": \"max-age=0\",\n"
          + "    \"CloudFront-Forwarded-Proto\": \"https\",\n"
          + "    \"CloudFront-Is-Desktop-Viewer\": \"true\",\n"
          + "    \"CloudFront-Is-Mobile-Viewer\": \"false\",\n"
          + "    \"CloudFront-Is-SmartTV-Viewer\": \"false\",\n"
          + "    \"CloudFront-Is-Tablet-Viewer\": \"false\",\n"
          + "    \"CloudFront-Viewer-Country\": \"US\",\n"
          + "    \"Host\": \"1234567890.execute-api.eu-west-2.amazonaws.com\",\n"
          + "    \"Upgrade-Insecure-Requests\": \"1\",\n"
          + "    \"User-Agent\": \"Custom User Agent String\",\n"
          + "    \"Via\": \"1.1 08f323deadbeefa7af34d5feb414ce27.cloudfront.net (CloudFront)\",\n"
          + "    \"X-Amz-Cf-Id\": \"cDehVQoZnx43VYQb9j2-nvCh-9z396Uhbp027Y2JvkCPNLmGJHqlaA==\",\n"
          + "    \"X-Forwarded-For\": \"127.0.0.1, 127.0.0.2\",\n"
          + "    \"X-Forwarded-Port\": \"443\",\n"
          + "    \"X-Forwarded-Proto\": \"https\"\n"
          + "  },\n"
          + "  \"requestContext\": {\n"
          + "    \"accountId\": \"123456789012\",\n"
          + "    \"resourceId\": \"123456\",\n"
          + "    \"stage\": \"prod\",\n"
          + "    \"requestId\": \"c6af9ac6-7b61-11e6-9a41-93e8deadbeef\",\n"
          + "    \"requestTime\": \"09/Apr/2015:12:34:56 +0000\",\n"
          + "    \"requestTimeEpoch\": 1428582896000,\n"
          + "    \"identity\": {\n"
          + "      \"cognitoIdentityPoolId\": null,\n"
          + "      \"accountId\": null,\n"
          + "      \"cognitoIdentityId\": null,\n"
          + "      \"caller\": null,\n"
          + "      \"accessKey\": null,\n"
          + "      \"sourceIp\": \"127.0.0.1\",\n"
          + "      \"cognitoAuthenticationType\": null,\n"
          + "      \"cognitoAuthenticationProvider\": null,\n"
          + "      \"userArn\": null,\n"
          + "      \"userAgent\": \"Custom User Agent String\",\n"
          + "      \"user\": null\n"
          + "    },\n"
          + "    \"path\": \"/prod/path/to/resource\",\n"
          + "    \"resourcePath\": \"/{proxy+}\",\n"
          + "    \"httpMethod\": \"GET\",\n"
          + "    \"apiId\": \"1234567890\",\n"
          + "    \"protocol\": \"HTTP/1.1\"\n"
          + "  }\n"
          + "}";

  @Test
  public void testPost() {
    System.setProperty("service", "/in/erail/service/HelloServicePost");
    String result = new AWSLambda().handleMessage(new JsonObject(EVENT_MSG_1)).blockingGet();
    JsonObject jsonResp = new JsonObject(result);
    ResponseEvent resp = new JsonObject(result).mapTo(ResponseEvent.class);
    assertTrue(resp.isIsBase64Encoded());
    assertEquals("{\"length\":15,\"aws-event-source\":\"\"}", new String(resp.getBody()));
    assertTrue(jsonResp.containsKey("isBase64Encoded"));
    assertTrue(jsonResp.containsKey("statusCode"));
    assertTrue(jsonResp.containsKey("headers"));
    assertTrue(jsonResp.containsKey("multiValueHeaders"));
    assertTrue(jsonResp.containsKey("body"));
    assertEquals("prod", System.getProperty("stage"));
  }

  @Test
  public void testGet() {
    System.setProperty("service", "/in/erail/service/HelloServiceGet");
    String result = new AWSLambda().handleMessage(new JsonObject(EVENT_MSG_2)).blockingGet();
    JsonObject jsonResp = new JsonObject(result);
    ResponseEvent resp = new JsonObject(result).mapTo(ResponseEvent.class);
    assertTrue(resp.isIsBase64Encoded());
    assertEquals("[\"S1\",\"S2\",\"S3\",\"S4\",\"S5\"]", new String(resp.getBody()));
    assertTrue(jsonResp.containsKey("isBase64Encoded"));
    assertTrue(jsonResp.containsKey("statusCode"));
    assertTrue(jsonResp.containsKey("headers"));
    assertTrue(jsonResp.containsKey("multiValueHeaders"));
    assertTrue(jsonResp.containsKey("body"));
    assertEquals("prod", System.getProperty("stage"));
  }
}
