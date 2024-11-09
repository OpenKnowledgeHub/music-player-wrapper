package de.jguhlke.mpw.application.service;

import de.jguhlke.mpw.application.port.in.PlayTrack;
import de.jguhlke.mpw.application.port.out.MusicSystem;
import de.jguhlke.mpw.application.port.out.PlayerRepository;
import de.jguhlke.mpw.application.port.out.TrackRepository;
import de.jguhlke.mpw.model.Authentication;
import de.jguhlke.mpw.model.Player;
import de.jguhlke.mpw.model.Track;
import de.jguhlke.mpw.model.exception.MusicPlayerWrapperException;
import de.jguhlke.mpw.model.id.TrackId;
import java.util.Objects;

public class PlayTrackService implements PlayTrack {

  private final MusicSystem musicSystem;
  private final PlayerRepository playerRepository;
  private final TrackRepository trackRepository;

  public PlayTrackService(
      MusicSystem musicSystem, PlayerRepository playerRepository, TrackRepository trackRepository) {
    this.musicSystem = musicSystem;
    this.playerRepository = playerRepository;
    this.trackRepository = trackRepository;
  }

  @Override
  public Player play(TrackId trackId, Authentication authentication) {
    Objects.requireNonNull(trackId, "'trackId' must be set");
    Objects.requireNonNull(authentication, "'authentication' must be set");

    Player player =
        playerRepository
            .fetchCurrentPlayer(authentication)
            .orElseThrow(() -> new MusicPlayerWrapperException("No current player found!"));

    Track track =
        trackRepository
            .findTrackById(trackId, authentication)
            .orElseThrow(
                () ->
                    new MusicPlayerWrapperException(
                        String.format("No track for id (%s) found!", trackId)));

    return musicSystem.produceState(player.play(track), authentication);
  }
}
