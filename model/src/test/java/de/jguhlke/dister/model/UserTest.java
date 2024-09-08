package de.jguhlke.dister.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import de.jguhlke.dister.model.exception.DisterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Test User entity")
class UserTest {

  final String testUserName = "Hello";
  final EntityId testUserId = new EntityId("User:1234");

  @Nested
  @DisplayName("Test User creation")
  public class UserCreationTest {
    @Test
    @DisplayName("Should create a User")
    public void testCreateUser() {
      final var createdUser = new User(testUserId, testUserName);

      assertThat(createdUser).isNotNull();
      assertThat(createdUser.id()).isEqualTo(testUserId);
      assertThat(createdUser.name()).isEqualTo(testUserName);
    }

    @Test
    @DisplayName("Should not create a User without id")
    public void testCreateUserWithoutId() {
      assertThatThrownBy(() -> new User(null, testUserName))
          .isInstanceOf(NullPointerException.class)
          .hasMessage("'id' must be set");
    }

    @Test
    @DisplayName("Should not create a User without name")
    public void testCreateUserWithoutName() {
      assertThatThrownBy(() -> new User(testUserId, null))
          .isInstanceOf(NullPointerException.class)
          .hasMessage("'name' must be set");
    }

    @Test
    @DisplayName("Should not create a User without name content")
    public void testCreateUserWithoutNameContent() {
      assertThatThrownBy(() -> new User(testUserId, ""))
          .isInstanceOf(DisterException.class)
          .hasMessage("'name' must not be blank");
    }
  }

  @Nested
  @DisplayName("Test equality and hash code")
  public class TestEqualityAndHashCode {
    @Test
    @DisplayName("Should be equal and same hash code with same id")
    public void testEquals() {
      final var userOne = new User(new EntityId("User:1234"), testUserName);
      final var userTwo = new User(new EntityId("User:1234"), testUserName + "2");

      assertThat(userOne).isEqualTo(userTwo);
      assertThat(userOne.hashCode()).isEqualTo(userTwo.hashCode());
    }

    @Test
    @DisplayName("Should not be equal and differ hash code with different id")
    public void testNotEquals() {
      final var userOne = new User(new EntityId("User:1234"), testUserName);
      final var userTwo = new User(new EntityId("User:4567"), testUserName);

      assertThat(userOne).isNotEqualTo(userTwo);
      assertThat(userOne.hashCode()).isNotEqualTo(userTwo.hashCode());
    }
  }
}
