package com.abcanthur.website.codegenhack;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

public class CustomConnectionProvider implements ConnectionProvider {
	private final Connection connection;

    public CustomConnectionProvider() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:h2:mem:jooq-meta-extensions", "sa", "");
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean isUnwrappableAs(Class unwrapType) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        return null;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public void closeConnection(Connection conn) throws SQLException {
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return true;
    }
}
