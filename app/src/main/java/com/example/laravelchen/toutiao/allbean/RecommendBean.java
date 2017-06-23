package com.example.laravelchen.toutiao.allbean;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by LaravelChen on 2017/6/10.
 */

public class RecommendBean {
    private String title;
    private String img;
    private String img2;
    private String img3;
    private String source;
    private boolean hasimg;
    private String comment_couont;
    private String share_url;
    private String group_id;

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }

    public boolean isHasimg() {
        return hasimg;
    }

    public void setHasimg(boolean hasimg) {
        this.hasimg = hasimg;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getComment_couont() {
        return comment_couont;
    }

    public void setComment_couont(String comment_couont) {
        this.comment_couont = comment_couont;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
