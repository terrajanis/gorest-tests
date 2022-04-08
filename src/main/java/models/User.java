package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.imifou.jsonschema.module.addon.annotation.JsonSchema;
import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @EqualsAndHashCode.Exclude
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
