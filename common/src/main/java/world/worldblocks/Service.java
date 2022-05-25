package world.worldblocks;

import world.worldblocks.configuration.WBConfig;

public interface Service {

    void init(WBConfig config);

    boolean isInitialized();

    void close();
}
