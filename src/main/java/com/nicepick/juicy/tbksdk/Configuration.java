package com.nicepick.juicy.tbksdk;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("taobao.api")
public class Configuration {
    private String appKey;
    private String appSecret;
    private String appUrl;
    private String worldUrl;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getWorldUrl() {
        return worldUrl;
    }

    public void setWorldUrl(String worldUrl) {
        this.worldUrl = worldUrl;
    }

    public enum PLATFORM {
        PC(1),
        MOBILE(2);
        PLATFORM(long code) {
            this.code = code;
        }
        public long value() {
            return this.code;
        }
        private final long code;
    }

    public enum DOMAIN {
        TAOBAO_PC("item.taobao.com"),
        TAOBAO_MOBILE("h5.m.taobao.com"),
        TMALL_PC("detail.tmall.com"),
        TMALL_MOBILE("detail.m.tmall.com");
        public boolean accept(String url) {
            return url != null && url.contains(keyword);
        }
        DOMAIN(String keyword) {
            this.keyword = keyword;
        }
        private final String keyword;
    }
}
