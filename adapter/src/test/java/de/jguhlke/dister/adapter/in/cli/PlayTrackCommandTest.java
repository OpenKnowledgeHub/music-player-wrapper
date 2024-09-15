package de.jguhlke.dister.adapter.in.cli;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import de.jguhlke.dister.application.port.in.PlayTrack;
import de.jguhlke.dister.model.Authentication;
import de.jguhlke.dister.model.Player;
import de.jguhlke.dister.model.exception.DisterException;
import de.jguhlke.dister.model.id.TrackId;
import java.util.Objects;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test PlayTrack command line command")
class PlayTrackCommandTest {

  @Mock PlayTrack playTrack;

  @Mock Player dummyPlayer;

  final String testTrackId = "track:1234";
  final String testAuthentication = "auth:1234";
  private ArgumentMatcher<TrackId> trackIdMatcher;
  private ArgumentMatcher<Authentication> authenticationMatcher;

  @BeforeEach
  public void setup() {
    trackIdMatcher = trackId -> Objects.equals(trackId.id(), testTrackId);
    authenticationMatcher =
        authentication -> Objects.equals(authentication.getAuthentication(), testAuthentication);
  }

  @Test
  @DisplayName("Should run play with trackId and authentication")
  void testRunPlay() {
    final var underTest = new PlayTrackCommand(playTrack, testAuthentication, testTrackId);
    doReturn(dummyPlayer)
        .when(playTrack)
        .play(argThat(trackIdMatcher), argThat(authenticationMatcher));

    final var runningPlayer = underTest.call();

    verify(playTrack, times(1)).play(argThat(trackIdMatcher), argThat(authenticationMatcher));
    verifyNoMoreInteractions(playTrack);
    assertThat(runningPlayer).isEqualTo(dummyPlayer);
  }

  @Test
  @DisplayName("Should not run play without authentication")
  void testRunPlayWithoutAuthentication() {
    final var underTest = new PlayTrackCommand(playTrack, null, testTrackId);

    Assertions.assertThatThrownBy(underTest::call)
        .isInstanceOf(NullPointerException.class)
        .hasMessage("'authentication' must be set");

    verifyNoInteractions(playTrack);
  }

  @Test
  @DisplayName("Should not run play without trackId")
  void testRunPlayWithoutTrackId() {
    final var underTest = new PlayTrackCommand(playTrack, testAuthentication, null);

    Assertions.assertThatThrownBy(underTest::call)
        .isInstanceOf(NullPointerException.class)
        .hasMessage("'rawTrackId' must be set");

    verifyNoInteractions(playTrack);
  }

  @Test
  @DisplayName("Should not run play without trackId content")
  void testRunPlayWithoutTrackIdContent() {
    final var underTest = new PlayTrackCommand(playTrack, testAuthentication, "");

    Assertions.assertThatThrownBy(underTest::call)
        .isInstanceOf(DisterException.class)
        .hasMessage("'rawTrackId' must not be blank");

    verifyNoInteractions(playTrack);
  }

  @Test
  @DisplayName("Should not run play without authentication content")
  void testRunPlayWithoutAuthenticationContent() {
    final var underTest = new PlayTrackCommand(playTrack, "", testTrackId);

    Assertions.assertThatThrownBy(underTest::call)
        .isInstanceOf(DisterException.class)
        .hasMessage("'authentication' must not be blank");

    verifyNoInteractions(playTrack);
  }
}
