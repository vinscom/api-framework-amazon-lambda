package in.erail.service;

import in.erail.amazon.lambda.AWSLambda;
import in.erail.amazon.lambda.eventsource.*;
import in.erail.glue.Glue;
import in.erail.server.Server;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.reactivex.ext.web.client.WebClient;
import java.util.Base64;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(VertxExtension.class)
public class HelloServiceTest {

  public HelloServiceTest() {
    System.setProperty(SQSEventSource.ENV_NAME, "/v1/hello");
    System.setProperty(SNSEventSource.ENV_NAME, "/v1/hello");
    System.setProperty(SESEventSource.ENV_NAME, "/v1/hello");
    System.setProperty(S3EventSource.ENV_NAME, "/v1/hello");
    System.setProperty(KinesisEventSource.ENV_NAME, "/v1/hello");
    System.setProperty(DynamoDBEventSource.ENV_NAME, "/v1/hello");
    System.setProperty(CodeCommitEventSource.ENV_NAME, "/v1/hello");
    System.setProperty(AWSLambda.SERVICE_ENV, "/in/erail/amazon/lambda/service/ProxyService");
  }

  @Test
  public void testGetRequest(VertxTestContext testContext) {

    Server server = Glue.instance().<Server>resolve("/in/erail/server/Server");

    WebClient
            .create(server.getVertx())
            .get(server.getHttpServerOptions().getPort(), server.getHttpServerOptions().getHost(), "/v1/hello")
            .rxSend()
            .doOnSuccess(response -> assertEquals(response.statusCode(), 200, response.statusMessage()))
            .doOnSuccess(response -> {
              JsonArray data = response.bodyAsJsonArray();
              assertEquals(5, data.size());
            })
            .subscribe(t -> testContext.completeNow(), err -> testContext.failNow(err));
  }

  @Test
  public void testSQS(VertxTestContext testContext) {
    String msg = "{\n"
            + "  \"Records\": [\n"
            + "    {\n"
            + "      \"messageId\": \"19dd0b57-b21e-4ac1-bd88-01bbb068cb78\",\n"
            + "      \"receiptHandle\": \"MessageReceiptHandle\",\n"
            + "      \"body\": \"Hello from SQS!\",\n"
            + "      \"attributes\": {\n"
            + "        \"ApproximateReceiveCount\": \"1\",\n"
            + "        \"SentTimestamp\": \"1523232000000\",\n"
            + "        \"SenderId\": \"123456789012\",\n"
            + "        \"ApproximateFirstReceiveTimestamp\": \"1523232000001\"\n"
            + "      },\n"
            + "      \"messageAttributes\": {},\n"
            + "      \"md5OfBody\": \"7b270e59b47ff90a553787216d55d91d\",\n"
            + "      \"eventSource\": \"aws:sqs\",\n"
            + "      \"eventSourceARN\": \"arn:aws:sqs:eu-west-2:123456789012:MyQueue\",\n"
            + "      \"awsRegion\": \"eu-west-2\"\n"
            + "    }\n"
            + "  ]\n"
            + "}";

    JsonObject msgJson = new JsonObject(msg);
    JsonObject respJson = new JsonObject(new String(Base64.getDecoder().decode(new JsonObject(new AWSLambda().handleMessage(msgJson).blockingGet()).getString("body"))));
    assertEquals("SQS", respJson.getString("aws-event-source"));
    assertEquals(464, respJson.getLong("length"));

    testContext.completeNow();
  }

