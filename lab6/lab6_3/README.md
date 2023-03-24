# Exercise 3:  Custom QG

## Review Questions

### a) Define a custom quality gate to this project. Feel free to mix the metrics but explain your chosen configuration.

| Condition | Threshold |
|-----------|-----------|
| Maintainability Rating | < A |
| Reliability Rating | < A |
| Reliability Remediation Effort | > 3h |
| Security Rating | < A |
| Security Review Rating | < B |
| Critical Issues | > 10 |
| Major Issues | > 30 |
| Technical Debt | > 3d |

### Explanation for Each Condition

- **Maintainability Rating is worse than A:** We chose A as the threshold because it represents a good level of maintainability for the code. If the maintainability rating is worse than A, it could mean that the code is difficult to change and maintain in the future, which could cause problems down the line.

- **Reliability Rating is worse than A:** We chose A as the threshold because it represents a good level of reliability for the code. If the reliability rating is worse than A, it could mean that the code has defects that could cause problems or errors in the system.

- **Reliability Remediation Effort is greater than 3h:** We set this threshold because we believe that any issues with the code should be resolved in a timely manner. Waiting longer than 3 hours to fix a reliability issue could cause the system to become unreliable and unpredictable.

- **Security Rating is worse than A:** We chose A as the threshold because it represents a good level of security for the code. If the security rating is worse than A, it could mean that the code has weaknesses that could be exploited by hackers, putting the system and its users at risk.

- **Security Review Rating is worse than B:** We set this threshold because we believe that thorough security review is essential to ensure the code is as secure as possible. If the security review rating is worse than B, it could mean that the code has not been thoroughly checked for security issues.

- **Critical Issues is greater than 10:** We set this threshold because having more than 10 critical issues in the code could indicate that the system is at risk of serious problems or security breaches.

- **Major Issues is greater than 30:** We set this threshold because having more than 30 major issues in the code could indicate that there are too many significant problems that could make it harder to understand and improve the system.

- **Technical Debt is greater than 3d:** We set this threshold because we believe that technical debt should be addressed promptly. Waiting longer than 3 days to address technical debt could make it harder to fix the issue later and could potentially cause more problems in the system.

## Image reports

### Default quality gate
![Default quality gate](default-quality-gate.png "Default quality gate")

### Custom quality gate
![Custom quality gate](custom-quality-gate.png "Custom quality gate")
