package world.worldblocks.redis;

import lombok.Getter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.UUID;

public class NetworkPlayer {

    @Getter private UUID uuid;
    @Getter private String lastKnownUsername;
    @Getter private long lastOnlineUnix;
    @Getter private String lastKnownServer;

    public NetworkPlayer(String json) {
        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(json);
            uuid = UUID.fromString((String) jsonObject.get("uuid"));
            lastKnownUsername = (String) jsonObject.get("lastKnownUsername");
            lastOnlineUnix = (Long) jsonObject.get("lastOnlineUnix");
            lastKnownServer = (String) jsonObject.get("lastKnownServer");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public NetworkPlayer(UUID uuid, String lastUsername, String lastKnownServer, long unix) {
        this.uuid = uuid;
        this.lastKnownUsername = lastUsername;
        this.lastKnownServer = lastKnownServer;
        this.lastOnlineUnix = unix;
    }

    public String toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uuid", uuid.toString());
        jsonObject.put("lastKnownUsername", lastKnownUsername);
        jsonObject.put("lastOnlineUnix", lastOnlineUnix);
        jsonObject.put("lastKnownServer", lastKnownServer);
        return jsonObject.toJSONString();
    }

}
