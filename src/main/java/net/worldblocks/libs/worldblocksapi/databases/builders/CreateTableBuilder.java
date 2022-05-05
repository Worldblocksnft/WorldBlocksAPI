package net.worldblocks.libs.worldblocksapi.databases.builders;

import net.worldblocks.libs.worldblocksapi.databases.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CreateTableBuilder {

    private StringBuilder update;
    private List<PostgresColumn> columns = new ArrayList<>();
    public CreateTableBuilder(String table) {
        this.update = new StringBuilder();
        this.update.append("CREATE TABLE IF NOT EXISTS ").append(table).append(" ( ");
    }

    public CreateTableBuilder addColumn(PostgresColumn column) {
        this.columns.add(column);
        return this;
    }

    public CreateTableBuilder addColumn(String name, PostgresType type) {
        return addColumn(new PostgresColumn(name, type));
    }

    public PreparedStatement build(Connection connection) {
        //Make first key primary key
        int i = 0;
        for (PostgresColumn column : columns) {
            if (i == 0) {
                if (columns.size() == 1) {
                    update.append(column.getColumnID()).append(" ").append(column.getColumnType().getType()).append(" PRIMARY KEY");
                } else {
                    update.append(column.getColumnID()).append(" ").append(column.getColumnType().getType()).append(" PRIMARY KEY, ");
                }
            } else {
                update.append(column.getColumnID()).append(" ").append(column.getColumnType().getType()).append(", ");
            }
            i++;
        }
        update.delete(update.length() - 2, update.length());
        update.append(" ) ");
        try {
            return connection.prepareStatement(update.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PreparedStatement build(SQLClient client) {
        return build(client.getConnection());
    }

}
