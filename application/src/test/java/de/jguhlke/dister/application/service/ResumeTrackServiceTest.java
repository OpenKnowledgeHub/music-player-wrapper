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

@DisplayName("Test ResumeTrackService")
@ExtendWith(MockitoExtension.class)
class ResumeTrackServiceTest {
  @Mock MusicSystem musicSystem;

  @Mock PlayerRepository playerRepository;

  @InjectMocks ResumeTrackService underTest;

  final TrackId testTrackId = new TrackId("track:1234");
  final PlayerId testPlayerId = new PlayerId("player:1234");
  final Authentication testAuthentication = new TokenAuthentication("123456789");
  final Device testDevice = new Device(new DeviceId("device:1234"), "Device", true);
  final Track testTrack = new Track(testTrackId, "Track");

  @Test
  @DisplayName("Should resume a track on player by id")
  public void testResumeTrackOnPlayer() {
    final var player = new Player(testPlayerId, "Player", false, testDevice, testTrack);

    doReturn(Optional.of(player))
        .when(playerRepository)
        .findPlayerById(testPlayerId, testAuthentication);

    doAnswer(it -> it.getArguments()[0])
        .when(musicSystem)
        .produceState(any(Player.class), any(Authentication.class));

    final var pausedPlayer = underTest.resumeTrack(testPlayerId, testAuthentication);

    assertThat(pausedPlayer).isNotNull();
    assertThat(pausedPlayer.playing()).isTrue();
    assertThat(pausedPlayer.currentTrack().id()).isEqualTo(testTrackId);
    assertThat(pausedPlayer.id()).isEqualTo(testPlayerId);

    verify(playerRepository, times(1)).findPlayerById(testPlayerId, testAuthentication);
    verify(musicSystem, times(1)).produceState(any(Player.class), any(Authentication.class));
    verifyNoMoreInteractions(musicSystem, playerRepository);
  }

  @Test
  @DisplayName("Should not resume a track on player if player not found")
  public void testResumeTrackOnPlayerNotFoundPlayer() {
    doReturn(Optional.empty())
        .when(playerRepository)
        .findPlayerById(testPlayerId, testAuthentication);

    assertThatThrownBy(() -> underTest.resumeTrack(testPlayerId, testAuthentication))
        .isInstanceOf(DisterException.class)
        .hasMessage(String.format("No player for id (%s) found!", testPlayerId));

    verify(playerRepository, times(1)).findPlayerById(testPlayerId, testAuthentication);
    verifyNoMoreInteractions(musicSystem, playerRepository);
  }

  @Test
  @DisplayName("Should not resume a track on player if playerId is null")
  public void testResumeTrackOnPlayerWithoutTrackId() {
    assertThatThrownBy(() -> underTest.resumeTrack(null, testAuthentication))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("'playerId' must be set");

    verifyNoInteractions(musicSystem);
  }

  @Test
  @DisplayName("Should not resume a track on player if authentication is null")
  public void testResumeTrackOnPlayerWithoutAuthentication() {
    assertThatThrownBy(() -> underTest.resumeTrack(testPlayerId, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("'authentication' must be set");

    verifyNoInteractions(musicSystem);
  }
}
