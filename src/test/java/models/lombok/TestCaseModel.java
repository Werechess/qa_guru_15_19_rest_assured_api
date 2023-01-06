package models.lombok;

import lombok.Data;

@Data
public class TestCaseModel {

    private Integer id;
    private String name;
    private Boolean automated;
    private Boolean external;
    private Double createdDate;
    private String statusName;
    private String statusColor;
}
