package world.worldblocks.location;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@Data
public class LocationWrapper {

    private final float yaw;
    private final float pitch;
    private final String world;
    private final String instance;
    private final double x;
    private final double y;
    private final double z;

    public LocationWrapper(double x, double y, double z, float yaw, float pitch, String world, String instance) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.world = world;
        this.instance = instance;
    }

    public LocationWrapper(Location location, String instance) {
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.world = location.getWorld().getName();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
        this.instance = instance;
    }

    public Location toBukkitLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

}
