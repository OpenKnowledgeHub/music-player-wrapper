package de.jguhlke.dister.adapter.in.cli;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import de.jguhlke.dister.application.port.in.PauseTrack;
import de.jguhlke.dister.model.Authentication;
import de.jguhlke.dister.model.Player;
import de.jguhlke.dister.model.exception.DisterException;
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
@DisplayName("Test PauseTrack command line command")
class PauseTrackCommandTest {

  @Mock PauseTrack pauseTrack;

  @Mock Player dummyPlayer;

  final String testAuthentication = "auth:1234";
  private ArgumentMatcher<Authentication> authenticationMatcher;

  @BeforeEach
  public void setup() {
    authenticationMatcher =
        authentication -> Objects.equals(authentication.getAuthentication(), testAuthentication);
  }

  @Test
  @DisplayName("Should run pause with authentication")
  void testRunPause() {
    final var underTest = new PauseTrackCommand(pauseTrack, testAuthentication);
    doReturn(dummyPlayer).when(pauseTrack).pause(argThat(authenticationMatcher));

    final var runningPlayer = underTest.call();

    verify(pauseTrack, times(1)).pause(argThat(authenticationMatcher));
    verifyNoMoreInteractions(pauseTrack);
    assertThat(runningPlayer).isEqualTo(dummyPlayer);
  }

  @Test
  @DisplayName("Should not run pause without authentication")
  void testRunPauseWithoutAuthentication() {
    final var underTest = new PauseTrackCommand(pauseTrack, null);

    Assertions.assertThatThrownBy(underTest::call)
        .isInstanceOf(NullPointerException.class)
        .hasMessage("'authentication' must be set");

    verifyNoInteractions(pauseTrack);
  }

  @Test
  @DisplayName("Should not run pause without authentication content")
  void testRunPauseWithoutAuthenticationContent() {
    final var underTest = new PauseTrackCommand(pauseTrack, "");

    Assertions.assertThatThrownBy(underTest::call)
        .isInstanceOf(DisterException.class)
        .hasMessage("'authentication' must not be blank");

    verifyNoInteractions(pauseTrack);
  }
}
