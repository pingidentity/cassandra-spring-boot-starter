This is an example of spring-boot 2.1 REST API leveraging cassandra auto configuration.
It implements simple CRUD service backed with Cassandra DB.

To run it locally:
1. Clone folder
2. Make sure you running cassandra db on localhost
3. Import schema from [src/main/cql/01-schema.cql](src/main/cql/01-schema.cql)
4. mvn clean spring-boot:run

Feed some data:
`http post http://localhost:8080/videos < video.json`

where video.json is something like:
```JSON
{
	"title": "Molly's Game",
	"description": "The true story of Molly Bloom, an Olympic-class skier who ran the world's most exclusive high-stakes poker game and became an FBI target.",
	"date": "2017/01/05",
	"year": 2017	
}
```

Query data back:
`http http://localhost:8080/videos?start=2017\&end=2020`

Observe service health information:
`http http://localhost:8080/actuator/health`