package arsw.wherewe.back.arepalocations.model;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Location message sent via WebSocket to share a user's location")
public class LocationMessage {
    @Schema(description = "ID of the user sending the location", example = "user123")
    private String userId;
    @Schema(description = "Latitude coordinate of the user's location", example = "40.785091")
    private double latitude;
    @Schema(description = "Longitude coordinate of the user's location", example = "-73.968285")
    private double longitude;
    @Schema(description = "ID of the group the user belongs to", example = "group123")
    private String groupId;
    @Schema(description = "Status of the user (e.g., active, inactive)", example = "active")
    private String status;
    @Schema(description = "Accuracy of the location in meters", example = "10.0")
    private float accuracy;
    @Schema(description = "Speed of the user in meters per second", example = "1.5")
    private float speed;
    @Schema(description = "Heading direction in degrees", example = "90.0")
    private float heading;
    @Schema(description = "Altitude of the user in meters", example = "50.0")
    private float altitude;
    @Schema(description = "Timestamp of the location message", example = "1696118400000")
    private long timestamp;

    public LocationMessage() {
        this.timestamp = Instant.now().toEpochMilli();
    }

    public LocationMessage(String userId, double latitude, double longitude, String groupId) {
        this.userId = userId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.groupId = groupId;
        this.status = "active";
        this.timestamp = Instant.now().toEpochMilli();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getHeading() {
        return heading;
    }

    public void setHeading(float heading) {
        this.heading = heading;
    }

    public float getAltitude() {
        return altitude;
    }

    public void setAltitude(float altitude) {
        this.altitude = altitude;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "LocationMessage{" +
                "userId='" + userId + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", groupId='" + groupId + '\'' +
                ", status='" + status + '\'' +
                ", accuracy=" + accuracy +
                ", speed=" + speed +
                ", heading=" + heading +
                ", altitude=" + altitude +
                ", timestamp=" + timestamp +
                '}';
    }
}