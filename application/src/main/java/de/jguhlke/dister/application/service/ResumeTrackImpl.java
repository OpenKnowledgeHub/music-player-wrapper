package de.jguhlke.dister.application.service;

import de.jguhlke.dister.application.port.in.ResumeTrack;
import de.jguhlke.dister.application.port.out.MusicSystemPort;
import de.jguhlke.dister.model.Player;

import java.util.Objects;

public class ResumeTrackImpl implements ResumeTrack {

  private final MusicSystemPort musicSystemPort;

  public ResumeTrackImpl(MusicSystemPort musicSystemPort) {
    this.musicSystemPort = musicSystemPort;
  }

  @Override
  public Player resumeTrack(Player player) {
    Objects.requireNonNull(player, "player must be set");
    return musicSystemPort.resumeTrack(player);
  }
}