  @Test
  public void testSNS(VertxTestContext testContext) {
    String msg = "{\n"
            + "  \"Records\": [\n"
            + "    {\n"
            + "      \"EventSource\": \"aws:sns\",\n"
            + "      \"EventVersion\": \"1.0\",\n"
            + "      \"EventSubscriptionArn\": \"arn:aws:sns:eu-west-2:{{accountId}}:ExampleTopic\",\n"
            + "      \"Sns\": {\n"
            + "        \"Type\": \"Notification\",\n"
            + "        \"MessageId\": \"95df01b4-ee98-5cb9-9903-4c221d41eb5e\",\n"
            + "        \"TopicArn\": \"arn:aws:sns:eu-west-2:123456789012:ExampleTopic\",\n"
            + "        \"Subject\": \"example subject\",\n"
            + "        \"Message\": \"example message\",\n"
            + "        \"Timestamp\": \"1970-01-01T00:00:00.000Z\",\n"
            + "        \"SignatureVersion\": \"1\",\n"
            + "        \"Signature\": \"EXAMPLE\",\n"
            + "        \"SigningCertUrl\": \"EXAMPLE\",\n"
            + "        \"UnsubscribeUrl\": \"EXAMPLE\",\n"
            + "        \"MessageAttributes\": {\n"
            + "          \"Test\": {\n"
            + "            \"Type\": \"String\",\n"
            + "            \"Value\": \"TestString\"\n"
            + "          },\n"
            + "          \"TestBinary\": {\n"
            + "            \"Type\": \"Binary\",\n"
            + "            \"Value\": \"TestBinary\"\n"
            + "          }\n"
            + "        }\n"
            + "      }\n"
            + "    }\n"
            + "  ]\n"
            + "}";

    JsonObject msgJson = new JsonObject(msg);
    JsonObject respJson = new JsonObject(new String(Base64.getDecoder().decode(new JsonObject(new AWSLambda().handleMessage(msgJson).blockingGet()).getString("body"))));
    assertEquals("SNS", respJson.getString("aws-event-source"));
    assertEquals(590, respJson.getLong("length"));

    testContext.completeNow();
  }

