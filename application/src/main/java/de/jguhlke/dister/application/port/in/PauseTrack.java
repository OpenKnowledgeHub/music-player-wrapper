package de.jguhlke.dister.application.port.in;

import de.jguhlke.dister.model.Authentication;
import de.jguhlke.dister.model.Player;

public interface PauseTrack {
  Player pause(Authentication authentication);
}
