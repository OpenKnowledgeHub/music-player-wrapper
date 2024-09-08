package de.jguhlke.dister.model;

import de.jguhlke.dister.model.exception.DisterException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Test Player entity")
class PlayerTest {

  final String testPlayerName = "player";
  final EntityId testPlayerId = new EntityId("player:123");
  final EntityId testTrackId = new EntityId("track:123");
  final Track testTrack = new Track(testTrackId, "Song");
  final EntityId testDeviceId = new EntityId("device:123");
  final Device testDevice = new Device(testDeviceId, "Kitchen Bar", true);

  @Nested
  @DisplayName("Test Player creation")
  public class PlayerCreationTest {
    @Test
    @DisplayName("Should create a player")
    public void testCreate() {
      final var player = new Player(testPlayerId, testPlayerName, false, testDevice, testTrack);

      assertThat(player).isNotNull();
      assertThat(player.id()).isEqualTo(testPlayerId);
      assertThat(player.name()).isEqualTo(testPlayerName);
      assertThat(player.playing()).isFalse();
      assertThat(player.activeDevice()).isEqualTo(testDevice);
      assertThat(player.currentTrack()).isEqualTo(testTrack);
    }

    @Test
    @DisplayName("Should not create a player without id")
    public void testCreateWithoutId() {
      assertThatThrownBy(() -> new Player(null, testPlayerName, false, testDevice, testTrack))
          .isInstanceOf(NullPointerException.class)
          .hasMessage("'id' must be set");
    }

    @Test
    @DisplayName("Should not create a player without name")
    public void testCreateWithoutName() {
      assertThatThrownBy(() -> new Player(testPlayerId, null, false, testDevice, testTrack))
          .isInstanceOf(NullPointerException.class)
          .hasMessage("'name' must be set");
    }

    @Test
    @DisplayName("Should not create a player without name content")
    public void testCreateWithoutNameContent() {
      assertThatThrownBy(() -> new Player(testPlayerId, "", false, testDevice, testTrack))
          .isInstanceOf(DisterException.class)
          .hasMessage("'name' must not be blank");
    }

    @Test
    @DisplayName("Should not create a playing player without device")
    public void testCreatePlayingWithoutDevice() {
      assertThatThrownBy(() -> new Player(testPlayerId, testPlayerName, true, null, testTrack))
          .isInstanceOf(DisterException.class)
          .hasMessage("'playing' cannot be true without active device");
    }

    @Test
    @DisplayName("Should not create a playing player without track")
    public void testCreatePlayingWithoutTrack() {
      assertThatThrownBy(() -> new Player(testPlayerId, testPlayerName, true, testDevice, null))
          .isInstanceOf(DisterException.class)
          .hasMessage("'playing' cannot be true without current track");
    }
  }

  @Nested
  @DisplayName("Test play a track")
  public class PlayTest {
    @Test
    @DisplayName("Should play a track")
    public void testPlay() {
      final var player = new Player(testPlayerId, testPlayerName, false, testDevice, null);
      final var trackId = new EntityId("track:456");
      final var track = new Track(trackId, "New Song");

      final var playingPlayer = player.play(track);

      assertThat(playingPlayer).isNotNull();
      assertThat(playingPlayer.playing()).isTrue();
      assertThat(playingPlayer.activeDevice()).isEqualTo(testDevice);
      assertThat(playingPlayer.id()).isEqualTo(testPlayerId);
      assertThat(playingPlayer.currentTrack()).isEqualTo(track);
    }

    @Test
    @DisplayName("Should not play a track without device")
    public void testPlayWithoutDevice() {
      final var player = new Player(testPlayerId, testPlayerName, false, null, null);

      Assertions.assertThatThrownBy(() -> player.play(testTrack))
          .isInstanceOf(DisterException.class)
          .hasMessage("It is not allowed to play a track without an active device");
    }

    @Test
    @DisplayName("Should not play a track without track")
    public void testPlayWithoutTrack() {
      final var player = new Player(testPlayerId, testPlayerName, false, testDevice, null);

      Assertions.assertThatThrownBy(() -> player.play(null))
          .isInstanceOf(NullPointerException.class)
          .hasMessage("Track needs to be set");
    }
  }

  @Nested
  @DisplayName("Test stop a track")
  public class StopTest {

    @Test
    @DisplayName("Should stop a track")
    public void testStop() {
      final var player = new Player(testPlayerId, testPlayerName, true, testDevice, testTrack);

      final var stoppedPlayer = player.stop();

      assertThat(stoppedPlayer).isNotNull();
      assertThat(stoppedPlayer.playing()).isFalse();
      assertThat(stoppedPlayer.activeDevice()).isEqualTo(testDevice);
      assertThat(stoppedPlayer.id()).isEqualTo(testPlayerId);
      assertThat(stoppedPlayer.currentTrack()).isEqualTo(testTrack);
    }

