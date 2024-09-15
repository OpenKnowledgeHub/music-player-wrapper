package de.jguhlke.dister.adapter.in.cli;

import de.jguhlke.dister.application.port.in.PlayTrack;
import de.jguhlke.dister.model.Player;
import de.jguhlke.dister.model.TokenAuthentication;
import de.jguhlke.dister.model.exception.DisterException;
import de.jguhlke.dister.model.id.TrackId;
import java.util.Objects;
import java.util.concurrent.Callable;
import picocli.CommandLine;

@CommandLine.Command(
    name = "play",
    mixinStandardHelpOptions = true,
    description = "Plays a track with given id on the current player")
public class PlayTrackCommand implements Callable<Player> {

  private final PlayTrack playTrack;

  @CommandLine.Option(
      names = {"-a", "--auth"},
      description = "the authentication string",
      required = true)
  private String authentication;

  @CommandLine.Option(
      names = {"-t", "--trackId"},
      description = "the id of the track",
      required = true)
  private String rawTrackId;

  public PlayTrackCommand(PlayTrack playTrack) {
    this.playTrack = playTrack;
  }

  PlayTrackCommand(PlayTrack playTrack, String authentication, String rawTrackId) {
    this(playTrack);
    this.authentication = authentication;
    this.rawTrackId = rawTrackId;
  }

  @Override
  public Player call() {
    Objects.requireNonNull(authentication, "'authentication' must be set");
    Objects.requireNonNull(rawTrackId, "'rawTrackId' must be set");

    if (rawTrackId.isBlank()) {
      throw new DisterException("'rawTrackId' must not be blank");
    }

    if (authentication.isBlank()) {
      throw new DisterException("'authentication' must not be blank");
    }

    return playTrack.play(new TrackId(rawTrackId), new TokenAuthentication(authentication));
  }
}
