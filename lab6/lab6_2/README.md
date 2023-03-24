# Exercise 2:  Technical debt (Cars)

## Review Questions

### a) Take note of the technical debt found. Explain what this value means.
SonarQube analysis showed a "0 debt time", it means that there is no technical debt in my project. Debt time represents the amount of time required to fix all the code issues that were identified.

However, the presence of one vulnerability related to the use of persistence entities as arguments of `@RequestMapping` methods suggests that there is a potential source of technical debt that needs to be addressed.

To eliminate that technical debt, the code needs to be refactored by replacing the persistence entity `Car` with a simple DTO object `CarDto`.

### d) How many lines are “not covered”? And how many conditions?
| Metric                | Value     |
|-----------------------|-----------|
| Coverage              | 88.4%     |
| Lines to Cover        | 41        |
| Uncovered Lines       | 4         |
| Line Coverage         | 90.2%     |
| Conditions to Cover   | 2         |
| Uncovered Conditions  | 1         |
| Condition Coverage    | 50.0%     |


The `Uncovered Lines` metric shows that there are 4 lines that were not executed during the testing process. This means that the code paths associated with those lines were not tested and could potentially contain defects or bugs.

The `Uncovered Conditions` metric indicates that 1 of the 2 total conditions was not executed during testing. This means that the conditional statement associated with that condition was not evaluated during testing and could potentially contain bugs or logic errors.