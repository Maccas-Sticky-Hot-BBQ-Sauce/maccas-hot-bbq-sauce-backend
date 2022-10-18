### Backend

This project uses:
- `gtfs-realtime.proto` from [Google GTFS Specification](https://developers.google.com/transit/gtfs-realtime/) which is compiled into `GtfsRealtime` class.
- Static SEQ GTFS dataset on `./src/resources/static_gtfs/` from [Translink Open API](https://www.data.qld.gov.au/dataset/general-transit-feed-specification-gtfs-seq).

The live implementation can be seen via [Postman](https://www.postman.com/maccas-bbq-sauce/workspace/deco3801-maccas-sticky-hot-bbq-sauce).

For running the application you need to have this list of dependencies:
1. Java 1.8
2. Gradle 7.5
2. MongoDB 6.0.1
3. Google Maps Places API
4. Translink GTFS Realtime API

This application uses this list of environment variables to configure it's runtime:
- `MONGO_HOST` to specify MongoDB host.
- `MONGO_PORT` to specify MongoDB port.
- `MONGO_USER` to specify MongoDB user.
- `MONGO_PASSWORD` to specify MongoDB password.
- `MAPS_KEY` to specify Google Maps API key.

To run the application:
1. Build the fat jar.
```
./gradlew clean assemble
```

2. Run the jar.
```
java -Dlog4j2.contextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector -jar ./build/libs/api-0.0.1-SNAPSHOT.jar
```

You can specify this list of program arguments to modify the runtime:
- `--refresh-data.static=true` to rebuild the static database from scratch.
- `--refresh-data.places=true` to rebuild the landmark database from scratch.
- `--batch-size` to specify the batch size on rebuilding the database.