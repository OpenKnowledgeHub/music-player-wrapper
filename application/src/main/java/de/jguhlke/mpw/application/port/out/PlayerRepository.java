package de.jguhlke.mpw.application.port.out;

import de.jguhlke.mpw.model.Authentication;
import de.jguhlke.mpw.model.Player;
import java.util.Optional;

public interface PlayerRepository {
  Optional<Player> fetchCurrentPlayer(Authentication authentication);
}
