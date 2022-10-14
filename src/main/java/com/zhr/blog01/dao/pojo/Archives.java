package com.zhr.blog01.dao.pojo;

import lombok.*;
import org.springframework.data.relational.core.sql.In;

@Data
public class Archives {
    private Integer year;
    private Integer month;
    private Integer count;
}
