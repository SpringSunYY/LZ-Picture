package com.lz.picture.strategy.userPictureApiSearchStrategy.dto;

import lombok.Data;

import java.util.List;

@Data
public class PexelsResponse {
    private int page;
    private int per_page;
    private List<Photo> photos;
    private int total_results;
    private String next_page;

    @Data
    public static class Photo {
        private long id;
        private int width;
        private int height;
        private String url;
        private String photographer;
        private String photographer_url;
        private long photographer_id;
        private String avg_color;
        private Src src;
        private boolean liked;
        private String alt;
    }

    @Data
    public static class Src {
        private String original;
        private String large2x;
        private String large;
        private String medium;
        private String small;
        private String portrait;
        private String landscape;
        private String tiny;
    }
}


