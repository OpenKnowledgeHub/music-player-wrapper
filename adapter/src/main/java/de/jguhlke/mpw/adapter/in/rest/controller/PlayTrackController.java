package de.jguhlke.mpw.adapter.in.rest.controller;

import de.jguhlke.mpw.adapter.in.rest.model.PlayTrackPostRequestBody;
import de.jguhlke.mpw.application.exception.InvalidClientInputException;
import de.jguhlke.mpw.application.exception.AuthenticationException;
import de.jguhlke.mpw.application.port.in.PlayTrack;
import de.jguhlke.mpw.model.Player;
import de.jguhlke.mpw.model.TokenAuthentication;
import de.jguhlke.mpw.model.id.TrackId;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.Objects;

@Path("/play")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PlayTrackController {

  private final PlayTrack playTrack;

  public PlayTrackController(PlayTrack playTrack) {
    this.playTrack = playTrack;
  }

  @POST
  public Player playTrack(PlayTrackPostRequestBody requestBody) {
    if (Objects.isNull(requestBody.token()) || requestBody.token().isBlank()) {
      throw new AuthenticationException();
    }

    if (Objects.isNull(requestBody.trackId()) || requestBody.trackId().isBlank()) {
      throw new InvalidClientInputException("'trackId' must be set");
    }

    return playTrack.play(
        new TrackId(requestBody.trackId()), new TokenAuthentication(requestBody.token()));
  }
}
