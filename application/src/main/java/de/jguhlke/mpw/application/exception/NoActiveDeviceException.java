package de.jguhlke.mpw.application.exception;

import de.jguhlke.mpw.model.exception.MusicPlayerWrapperException;

public class NoActiveDeviceException extends MusicPlayerWrapperException {
  public NoActiveDeviceException() {
    super("No current active device");
  }
}
