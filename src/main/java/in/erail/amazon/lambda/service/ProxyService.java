package in.erail.amazon.lambda.service;

import com.google.common.base.Joiner;
import in.erail.model.Event;
import in.erail.model.RequestEvent;
import in.erail.model.ResponseEvent;
import in.erail.server.Server;
import in.erail.service.RESTServiceImpl;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.vertx.reactivex.core.MultiMap;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.ext.web.client.HttpRequest;
import io.vertx.reactivex.ext.web.client.WebClient;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.logging.log4j.util.Strings;

/**
 *
 * @author vinay
 */
public class ProxyService extends RESTServiceImpl {

  private String mHost;
  private int mPort;
  private String mPathPrefix;
  private WebClient mWebClient;
  private Server mServer;

  protected String generateURL(RequestEvent pRequest) {
    StringBuilder sb = new StringBuilder("http://");

    sb
            .append(getHost())
            .append(":")
            .append(getPort());

    if (Strings.isNotBlank(getPathPrefix())) {
      sb.append(getPathPrefix());
    }

    sb.append(pRequest.getPath());

    Optional
            .ofNullable(pRequest.getQueryStringParameters())
            .filter((t) -> !t.isEmpty())
            .ifPresent((t) -> {
              List<String> queryList = t
                      .entrySet()
                      .stream()
                      .reduce(new ArrayList<>(), (acc, entry) -> {
                        try {
                          StringBuilder param = new StringBuilder();
                          param.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.name()));
                          param.append("=");
                          param.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.name()));
                          acc.add(param.toString());
                        } catch (UnsupportedEncodingException ex) {
                          Logger.getLogger(ProxyService.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        return acc;
                      }, (first, second) -> {
                        first.addAll(second);
                        return first;
                      });
              sb.append("?").append(Joiner.on("&").join(queryList));
            });

    return sb.toString();
  }

  public MaybeSource<Event> process(Maybe<Event> pRequest) {
    return pRequest.flatMap(this::handle);
  }

  public Maybe<Event> handle(Event pEvent) {

    RequestEvent proxyRequest = pEvent.getRequest();
    
    //Build Request
    HttpRequest<Buffer> clientRequest = getWebClient().requestAbs(proxyRequest.getHttpMethod(), generateURL(proxyRequest));

    //Add Headers
    Optional<Map<String, String>> headers = Optional
            .ofNullable(proxyRequest.getHeaders())
            .filter(t -> !t.isEmpty());

    headers.ifPresent((t) -> t.forEach((k, v) -> clientRequest.putHeader(k, v)));

    //Add Body
    byte[] body = Optional
            .ofNullable(proxyRequest.getBody())
            .orElse(new byte[0]);

    //Send Request
    return clientRequest
            .rxSendBuffer(Buffer.buffer(body))
            .map((resp) -> {
              ResponseEvent responseEvent = pEvent.getResponse();

              //Add Headers
              Optional
                      .ofNullable(resp.headers())
                      .orElse(MultiMap.caseInsensitiveMultiMap())
                      .entries()
                      .stream()
                      .forEach((t) -> responseEvent.addHeader(t.getKey(), t.getValue()));

              responseEvent.setStatusCode(resp.statusCode());
              responseEvent.setIsBase64Encoded(true);

              Optional
                      .ofNullable(resp.body())
                      .ifPresent(b -> responseEvent.setBody(b.getBytes()));

              return pEvent;
            })
            .doOnSuccess(e -> getLog().debug(() -> e.toString()))
            .toMaybe();

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

  public String getPathPrefix() {
    return mPathPrefix;
  }

  public void setPathPrefix(String pPathPrefix) {
    this.mPathPrefix = pPathPrefix;
  }

  public WebClient getWebClient() {
    return mWebClient;
  }

  public void setWebClient(WebClient pWebClient) {
    this.mWebClient = pWebClient;
  }

  public Server getServer() {
    return mServer;
  }

  public void setServer(Server pServer) {
    this.mServer = pServer;
  }

}
