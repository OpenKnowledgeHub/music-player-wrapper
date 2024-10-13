package de.jguhlke.dister.bootstrap;

import de.jguhlke.dister.adapter.in.rest.ExchangeTokenController;
import de.jguhlke.dister.adapter.in.rest.PauseTrackController;
import de.jguhlke.dister.adapter.in.rest.PlayTrackController;
import de.jguhlke.dister.adapter.in.rest.ResumeTrackController;
import de.jguhlke.dister.adapter.out.spotify.SpotifyMusicSystem;
import de.jguhlke.dister.adapter.out.spotify.SpotifyPlayerRepository;
import de.jguhlke.dister.adapter.out.spotify.SpotifyTrackRepository;
import de.jguhlke.dister.application.port.in.ExchangeToken;
import de.jguhlke.dister.application.port.in.PauseTrack;
import de.jguhlke.dister.application.port.in.PlayTrack;
import de.jguhlke.dister.application.port.in.ResumeTrack;
import de.jguhlke.dister.application.port.out.MusicSystem;
import de.jguhlke.dister.application.port.out.PlayerRepository;
import de.jguhlke.dister.application.port.out.TrackRepository;
import de.jguhlke.dister.application.service.ExchangeTokenService;
import de.jguhlke.dister.application.service.PauseTrackService;
import de.jguhlke.dister.application.service.PlayTrackService;
import de.jguhlke.dister.application.service.ResumeTrackService;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.Set;
import org.jboss.resteasy.plugins.interceptors.CorsFilter;

@ApplicationPath("/")
public class ControllerApplication extends Application {

  private final PlayTrack playTrack;
  private final PauseTrack pauseTrack;
  private final ResumeTrack resumeTrack;
  private final ExchangeToken exchangeToken;
  private final CorsFilter corsFilter;

  public ControllerApplication() {
    MusicSystem musicSystem = new SpotifyMusicSystem();
    PlayerRepository playerRepository = new SpotifyPlayerRepository();
    TrackRepository trackRepository = new SpotifyTrackRepository();

    this.playTrack = new PlayTrackService(musicSystem, playerRepository, trackRepository);
    this.pauseTrack = new PauseTrackService(musicSystem, playerRepository);
    this.resumeTrack = new ResumeTrackService(musicSystem, playerRepository);
    this.exchangeToken = new ExchangeTokenService(musicSystem);

    this.corsFilter = new CorsFilter();
    this.corsFilter.getAllowedOrigins().add(System.getenv("CORS_ORIGIN"));
  }

  @Override
  public Set<Object> getSingletons() {
    return Set.of(
        corsFilter,
        new ExchangeTokenController(this.exchangeToken),
        new PlayTrackController(this.playTrack),
        new PauseTrackController(this.pauseTrack),
        new ResumeTrackController(this.resumeTrack));
  }
}
