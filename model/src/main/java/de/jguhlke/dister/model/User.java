package de.jguhlke.dister.model;

import de.jguhlke.dister.model.exception.DisterException;
import java.util.Objects;

public record User(EntityId id, String name) {
  public User {
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
    User user = (User) o;
    return Objects.equals(id, user.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}