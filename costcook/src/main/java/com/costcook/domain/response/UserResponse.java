package com.costcook.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.format.DateTimeFormatter;

import com.costcook.entity.User;

@Data
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 모든 필드를 매개변수로 하는 생성자
public class UserResponse {
    private Long id;
    private String email;
    private String nickname; // null일 수 있음
    private String profileUrl; // null일 수 있음
    private String createdAt; // String으로 변환된 생성일

    // User 객체를 UserResponse로 변환하는 정적 메소드
    public static UserResponse from(User user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // 원하는 형식으로 변경 가능
        return new UserResponse(
            user.getId(),
            user.getEmail(),
            user.getNickname(),
            user.getProfileUrl(),
            user.getCreatedAt() != null ? user.getCreatedAt().format(formatter) : null // LocalDateTime을 String으로 변환
        );
    }
}
