package jikgong.global.security.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jikgong.domain.member.entity.Member;
import jikgong.domain.member.repository.MemberRepository;
import jikgong.global.dto.Response;
import jikgong.global.exception.CustomException;
import jikgong.global.exception.ErrorCode;
import jikgong.global.security.principal.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtProvider;
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            String token;
            String username;
            // 헤더가 null 이 아니고 올바른 토큰이라면
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ") && !request.getRequestURI().equals("/reissue")) {
                // 토큰 추출
                token = authorizationHeader.substring(7);
                // 만료 체크
                if (jwtProvider.isExpiration(token)) {
                    log.info("access token 만료");
                    throw new CustomException(ErrorCode.ACCESS_TOKEN_EXPIRED);
                }

                // claim 을 받아와 정보 추출
                username = (String) jwtProvider.get(token).get("username");

                // DB 에 정보가 있는지 확인
                Member member = memberRepository.findByUsername(username)
                        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

                // principalDetails 생성
                PrincipalDetails principalDetails = new PrincipalDetails(member);

                // 인증 정보 생성
                Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
                // SecurityContextHolder에 인증 정보 설정
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("회원 인증 완료");
            }

            filterChain.doFilter(request, response);
        } catch (CustomException e) {
            // response 세팅
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8"); // JSON 응답을 UTF-8로 설정
            response.setContentType(APPLICATION_JSON_VALUE);

            // 만료된 토큰 에러라면
            if (e.getMessage().equalsIgnoreCase("만료된 access token 입니다.")) {
                writeErrorLogs("EXPIRED_ACCESS_TOKEN", e.getMessage(), e.getStackTrace());
                // 응값 코드 세팅
                response.setStatus(e.getErrorCode().getStatus().value());
                response.getWriter().write(objectMapper.writeValueAsString(new Response<String>("만료된 access token 입니다.")));
                response.getWriter().flush();
                response.getWriter().close();
            }
            // DB 에 없는 유저라면
            else if (e.getMessage().equalsIgnoreCase("회원 정보가 없습니다.")) {
                writeErrorLogs("CANNOT_FOUND_USER", e.getMessage(), e.getStackTrace());
                response.setStatus(e.getErrorCode().getStatus().value());
                response.getWriter().write(objectMapper.writeValueAsString(new Response<String>("회원 정보가 없습니다.")));
                response.getWriter().flush();
                response.getWriter().close();
            }
        }
//        } catch (Exception e) {
//            writeErrorLogs("Exception", e.getMessage(), e.getStackTrace());
//
//            if (response.getStatus() == HttpStatus.OK.value()) {
//                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            }
//        } finally {
//            log.debug("**** SECURITY FILTER FINISH");
//        }
    }

    // 에러를 log 로 기록 하기 위한 함수
    private void writeErrorLogs(String exception, String message, StackTraceElement[] stackTraceElements) {
        log.error("**** " + exception + " ****");
        log.error("**** error message : " + message);
        log.error("**** stack trace : " + Arrays.toString(stackTraceElements));
    }
}
