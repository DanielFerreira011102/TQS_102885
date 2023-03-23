# Exercise 1:  Local analysis

key=sqp_7d0b40583cbcd2f82bca876d1fca4a8fe82c11a2

cmd=mvn clean verify sonar:sonar -Dsonar.projectKey=getting-started-sonarqube -Dsonar.host.url=http://localhost:9000 -Dsonar.login=sqp_7d0b40583cbcd2f82bca876d1fca4a8fe82c11a2

## Review Questions

### a) Has my project passed the defined quality gate? 
TODO

### b) Explore the analysis results and complete with a few sample issues, as applicable.

| Issue                  | Problem description                                                                          | How to solve                                    |
|------------------------|----------------------------------------------------------------------------------------------|-------------------------------------------------|
| Bug                    | -                                                                                            | -                                               |
| Vulnerability          | -                                                                                            | -                                               |
| Code smell (major)     | Invoke method(s) only conditionally.                                                         | Invoke method(s) only conditionally             |                                             
| Code smell (major)     | Refactor the code in order to not assign to this loop counter from within the loop body.     | Do not update counter in the body of the loop   |                            