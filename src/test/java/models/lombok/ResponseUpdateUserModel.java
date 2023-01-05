package models.lombok;

import lombok.Data;

@Data
public class ResponseUpdateUserModel {

    private String name,
            job,
            updatedAt;
}
