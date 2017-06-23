package com.example.laravelchen.toutiao;

import java.util.Random;

/**
 * Created by LaravelChen on 2017/6/22.
 */

public class PathRandom {
    private Random random;
    private static final String[] homepath = {
            "https://www.toutiao.com/api/article/feed/?category=news_hot&utm_source=toutiao&widen=1&max_behot_time=0&max_behot_time_tmp=0&tadrequire=true&as=A11539B37F25C95&cp=593FA5BC29856E1",
            "https://www.toutiao.com/api/article/feed/?category=news_society&utm_source=toutiao&widen=1&max_behot_time=0&max_behot_time_tmp=0&tadrequire=true&as=A11539B37F25C95&cp=593FA5BC29856E1",
            "https://www.toutiao.com/api/article/feed/?category=news_entertainment&utm_source=toutiao&widen=1&max_behot_time=0&max_behot_time_tmp=0&tadrequire=true&as=A11539B37F25C95&cp=593FA5BC29856E1",
            "https://www.toutiao.com/api/article/feed/?category=news_tech&utm_source=toutiao&widen=1&max_behot_time=0&max_behot_time_tmp=0&tadrequire=true&as=A11539B37F25C95&cp=594BDA7327C3FE1",
            "https://www.toutiao.com/api/article/feed/?category=news_sports&utm_source=toutiao&widen=1&max_behot_time=0&max_behot_time_tmp=0&tadrequire=true&as=A1C52904FB1A3B1&cp=594B0A339B915E1",
            "https://www.toutiao.com/api/article/feed/?category=news_finance&utm_source=toutiao&widen=1&max_behot_time=0&max_behot_time_tmp=0&tadrequire=true&as=A17599547B4A3F7&cp=594B2A233FC7EE1",
            "https://www.toutiao.com/api/article/feed/?category=news_world&utm_source=toutiao&widen=1&max_behot_time=0&max_behot_time_tmp=0&tadrequire=true&as=A1750974FBEA42C&cp=594BDAA4E2EC5E1",
    };
    private static final String[] photopath = {
            "https://www.toutiao.com/api/article/feed/?category=gallery_photograthy&utm_source=toutiao&max_behot_time=0&as=A1F549836F636E8&cp=593F23967E985E1",
            "http://www.toutiao.com/api/article/feed/?category=%E7%BB%84%E5%9B%BE&utm_source=toutiao&max_behot_time=0&as=A195B9640B4A5D9&cp=594BDAF54D498E1",
            "http://www.toutiao.com/api/article/feed/?category=gallery_old_picture&utm_source=toutiao&max_behot_time=0&as=A1C579E48B1A73D&cp=594BBA57E30DEE1",
    };
    private static final String[] videopath={
            "https://www.toutiao.com/api/article/feed/?category=video&utm_source=toutiao&max_behot_time=0&as=A1F549836F636E8&cp=593F23967E985E1",
            "https://www.toutiao.com/api/article/feed/?category=subv_voice&utm_source=toutiao&widen=1&max_behot_time=0&max_behot_time_tmp=0&tadrequire=true&as=A1F549836F636E8&cp=593F23967E985E1",
            "https://www.toutiao.com/api/article/feed/?category=subv_funny&utm_source=toutiao&widen=1&max_behot_time=0&max_behot_time_tmp=0&tadrequire=true&as=A1855934BBAAB1E&cp=594B3A9BD1DE5E1",
            "https://www.toutiao.com/api/article/feed/?category=subv_society&utm_source=toutiao&widen=1&max_behot_time=0&max_behot_time_tmp=0&tadrequire=true&as=A15529E4AB7AB4B&cp=594B5A5BA44B7E1",
            "https://www.toutiao.com/api/article/feed/?category=%E4%B8%AD%E5%9B%BD%E6%96%B0%E5%94%B1%E5%B0%86&utm_source=toutiao&widen=1&max_behot_time=0&max_behot_time_tmp=0&tadrequire=true&as=A1C559B4BBCABD6&cp=594BBA7B5DE6BE1",
            "https://www.toutiao.com/api/article/feed/?category=subv_entertainment&utm_source=toutiao&widen=1&max_behot_time=0&max_behot_time_tmp=0&tadrequire=true&as=A1A5D934FB4AC92&cp=594B1AFC29929E1",
    };

    public String getHomePath() {
        random = new Random();
        int i = random.nextInt(homepath.length);
        return homepath[i];
    }

    public String getPhotoPath() {
        random = new Random();
        int i = random.nextInt(photopath.length);
        return photopath[i];
    }

    public String getVideoPath(){
        random = new Random();
        int i = random.nextInt(videopath.length);
        return videopath[i];
    }
}