    @Test
    @DisplayName("Should not stop a track without playing a track")
    public void testStopWithoutPlayingTrack() {
      final var player = new Player(testPlayerId, testPlayerName, false, testDevice, testTrack);

      Assertions.assertThatThrownBy(player::stop)
          .isInstanceOf(DisterException.class)
          .hasMessage("It is not allowed to stop a not running track");
    }
  }

  @Nested
  @DisplayName("Test resume a track")
  public class ResumeTest {

    @Test
    @DisplayName("Should resume a track")
    public void testResume() {
      final var player = new Player(testPlayerId, testPlayerName, false, testDevice, testTrack);

      final var resumedPlayer = player.resume();

      assertThat(resumedPlayer).isNotNull();
      assertThat(resumedPlayer.playing()).isTrue();
      assertThat(resumedPlayer.activeDevice()).isEqualTo(testDevice);
      assertThat(resumedPlayer.id()).isEqualTo(testPlayerId);
      assertThat(resumedPlayer.currentTrack()).isEqualTo(testTrack);
    }

    @Test
    @DisplayName("Should not resume a track without device")
    public void testResumeWithoutDevice() {
      final var player = new Player(testPlayerId, testPlayerName, false, null, testTrack);

      Assertions.assertThatThrownBy(player::resume)
          .isInstanceOf(DisterException.class)
          .hasMessage("It is not allowed to resume a track without an active device");
    }

    @Test
    @DisplayName("Should not resume a track without track")
    public void testResumeWithoutTrack() {
      final var player = new Player(testPlayerId, testPlayerName, false, testDevice, null);

      Assertions.assertThatThrownBy(player::resume)
          .isInstanceOf(DisterException.class)
          .hasMessage("It is not allowed to resume a track without a current track");
    }

    @Test
    @DisplayName("Should not resume a track with running track")
    public void testResumeWithRunningTrack() {
      final var player = new Player(testPlayerId, testPlayerName, true, testDevice, testTrack);

      Assertions.assertThatThrownBy(player::resume)
          .isInstanceOf(DisterException.class)
          .hasMessage("It is not allowed to resume a running track");
    }
  }

  @Nested
  @DisplayName("Test play on device")
  public class PlayOnTest {

    @Test
    @DisplayName("Should change the active device")
    public void testChangeActiveDevice() {
      final var player = new Player(testPlayerId, testPlayerName, false, testDevice, testTrack);
      final var device = new Device(new EntityId("device:12345"), "New", true);

      final var playerWithNewDevice = player.playOn(device);

      assertThat(playerWithNewDevice).isNotNull();
      assertThat(playerWithNewDevice.playing()).isFalse();
      assertThat(playerWithNewDevice.activeDevice()).isEqualTo(device);
      assertThat(playerWithNewDevice.id()).isEqualTo(testPlayerId);
      assertThat(playerWithNewDevice.currentTrack()).isEqualTo(testTrack);
    }

    @Test
    @DisplayName("Should disable active device")
    public void testChangeActiveDeviceWithoutNewDevice() {
      final var player = new Player(testPlayerId, testPlayerName, true, testDevice, testTrack);
      assertThat(player.playing()).isTrue();

      final var playerWithoutActiveDevice = player.playOn(null);

      assertThat(playerWithoutActiveDevice).isNotNull();
      assertThat(playerWithoutActiveDevice.playing()).isFalse();
      assertThat(playerWithoutActiveDevice.activeDevice()).isNull();
      assertThat(playerWithoutActiveDevice.currentTrack()).isEqualTo(testTrack);
      assertThat(playerWithoutActiveDevice.id()).isEqualTo(testPlayerId);
    }
  }

  @Nested
  @DisplayName("Test equality and hash code")
  public class TestEqualityAndHashCode {
    @Test
    @DisplayName("Should be equal and same hash code with same id")
    public void testEquals() {
      final var playerOne =
          new Player(new EntityId("player:1234"), testPlayerName, true, testDevice, testTrack);
      final var playerTwo =
          new Player(
              new EntityId("player:1234"),
              testPlayerName + "2",
              false,
              new Device(new EntityId("device:4567"), "Device", true),
              new Track(new EntityId("entity:789"), "Track"));

      assertThat(playerOne).isEqualTo(playerTwo);
      assertThat(playerOne.hashCode()).isEqualTo(playerTwo.hashCode());
    }

    @Test
    @DisplayName("Should not be equal and differ hash code with different id")
    public void testNotEquals() {
      final var playerOne =
          new Player(new EntityId("player:1234"), testPlayerName, true, testDevice, testTrack);
      final var playerTwo =
          new Player(new EntityId("player:4567"), testPlayerName, true, testDevice, testTrack);

      assertThat(playerOne).isNotEqualTo(playerTwo);
      assertThat(playerOne.hashCode()).isNotEqualTo(playerTwo.hashCode());
    }
  }
}
