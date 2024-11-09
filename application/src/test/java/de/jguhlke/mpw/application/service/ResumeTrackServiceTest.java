package de.jguhlke.mpw.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import de.jguhlke.mpw.application.port.out.MusicSystem;
import de.jguhlke.mpw.application.port.out.PlayerRepository;
import de.jguhlke.mpw.model.Authentication;
import de.jguhlke.mpw.model.Device;
import de.jguhlke.mpw.model.Player;
import de.jguhlke.mpw.model.TokenAuthentication;
import de.jguhlke.mpw.model.Track;
import de.jguhlke.mpw.model.exception.MusicPlayerWrapperException;
import de.jguhlke.mpw.model.id.DeviceId;
import de.jguhlke.mpw.model.id.TrackId;
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
  final Authentication testAuthentication = new TokenAuthentication("123456789");
  final Device testDevice = new Device(new DeviceId("device:1234"), "Device", true);
  final Track testTrack = new Track(testTrackId, "Track");

  @Test
  @DisplayName("Should resume a track on player by id")
  public void testResumeOnPlayer() {
    final var player = new Player(false, false, testDevice, testTrack);

    doReturn(Optional.of(player)).when(playerRepository).fetchCurrentPlayer(testAuthentication);

    doAnswer(it -> it.getArguments()[0])
        .when(musicSystem)
        .produceState(any(Player.class), any(Authentication.class));

    final var resumedPlayer = underTest.resume(testAuthentication);

    assertThat(resumedPlayer).isNotNull();
    assertThat(resumedPlayer.playing()).isTrue();
    assertThat(resumedPlayer.resumed()).isTrue();
    assertThat(resumedPlayer.currentTrack().id()).isEqualTo(testTrackId);

    verify(playerRepository, times(1)).fetchCurrentPlayer(testAuthentication);
    verify(musicSystem, times(1)).produceState(any(Player.class), any(Authentication.class));
    verifyNoMoreInteractions(musicSystem, playerRepository);
  }

  @Test
  @DisplayName("Should not resume a track on player if player not found")
  public void testResumeOnPlayerNotFoundPlayer() {
    doReturn(Optional.empty()).when(playerRepository).fetchCurrentPlayer(testAuthentication);

    assertThatThrownBy(() -> underTest.resume(testAuthentication))
        .isInstanceOf(MusicPlayerWrapperException.class)
        .hasMessage("No current player found!");

    verify(playerRepository, times(1)).fetchCurrentPlayer(testAuthentication);
    verifyNoMoreInteractions(musicSystem, playerRepository);
  }

  @Test
  @DisplayName("Should not resume a track on player if authentication is null")
  public void testResumeOnPlayerWithoutAuthentication() {
    assertThatThrownBy(() -> underTest.resume(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("'authentication' must be set");

    verifyNoInteractions(musicSystem);
  }
}
