package de.jguhlke.dister.bootstrap;

import de.jguhlke.dister.adapter.in.cli.DisterCommand;
import de.jguhlke.dister.adapter.in.cli.PauseTrackCommand;
import de.jguhlke.dister.adapter.in.cli.PlayTrackCommand;
import de.jguhlke.dister.adapter.in.cli.ResumeTrackCommand;
import de.jguhlke.dister.adapter.out.spotify.SpotifyMusicSystem;
import de.jguhlke.dister.adapter.out.spotify.SpotifyPlayerRepository;
import de.jguhlke.dister.adapter.out.spotify.SpotifyTrackRepository;
import de.jguhlke.dister.application.port.out.MusicSystem;
import de.jguhlke.dister.application.port.out.PlayerRepository;
import de.jguhlke.dister.application.port.out.TrackRepository;
import de.jguhlke.dister.application.service.PauseTrackService;
import de.jguhlke.dister.application.service.PlayTrackService;
import de.jguhlke.dister.application.service.ResumeTrackService;
import picocli.CommandLine;

public class CliLauncher {
  public static void main(String[] args) {
    MusicSystem musicSystem = new SpotifyMusicSystem();
    TrackRepository trackRepository = new SpotifyTrackRepository();
    PlayerRepository playerRepository = new SpotifyPlayerRepository();

    PlayTrackCommand playCommand =
        new PlayTrackCommand(new PlayTrackService(musicSystem, playerRepository, trackRepository));
    PauseTrackCommand pauseCommand =
        new PauseTrackCommand(new PauseTrackService(musicSystem, playerRepository));
    ResumeTrackCommand resumeCommand =
        new ResumeTrackCommand(new ResumeTrackService(musicSystem, playerRepository));

    new CommandLine(new DisterCommand())
        .addSubcommand(playCommand)
        .addSubcommand(pauseCommand)
        .addSubcommand(resumeCommand)
        .execute(args);
  }
}
