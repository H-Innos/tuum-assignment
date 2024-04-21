## Running the project
Navigate to the root directory and run 
```bash
docker-compose up
```
The application will be running at http://localhost:8080.

RabbitMQ console is accessible at http://localhost:15672
with username: 'myuser' and password: 'secret'.

## Endpoints
### GET /accounts/{accountId}
#### Description:
Retrieves details of a specific account based on the provided account ID.

#### Parameters:
accountId: The unique identifier of the account to retrieve details for.

#### Response:
Status Code: 200 OK 

Content Type: application/json

### POST /accounts
#### Description:
Creates a new account with the provided details.

#### Request Body:
- customerId: The ID of the customer associated with the account.
- balance: The initial balance of the account.
- currencies: list of currencies.
#### Response:
Status Code: 201 Created

Content Type: application/json
### GET /transactions/{accountId}
Description:
Retrieves all transactions associated with the specified account.

#### Parameters:
- accountId: The unique identifier of the account to retrieve transactions for.
#### Response:
Status Code: 200 OK

Content Type: application/json
### POST /transactions
#### Description:
Creates a new transaction for the specified account.
#### Request Body:
- accountId: The unique identifier of the account to perform the transaction.
- amount: The amount of the transaction.
- currency: The currency of the transaction.
- direction: The direction of the transaction (IN or OUT).
- description: A description of the transaction.

#### Response:
Status Code: 201 Created

Content Type: application/json

## Project description
`controller.AccountApi.java` is the main controller, which specifies all the endpoints. For POST requests, it takes in a JSON object in the request body, which contains the necessary data. Data objects are created based on the request object data and passed to the service layer. 

`service.AccountService.java` manages all business logic and `repository.AccountRepository.java` handles data access. When the operation is successful, the service layer returns the data object, and the controller maps it into a response object, which is sent back in the HTTP response. 

`exception.ApiExceptionHandler.java` handles all exceptions that may happen, and sends a custom error response to the client. 

Note: I had trouble with setting up a MyBatis implementation, so I used the Spring Data JPA instead to be able to deliver a working project. 

## Answers
1. The average response time for creating a transaction is ~17ms, which means that the application can handle ~59 requests per second.
2. When horizontally scaling applications, you have to ensure that application instances have minimal reliance on server-side state. Additionally, a load balancer needs to be implemented, to distribute incoming requests evenly.
   Shared resources, such as message queues and databases may become bottlenecks, so they should be swapped out for scalable alternatives.
