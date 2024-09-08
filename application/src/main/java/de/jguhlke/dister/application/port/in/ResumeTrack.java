package de.jguhlke.dister.application.port.in;

import de.jguhlke.dister.model.Authentication;
import de.jguhlke.dister.model.Player;
import de.jguhlke.dister.model.id.PlayerId;

public interface ResumeTrack {
  Player resumeTrack(PlayerId playerId, Authentication authentication);
}
