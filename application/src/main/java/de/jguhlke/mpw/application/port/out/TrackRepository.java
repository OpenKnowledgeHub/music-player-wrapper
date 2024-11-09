package de.jguhlke.mpw.application.port.out;

import de.jguhlke.mpw.model.Authentication;
import de.jguhlke.mpw.model.Track;
import de.jguhlke.mpw.model.id.TrackId;
import java.util.Optional;

public interface TrackRepository {
  Optional<Track> findTrackById(TrackId trackId, Authentication authentication);
}
