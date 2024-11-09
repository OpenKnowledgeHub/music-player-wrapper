package de.jguhlke.mpw.bootstrap;

import de.jguhlke.mpw.adapter.in.cli.MusicPlayerWrapperCommand;
import de.jguhlke.mpw.adapter.in.cli.PauseTrackCommand;
import de.jguhlke.mpw.adapter.in.cli.PlayTrackCommand;
import de.jguhlke.mpw.adapter.in.cli.ResumeTrackCommand;
import de.jguhlke.mpw.adapter.out.spotify.SpotifyMusicSystem;
import de.jguhlke.mpw.adapter.out.spotify.SpotifyPlayerRepository;
import de.jguhlke.mpw.adapter.out.spotify.SpotifyTrackRepository;
import de.jguhlke.mpw.application.port.out.MusicSystem;
import de.jguhlke.mpw.application.port.out.PlayerRepository;
import de.jguhlke.mpw.application.port.out.TrackRepository;
import de.jguhlke.mpw.application.service.PauseTrackService;
import de.jguhlke.mpw.application.service.PlayTrackService;
import de.jguhlke.mpw.application.service.ResumeTrackService;
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

    new CommandLine(new MusicPlayerWrapperCommand())
        .addSubcommand(playCommand)
        .addSubcommand(pauseCommand)
        .addSubcommand(resumeCommand)
        .execute(args);
  }
}
