package arsw.wherewe.back.arepalocations.model;

import java.time.Instant;

public class LocationMessage {
    private String userId;
    private double latitude;
    private double longitude;
    private String groupId;
    private String status;
    private float accuracy;
    private float speed;
    private float heading;
    private float altitude;
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
                ", timestamp=" + timestamp +
                '}';
    }
}