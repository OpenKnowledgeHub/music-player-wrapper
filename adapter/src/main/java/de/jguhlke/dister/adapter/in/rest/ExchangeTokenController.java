package de.jguhlke.dister.adapter.in.rest;

import de.jguhlke.dister.application.port.in.ExchangeToken;
import de.jguhlke.dister.application.port.in.ExchangeTokenRequest;
import de.jguhlke.dister.application.port.in.ExchangeTokenResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.Objects;

@Path("/token")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ExchangeTokenController {

  private final ExchangeToken exchangeToken;

  public ExchangeTokenController(ExchangeToken exchangeToken) {
    this.exchangeToken = exchangeToken;
  }

  @POST
  public ExchangeTokenResponse exchangeToken(ExchangeTokenRequest exchangeTokenRequest) {
    Objects.requireNonNull(exchangeTokenRequest.code(), "'code' must be set");

    return exchangeToken.exchangeToken(exchangeTokenRequest);
  }
}
