package com.restapi.pojo;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class JiraBugPayload {
    private Fields fields;

    @Data
    @Builder
    public static class Fields {
        private Project project;
        private String summary;
        private Description description;
        private IssueType issuetype;
    }

    @Data
    @Builder
    public static class Project {
        private String key;
    }

    @Data
    @Builder
    public static class IssueType {
        private String name;
    }

    @Data
    @Builder
    public static class Description {
        private String type;
        private int version;
        private List<Content> content;
    }

    @Data
    @Builder
    public static class Content {
        private String type;
        private List<Map<String, String>> content;
    }
}
