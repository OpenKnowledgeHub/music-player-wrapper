package de.jguhlke.mpw.adapter.in.rest.controller;

import de.jguhlke.mpw.application.exception.InvalidClientInputException;
import de.jguhlke.mpw.application.port.in.ExchangeToken;
import de.jguhlke.mpw.application.port.in.ExchangeTokenRequest;
import de.jguhlke.mpw.application.port.in.ExchangeTokenResponse;
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
    if (Objects.isNull(exchangeTokenRequest.code()) || exchangeTokenRequest.code().isBlank()) {
      throw new InvalidClientInputException("'code' must be set");
    }

    return exchangeToken.exchangeToken(exchangeTokenRequest);
  }
}
