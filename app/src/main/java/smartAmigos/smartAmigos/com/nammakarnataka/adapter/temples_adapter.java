package smartAmigos.smartAmigos.com.nammakarnataka.adapter;


public class temples_adapter {

    String image, templeTitle, templeDescription, district;
    Double latitude, longitude;


    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTempleTitle(String templeTitle) {
        this.templeTitle = templeTitle;
    }

    public void setTempleDescription(String templeDescription) {
        this.templeDescription = templeDescription;
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

    public String getTempleTitle() {
        return templeTitle;
    }

    public String getTempleDescription() {
        return templeDescription;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public temples_adapter(String image, String templeTitle, String templeDescription, String district, Double latitude, Double longitude) {
        this.image = image;
        this.templeTitle = templeTitle;
        this.templeDescription = templeDescription;
        this.district = district;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
