package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import org.testng.Assert;


@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private String name;

    private String email;

    private String gender;

    private String status;

    public void compareWithResponse(UserFromResponse userFromResponse) {
        Assert.assertEquals(this.getEmail(), userFromResponse.getEmail(), "Emails aren't equal");
    }
}
