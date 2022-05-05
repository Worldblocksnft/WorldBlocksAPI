package net.worldblocks.libs.worldblocksapi.databases.builders;

import net.worldblocks.libs.worldblocksapi.databases.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeleteBuilder {

    private StringBuilder update;
    private List<PostgresData<?>> requirements = new ArrayList<>();
    public DeleteBuilder(String table) {
        this.update = new StringBuilder();
        this.update.append("DELETE FROM ").append(table).append(" WHERE ");
    }

    public DeleteBuilder addRequirement(PostgresData data) {
        requirements.add(data);
        this.update.append(data.getKey());
        this.update.append("=?");
        if (this.requirements.size() != 1) {
            this.update.append(" AND ");
        }
        return this;
    }

    public PreparedStatement build(Connection connection) {
        try {
            //Append a postgres on conflict clause to the update StringBuilder
            PreparedStatement statement = connection.prepareStatement(this.update.toString());
            int index = 1;
            return getPreparedStatement(statement, index);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PreparedStatement build(SQLClient sqlClient) {
        try {
            //Append a postgres on conflict clause to the update StringBuilder
            this.update = this.update.append(";");
            PreparedStatement statement = sqlClient.getConnection().prepareStatement(this.update.toString());
            int index = 1;
            return getPreparedStatement(statement, index);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private PreparedStatement getPreparedStatement(PreparedStatement statement, int index) throws SQLException {
        for (int i = 0; i < this.requirements.size(); i++) {
            PostgresData<?> postgresData = this.requirements.get(i);
            statement.setObject(index, postgresData.getValue());
            index++;
        }

        return statement;
    }

}
