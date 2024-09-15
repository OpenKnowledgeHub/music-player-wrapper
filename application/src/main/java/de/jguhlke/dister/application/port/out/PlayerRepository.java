package de.jguhlke.dister.application.port.out;

import de.jguhlke.dister.model.Authentication;
import de.jguhlke.dister.model.Player;

import java.util.Optional;

public interface PlayerRepository {
    Optional<Player> fetchCurrentPlayer(Authentication authentication);
}
