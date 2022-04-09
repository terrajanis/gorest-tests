package database.entities;

import lombok.Data;

@Data
public class UsersEntity {

    private Long id;
    private String name;
    private String email;
    private String gender;
    private String status;
}
