package net.worldblocks.libs.worldblocksapi;

import net.worldblocks.libs.worldblocksapi.configuration.WBConfig;

import java.io.Closeable;

public interface Service {

    void init(WBConfig config);

    boolean isInitialized();

    void close();
}
