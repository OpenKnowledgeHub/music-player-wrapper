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

  private static final String BASE_URL = "https://api.spotify.com/v1";
  private static final String PLAYER_URL = BASE_URL + "/me/player";
  private static final String TRACK_URL = BASE_URL + "/tracks";
  private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

  private final Authentication authentication;
  private final ObjectMapper objectMapper;

  SpotifyClient(Authentication authentication) {
    this.authentication = authentication;
    this.objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  void play(TrackId trackId, DeviceId deviceId) {
    String trackUri = trackId.id();
    if (!trackUri.startsWith("spotify:")) {
      trackUri = "spotify:track:" + trackUri;
    }

    String jsonPutBody;

    try {
      jsonPutBody = objectMapper.writeValueAsString(new SpotifyPlayRequest(List.of(trackUri)));
    } catch (JsonProcessingException exception) {
      throw new SpotifyClientException(exception);
    }

    HttpRequest request =
        createPutRequestFor(
            URI.create(PLAYER_URL + "/play?deviceId=" + deviceId.id()), jsonPutBody);

    try {
      HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (IOException | InterruptedException exception) {
      throw new SpotifyClientException(exception);
    }
  }

  void pause() {
    HttpRequest request = createPutRequestFor(URI.create(PLAYER_URL + "/pause"));

    try {
      HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (IOException | InterruptedException exception) {
      LOG.log(Level.SEVERE, exception.getMessage(), exception);
      throw new SpotifyClientException(exception);
    }
  }

  void resume(DeviceId deviceId) {
    HttpRequest request =
        createPutRequestFor(URI.create(PLAYER_URL + "/play?deviceId=" + deviceId.id()));

    try {
      HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (IOException | InterruptedException exception) {
      LOG.log(Level.SEVERE, exception.getMessage(), exception);
      throw new SpotifyClientException(exception);
    }
  }

  Optional<Player> fetchCurrentPlayer() {
    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(URI.create(PLAYER_URL))
            .header("Authorization", "Bearer " + authentication.getAuthentication())
            .GET()
            .build();

    HttpResponse<String> response;
    try {
      response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (IOException | InterruptedException exception) {
      LOG.log(Level.WARNING, exception.getMessage(), exception);
      return Optional.empty();
    }

    SpotifyPlayerResponse playerResponse;
    try {
      playerResponse = objectMapper.readValue(response.body(), SpotifyPlayerResponse.class);
    } catch (JsonProcessingException exception) {
      LOG.log(Level.WARNING, exception.getMessage(), exception);
      return Optional.empty();
    }

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

    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(URI.create(TRACK_URL + "/" + trackId.id()))
            .header("Authorization", "Bearer " + authentication.getAuthentication())
            .GET()
            .build();

    HttpResponse<String> response;
    try {
      response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (IOException | InterruptedException exception) {
      LOG.log(Level.WARNING, exception.getMessage(), exception);
      return Optional.empty();
    }

    SpotifyTrackResponse trackResponse;
    try {
      trackResponse = objectMapper.readValue(response.body(), SpotifyTrackResponse.class);
    } catch (JsonProcessingException exception) {
      LOG.log(Level.WARNING, exception.getMessage(), exception);
      return Optional.empty();
    }

    return Optional.of(new Track(new TrackId(trackResponse.id()), trackResponse.name()));
  }

  private HttpRequest createPutRequestFor(URI uri) {
    return createBaseRequestBuilderFor(uri).PUT(HttpRequest.BodyPublishers.noBody()).build();
  }

  private HttpRequest createPutRequestFor(URI uri, String body) {
    return createBaseRequestBuilderFor(uri).PUT(HttpRequest.BodyPublishers.ofString(body)).build();
  }

  private HttpRequest.Builder createBaseRequestBuilderFor(URI uri) {
    return HttpRequest.newBuilder()
        .uri(uri)
        .header("Authorization", "Bearer " + authentication.getAuthentication());
  }
}
