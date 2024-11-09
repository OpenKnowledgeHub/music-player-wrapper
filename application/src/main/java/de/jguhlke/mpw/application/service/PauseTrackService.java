package de.jguhlke.mpw.application.service;

import de.jguhlke.mpw.application.port.in.PauseTrack;
import de.jguhlke.mpw.application.port.out.MusicSystem;
import de.jguhlke.mpw.application.port.out.PlayerRepository;
import de.jguhlke.mpw.model.Authentication;
import de.jguhlke.mpw.model.Player;
import de.jguhlke.mpw.model.exception.MusicPlayerWrapperException;
import java.util.Objects;

public class PauseTrackService implements PauseTrack {

  private final MusicSystem musicSystem;
  private final PlayerRepository playerRepository;

  public PauseTrackService(MusicSystem musicSystem, PlayerRepository playerRepository) {
    this.musicSystem = musicSystem;
    this.playerRepository = playerRepository;
  }

  @Override
  public Player pause(Authentication authentication) {
    Objects.requireNonNull(authentication, "'authentication' must be set");

    Player player =
        playerRepository
            .fetchCurrentPlayer(authentication)
            .orElseThrow(() -> new MusicPlayerWrapperException("No current player found!"));

    return musicSystem.produceState(player.stop(), authentication);
  }
}
