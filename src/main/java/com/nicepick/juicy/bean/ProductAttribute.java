package com.nicepick.juicy.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductAttribute {
    public int version;
    public String pid;
    @JsonProperty("thumbnail_url")
    public String thumbnailUrl;
    @JsonProperty("showcase_urls")
    public List<String> scrollImages;
    @JsonProperty("detail_urls")
    public List<String> detailImages;
    public long volume;

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
