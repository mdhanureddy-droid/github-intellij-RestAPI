package com.restapi.pojo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddBook {
    private String name;
    private String isbn;
    private String aisle;
    private String author;
}
