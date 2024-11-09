package de.jguhlke.mpw.application.port.in;

import de.jguhlke.mpw.model.Authentication;
import de.jguhlke.mpw.model.Player;

public interface ResumeTrack {
  Player resume(Authentication authentication);
}
