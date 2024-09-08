package de.jguhlke.dister.application.service;

import de.jguhlke.dister.application.port.in.PlayTrackOnPlayer;
import de.jguhlke.dister.application.port.out.MusicSystem;
import de.jguhlke.dister.application.port.out.PlayerRepository;
import de.jguhlke.dister.application.port.out.TrackRepository;
import de.jguhlke.dister.model.Authentication;
import de.jguhlke.dister.model.Player;
import de.jguhlke.dister.model.Track;
import de.jguhlke.dister.model.exception.DisterException;
import de.jguhlke.dister.model.id.PlayerId;
import de.jguhlke.dister.model.id.TrackId;
import java.util.Objects;

public class PlayTrackOnPlayerService implements PlayTrackOnPlayer {

  private final MusicSystem musicSystem;
  private final PlayerRepository playerRepository;
  private final TrackRepository trackRepository;

  public PlayTrackOnPlayerService(
      MusicSystem musicSystem, PlayerRepository playerRepository, TrackRepository trackRepository) {
    this.musicSystem = musicSystem;
    this.playerRepository = playerRepository;
    this.trackRepository = trackRepository;
  }

  @Override
  public Player playTrackOn(TrackId trackId, PlayerId playerId, Authentication authentication) {
    Objects.requireNonNull(trackId, "'trackId' must be set");
    Objects.requireNonNull(playerId, "'playerId' must be set");
    Objects.requireNonNull(authentication, "'authentication' must be set");

    Player player =
        playerRepository
            .findPlayerById(playerId, authentication)
            .orElseThrow(
                () -> new DisterException(String.format("No player for id (%s) found!", playerId)));

    Track track =
        trackRepository
            .findTrackById(trackId, authentication)
            .orElseThrow(
                () -> new DisterException(String.format("No track for id (%s) found!", trackId)));

    return musicSystem.produceState(player.play(track), authentication);
  }
}
