package de.jguhlke.mpw.application.exception;

import de.jguhlke.mpw.model.exception.MusicPlayerWrapperException;

public class InvalidClientInputException extends MusicPlayerWrapperException {
  public InvalidClientInputException(String message) {
    super(message);
  }
}
