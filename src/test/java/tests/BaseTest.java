package tests;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BaseTest {

    protected Properties properties = new Properties();
    protected Properties authProperties = new Properties();

    {
        try {
            properties.load(new FileInputStream("src/test/resources/application.properties"));
            authProperties.load(new FileInputStream("src/test/resources/auth.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected final String BASE_URL = properties.getProperty("baseUrl");
    protected final String TOKEN = properties.getProperty("token");
}
