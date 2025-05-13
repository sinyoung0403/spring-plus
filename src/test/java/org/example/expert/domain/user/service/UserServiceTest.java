package org.example.expert.domain.user.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    @Transactional
    void setup() {
        System.out.println("100만 명 유저 생성 시작");
        Set<String> nicknames = new HashSet<>();
        List<User> users = new ArrayList<>();

        while (nicknames.size() < 1_000_000) {
            String nickname = UUID.randomUUID().toString().substring(0, 10);
            if (nicknames.add(nickname)) {
                users.add(new User(nickname + "@test.com", "pwd", nickname, UserRole.USER));
            }

            if (users.size() == 1000) {
                userRepository.saveAll(users);
                entityManager.flush();
                entityManager.clear();
                users.clear();
            }
        }

        if (!users.isEmpty()) {
            userRepository.saveAll(users);
            entityManager.flush();
            entityManager.clear();
        }

        System.out.println("100만 명 유저 저장 완료");

        userRepository.save(new User("test1@test.com", "pwd", "test", UserRole.USER));
        userRepository.save(new User("test2@test.com", "pwd", "test", UserRole.USER));
        userRepository.save(new User("test3@test.com", "pwd", "test", UserRole.USER));
        userRepository.save(new User("test4@test.com", "pwd", "test", UserRole.USER));

    }

    @Test
    @Transactional
    void 속도_테스트() {
        System.out.println("1번 - JPQL");
        long start1 = System.currentTimeMillis();
        List<UserResponse> userResponse1 = userService.searchUserWithJPQL("test");
        long end1 = System.currentTimeMillis();
        System.out.println(userResponse1.toString());
        System.out.println("JPQL 검색 시간: " + (end1 - start1) + "ms");

        System.out.println("2번 - QueryDSL");
        long start2 = System.currentTimeMillis();
        List<UserResponse> userResponse2 = userService.searchUserWithQueryDSL("test");
        long end2 = System.currentTimeMillis();
        System.out.println(userResponse2.toString());
        System.out.println("QueryDSL 검색 시간: " + (end2 - start2) + "ms");

        System.out.println("3번 - Dto");
        long start3 = System.currentTimeMillis();
        List<UserResponse> userResponse3 = userService.searchUserWithDto("test");
        long end3 = System.currentTimeMillis();
        System.out.println(userResponse3.toString());
        System.out.println("Dto 검색 시간: " + (end3 - start3) + "ms");

        System.out.println("4번 - Redis Cache 1번째");
        long start4 = System.currentTimeMillis();
        List<UserResponse> userResponse4 = userService.searchUserWithRedis("test");
        long end4 = System.currentTimeMillis();
        System.out.println(userResponse4.toString());
        System.out.println("Redis Cache 최초 검색 시간: " + (end4 - start4) + "ms");

        System.out.println("5번 - Redis Cache 2번째");
        long start5 = System.currentTimeMillis();
        List<UserResponse> userResponse5 = userService.searchUserWithRedis("test");
        long end5 = System.currentTimeMillis();
        System.out.println(userResponse5.toString());
        System.out.println("Redis Cache 2번째 검색 시간: " + (end5 - start5) + "ms");
    }
}
