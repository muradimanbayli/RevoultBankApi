# Revolut Task

## Tech stack

- Java 11
- REST - Spark Framework
- Testing - JUnit & Mockito

## How To run tests
`mvn test`

## How to Build
`mvn package` 

Then Fat Jar will be created under target folder.

## How to run Fat Jar 

`java -jar RevolutApi-1.0-SNAPSHOT-jar-with-dependencies.jar`

the app starts on port 8080 by default

## API
**GET** /v1/account/:id - get account information by id

Example Request
```
localhost:8080/v1/account/acc0002
```


Example Response

````
{
	"accountId":"acc0002",
	"balance":"1000"
}
````

**POST** /v1/account - create new account

````
{
	"accountId":"acc0001",
	"balance":"1000"
}
````
Example Response

````
{
	"accountId":"acc0001",
	"balance":"1000"
}
````


**POST** /v1/transfer - transfer money from one acc to another

Example Request

````
{ 
   "sourceAccountId":"test",
   "targetAccountId":"qozqzo",
   "amount":"1000"
}
````

Example response
````
{
    "transactionId": "1ba43d1d-b0f6-45dc-913d-813cf7add38f",
    "transferDate": "2020-01-29T01:33:40.567260",
    "message": "Your transfer completed successfully"
}
````

Error Response

````
{
    "code": "E0000000",
    "message": "Source account not found"
}
````