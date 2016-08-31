package smartAmigos.smartAmigos.com.nammakarnataka.adapter;


public class generic_adapter {

    String image[], title, description, district;
    String bestSeason,additionalInformation;
    Double latitude, longitude;

    public generic_adapter(String[] image, String title, String description, String district, String bestSeason, String additionalInformation, Double latitude, Double longitude) {
        this.image = image;
        this.title = title;
        this.description = description;
        this.district = district;
        this.bestSeason = bestSeason;
        this.additionalInformation = additionalInformation;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setImage(String[] image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setBestSeason(String bestSeason) {
        this.bestSeason = bestSeason;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String[] getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDistrict() {
        return district;
    }

    public String getBestSeason() {
        return bestSeason;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
