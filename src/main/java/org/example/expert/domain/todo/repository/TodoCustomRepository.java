package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.dto.response.QTodoSearchResponse;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.example.expert.domain.todo.entity.QTodo.todo;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Repository
public class TodoCustomRepository {
    private final JPAQueryFactory queryFactory;

    public Optional<Todo> findByIdWithUser(Long todoId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(todo)
                        .leftJoin(todo.user).fetchJoin()
                        .where(todo.id.eq(todoId))
                        .fetchOne()
        );
    }

    public Page<TodoSearchResponse> searchTodos(String title, LocalDateTime startDateTime, LocalDateTime endDateTime, String nickname, Pageable pageable) {
        JPAQuery<Long> countQuery = queryFactory
                .select(todo.count())
                .from(todo)
                .where(titleContains(title),
                        nicknameContains(nickname),
                        todo.createdAt.between(startDateTime, endDateTime));

        List<TodoSearchResponse> content = queryFactory
                .select(new QTodoSearchResponse(
                        todo.title,
                        todo.managers.size(),
                        todo.comments.size()
                ))
                .from(todo)
                .where(titleContains(title),
                        nicknameContains(nickname),
                        todo.createdAt.between(startDateTime, endDateTime))
                .orderBy(todo.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetchOne());
    }

    private BooleanExpression titleContains(String title) {
        return hasText(title) ? todo.title.contains(title) : null;
    }

    private BooleanExpression nicknameContains(String nickname) {
        return hasText(nickname) ? todo.user.nickname.contains(nickname) : null;
    }
}
