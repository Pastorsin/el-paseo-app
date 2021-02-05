package laboratorio.app.viewmodels;

public class Ubication {
    private Double latitude;
    private Double longitude;

    public Ubication() {

    }

    public Ubication(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public boolean isDefined() {
        return latitude != null && longitude != null;
    }
}
