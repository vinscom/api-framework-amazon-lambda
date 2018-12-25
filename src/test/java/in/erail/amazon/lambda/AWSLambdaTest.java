package in.erail.amazon.lambda;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.junit.Timeout;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.Rule;
import org.junit.runner.RunWith;

/**
 *
 * @author vinay
 */
@RunWith(VertxUnitRunner.class)
public class AWSLambdaTest {

  @Rule
  public Timeout rule = Timeout.seconds(2000);

  public static String EVENT_MSG = "{\n"
          + "  \"body\": \"{\\\"test\\\":\\\"body\\\"}\",\n"
          + "  \"resource\": \"/{proxy+}\",\n"
          + "  \"requestContext\": {\n"
          + "    \"resourceId\": \"123456\",\n"
          + "    \"apiId\": \"1234567890\",\n"
          + "    \"resourcePath\": \"/{proxy+}\",\n"
          + "    \"httpMethod\": \"GET\",\n"
          + "    \"requestId\": \"c6af9ac6-7b61-11e6-9a41-93e8deadbeef\",\n"
          + "    \"accountId\": \"123456789012\",\n"
          + "    \"identity\": {\n"
          + "      \"apiKey\": null,\n"
          + "      \"userArn\": null,\n"
          + "      \"cognitoAuthenticationType\": null,\n"
          + "      \"caller\": null,\n"
          + "      \"userAgent\": \"Custom User Agent String\",\n"
          + "      \"user\": null,\n"
          + "      \"cognitoIdentityPoolId\": null,\n"
          + "      \"cognitoIdentityId\": null,\n"
          + "      \"cognitoAuthenticationProvider\": null,\n"
          + "      \"sourceIp\": \"127.0.0.1\",\n"
          + "      \"accountId\": null\n"
          + "    },\n"
          + "    \"stage\": \"prod\"\n"
          + "  },\n"
          + "  \"queryStringParameters\": {\n"
          + "    \"foo\": \"bar\"\n"
          + "  },\n"
          + "  \"headers\": {\n"
          + "    \"Via\": \"1.1 08f323deadbeefa7af34d5feb414ce27.cloudfront.net (CloudFront)\",\n"
          + "    \"Accept-Language\": \"en-US,en;q=0.8\",\n"
          + "    \"CloudFront-Is-Desktop-Viewer\": \"true\",\n"
          + "    \"CloudFront-Is-SmartTV-Viewer\": \"false\",\n"
          + "    \"CloudFront-Is-Mobile-Viewer\": \"false\",\n"
          + "    \"X-Forwarded-For\": \"127.0.0.1, 127.0.0.2\",\n"
          + "    \"CloudFront-Viewer-Country\": \"US\",\n"
          + "    \"Accept\": \"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\",\n"
          + "    \"Upgrade-Insecure-Requests\": \"1\",\n"
          + "    \"X-Forwarded-Port\": \"443\",\n"
          + "    \"Host\": \"1234567890.execute-api.us-east-1.amazonaws.com\",\n"
          + "    \"X-Forwarded-Proto\": \"https\",\n"
          + "    \"X-Amz-Cf-Id\": \"cDehVQoZnx43VYQb9j2-nvCh-9z396Uhbp027Y2JvkCPNLmGJHqlaA==\",\n"
          + "    \"CloudFront-Is-Tablet-Viewer\": \"false\",\n"
          + "    \"Cache-Control\": \"max-age=0\",\n"
          + "    \"User-Agent\": \"Custom User Agent String\",\n"
          + "    \"CloudFront-Forwarded-Proto\": \"https\",\n"
          + "    \"Accept-Encoding\": \"gzip, deflate, sdch\"\n"
          + "  },\n"
          + "  \"pathParameters\": {\n"
          + "    \"proxy\": \"path/to/resource\"\n"
          + "  },\n"
          + "  \"httpMethod\": \"POST\",\n"
          + "  \"stageVariables\": {\n"
          + "    \"baz\": \"qux\"\n"
          + "  },\n"
          + "  \"path\": \"/path/to/resource\"\n"
          + "}";

  @Test
  public void testProcess() {
    System.setProperty("service", "/in/erail/service/SessionGetService");
    JsonObject result = new AWSLambda().handleMessage(new JsonObject(EVENT_MSG));
    System.out.println("--->" + result.toString());
  }

}
