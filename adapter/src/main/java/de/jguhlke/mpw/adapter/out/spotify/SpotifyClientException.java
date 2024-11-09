package de.jguhlke.mpw.adapter.out.spotify;

public class SpotifyClientException extends RuntimeException {
  public SpotifyClientException(String message) {
    super(message);
  }

  public SpotifyClientException(Exception exception) {
    super(exception);
  }
}