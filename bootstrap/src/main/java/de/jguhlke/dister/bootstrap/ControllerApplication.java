package de.jguhlke.dister.bootstrap;

import de.jguhlke.dister.adapter.in.rest.PauseTrackController;
import de.jguhlke.dister.adapter.in.rest.PlayTrackController;
import de.jguhlke.dister.adapter.in.rest.ResumeTrackController;
import de.jguhlke.dister.adapter.out.spotify.SpotifyMusicSystem;
import de.jguhlke.dister.adapter.out.spotify.SpotifyPlayerRepository;
import de.jguhlke.dister.adapter.out.spotify.SpotifyTrackRepository;
import de.jguhlke.dister.application.port.out.MusicSystem;
import de.jguhlke.dister.application.port.out.PlayerRepository;
import de.jguhlke.dister.application.port.out.TrackRepository;
import de.jguhlke.dister.application.service.PauseTrackService;
import de.jguhlke.dister.application.service.PlayTrackService;
import de.jguhlke.dister.application.service.ResumeTrackService;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.Set;
import org.jboss.resteasy.plugins.interceptors.CorsFilter;

@ApplicationPath("/")
public class ControllerApplication extends Application {

  private final PlayTrackService playTrackService;
  private final PauseTrackService pauseTrackService;
  private final ResumeTrackService resumeTrackService;
  private final CorsFilter corsFilter;

  public ControllerApplication() {
    MusicSystem musicSystem = new SpotifyMusicSystem();
    PlayerRepository playerRepository = new SpotifyPlayerRepository();
    TrackRepository trackRepository = new SpotifyTrackRepository();

    this.playTrackService = new PlayTrackService(musicSystem, playerRepository, trackRepository);
    this.pauseTrackService = new PauseTrackService(musicSystem, playerRepository);
    this.resumeTrackService = new ResumeTrackService(musicSystem, playerRepository);

    corsFilter = new CorsFilter();
    corsFilter.getAllowedOrigins().add("*");
  }

  @Override
  public Set<Object> getSingletons() {
    return Set.of(
        corsFilter,
        new PlayTrackController(playTrackService),
        new PauseTrackController(pauseTrackService),
        new ResumeTrackController(resumeTrackService));
  }
}
