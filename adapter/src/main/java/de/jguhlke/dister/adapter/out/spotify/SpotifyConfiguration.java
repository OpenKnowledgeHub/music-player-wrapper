package de.jguhlke.dister.adapter.out.spotify;

import java.util.Objects;
import java.util.logging.Logger;

class SpotifyConfiguration {

  public static final int SPOTIFY_DEFAULT_PORT = 8080;
  public static final String SPOTIFY_DEFAULT_BASE_URL = "http://localhost";

  private static final Logger LOG = Logger.getLogger(SpotifyConfiguration.class.getName());
  private static SpotifyConfiguration instance;

  private String baseUrl;

  private int port;

  private SpotifyConfiguration() {}

  public static synchronized SpotifyConfiguration getInstance() {
    if (Objects.isNull(instance)) {
      instance = new SpotifyConfiguration();
    }

    return instance;
  }

  public String getBaseUrl() {
    if (Objects.isNull(baseUrl) || baseUrl.isBlank()) {
      baseUrl = System.getenv("SPOTIFY_BASE_URL");
    }

    if (Objects.isNull(baseUrl) || baseUrl.isBlank()) {
      LOG.info("Set spotify base URL to default URL");
      baseUrl = SPOTIFY_DEFAULT_BASE_URL;
    }

    return baseUrl;
  }

  public int getPort() {
    if (port == 0) {
      String spotifyPort = System.getenv("SPOTIFY_PORT");

      if (Objects.nonNull(spotifyPort) && !spotifyPort.isBlank()) {
        port = Integer.parseInt(spotifyPort);
      } else {
        LOG.info("Set spotify port to default port");
        port = SPOTIFY_DEFAULT_PORT;
      }
    }

    return port;
  }
}
