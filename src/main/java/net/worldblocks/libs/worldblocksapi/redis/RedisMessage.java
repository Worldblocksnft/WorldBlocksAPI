package net.worldblocks.libs.worldblocksapi.redis;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RedisMessage {

    private String id;
    private String namespace;
    private List<String> received = new ArrayList<>();

    public RedisMessage(String namespace, String json) {
        this.namespace = namespace;
        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(json);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void addReceived(UUID uuid) {
        received.add(uuid.toString());
    }

}
