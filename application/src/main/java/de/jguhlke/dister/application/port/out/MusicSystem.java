package de.jguhlke.dister.application.port.out;

import de.jguhlke.dister.model.Authentication;
import de.jguhlke.dister.model.Player;

public interface MusicSystem {
  Player produceState(Player player, Authentication authentication);
}
