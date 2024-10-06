package de.jguhlke.dister.application.port.in;

public interface ExchangeToken {
  ExchangeTokenResponse exchangeToken(ExchangeTokenRequest exchangeTokenRequest);
}
