package in.erail.amazon.lambda;

import in.erail.model.ResponseEvent;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.junit.Timeout;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.Rule;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

/**
 *
 * @author vinay
 */
@RunWith(VertxUnitRunner.class)
public class AWSLambdaTest {

  @Rule
  public Timeout rule = Timeout.seconds(2000);

  public static String EVENT_MSG = "{\n"
          + "    \"resource\": \"/{proxy+}\",\n"
          + "    \"path\": \"/v1/users\",\n"
          + "    \"httpMethod\": \"POST\",\n"
          + "    \"headers\": {\n"
          + "        \"Accept\": \"*/*\",\n"
          + "        \"accept-encoding\": \"gzip, deflate\",\n"
          + "        \"cache-control\": \"no-cache\",\n"
          + "        \"Content-Type\": \"application/json\",\n"
          + "        \"Host\": \"jtsxwpy65j.execute-api.us-east-2.amazonaws.com\",\n"
          + "        \"Postman-Token\": \"dbbdafc8-5cc7-451c-b767-9f19eeb19941\",\n"
          + "        \"User-Agent\": \"PostmanRuntime/7.4.0\",\n"
          + "        \"X-Amzn-Trace-Id\": \"Root=1-5c2e854c-db47d2066bcaa15629df52d5\",\n"
          + "        \"X-Forwarded-For\": \"49.36.130.148\",\n"
          + "        \"X-Forwarded-Port\": \"443\",\n"
          + "        \"X-Forwarded-Proto\": \"https\"\n"
          + "    },\n"
          + "    \"multiValueHeaders\": {\n"
          + "        \"Accept\": [\n"
          + "            \"*/*\"\n"
          + "        ],\n"
          + "        \"accept-encoding\": [\n"
          + "            \"gzip, deflate\"\n"
          + "        ],\n"
          + "        \"cache-control\": [\n"
          + "            \"no-cache\"\n"
          + "        ],\n"
          + "        \"Content-Type\": [\n"
          + "            \"application/json\"\n"
          + "        ],\n"
          + "        \"Host\": [\n"
          + "            \"jtsxwpy65j.execute-api.us-east-2.amazonaws.com\"\n"
          + "        ],\n"
          + "        \"Postman-Token\": [\n"
          + "            \"dbbdafc8-5cc7-451c-b767-9f19eeb19941\"\n"
          + "        ],\n"
          + "        \"User-Agent\": [\n"
          + "            \"PostmanRuntime/7.4.0\"\n"
          + "        ],\n"
          + "        \"X-Amzn-Trace-Id\": [\n"
          + "            \"Root=1-5c2e854c-db47d2066bcaa15629df52d5\"\n"
          + "        ],\n"
          + "        \"X-Forwarded-For\": [\n"
          + "            \"49.36.130.148\"\n"
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
          + "    \"pathParameters\": {\n"
          + "        \"proxy\": \"v1/users\"\n"
          + "    },\n"
          + "    \"stageVariables\": {\n"
          + "        \"lambdaAlias\": \"test\"\n"
          + "    },\n"
          + "    \"requestContext\": {\n"
          + "        \"resourceId\": \"tjdogf\",\n"
          + "        \"resourcePath\": \"/{proxy+}\",\n"
          + "        \"httpMethod\": \"POST\",\n"
          + "        \"extendedRequestId\": \"S8nEBFgziYcFXrg=\",\n"
          + "        \"requestTime\": \"03/Jan/2019:21:57:32 +0000\",\n"
          + "        \"path\": \"/test/v1/users\",\n"
          + "        \"accountId\": \"904774318525\",\n"
          + "        \"protocol\": \"HTTP/1.1\",\n"
          + "        \"stage\": \"test\",\n"
          + "        \"domainPrefix\": \"jtsxwpy65j\",\n"
          + "        \"requestTimeEpoch\": 1546552652926,\n"
          + "        \"requestId\": \"928bba1b-0fa2-11e9-ad27-8354b9dd01f4\",\n"
          + "        \"identity\": {\n"
          + "            \"cognitoIdentityPoolId\": null,\n"
          + "            \"accountId\": null,\n"
          + "            \"cognitoIdentityId\": null,\n"
          + "            \"caller\": null,\n"
          + "            \"sourceIp\": \"49.36.130.148\",\n"
          + "            \"accessKey\": null,\n"
          + "            \"cognitoAuthenticationType\": null,\n"
          + "            \"cognitoAuthenticationProvider\": null,\n"
          + "            \"userArn\": null,\n"
          + "            \"userAgent\": \"PostmanRuntime/7.4.0\",\n"
          + "            \"user\": null\n"
          + "        },\n"
          + "        \"domainName\": \"jtsxwpy65j.execute-api.us-east-2.amazonaws.com\",\n"
          + "        \"apiId\": \"jtsxwpy65j\"\n"
          + "    },\n"
          + "    \"body\": \"ewogICAgInR5cGUiOiAiQURNSU4iLAogICAgImZpcnN0TmFtZSI6ICJGaXJzdCBOYW1lIiwKICAgICJtaWRkbGVOYW1lIjogIk1pZGRsZSBOYW1lIiwKICAgICJsYXN0TmFtZSI6ICJMYXN0IE5hbWUiLAogICAgImFkZHJlc3MiOiAiVUI5OTIyIiwKICAgICJkb2IiOiAiMTk5Ni0xMC0xNSIsCiAgICAicGhvbmVMYW5kbGluZSI6ICI5ODc2NTU0NjYiLAogICAgInBob25lTW9iaWxlIjogIjQ0NDQ0NDQ0MzQzIiwKICAgICJwZXJtaXNzaW9ucyI6IFsKICAgICAgICAiYWxsIgogICAgXSwKICAgICJ1c2VybmFtZSI6ICJ1c2VyIG5hbWUiLAogICAgInBhc3N3b3JkIjogInVzZXIgcGFzc3dvcmQiCn0=\",\n"
          + "    \"isBase64Encoded\": true\n"
          + "}";

  @Test
  public void testProcess() {
    System.setProperty("service", "/in/erail/service/HelloService");
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
    assertEquals(System.getProperty("stage"), "test");
  }

}
