package org.example.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BasicConnectionPool implements ConnectionPool {

    private final String url;
    private final List<Connection> connectionPool;
    private final List<Connection> usedConnections = new ArrayList<>();
    private final static int INITIAL_POOL_SIZE = 50;

    private final static int MAX_POOL_SIZE = 100;

    private final static int MAX_TIMEOUT = 5;

    public static BasicConnectionPool create(String url) throws SQLException {

        List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);
        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            pool.add(createConnection(url));
        }
        return new BasicConnectionPool(url, pool);
    }

    public BasicConnectionPool(String url, List<Connection> connectionPool) {
        this.url = url;
        this.connectionPool = connectionPool;
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (connectionPool.isEmpty()) {
            if (usedConnections.size() < MAX_POOL_SIZE) {
                connectionPool.add(createConnection(url));
            } else {
                throw new RuntimeException("Maximum pool size reached, no available connections!");
            }
        }

        Connection connection = connectionPool.remove(connectionPool.size() - 1);

        if (!connection.isValid(MAX_TIMEOUT)) {
            connection = createConnection(url);
        }

        usedConnections.add(connection);
        return connection;
    }

    @Override
    public boolean releaseConnection(Connection connection) {
        connectionPool.add(connection);
        return usedConnections.remove(connection);
    }

    @Override
    public String getUrl() {
        return url;
    }

    private static Connection createConnection(String url) throws SQLException {
        return DriverManager.getConnection(url);
    }

    public void shutdown() throws SQLException {
        usedConnections.forEach(this::releaseConnection);
        for (Connection c : connectionPool) {
            c.close();
        }
        connectionPool.clear();
    }

    public int getSize() {
        return connectionPool.size() + usedConnections.size();
    }

    public List<Connection> getConnectionPool() {
        return connectionPool;
    }

    public List<Connection> getUsedConnections() {
        return usedConnections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicConnectionPool that = (BasicConnectionPool) o;
        return Objects.equals(url, that.url) && Objects.equals(connectionPool, that.connectionPool) && Objects.equals(usedConnections, that.usedConnections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, connectionPool, usedConnections);
    }

    @Override
    public String toString() {
        return "BasicConnectionPool{" + "url='" + url + '\'' + ", connectionPool=" + connectionPool + ", usedConnections=" + usedConnections + '}';
    }
}
