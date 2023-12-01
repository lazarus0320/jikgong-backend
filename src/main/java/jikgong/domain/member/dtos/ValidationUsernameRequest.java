package jikgong.domain.member.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ValidationUsernameRequest {
    @Schema(description = "휴대폰 번호", example = "01012345678")
    private String phone; // 휴대폰 번호
}
