package de.jguhlke.dister.adapter.out.spotify;

import java.util.Objects;
import java.util.logging.Logger;

class SpotifyConfiguration {

  public static final int SPOTIFY_DEFAULT_PORT = 8080;
  public static final String SPOTIFY_DEFAULT_BASE_URL = "http://localhost";
  public static final String SPOTIFY_DEFAULT_CLIENT_ID = "client_id";
  public static final String SPOTIFY_DEFAULT_CLIENT_SECRET = "client_secret";
  public static final String SPOTIFY_DEFAULT_AUTH_URL = "http://localhost/authorize";
  public static final String SPOTIFY_DEFAULT_TOKEN_URL = "http://localhost/api/token";
  public static final String SPOTIFY_DEFAULT_REDIRECT_URL = "http://localhost/callback";

  private static final Logger LOG = Logger.getLogger(SpotifyConfiguration.class.getName());
  private static SpotifyConfiguration instance;

  private String baseUrl;
  private String clientId;
  private String clientSecret;
  private String authUrl;
  private String tokenUrl;
  private String redirectUrl;

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

  public String getClientId() {
    if (Objects.isNull(clientId) || clientId.isBlank()) {
      clientId = System.getenv("SPOTIFY_CLIENT_ID");
    }

    if (Objects.isNull(clientId) || clientId.isBlank()) {
      LOG.info("Set spotify client id to default");
      clientId = SPOTIFY_DEFAULT_CLIENT_ID;
    }

    return clientId;
  }

  public String getClientSecret() {
    if (Objects.isNull(clientSecret) || clientSecret.isBlank()) {
      clientSecret = System.getenv("SPOTIFY_CLIENT_SECRET");
    }

    if (Objects.isNull(clientSecret) || clientSecret.isBlank()) {
      LOG.info("Set spotify client secret to default");
      clientSecret = SPOTIFY_DEFAULT_CLIENT_SECRET;
    }

    return clientSecret;
  }

  public String getClientAuthorizationUrl() {
    if (Objects.isNull(authUrl) || authUrl.isBlank()) {
      authUrl = System.getenv("SPOTIFY_AUTH_URL");
    }

    if (Objects.isNull(authUrl) || authUrl.isBlank()) {
      LOG.info("Set spotify auth URL to default");
      authUrl = SPOTIFY_DEFAULT_AUTH_URL;
    }

    return authUrl;
  }

  public String getClientTokenUrl() {
    if (Objects.isNull(tokenUrl) || tokenUrl.isBlank()) {
      tokenUrl = System.getenv("SPOTIFY_TOKEN_URL");
    }

    if (Objects.isNull(tokenUrl) || tokenUrl.isBlank()) {
      LOG.info("Set spotify token URL to default");
      tokenUrl = SPOTIFY_DEFAULT_TOKEN_URL;
    }

    return tokenUrl;
  }

  public String getClientRedirectUrl() {
    if (Objects.isNull(redirectUrl) || redirectUrl.isBlank()) {
      redirectUrl = System.getenv("SPOTIFY_REDIRECT_URL");
    }

    if (Objects.isNull(redirectUrl) || redirectUrl.isBlank()) {
      LOG.info("Set spotify redirect URL to default");
      redirectUrl = SPOTIFY_DEFAULT_REDIRECT_URL;
    }

    return redirectUrl;
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
