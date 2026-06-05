package RouteWise.Transportation.dtos;

import lombok.Data;

@Data
public class AuthResponseDTO {
    private boolean success;
    private String message;
    private Long id;
    private String role;
}
