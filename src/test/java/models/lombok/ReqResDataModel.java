package models.lombok;

import lombok.Data;

@Data
public class ReqResDataModel {

    private int id;
    private String email,
            first_name,
            last_name,
            avatar;
}
