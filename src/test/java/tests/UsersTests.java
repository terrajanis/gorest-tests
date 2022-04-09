package tests;

import database.queries.UsersQueries;
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
        UsersQueries.INSTANCE.addUser(userFromResponse);
        Assert.assertNotNull(UsersQueries.INSTANCE.getUserByName(userFromResponse.getName()), "User isn't added");
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

    @Test(dependsOnMethods = "getExistingUser",
            dataProvider = "usersPositive",
            dataProviderClass = DataProviders.class)
    @Description("Send put request to edit user")
    public void editUser(User user) {
        final Response response = ApiHelper.put(path + "/" + userFromResponse.getId(), user, Collections.singletonMap("Authorization", "Bearer " + BASE_TOKEN));
        checkStatusCode(response, HttpStatus.SC_OK);
        checkResponseJsonSchema(response, User.class);
        User editedUser = response.as(User.class);
        compareTwoValues(editedUser, user);
    }

    @Test(dependsOnMethods = "getExistingUser",
            dataProvider = "usersNegative",
            dataProviderClass = DataProviders.class)
    @Description("Send put request to edit user with invalid data")
    public void editUserWithInvalid(User user, String field, String message) {
        final Response response = ApiHelper.put(path + "/" + userFromResponse.getId(), user, Collections.singletonMap("Authorization", "Bearer " + BASE_TOKEN));
        checkStatusCode(response, HttpStatus.SC_UNPROCESSABLE_ENTITY);
        checkError(response, field, message);
    }

    @Test(dependsOnMethods = "getExistingUser")
    @Description("Send put request to edit user without authorization")
    public void editUserWithoutAuth() {
        User user = getUser("Name", getRandomEmail(), "female", "active");
        final Response response = ApiHelper.put(path + "/" + userFromResponse.getId(), user);
        checkStatusCode(response, HttpStatus.SC_NOT_FOUND); // looks like a bug, should be 401
    }

    @Test
    @Description("Send put request to edit non-existent user")
    public void editNonExistentUser() {
        User user = getUser("Name", getRandomEmail(), "female", "active");
        final Response response = ApiHelper.put(path + "/0", user, Collections.singletonMap("Authorization", "Bearer " + BASE_TOKEN));
        checkStatusCode(response, HttpStatus.SC_NOT_FOUND);
    }

    @Test(dependsOnMethods = "editUser",
            dataProvider = "usersPositive",
            dataProviderClass = DataProviders.class)
    @Description("Send patch request to edit user")
    public void editUserWithPatch(User user) { // PATCH works the same as PUT, partial update isn't supported
        final Response response = ApiHelper.patch(path + "/" + userFromResponse.getId(), user, Collections.singletonMap("Authorization", "Bearer " + BASE_TOKEN));
        checkStatusCode(response, HttpStatus.SC_OK);
        checkResponseJsonSchema(response, User.class);
        User editedUser = response.as(User.class);
        compareTwoValues(editedUser, user);
    }

    @Test(dependsOnMethods = "getExistingUser",
            dataProvider = "usersNegative",
            dataProviderClass = DataProviders.class)
    @Description("Send patch request to edit user with invalid data")
    public void editUserWithInvalidWithPatch(User user, String field, String message) {
        final Response response = ApiHelper.patch(path + "/" + userFromResponse.getId(), user, Collections.singletonMap("Authorization", "Bearer " + BASE_TOKEN));
        checkStatusCode(response, HttpStatus.SC_UNPROCESSABLE_ENTITY);
        checkError(response, field, message);
    }

    @Test(dependsOnMethods = "getExistingUser")
    @Description("Send patch request to edit user without authorization")
    public void editUserWithoutAuthWithPatch() {
        User user = getUser("Name", getRandomEmail(), "female", "active");
        final Response response = ApiHelper.patch(path + "/" + userFromResponse.getId(), user);
        checkStatusCode(response, HttpStatus.SC_NOT_FOUND); // looks like a bug, should be 401
    }

    @Test
    @Description("Send patch request to edit non-existent user")
    public void editNonExistentUserWithPatch() {
        User user = getUser("Name", getRandomEmail(), "female", "active");
        final Response response = ApiHelper.patch(path + "/0", user, Collections.singletonMap("Authorization", "Bearer " + BASE_TOKEN));
        checkStatusCode(response, HttpStatus.SC_NOT_FOUND);
    }

    @Test(dependsOnMethods = "editUserWithPatch")
    @Description("Send delete request to delete user")
    public void deleteUser() {
        final Response response = ApiHelper.delete(path + "/" + userFromResponse.getId(), Collections.singletonMap("Authorization", "Bearer " + BASE_TOKEN));
        checkStatusCode(response, HttpStatus.SC_NO_CONTENT);
        final Response getResponse = ApiHelper.getWithAuth(path + "/" + userFromResponse.getId(), Collections.singletonMap("Authorization", "Bearer " + BASE_TOKEN));
        checkStatusCode(getResponse, HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @Description("Send delete request to delete non-existent user")
    public void deleteNonExistentUser() {
        final Response response = ApiHelper.delete(path + "/0", Collections.singletonMap("Authorization", "Bearer " + BASE_TOKEN));
        checkStatusCode(response, HttpStatus.SC_NOT_FOUND);
    }

    @Test(dependsOnMethods = "editUserWithPatch")
    @Description("Send delete request to delete user without authorization")
    public void deleteUserWithoutAuth() {
        final Response response = ApiHelper.delete(path + "/" + userFromResponse.getId());
        checkStatusCode(response, HttpStatus.SC_NOT_FOUND); // looks like a bug, should be 401
    }
}
