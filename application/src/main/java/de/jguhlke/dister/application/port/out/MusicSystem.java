package de.jguhlke.dister.application.port.out;

import de.jguhlke.dister.application.port.in.ExchangeTokenRequest;
import de.jguhlke.dister.application.port.in.ExchangeTokenResponse;
import de.jguhlke.dister.model.Authentication;
import de.jguhlke.dister.model.Player;

public interface MusicSystem {
  ExchangeTokenResponse exchangeToken(ExchangeTokenRequest exchangeTokenRequest);

  Player produceState(Player player, Authentication authentication);
}
