package org.example;

import com.alibaba.fastjson2.JSON;
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

    private static final Dotenv dotenv = Dotenv.load();
    public static void main(final String[] args) {
        String dbUrl = dotenv.get("DB_URL");
        System.out.println(dbUrl);
        Undertow server = Undertow.builder()
                .addHttpListener(8082, "localhost", ROUTES)
                .build();
        server.start();
    }

    private static final HttpHandler ROUTES = new RoutingHandler()
            .get("/java-benchmark", Main::helloWorld)
            .get("/java-benchmark/hello/{name}", Main::helloName)
            .get("/java-benchmark/person/limit/{limit}", PersonController::getPersonLimit)
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