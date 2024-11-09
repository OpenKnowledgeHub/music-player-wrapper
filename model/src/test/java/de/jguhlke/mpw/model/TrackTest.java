package de.jguhlke.mpw.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import de.jguhlke.mpw.model.exception.MusicPlayerWrapperException;
import de.jguhlke.mpw.model.id.TrackId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Test Track entity")
class TrackTest {

  final String testTrackName = "Hello";
  final TrackId testTrackId = new TrackId("track:1234");

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
          .isInstanceOf(MusicPlayerWrapperException.class)
          .hasMessage("'name' must not be blank");
    }
  }

  @Nested
  @DisplayName("Test equality and hash code")
  public class TestEqualityAndHashCode {
    @Test
    @DisplayName("Should be equal and same hash code with same id")
    public void testEquals() {
      final var trackOne = new Track(new TrackId("track:1234"), testTrackName);
      final var trackTwo = new Track(new TrackId("track:1234"), testTrackName + "2");

      assertThat(trackOne).isEqualTo(trackTwo);
      assertThat(trackOne.hashCode()).isEqualTo(trackTwo.hashCode());
    }

    @Test
    @DisplayName("Should not be equal and differ hash code with different id")
    public void testNotEquals() {
      final var trackOne = new Track(new TrackId("track:1234"), testTrackName);
      final var trackTwo = new Track(new TrackId("track:4567"), testTrackName);

      assertThat(trackOne).isNotEqualTo(trackTwo);
      assertThat(trackOne.hashCode()).isNotEqualTo(trackTwo.hashCode());
    }
  }
}
