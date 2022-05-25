package world.worldblocks.databases.builders;

import world.worldblocks.databases.PostgresData;
import world.worldblocks.databases.SQLClient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InsertBuilder {

    private StringBuilder update;
    private List<Object> values = new ArrayList<>();
    private List<Object> requirements = new ArrayList<>();
    public InsertBuilder(String table) {
        this.update = new StringBuilder();
        this.update.append("INSERT INTO ").append(table).append(" VALUES (");
    }

    public InsertBuilder addData(PostgresData<?> data) {
        if (this.values.size() > 0) {
            this.update.append(",");
        }
        this.update.append("?");
        this.values.add(data.getValue());
        return this;
    }

    public PreparedStatement build(Connection connection) {
        try {
            //Append a postgres on conflict clause to the update StringBuilder
            this.update = this.update.append(")");
            this.update = this.update.append(";");
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
            if (this.requirements.size() == 0) {
                this.update.append(")");
            }
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
        for (int i = 0; i < this.values.size(); i++) {
            statement.setObject(index, this.values.get(i));
            index++;
        }

        for (int i = 0; i < this.requirements.size(); i++) {
            statement.setObject(index, this.requirements.get(i));
            index++;
        }

        return statement;
    }

}
