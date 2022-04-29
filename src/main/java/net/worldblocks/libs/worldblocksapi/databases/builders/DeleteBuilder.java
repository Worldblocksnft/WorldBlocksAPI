package net.worldblocks.libs.worldblocksapi.databases.builders;

import net.worldblocks.libs.worldblocksapi.databases.PostgresRequirement;

public class DeleteBuilder {

    private String table;
    private String id;
    private String data;
    private PostgresRequirement[] requirements = null;

    public DeleteBuilder(String table, String id, Object data) {
        this.table = table;
        this.id = id;
        this.data = "'" + data + "'";
    }

    public DeleteBuilder(String table, PostgresRequirement... requirement) {
        this.table = table;
        this.requirements = requirement;
    }


    public String build() {
        if (requirements == null) {
            return "DELETE FROM " + table + " WHERE " + id + "=" + data;
        } else {
            String s = "DELETE FROM " + table + " WHERE ";
            for (PostgresRequirement postgresRequirement : requirements) {
                s = s + postgresRequirement.toWhere();
            }
            return s;
        }
    }

}
