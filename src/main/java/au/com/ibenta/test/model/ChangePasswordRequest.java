package au.com.ibenta.test.model;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@Accessors(chain = true)
public class ChangePasswordRequest {
    private Long userId;
    private String newPassword;
}
