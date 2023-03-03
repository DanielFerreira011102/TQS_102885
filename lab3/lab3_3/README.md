# Exercise 3: Integration test

This exercise involves adapting the integration test of the previous project to use a real database (e.g., MySQL). The process involves running a MySQL instance, adding the MySQL dependency to the POM, and configuring the connection properties file in the resources of the "test" part of the project.

## Key Takeaways
- Integration testing with a real database is essential to verify the behavior of the entire system
- Running a MySQL instance (e.g., using Docker) allows for testing with a real database without affecting the production environment
- Adding the MySQL dependency to the POM and configuring the connection properties file allows the application to connect to the MySQL instance
- Using `@TestPropertySource` and deactivating `@AutoConfigureTestDatabase` helps in configuring the integration test with the real database

For more details, check out the source code and the test cases in the repository.