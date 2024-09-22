package de.jguhlke.dister.adapter.out.spotify;

import de.jguhlke.dister.application.port.out.MusicSystem;
import de.jguhlke.dister.model.Authentication;
import de.jguhlke.dister.model.Player;
import java.util.Objects;

public class SpotifyMusicSystem implements MusicSystem {

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
