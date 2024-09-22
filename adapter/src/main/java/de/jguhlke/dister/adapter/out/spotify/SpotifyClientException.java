package de.jguhlke.dister.adapter.out.spotify;

public class SpotifyClientException extends RuntimeException {
  public SpotifyClientException(Exception exception) {
    super(exception);
  }
}
