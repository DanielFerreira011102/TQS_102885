# Exercise 1:  Stack Contract
In this exercise, we implement a stack data structure called TqsStack in Java and write appropriate unit tests to ensure the code works as expected. We adopt a write-the-tests-first workflow and use JUnit 5.x for our testing framework.

## Key Takeaways
This exercise highlights the importance of writing unit tests to ensure code correctness and adopting a write-the-tests-first workflow. By testing the TqsStack contract, we ensure that the implementation behaves as expected and meets the requirements set for it. This exercise also emphasizes the importance of testing for edge cases and error conditions, as well as for expected behavior.

## Steps
1. Create a new maven project for a Java standard application, and update the Java version in the POM.xml file and other dependencies as needed. You can also clone from a sample project.
2. Add the required dependencies to run JUnit 5 tests, such as junit-jupiter and maven-surefire-plugin.
3. Create the required class definition for TqsStack, but leave the method bodies empty for now. The code should compile, but the implementation is yet incomplete (you may need to add dummy return values).
4. Write unit tests that will verify the TqsStack contract. Use the IDE features to generate the testing class and be sure to use JUnit 5.x. The tests will verify several assertions that should evaluate to true for the test to pass.
5. Run the tests and prove that TqsStack implementation is not valid yet (the tests should run and fail for now, the first step in Red-Green-Refactor).
6. Correct or add the missing implementation to TqsStack.
7. Run the unit tests again.
8. Iterate from steps 4 to 7 and confirm that all tests are passing.

## Stack Contract
The TqsStack data structure should have the following methods:

- **push(x):** add an item on the top
- **pop():** remove the item at the top
- **peek():** return the item at the top (without removing it)
- **size():** return the number of items in the stack
- **isEmpty():** return whether the stack has no items

## What to Test
To ensure that the TqsStack implementation is correct, we will test the following assertions:

- A stack is empty on construction.
- A stack has size 0 on construction.
- After n pushes to an empty stack, n > 0, the stack is not empty and its size is n.
- If one pushes x then pops, the value popped is x.
- If one pushes x then peeks, the value returned is x, but the size stays the same.
- If the size is n, then after n pops, the stack is empty and has a size 0.
- Popping from an empty stack does throw a NoSuchElementException.
- Peeking into an empty stack does throw a NoSuchElementException.
- For bounded stacks only: pushing onto a full stack does throw an IllegalStateException.

## Write-the-tests-first workflow

Using write-the-tests-first workflow, also known as test-driven development (TDD), can have several benefits:

1.  **Improved code quality:** By writing tests before writing code, developers are forced to think about the requirements and behavior of the code before they start implementing it. This can lead to more robust, well-designed code.
2.  **Faster feedback:** With TDD, developers get immediate feedback on whether their code is working correctly, since they run the tests frequently. This can help catch and fix errors earlier in the development process, reducing the cost of fixing bugs later.
3.  **Better documentation:** Tests can serve as documentation for how the code should behave. By reading the tests, developers can quickly understand the requirements and expected behavior of the code.
4.  **Easier maintenance:** Tests can make it easier to maintain and refactor code, since changes to the code can be tested to ensure that they do not break existing functionality.

Overall, write-the-tests-first workflow can help improve the quality and maintainability of code, while reducing the cost of development and bug fixing.