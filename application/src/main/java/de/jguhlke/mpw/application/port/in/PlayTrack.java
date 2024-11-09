package de.jguhlke.mpw.application.port.in;

import de.jguhlke.mpw.model.Authentication;
import de.jguhlke.mpw.model.Player;
import de.jguhlke.mpw.model.id.TrackId;

public interface PlayTrack {
  Player play(TrackId trackId, Authentication authentication);
}
