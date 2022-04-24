package net.worldblocks.libs.worldblocksapi.databases;

public class PostgresRequirement {

    private String id;
    private Object data;

    public PostgresRequirement(String id, Object data) {
        this.id = id;
        this.data = data;
    }

    public String toWhere() {
        return id + "=" + "'" + data + "'";
    }

}
