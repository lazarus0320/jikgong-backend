package jikgong.domain.member.entity;

import jakarta.persistence.*;
import jikgong.domain.certification.entity.Certification;
import jikgong.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String phone;
    private String authCode; // 인증 코드 (비밀번호로 사용)

    private String account; // 계좌
    private String bank; // 은행 종류
    private String deviceToken; // 기기 토큰
    private Boolean isNotification; // 알림 수신 여부
    private Boolean isDeleted; // 회원 탈퇴 여부

    @Enumerated(value = EnumType.STRING)
    private Role role;
    @Embedded
    private Worker workerInfo;
    @Embedded
    private Company companyInfo;

    @OneToOne
    @JoinColumn(name = "certification_id")
    private Certification certification;

    @Builder
    public Member(String phone, String authCode, String account, String bank, String deviceToken, Boolean isNotification, Role role, Worker workerInfo, Company companyInfo, Certification certification) {
        this.phone = phone;
        this.authCode = authCode;
        this.account = account;
        this.bank = bank;
        this.deviceToken = deviceToken;
        this.isNotification = isNotification;
        this.role = role;
        this.workerInfo = workerInfo;
        this.companyInfo = companyInfo;
        this.certification = certification;
        this.isDeleted = false;
    }
}
