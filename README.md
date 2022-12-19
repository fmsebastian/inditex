# Inditex

##### Inditex technical POC

This is a POC for Inditex client.  
Initial requisites can be found in file *PruebaTecnicaInditexPlataforma.txt*

The project has been built with Maven (v3.8.6), SpringBoot (v2.7.6) and Java 11.

## Run project

To run your project locally, you can use the following command (assuming you have installed maven):

> mvn spring-boot:run

You can also run it using java command after maven compilation:

> java -jar ./target/inditex-0.0.1-SNAPSHOT.jar

For further information about running spring-boot project, you can visit:
https://docs.spring.io/spring-boot/docs/1.5.22.RELEASE/reference/html/using-boot-running-your-application.html

## Test

Once the project is running, the application exposes an api http GET endpoint in localhost:8088.  
You can change the port in *application.yaml* (server.port)

URL example:

> localhost:8088/api/v1/productprices?brandId=1&productId=35455&applicationDate=2020-06-15T09:15:00

You can test it using your favourite http tool (e.g. HTTPie or Postman).  
You must use *brandId*, *productId* and *applicationDate* as query parameters in order to find a product.

**NOTE**: applicationDate should be in "yyyy-MM-ddTHH:mm:ss" (see example above).

Response should be like this:

    {
        "productId": 35455,
        "brandId": 1,
        "priceList": 3,
        "startDate": "2020-06-15T00:00:00",
        "endDate": "2020-06-15T11:00:00",
        "price": 30.5,
        "currency": "EUR"
    }

Or, in case no product has been found a 404 (NOT FOUND) error response:

    {
        "timestamp": "2022-12-14T10:53:35.321+00:00",
        "status": 404,
        "error": "Not Found",
        "message": "Price not found for date 2022-06-15T09:15:00",
        "path": "/api/v1/productprices"
    }

Check below the technical assumptions for other possible errors.

## Database access

Database console has been enabled, and it is available in:

> http://localhost:8088/h2-console/

With the following connection data:

    url: jdbc:h2:mem:testdb
    username: sa
    password: test1
    driverClassName: org.h2.Driver

You can check, add or remove data in *PRICES* database using SQL statements.  
The database is initially filled with the examples in *PruebaTecnicaInditexPlataforma.txt*. You can add or remove
examples in *data.sql* file, with insert statements.

This behaviour can be change in *application.yaml* file under spring properties:

    h2:
    console:
    enabled: true
    path: /h2-console

## Technical assumptions and decisions

Since this is a poc, primary focus has been to create a working code and quick as possible delivery, so there are a few
improvable aspects that have been willingly ignored.

#### Api format

The API could have been designed in a more restful way, such as:

> localhost:8088/api/v1/brands/1/products/35455/prices?applicationDate=2020-06-15T09:15:00

But it is known almost nothing about brands or products as resources, and a full rest api was out of the table in terms
of time to build it.  
So instead of mocking or simulate a restful api a simple endpoint was exposed.

Security and validation are also ignored in order to focus on behaviour.  
However, there is a block code with a TODO to implement validation (such as existence and format of the parameters) in a
near future.

#### Database assumptions

Even if we are implementing an in-memory h2 database for simplicity, the design has been done as if it is an outside
service which we can not alter or decide much about accessibility.  
Therefore, the database is exactly the same as provided as example in *PruebaTecnicaInditexPlataforma.txt*, and the only
interface we can access is a request to find product prices by product ids (considering so far productId and brandId).

Other options could have been chosen, like the possibility of filtering directly in the database by matching dates; it
may be implemented as example in future tasks.

#### Feature considerations

Since the database is considered to expose only the option to retrieve data by product and brand ids, all logic
regarding filtering and choosing the correct date range has been done in the domain layer.  
The database is considered to be consistent and coherent, and validations such as having two price possibilities with
same priority for a given date have been deliberately ignored.

Since there is no functional information of what to do in case of having two equally valid options, consistency in the
result can not be assured, although no exception will be thrown.

It is meant to propose an option to ensure the consistency of the database assuming we can control the creation of new
price ranges in a future task.

#### Test

TDD has been used as much as possible to implement the task, specially in the domain layer.  
Although tests could be more thorough, unit test are present for important classes (mapper and such are ignored).

There is also an integration test for the full feature, where extra cases are added.

#### Architecture and packaging

Solid, clean code, and clean architecture principles have been considered while producing the code for this POC.

High level by-feature package has been used, but with a low level port & adapters approach, as in a hexagonal
architecture:

- A domain layer containing:
    - **logic** package with business logic,
    - **model** package with value objects used
    - **ports** package with definitions of inbound (contract for calling the domain business, such as http endpoints)
      and outbound (contract for low level implementation details, such as databases) ports.

- An adapter layer containing:
    - **web** package with controller implementation and its resources
    - **data** package with specific database implementation and its entities

