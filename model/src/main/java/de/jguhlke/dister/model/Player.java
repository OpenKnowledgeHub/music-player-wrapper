package de.jguhlke.dister.model;

import de.jguhlke.dister.model.exception.DisterException;
import de.jguhlke.dister.model.id.PlayerId;

import java.util.Objects;

public record Player(
        PlayerId id, String name, boolean playing, Device activeDevice, Track currentTrack) {

  public Player {
    Objects.requireNonNull(id, "'id' must be set");
    Objects.requireNonNull(name, "'name' must be set");

    if (name.isBlank()) {
      throw new DisterException("'name' must not be blank");
    }

    if (playing && Objects.isNull(activeDevice)) {
      throw new DisterException("'playing' cannot be true without active device");
    }

    if (playing && Objects.isNull(currentTrack)) {
      throw new DisterException("'playing' cannot be true without current track");
    }
  }

  public Player play(Track track) throws DisterException {
    Objects.requireNonNull(track, "Track needs to be set");

    if (Objects.isNull(activeDevice)) {
      throw new DisterException("It is not allowed to play a track without an active device");
    }

    return new Player(id, name, true, activeDevice, track);
  }

  public Player stop() {
    if (Objects.isNull(activeDevice)) {
      throw new DisterException("It is not allowed to stop a track without an active device");
    }

    if (Objects.isNull(currentTrack)) {
      throw new DisterException("It is not allowed to stop a track without a current track");
    }

    if (!playing) {
      throw new DisterException("It is not allowed to stop a not running track");
    }

    return new Player(id, name, false, activeDevice, currentTrack);
  }

  public Player resume() {
    if (Objects.isNull(activeDevice)) {
      throw new DisterException("It is not allowed to resume a track without an active device");
    }

    if (Objects.isNull(currentTrack)) {
      throw new DisterException("It is not allowed to resume a track without a current track");
    }

    if (playing) {
      throw new DisterException("It is not allowed to resume a running track");
    }

    return new Player(id, name, true, activeDevice, currentTrack);
  }

  public Player playOn(Device device) {
    if (Objects.isNull(device) && playing) {
      // first stop playing
      return stop().playOn(null);
    }

    return new Player(id, name, playing, device, currentTrack);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Player player = (Player) o;
    return Objects.equals(id, player.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
