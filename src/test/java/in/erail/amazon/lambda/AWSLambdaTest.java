package in.erail.amazon.lambda;

import in.erail.model.ResponseEvent;
import io.reactivex.schedulers.Schedulers;
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

  public static String EVENT_MSG_2 = "{\n"
          + "  \"Records\": [\n"
          + "    {\n"
          + "      \"eventVersion\": \"2.0\",\n"
          + "      \"eventSource\": \"aws:s3\",\n"
          + "      \"awsRegion\": \"eu-west-1\",\n"
          + "      \"eventTime\": \"1970-01-01T00:00:00.000Z\",\n"
          + "      \"eventName\": \"ObjectCreated:Put\",\n"
          + "      \"userIdentity\": {\n"
          + "        \"principalId\": \"EXAMPLE\"\n"
          + "      },\n"
          + "      \"requestParameters\": {\n"
          + "        \"sourceIPAddress\": \"127.0.0.1\"\n"
          + "      },\n"
          + "      \"responseElements\": {\n"
          + "        \"x-amz-request-id\": \"EXAMPLE123456789\",\n"
          + "        \"x-amz-id-2\": \"EXAMPLE123/5678abcdefghijklambdaisawesome/mnopqrstuvwxyzABCDEFGH\"\n"
          + "      },\n"
          + "      \"s3\": {\n"
          + "        \"s3SchemaVersion\": \"1.0\",\n"
          + "        \"configurationId\": \"testConfigRule\",\n"
          + "        \"bucket\": {\n"
          + "          \"name\": \"example-bucket\",\n"
          + "          \"ownerIdentity\": {\n"
          + "            \"principalId\": \"EXAMPLE\"\n"
          + "          },\n"
          + "          \"arn\": \"arn:aws:s3:::example-bucket\"\n"
          + "        },\n"
          + "        \"object\": {\n"
          + "          \"key\": \"test/key\",\n"
          + "          \"size\": 1024,\n"
          + "          \"eTag\": \"0123456789abcdef0123456789abcdef\",\n"
          + "          \"sequencer\": \"0A1B2C3D4E5F678901\"\n"
          + "        }\n"
          + "      }\n"
          + "    }\n"
          + "  ]\n"
          + "}";

  public static String EVENT_MSG_3 = "{\n"
          + "  \"records\": [\n"
          + "    {\n"
          + "      \"recordId\": \"49578734086442259037497492980620233840400173390482112514000000\",\n"
          + "      \"data\": \"H4sIAAAAAAAAADWO0QqCMBiFX2XsWiJFi7wLUW8sIYUuQmLpnxvpJttMQnz3Ztrlxzmc8424BaVIDfmnA+zjID3nlzS5n8IsO8YhtrAYOMg5aURfDUSXNBG1MkEj6liKvjPZQpmWQNoFVf9QpWSdZoJHrNEgFfZvxa8XvoHrGUfMqqWumdHQpDVjtmdvHc91dwdn71p/vVngmqBVD616PgoolC/Ga0SBNJoi8USVWWKczM8oYhKoULDBUzF9Aeua5yHsAAAA\",\n"
          + "      \"approximateArrivalTimestamp\": 1510254469499\n"
          + "    },\n"
          + "    {\n"
          + "      \"recordId\": \"49578734086442259037497492980621442766219788363254202370000000\",\n"
          + "      \"data\": \"H4sIAAAAAAAAAJWRTWsbMRCG/8ueLZjRjL5yc9NNLnZDapemlFAkrTYstb3Lep0Qgv97x00KgTSHnAQzmkeP3nmqtmW/j3dl/TiU6qz6PF/Pfy3r1Wp+WVezqn/YlVHK2pK3Hr0Jxkt5099djv1hkE7uh0eVHzZqE7epiarb3fe/ixzDYVJoELRhssYQqsXLlEJ3jd8//biy4QYWz7jVNJa4/TDveQwV+qsada0v/HnthLg/pH0eu2Hq+t1Ft5nKuK/Ofn4EvnpDUAu7Xi6/LL9en3/z1e1f7fq+7KYT+qnqGrEnsi54AGS2wbHWxjCjoWAYGawmzawByIG3Dp0JzjOxsaI8dbKJKW4l1BcTdgg+zP5tSPCeQ/Bso/I+o+I2kUptjgrRlQyasslUHWdvZRwGJ4+HYJGCtiKgQTYKSJ4gODLgAkpFk3f0rkyA1zLGSsvoVsVCRTFakUkNqKxt1IyFc8T/y0gEmoHZo5a/W9HhU0TeWHMyIJaoQC6zDvC+DL6WSW3MqZSkiolJcWoalWybJSNIJTXcRgjV8fb4BwwLrNzwAgAA\",\n"
          + "      \"approximateArrivalTimestamp\": 1510254473773\n"
          + "    },\n"
          + "    {\n"
          + "      \"recordId\": \"49578734086442259037497492980622651692039402992428908546000000\",\n"
          + "      \"data\": \"H4sIAAAAAAAAAJWRbWsbMQyA/8t9jkGSJdnut2zLCiXZyJKyZaMM352vHEty4e7SUkr++9yXwUbXD8Vgg2w9eizdF7s0DPE6re8OqTgrPkzX05+L2Wo1PZ8Vk6K73ac+h0mtV49egvgc3nbX5313POSbqjvcmep2a7ZxV9bRtPub7lfKx+E4GhQEErYqYtHMn7MMuiV+fbf5rOEbzJ9wq7FPcfdm3lOaNReXyws/3cw2fvk9A4djOVR9exjbbv+x3Y6pH4qzH29hr14QzFzXi8WnxZfl+0tfXD1az27SfnxA3xdtneWtVRc8ADJrcEwkwoxigzAyiBNxzkJuIxGrei+g3gbgrDy2eRBj3OWePpuwQ/Bh8mdAGR+J69pJMFXKihwTGJ+aYJArpkjYQB2K0+SljMPgyFIIijaQgs2BAMEyexbns1NeoqpsCV+VCfCPTOVLLgUMU4h5S5UpE4BRm6ROqCEF/r8MExBDro3ED0XBMigFVM0iQlkRvZLml9a/LoN/yzSYKoIKTOmVTf6VNTHZxkjTIElkqlCL09XpN5PgkxrvAgAA\",\n"
          + "      \"approximateArrivalTimestamp\": 1510254474027\n"
          + "    },\n"
          + "    {\n"
          + "      \"recordId\": \"49578734086442259037497492980623860617859017621603614722000000\",\n"
          + "      \"data\": \"H4sIAAAAAAAAAJWRW28aQQyF/8s+M9J47LHHeaMtzUOhEQXSVlVUDctstCqwCJZEUcR/r3OpFCnNQ17mcjw+8+n4vtqUwyFfl/ndrlRn1afhfPh7MprNhuejalB1t9uyNzkwJk6QosZk8rq7Pt93x51V6m535+rbtVvnzXKVXbu96f4U23bH3kEEHyIhx4jgxs9dDmQK3z/8vGD94cdPdrN+X/Lm3X5PbcHp5QLkYrqYLC6/mOHhuDzU+3bXt932c7vuy/5Qnf16j/fslYMb83wy+Tr5Nv24SNXVI/Xopmz7B+v7ql0ZPCKLJu+BiFUohBiJIKJGAvIkSTgpsU8aVBNangymsCH3rQ2izxvL9JmEBOzh4N+AzL6gX3JD7CLn4kg8OiVduahNkIa0BtbqNHgNI6AS0P5kQA3sUcA4IDCElCBKwgdgiCoI+CaM+pcwbAVfN8F5r2owGV0OdpWkS8kp52a1/D8MBR8sDUoQKDIbDnqlhAgQLTMWz8YbRQT92zDwEkbIQ10YHUZbKGfvUmrAIWodih2btKpOV6e/zXGIX+8CAAA=\",\n"
          + "      \"approximateArrivalTimestamp\": 1510254474388\n"
          + "    }\n"
          + "  ],\n"
          + "  \"region\": \"eu-west-1\",\n"
          + "  \"deliveryStreamArn\": \"arn:aws:firehose:eu-west-1:123456789012:deliverystream/copy-cwl-lambda-invoke-input-151025436553-Firehose-8KILJ01Q5OBN\",\n"
          + "  \"invocationId\": \"a7234216-12b6-4bc0-96d7-82606c0e80cf\"\n"
          + "}";

  public static String EVENT_MSG_4 = "{\n"
          + "  \"RequestType\": \"Create\",\n"
          + "  \"ResponseURL\": \"http://pre-signed-S3-url-for-response\",\n"
          + "  \"StackId\": \"arn:aws:cloudformation:eu-west-1:123456789012:stack/MyStack/guid\",\n"
          + "  \"RequestId\": \"unique id for this create request\",\n"
          + "  \"ResourceType\": \"Custom::TestResource\",\n"
          + "  \"LogicalResourceId\": \"MyTestResource\",\n"
          + "  \"ResourceProperties\": {\n"
          + "    \"StackName\": \"MyStack\",\n"
          + "    \"List\": [\n"
          + "      \"1\",\n"
          + "      \"2\",\n"
          + "      \"3\"\n"
          + "    ]\n"
          + "  }\n"
          + "}";

  @Test
  public void testProcess() {
    System.setProperty("service", "/in/erail/service/HelloService");
    String result = new AWSLambda().handleMessage(new JsonObject(EVENT_MSG_1)).blockingGet();
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

  @Test
  public void testProcess2() {
    System.setProperty("service", "/in/erail/service/HelloService");
    new AWSLambda().handleMessage(new JsonObject(EVENT_MSG_2)).subscribeOn(Schedulers.computation()).blockingGet();
    new AWSLambda().handleMessage(new JsonObject(EVENT_MSG_3)).subscribeOn(Schedulers.computation()).blockingGet();
    new AWSLambda().handleMessage(new JsonObject(EVENT_MSG_4)).subscribeOn(Schedulers.computation()).blockingGet();
  }
}
