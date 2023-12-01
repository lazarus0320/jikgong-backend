package jikgong.domain.member.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jikgong.domain.member.entity.Gender;
import jikgong.domain.member.entity.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class JoinRequest {
    @Schema(description = "휴대폰 번호", example = "01012345678")
    private String phone;
    @Schema(description = "인증 코드 6자리", example = "")
    private String authCode; // 인증 코드
    @Schema(description = "회원 타입 [ROLE_WORKER or ROLE_REGISTER]", example = "ROLE_WORKER")
    private Role role; // 회원 타입
    @Schema(description = "계좌 번호", example = "12341234123412")
    private String account; // 게좌 번호
    @Schema(description = "은행 종류", example = "국민은행")
    private String bank; // 은행

    // 위치 정보
    @Schema(description = "도로명 주소", example = "부산광역시 사하구 낙동대로 550번길 37")
    private String address; // 도로명 주소
    @Schema(description = "위도", example = "35.116777388697734")
    private Float latitude; // 위도
    @Schema(description = "경도", example = "128.9685393114043")
    private Float longitude; // 경도

    // 회사 정보
    @Schema(description = "사업자 번호", example = "00000000")
    private String businessNumber; // 사업자 번호
    @Schema(description = "지역", example = "서울")
    private String region; // 지역
    @Schema(description = "회사 명", example = "삼성")
    private String companyName; // 회사 명
    @Schema(description = "이메일", example = "jaeyoung@naver.com")
    private String email; // 이메일
    @Schema(description = "담당자 이름", example = "이재용")
    private String manager; // 담당자 이름
    @Schema(description = "문의 내용", example = "직공 서비스에 가입하고 싶습니다.")
    private String requestContent; // 문의 내용

    // 노동자 정보
    @Schema(description = "노동자 이름", example = "홍길동")
    private String workerName; // 노동자 이름
    @Schema(description = "생년월일", example = "19750101")
    private String rrnPrefix; // 생년월일
    @Schema(description = "성별 [MALE or FEMAILE]", example = "MALE")
    private Gender gender; // 성별
    @Schema(description = "국적", example = "대한민국")
    private String nationality; // 국적
}
