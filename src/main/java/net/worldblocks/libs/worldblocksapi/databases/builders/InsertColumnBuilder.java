package net.worldblocks.libs.worldblocksapi.databases.builders;

import net.worldblocks.libs.worldblocksapi.databases.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertColumnBuilder {

    private String table;
    private String columnName;
    private PostgresType type;
    public InsertColumnBuilder(String table, String columnName, PostgresType type) {
        this.columnName = columnName;
        this.table = table;
        this.type = type;
    }

    public PreparedStatement build(Connection connection) {
        //Write a statement to insert a column into a pre-existing table.
        String sql = "ALTER TABLE " + table + " ADD COLUMN " + columnName + " " + type.getType();
        try {
            return connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PreparedStatement build(SQLClient client) {
        return build(client.getConnection());
    }

}
