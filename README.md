# spring-boot-books

This is a demo Spring Boot API project

## Quickstart

1. install Java jdk-24 (https://www.oracle.com/hk/java/technologies/downloads/#java24)

2. check Java version using `java --version` on terminal

3. cd to `/book` then run command 
```
mvnw spring-boot:run
```

4. default port is `8080`. From browser or using tools like Postman, check 
`localhost:8080/health` and you should see the `Healthy!` response

## APIs

### GET /Books

Gets a list of boks

#### Parameters
None
#### Responses
- 200
```
{
    id: number,
    title: string,
    author: string,
    published: boolean
}[]
```
### POST /Books
#### Parameters
application/json.
```
{
    id: number,
    title: string,
    author: string,
    published: boolean
}
```
#### Responses
Submit a book
- 200. The created book
```
{
    id: number,
    title: string,
    author: string,
    published: boolean
}
```
- 400. When the book input is invalid.
```
{
    "timestamp": string,
    "status": 400,
    "error": "BAD_REQUEST",
    "message": "Constraint Violation",
    "details": string,
    "path": string
}
```
- 415. When request body is not type is not json.
```
{
    "timestamp": timestamp,
    "status": 415,
    "error": "Unsupported Media Type",
    "message": string,
    "path": string
}
```
### DELETE /Book/{id}

Delete a book. 

#### Parameters
- id. Id of a book.
#### Responses
- 200. Book deleted. Note that if the id does not exist 200 will still be returned.

## Dev Notes

- The DB is set to sqlite. The file is in `/data/test.db`.

- On testing mode, DB is set to h2 in-memory database.

## Screenshot