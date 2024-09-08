package de.jguhlke.dister.application.service;

import de.jguhlke.dister.application.port.in.PauseTrack;
import de.jguhlke.dister.application.port.out.MusicSystem;
import de.jguhlke.dister.application.port.out.PlayerRepository;
import de.jguhlke.dister.model.Authentication;
import de.jguhlke.dister.model.Player;
import de.jguhlke.dister.model.exception.DisterException;
import de.jguhlke.dister.model.id.PlayerId;
import java.util.Objects;

public class PauseTrackService implements PauseTrack {

  private final MusicSystem musicSystem;
  private final PlayerRepository playerRepository;

  public PauseTrackService(MusicSystem musicSystem, PlayerRepository playerRepository) {
    this.musicSystem = musicSystem;
    this.playerRepository = playerRepository;
  }

  @Override
  public Player pauseTrack(PlayerId playerId, Authentication authentication) {
    Objects.requireNonNull(playerId, "'playerId' must be set");
    Objects.requireNonNull(authentication, "'authentication' must be set");

    Player player =
            playerRepository
                    .findPlayerById(playerId, authentication)
                    .orElseThrow(
                            () -> new DisterException(String.format("No player for id (%s) found!", playerId)));

    return musicSystem.produceState(player.stop(), authentication);
  }
}
