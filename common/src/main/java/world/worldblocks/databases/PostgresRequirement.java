package world.worldblocks.databases;

import lombok.Getter;
import lombok.Setter;

public class PostgresRequirement {

    @Getter private String key;
    @Getter private Object requirement;
    @Getter @Setter private String comparator;

    public PostgresRequirement(String key, Object requirement) {
        this.key = key;
        this.requirement = requirement;
        this.comparator = "=";
    }

}
