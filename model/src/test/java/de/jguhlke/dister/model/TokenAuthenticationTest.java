package de.jguhlke.dister.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import de.jguhlke.dister.model.exception.DisterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Test TokenAuthentication entity")
class TokenAuthenticationTest {
  @Nested
  @DisplayName("Test TokenAuthentication creation")
  public class TokenAuthenticationCreationTest {

    final String testToken = "12345abcdefgh";

    @Test
    @DisplayName("Should create a TokenAuthentication")
    public void testCreateTokenAuthentication() {
      final var createdAuthentication = new TokenAuthentication(testToken);

      assertThat(createdAuthentication).isNotNull();
      assertThat(createdAuthentication.token()).isEqualTo(testToken);
    }

    @Test
    @DisplayName("Should not create a TokenAuthentication without token")
    public void testCreateTokenAuthenticationWithoutName() {
      assertThatThrownBy(() -> new TokenAuthentication(null))
          .isInstanceOf(NullPointerException.class)
          .hasMessage("'token' must be set");
    }

    @Test
    @DisplayName("Should not create a TokenAuthentication without token content")
    public void testCreateTokenAuthenticationWithoutNameContent() {
      assertThatThrownBy(() -> new TokenAuthentication(""))
          .isInstanceOf(DisterException.class)
          .hasMessage("'token' must not be blank");
    }
  }
}
