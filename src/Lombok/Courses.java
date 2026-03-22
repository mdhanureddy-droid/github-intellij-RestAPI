package Lombok;

import lombok.Data;

import java.util.List;

@Data
public class Courses {

    private List<WebAutomation> webAutomation;
    private List<Api> api;
    private List<Mobile> mobile;

}
