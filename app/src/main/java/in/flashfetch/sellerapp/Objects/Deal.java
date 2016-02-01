package in.flashfetch.sellerapp.Objects;

import android.graphics.Bitmap;

/**
 * Created by SAM10795 on 30-01-2016.
 */
public class Deal {
    public String Name;
    public String Price;
    public String time;
    public Bitmap image;

    public Bitmap getImage() {
        return image;
    }

    public String getName() {
        return Name;
    }

    public String getPrice() {
        return Price;
    }

    public String getTime() {
        return time;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