  @Test
  public void testSES(VertxTestContext testContext) {
    String msg = "{\n"
            + "  \"Records\": [\n"
            + "    {\n"
            + "      \"eventSource\": \"aws:ses\",\n"
            + "      \"eventVersion\": \"1.0\",\n"
            + "      \"ses\": {\n"
            + "        \"mail\": {\n"
            + "          \"commonHeaders\": {\n"
            + "            \"date\": \"Wed, 7 Oct 2015 12:34:56 -0700\",\n"
            + "            \"from\": [\n"
            + "              \"Jane Doe <janedoe@example.com>\"\n"
            + "            ],\n"
            + "            \"messageId\": \"<0123456789example.com>\",\n"
            + "            \"returnPath\": \"janedoe@example.com\",\n"
            + "            \"subject\": \"Test Subject\",\n"
            + "            \"to\": [\n"
            + "              \"johndoe@example.com\"\n"
            + "            ]\n"
            + "          },\n"
            + "          \"destination\": [\n"
            + "            \"johndoe@example.com\"\n"
            + "          ],\n"
            + "          \"headers\": [\n"
            + "            {\n"
            + "              \"name\": \"Return-Path\",\n"
            + "              \"value\": \"<janedoe@example.com>\"\n"
            + "            },\n"
            + "            {\n"
            + "              \"name\": \"Received\",\n"
            + "              \"value\": \"from mailer.example.com (mailer.example.com [203.0.113.1]) by inbound-smtp.eu-west-2.amazonaws.com with SMTP id o3vrnil0e2ic28trm7dfhrc2v0cnbeccl4nbp0g1 for johndoe@example.com; Wed, 07 Oct 2015 12:34:56 +0000 (UTC)\"\n"
            + "            },\n"
            + "            {\n"
            + "              \"name\": \"DKIM-Signature\",\n"
            + "              \"value\": \"v=1; a=rsa-sha256; c=relaxed/relaxed; d=example.com; s=example; h=mime-version:from:date:message-id:subject:to:content-type; bh=jX3F0bCAI7sIbkHyy3mLYO28ieDQz2R0P8HwQkklFj4=; b=sQwJ+LMe9RjkesGu+vqU56asvMhrLRRYrWCbVt6WJulueecwfEwRf9JVWgkBTKiL6m2hr70xDbPWDhtLdLO+jB3hzjVnXwK3pYIOHw3vxG6NtJ6o61XSUwjEsp9tdyxQjZf2HNYee873832l3K1EeSXKzxYk9Pwqcpi3dMC74ct9GukjIevf1H46hm1L2d9VYTL0LGZGHOAyMnHmEGB8ZExWbI+k6khpurTQQ4sp4PZPRlgHtnj3Zzv7nmpTo7dtPG5z5S9J+L+Ba7dixT0jn3HuhaJ9b+VThboo4YfsX9PMNhWWxGjVksSFOcGluPO7QutCPyoY4gbxtwkN9W69HA==\"\n"
            + "            },\n"
            + "            {\n"
            + "              \"name\": \"MIME-Version\",\n"
            + "              \"value\": \"1.0\"\n"
            + "            },\n"
            + "            {\n"
            + "              \"name\": \"From\",\n"
            + "              \"value\": \"Jane Doe <janedoe@example.com>\"\n"
            + "            },\n"
            + "            {\n"
            + "              \"name\": \"Date\",\n"
            + "              \"value\": \"Wed, 7 Oct 2015 12:34:56 -0700\"\n"
            + "            },\n"
            + "            {\n"
            + "              \"name\": \"Message-ID\",\n"
            + "              \"value\": \"<0123456789example.com>\"\n"
            + "            },\n"
            + "            {\n"
            + "              \"name\": \"Subject\",\n"
            + "              \"value\": \"Test Subject\"\n"
            + "            },\n"
            + "            {\n"
            + "              \"name\": \"To\",\n"
            + "              \"value\": \"johndoe@example.com\"\n"
            + "            },\n"
            + "            {\n"
            + "              \"name\": \"Content-Type\",\n"
            + "              \"value\": \"text/plain; charset=UTF-8\"\n"
            + "            }\n"
            + "          ],\n"
            + "          \"headersTruncated\": false,\n"
            + "          \"messageId\": \"o3vrnil0e2ic28trm7dfhrc2v0clambda4nbp0g1\",\n"
            + "          \"source\": \"janedoe@example.com\",\n"
            + "          \"timestamp\": \"1970-01-01T00:00:00.000Z\"\n"
            + "        },\n"
            + "        \"receipt\": {\n"
            + "          \"action\": {\n"
            + "            \"functionArn\": \"arn:aws:lambda:eu-west-2:123456789012:function:Example\",\n"
            + "            \"invocationType\": \"Event\",\n"
            + "            \"type\": \"Lambda\"\n"
            + "          },\n"
            + "          \"dkimVerdict\": {\n"
            + "            \"status\": \"PASS\"\n"
            + "          },\n"
            + "          \"processingTimeMillis\": 574,\n"
            + "          \"recipients\": [\n"
            + "            \"johndoe@example.com\"\n"
            + "          ],\n"
            + "          \"spamVerdict\": {\n"
            + "            \"status\": \"PASS\"\n"
            + "          },\n"
            + "          \"spfVerdict\": {\n"
            + "            \"status\": \"PASS\"\n"
            + "          },\n"
            + "          \"timestamp\": \"1970-01-01T00:00:00.000Z\",\n"
            + "          \"virusVerdict\": {\n"
            + "            \"status\": \"PASS\"\n"
            + "          }\n"
            + "        }\n"
            + "      }\n"
            + "    }\n"
            + "  ]\n"
            + "}";

    JsonObject msgJson = new JsonObject(msg);
    JsonObject respJson = new JsonObject(new String(Base64.getDecoder().decode(new JsonObject(new AWSLambda().handleMessage(msgJson).blockingGet()).getString("body"))));
    assertEquals("SES", respJson.getString("aws-event-source"));
    assertEquals(2082, respJson.getLong("length"));

    testContext.completeNow();
  }

  @Test
  public void testS3(VertxTestContext testContext) {
    String msg = "{\n"
            + "  \"Records\": [\n"
            + "    {\n"
            + "      \"eventVersion\": \"2.0\",\n"
            + "      \"eventSource\": \"aws:s3\",\n"
            + "      \"awsRegion\": \"eu-west-2\",\n"
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

    JsonObject msgJson = new JsonObject(msg);
    JsonObject respJson = new JsonObject(new String(Base64.getDecoder().decode(new JsonObject(new AWSLambda().handleMessage(msgJson).blockingGet()).getString("body"))));
    assertEquals("S3", respJson.getString("aws-event-source"));
    assertEquals(680, respJson.getLong("length"));

    testContext.completeNow();
  }

