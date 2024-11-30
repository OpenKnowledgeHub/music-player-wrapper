package de.jguhlke.mpw.application.exception;

import de.jguhlke.mpw.model.exception.MusicPlayerWrapperException;

public class AuthenticationException extends MusicPlayerWrapperException {
  public AuthenticationException() {
    super("Authentication is missing or is invalid");
  }
}
