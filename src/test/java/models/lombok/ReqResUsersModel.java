package models.lombok;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReqResUsersModel {

    private int page,
            per_page,
            total,
            total_pages;
    private List<ReqResDataModel> data;
}
