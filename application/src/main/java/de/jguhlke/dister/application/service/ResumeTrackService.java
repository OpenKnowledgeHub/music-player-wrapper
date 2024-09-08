package de.jguhlke.dister.application.service;

import de.jguhlke.dister.application.port.in.ResumeTrack;
import de.jguhlke.dister.application.port.out.MusicSystemPort;
import de.jguhlke.dister.model.Authentication;
import de.jguhlke.dister.model.Player;
import de.jguhlke.dister.model.id.PlayerId;
import java.util.Objects;

public class ResumeTrackService implements ResumeTrack {

  private final MusicSystemPort musicSystemPort;

  public ResumeTrackService(MusicSystemPort musicSystemPort) {
    this.musicSystemPort = musicSystemPort;
  }

  @Override
  public Player resumeTrack(PlayerId playerId, Authentication authentication) {
    Objects.requireNonNull(playerId, "'playerId' must be set");
    Objects.requireNonNull(authentication, "'authentication' must be set");

    return musicSystemPort.resumeTrackById(playerId, authentication);
  }
}
