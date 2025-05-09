package org.example.expert.domain.todo.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.StringPath;
import lombok.Getter;

@Getter
public class TodoSearchResponse {
    private String title;
    private int countManager;
    private int countComment;

    @QueryProjection
    public TodoSearchResponse(String title, int countManager, int countComment) {
        this.title = title;
        this.countManager = countManager;
        this.countComment = countComment;
    }
}
