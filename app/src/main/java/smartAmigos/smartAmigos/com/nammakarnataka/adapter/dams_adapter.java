package smartAmigos.smartAmigos.com.nammakarnataka.adapter;

/**
 * Created by avinashk on 25/08/16.
 */
public class dams_adapter {




    String image, damTitle, damDescription, district;
    Double latitude, longitude;


    public dams_adapter(String image, String damTitle, String damDescription, String district, Double latitude, Double longitude) {
        this.image = image;
        this.damTitle = damTitle;
        this.damDescription = damDescription;
        this.district = district;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setImage(String image) {

        this.image = image;
    }

    public void setDamTitle(String damTitle) {
        this.damTitle = damTitle;
    }

    public void setDamDescription(String damDescription) {
        this.damDescription = damDescription;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getImage() {

        return image;
    }

    public String getDamTitle() {
        return damTitle;
    }

    public String getDamDescription() {
        return damDescription;
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
