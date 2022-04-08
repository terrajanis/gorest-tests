package helpers;

import allure.AllureRestAssuredFilter;
import io.qameta.allure.Step;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;


public class ApiHelper {

    private static final Logger logger = LogManager.getLogger(ApiHelper.class);
    private static final String APPLICATION_JSON = "application/json";


    private static void logResponse(Response response) {
        logger.debug("Response code: " + response.getStatusCode());
        logger.debug("Response headers: " + response.getHeaders());
        logger.debug("Response cookies: " + response.getCookies());
        logger.debug("Response message: " + response.asString());
    }

    @Step("Send post request")
    public static Response post(String path, Object o) {
        Response response = getBaseRequest()
                .body(o)
                .post(path);
        logResponse(response);
        return response;
    }

    @Step("Send post request")
    public static Response post(String path, Object o, Map<String,String> headers) {
        Response response = getBaseRequest()
                .headers(headers)
                .body(o)
                .post(path);
        logResponse(response);
        return response;
    }

    @Step("Send get request")
    public static Response get(String path) {
        Response response = getBaseRequest()
                .get(path);
        logResponse(response);
        return response;
    }

    @Step("Send get request")
    public static Response getWithAuth(String path, Map<String, String> headers) {
        Response response = getBaseRequest()
                .headers(headers)
                .get(path);
        logResponse(response);
        return response;
    }

    @Step("Send get request")
    public static Response get(String path, Map<String, String> params) {
        Response response = getBaseRequest()
                .queryParams(params)
                .get(path);
        logResponse(response);
        return response;
    }

    @Step("Send put request")
    public static Response put(String path, Object o, Map<String,String> headers) {
        Response response = getBaseRequest()
                .headers(headers)
                .body(o)
                .put(path);
        logResponse(response);
        return response;
    }

    @Step("Send put request")
    public static Response put(String path, Object o) {
        Response response = getBaseRequest()
                .body(o)
                .put(path);
        logResponse(response);
        return response;
    }

    @Step("Send patch request")
    public static Response patch(String path, Object o, Map<String,String> headers) {
        Response response = getBaseRequest()
                .headers(headers)
                .body(o)
                .patch(path);
        logResponse(response);
        return response;
    }

    @Step("Send patch request")
    public static Response patch(String path, Object o) {
        Response response = getBaseRequest()
                .body(o)
                .patch(path);
        logResponse(response);
        return response;
    }


    @Step("Send delete request")
    public static Response delete(String path, Map<String, String> headers, Map<String, String> cookies) {
        Response response = getBaseRequest()
                .headers(new Headers(getHeaders(headers)))
                .cookies(cookies)
                .delete(path);
        logResponse(response);
        return response;
    }

    private static RequestSpecification getBaseRequest() {
        return  given().log().all(true)
                .filter(new AllureRestAssuredFilter())
                .contentType(APPLICATION_JSON);
    }

    private static List<Header> getHeaders(Map<String, String> headers) {
        List<Header> headersList = new ArrayList<>();
        for (Map.Entry<String, String> header : headers.entrySet()) {
            headersList.add(new Header(header.getKey(), header.getValue()));
        }
        return  headersList;
    }
}
