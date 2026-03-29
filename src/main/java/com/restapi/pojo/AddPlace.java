package com.restapi.pojo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AddPlace {

    private int accuracy;
    private String name;
    private String phoneNumber;
    private String address;
    private String website;
    private String language;
    private Location location;
    private List<String> types;

}
