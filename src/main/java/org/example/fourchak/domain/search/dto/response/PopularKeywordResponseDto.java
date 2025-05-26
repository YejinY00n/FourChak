package org.example.fourchak.domain.search.dto.response;

import lombok.Getter;

@Getter
public class PopularKeywordResponseDto {

    private final String keyword;
    private final int count;

    public PopularKeywordResponseDto(String keyword, int count) {
        this.keyword = keyword;
        this.count = count;
    }
}
