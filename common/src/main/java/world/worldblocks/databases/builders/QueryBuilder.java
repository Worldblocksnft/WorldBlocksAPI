package world.worldblocks.databases.builders;

import world.worldblocks.databases.PostgresRequirement;
import world.worldblocks.databases.SQLClient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QueryBuilder {

    private StringBuilder update;
    private String table;
    private List<String> columns = new ArrayList<>();
    private List<Object> requirements = new ArrayList<>();
    public QueryBuilder(String table) {
        this.update = new StringBuilder();
        this.update.append("SELECT ");
        this.table = table;
    }

    public QueryBuilder addColumn(String column) {
        this.columns.add(column);
        return this;
    }

    public QueryBuilder addRequirement(PostgresRequirement requirement) {
        this.requirements.add(requirement);
        return this;
    }

    public QueryBuilder addRequirement(String column, Object value) {
        this.requirements.add(new PostgresRequirement(column, value));
        return this;
    }

    public PreparedStatement build(Connection connection) {
        try {
            this.update.append(String.join(", ", this.columns));
            this.update.append(" FROM ");
            this.update.append(this.table);
            this.update.append(" WHERE (");
            for (int i = 0; i < this.requirements.size(); i++) {
                PostgresRequirement requirement = (PostgresRequirement) this.requirements.get(i);
                this.update.append(requirement.getKey());
                this.update.append(requirement.getComparator());
                this.update.append("?");
                if (i < this.requirements.size() - 1) {
                    this.update.append(" AND ");
                }
            }
            this.update.append(")");

            PreparedStatement statement = connection.prepareStatement(this.update.toString());
            for (int i = 0; i < this.requirements.size(); i++) {
                PostgresRequirement requirement = (PostgresRequirement) this.requirements.get(i);
                statement.setObject(i + 1, requirement.getRequirement());
            }

            return statement;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PreparedStatement build(SQLClient client) {
        return this.build(client.getConnection());
    }

}
