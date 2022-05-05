package net.worldblocks.libs.worldblocksapi.databases;

import net.worldblocks.libs.worldblocksapi.databases.builders.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLClient {
    private String host;
    private String port;
    private String database;
    private String username;
    private String password;
    private String URI;
    private Connection connection;

    public SQLClient(String host, int port, String database, String username, String password) {
        this.host = host;
        this.port = String.valueOf(port);
        this.database = database;
        this.username = username;
        this.password = password;

        //Generate a Postgres JDBC URI from the provided information, ensure the URI has SSLMode set to preferred.
        this.URI = "jdbc:postgresql://" + this.host + ":" + this.port + "/" + this.database + "?sslmode=prefer";

        //Instantiate a connection and optionally use the authorization details provided
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(URI, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTable(String table, PostgresColumn... columns) {
        CreateTableBuilder createTableBuilder = new CreateTableBuilder(table);
        for (int i = 0; i < columns.length; i++) {
            createTableBuilder.addColumn(columns[i]);
        }
        PreparedStatement preparedStatement = createTableBuilder.build(connection);
        try {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void executeInsert(String table, List<PostgresData> postgresData) {
        boolean anyKeyExists = false;
        if (keyExists(table, postgresData.get(0).getKey(), postgresData.get(0).getValue())) {
            anyKeyExists = true;
        }

        if (!anyKeyExists) {
            InsertBuilder insertBuilder = new InsertBuilder(table);
            for (int i = 0; i < postgresData.size(); i++) {
                insertBuilder.addData(postgresData.get(i));
            }
            PreparedStatement preparedStatement = insertBuilder.build(connection);
            try {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            executeUpdate(table, postgresData, new ArrayList<>());
        }
    }

    public void executeUpdate(String table, List<PostgresData> postgresData, List<PostgresRequirement> requirements) {
        UpdateBuilder updateBuilder = new UpdateBuilder(table);
        for (int i = 0; i < postgresData.size(); i++) {
            updateBuilder.addData(postgresData.get(i));
        }
        for (int i = 0; i < requirements.size(); i++) {
            updateBuilder.addRequirement(requirements.get(i));
        }
        PreparedStatement preparedStatement = updateBuilder.build(connection);
        try {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<PostgresData<?>> executeQuery(String table, List<String> columns, List<PostgresRequirement> requirements) {
        QueryBuilder queryBuilder = new QueryBuilder(table);
        for (int i = 0; i < columns.size(); i++) {
            queryBuilder.addColumn(columns.get(i));
        }
        for (int i = 0; i < requirements.size(); i++) {
            queryBuilder.addRequirement(requirements.get(i));
        }
        List<PostgresData<?>> results = new ArrayList<>();
        PreparedStatement preparedStatement = queryBuilder.build(connection);
        ResultSet resultSet = null;
        try {
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Object result = resultSet.getObject(1);
                String columnName = resultSet.getMetaData().getColumnName(1);
                PostgresData<?> postgresData = new PostgresData<>(columnName, result);
                results.add(postgresData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    public void insertColumn(String table, String columnName, PostgresType type) {
        InsertColumnBuilder insertColumnBuilder = new InsertColumnBuilder(table, columnName, type);
        PreparedStatement preparedStatement = insertColumnBuilder.build(connection);
        try {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRow(String table, PostgresData<?>... data) {
        DeleteBuilder deleteBuilder = new DeleteBuilder(table);
        for (int i = 0; i < data.length; i++) {
            deleteBuilder.addRequirement(data[i]);
        }
        PreparedStatement preparedStatement = deleteBuilder.build(connection);
        try {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean keyExists(String table, String key, Object value) {
        try {
            //Check if a key exists in a specific table postgresql.
            String query = "SELECT * FROM " + table + " WHERE " + key + " = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setObject(1, value);
            ResultSet rs = preparedStatement.executeQuery();

            return rs.next();
        } catch (SQLException e) {}
        return false;
    }

    public void startKeepAlive(Plugin plugin) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new SQLKeepAliveTask(this), 0L, 20L);
    }

    void keepAlive() {
        try {
            if (connection != null && connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:postgresql://"+host+":"+port+"/"+database+"?user="+username+"&password="+password);
            } else if (connection == null) {
                connection = DriverManager.getConnection("jdbc:postgresql://"+host+":"+port+"/"+database+"?user="+username+"&password="+password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getURI() {
        return URI;
    }

    public Connection getConnection() {
        return connection;
    }
}
