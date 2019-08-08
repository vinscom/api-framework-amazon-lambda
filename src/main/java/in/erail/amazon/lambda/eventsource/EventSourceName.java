package in.erail.amazon.lambda.eventsource;

/**
 *
 * @author vinay
 */
public enum EventSourceName {

  S3("aws:s3"),
  SQS("aws:sqs"),
  CODE_COMMIT("aws:codecommit"),
  SNS("aws:sns"),
  API_GATEWAY("aws:apigateway"),
  SES("aws:ses"),
  KINESIS("aws:kinesis"),
  DYNAMODB("aws:dynamodb"),
  DEFAULT("");

  private final String event;

  EventSourceName(String event) {
    this.event = event;
  }

  public String event() {
    return event;
  }
}
