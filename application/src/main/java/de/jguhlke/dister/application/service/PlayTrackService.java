package de.jguhlke.dister.application.service;

import de.jguhlke.dister.application.port.in.PlayTrack;
import de.jguhlke.dister.application.port.out.MusicSystem;
import de.jguhlke.dister.application.port.out.PlayerRepository;
import de.jguhlke.dister.application.port.out.TrackRepository;
import de.jguhlke.dister.model.Authentication;
import de.jguhlke.dister.model.Player;
import de.jguhlke.dister.model.Track;
import de.jguhlke.dister.model.exception.DisterException;
import de.jguhlke.dister.model.id.TrackId;
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
            .orElseThrow(() -> new DisterException("No current player found!"));

    Track track =
        trackRepository
            .findTrackById(trackId, authentication)
            .orElseThrow(
                () -> new DisterException(String.format("No track for id (%s) found!", trackId)));

    return musicSystem.produceState(player.play(track), authentication);
  }
}
