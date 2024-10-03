package de.jguhlke.dister.bootstrap;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;

public class CORSHandler implements HttpHandler {

  private final HttpHandler next;

  public CORSHandler(HttpHandler next) {
    this.next = next;
  }

  @Override
  public void handleRequest(HttpServerExchange httpServerExchange) throws Exception {
    httpServerExchange
        .getResponseHeaders()
        .add(HttpString.tryFromString("Access-Control-Allow-Origin"), "*");
    httpServerExchange
        .getResponseHeaders()
        .add(HttpString.tryFromString("Access-Control-Allow-Methods"), "GET, POST, OPTIONS");

    // Wenn die Anfrage eine OPTIONS-Anfrage ist, beende hier
    if ("OPTIONS".equalsIgnoreCase(httpServerExchange.getRequestMethod().toString())) {
      httpServerExchange.setStatusCode(204);
      httpServerExchange.endExchange();
      return;
    }

    // Ansonsten rufe den nächsten Handler auf
    next.handleRequest(httpServerExchange);
  }
}
