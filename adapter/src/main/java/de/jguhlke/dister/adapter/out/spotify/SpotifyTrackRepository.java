package de.jguhlke.dister.adapter.out.spotify;

import de.jguhlke.dister.application.port.out.TrackRepository;
import de.jguhlke.dister.model.Authentication;
import de.jguhlke.dister.model.Track;
import de.jguhlke.dister.model.id.TrackId;
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
