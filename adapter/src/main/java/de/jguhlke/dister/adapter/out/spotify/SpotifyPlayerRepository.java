package de.jguhlke.dister.adapter.out.spotify;

import de.jguhlke.dister.application.port.out.PlayerRepository;
import de.jguhlke.dister.model.Authentication;
import de.jguhlke.dister.model.Player;

import java.util.Objects;
import java.util.Optional;

public class SpotifyPlayerRepository implements PlayerRepository {

  @Override
  public Optional<Player> fetchCurrentPlayer(Authentication authentication) {
    Objects.requireNonNull(authentication, "'authentication' must be set");

    return new SpotifyClient(authentication).fetchCurrentPlayer();
  }
}
