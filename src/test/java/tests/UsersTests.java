package tests;

import helpers.ApiHelper;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import models.User;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;
import providers.DataProviders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static factories.UserFactory.getUser;
import static helpers.AssertHelper.*;
import static utils.RandomUtil.getRandomEmail;

@Feature("Users")
public class UsersTests extends BaseTest {

    private String path = BASE_URL + "/public/v2/users";
    private User userFromResponse;

    @Test
    @Description("Send get request to get all users without pagination")
    public void getAllUsers() {
        final Response response = ApiHelper.get(path);
        checkStatusCode(response, HttpStatus.SC_OK);
        checkResponseJsonSchema(response, User[].class);
        List<User> usersFromResponse = Arrays.asList(response.as(User[].class));
        compareTwoValues(usersFromResponse.size(), 20);
    }

    @Test
    @Description("Send get request to get all users with pagination")
    public void getAllUsersWithPagination() {
        final Response response = ApiHelper.get(path, Collections.singletonMap("page", "2"));
        checkStatusCode(response, HttpStatus.SC_OK);
        checkResponseJsonSchema(response, User[].class);
        List<User> usersFromResponse = Arrays.asList(response.as(User[].class));
        compareTwoValues(usersFromResponse.size(), 20);
    }

    @Test
    @Description("Send get request to get all users for a non-existent page")
    public void getAllUsersForNonExistentPage() {
        final Response response = ApiHelper.get(path, Collections.singletonMap("page", "2000000"));
        checkStatusCode(response, HttpStatus.SC_OK);
        checkResponseJsonSchema(response, User[].class);
        List<User> usersFromResponse = Arrays.asList(response.as(User[].class));
        Assert.assertTrue(usersFromResponse.isEmpty(), "List of users isn't empty");
    }

    @Test(dataProvider = "usersPositive",
            dataProviderClass = DataProviders.class)
    @Description("Send post request to create a user")
    public void createUser(User user) {
        final Response response = ApiHelper.post(path, user, Collections.singletonMap("Authorization", "Bearer " + BASE_TOKEN));
        checkStatusCode(response, HttpStatus.SC_CREATED);
        checkResponseJsonSchema(response, User.class);
        userFromResponse = response.as(User.class);
        compareTwoValues(userFromResponse, user);
    }

    @Test(dataProvider = "usersNegative",
            dataProviderClass = DataProviders.class)
    @Description("Send post request to create a user with invalid params")
    public void createUserWithInvalidParams(User user, String field, String message) {
        final Response response = ApiHelper.post(path, user, Collections.singletonMap("Authorization", "Bearer " + BASE_TOKEN));
        checkStatusCode(response, HttpStatus.SC_UNPROCESSABLE_ENTITY);
        checkError(response, field, message);
    }

    @Test(dependsOnMethods = "createUser")
    @Description("Send post request to create a user with existing email")
    public void createUserWithExistingEmail() {
        final Response response = ApiHelper.post(path, userFromResponse, Collections.singletonMap("Authorization", "Bearer " + BASE_TOKEN));
        checkStatusCode(response, HttpStatus.SC_UNPROCESSABLE_ENTITY);
        checkError(response, "email", "has already been taken");
    }

    @Test
    @Description("Send post request to create a user without authorization")
    public void createUserWithoutAuth() {
        User user = getUser("Name", getRandomEmail(), "female", "active");
        final Response response = ApiHelper.post(path, user);
        checkStatusCode(response, HttpStatus.SC_UNAUTHORIZED);
    }

    @Test(dependsOnMethods = "createUser")
    @Description("Send get request to get user")
    public void getExistingUser() {
        final Response response = ApiHelper.getWithAuth(path + "/" + userFromResponse.getId(), Collections.singletonMap("Authorization", "Bearer " + BASE_TOKEN));
        checkStatusCode(response, HttpStatus.SC_OK);
        checkResponseJsonSchema(response, User.class);
        User receivedUser = response.as(User.class);
        compareTwoValues(receivedUser, userFromResponse);
    }

    @Test(dependsOnMethods = "createUser")
    @Description("Send get request to get user without authorization")
    public void getUserWithoutAuth() {
        final Response response = ApiHelper.get(path + "/" + userFromResponse.getId());
        checkStatusCode(response, HttpStatus.SC_NOT_FOUND); // looks like a bug, should be 401
    }

    @Test
    @Description("Send get request to get non-existent user")
    public void getNonExistentUser() {
        final Response response = ApiHelper.get(path + "/0", Collections.singletonMap("Authorization", "Bearer " + BASE_TOKEN));
        checkStatusCode(response, HttpStatus.SC_NOT_FOUND);
    }
}
