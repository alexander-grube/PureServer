package org.example.controllers;

import com.alibaba.fastjson2.JSON;
import com.zaxxer.hikari.HikariDataSource;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import org.example.models.Person;
import org.jboss.logging.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class PersonController {

    private static final Logger LOGGER = Logger.getLogger(PersonController.class);

    public static void getPersonLimit(HttpServerExchange exchange, HikariDataSource dataSource) {
        String limit = exchange.getQueryParameters().get("limit").getFirst();
        var persons = new ArrayList<Person>();
        try (Connection connection = dataSource.getConnection()) {
            var startTimer = System.currentTimeMillis();
            var statement = connection.createStatement();
            var resultSet = statement.executeQuery("SELECT * FROM person ORDER BY id ASC LIMIT " + limit);
            var endTimer = System.currentTimeMillis();
            System.out.println("Time taken to execute query: " + (endTimer - startTimer) + "ms");
            startTimer = System.currentTimeMillis();
            // map the result set to a list of Person objects
            while (resultSet.next()) {
                var person = new Person(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("job"),
                        resultSet.getBoolean("is_adult"),
                        resultSet.getShort("favorite_number")
                );
                persons.add(person);
            }
            endTimer = System.currentTimeMillis();
            System.out.println("Time taken to map result set to list of Person objects: " + (endTimer - startTimer) + "ms");
        } catch (SQLException e) {
            LOGGER.error("Error connecting to database", e);
        }
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
        exchange.getResponseSender().send(JSON.toJSONString(persons));
    }
}
