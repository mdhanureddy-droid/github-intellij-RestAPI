package Lombok;

import lombok.Data;

import java.util.List;

@Data
public class Orders {

    private List<OrderDetail> orders;

}
