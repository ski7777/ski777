package com.autumncode.ski777;

import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.*;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

public class TestDatabase {
    @Test
    public void testDatabaseCreation() {
        Database database = new Database();
        // did we create the database properly?

        try (Connection connection = DriverManager.getConnection("jdbc:h2:./music")) {
            try (PreparedStatement ps = connection.prepareStatement("select count(*) from instruments")) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int results = rs.getInt(1);
                        // we should be okay now, we have a record count, doesn't matter what it is
                    }
                }
            }
        } catch (SQLException e) {
            fail(e.getMessage(), e);
        }
        database.clear();
    }

    @Test
    public void testImport() throws IOException, SQLException {
        Database database = new Database();
        database.importCSV("/assets/instruments.csv");
        try (Connection connection = DriverManager.getConnection("jdbc:h2:./music")) {
            try (PreparedStatement ps = connection.prepareStatement("select count(*) from instruments")) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int results = rs.getInt(1);
                        assertEquals(results,58);
                    }
                }
            }
        } catch (SQLException e) {
            fail(e.getMessage(), e);
        }
    }
}
