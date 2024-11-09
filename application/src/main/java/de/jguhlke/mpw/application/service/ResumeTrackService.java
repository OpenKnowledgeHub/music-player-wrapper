package de.jguhlke.mpw.application.service;

import de.jguhlke.mpw.application.port.in.ResumeTrack;
import de.jguhlke.mpw.application.port.out.MusicSystem;
import de.jguhlke.mpw.application.port.out.PlayerRepository;
import de.jguhlke.mpw.model.Authentication;
import de.jguhlke.mpw.model.Player;
import de.jguhlke.mpw.model.exception.MusicPlayerWrapperException;
import java.util.Objects;

public class ResumeTrackService implements ResumeTrack {

  private final MusicSystem musicSystem;
  private final PlayerRepository playerRepository;

  public ResumeTrackService(MusicSystem musicSystem, PlayerRepository playerRepository) {
    this.musicSystem = musicSystem;
    this.playerRepository = playerRepository;
  }

  @Override
  public Player resume(Authentication authentication) {
    Objects.requireNonNull(authentication, "'authentication' must be set");

    Player player =
        playerRepository
            .fetchCurrentPlayer(authentication)
            .orElseThrow(() -> new MusicPlayerWrapperException("No current player found!"));

    return musicSystem.produceState(player.resume(), authentication);
  }
}
