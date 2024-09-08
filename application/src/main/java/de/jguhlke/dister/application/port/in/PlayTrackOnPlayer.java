package de.jguhlke.dister.application.port.in;

import de.jguhlke.dister.model.Player;
import de.jguhlke.dister.model.Track;

public interface PlayTrackOnPlayer {
  Player playTrackOn(Track track, Player player);
}
