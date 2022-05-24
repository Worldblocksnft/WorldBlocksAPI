package net.worldblocks.libs.worldblocksapi.bungee;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import lombok.Getter;
import net.worldblocks.libs.worldblocksapi.WorldBlocksAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

@Getter
@SuppressWarnings({"unchecked", "unstable"})
public class BungeeHandler implements PluginMessageListener {

    private final Map<String, Queue<CompletableFuture<?>>> callbackMap;
    private Map<String, ForwardConsumer> forwardListeners;
    private ForwardConsumer globalForwardListener;

    public BungeeHandler() {
        this.callbackMap = new HashMap<>();
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(WorldBlocksAPI.getInstance(), "BungeeCord");
        Bukkit.getServer().getMessenger().registerIncomingPluginChannel(WorldBlocksAPI.getInstance(), "BungeeCord", this);
        BungeeUtil.setBungeeHandler(this);
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equalsIgnoreCase("BungeeCord")) return;

        ByteArrayDataInput input = ByteStreams.newDataInput(message);
        String subchannel = input.readUTF();

        synchronized (callbackMap) {
            Queue<CompletableFuture<?>> callbacks;

            if (subchannel.equals("PlayerCount") || subchannel.equals("PlayerList") ||
                    subchannel.equals("UUIDOther") || subchannel.equals("ServerIP")) {
                String identifier = input.readUTF(); // Server/player name
                callbacks = callbackMap.get(subchannel + "-" + identifier);

                if (callbacks == null || callbacks.isEmpty()) {
                    return;
                }

                CompletableFuture<?> callback = callbacks.poll();

                try {
                    switch (subchannel) {
                        case "PlayerCount":
                            ((CompletableFuture<Integer>) callback).complete(Integer.valueOf(input.readInt()));
                            break;

                        case "PlayerList":
                            ((CompletableFuture<List<String>>) callback).complete(Arrays.asList(input.readUTF().split(", ")));
                            break;

                        case "UUIDOther":
                            ((CompletableFuture<String>) callback).complete(input.readUTF());
                            break;

                        case "ServerIP": {
                            String ip = input.readUTF();
                            int port = input.readUnsignedShort();
                            ((CompletableFuture<InetSocketAddress>) callback).complete(new InetSocketAddress(ip, port));
                            break;
                        }
                    }
                } catch (Exception ex) {
                    callback.completeExceptionally(ex);
                }

                return;
            }

            callbacks = callbackMap.get(subchannel);

            if (callbacks == null) {
                short dataLength = input.readShort();
                byte[] data = new byte[dataLength];
                input.readFully(data);

                if (globalForwardListener != null) {
                    globalForwardListener.accept(subchannel, player, data);
                }

                if (forwardListeners != null) {
                    synchronized (forwardListeners) {
                        ForwardConsumer listener = forwardListeners.get(subchannel);
                        if (listener != null) {
                            listener.accept(subchannel, player, data);
                        }
                    }
                }

                return;
            }

            if (callbacks.isEmpty()) {
                return;
            }

            final CompletableFuture<?> callback = callbacks.poll();

            try {
                switch (subchannel) {
                    case "GetServers":
                        ((CompletableFuture<List<String>>) callback).complete(Arrays.asList(input.readUTF().split(", ")));
                        break;

                    case "GetServer":
                    case "UUID":
                        ((CompletableFuture<String>) callback).complete(input.readUTF());
                        break;

                    case "IP": {
                        String ip = input.readUTF();
                        int port = input.readInt();
                        ((CompletableFuture<InetSocketAddress>) callback).complete(new InetSocketAddress(ip, port));
                        break;
                    }

                    default:
                        break;
                }
            } catch (Exception ex) {
                callback.completeExceptionally(ex);
            }
        }
    }

    public void compute(String string, CompletableFuture<?> completableFuture) {
        callbackMap.compute(string, computeQueueValue(completableFuture));
    }

    private BiFunction<String, Queue<CompletableFuture<?>>, Queue<CompletableFuture<?>>> computeQueueValue(CompletableFuture<?> queueValue) {
        return (key, value) -> {
            if (value == null) value = new ArrayDeque<CompletableFuture<?>>();
            value.add(queueValue);
            return value;
        };
    }

}
