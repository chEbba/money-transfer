# money-transfer
Simple example of Scala power.

## How to run
From source:
```
sbt run
```
Or just download the current release from release pages and run:
```
java -jar money-transfer-assembly-{VERSION}.jar
```

Go to [http://localhost:9000](http://localhost:9000)

Properties:

| Property              | Description   | Default   |
| ---                   | ---           | ---       |
| -Dorg.chebba.mt.port  | Http port     | 9000      |

## Internals
The main goal to show different aspects of modern scala development:

* Scala simplicity in business logic parts
* Scala power in DSL
* Usage of Java and Scala modern libraries 

So, what is inside?

### Business Logic
Simple application for money transfers between accounts. 
It contains few functions to manage accounts and work with account balances.

### REST
Application provides REST-like API described with [OpenAPI](https://openapis.org) aka [Swagger](http://swagger.io)
Swagger implementation is done by my old DSL lib. 
It's not so cool as can be (have an idea to reimplement it with macros and combinators for model description), 
but anyway is much better than Swagger annotations.

### HTTP
Http server is a simplified version of an implementation used in my previous projects. 
It's based on [Netty](http://netty.io) and works pretty well for fast simple use-cases.

### Tests
Simple functional testing is done with [ScalaTest](http://www.scalatest.org) and [dispatch](http://dispatch.databinder.net)
  
## What is missed?
* Better error handling and data validation
* Better test coverage
* Shorter test syntax for JSON assertions
