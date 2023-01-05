package models.lombok;

import lombok.Data;

@Data
public class ResponseCreateUserModel {

    private String name,
            job,
            id,
            createdAt;
}
