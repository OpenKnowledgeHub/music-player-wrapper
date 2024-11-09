package de.jguhlke.mpw.adapter.out.spotify;

import de.jguhlke.mpw.application.port.out.PlayerRepository;
import de.jguhlke.mpw.model.Authentication;
import de.jguhlke.mpw.model.Player;
import java.util.Objects;
import java.util.Optional;

public class SpotifyPlayerRepository implements PlayerRepository {

  @Override
  public Optional<Player> fetchCurrentPlayer(Authentication authentication) {
    Objects.requireNonNull(authentication, "'authentication' must be set");

    return new SpotifyClient(authentication).fetchCurrentPlayer();
  }
}
