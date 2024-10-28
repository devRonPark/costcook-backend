package com.costcook.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import com.costcook.entity.User;

@Data
@Builder
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 모든 필드를 매개변수로 하는 생성자
public class UserResponse {
    private Long id;
    private String email;
    private String nickname; // null일 수 있음
    private String profileUrl; // null일 수 있음
    private String createdAt; // String으로 변환된 생성일
    private List<Long> preferredIngredients; // 선호 재료 ({id: 1, name: ""})
    private List<Long> dislikedIngredients; // 기피 재료 ({id: 1, name: ""})

    // User 객체를 UserResponse로 변환하는 정적 메소드
    public static UserResponse from(User user, Map<String, List<Long>> userTaste) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // 원하는 형식으로 변경 가능
        return UserResponse.builder()
            .id(user.getId())
            .email(user.getEmail())
            .nickname(user.getNickname())
            .profileUrl("http://localhost:8080" + user.getProfileUrl())
            .createdAt(user.getCreatedAt() != null ? user.getCreatedAt().format(formatter) : null) // LocalDateTime을 String으로 변환)
            .preferredIngredients(userTaste.get("preferredIngredients"))
            .dislikedIngredients(userTaste.get("dislikedIngredients"))
            .build(); 
    }
}