  @Test
  public void testKinesis(VertxTestContext testContext) {
    String msg = "{\n"
            + "  \"Records\": [\n"
            + "    {\n"
            + "      \"kinesis\": {\n"
            + "        \"partitionKey\": \"partitionKey-03\",\n"
            + "        \"kinesisSchemaVersion\": \"1.0\",\n"
            + "        \"data\": \"SGVsbG8sIHRoaXMgaXMgYSB0ZXN0IDEyMy4=\",\n"
            + "        \"sequenceNumber\": \"49545115243490985018280067714973144582180062593244200961\",\n"
            + "        \"approximateArrivalTimestamp\": 1428537600\n"
            + "      },\n"
            + "      \"eventSource\": \"aws:kinesis\",\n"
            + "      \"eventID\": \"shardId-000000000000:49545115243490985018280067714973144582180062593244200961\",\n"
            + "      \"invokeIdentityArn\": \"arn:aws:iam::EXAMPLE\",\n"
            + "      \"eventVersion\": \"1.0\",\n"
            + "      \"eventName\": \"aws:kinesis:record\",\n"
            + "      \"eventSourceARN\": \"arn:aws:kinesis:EXAMPLE\",\n"
            + "      \"awsRegion\": \"eu-west-2\"\n"
            + "    }\n"
            + "  ]\n"
            + "}";

    JsonObject msgJson = new JsonObject(msg);
    JsonObject respJson = new JsonObject(new String(Base64.getDecoder().decode(new JsonObject(new AWSLambda().handleMessage(msgJson).blockingGet()).getString("body"))));
    assertEquals("KINESIS", respJson.getString("aws-event-source"));
    assertEquals(534, respJson.getLong("length"));

    testContext.completeNow();
  }

  @Test
  public void testDynamoDB(VertxTestContext testContext) {
    String msg = "{\n"
            + "  \"Records\": [\n"
            + "    {\n"
            + "      \"eventID\": \"1\",\n"
            + "      \"eventVersion\": \"1.0\",\n"
            + "      \"dynamodb\": {\n"
            + "        \"Keys\": {\n"
            + "          \"Id\": {\n"
            + "            \"N\": \"101\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"NewImage\": {\n"
            + "          \"Message\": {\n"
            + "            \"S\": \"New item!\"\n"
            + "          },\n"
            + "          \"Id\": {\n"
            + "            \"N\": \"101\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"StreamViewType\": \"NEW_AND_OLD_IMAGES\",\n"
            + "        \"SequenceNumber\": \"111\",\n"
            + "        \"SizeBytes\": 26\n"
            + "      },\n"
            + "      \"awsRegion\": \"eu-west-2\",\n"
            + "      \"eventName\": \"INSERT\",\n"
            + "      \"eventSourceARN\": \"arn:aws:dynamodb:eu-west-2:account-id:table/ExampleTableWithStream/stream/2015-06-27T00:48:05.899\",\n"
            + "      \"eventSource\": \"aws:dynamodb\"\n"
            + "    },\n"
            + "    {\n"
            + "      \"eventID\": \"2\",\n"
            + "      \"eventVersion\": \"1.0\",\n"
            + "      \"dynamodb\": {\n"
            + "        \"OldImage\": {\n"
            + "          \"Message\": {\n"
            + "            \"S\": \"New item!\"\n"
            + "          },\n"
            + "          \"Id\": {\n"
            + "            \"N\": \"101\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"SequenceNumber\": \"222\",\n"
            + "        \"Keys\": {\n"
            + "          \"Id\": {\n"
            + "            \"N\": \"101\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"SizeBytes\": 59,\n"
            + "        \"NewImage\": {\n"
            + "          \"Message\": {\n"
            + "            \"S\": \"This item has changed\"\n"
            + "          },\n"
            + "          \"Id\": {\n"
            + "            \"N\": \"101\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"StreamViewType\": \"NEW_AND_OLD_IMAGES\"\n"
            + "      },\n"
            + "      \"awsRegion\": \"eu-west-2\",\n"
            + "      \"eventName\": \"MODIFY\",\n"
            + "      \"eventSourceARN\": \"arn:aws:dynamodb:eu-west-2:account-id:table/ExampleTableWithStream/stream/2015-06-27T00:48:05.899\",\n"
            + "      \"eventSource\": \"aws:dynamodb\"\n"
            + "    },\n"
            + "    {\n"
            + "      \"eventID\": \"3\",\n"
            + "      \"eventVersion\": \"1.0\",\n"
            + "      \"dynamodb\": {\n"
            + "        \"Keys\": {\n"
            + "          \"Id\": {\n"
            + "            \"N\": \"101\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"SizeBytes\": 38,\n"
            + "        \"SequenceNumber\": \"333\",\n"
            + "        \"OldImage\": {\n"
            + "          \"Message\": {\n"
            + "            \"S\": \"This item has changed\"\n"
            + "          },\n"
            + "          \"Id\": {\n"
            + "            \"N\": \"101\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"StreamViewType\": \"NEW_AND_OLD_IMAGES\"\n"
            + "      },\n"
            + "      \"awsRegion\": \"eu-west-2\",\n"
            + "      \"eventName\": \"REMOVE\",\n"
            + "      \"eventSourceARN\": \"arn:aws:dynamodb:eu-west-2:account-id:table/ExampleTableWithStream/stream/2015-06-27T00:48:05.899\",\n"
            + "      \"eventSource\": \"aws:dynamodb\"\n"
            + "    }\n"
            + "  ]\n"
            + "}";

    JsonObject msgJson = new JsonObject(msg);
    JsonObject respJson = new JsonObject(new String(Base64.getDecoder().decode(new JsonObject(new AWSLambda().handleMessage(msgJson).blockingGet()).getString("body"))));
    assertEquals("DYNAMODB", respJson.getString("aws-event-source"));
    assertEquals(1298, respJson.getLong("length"));

    testContext.completeNow();
  }

