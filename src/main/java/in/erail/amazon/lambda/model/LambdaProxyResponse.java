package in.erail.amazon.lambda.model;

import io.vertx.core.MultiMap;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author vinay
 */
public class LambdaProxyResponse {

  private boolean mIsBase64Encoded = true;
  private int mStatusCode = 200;
  private Map<String, String> mHeaders;
  private String mBody = "";

  public boolean isIsBase64Encoded() {
    return mIsBase64Encoded;
  }

  public void setIsBase64Encoded(boolean pIsBase64Encoded) {
    this.mIsBase64Encoded = pIsBase64Encoded;
  }

  public int getStatusCode() {
    return mStatusCode;
  }

  public void setStatusCode(int pStatusCode) {
    this.mStatusCode = pStatusCode;
  }

  public Map<String, String> getHeaders() {
    if(mHeaders == null) {
      mHeaders = new HashMap<>();
    }
    return mHeaders;
  }

  public void setHeaders(Map<String, String> pHeaders) {
    this.mHeaders = pHeaders;
  }

  public String getBody() {
    return mBody;
  }

  public void setBody(String pBody) {
    this.mBody = pBody;
  }

}
