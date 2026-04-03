package com.restapi.pojo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CoursePriceDashboard {
    private int purchaseAmount;
    private String website;
}
