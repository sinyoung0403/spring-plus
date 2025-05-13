package org.example.expert.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.expert.domain.user.entity.User;

@Getter
@NoArgsConstructor(force = true)
public class UserResponse {

    private final Long id;
    private final String email;
    private final String nickname;

    @QueryProjection
    @JsonCreator
    public UserResponse(
            @JsonProperty("id") Long id,
            @JsonProperty("email") String email,
            @JsonProperty("nickname") String nickname
    ) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
    }

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getNickname());
    }
}
