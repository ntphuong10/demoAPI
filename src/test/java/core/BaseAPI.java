package core;

import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;

import java.util.HashMap;
import java.util.Map;

public class BaseAPI {
  public Response postAPI(String baseURL, String basePath, JsonNode jsonNode) {
    Response response = SerenityRest.given()
            .contentType("application/json")
            .baseUri(baseURL)
            .basePath(basePath)
            .body(jsonNode)
            .when()
            .post()
            .andReturn();
    return response;
  }

  public Response getAPI(String baseURL, String basePath, String id) {
    Response response = SerenityRest.given()
            .contentType("application/json")
            .baseUri(baseURL)
            .basePath(basePath)
            .when()
            .get(id)
            .andReturn();
    return response;
  }

  public Response getAPI(String baseURL, String basePath) {
    Response response = SerenityRest.given()
            .contentType("application/json")
            .baseUri(baseURL)
            .basePath(basePath)
            .when()
            .get()
            .andReturn();
    return response;
  }
  public Response getToken(String baseURL, String baseToken, JsonNode userToken) {
    Response token = SerenityRest.given()
            .contentType("application/json")
            .baseUri(baseURL)
            .basePath(baseToken)
            .body(userToken)
            .when()
            .post()
            .andReturn();
    return token;
  }

  public static Map<String, String> getHeader(String token) {
    Map<String, String> headers = new HashMap<>();
    headers.put("cookie", token);
    headers.put("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=");
    return headers;
  }
  public Response putAPI(String baseURl, String basePath,String token, JsonNode jsonNode, String id) {
    Response response = SerenityRest.given()
            .contentType("application/json")
            .headers(getHeader(token))
            .baseUri(baseURl)
            .basePath(basePath)
            .body(jsonNode)
            .when()
            .put(id)
            .andReturn();
    return response;
  }
  public Response deleteAPI(String baseURL, String basePath, String token, String id) {
    Response response = SerenityRest.given()
            .contentType("application/json")
            .headers(getHeader(token))
            .baseUri(baseURL)
            .basePath(basePath)
            .when()
            .delete(id)
            .andReturn();
    return response;
  }
}