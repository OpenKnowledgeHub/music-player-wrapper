package de.jguhlke.dister.application.port.out;

import de.jguhlke.dister.model.Authentication;
import de.jguhlke.dister.model.Track;
import de.jguhlke.dister.model.id.TrackId;
import java.util.Optional;

public interface TrackRepository {
  Optional<Track> findTrackById(TrackId trackId, Authentication authentication);
}
