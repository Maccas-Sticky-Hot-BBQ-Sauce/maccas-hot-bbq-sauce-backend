package com.translink.api.repository;

import com.google.gson.Gson;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

@Service
public class GtfsStaticReader {
    private Gson gson;

    @Autowired
    public void setGson(Gson gson) {
        this.gson = gson;
    }

    public void populateDatabase(List<String> filenames) {

    }

    private Iterable<CSVRecord> readFile(String filename) {
        try (Reader reader = new FileReader(filename)) {
            return CSVFormat.DEFAULT.builder()
                    .setSkipHeaderRecord(true)
                    .build().parse(reader);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
