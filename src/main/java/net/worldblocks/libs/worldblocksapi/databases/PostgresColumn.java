package net.worldblocks.libs.worldblocksapi.databases;

import lombok.Getter;

public class PostgresColumn {

    @Getter private String columnID;
    @Getter private PostgresType columnType;

    public PostgresColumn(String columnID, PostgresType columnType) {
        this.columnID = columnID;
        this.columnType = columnType;
    }

}
