package com.cliento.clientointerview;


import io.restassured.response.Response;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ClientoInterviewApplicationTests {

    @Test
    public void test_VerifyHelloWorld() {

        given().get("/hello-world").then().statusCode(200).assertThat().body(is("Hello World!"));

    }


    @Test
	public void test_495kr_sale_OK() throws IOException {

	    String body = fileAsString("post-sale-495kr.json");

		Response res = given()
				.header("Content-Type", "application/json")
				.body(body)
				.log().all()
				.post("/sale")
				.then()
				.log().all()
				.statusCode(200).extract().response();


		assertThat(res.path("saleId"), is(0));
        assertThat(res.path("transactionRef"), is("TXREF2"));

        Response salesResponse = given().get("/sales").then().statusCode(200).extract().response();

        assertThat(salesResponse.path("sales[0].cardTransactionRef"), is("TXREF2"));
        assertThat(salesResponse.path("sales[0].totalAmount"), is(495.0f));
	}

    @Test
    public void test_0kr_sale_BAD_REQUEST() throws IOException {

        String body = fileAsString("post-sale-0kr.json");

        Response res = given()
                .header("Content-Type", "application/json")
                .body(body)
                .log().all()
                .post("/sale")
                .then()
                .log().all()
                .statusCode(400).extract().response();

        assertThat(res.path("message"), is("Transaction Denied: Negative or zero amount"));
    }

    @Test
    public void test_600kr_sale_BAD_REQUEST() throws IOException {

        String body = fileAsString("post-sale-600kr.json");

        Response res = given()
                .header("Content-Type", "application/json")
                .body(body)
                .log().all()
                .post("/sale")
                .then()
                .log().all()
                .statusCode(400).extract().response();

        assertThat(res.path("message"), is("Transaction Denied: not enough funds"));
    }


	private String fileAsString(String file) throws IOException {
        return IOUtils.toString(
                this.getClass().getClassLoader().getResourceAsStream(file),
                "utf-8"
        );
    }


}
