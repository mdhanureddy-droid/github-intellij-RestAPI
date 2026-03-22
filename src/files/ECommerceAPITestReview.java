package files;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import Lombok.LoginRequest;
import Lombok.LoginResponse;
import Lombok.OrderDetail;
import Lombok.Orders;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class ECommerceAPITestReview {

    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream("src/resources/config.properties"));

        RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .setContentType(ContentType.JSON).build();

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserEmail(properties.getProperty("ecom.userEmail"));
        loginRequest.setUserPassword(properties.getProperty("ecom.userPassword"));
        RequestSpecification reqLogin = given().relaxedHTTPSValidation().log().all().spec(req).body(loginRequest);
        LoginResponse loginResponse = reqLogin.when().post("/api/ecom/auth/login").then().log().all().extract().response()
                .as(LoginResponse.class);
        System.out.println(loginResponse.getToken());
        String token = loginResponse.getToken();
        System.out.println(loginResponse.getUserId());
        String userId = loginResponse.getUserId();

        RequestSpecification addProductBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .addHeader("authorization", token)
                .build();

        RequestSpecification reqAddProduct = given().log().all().spec(addProductBaseReq).param("productName", "Laptop")
                .param("productAddedBy", userId).param("productCategory", "fashion")
                .param("productSubCategory", "shirts").param("productPrice", "11500")
                .param("productDescription", "Lenova").param("productFor", "men")
                .multiPart("productImage", new File("testdata/AddAttachment.png"));

        String addProductResponse = reqAddProduct.when().post("/api/ecom/product/add-product")
                .then().log().all().extract().response().asString();
        JsonPath jsonPath = new JsonPath(addProductResponse);
        String productId = jsonPath.get("productId");

        RequestSpecification createOrderBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .addHeader("authorization", token).setContentType(ContentType.JSON)
                .build();
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setCountry("India");
        orderDetail.setProductOrderedId(productId);

        List<OrderDetail> orderDetailList = new ArrayList<OrderDetail>();
        orderDetailList.add(orderDetail);
        Orders orders = new Orders();
        orders.setOrders(orderDetailList);

        RequestSpecification createOrderReq = given().log().all().spec(createOrderBaseReq).body(orders);

        String responseAddOrder = createOrderReq.when().post("/api/ecom/order/create-order").then().log().all().extract().response().asString();
        System.out.println(responseAddOrder);

        RequestSpecification deleteProdBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .addHeader("authorization", token).setContentType(ContentType.JSON)
                .build();

        RequestSpecification deleteProdReq = given().log().all().spec(deleteProdBaseReq).pathParam("productId", productId);

        String deleteProductResponse = deleteProdReq.when().delete("/api/ecom/product/delete-product/{productId}").then().log().all()
                .extract().response().asString();

        JsonPath jsonPath1 = new JsonPath(deleteProductResponse);

        Assert.assertEquals("Product Deleted Successfully", jsonPath1.get("message"));
    }

}
