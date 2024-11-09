package de.jguhlke.mpw.model;

import de.jguhlke.mpw.model.exception.MusicPlayerWrapperException;
import de.jguhlke.mpw.model.id.DeviceId;
import java.util.Objects;

public record Device(DeviceId id, String name, boolean active) {

  public Device {
    Objects.requireNonNull(id, "'id' must be set");
    Objects.requireNonNull(name, "'name' must be set");

    if (name.isBlank()) {
      throw new MusicPlayerWrapperException("'name' must not be blank");
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Device device = (Device) o;
    return Objects.equals(id, device.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}