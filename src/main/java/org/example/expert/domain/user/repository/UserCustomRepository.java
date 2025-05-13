package org.example.expert.domain.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.user.dto.response.QUserResponse;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.example.expert.domain.user.entity.QUser.user;

@RequiredArgsConstructor
@Repository
public class UserCustomRepository {
    private final JPAQueryFactory queryFactory;

    public List<UserResponse> findByNickNameWith(String nickname) {
        return queryFactory
                        .select(new QUserResponse(user.id, user.email, user.nickname))
                        .from(user)
                        .where(user.nickname.eq(nickname))
                        .fetch()
        ;
    }
}