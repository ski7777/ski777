package com.autumncode.ski777;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {
    String connectionUrl = "jdbc:h2:./music";

    public Database() {
        String[] tables = new String[]{
                "create table instruments (id int not null, lang varchar(2) not null, translation varchar(255) not null, primary key (id, lang))"
        };

        try (Connection connection = DriverManager.getConnection("jdbc:h2:./music")) {
            for (String statement : tables) {
                try (PreparedStatement ps = connection.prepareStatement(statement)) {
                    try {
                        ps.executeUpdate();
                    } catch (SQLException sqle) {
                        // 42101 is "table exists"
                        if (sqle.getErrorCode() != 42101) {
                            throw sqle;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void importCSV(String resourceName) throws IOException, SQLException {
        try (InputStream in = Database.class.getResourceAsStream(resourceName)) {
            try (Reader reader = new InputStreamReader(in)) {
                try (Connection connection = DriverManager.getConnection("jdbc:h2:./music")) {
                    try (PreparedStatement ps = connection.prepareStatement("insert into instruments values (?, ?, ?)")) {
                        CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);
                        for (CSVRecord record : parser.getRecords()) {
                            ps.setInt(1, Integer.parseInt(record.get(0)));
                            ps.setString(2, record.get(1));
                            ps.setString(3, record.get(2));
                            ps.executeUpdate();
                        }
                    }
                }
            }
        }
    }

    public void clear() {
        String[] tables = new String[]{
                "drop table if exists instruments"};

        try (Connection connection = DriverManager.getConnection("jdbc:h2:./music")) {
            for (String statement : tables) {
                try (PreparedStatement ps = connection.prepareStatement(statement)) {
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
