package org.example;

import com.alibaba.fastjson2.JSON;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import io.undertow.util.Headers;
import org.example.models.Person;
import org.jboss.logging.Logger;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class);
    public static void main(final String[] args) {
        Undertow server = Undertow.builder()
                .addHttpListener(8080, "localhost", ROUTES)
                .build();
        server.start();
    }

    private static final HttpHandler ROUTES = new RoutingHandler()
            .get("/", Main::helloWorld)
            .get("/hello/{name}", Main::helloName)
            .post("/hello", Main::helloPost);

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