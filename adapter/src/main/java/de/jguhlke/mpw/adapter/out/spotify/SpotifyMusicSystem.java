package de.jguhlke.mpw.adapter.out.spotify;

import de.jguhlke.mpw.application.port.in.ExchangeTokenRequest;
import de.jguhlke.mpw.application.port.in.ExchangeTokenResponse;
import de.jguhlke.mpw.application.port.out.MusicSystem;
import de.jguhlke.mpw.model.Authentication;
import de.jguhlke.mpw.model.Player;
import java.util.Objects;

public class SpotifyMusicSystem implements MusicSystem {

  @Override
  public ExchangeTokenResponse exchangeToken(ExchangeTokenRequest exchangeTokenRequest) {
    SpotifyClient spotifyClient = new SpotifyClient();

    return spotifyClient.exchangeToken(exchangeTokenRequest);
  }

  @Override
  public Player produceState(Player player, Authentication authentication) {
    Objects.requireNonNull(player, "'player' must be set");
    Objects.requireNonNull(authentication, "'authentication' must be set");

    SpotifyClient spotifyClient = new SpotifyClient(authentication);

    if (!player.playing()) {
      spotifyClient.pause();
      return player;
    }

    if (player.resumed()) {
      spotifyClient.resume(player.activeDevice().id());
      return player;
    }

    spotifyClient.play(player.currentTrack().id(), player.activeDevice().id());

    return player;
  }
}
