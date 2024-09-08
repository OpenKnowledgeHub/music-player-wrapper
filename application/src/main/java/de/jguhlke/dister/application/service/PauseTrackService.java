package de.jguhlke.dister.application.service;

import de.jguhlke.dister.application.port.in.PauseTrack;
import de.jguhlke.dister.application.port.out.MusicSystemPort;
import de.jguhlke.dister.model.Authentication;
import de.jguhlke.dister.model.Player;
import de.jguhlke.dister.model.id.PlayerId;
import java.util.Objects;

public class PauseTrackService implements PauseTrack {

  private final MusicSystemPort musicSystemPort;

  public PauseTrackService(MusicSystemPort musicSystemPort) {
    this.musicSystemPort = musicSystemPort;
  }

  @Override
  public Player pauseTrack(PlayerId playerId, Authentication authentication) {
    Objects.requireNonNull(playerId, "'playerId' must be set");
    Objects.requireNonNull(authentication, "'authentication' must be set");

    return musicSystemPort.pauseTrackById(playerId, authentication);
  }
}
