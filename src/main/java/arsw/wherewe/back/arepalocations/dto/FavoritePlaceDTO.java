package arsw.wherewe.back.arepalocations.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO representing a favorite place for a group")
public class FavoritePlaceDTO {
    @Schema(description = "Unique identifier of the favorite place", example = "507f1f77bcf86cd799439011")
    private String id;

    @Schema(description = "Name of the favorite place", example = "Central Park")
    private String placeName;

    @Schema(description = "Latitude coordinate of the place", example = "40.785091")
    private double latitude;

    @Schema(description = "Longitude coordinate of the place", example = "-73.968285")
    private double longitude;

    @Schema(description = "ID of the group associated with this place", example = "group123")
    private String groupId;

    @Schema(description = "Radius around the place in meters", example = "100.0")
    private float radius;

    @Schema(description = "Timestamp when the place was created or updated", example = "1696118400000")
    private long timestamp;

    // Constructors
    public FavoritePlaceDTO() {
        this.timestamp = System.currentTimeMillis();
    }

    public FavoritePlaceDTO(String id, String placeName, double latitude, double longitude, String groupId, float radius, long timestamp) {
        this.id = id;
        this.placeName = placeName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.groupId = groupId;
        this.radius = radius;
        this.timestamp = timestamp;
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
        return "FavoritePlaceDTO{" +
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