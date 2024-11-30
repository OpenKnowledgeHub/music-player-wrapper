package de.jguhlke.mpw.adapter.out.spotify;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.jguhlke.mpw.application.exception.AuthenticationException;
import de.jguhlke.mpw.application.port.in.ExchangeTokenRequest;
import de.jguhlke.mpw.application.port.in.ExchangeTokenResponse;
import de.jguhlke.mpw.model.Authentication;
import de.jguhlke.mpw.model.Device;
import de.jguhlke.mpw.model.Player;
import de.jguhlke.mpw.model.Track;
import de.jguhlke.mpw.model.id.DeviceId;
import de.jguhlke.mpw.model.id.TrackId;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

class SpotifyClient {

  private static final Logger LOG = Logger.getLogger(SpotifyClient.class.getName());

  public static final String API_PLAYER_PATH = "/me/player";

  private final String playerUrl;
  private final String trackUrl;
  private final SpotifyConfiguration spotifyConfiguration;
  private final HttpClient httpClient = HttpClient.newHttpClient();

  private Authentication authentication;
  private final ObjectMapper objectMapper;

  SpotifyClient(Authentication authentication) {
    this();
    this.authentication = authentication;
  }

  SpotifyClient() {
    this.objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    this.spotifyConfiguration = SpotifyConfiguration.getInstance();

    String baseUrl =
        "%s:%d/v1".formatted(spotifyConfiguration.getBaseUrl(), spotifyConfiguration.getPort());

    playerUrl = baseUrl + API_PLAYER_PATH;
    trackUrl = baseUrl + "/tracks";
  }

  void play(TrackId trackId, DeviceId deviceId) {
    String trackUri = trackId.id();
    if (!trackUri.startsWith("spotify:")) {
      trackUri = "spotify:track:" + trackUri;
    }

    String jsonPutBody = mapObjectToJsonString(new SpotifyPlayRequest(List.of(trackUri)));

    HttpRequest request =
        createPutRequestFor(URI.create(playerUrl + "/play?deviceId=" + deviceId.id()), jsonPutBody);

    sendRequest(request);
  }

  void pause() {
    HttpRequest request = createPutRequestFor(URI.create(playerUrl + "/pause"));

    sendRequest(request);
  }

  void resume(DeviceId deviceId) {
    HttpRequest request =
        createPutRequestFor(URI.create(playerUrl + "/play?deviceId=" + deviceId.id()));

    sendRequest(request);
  }

  Optional<Player> fetchCurrentPlayer() {
    HttpRequest request = createGetRequestFor(URI.create(playerUrl));

    HttpResponse<String> response = sendRequest(request);

    Optional<SpotifyPlayerResponse> optionalPlayerResponse =
        mapJsonStringToObject(response.body(), SpotifyPlayerResponse.class);

    if (optionalPlayerResponse.isEmpty()) {
      return Optional.empty();
    }

    SpotifyPlayerResponse playerResponse = optionalPlayerResponse.get();

    Device device = null;

    if (Objects.nonNull(playerResponse.device())) {
      device =
          new Device(
              new DeviceId(playerResponse.device().id()),
              playerResponse.device().name(),
              playerResponse.device().is_active());
    }

    Track track = null;

    if (Objects.nonNull(playerResponse.item())) {
      track = new Track(new TrackId(playerResponse.item().id()), playerResponse.item().name());
    }

    return Optional.of(new Player(playerResponse.is_playing(), false, device, track));
  }

  Optional<Track> fetchTrackById(TrackId trackId) {
    Objects.requireNonNull(trackId, "'trackId' must be set");

    HttpRequest request = createGetRequestFor(URI.create(trackUrl + "/" + trackId.id()));

    HttpResponse<String> response = sendRequest(request);

    Optional<SpotifyTrackResponse> optionalSpotifyTrackResponse =
        mapJsonStringToObject(response.body(), SpotifyTrackResponse.class);

    if (optionalSpotifyTrackResponse.isEmpty()) {
      return Optional.empty();
    }

    SpotifyTrackResponse trackResponse = optionalSpotifyTrackResponse.get();

    return Optional.of(new Track(new TrackId(trackResponse.id()), trackResponse.name()));
  }

