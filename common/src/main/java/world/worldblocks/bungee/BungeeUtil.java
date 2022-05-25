package world.worldblocks.bungee;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import world.worldblocks.WorldBlocksAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@UtilityClass
public class BungeeUtil {

    @Setter
    private BungeeHandler bungeeHandler;

    private Player getFirstPlayer() {
        Optional<? extends Player> optionalPlayer = Bukkit.getOnlinePlayers().stream().findFirst();
        return optionalPlayer.orElse(null);
    }

    public void send(Player player, String serverName) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("Connect");
        output.writeUTF(serverName);
        player.sendPluginMessage(WorldBlocksAPI.getInstance(), "BungeeCord", output.toByteArray());
    }

    public void send(String target, String server) {
        Player player = getFirstPlayer();
        ByteArrayDataOutput output = ByteStreams.newDataOutput();

        output.writeUTF("ConnectOther");
        output.writeUTF(target);
        output.writeUTF(server);
        player.sendPluginMessage(WorldBlocksAPI.getInstance(), "BungeeCord", output.toByteArray());
    }

    public CompletableFuture<String> getServer() {
        Player player = getFirstPlayer();
        CompletableFuture<String> future = new CompletableFuture<>();

        synchronized (WorldBlocksAPI.getInstance().getBungeeHandler().getCallbackMap()) {
            bungeeHandler.compute("GetServer", future);
        }

        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("GetServer");
        player.sendPluginMessage(WorldBlocksAPI.getInstance(), "BungeeCord", output.toByteArray());
        return future;
    }

    public CompletableFuture<InetSocketAddress> getIp(Player player) {
        CompletableFuture<InetSocketAddress> future = new CompletableFuture<>();

        synchronized (bungeeHandler.getCallbackMap()) {
            bungeeHandler.compute("IP", future);
        }

        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("IP");
        player.sendPluginMessage(WorldBlocksAPI.getInstance(), "BungeeCord", output.toByteArray());
        return future;
    }



}
