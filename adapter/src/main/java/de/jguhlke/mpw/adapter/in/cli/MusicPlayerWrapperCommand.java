package de.jguhlke.mpw.adapter.in.cli;

import java.util.logging.Logger;
import picocli.CommandLine;

@CommandLine.Command(
    name = "mpw",
    mixinStandardHelpOptions = true,
    description = "Interacts with the mpw API")
public class MusicPlayerWrapperCommand implements Runnable {

  private static final Logger LOG = Logger.getLogger(MusicPlayerWrapperCommand.class.getSimpleName());

  @Override
  public void run() {
    LOG.warning("Use the cli with play, pause or resume command");
  }
}
