package com.restapi.pojo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CoursePricePayload {
    private CoursePriceDashboard dashboard;
    private List<CoursePriceCourse> courses;
}
