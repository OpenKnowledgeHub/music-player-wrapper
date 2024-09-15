package de.jguhlke.dister.application.port.in;

import de.jguhlke.dister.model.Authentication;
import de.jguhlke.dister.model.Player;
import de.jguhlke.dister.model.id.TrackId;

public interface PlayTrack {
  Player play(TrackId trackId, Authentication authentication);
}
