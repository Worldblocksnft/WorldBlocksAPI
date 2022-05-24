package net.worldblocks.libs.worldblocksapi.cache;

import java.io.Closeable;

public interface CacheClient {
  void connect();

  Object getObject(String key);

  void putObject(String key, Object object);

  String getStr(String key);

  void putStr(String key, String value);

  void rm(String key);

  void close();
}
