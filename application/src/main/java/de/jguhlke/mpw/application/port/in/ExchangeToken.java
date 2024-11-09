package de.jguhlke.mpw.application.port.in;

public interface ExchangeToken {
  ExchangeTokenResponse exchangeToken(ExchangeTokenRequest exchangeTokenRequest);
}
