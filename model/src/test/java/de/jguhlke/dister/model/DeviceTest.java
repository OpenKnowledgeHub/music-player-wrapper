package de.jguhlke.dister.model;

import de.jguhlke.dister.model.exception.DisterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Test Device entity")
class DeviceTest {

  private EntityId testDeviceId = new EntityId("device:1234");
  private String testDeviceName = "Kitchen";

  @Nested
  @DisplayName("Test Device creation")
  public class DeviceCreationTest {
    @Test
    @DisplayName("Should create a device")
    public void testCreate() {
      final var createdDevice = new Device(testDeviceId, testDeviceName, true);

      assertThat(createdDevice).isNotNull();
      assertThat(createdDevice.id()).isEqualTo(testDeviceId);
      assertThat(createdDevice.name()).isEqualTo(testDeviceName);
      assertThat(createdDevice.active()).isTrue();
    }

    @Test
    @DisplayName("Should not create a device without id")
    public void testCreateWithoutId() {
      assertThatThrownBy(() -> new Device(null, testDeviceName, true))
          .isInstanceOf(NullPointerException.class)
          .hasMessage("'id' must be set");
    }

    @Test
    @DisplayName("Should not create a device without name")
    public void testCreateWithoutName() {
      assertThatThrownBy(() -> new Device(testDeviceId, null, true))
          .isInstanceOf(NullPointerException.class)
          .hasMessage("'name' must be set");
    }

    @Test
    @DisplayName("Should not create a device without name content")
    public void testCreateWithoutNameContent() {
      assertThatThrownBy(() -> new Device(testDeviceId, "", true))
          .isInstanceOf(DisterException.class)
          .hasMessage("'name' must not be blank");
    }
  }

  @Nested
  @DisplayName("Test equality and hash code")
  public class TestEqualityAndHashCode {
    @Test
    @DisplayName("Should be equal and same hash code with same id")
    public void testEquals() {
      final var deviceOne = new Device(new EntityId("device:1234"), testDeviceName, true);
      final var deviceTwo = new Device(new EntityId("device:1234"), testDeviceName + "2", false);

      assertThat(deviceOne).isEqualTo(deviceTwo);
      assertThat(deviceOne.hashCode()).isEqualTo(deviceTwo.hashCode());
    }

    @Test
    @DisplayName("Should not be equal and differ hash code with different id")
    public void testNotEquals() {
      final var deviceOne = new Device(new EntityId("device:1234"), testDeviceName, true);
      final var deviceTwo = new Device(new EntityId("device:12345"), testDeviceName, true);

      assertThat(deviceOne).isNotEqualTo(deviceTwo);
      assertThat(deviceOne.hashCode()).isNotEqualTo(deviceTwo.hashCode());
    }
  }
}
