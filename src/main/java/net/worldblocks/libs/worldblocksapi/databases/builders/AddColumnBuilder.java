package net.worldblocks.libs.worldblocksapi.databases.builders;

import net.worldblocks.libs.worldblocksapi.databases.PostgresType;

public class AddColumnBuilder {

    private String table;
    private String update = "ALTER TABLE ";

    public AddColumnBuilder(String table) {
        this.table = table;
        update = update + table;
    }

    public AddColumnBuilder addColumn(String column, PostgresType type) {
        update = update + " ADD COLUMN IF NOT EXISTS " + column + " " + type.getType();

        return this;
    }

    public String build() {
        return update + ";";
    }

}
