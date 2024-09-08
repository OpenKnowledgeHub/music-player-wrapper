package de.jguhlke.dister.model;

import de.jguhlke.dister.model.exception.DisterException;
import de.jguhlke.dister.model.id.TrackId;

import java.util.Objects;

public record Track(TrackId id, String name) {
  public Track {
    Objects.requireNonNull(id, "'id' must be set");
    Objects.requireNonNull(name, "'name' must be set");

    if (name.isBlank()) {
      throw new DisterException("'name' must not be blank");
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Track track = (Track) o;
    return Objects.equals(id, track.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
