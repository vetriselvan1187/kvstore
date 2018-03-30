# kvstore

In Memory Key Value Store:
        key value pairs are stored in memory. there are 2 process runs as separate server. Both of them are configured to replicate the data to each other. Sping boot is used to implement KVStore. Since it s for demonstration purpose only same can be replaced with any other java libraries.

	Dependencies
		1. Spring Boot
		2. Junit
		3. Maven

	Usage:
	    mvn clean package -DskipTests

	    Terminal 1:
	         java -jar target/kv-store-0.1.0.jar --spring.application.json='{"server.port":"4455", "kvstore.replication.host":"http://localhost:4466"}'

	    Terminal 2:
	         java -jar target/kv-store-0.1.0.jar --spring.application.json='{"server.port":"4466", "kvstore.replication.host":"http://localhost:4455"}'

 
            Terminal 3:
	        curl -H "Content-Type: application/json" http://localhost:4455/set/key -d '{"name":"name"}'

		curl -H "Content-Type: application/json" http://localhost:4466//get/key



