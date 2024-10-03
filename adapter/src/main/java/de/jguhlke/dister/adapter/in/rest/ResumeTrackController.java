package de.jguhlke.dister.adapter.in.rest;

import de.jguhlke.dister.application.port.in.ResumeTrack;
import de.jguhlke.dister.model.Player;
import de.jguhlke.dister.model.TokenAuthentication;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.Objects;

@Path("/resume")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ResumeTrackController {

  private final ResumeTrack resumeTrack;

  public ResumeTrackController(ResumeTrack resumeTrack) {
    this.resumeTrack = resumeTrack;
  }

  @POST
  public Player playTrack(TokenPostRequestBody requestBody) {
    Objects.requireNonNull(requestBody.token(), "'token' must be set");

    return resumeTrack.resume(new TokenAuthentication(requestBody.token()));
  }
}
