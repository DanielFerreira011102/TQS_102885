# Exercise 1:  Local analysis

## Review Questions

### a) Has my project passed the defined quality gate? 
Yes, my project has passed the defined quality gate, which means that it meets the predefined standards and quality criteria set for the project. However, during the code analysis using Sonarqube, we identified 1 possible security hotspot and 26 code smells.

- A security hotspot is a potential security vulnerability that may require attention to prevent security breaches.

- Code smells are not necessarily bugs or defects, but they indicate poor code design or structure, which can lead to maintenance issues and future bugs.

- The total debt time of 1h26 refers to the estimated time required to fix all the issues found by Sonarqube. This is based on the effort required to fix the detected issues, such as the complexity of the code, the scope of the changes, and the required testing.

### b) Explore the analysis results and complete with a few sample issues, as applicable.

| Issue                  | Problem description                                                                          | How to solve                                    |
|------------------------|----------------------------------------------------------------------------------------------|-------------------------------------------------|
| Bug                    | -                                                                                            | -                                               |
| Vulnerability          | -                                                                                            | -                                               |
| Code smell (major)     | Invoke method(s) only conditionally.                                                         | Invoke method(s) only conditionally             |                                             
| Code smell (major)     | Refactor the code in order to not assign to this loop counter from within the loop body.     | Do not update counter in the body of the loop   |                            