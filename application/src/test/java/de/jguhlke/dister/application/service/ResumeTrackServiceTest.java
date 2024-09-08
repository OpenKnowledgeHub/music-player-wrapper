package de.jguhlke.dister.application.service;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@DisplayName("Test ResumeTrackService")
@ExtendWith(MockitoExtension.class)
class ResumeTrackServiceTest {
  @Mock MusicSystemPort musicSystemPort;

  @InjectMocks ResumeTrackService underTest;

  final TrackId testTrackId = new TrackId("track:1234");
  final PlayerId testPlayerId = new PlayerId("player:1234");
  final Authentication testAuthentication = new TokenAuthentication("123456789");

  @Test
  @DisplayName("Should resume a track on player by id")
  public void testResumeTrackOnPlayer() {
    doReturn(
            new Player(
                    testPlayerId,
                    "Player",
                    true,
                    new Device(new DeviceId("device:1234"), "Device", true),
                    new Track(testTrackId, "Track")))
            .when(musicSystemPort)
            .resumeTrackById(testPlayerId, testAuthentication);

    final var playingPlayer = underTest.resumeTrack(testPlayerId, testAuthentication);

    assertThat(playingPlayer).isNotNull();
    assertThat(playingPlayer.playing()).isTrue();
    assertThat(playingPlayer.currentTrack().id()).isEqualTo(testTrackId);
    assertThat(playingPlayer.id()).isEqualTo(testPlayerId);

    verify(musicSystemPort).resumeTrackById(testPlayerId, testAuthentication);
    verifyNoMoreInteractions(musicSystemPort);
  }

  @Test
  @DisplayName("Should not resume a track on player if playerId is null")
  public void testResumeTrackOnPlayerWithoutTrackId() {
    assertThatThrownBy(() -> underTest.resumeTrack(null, testAuthentication))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("'playerId' must be set");

    verifyNoInteractions(musicSystemPort);
  }

  @Test
  @DisplayName("Should not resume a track on player if authentication is null")
  public void testResumeTrackOnPlayerWithoutAuthentication() {
    assertThatThrownBy(() -> underTest.resumeTrack(testPlayerId, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("'authentication' must be set");

    verifyNoInteractions(musicSystemPort);
  }
}
