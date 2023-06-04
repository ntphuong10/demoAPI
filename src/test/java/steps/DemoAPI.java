package steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import core.BaseAPI;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;

import static core.ComponentConstant.*;

public class DemoAPI {
  JsonNode customerData;
  JsonNode expectedJsonNode;

  JsonNode actualJsonNode;

  ObjectNode actualObjectNode;
  ObjectNode objectNodeDeposit;
  BaseAPI baseAPI = new BaseAPI();
  Response response;

  int idActual;
  ObjectMapper mapper = new ObjectMapper();

  @Given("I have data of customer booking")
  public void iHaveDataOfCustomerBooking() throws IOException {
    File customerBooking = new File(CUSTOMER_DATA_FILE);
    customerData = mapper.readTree(customerBooking);
  }

  @When("I call API to create booking information")
  public void iCallAPIToCreateBookingInformation() {
    response = baseAPI.postAPI(API_URL, BASE_PATH, customerData);
  }

  @Then("the API should return status {int} and all fields display correctly")
  public void theAPIShouldReturnStatusAndAllFieldsDisplayCorrectly(int statusCode) throws IOException {
    Assert.assertEquals(statusCode, response.getStatusCode());
    actualJsonNode = mapper.readTree(response.getBody().prettyPrint());
    actualObjectNode = (ObjectNode) actualJsonNode;
    idActual = actualObjectNode.get("bookingid").asInt();
    File customerData = new File(CUSTOMER_DATA_EXPECTED_FILE);
    expectedJsonNode = mapper.readTree(customerData);
    ObjectNode expectedObjectNode = (ObjectNode) expectedJsonNode;
    expectedObjectNode.put("bookingid", idActual);
    expectedJsonNode = expectedObjectNode;
    Assert.assertTrue(expectedJsonNode.equals(actualJsonNode));
  }

  @When("I call API to {string} customer booking information with id created above")
  public void iCallAPIToCustomerBookingInformationWithIdCreatedAbove(String action) throws IOException {
    File token = new File(USER_GET_TOKEN);
    JsonNode getUserToken = mapper.readTree(token);
    Response userToken = baseAPI.getToken(API_URL, BASE_TOKEN, getUserToken);
    JsonNode jsonNodeToken = mapper.readTree(userToken.getBody().prettyPrint());
    ObjectNode objectNodeToken = (ObjectNode) jsonNodeToken;
    String realToken = objectNodeToken.get("token").toString();
    if (action.equals("update")) {
      File userDeposit = new File(DEPOSIT_UPDATE);
      JsonNode jsonNodeDeposit = mapper.readTree(userDeposit);
      objectNodeDeposit = (ObjectNode) jsonNodeDeposit;
      response = baseAPI.putAPI(API_URL, BASE_PATH, realToken, jsonNodeDeposit, String.valueOf(idActual));
    } else {
      response = baseAPI.deleteAPI(API_URL, BASE_PATH, realToken, String.valueOf(idActual));
    }
  }

  @When("I call API to get customer booking information with id created above")
  public void iCallAPIToGetCustomerBookingInformationWithIdCreatedAbove() {
    response = baseAPI.getAPI(API_URL, BASE_PATH, String.valueOf(idActual));
  }

  @Then("the API should return status {int} and response display correctly")
  public void theAPIShouldReturnStatusAndResponseDisplayCorrectly(int statusCode) throws JsonProcessingException {
    Assert.assertEquals(statusCode, response.getStatusCode());
    JsonNode actualResult = mapper.readTree(response.getBody().prettyPrint());
    Assert.assertTrue(customerData.equals(actualResult));
  }

  @Then("the API should return status {int} and data is deleted successfully")
  public void theAPIShouldReturnStatusAndDataIsDeletedSuccessfully(int statusCode) throws JsonProcessingException {
    Assert.assertEquals(statusCode, response.getStatusCode());
    Response response = baseAPI.getAPI(API_URL, BASE_PATH);
    JsonNode arrayResponse = mapper.readTree(response.getBody().prettyPrint());
    boolean check = true;
    for (JsonNode obj : arrayResponse) {
      if (obj.get("bookingid").toString().equals(idActual)) {
        check = false;
        break;
      }
    }
    Assert.assertTrue(check);
  }

  @Then("the API should return status {int} and data is updated successfully")
  public void theAPIShouldReturnStatusAndDataIsUpdatedSuccessfully(int statusCode) throws JsonProcessingException {
    Assert.assertEquals(statusCode, response.getStatusCode());
    String actualDeposit = mapper.readTree(response.getBody().prettyPrint()).get("depositpaid").toString();
    String expectedDeposit = objectNodeDeposit.get("depositpaid").toString();
    Assert.assertTrue(expectedDeposit.equals(actualDeposit));
  }
}
