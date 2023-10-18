package org.example;

import com.alibaba.fastjson2.JSON;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import io.undertow.util.Headers;
import org.example.controllers.PersonController;
import org.example.models.Person;
import org.jboss.logging.Logger;


public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class);

    private static final HikariDataSource dataSource;

    private static final Dotenv dotenv = Dotenv.load();

    private static final String dbUrl = dotenv.get("DB_URL");

    private static final String dbUser = dotenv.get("DB_USER");

    private static final String dbPassword = dotenv.get("DB_PASSWORD");

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dbUrl);
        config.setUsername(dbUser);
        config.setPassword(dbPassword);
        config.addDataSourceProperty("minimumIdle", "5");
        config.addDataSourceProperty("maximumPoolSize", "25");

        dataSource = new HikariDataSource(config);
    }

    public static void main(final String[] args) {
        Undertow server = Undertow.builder()
                .addHttpListener(8082, "localhost", ROUTES)
                .build();
        server.start();
    }

    private static final HttpHandler ROUTES = new RoutingHandler()
            .get("/java-benchmark", Main::helloWorld)
            .get("/java-benchmark/hello/{name}", Main::helloName)
            .get("/java-benchmark/person/limit/{limit}", exchange -> PersonController.getPersonLimit(exchange, dataSource))
            .post("/java-benchmark/hello", Main::helloPost);

    private static void helloWorld(HttpServerExchange exchange) {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
        exchange.getResponseSender().send("Hello World");
    }

    private static void helloName(HttpServerExchange exchange) {
        String name = exchange.getQueryParameters().get("name").getFirst();
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
        exchange.getResponseSender().send("Hello " + name);
    }

    private static void helloPost(HttpServerExchange exchange) {
        LOGGER.info("Received POST request");
        // block for the body
        exchange.getRequestReceiver().receiveFullBytes((exchange1, message) -> {
            // parse json body
            Person person = JSON.parseObject(message, Person.class);
            exchange1.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
            exchange1.getResponseSender().send("Hello " + person.getName());
        });
    }
}