package com.example.laravelchen.toutiao.allbean;

/**
 * Created by LaravelChen on 2017/6/12.
 */

public class PhotoBean {
    private String title;
    private String img1;
    private String source;
    private String tag;
    private String gallary_image_count;
    private String group_id;

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGallary_image_count() {
        return gallary_image_count;
    }

    public void setGallary_image_count(String gallary_image_count) {
        this.gallary_image_count = gallary_image_count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
