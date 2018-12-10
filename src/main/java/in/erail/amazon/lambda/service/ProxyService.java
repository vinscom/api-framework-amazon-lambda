package in.erail.amazon.lambda.service;

import com.google.common.base.Joiner;
import com.google.common.io.BaseEncoding;
import in.erail.amazon.lambda.model.LambdaProxyRequest;
import in.erail.common.FrameworkConstants;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.eventbus.Message;
import in.erail.service.RESTServiceImpl;
import io.vertx.core.http.HttpMethod;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.core.http.HttpClientRequest;
import io.vertx.reactivex.core.http.HttpClientResponse;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @author vinay
 */
public class ProxyService extends RESTServiceImpl {

  private String mHost;
  private int mPort;
  private String mBaseMountRoute;

  @Override
  public void process(Message<JsonObject> pMessage) {
    LambdaProxyRequest proxyRequest = pMessage.body().mapTo(LambdaProxyRequest.class);

    //Build Request
    HttpClientRequest clientRequest = getVertx()
            .createHttpClient()
            .requestAbs(HttpMethod.valueOf(proxyRequest.getHttpMethod()), generateURL(proxyRequest));

    //Add Headers
    Optional<Map<String, String>> headers = Optional
            .ofNullable(proxyRequest.getHeaders())
            .filter(t -> !t.isEmpty());

    headers.ifPresent((t) -> t.forEach((k, v) -> clientRequest.putHeader(k, v)));

    //Add Body
    Optional<String> body = Optional
            .ofNullable(proxyRequest.getBody())
            .map((t) -> {
              if (proxyRequest.isIsBase64Encoded()) {
                return new String(BaseEncoding.base64().decode(t));
              }
              return t;
            });

    body.ifPresent((t) -> clientRequest.write(Buffer.buffer(t)));

    //Send Request
    clientRequest
            .toObservable()
            .subscribe((resp) -> {
              JsonObject response = new JsonObject();

              //Add Headers
              Optional<JsonObject> respHeaders = Optional
                      .ofNullable(resp.headers())
                      .flatMap(t -> Optional.of(t.getDelegate().entries()))
                      .map((r) -> {
                        JsonObject h = new JsonObject();
                        r.forEach((i) -> h.put(i.getKey(), i.getValue()));
                        return h;
                      });

              respHeaders.ifPresent(t -> response.put(FrameworkConstants.RoutingContext.Json.HEADERS, t));

              //Add status code
              response.put(FrameworkConstants.RoutingContext.Json.STATUS_CODE, resp.statusCode());

              //Add Body
              response.put(FrameworkConstants.RoutingContext.Json.IS_BASE64_ENCODED, true);

              resp
                      .toObservable()
                      .subscribe((t) -> {
                        response.put(FrameworkConstants.RoutingContext.Json.BODY, t.getDelegate().getBytes());
                        pMessage.reply(response);
                      });
            });
  }

  protected String generateURL(LambdaProxyRequest pRequest) {
    StringBuilder sb = new StringBuilder("http://");

    sb
            .append(getHost())
            .append(":")
            .append(getPort())
            .append("/")
            .append(pRequest.getPath());

    Optional<Map<String, String>> qs = Optional
            .ofNullable(pRequest.getQueryStringParameters())
            .filter((t) -> !t.isEmpty());

    qs.ifPresent((t) -> {
      sb.append("?");
      sb.append(Joiner.on("&").join(t.entrySet()));
    });

    return sb.toString();
  }

  public String getHost() {
    return mHost;
  }

  public void setHost(String pHost) {
    this.mHost = pHost;
  }

  public int getPort() {
    return mPort;
  }

  public void setPort(int pPort) {
    this.mPort = pPort;
  }

  public String getBaseMountRoute() {
    return mBaseMountRoute;
  }

  public void setBaseMountRoute(String pBaseMountRoute) {
    this.mBaseMountRoute = pBaseMountRoute;
  }

}
