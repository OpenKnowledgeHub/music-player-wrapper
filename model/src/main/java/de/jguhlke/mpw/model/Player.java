package de.jguhlke.mpw.model;

import de.jguhlke.mpw.model.exception.MusicPlayerWrapperException;
import java.util.Objects;

public record Player(boolean playing, boolean resumed, Device activeDevice, Track currentTrack) {

  public Player {
    if (playing && Objects.isNull(activeDevice)) {
      throw new MusicPlayerWrapperException("'playing' cannot be true without active device");
    }

    if (playing && Objects.isNull(currentTrack)) {
      throw new MusicPlayerWrapperException("'playing' cannot be true without current track");
    }
  }

  public Player play(Track track) throws MusicPlayerWrapperException {
    Objects.requireNonNull(track, "Track needs to be set");

    if (Objects.isNull(activeDevice)) {
      throw new MusicPlayerWrapperException(
          "It is not allowed to play a track without an active device");
    }

    return new Player(true, false, activeDevice, track);
  }

  public Player stop() {
    if (Objects.isNull(activeDevice)) {
      throw new MusicPlayerWrapperException(
          "It is not allowed to stop a track without an active device");
    }

    if (Objects.isNull(currentTrack)) {
      throw new MusicPlayerWrapperException(
          "It is not allowed to stop a track without a current track");
    }

    if (!playing) {
      throw new MusicPlayerWrapperException("It is not allowed to stop a not running track");
    }

    return new Player(false, false, activeDevice, currentTrack);
  }

  public Player resume() {
    if (Objects.isNull(activeDevice)) {
      throw new MusicPlayerWrapperException(
          "It is not allowed to resume a track without an active device");
    }

    if (Objects.isNull(currentTrack)) {
      throw new MusicPlayerWrapperException(
          "It is not allowed to resume a track without a current track");
    }

    if (playing) {
      throw new MusicPlayerWrapperException("It is not allowed to resume a running track");
    }

    return new Player(true, true, activeDevice, currentTrack);
  }

  public Player playOn(Device device) {
    if (Objects.isNull(device) && playing) {
      // first stop playing
      return stop().playOn(null);
    }

    return new Player(playing, resumed, device, currentTrack);
  }
}
