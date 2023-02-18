# Lab 1: Unit testing  (with JUnit 5)  

## Learning objectives
- Identify relevant unit tests to verify the contract of a module.  
- Write and  execute unit tests using the JUnit framework.  
- Link the unit tests results with further analysis tools (e.g.: code coverage)
  
## Key points
- Unit testing is when you (as a programmer) write test code to verify units of (production) code. A unit  
is a small, coherent  subset of a much larger  solution. A true “unit”  should  not depend  on  the  
behavior of  other (collaborating)  modules.  
- Unit tests help the developers to (i) understand the module contract (what to construct); (ii)  
document the intended use of a component;  (iii) prevent regression errors; (iv) increase confidence  
in the code.  
- JUnit and TestNG are popular frameworks for unit testing in Java.
- 
## JUnit best practices: unit test one object at a time**
A vital aspect of unit tests is that they're finely grained. A unit test independently examines each object you create, so that you can isolate problems as soon as they occur. If you put more than one object under test, you can't predict how the objects will interact when changes occur to one or the other. When an object interacts with other complex objects, you can surround the object under test with predict-able test objects. Another form of software test, integration testing, examines how working objects interact with each other.

## Unit testing & JUnit

Unit testing is a software testing technique where individual units or components of the software are tested in isolation to ensure that they work as expected. JUnit is a widely used open-source framework for writing and executing unit tests in Java.

Here are some advantages of unit testing and JUnit:

1. **Catching bugs early**: Unit testing allows you to catch bugs early in the development process, before they become more expensive and time-consuming to fix.
2.  **Ensuring code correctness:** Unit tests ensure that each component of the code behaves as expected and meets the requirements set for it.
3.  **Facilitating refactoring:** Unit tests provide a safety net for refactoring, allowing you to make changes to the code without worrying about introducing new bugs.
4.  **Improving code quality:** Unit testing promotes good programming practices, such as writing modular and reusable code and reducing code complexity.
5.  **Saving time and money:** By catching bugs early, unit testing helps reduce the time and cost of fixing bugs later in the development process.
6.  **Increasing developer confidence:** Unit tests give developers confidence in their code, allowing them to make changes and add new features without worrying about breaking existing functionality.
7.  **Supporting continuous integration:** Unit tests can be integrated into a continuous integration process to ensure that changes made to the codebase do not break existing functionality.
8.  **Promoting collaboration:** Unit tests serve as a common language between developers and stakeholders, facilitating communication and collaboration between different teams.
9.  **Providing documentation:** Unit tests serve as a form of documentation, showing how the code is intended to be used and how it should behave.
  
Overall, unit testing with JUnit is a valuable tool for developers to improve the quality and reliability of their code.
