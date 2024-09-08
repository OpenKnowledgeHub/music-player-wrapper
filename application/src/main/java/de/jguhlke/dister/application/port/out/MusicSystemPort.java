package de.jguhlke.dister.application.port.out;

import de.jguhlke.dister.model.Player;
import de.jguhlke.dister.model.Track;

public interface MusicSystemPort {
  Player playTrackOn(Track track, Player player);

  Player resumeTrack(Player player);
}
