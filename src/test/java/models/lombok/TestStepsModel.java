package models.lombok;

import lombok.Data;

import java.util.List;

@Data
public class TestStepsModel {
    private List<TestStepModel> steps;
}
