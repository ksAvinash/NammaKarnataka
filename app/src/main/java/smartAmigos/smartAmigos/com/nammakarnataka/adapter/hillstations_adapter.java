package smartAmigos.smartAmigos.com.nammakarnataka.adapter;

/**
 * Created by avinashk on 25/08/16.
 */
public class hillstations_adapter {


    String image, hillstationsTitle, hillstationsDescription, district;
    Double latitude, longitude;


    public hillstations_adapter(String image, String hillstationsTitle, String hillstationsDescription, String district, Double latitude, Double longitude) {
        this.image = image;
        this.hillstationsTitle = hillstationsTitle;
        this.hillstationsDescription = hillstationsDescription;
        this.district = district;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getImage() {
        return image;
    }

    public String getHillstationsTitle() {
        return hillstationsTitle;
    }

    public String getHillstationsDescription() {
        return hillstationsDescription;
    }

    public String getDistrict() {
        return district;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
