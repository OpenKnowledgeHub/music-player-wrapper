package de.jguhlke.dister.adapter.out.spotify;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.jguhlke.dister.model.Authentication;
import de.jguhlke.dister.model.Device;
import de.jguhlke.dister.model.Player;
import de.jguhlke.dister.model.Track;
import de.jguhlke.dister.model.id.DeviceId;
import de.jguhlke.dister.model.id.TrackId;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

class SpotifyClient {

  private static final Logger LOG = Logger.getLogger(SpotifyClient.class.getName());

  public static final String API_PLAYER_PATH = "/me/player";

  private final String playerUrl;
  private final String trackUrl;
  private final HttpClient httpClient = HttpClient.newHttpClient();

  private final Authentication authentication;
  private final ObjectMapper objectMapper;

  SpotifyClient(Authentication authentication) {
    this.authentication = authentication;
    this.objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    SpotifyConfiguration spotifyConfiguration = SpotifyConfiguration.getInstance();

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

    return Optional.of(
        new Player(
            playerResponse.is_playing(),
            false,
            new Device(
                new DeviceId(playerResponse.device().id()),
                playerResponse.device().name(),
                playerResponse.device().is_active()),
            new Track(new TrackId(playerResponse.item().id()), playerResponse.item().name())));
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
      return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
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
}
