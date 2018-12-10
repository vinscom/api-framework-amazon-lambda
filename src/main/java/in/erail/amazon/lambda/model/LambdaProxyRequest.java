package in.erail.amazon.lambda.model;

import java.util.Map;

/**
 *
 * @author vinay
 */
public class LambdaProxyRequest {

  private String mResource;
  private String mPath;
  private String mHttpMethod;
  private Map<String, String> mHeaders;
  private Map<String, String[]> mMultiValueHeaders;
  private Map<String, String> mQueryStringParameters;
  private Map<String, String[]> mMultiValueQueryStringParameters;
  private Map<String, String> mPathParameters;
  private Map<String, String> mStageVariables;
  private Map mRequestContext;
  private String mBody;
  private boolean mIsBase64Encoded = false;

  public String getResource() {
    return mResource;
  }

  public void setResource(String pResource) {
    this.mResource = pResource;
  }

  public String getPath() {
    return mPath;
  }

  public void setPath(String pPath) {
    this.mPath = pPath;
  }

  public String getHttpMethod() {
    return mHttpMethod;
  }

  public void setHttpMethod(String pHttpMethod) {
    this.mHttpMethod = pHttpMethod;
  }

  public Map<String, String> getHeaders() {
    return mHeaders;
  }

  public void setHeaders(Map<String, String> pHeaders) {
    this.mHeaders = pHeaders;
  }

  public Map<String, String[]> getMultiValueHeaders() {
    return mMultiValueHeaders;
  }

  public void setMultiValueHeaders(Map<String, String[]> pMultiValueHeaders) {
    this.mMultiValueHeaders = pMultiValueHeaders;
  }

  public Map<String, String> getQueryStringParameters() {
    return mQueryStringParameters;
  }

  public void setQueryStringParameters(Map<String, String> pQueryStringParameters) {
    this.mQueryStringParameters = pQueryStringParameters;
  }

  public Map<String, String[]> getMultiValueQueryStringParameters() {
    return mMultiValueQueryStringParameters;
  }

  public void setMultiValueQueryStringParameters(Map<String, String[]> pMultiValueQueryStringParameters) {
    this.mMultiValueQueryStringParameters = pMultiValueQueryStringParameters;
  }

  public Map<String, String> getPathParameters() {
    return mPathParameters;
  }

  public void setPathParameters(Map<String, String> pPathParameters) {
    this.mPathParameters = pPathParameters;
  }

  public Map<String, String> getStageVariables() {
    return mStageVariables;
  }

  public void setStageVariables(Map<String, String> pStageVariables) {
    this.mStageVariables = pStageVariables;
  }

  public Map getRequestContext() {
    return mRequestContext;
  }

  public void setRequestContext(Map pRequestContext) {
    this.mRequestContext = pRequestContext;
  }

  public String getBody() {
    return mBody;
  }

  public void setBody(String pBody) {
    this.mBody = pBody;
  }

  public boolean isIsBase64Encoded() {
    return mIsBase64Encoded;
  }

  public void setIsBase64Encoded(boolean pIsBase64Encoded) {
    this.mIsBase64Encoded = pIsBase64Encoded;
  }
  
}
