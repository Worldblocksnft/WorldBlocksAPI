package net.worldblocks.libs.worldblocksapi.databases.builders;

public class DeleteBuilder {

    private String table;
    private String id;
    private String data;

    public DeleteBuilder(String table, String id, Object data) {
        this.table = table;
        this.id = id;
        this.data = "'" + data + "'";
    }

    public String build() {
        return "DELETE FROM " + table + " WHERE " + id + "=" + data;
    }

}
