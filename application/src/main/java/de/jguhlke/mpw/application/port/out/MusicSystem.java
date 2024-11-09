package de.jguhlke.mpw.application.port.out;

import de.jguhlke.mpw.application.port.in.ExchangeTokenRequest;
import de.jguhlke.mpw.application.port.in.ExchangeTokenResponse;
import de.jguhlke.mpw.model.Authentication;
import de.jguhlke.mpw.model.Player;

public interface MusicSystem {
  ExchangeTokenResponse exchangeToken(ExchangeTokenRequest exchangeTokenRequest);

  Player produceState(Player player, Authentication authentication);
}
