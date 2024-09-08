package de.jguhlke.dister.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import de.jguhlke.dister.application.port.out.MusicSystem;
import de.jguhlke.dister.application.port.out.PlayerRepository;
import de.jguhlke.dister.application.port.out.TrackRepository;
import de.jguhlke.dister.model.Authentication;
import de.jguhlke.dister.model.Device;
import de.jguhlke.dister.model.Player;
import de.jguhlke.dister.model.TokenAuthentication;
import de.jguhlke.dister.model.Track;
import de.jguhlke.dister.model.exception.DisterException;
import de.jguhlke.dister.model.id.DeviceId;
import de.jguhlke.dister.model.id.PlayerId;
import de.jguhlke.dister.model.id.TrackId;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Test PlayTrackOnPlayerService")
@ExtendWith(MockitoExtension.class)
class PlayTrackOnPlayerServiceTest {
  @Mock MusicSystem musicSystem;

  @Mock PlayerRepository playerRepository;

  @Mock TrackRepository trackRepository;

  @InjectMocks PlayTrackOnPlayerService underTest;

  final TrackId testTrackId = new TrackId("track:1234");
  final PlayerId testPlayerId = new PlayerId("player:1234");
  final Authentication testAuthentication = new TokenAuthentication("123456789");
  final Device testDevice = new Device(new DeviceId("device:1234"), "Device", true);
  final Track testTrack = new Track(testTrackId, "Track");

  @Test
  @DisplayName("Should play a track on player by id")
  public void testPlayTrackOnPlayer() {
    final var trackId = new TrackId("track:5678");
    final var newTrack = new Track(trackId, "Track");
    final var player = new Player(testPlayerId, "Player", false, testDevice, testTrack);

    doReturn(Optional.of(player))
        .when(playerRepository)
        .findPlayerById(testPlayerId, testAuthentication);

    doReturn(Optional.of(newTrack))
        .when(trackRepository)
        .findTrackById(trackId, testAuthentication);

    doAnswer(it -> it.getArguments()[0])
        .when(musicSystem)
        .produceState(any(Player.class), any(Authentication.class));

    final var playingPlayer = underTest.playTrackOn(trackId, testPlayerId, testAuthentication);

    assertThat(playingPlayer).isNotNull();
    assertThat(playingPlayer.playing()).isTrue();
    assertThat(playingPlayer.currentTrack()).isEqualTo(newTrack);
    assertThat(playingPlayer).isEqualTo(player);

    verify(playerRepository, times(1)).findPlayerById(testPlayerId, testAuthentication);
    verify(trackRepository, times(1)).findTrackById(trackId, testAuthentication);
    verify(musicSystem, times(1)).produceState(any(Player.class), any(Authentication.class));
    verifyNoMoreInteractions(musicSystem, playerRepository, trackRepository);
  }

  @Test
  @DisplayName("Should not play a track on player if player not found")
  public void testPlayTrackOnPlayerNotFoundPlayer() {
    final var trackId = new TrackId("track:5678");

    doReturn(Optional.empty())
        .when(playerRepository)
        .findPlayerById(testPlayerId, testAuthentication);

    assertThatThrownBy(() -> underTest.playTrackOn(trackId, testPlayerId, testAuthentication))
        .isInstanceOf(DisterException.class)
        .hasMessage(String.format("No player for id (%s) found!", testPlayerId));

    verify(playerRepository, times(1)).findPlayerById(testPlayerId, testAuthentication);
    verifyNoMoreInteractions(musicSystem, playerRepository, trackRepository);
  }

  @Test
  @DisplayName("Should not play a track on player if track not found")
  public void testPlayTrackOnPlayerNotFoundTrack() {
    final var trackId = new TrackId("track:5678");
    final var player = new Player(testPlayerId, "Player", false, testDevice, testTrack);

    doReturn(Optional.of(player))
        .when(playerRepository)
        .findPlayerById(testPlayerId, testAuthentication);

    doReturn(Optional.empty()).when(trackRepository).findTrackById(trackId, testAuthentication);

    assertThatThrownBy(() -> underTest.playTrackOn(trackId, testPlayerId, testAuthentication))
        .isInstanceOf(DisterException.class)
        .hasMessage(String.format("No track for id (%s) found!", trackId));

    verify(playerRepository, times(1)).findPlayerById(testPlayerId, testAuthentication);
    verify(trackRepository, times(1)).findTrackById(trackId, testAuthentication);
    verifyNoMoreInteractions(musicSystem, playerRepository, trackRepository);
  }

  @Test
  @DisplayName("Should not play a track on player if trackId is null")
  public void testPlayTrackOnPlayerWithoutTrackId() {
    assertThatThrownBy(() -> underTest.playTrackOn(null, testPlayerId, testAuthentication))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("'trackId' must be set");

    verifyNoInteractions(musicSystem);
  }

  @Test
  @DisplayName("Should not play a track on player if playerId is null")
  public void testPlayTrackOnPlayerWithoutPlayerId() {
    assertThatThrownBy(() -> underTest.playTrackOn(testTrackId, null, testAuthentication))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("'playerId' must be set");

    verifyNoInteractions(musicSystem);
  }

  @Test
  @DisplayName("Should not play a track on player if authentication is null")
  public void testPlayTrackOnPlayerWithoutAuthentication() {
    assertThatThrownBy(() -> underTest.playTrackOn(testTrackId, testPlayerId, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("'authentication' must be set");

    verifyNoInteractions(musicSystem);
  }
}
