package nullnumber1.lab4.dto;

import lombok.Data;
import org.springframework.lang.NonNull;

@Data
public class AuthRequestDto {
    @NonNull
    private String username;
    @NonNull
    private String password;
}
