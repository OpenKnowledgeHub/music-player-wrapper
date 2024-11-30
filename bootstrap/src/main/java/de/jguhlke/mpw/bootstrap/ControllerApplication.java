package de.jguhlke.mpw.bootstrap;

import de.jguhlke.mpw.adapter.in.rest.controller.ExchangeTokenController;
import de.jguhlke.mpw.adapter.in.rest.controller.PauseTrackController;
import de.jguhlke.mpw.adapter.in.rest.controller.PlayTrackController;
import de.jguhlke.mpw.adapter.in.rest.controller.ResumeTrackController;
import de.jguhlke.mpw.adapter.in.rest.mapper.FallbackExceptionMapper;
import de.jguhlke.mpw.adapter.in.rest.mapper.InvalidClientInputExceptionMapper;
import de.jguhlke.mpw.adapter.in.rest.mapper.AuthenticationExceptionMapper;
import de.jguhlke.mpw.adapter.in.rest.mapper.MusicPlayerWrapperExceptionMapper;
import de.jguhlke.mpw.adapter.in.rest.mapper.NoActiveDeviceExceptionMapper;
import de.jguhlke.mpw.adapter.out.spotify.SpotifyMusicSystem;
import de.jguhlke.mpw.adapter.out.spotify.SpotifyPlayerRepository;
import de.jguhlke.mpw.adapter.out.spotify.SpotifyTrackRepository;
import de.jguhlke.mpw.application.port.in.ExchangeToken;
import de.jguhlke.mpw.application.port.in.PauseTrack;
import de.jguhlke.mpw.application.port.in.PlayTrack;
import de.jguhlke.mpw.application.port.in.ResumeTrack;
import de.jguhlke.mpw.application.port.out.MusicSystem;
import de.jguhlke.mpw.application.port.out.PlayerRepository;
import de.jguhlke.mpw.application.port.out.TrackRepository;
import de.jguhlke.mpw.application.service.ExchangeTokenService;
import de.jguhlke.mpw.application.service.PauseTrackService;
import de.jguhlke.mpw.application.service.PlayTrackService;
import de.jguhlke.mpw.application.service.ResumeTrackService;
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
        new ResumeTrackController(this.resumeTrack),
        new NoActiveDeviceExceptionMapper(),
        new AuthenticationExceptionMapper(),
        new InvalidClientInputExceptionMapper(),
        new MusicPlayerWrapperExceptionMapper(),
        new FallbackExceptionMapper());
  }
}
