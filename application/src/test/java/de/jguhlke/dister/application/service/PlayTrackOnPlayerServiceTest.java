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

@DisplayName("Test PlayTrackOnPlayerService")
@ExtendWith(MockitoExtension.class)
class PlayTrackOnPlayerServiceTest {
  @Mock MusicSystemPort musicSystemPort;

  @InjectMocks PlayTrackOnPlayerService underTest;

  final TrackId testTrackId = new TrackId("track:1234");
  final PlayerId testPlayerId = new PlayerId("player:1234");
  final Authentication testAuthentication = new TokenAuthentication("123456789");

  @Test
  @DisplayName("Should play a track on player by id")
  public void testPlayTrackOnPlayer() {
    doReturn(
            new Player(
                testPlayerId,
                "Player",
                true,
                new Device(new DeviceId("device:1234"), "Device", true),
                new Track(testTrackId, "Track")))
        .when(musicSystemPort)
        .playTrackById(testTrackId, testPlayerId, testAuthentication);

    final var playingPlayer = underTest.playTrackOn(testTrackId, testPlayerId, testAuthentication);

    assertThat(playingPlayer).isNotNull();
    assertThat(playingPlayer.playing()).isTrue();
    assertThat(playingPlayer.currentTrack().id()).isEqualTo(testTrackId);
    assertThat(playingPlayer.id()).isEqualTo(testPlayerId);

    verify(musicSystemPort).playTrackById(testTrackId, testPlayerId, testAuthentication);
    verifyNoMoreInteractions(musicSystemPort);
  }

  @Test
  @DisplayName("Should not play a track on player if trackId is null")
  public void testPlayTrackOnPlayerWithoutTrackId() {
    assertThatThrownBy(() -> underTest.playTrackOn(null, testPlayerId, testAuthentication))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("'trackId' must be set");

    verifyNoInteractions(musicSystemPort);
  }

  @Test
  @DisplayName("Should not play a track on player if playerId is null")
  public void testPlayTrackOnPlayerWithoutPlayerId() {
    assertThatThrownBy(() -> underTest.playTrackOn(testTrackId, null, testAuthentication))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("'playerId' must be set");

    verifyNoInteractions(musicSystemPort);
  }

  @Test
  @DisplayName("Should not play a track on player if authentication is null")
  public void testPlayTrackOnPlayerWithoutAuthentication() {
    assertThatThrownBy(() -> underTest.playTrackOn(testTrackId, testPlayerId, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("'authentication' must be set");

    verifyNoInteractions(musicSystemPort);
  }
}