  @Test
  public void testCodeCommit(VertxTestContext testContext) {
    String msg = "{\n"
            + "  \"Records\": [\n"
            + "    {\n"
            + "      \"awsRegion\": \"eu-west-2\",\n"
            + "      \"codecommit\": {\n"
            + "        \"references\": [\n"
            + "          {\n"
            + "            \"commit\": \"5c4ef1049f1d27deadbeeff313e0730018be182b\",\n"
            + "            \"ref\": \"refs/heads/master\"\n"
            + "          }\n"
            + "        ]\n"
            + "      },\n"
            + "      \"customData\": \"this is custom data\",\n"
            + "      \"eventId\": \"5a824061-17ca-46a9-bbf9-114edeadbeef\",\n"
            + "      \"eventName\": \"TriggerEventTest\",\n"
            + "      \"eventPartNumber\": 1,\n"
            + "      \"eventSource\": \"aws:codecommit\",\n"
            + "      \"eventSourceARN\": \"arn:aws:codecommit:eu-west-2:123456789012:my-repo\",\n"
            + "      \"eventTime\": \"2016-01-01T23:59:59.000+0000\",\n"
            + "      \"eventTotalParts\": 1,\n"
            + "      \"eventTriggerConfigId\": \"5a824061-17ca-46a9-bbf9-114edeadbeef\",\n"
            + "      \"eventTriggerName\": \"my-trigger\",\n"
            + "      \"eventVersion\": \"1.0\",\n"
            + "      \"userIdentityARN\": \"arn:aws:iam::123456789012:root\"\n"
            + "    }\n"
            + "  ]\n"
            + "}";

    JsonObject msgJson = new JsonObject(msg);
    JsonObject respJson = new JsonObject(new String(Base64.getDecoder().decode(new JsonObject(new AWSLambda().handleMessage(msgJson).blockingGet()).getString("body"))));
    assertEquals("CODE_COMMIT", respJson.getString("aws-event-source"));
    assertEquals(613, respJson.getLong("length"));

    testContext.completeNow();
  }
}
