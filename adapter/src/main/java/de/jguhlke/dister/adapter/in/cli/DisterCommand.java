package de.jguhlke.dister.adapter.in.cli;

import java.util.logging.Logger;
import picocli.CommandLine;

@CommandLine.Command(
    name = "dister",
    mixinStandardHelpOptions = true,
    description = "Interacts with the dister API")
public class DisterCommand implements Runnable {

  private static final Logger LOG = Logger.getLogger(DisterCommand.class.getSimpleName());

  @Override
  public void run() {
    LOG.warning("Use the cli with play, pause or resume command");
  }
}
