package in.erail.amazon.lambda.service;

import in.erail.amazon.lambda.AWSLambda;
import in.erail.glue.Glue;
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
public class ProxyServiceTest {

  public static String EVENT_MSG = "{\n"
          + "    \"resource\": \"/v1/hello\",\n"
          + "    \"path\": \"/v1/hello\",\n"
          + "    \"httpMethod\": \"GET\",\n"
          + "    \"headers\": {\n"
          + "        \"Accept\": \"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\",\n"
          + "        \"Accept-Encoding\": \"gzip, deflate, br\",\n"
          + "        \"Accept-Language\": \"en-GB,en-US;q=0.9,en;q=0.8\",\n"
          + "        \"cache-control\": \"max-age=0\",\n"
          + "        \"CloudFront-Forwarded-Proto\": \"https\",\n"
          + "        \"CloudFront-Is-Desktop-Viewer\": \"true\",\n"
          + "        \"CloudFront-Is-Mobile-Viewer\": \"false\",\n"
          + "        \"CloudFront-Is-SmartTV-Viewer\": \"false\",\n"
          + "        \"CloudFront-Is-Tablet-Viewer\": \"false\",\n"
          + "        \"CloudFront-Viewer-Country\": \"GB\",\n"
          + "        \"Host\": \"bhpdtkdfek.execute-api.us-east-2.amazonaws.com\",\n"
          + "        \"upgrade-insecure-requests\": \"1\",\n"
          + "        \"User-Agent\": \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36\",\n"
          + "        \"Via\": \"2.0 cd068397b3367ed727e4988c0cabf85a.cloudfront.net (CloudFront)\",\n"
          + "        \"X-Amz-Cf-Id\": \"6opg4d-x5Hptb8RAXkVBhgxlN-KZbU57CU2ew1AVns864MLepMpESA==\",\n"
          + "        \"X-Amzn-Trace-Id\": \"Root=1-5c22ae99-c8f508c5e9bdc966fbd01b03\",\n"
          + "        \"X-Forwarded-For\": \"92.234.149.241, 70.132.20.88\",\n"
          + "        \"X-Forwarded-Port\": \"443\",\n"
          + "        \"X-Forwarded-Proto\": \"https\"\n"
          + "    },\n"
          + "    \"multiValueHeaders\": {\n"
          + "        \"Accept\": [\n"
          + "            \"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\"\n"
          + "        ],\n"
          + "        \"Accept-Encoding\": [\n"
          + "            \"gzip, deflate, br\"\n"
          + "        ],\n"
          + "        \"Accept-Language\": [\n"
          + "            \"en-GB,en-US;q=0.9,en;q=0.8\"\n"
          + "        ],\n"
          + "        \"cache-control\": [\n"
          + "            \"max-age=0\"\n"
          + "        ],\n"
          + "        \"CloudFront-Forwarded-Proto\": [\n"
          + "            \"https\"\n"
          + "        ],\n"
          + "        \"CloudFront-Is-Desktop-Viewer\": [\n"
          + "            \"true\"\n"
          + "        ],\n"
          + "        \"CloudFront-Is-Mobile-Viewer\": [\n"
          + "            \"false\"\n"
          + "        ],\n"
          + "        \"CloudFront-Is-SmartTV-Viewer\": [\n"
          + "            \"false\"\n"
          + "        ],\n"
          + "        \"CloudFront-Is-Tablet-Viewer\": [\n"
          + "            \"false\"\n"
          + "        ],\n"
          + "        \"CloudFront-Viewer-Country\": [\n"
          + "            \"GB\"\n"
          + "        ],\n"
          + "        \"Host\": [\n"
          + "            \"bhpdtkdfek.execute-api.us-east-2.amazonaws.com\"\n"
          + "        ],\n"
          + "        \"upgrade-insecure-requests\": [\n"
          + "            \"1\"\n"
          + "        ],\n"
          + "        \"User-Agent\": [\n"
          + "            \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36\"\n"
          + "        ],\n"
          + "        \"Via\": [\n"
          + "            \"2.0 cd068397b3367ed727e4988c0cabf85a.cloudfront.net (CloudFront)\"\n"
          + "        ],\n"
          + "        \"X-Amz-Cf-Id\": [\n"
          + "            \"6opg4d-x5Hptb8RAXkVBhgxlN-KZbU57CU2ew1AVns864MLepMpESA==\"\n"
          + "        ],\n"
          + "        \"X-Amzn-Trace-Id\": [\n"
          + "            \"Root=1-5c22ae99-c8f508c5e9bdc966fbd01b03\"\n"
          + "        ],\n"
          + "        \"X-Forwarded-For\": [\n"
          + "            \"92.234.149.241, 70.132.20.88\"\n"
          + "        ],\n"
          + "        \"X-Forwarded-Port\": [\n"
          + "            \"443\"\n"
          + "        ],\n"
          + "        \"X-Forwarded-Proto\": [\n"
          + "            \"https\"\n"
          + "        ]\n"
          + "    },\n"
          + "    \"queryStringParameters\": null,\n"
          + "    \"multiValueQueryStringParameters\": null,\n"
          + "    \"pathParameters\": null,\n"
          + "    \"stageVariables\": null,\n"
          + "    \"requestContext\": {\n"
          + "        \"resourceId\": \"ikgn3l\",\n"
          + "        \"resourcePath\": \"/hello\",\n"
          + "        \"httpMethod\": \"GET\",\n"
          + "        \"extendedRequestId\": \"SfA3_EY2CYcFowA=\",\n"
          + "        \"requestTime\": \"25/Dec/2018:22:26:33 +0000\",\n"
          + "        \"path\": \"/tutorial/hello\",\n"
          + "        \"accountId\": \"762985365920\",\n"
          + "        \"protocol\": \"HTTP/1.1\",\n"
          + "        \"stage\": \"tutorial\",\n"
          + "        \"domainPrefix\": \"bhpdtkdfek\",\n"
          + "        \"requestTimeEpoch\": 1545776793579,\n"
          + "        \"requestId\": \"22568c6e-0894-11e9-90cf-f74da74d2315\",\n"
          + "        \"identity\": {\n"
          + "            \"cognitoIdentityPoolId\": null,\n"
          + "            \"accountId\": null,\n"
          + "            \"cognitoIdentityId\": null,\n"
          + "            \"caller\": null,\n"
          + "            \"sourceIp\": \"92.234.149.241\",\n"
          + "            \"accessKey\": null,\n"
          + "            \"cognitoAuthenticationType\": null,\n"
          + "            \"cognitoAuthenticationProvider\": null,\n"
          + "            \"userArn\": null,\n"
          + "            \"userAgent\": \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36\",\n"
          + "            \"user\": null\n"
          + "        },\n"
          + "        \"domainName\": \"bhpdtkdfek.execute-api.us-east-2.amazonaws.com\",\n"
          + "        \"apiId\": \"bhpdtkdfek\"\n"
          + "    },\n"
          + "    \"body\": null,\n"
          + "    \"isBase64Encoded\": false "
          + "}";

  @Test
  public void testProcess() {
    Glue.instance().resolve("/in/erail/server/Server");
    System.setProperty("service", "/in/erail/amazon/lambda/service/ProxyService");
    String result = new AWSLambda().handleMessage(new JsonObject(EVENT_MSG)).blockingGet();
    JsonObject jsonResp = new JsonObject(result);
    ResponseEvent resp = new JsonObject(result).mapTo(ResponseEvent.class);
    assertTrue(resp.isIsBase64Encoded());
    assertEquals(new String(resp.getBody()), "[\"S1\",\"S2\",\"S3\",\"S4\",\"S5\"]");
    assertTrue(jsonResp.containsKey("isBase64Encoded"));
    assertTrue(jsonResp.containsKey("statusCode"));
    assertTrue(jsonResp.containsKey("headers"));
    assertTrue(jsonResp.containsKey("multiValueHeaders"));
    assertTrue(jsonResp.containsKey("body"));
    assertEquals(jsonResp.fieldNames().size(), 5);
  }

}
