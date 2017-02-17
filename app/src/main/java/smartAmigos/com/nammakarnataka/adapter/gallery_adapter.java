package smartAmigos.com.nammakarnataka.adapter;

/**
 * Created by avinashk on 16/02/17.
 */

public class gallery_adapter {

    String url, text;

    public gallery_adapter(String url, String text) {
        this.url = url;
        this.text = text;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {

        return url;
    }

    public String getText() {
        return text;
    }
}
