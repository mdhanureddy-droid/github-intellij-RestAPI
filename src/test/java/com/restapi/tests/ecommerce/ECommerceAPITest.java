package com.restapi.tests.ecommerce;

import com.restapi.pojo.LoginRequest;
import com.restapi.pojo.LoginResponse;
import com.restapi.pojo.OrderDetail;
import com.restapi.pojo.Orders;
import com.restapi.utils.ConfigReader;
import main.java.com.restapi.utils.ExcelDataReaderReview;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ECommerceAPITest {

    private static final Logger logger = LogManager.getLogger(ECommerceAPITest.class);
    private static final String BASE_URL = ConfigReader.getBaseUrl();

    public static void main(String[] args) throws IOException {
        RequestSpecification baseSpec = buildBaseSpec(ContentType.JSON);

        LoginResponse loginResponse = login(baseSpec);
        String token = loginResponse.getToken();
        String userId = loginResponse.getUserId();
        logger.info("Token: {}, UserId: {}", token, userId);

        Map<String, String> testData = new ExcelDataReaderReview().getDataAsMap("EcomOrder", "testdata");

        String productId = addProduct(token, userId, testData);
        logger.info("Product ID: {}", productId);

        String responseAddOrder = createOrder(token, testData, productId);
        logger.info("Create Order Response: {}", responseAddOrder);

        String deleteProductResponse = deleteProduct(token, productId);
        Assert.assertEquals("Product Deleted Successfully", new JsonPath(deleteProductResponse).get("message"));
    }

    private static RequestSpecification buildBaseSpec(ContentType contentType) {
        return new RequestSpecBuilder().setBaseUri(BASE_URL)
                .setContentType(contentType).build();
    }

    private static RequestSpecification buildAuthSpec(String token, ContentType contentType) {
        return new RequestSpecBuilder().setBaseUri(BASE_URL)
                .addHeader("authorization", token)
                .setContentType(contentType).build();
    }

    private static LoginResponse login(RequestSpecification baseSpec) {
        LoginRequest loginRequest = LoginRequest.builder()
                .userEmail(ConfigReader.get("ecom.userEmail"))
                .userPassword(ConfigReader.get("ecom.userPassword"))
                .build();
        return given().relaxedHTTPSValidation().log().all().spec(baseSpec).body(loginRequest)
                .when().post("/api/ecom/auth/login")
                .then().log().all().extract().response().as(LoginResponse.class);
    }

    private static String addProduct(String token, String userId, Map<String, String> testData) {
        RequestSpecification addProductSpec = new RequestSpecBuilder().setBaseUri(BASE_URL)
                .addHeader("authorization", token).build();

        String response = given().log().all().spec(addProductSpec)
                .param("productName", testData.getOrDefault("ProductName", "Laptop"))
                .param("productAddedBy", userId)
                .param("productCategory", testData.getOrDefault("ProductCategory", "fashion"))
                .param("productSubCategory", testData.getOrDefault("ProductSubCategory", "shirts"))
                .param("productPrice", testData.getOrDefault("ProductPrice", "11500"))
                .param("productDescription", testData.getOrDefault("ProductDescription", "Lenova"))
                .param("productFor", testData.getOrDefault("ProductFor", "men"))
                .multiPart("productImage", new File("testdata/AddAttachment.png"))
                .when().post("/api/ecom/product/add-product")
                .then().log().all().extract().response().asString();

        String productId = new JsonPath(response).get("productId");
        if (productId == null) {
            logger.error("productId is null. Full response: {}", response);
            throw new RuntimeException("Failed to retrieve productId from add-product response");
        }
        return productId;
    }

    private static String createOrder(String token, Map<String, String> testData, String productId) {
        Orders orders = Orders.builder()
                .orders(List.of(OrderDetail.builder()
                        .country(testData.getOrDefault("Country", "India"))
                        .productOrderedId(productId)
                        .build()))
                .build();

        return given().log().all().spec(buildAuthSpec(token, ContentType.JSON)).body(orders)
                .when().post("/api/ecom/order/create-order")
                .then().log().all().extract().response().asString();
    }

    private static String deleteProduct(String token, String productId) {
        return given().log().all().spec(buildAuthSpec(token, ContentType.JSON))
                .pathParam("productId", productId)
                .when().delete("/api/ecom/product/delete-product/{productId}")
                .then().log().all().extract().response().asString();
    }

}
