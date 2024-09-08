package de.jguhlke.dister.model;

import de.jguhlke.dister.model.exception.DisterException;

import java.util.Objects;

public record Track(TrackId id, String name) {
  public Track {
    Objects.requireNonNull(id, "'id' must be set");
    Objects.requireNonNull(name, "'name' must be set");

    if (name.isBlank()) {
      throw new DisterException("'name' must not be blank");
    }
  }
}
