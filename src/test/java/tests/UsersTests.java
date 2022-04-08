package tests;

import helpers.ApiHelper;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import models.User;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static helpers.AssertHelper.*;

@Feature("Users")
public class UsersTests extends BaseTest{

    private String path = BASE_URL + "/public/v2/users";

    @Test
    @Description("Send get request to get all users without pagination")
    public void getAllUsers(){
      final Response response = ApiHelper.get(path);
        checkStatusCode(response, HttpStatus.SC_OK);
        checkResponseJsonSchema(response, User[].class);
        List<User> usersFromResponse = Arrays.asList(response.as(User[].class));
        compareTwoValues(usersFromResponse.size(), 20);
    }

    @Test
    @Description("Send get request to get all users with pagination")
    public void getAllUsersWithPagination(){
        final Response response = ApiHelper.get(path, Collections.singletonMap("page", "2"));
        checkStatusCode(response, HttpStatus.SC_OK);
        checkResponseJsonSchema(response, User[].class);
        List<User> usersFromResponse = Arrays.asList(response.as(User[].class));
        compareTwoValues(usersFromResponse.size(), 20);
    }

    @Test
    @Description("Send get request to get all users for a non-existent page")
    public void getAllUsersForNonExistentPage(){
        final Response response = ApiHelper.get(path, Collections.singletonMap("page", "2000000"));
        checkStatusCode(response, HttpStatus.SC_OK);
        checkResponseJsonSchema(response, User[].class);
        List<User> usersFromResponse = Arrays.asList(response.as(User[].class));
        Assert.assertTrue(usersFromResponse.isEmpty(), "List of users isn't empty");
    }
}
