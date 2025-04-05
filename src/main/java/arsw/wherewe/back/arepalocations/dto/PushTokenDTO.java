package arsw.wherewe.back.arepalocations.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PushTokenDTO {
    private String id;
    private String userId;
    private String groupId;
    @JsonProperty("pushToken")
    private String token;

    public PushTokenDTO() {}

    public PushTokenDTO(String id, String userId, String groupId, String token) {
        this.id = id;
        this.userId = userId;
        this.groupId = groupId;
        this.token = token;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "PushTokenDTO{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", groupId='" + groupId + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}