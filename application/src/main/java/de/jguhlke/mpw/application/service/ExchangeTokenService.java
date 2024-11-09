package de.jguhlke.mpw.application.service;

import de.jguhlke.mpw.application.port.in.ExchangeToken;
import de.jguhlke.mpw.application.port.in.ExchangeTokenRequest;
import de.jguhlke.mpw.application.port.in.ExchangeTokenResponse;
import de.jguhlke.mpw.application.port.out.MusicSystem;
import java.util.Objects;

public class ExchangeTokenService implements ExchangeToken {

  private final MusicSystem musicSystem;

  public ExchangeTokenService(MusicSystem musicSystem) {
    this.musicSystem = musicSystem;
  }

  @Override
  public ExchangeTokenResponse exchangeToken(ExchangeTokenRequest exchangeTokenRequest) {
    Objects.requireNonNull(exchangeTokenRequest, "'exchangeTokenRequest' must be set");

    return musicSystem.exchangeToken(exchangeTokenRequest);
  }
}
