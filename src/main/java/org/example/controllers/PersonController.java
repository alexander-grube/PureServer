package org.example.controllers;

import com.zaxxer.hikari.HikariDataSource;
import io.fury.Fury;
import io.fury.config.Language;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import org.example.models.Person;
import org.example.utils.ResultSetUtil;
import org.jboss.logging.Logger;

import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class PersonController {

    private static final Logger LOGGER = Logger.getLogger(PersonController.class);

    public static void getPersonLimit(HttpServerExchange exchange, HikariDataSource dataSource) {
        long limit = Long.parseLong(exchange.getQueryParameters().get("limit").getFirst());
        var persons = new ArrayList<Person>();
        try (Connection connection = dataSource.getConnection()) {
            var startTimer = System.currentTimeMillis();
            var statement = connection.createStatement();
            var resultSet = statement.executeQuery("SELECT * FROM person ORDER BY id ASC LIMIT " + limit);
            var endTimer = System.currentTimeMillis();
            LOGGER.info("Time taken to execute query: " + (endTimer - startTimer) + "ms");
            startTimer = System.currentTimeMillis();
            // map the result set to a list of Person objects
            while (resultSet.next()) {
                var person = new Person(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("job"),
                        resultSet.getBoolean("is_adult"),
                        resultSet.getShort("favorite_number"),
                        (String[]) resultSet.getArray("hobbies").getArray(),
                        (Integer[]) resultSet.getArray("lucky_numbers").getArray(),
                        (Double[]) resultSet.getArray("favorite_numbers").getArray(),
                        ResultSetUtil.getArrayListFromResultSet(resultSet, "favorite_foods")
                );
                persons.add(person);
            }
            endTimer = System.currentTimeMillis();
            LOGGER.info("Time taken to map result set to list of Person objects: " + (endTimer - startTimer) + "ms");
        } catch (SQLException e) {
            LOGGER.error("Error connecting to database", e);
        }
        Fury fury = Fury.builder().withLanguage(Language.XLANG).requireClassRegistration(false).build();
        fury.register(Person.class);
        var startTimer = System.currentTimeMillis();
        byte[] bytes = fury.serialize(persons);
        var endTimer = System.currentTimeMillis();
        LOGGER.info("Time taken to serialize list of Person objects: " + (endTimer - startTimer) + "ms");
        var deserialized = (ArrayList<Person>) fury.deserialize(bytes);
        LOGGER.info("Deserialized: " + deserialized.size() + " persons");
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/octet-stream");
        exchange.getResponseSender().send(ByteBuffer.wrap(bytes));
    }

    public static void addPerson(HttpServerExchange exchange, HikariDataSource dataSource) {
        exchange.getRequestReceiver().receiveFullBytes((exchange1, message) -> {
            var fury = Fury.builder().withLanguage(Language.XLANG).requireClassRegistration(false).build();
            fury.register(Person.class);
            Person person = (Person) fury.deserialize(message);
            try (Connection connection = dataSource.getConnection()) {
                var statement = connection.createStatement();
                var resultSet = statement.executeQuery("INSERT INTO person (name, job, is_adult, favorite_number, hobbies, lucky_numbers, favorite_numbers, favorite_foods) VALUES ('" + person.getName() + "', '" + person.getJob() + "', " + person.isAdult() + ", " + person.getFavoriteNumber() + ", '{" + String.join(",", person.getHobbies()) + "}', '{" + Arrays.toString(person.getLuckyNumbers()).replace("[", "").replace("]", "") + "}', '{" + Arrays.toString(person.getFavoriteNumbers()).replace("[", "").replace("]", "") + "}', '{" + String.join(",", person.getFavoriteFoods()) + "}') RETURNING id");
                if (resultSet.next()) {
                    person.setId(resultSet.getInt("id"));
                }
            } catch (SQLException e) {
                LOGGER.error("Error connecting to database", e);
            }
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/octet-stream");
            exchange.getResponseSender().send(ByteBuffer.wrap(fury.serialize(person)));
        });

    }

    public static void getPerson(HttpServerExchange exchange, HikariDataSource dataSource) {
        int id = Integer.parseInt(exchange.getQueryParameters().get("id").getFirst());
        Person person = null;
        try (Connection connection = dataSource.getConnection()) {
            var statement = connection.createStatement();
            var resultSet = statement.executeQuery("SELECT * FROM person WHERE id = " + id);
            if (resultSet.next()) {
                person = new Person(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("job"),
                        resultSet.getBoolean("is_adult"),
                        resultSet.getShort("favorite_number"),
                        (String[]) resultSet.getArray("hobbies").getArray(),
                        (Integer[]) resultSet.getArray("lucky_numbers").getArray(),
                        (Double[]) resultSet.getArray("favorite_numbers").getArray(),
                        ResultSetUtil.getArrayListFromResultSet(resultSet, "favorite_foods")
                );
            }
        } catch (SQLException e) {
            LOGGER.error("Error connecting to database", e);
        }
        Fury fury = Fury.builder().withLanguage(Language.XLANG).build();
        fury.register(Person.class);
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/octet-stream");
        exchange.getResponseSender().send(ByteBuffer.wrap(fury.serialize(person)));
    }

    public static void generatePerson(HttpServerExchange exchange) {
        Person person = new Person(
                "John Doe",
                "Software Engineer",
                true,
                (short) 42,
                new String[]{"Reading", "Coding"},
                new Integer[]{7, 13, 42},
                new Double[]{3.14, 2.718},
                new ArrayList<>() {{
                    add("Pizza");
                    add("Pasta");
                }}
        );
        Fury fury = Fury.builder().withLanguage(Language.XLANG).requireClassRegistration(false).build();
        fury.register(Person.class);
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/octet-stream");
        exchange.getResponseSender().send(ByteBuffer.wrap(fury.serialize(person)));
    }
}
