package de.jguhlke.dister.application.service;

import de.jguhlke.dister.application.port.in.PlayTrackOnPlayer;
import de.jguhlke.dister.application.port.out.MusicSystemPort;
import de.jguhlke.dister.model.Player;
import de.jguhlke.dister.model.Track;

import java.util.Objects;

public class PlayTrackOnPlayerService implements PlayTrackOnPlayer {

    private final MusicSystemPort musicSystemPort;

    public PlayTrackOnPlayerService(MusicSystemPort musicSystemPort) {
        this.musicSystemPort = musicSystemPort;
    }

    @Override
    public Player playTrackOn(Track track, Player player) {
        Objects.requireNonNull(track, "'track' must be set");
        Objects.requireNonNull(player, "'player' must be set");

        return musicSystemPort.playTrackOn(track, player);
    }
}
