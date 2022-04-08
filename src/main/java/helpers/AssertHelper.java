package helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.imifou.jsonschema.module.addon.AddonModule;
import com.github.victools.jsonschema.generator.*;
import com.github.victools.jsonschema.module.jackson.JacksonModule;
import com.github.victools.jsonschema.module.javax.validation.JavaxValidationModule;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.Objects;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

public class AssertHelper {

    @Step("Check a response status code")
    public static void checkStatusCode(Response response, int expectedStatusCode){
        int actualStatusCode = response.getStatusCode();
        Assert.assertEquals(actualStatusCode, expectedStatusCode, "Status code is wrong for the response: " + response.asString());
    }

    @Step("Check response json schema")
    public static void checkResponseJsonSchema(Response response, Class<?> clazz) {
        String expectedSchema = getJsonSchema(clazz);
        response.then().assertThat().body(matchesJsonSchema(expectedSchema));
    }

    private static String getJsonSchema(Class<?> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        AddonModule module = new AddonModule();
        SchemaGeneratorConfigBuilder configBuilder = new SchemaGeneratorConfigBuilder(objectMapper, SchemaVersion.DRAFT_2019_09, OptionPreset.PLAIN_JSON)
                .with(module);
        configBuilder
                .with(new AddonModule())
                .with(Option.NONSTATIC_NONVOID_NONGETTER_METHODS)
                .with(new JacksonModule())
                .with(new JavaxValidationModule());
        SchemaGeneratorConfig config = configBuilder.build();
        SchemaGenerator generator = new SchemaGenerator(config);
        JsonNode jsonSchema = generator.generateSchema(clazz);

        return jsonSchema.toString();
    }

    @Step("Check that two values are equal")
    public static <T> void compareTwoValues(T firstValue, T secondValue) {
        Assert.assertEquals(firstValue, secondValue, String.format("Values %s и %s are not equal",
                Objects.isNull(firstValue) ? "null" : firstValue,
                Objects.isNull(secondValue) ? "null" : secondValue));
    }
}
