package de.jguhlke.dister.application.service;

import de.jguhlke.dister.application.port.in.PlayTrackOnPlayer;
import de.jguhlke.dister.application.port.out.MusicSystemPort;
import de.jguhlke.dister.model.Authentication;
import de.jguhlke.dister.model.Player;
import de.jguhlke.dister.model.id.PlayerId;
import de.jguhlke.dister.model.id.TrackId;
import java.util.Objects;

public class PlayTrackOnPlayerService implements PlayTrackOnPlayer {

  private final MusicSystemPort musicSystemPort;

  public PlayTrackOnPlayerService(MusicSystemPort musicSystemPort) {
    this.musicSystemPort = musicSystemPort;
  }

  @Override
  public Player playTrackOn(TrackId trackId, PlayerId playerId, Authentication authentication) {
    Objects.requireNonNull(trackId, "'trackId' must be set");
    Objects.requireNonNull(playerId, "'playerId' must be set");
    Objects.requireNonNull(authentication, "'authentication' must be set");

    return musicSystemPort.playTrackById(trackId, playerId, authentication);
  }
}
