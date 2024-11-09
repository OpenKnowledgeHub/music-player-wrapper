package de.jguhlke.mpw.model;

import de.jguhlke.mpw.model.exception.MusicPlayerWrapperException;
import java.util.Objects;

public record TokenAuthentication(String token) implements Authentication {
  public TokenAuthentication {
    Objects.requireNonNull(token, "'token' must be set");

    if (token.isBlank()) {
      throw new MusicPlayerWrapperException("'token' must not be blank");
    }
  }

  @Override
  public String getAuthentication() {
    return token;
  }
}
