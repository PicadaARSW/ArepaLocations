package arsw.wherewe.back.arepalocations.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "push_tokens")
public class PushToken {
    @Id
    private String id;
    private String userId;
    private String groupId;
    @Field("pushToken")
    @JsonProperty("pushToken")
    private String token;

    public PushToken() {}

    public PushToken(String userId, String groupId, String token) {
        this.userId = userId;
        this.groupId = groupId;
        this.token = token;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}