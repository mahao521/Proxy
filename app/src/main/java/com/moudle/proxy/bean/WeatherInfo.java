package com.moudle.proxy.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/8/8.
 */

public class WeatherInfo {
    private String status;
    private String count;
    private String info;
    private String infocode;
    private List<Lives> lives;

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCount() {
        return count;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public void setInfocode(String infocode) {
        this.infocode = infocode;
    }

    public String getInfocode() {
        return infocode;
    }

    public void setLives(List<Lives> lives) {
        this.lives = lives;
    }

    public List<Lives> getLives() {
        return lives;
    }
}
