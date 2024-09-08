package de.jguhlke.dister.application.port.out;

import de.jguhlke.dister.model.Authentication;
import de.jguhlke.dister.model.Player;
import de.jguhlke.dister.model.id.PlayerId;
import de.jguhlke.dister.model.id.TrackId;

public interface MusicSystemPort {
  Player playTrackById(TrackId trackId, PlayerId playerId, Authentication authentication);

  Player resumeTrackById(PlayerId playerId, Authentication authentication);
}
