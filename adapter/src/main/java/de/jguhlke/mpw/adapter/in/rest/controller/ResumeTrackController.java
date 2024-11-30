package de.jguhlke.mpw.adapter.in.rest.controller;

import de.jguhlke.mpw.adapter.in.rest.model.TokenPostRequestBody;
import de.jguhlke.mpw.application.exception.AuthenticationException;
import de.jguhlke.mpw.application.port.in.ResumeTrack;
import de.jguhlke.mpw.model.Player;
import de.jguhlke.mpw.model.TokenAuthentication;
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
    if (Objects.isNull(requestBody.token()) || requestBody.token().isBlank()) {
      throw new AuthenticationException();
    }

    return resumeTrack.resume(new TokenAuthentication(requestBody.token()));
  }
}
