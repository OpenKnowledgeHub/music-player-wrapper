package de.jguhlke.dister.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import de.jguhlke.dister.application.port.out.MusicSystemPort;
import de.jguhlke.dister.model.Authentication;
import de.jguhlke.dister.model.Device;
import de.jguhlke.dister.model.Player;
import de.jguhlke.dister.model.TokenAuthentication;
import de.jguhlke.dister.model.Track;
import de.jguhlke.dister.model.id.DeviceId;
import de.jguhlke.dister.model.id.PlayerId;
import de.jguhlke.dister.model.id.TrackId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Test PauseTrackService")
@ExtendWith(MockitoExtension.class)
class PauseTrackServiceTest {

  @Mock MusicSystemPort musicSystemPort;

  @InjectMocks PauseTrackService underTest;

  final TrackId testTrackId = new TrackId("track:1234");
  final PlayerId testPlayerId = new PlayerId("player:1234");
  final Authentication testAuthentication = new TokenAuthentication("123456789");

  @Test
  @DisplayName("Should pause a track on player by id")
  public void testPauseTrackOnPlayer() {
    doReturn(
            new Player(
                testPlayerId,
                "Player",
                false,
                new Device(new DeviceId("device:1234"), "Device", true),
                new Track(testTrackId, "Track")))
        .when(musicSystemPort)
        .pauseTrackById(testPlayerId, testAuthentication);

    final var pausedPlayer = underTest.pauseTrack(testPlayerId, testAuthentication);

    assertThat(pausedPlayer).isNotNull();
    assertThat(pausedPlayer.playing()).isFalse();
    assertThat(pausedPlayer.currentTrack().id()).isEqualTo(testTrackId);
    assertThat(pausedPlayer.id()).isEqualTo(testPlayerId);

    verify(musicSystemPort).pauseTrackById(testPlayerId, testAuthentication);
    verifyNoMoreInteractions(musicSystemPort);
  }

  @Test
  @DisplayName("Should not pause a track on player if playerId is null")
  public void testPauseTrackOnPlayerWithoutTrackId() {
    assertThatThrownBy(() -> underTest.pauseTrack(null, testAuthentication))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("'playerId' must be set");

    verifyNoInteractions(musicSystemPort);
  }

  @Test
  @DisplayName("Should not pause a track on player if authentication is null")
  public void testPauseTrackOnPlayerWithoutAuthentication() {
    assertThatThrownBy(() -> underTest.pauseTrack(testPlayerId, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("'authentication' must be set");

    verifyNoInteractions(musicSystemPort);
  }
}
