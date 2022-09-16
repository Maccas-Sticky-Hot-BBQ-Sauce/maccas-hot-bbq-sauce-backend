package com.translink.api.staticdata;

import com.translink.api.config.format.model.SpecializedTime;
import com.translink.api.repository.BulkBatchProcessor;
import com.translink.api.repository.model.Route;
import com.translink.api.repository.model.Stop;
import com.translink.api.repository.model.StopTime;
import com.translink.api.repository.model.Trip;
import com.translink.api.repository.model.embed.*;
import com.translink.api.repository.model.embed.Calendar;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class StaticDataInitializer {
    public static final String COMPLETED_READING_COUNT = "Completed reading {}, count = {}";

    @Value(value = "${refresh-data}")
    private boolean refreshData;

    @Value(value = "${spring.data.mongodb.host}")
    private String mongoHost;

    @Value(value = "classpath:static_gtfs/agency.csv")
    private Resource agencyData;

    @Value(value = "classpath:static_gtfs/calendar.csv")
    private Resource calendarData;

    @Value(value = "classpath:static_gtfs/calendar_dates.csv")
    private Resource datesData;

    @Value(value = "classpath:static_gtfs/feed_info.csv")
    private Resource feedData;

    @Value(value = "classpath:static_gtfs/routes.csv")
    private Resource routeData;

    @Value(value = "classpath:static_gtfs/shapes.csv")
    private Resource shapeData;

    @Value(value = "classpath:static_gtfs/stop_times.csv")
    private Resource stopTimeData;

    @Value(value = "classpath:static_gtfs/stops.csv")
    private Resource stopData;

    @Value(value = "classpath:static_gtfs/trips.csv")
    private Resource tripData;

    private BulkBatchProcessor batchProcessor;
    private MongoTemplate mongoTemplate;

    @Autowired
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Autowired
    public void setBatchProcessor(BulkBatchProcessor batchProcessor) {
        this.batchProcessor = batchProcessor;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void populateDatabaseOnStartup() throws IOException {
        if(!refreshData) {
            log.info("Skip refreshing database {}", mongoHost);

            return;
        }

        long timelapse = Instant.now().toEpochMilli();
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .build();

        cleanUpDatabase();
        try {
            log.info("Populating {} with Static Data", mongoHost);

            Map<String, Calendar> calendarMap = new HashMap<>();
            Map<String, List<CalendarException>> exceptionsMap = new HashMap<>();
            Map<String, List<Shape>> shapeMap = new HashMap<>();

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            try(Reader reader = new FileReader(calendarData.getFile())) {
                for(CSVRecord csvRecord : csvFormat.parse(reader)) {
                    Calendar calendar = Calendar.builder()
                            .days(new HashSet<>())
                            .startDate(LocalDate.parse(csvRecord.get(8), dateFormatter))
                            .endDate(LocalDate.parse(csvRecord.get(9), dateFormatter))
                            .build();

                    for(int i = 1; i <= 7; i++) {
                        if("1".equals(csvRecord.get(i))) {
                            calendar.getDays().add(Days.values()[i-1]);
                        }
                    }

                    calendarMap.put(csvRecord.get(0), calendar);
                }
            }
            log.info(COMPLETED_READING_COUNT, calendarData.getFilename(), calendarMap.keySet().size());

            try(Reader reader = new FileReader(datesData.getFile())) {
                for(CSVRecord csvRecord : csvFormat.parse(reader)) {
                    CalendarException exceptions = CalendarException.builder()
                            .date(LocalDate.parse(csvRecord.get(1), dateFormatter))
                            .exceptionType(
                                    ExceptionType.values()[Integer.parseInt(csvRecord.get(2))]
                            )
                            .build();

                    if(!exceptionsMap.containsKey(csvRecord.get(0))) {
                        exceptionsMap.put(csvRecord.get(0), new ArrayList<>());
                    }

                    exceptionsMap.get(csvRecord.get(0)).add(exceptions);
                }
            }
            log.info(COMPLETED_READING_COUNT, datesData.getFilename(), exceptionsMap.keySet().size());

            try(Reader reader = new FileReader(shapeData.getFile())) {
                for(CSVRecord csvRecord : csvFormat.parse(reader)) {
                    Shape shape = Shape.builder()
                            .sequence(Integer.parseInt(csvRecord.get(3)))
                            .latitude(Double.parseDouble(csvRecord.get(1)))
                            .longitude(Double.parseDouble(csvRecord.get(2)))
                            .build();

                    if(!shapeMap.containsKey(csvRecord.get(0))) {
                        shapeMap.put(csvRecord.get(0), new ArrayList<>());
                    }

                    shapeMap.get(csvRecord.get(0)).add(shape);
                }
            }

            Map<String, Route> routeMap = new HashMap<>();
            try(Reader reader = new FileReader(routeData.getFile())) {
                for(CSVRecord csvRecord : csvFormat.parse(reader)) {
                    Route route = Route.builder()
                            .id(new ObjectId().toString())
                            .routeId(csvRecord.get(0))
                            .shortName(csvRecord.get(1))
                            .longName(csvRecord.get(2))
                            .description(csvRecord.get(3))
                            .routeType(
                                    RouteType.values()[Integer.parseInt(
                                            Optional.ofNullable(csvRecord.get(4)).orElse("5")
                                    )]
                            )
                            .routeUrl(csvRecord.get(5))
                            .routeColor(csvRecord.get(6))
                            .textColor(csvRecord.get(7))
                            .trips(new ArrayList<>())
                            .build();

                    routeMap.put(route.getRouteId(), route);
                }
            }
            log.info(COMPLETED_READING_COUNT, routeData.getFilename(), routeMap.keySet().size());

            Map<String, Trip> tripMap = new HashMap<>();
            try(Reader reader = new FileReader(tripData.getFile())) {
                for(CSVRecord csvRecord : csvFormat.parse(reader)) {
                    Trip trip = Trip.builder()
                            .id(new ObjectId().toString())
                            .route(routeMap.get(csvRecord.get(0)))
                            .serviceId(csvRecord.get(1))
                            .calendar(calendarMap.get(csvRecord.get(1)))
                            .exceptions(exceptionsMap.get(csvRecord.get(1)))
                            .tripId(csvRecord.get(2))
                            .headsign(csvRecord.get(3))
                            .direction(Direction.values()[Integer.parseInt(csvRecord.get(4))])
                            .blockId(csvRecord.get(5))
                            .shapeId(csvRecord.get(6))
                            .shapes(shapeMap.get(csvRecord.get(6)))
                            .stopTimes(new ArrayList<>())
                            .build();

                    routeMap.get(csvRecord.get(0)).getTrips().add(trip);

                    tripMap.put(trip.getTripId(), trip);
                }
            }
            log.info(COMPLETED_READING_COUNT, tripData.getFilename(), tripMap.keySet().size());

            Map<String, Stop> stopMap = new HashMap<>();
            try(Reader reader = new FileReader(stopData.getFile())) {
                for(CSVRecord csvRecord : csvFormat.parse(reader)) {
                    Stop stop = Stop.builder()
                            .id(new ObjectId().toString())
                            .stopId(csvRecord.get(0))
                            .stopCode(csvRecord.get(1))
                            .name(csvRecord.get(2))
                            .description(csvRecord.get(3))
                            .latitude(Double.parseDouble(csvRecord.get(4)))
                            .longitude(Double.parseDouble(csvRecord.get(5)))
                            .zoneId(csvRecord.get(6))
                            .stopUrl(csvRecord.get(7))
                            .locationType(Integer.parseInt(csvRecord.get(8)))
                            .stopTimes(new ArrayList<>())
                            .childStops(new ArrayList<>())
                            .build();

                    String parentStation = csvRecord.get(9);
                    if(parentStation != null && !parentStation.isEmpty()) {
                        stop.setParentStop(
                                Stop.builder()
                                        .stopId(parentStation)
                                        .build()
                        );
                        stop.setPlatformCode(csvRecord.get(10));
                    }

                    stopMap.put(stop.getStopId(), stop);
                }

                stopMap.values().stream()
                        .filter(stop -> stop.getParentStop() != null)
                        .forEach(childStop -> {
                            Stop parentStop = stopMap.get(childStop.getParentStop().getStopId());

                            parentStop.getChildStops().add(childStop);
                            childStop.setParentStop(parentStop);
                        });
            }
            log.info(COMPLETED_READING_COUNT, stopData.getFilename(), stopMap.keySet().size());

            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            List<StopTime> stopTimes = new ArrayList<>();
            try(Reader reader = new FileReader(stopTimeData.getFile())) {
                for(CSVRecord csvRecord : csvFormat.parse(reader)) {
                    StopTime stopTime = StopTime.builder()
                            .id(new ObjectId().toString())
                            .arrival(SpecializedTime.parse(csvRecord.get(1), timeFormatter))
                            .departure(SpecializedTime.parse(csvRecord.get(2), timeFormatter))
                            .sequence(Integer.parseInt(csvRecord.get(4)))
                            .pickupType(StopPickupType.values()[Integer.parseInt(csvRecord.get(5))])
                            .dropOffType(StopDropOffType.values()[Integer.parseInt(csvRecord.get(6))])
                            .build();

                    Trip trip = tripMap.get(csvRecord.get(0));
                    stopTime.setTrip(trip);
                    trip.getStopTimes().add(stopTime);

                    Stop stop = stopMap.get(csvRecord.get(3));
                    stopTime.setStop(stop);
                    stop.getStopTimes().add(stopTime);

                    stopTimes.add(stopTime);
                }
            }
            log.info(COMPLETED_READING_COUNT, stopTimeData.getFilename(), stopTimes.size());

            batchProcessor.bulkInsert(Route.class, new ArrayList<>(routeMap.values()));
            batchProcessor.bulkInsert(Trip.class, new ArrayList<>(tripMap.values()));
            batchProcessor.bulkInsert(Stop.class, new ArrayList<>(stopMap.values()));
            batchProcessor.bulkInsert(StopTime.class, stopTimes);
        } catch (Throwable e) {
            cleanUpDatabase();
            log.error("Error on populating database, database is rolled back");

            throw e;
        } finally {
            log.info("Refresh database timelapse: {}ms", Instant.now().toEpochMilli() - timelapse);
        }
    }

    private void cleanUpDatabase() {
        mongoTemplate.dropCollection(Route.class);
        mongoTemplate.dropCollection(Trip.class);
        mongoTemplate.dropCollection(Stop.class);
        mongoTemplate.dropCollection(StopTime.class);

        log.info("Cleaned up {}", mongoHost);
    }
}
