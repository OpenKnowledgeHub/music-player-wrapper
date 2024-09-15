package de.jguhlke.dister.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import de.jguhlke.dister.model.exception.DisterException;
import de.jguhlke.dister.model.id.DeviceId;
import de.jguhlke.dister.model.id.TrackId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Test Player entity")
class PlayerTest {

  final TrackId testTrackId = new TrackId("track:123");
  final Track testTrack = new Track(testTrackId, "Song");
  final DeviceId testDeviceId = new DeviceId("device:123");
  final Device testDevice = new Device(testDeviceId, "Kitchen Bar", true);

  @Nested
  @DisplayName("Test Player creation")
  public class PlayerCreationTest {
    @Test
    @DisplayName("Should create a player")
    public void testCreate() {
      final var player = new Player(false, testDevice, testTrack);

      assertThat(player).isNotNull();
      assertThat(player.playing()).isFalse();
      assertThat(player.activeDevice()).isEqualTo(testDevice);
      assertThat(player.currentTrack()).isEqualTo(testTrack);
    }

    @Test
    @DisplayName("Should not create a playing player without device")
    public void testCreatePlayingWithoutDevice() {
      assertThatThrownBy(() -> new Player(true, null, testTrack))
          .isInstanceOf(DisterException.class)
          .hasMessage("'playing' cannot be true without active device");
    }

    @Test
    @DisplayName("Should not create a playing player without track")
    public void testCreatePlayingWithoutTrack() {
      assertThatThrownBy(() -> new Player(true, testDevice, null))
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
      final var player = new Player(false, testDevice, null);
      final var trackId = new TrackId("track:456");
      final var track = new Track(trackId, "New Song");

      final var playingPlayer = player.play(track);

      assertThat(playingPlayer).isNotNull();
      assertThat(playingPlayer.playing()).isTrue();
      assertThat(playingPlayer.activeDevice()).isEqualTo(testDevice);
      assertThat(playingPlayer.currentTrack()).isEqualTo(track);
    }

    @Test
    @DisplayName("Should not play a track without device")
    public void testPlayWithoutDevice() {
      final var player = new Player(false, null, null);

      Assertions.assertThatThrownBy(() -> player.play(testTrack))
          .isInstanceOf(DisterException.class)
          .hasMessage("It is not allowed to play a track without an active device");
    }

    @Test
    @DisplayName("Should not play a track without track")
    public void testPlayWithoutTrack() {
      final var player = new Player(false, testDevice, null);

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
      final var player = new Player(true, testDevice, testTrack);

      final var stoppedPlayer = player.stop();

      assertThat(stoppedPlayer).isNotNull();
      assertThat(stoppedPlayer.playing()).isFalse();
      assertThat(stoppedPlayer.activeDevice()).isEqualTo(testDevice);
      assertThat(stoppedPlayer.currentTrack()).isEqualTo(testTrack);
    }

    @Test
    @DisplayName("Should not stop a track without playing a track")
    public void testStopWithoutPlayingTrack() {
      final var player = new Player(false, testDevice, testTrack);

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
      final var player = new Player(false, testDevice, testTrack);

      final var resumedPlayer = player.resume();

      assertThat(resumedPlayer).isNotNull();
      assertThat(resumedPlayer.playing()).isTrue();
      assertThat(resumedPlayer.activeDevice()).isEqualTo(testDevice);
      assertThat(resumedPlayer.currentTrack()).isEqualTo(testTrack);
    }

    @Test
    @DisplayName("Should not resume a track without device")
    public void testResumeWithoutDevice() {
      final var player = new Player(false, null, testTrack);

      Assertions.assertThatThrownBy(player::resume)
          .isInstanceOf(DisterException.class)
          .hasMessage("It is not allowed to resume a track without an active device");
    }

    @Test
    @DisplayName("Should not resume a track without track")
    public void testResumeWithoutTrack() {
      final var player = new Player(false, testDevice, null);

      Assertions.assertThatThrownBy(player::resume)
          .isInstanceOf(DisterException.class)
          .hasMessage("It is not allowed to resume a track without a current track");
    }

    @Test
    @DisplayName("Should not resume a track with running track")
    public void testResumeWithRunningTrack() {
      final var player = new Player(true, testDevice, testTrack);

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
      final var player = new Player(false, testDevice, testTrack);
      final var device = new Device(new DeviceId("device:12345"), "New", true);

      final var playerWithNewDevice = player.playOn(device);

      assertThat(playerWithNewDevice).isNotNull();
      assertThat(playerWithNewDevice.playing()).isFalse();
      assertThat(playerWithNewDevice.activeDevice()).isEqualTo(device);
      assertThat(playerWithNewDevice.currentTrack()).isEqualTo(testTrack);
    }

    @Test
    @DisplayName("Should disable active device")
    public void testChangeActiveDeviceWithoutNewDevice() {
      final var player = new Player(true, testDevice, testTrack);
      assertThat(player.playing()).isTrue();

      final var playerWithoutActiveDevice = player.playOn(null);

      assertThat(playerWithoutActiveDevice).isNotNull();
      assertThat(playerWithoutActiveDevice.playing()).isFalse();
      assertThat(playerWithoutActiveDevice.activeDevice()).isNull();
      assertThat(playerWithoutActiveDevice.currentTrack()).isEqualTo(testTrack);
    }
  }
}
