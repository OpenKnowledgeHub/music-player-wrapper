package de.jguhlke.mpw.adapter.in.rest;

import de.jguhlke.mpw.application.port.in.PauseTrack;
import de.jguhlke.mpw.model.Player;
import de.jguhlke.mpw.model.TokenAuthentication;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.Objects;

@Path("/pause")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PauseTrackController {

  private final PauseTrack pauseTrack;

  public PauseTrackController(PauseTrack pauseTrack) {
    this.pauseTrack = pauseTrack;
  }

  @POST
  public Player pauseTrack(TokenPostRequestBody requestBody) {
    Objects.requireNonNull(requestBody.token(), "'token' must be set");

    return pauseTrack.pause(new TokenAuthentication(requestBody.token()));
  }
}
