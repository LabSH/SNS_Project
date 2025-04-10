package induk.sns_study.config;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;

import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

public class ChatHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {

        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest httpRequest = servletRequest.getServletRequest();
            HttpSession session = httpRequest.getSession(false); // 세션이 없으면 null 반환

            if (session != null) {
                // 세션에서 사용자 ID 가져오기 (로그인 시 저장해놓은 값)
                String memberId = String.valueOf(session.getAttribute("memberId"));

                if (memberId != null) {
                    System.out.println("세션 획득: " + memberId);
                    return new StompPrincipal(memberId);
                }
            }
        }

        // 세션이 없거나 userId가 없을 경우 익명 처리
        return new StompPrincipal("anonymous");
    }

    // 사용자 식별용 커스텀 Principal 클래스
    private static class StompPrincipal implements Principal {
        private final String name;

        public StompPrincipal(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
