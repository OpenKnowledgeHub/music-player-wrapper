package de.jguhlke.dister.adapter.in.cli;

import de.jguhlke.dister.application.port.in.PauseTrack;
import de.jguhlke.dister.model.Player;
import de.jguhlke.dister.model.TokenAuthentication;
import de.jguhlke.dister.model.exception.DisterException;
import java.util.Objects;
import java.util.concurrent.Callable;
import picocli.CommandLine;

@CommandLine.Command(
    name = "pause",
    mixinStandardHelpOptions = true,
    description = "Pause running track on the current player")
public class PauseTrackCommand implements Callable<Player> {

  private final PauseTrack pauseTrack;

  @CommandLine.Option(
      names = {"-a", "--auth"},
      description = "the authentication string",
      required = true)
  private String authentication;

  public PauseTrackCommand(PauseTrack pauseTrack) {
    this.pauseTrack = pauseTrack;
  }

  public PauseTrackCommand(PauseTrack pauseTrack, String authentication) {
    this(pauseTrack);
    this.authentication = authentication;
  }

  @Override
  public Player call() {
    Objects.requireNonNull(authentication, "'authentication' must be set");

    if (authentication.isBlank()) {
      throw new DisterException("'authentication' must not be blank");
    }

    return pauseTrack.pause(new TokenAuthentication(authentication));
  }
}
