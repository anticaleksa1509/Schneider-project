Auth Service (Spring Boot, Java): 
A standalone microservice for authentication and authorization.
I implemented manual token generation (with expiration, signature, roles/permissions) and a custom authorization mechanism based on annotations (e.g., @RequiresRole, @RequiresPermission).
Using reflection and aspect-oriented programming (AOP), the system inspects annotations at runtime and validates user access before executing controller/service methods.
The service is stateless (Bearer tokens in headers), and password handling is secured with hashing (e.g., BCrypt).
