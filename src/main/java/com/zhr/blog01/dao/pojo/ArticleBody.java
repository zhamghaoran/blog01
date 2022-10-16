package com.zhr.blog01.dao.pojo;

import lombok.Data;

@Data
public class ArticleBody {
    private long id;
    private String content;
    private String contentHtml;
    private Long ArticleId;
}
