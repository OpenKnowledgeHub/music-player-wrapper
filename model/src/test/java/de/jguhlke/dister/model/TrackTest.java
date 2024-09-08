package de.jguhlke.dister.model;

import de.jguhlke.dister.model.exception.DisterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Test Track entity")
class TrackTest {

  final String testTrackName = "Hello";
  final EntityId testTrackId = new EntityId("track:1234");

  @Nested
  @DisplayName("Test Track creation")
  public class TrackCreationTest {
    @Test
    @DisplayName("Should create a track")
    public void testCreateTrack() {
      final var createdTrack = new Track(testTrackId, testTrackName);

      assertThat(createdTrack).isNotNull();
      assertThat(createdTrack.id()).isEqualTo(testTrackId);
      assertThat(createdTrack.name()).isEqualTo(testTrackName);
    }

    @Test
    @DisplayName("Should not create a track without id")
    public void testCreateTrackWithoutId() {
      assertThatThrownBy(() -> new Track(null, testTrackName))
          .isInstanceOf(NullPointerException.class)
          .hasMessage("'id' must be set");
    }

    @Test
    @DisplayName("Should not create a track without name")
    public void testCreateTrackWithoutName() {
      assertThatThrownBy(() -> new Track(testTrackId, null))
          .isInstanceOf(NullPointerException.class)
          .hasMessage("'name' must be set");
    }

    @Test
    @DisplayName("Should not create a track without name content")
    public void testCreateTrackWithoutNameContent() {
      assertThatThrownBy(() -> new Track(testTrackId, ""))
              .isInstanceOf(DisterException.class)
              .hasMessage("'name' must not be blank");
    }
  }
}
