package de.jguhlke.dister.adapter.in.rest;

import de.jguhlke.dister.application.port.in.PlayTrack;
import de.jguhlke.dister.model.Player;
import de.jguhlke.dister.model.TokenAuthentication;
import de.jguhlke.dister.model.id.TrackId;
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
    Objects.requireNonNull(requestBody.trackId(), "'trackId' must be set");
    Objects.requireNonNull(requestBody.token(), "'token' must be set");

    return playTrack.play(
        new TrackId(requestBody.trackId()), new TokenAuthentication(requestBody.token()));
  }
}
