package net.worldblocks.libs.worldblocksapi.databases;

import lombok.Getter;

public class PostgresData<K> {

    @Getter private String key;
    @Getter private K value;
    public PostgresData(String key, Object value) {
        this.key = key;
        this.value = (K) value;
    }

    public static <K> PostgresData<K> of(String key, Object value) {
        return new PostgresData<>(key, value);
    }

}
