package de.jguhlke.dister.bootstrap;

import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;

public class RestLauncher {

  private UndertowJaxrsServer server;

  public static void main(String[] args) {
    new RestLauncher().startOnDefaultPort();
  }

  public void startOnDefaultPort() {
    server = new UndertowJaxrsServer();
    startServer();
  }

  private void startServer() {
    server.start(
        Undertow.builder()
            .addHttpListener(8081, "0.0.0.0")
            .setServerOption(UndertowOptions.ENABLE_HTTP2, true));

    server.deploy(ControllerApplication.class, "dister/v1");

    server.getDeployment();
  }
}
