package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.imifou.jsonschema.module.addon.annotation.JsonSchema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @JsonSchema(required = true)
    private Long id;

    @JsonSchema(required = true)
    private String name;

    @JsonSchema(required = true)
    private String email;

    @JsonSchema(required = true)
    private String gender;

    @JsonSchema(required = true)
    private String status;

}
