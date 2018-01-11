package in.erail.amazon.lambda;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import java.util.concurrent.CompletableFuture;

/**
 *
 * @author vinay
 */
public class MessageAWSLambda<T> implements Message<T> {

  private CompletableFuture<T> mResult;
  private T mBody;

  public MessageAWSLambda(CompletableFuture<T> pResult) {
    this(pResult, null);
  }

  public MessageAWSLambda(CompletableFuture<T> pResult, T pBody) {
    this.mResult = pResult;
    this.mBody = pBody;
  }

  @Override
  public String address() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public MultiMap headers() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public T body() {
    return mBody;
  }

  @Override
  public String replyAddress() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean isSend() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void reply(Object pMessage) {
    mResult.complete((T) pMessage);
  }

  @Override
  public <R> void reply(Object pMessage, Handler<AsyncResult<Message<R>>> pReplyHandler) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void reply(Object pMessage, DeliveryOptions pOptions) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public <R> void reply(Object pMessage, DeliveryOptions pOptions, Handler<AsyncResult<Message<R>>> pReplyHandler) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void fail(int pFailureCode, String pMessage) {
    mResult.complete((T) new JsonObject().put("error", pMessage));
  }

}
