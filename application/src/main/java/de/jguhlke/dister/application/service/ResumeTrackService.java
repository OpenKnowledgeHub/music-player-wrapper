package de.jguhlke.dister.application.service;

import de.jguhlke.dister.application.port.in.ResumeTrack;
import de.jguhlke.dister.application.port.out.MusicSystem;
import de.jguhlke.dister.application.port.out.PlayerRepository;
import de.jguhlke.dister.model.Authentication;
import de.jguhlke.dister.model.Player;
import de.jguhlke.dister.model.exception.DisterException;
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
            .orElseThrow(() -> new DisterException("No current player found!"));

    return musicSystem.produceState(player.resume(), authentication);
  }
}
