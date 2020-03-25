package com.sb.android.Home;

/**
 * Created by singhsh on 9/17/2019.
 */

public class newsinfo {

    String[] title;
    String[] description;
    String[] imageurl;
    String[] sourceurl;
    public newsinfo(){
        title = new String[22];
        description = new String[22];
        imageurl = new String[22];
        sourceurl = new String[22];
    }
    public String getfirstelement(){
        return title[0];
    }
    public String getSourceurl(int i) {
        return sourceurl[i];
    }

    public void setSourceurl(String[] sourceurl) {
        this.sourceurl = sourceurl;
    }

    public String getTitle(int i) {
        return title[i];
    }

    public void setTitle(String[] title) {
        this.title = title;
    }

    public String getDescription(int i) {
        return description[i];
    }

    public void setDescription(String[] description) {
        this.description = description;
    }

    public String getImageurl(int i) {
        return imageurl[i];
    }

    public void setImageurl(String[] imageurl) {
        this.imageurl = imageurl;
    }
}
