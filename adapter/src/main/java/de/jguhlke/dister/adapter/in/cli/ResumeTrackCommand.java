package de.jguhlke.dister.adapter.in.cli;

import de.jguhlke.dister.application.port.in.ResumeTrack;
import de.jguhlke.dister.model.Player;
import de.jguhlke.dister.model.TokenAuthentication;
import de.jguhlke.dister.model.exception.DisterException;
import java.util.Objects;
import java.util.concurrent.Callable;
import picocli.CommandLine;

@CommandLine.Command(
    name = "resume",
    mixinStandardHelpOptions = true,
    description = "Resume last played track on the current player")
public class ResumeTrackCommand implements Callable<Player> {

  private final ResumeTrack resumeTrack;

  @CommandLine.Option(
      names = {"-a", "--auth"},
      description = "the authentication string",
      required = true)
  private String authentication;

  public ResumeTrackCommand(ResumeTrack pauseTrack) {
    this.resumeTrack = pauseTrack;
  }

  public ResumeTrackCommand(ResumeTrack resumeTrack, String authentication) {
    this(resumeTrack);
    this.authentication = authentication;
  }

  @Override
  public Player call() {
    Objects.requireNonNull(authentication, "'authentication' must be set");

    if (authentication.isBlank()) {
      throw new DisterException("'authentication' must not be blank");
    }

    return resumeTrack.resume(new TokenAuthentication(authentication));
  }
}
