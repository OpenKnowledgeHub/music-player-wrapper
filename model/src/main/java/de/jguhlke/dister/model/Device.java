package de.jguhlke.dister.model;

import java.util.Objects;

public record Device(EntityId id, String name, boolean active, Track runningTrack) {

    public Device {
        Objects.requireNonNull(id, "'id' needs to be set");
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
