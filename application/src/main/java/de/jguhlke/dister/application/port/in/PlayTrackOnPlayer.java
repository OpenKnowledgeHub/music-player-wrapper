package de.jguhlke.dister.application.port.in;

import de.jguhlke.dister.model.Authentication;
import de.jguhlke.dister.model.Player;
import de.jguhlke.dister.model.id.PlayerId;
import de.jguhlke.dister.model.id.TrackId;

public interface PlayTrackOnPlayer {
  Player playTrackOn(TrackId trackId, PlayerId playerId, Authentication authentication);
}
