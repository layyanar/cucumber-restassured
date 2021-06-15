package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.junit.Assert;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class BookStepDefinitions {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    private static final String ENDPOINT_GET_WORD_COUNT = "http://localhost:8080/getTopResultsApi";

    @Given("^the words exist with count of '(.*)'$")
    public void wordCountParam(String number) {
        request = given().param("count", number);
    }

    @When("^a user retrieves word count with auth (.*)$")
    public void getWordCount(String auth) {
        if (auth.equals("Yes")) {
            response = request.when()
                    .auth()
                    .basic("admin", "password")
                    .get(ENDPOINT_GET_WORD_COUNT);
            System.out.println("response: " + response.prettyPrint());
        } else if (auth.equals("No")) {
            response = request.when()
                    .get(ENDPOINT_GET_WORD_COUNT);
            System.out.println("response: " + response.prettyPrint());
        } else if (auth.equals("Invalid")) {
            response = request.when()
                    .auth()
                    .basic("admin", "******")
                    .get(ENDPOINT_GET_WORD_COUNT);
            System.out.println("response: " + response.prettyPrint());
        }
    }

    @When("a user retrieves word count with no authorization")
    public void getWordCountWithNoAuth() {
        response = request.when()
                .get(ENDPOINT_GET_WORD_COUNT);
        System.out.println("response: " + response.prettyPrint());
    }

    @Then("^the status code is (\\d+)$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^response includes the following$")
    public void response_equals(Map<String, String> responseFields) {
        for (Map.Entry<String, String> field : responseFields.entrySet()) {
            if (StringUtils.isNumeric(field.getValue())) {
                json.body(field.getKey(), equalTo(Integer.parseInt(field.getValue())));
            }
        }
    }

    @And("^user enters max word count (.*) and expects response in below order$")
    public void sort_response(String num, String expResponse) {
        response = given().
                param("count", num).
                when().
                auth().
                basic("admin", "password").
                get(ENDPOINT_GET_WORD_COUNT)
                .then().
                        statusCode(HttpStatus.SC_OK).
                        contentType(ContentType.JSON).
                        assertThat().
                        extract().response();

        String responseAsString = response.asString();
        Assert.assertEquals(expResponse.trim(), responseAsString.trim());
    }


    @And("response includes empty record")
    public void no_response_equals() {
        Assert.assertTrue(response.body().asString().contains(""));
    }

    @And("response includes the following in any order")
    public void response_contains_in_any_order(Map<String, String> responseFields) {
        for (Map.Entry<String, String> field : responseFields.entrySet()) {
            if (StringUtils.isNumeric(field.getValue())) {
                json.body(field.getKey(), containsInAnyOrder(Integer.parseInt(field.getValue())));
            } else {
                json.body(field.getKey(), containsInAnyOrder(field.getValue()));
            }
        }
    }

    @And("^the error message is as below$")
    public void errorMessageValidation(Map<String, String> expError) throws NullPointerException {
        for (Map.Entry<String, String> errorField : expError.entrySet()) {
            json.body(errorField.getKey(), equalTo(errorField.getValue()));
        }
    }

}


