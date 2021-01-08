package au.com.ibenta.test.model;

import au.com.ibenta.test.persistence.UserEntity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import net.minidev.json.annotate.JsonIgnore;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@Accessors(chain = true)
@ToString
public class User {
    private Long id;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @NotEmpty
    @Email
    private String email;

    @JsonIgnore
    @Getter(AccessLevel.PRIVATE)
    @NotEmpty
    private String password;

    @JsonIgnore
    public UserEntity toUserEntity() {
        return new UserEntity()
                .setId(id)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setEmail(email)
                .setPassword(password);
    }

    @JsonIgnore
    public static User fromUserEntity(UserEntity user) {
        return new User()
                .setId(user.getId())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setEmail(user.getEmail())
                .setPassword(user.getPassword());

    }


}
