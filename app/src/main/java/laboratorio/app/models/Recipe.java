package laboratorio.app.models;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Recipe implements Serializable {
    private String title;
    private Bitmap image;
    private String text;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Bitmap getImage() {
        return image;
    }

    public boolean hasImage(){
        return (image != null);
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
