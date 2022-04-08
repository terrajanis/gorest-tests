package providers;

import org.apache.commons.lang3.RandomUtils;
import org.testng.annotations.DataProvider;

import static factories.UserFactory.getUser;
import static utils.RandomUtil.getRandomEmail;

public class DataProviders {

    @DataProvider(name = "usersPositive")
    public static Object[][] getUsersPositive() {
        return new Object[][]{
                {getUser("Name" + RandomUtils.nextInt(1, 100), getRandomEmail(),"female", "active")},
                {getUser("Name" + RandomUtils.nextInt(1, 100), getRandomEmail(),"male", "inactive")}
        };
    }

    @DataProvider(name = "usersNegative")
    public static Object[][] getUsersNegative() {
        return new Object[][]{
                {getUser("", getRandomEmail(),"female", "active"), "name", "can't be blank"},
                {getUser("Name" + RandomUtils.nextInt(1, 100), "123123","male", "active"), "email","is invalid"},
                {getUser("Name" + RandomUtils.nextInt(1, 100), "","male", "active"),"email", "can't be blank"},
                {getUser("Name" + RandomUtils.nextInt(1, 100), getRandomEmail(),"", "active"), "gender","can't be blank"},
                {getUser("Name" + RandomUtils.nextInt(1, 100), getRandomEmail(),"female", ""),"status", "can't be blank"},
        };
    }
}
