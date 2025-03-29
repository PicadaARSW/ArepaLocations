package arsw.wherewe.back.arepalocations.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "locations")
public class FavoritePlace {
    @Id
    private String id;
    private String placeName;
    private double latitude;
    private double longitude;
    private String groupId;
    private float radius; // in meters
    private long timestamp;

    // Constructors
    public FavoritePlace() {
        this.timestamp = System.currentTimeMillis();
    }

    public FavoritePlace(String placeName, double latitude, double longitude, String groupId, float radius) {
        this.placeName = placeName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.groupId = groupId;
        this.radius = radius;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
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

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "FavoritePlace{" +
                "id='" + id + '\'' +
                ", placeName='" + placeName + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", groupId='" + groupId + '\'' +
                ", radius=" + radius +
                ", timestamp=" + timestamp +
                '}';
    }
}
