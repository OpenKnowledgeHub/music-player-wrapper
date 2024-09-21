package de.jguhlke.dister.adapter.in.cli;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import de.jguhlke.dister.application.port.in.ResumeTrack;
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
@DisplayName("Test ResumeTrack command line command")
class ResumeTrackCommandTest {

  @Mock ResumeTrack resumeTrack;

  @Mock Player dummyPlayer;

  final String testAuthentication = "auth:1234";
  private ArgumentMatcher<Authentication> authenticationMatcher;

  @BeforeEach
  public void setup() {
    authenticationMatcher =
        authentication -> Objects.equals(authentication.getAuthentication(), testAuthentication);
  }

  @Test
  @DisplayName("Should run resume with authentication")
  void testRunResume() {
    final var underTest = new ResumeTrackCommand(resumeTrack, testAuthentication);
    doReturn(dummyPlayer).when(resumeTrack).resume(argThat(authenticationMatcher));

    final var runningPlayer = underTest.call();

    verify(resumeTrack, times(1)).resume(argThat(authenticationMatcher));
    verifyNoMoreInteractions(resumeTrack);
    assertThat(runningPlayer).isEqualTo(dummyPlayer);
  }

  @Test
  @DisplayName("Should not run resume without authentication")
  void testRunResumeWithoutAuthentication() {
    final var underTest = new ResumeTrackCommand(resumeTrack, null);

    Assertions.assertThatThrownBy(underTest::call)
        .isInstanceOf(NullPointerException.class)
        .hasMessage("'authentication' must be set");

    verifyNoInteractions(resumeTrack);
  }

  @Test
  @DisplayName("Should not run resume without authentication content")
  void testRunResumeWithoutAuthenticationContent() {
    final var underTest = new ResumeTrackCommand(resumeTrack, "");

    Assertions.assertThatThrownBy(underTest::call)
        .isInstanceOf(DisterException.class)
        .hasMessage("'authentication' must not be blank");

    verifyNoInteractions(resumeTrack);
  }
}
