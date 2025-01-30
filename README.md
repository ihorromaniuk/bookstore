![bookshelf.jpg](src/main/resources/static/bookshelf.jpg)
# Book Store
This project is an API for managing and using book store. 
 As an admin you can manage state of your book store like adding/updating/removing books 
and categories(genres) and as a user you can look for books, adding them to your shopping cart and 
creating orders based on your shopping cart.

## Technology stack
- **Java Core**
- **Database**: Hibernate, JDBC, MySQL
- **Spring**: Spring Core, Spring Web, Spring Security, Spring Data
- **Web Development**: Tomcat, JSON
- **Tools**: JUnit, Maven, Liquibase, Mockito, Test containers, Docker / Docker compose, Swagger

## Launch instructions
After cloning this project on your machine the only requirement is to have running docker,
but first you need to create .env file in the root folder of this project to specify docker ports, 
local ports and info for database the project will be running with (database is pulled from 
DockerHUB so there is no need for you to install it locally), example of .env file: 
```dotenv
MYSQL_USER=mysql_user
MYSQL_PASSWORD=mysql1234
MYSQL_DATABASE=bookstore
MYSQL_LOCAL_PORT=3307
MYSQL_DOCKER_PORT=3306

MYSQL_ROOT_PASSWORD=root1234

SPRING_LOCAL_PORT=8081
SPRING_DOCKER_PORT=8080
DEBUG_PORT=5005
```
then run this command (default docker server URI is http://localhost:8081, you can specify it 
in recently created .env file )
```console
docker-compose up
```
if you want to run project locally then you also need to have installed Java 21+ version 
(database is still pulled from DockerHUB), in this case default server URI will be
http://localhost:8080, you can specify port in 
[application.yml](src/main/resources/application.yml) file

## Functionality (endpoints)
After you start an application, by default you will have registered admin user with credentials:
```json
{
 "email": "admin@example.com",
 "password": "12345678"
}
```

**\*server URI*** /swagger-ui/index.html - Swagger documentation URL to visit in your browser to 
get full information about endpoints
### Non authenticated users endpoints
- POST: /api/auth/register - register new user
- POST: /api/auth/login - authenticate(login)
### Admin endpoints
- POST: /api/books/ - create new book
- PUT: /api/books/{id} - update book in database
- DELETE: /api/books/{id} - remove book (soft delete)
- POST: /api/categories - create new category
- PUT: /api/categories/{id} - update category in database
- DELETE: /api/categories/{id} - remove category (soft delete)
### User endpoints
- GET: /api/books - get list of available books
- GET: /api/books/{id} - get specific book
- POST: /api/books/search - get filtered list of books
- GET: /api/categories - get list of available categories
- GET: /api/categories/{id} - get specific category
- GET: /api/categories/{id}/books - get books filtered by category
- GET: /api/cart - get current state of your shopping cart
- POST: /api/cart - add item to your shopping cart
- PUT: /api/cart/items/{cartItemId} - update item in your shopping cart
- DELETE: /api/cart/items/{cartItemId} - remove item from your shopping cart
- POST: /api/orders - create an order from your shopping cart
- GET: /api/orders - get list of your orders
- GET: /api/orders/{orderId}/items - get list of items in specific order
- GET: /api/orders/{orderId}/items/{itemId} - get info about specific order item

## Postman Collection
In Postman, you can import .json [file](BookStore.postman_collection.json) from the root of this
project to easily start working with all the endpoints this project has (use [api_url]() postman 
collection variable to specify server URI for all endpoints).