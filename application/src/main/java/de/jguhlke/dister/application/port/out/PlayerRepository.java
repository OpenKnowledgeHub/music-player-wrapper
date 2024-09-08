package de.jguhlke.dister.application.port.out;

import de.jguhlke.dister.model.Authentication;
import de.jguhlke.dister.model.Player;
import de.jguhlke.dister.model.id.PlayerId;

import java.util.Optional;

public interface PlayerRepository {
    Optional<Player> findPlayerById(PlayerId playerId, Authentication authentication);
}
