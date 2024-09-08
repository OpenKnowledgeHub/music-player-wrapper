package de.jguhlke.dister.model;

import de.jguhlke.dister.model.exception.DisterException;
import java.util.Objects;

public record TokenAuthentication(String token) implements Authentication {
  public TokenAuthentication {
    Objects.requireNonNull(token, "'token' must be set");

    if (token.isBlank()) {
      throw new DisterException("'token' must not be blank");
    }
  }
}
