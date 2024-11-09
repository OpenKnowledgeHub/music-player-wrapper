package de.jguhlke.mpw.adapter.out.spotify;

import de.jguhlke.mpw.application.port.out.TrackRepository;
import de.jguhlke.mpw.model.Authentication;
import de.jguhlke.mpw.model.Track;
import de.jguhlke.mpw.model.id.TrackId;
import java.util.Objects;
import java.util.Optional;

public class SpotifyTrackRepository implements TrackRepository {

  @Override
  public Optional<Track> findTrackById(TrackId trackId, Authentication authentication) {
    Objects.requireNonNull(authentication, "'authentication' must be set");
    Objects.requireNonNull(trackId, "'trackId' must be set");

    return new SpotifyClient(authentication).fetchTrackById(trackId);
  }
}
