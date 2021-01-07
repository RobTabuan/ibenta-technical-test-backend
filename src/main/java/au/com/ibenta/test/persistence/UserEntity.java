package au.com.ibenta.test.persistence;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user")
@Accessors(chain = true)
@ToString
public class UserEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, name = "first_name")
    private String firstName;

    @Column(nullable = false, name = "last_name")
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;
}