  ExchangeTokenResponse exchangeToken(ExchangeTokenRequest exchangeTokenRequest) {
    Objects.requireNonNull(exchangeTokenRequest, "'exchangeTokenRequest' must be set");

    Map<String, String> formParams =
        Map.of(
            "grant_type",
            "authorization_code",
            "code",
            exchangeTokenRequest.code(),
            "redirect_uri",
            spotifyConfiguration.getClientRedirectUrl());

    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(URI.create(spotifyConfiguration.getClientTokenUrl()))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .header(
                "Authorization",
                "Basic "
                    + Base64.getEncoder()
                        .encodeToString(
                            ("%s:%s"
                                    .formatted(
                                        spotifyConfiguration.getClientId(),
                                        spotifyConfiguration.getClientSecret()))
                                .getBytes()))
            .POST(HttpRequest.BodyPublishers.ofString(mapFormDataAsString(formParams)))
            .build();

    HttpResponse<String> response = sendRequest(request);

    Optional<SpotifyTokenResponse> optionalSpotifyTokenResponse =
        mapJsonStringToObject(response.body(), SpotifyTokenResponse.class);

    if (optionalSpotifyTokenResponse.isEmpty()) {
      throw new SpotifyClientException("Token exchange could not be parsed");
    }

    SpotifyTokenResponse spotifyTokenResponse = optionalSpotifyTokenResponse.get();

    return new ExchangeTokenResponse(
        spotifyTokenResponse.access_token(),
        spotifyTokenResponse.refresh_token(),
        spotifyTokenResponse.expires_in());
  }

  private HttpRequest createPutRequestFor(URI uri) {
    return createBaseRequestBuilderFor(uri).PUT(HttpRequest.BodyPublishers.noBody()).build();
  }

  private HttpRequest createPutRequestFor(URI uri, String body) {
    return createBaseRequestBuilderFor(uri).PUT(HttpRequest.BodyPublishers.ofString(body)).build();
  }

  private HttpRequest createGetRequestFor(URI uri) {
    return createBaseRequestBuilderFor(uri).GET().build();
  }

  private HttpRequest.Builder createBaseRequestBuilderFor(URI uri) {
    return HttpRequest.newBuilder()
        .uri(uri)
        .header("Authorization", "Bearer " + authentication.getAuthentication());
  }

  private HttpResponse<String> sendRequest(HttpRequest request) {
    try {
      HttpResponse<String> response =
          httpClient.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() >= 200 && response.statusCode() < 300) {
        return response;
      }

      if (response.statusCode() == 401 || response.statusCode() == 403) {
        throw new AuthenticationException();
      }

      throw new SpotifyClientException(
          "Unexpected status code %d occurred on request to %s "
              .formatted(response.statusCode(), request.uri().toString()));

    } catch (IOException | InterruptedException exception) {
      throw new SpotifyClientException(exception);
    }
  }

  private String mapObjectToJsonString(SpotifyPlayRequest value) {
    try {
      return objectMapper.writeValueAsString(value);
    } catch (JsonProcessingException exception) {
      throw new SpotifyClientException(exception);
    }
  }

  private <T> Optional<T> mapJsonStringToObject(String jsonString, Class<T> targetClass) {
    try {
      return Optional.of(objectMapper.readValue(jsonString, targetClass));
    } catch (JsonProcessingException exception) {
      LOG.log(Level.WARNING, exception.getMessage(), exception);
      return Optional.empty();
    }
  }

  private String mapFormDataAsString(Map<String, String> formData) {
    Objects.requireNonNull(formData, "'formData' must be set");

    StringBuilder mappedStringBuilder = new StringBuilder();

    for (Map.Entry<String, String> formEntry : formData.entrySet()) {
      if (!mappedStringBuilder.isEmpty()) {
        mappedStringBuilder.append("&");
      }

      mappedStringBuilder.append(URLEncoder.encode(formEntry.getKey(), UTF_8));
      mappedStringBuilder.append("=");
      mappedStringBuilder.append(URLEncoder.encode(formEntry.getValue(), UTF_8));
    }

    return mappedStringBuilder.toString();
  }
}
