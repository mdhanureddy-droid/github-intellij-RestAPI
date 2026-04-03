package com.restapi.pojo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CoursePriceCourse {
    private String title;
    private int price;
    private int copies;
}
