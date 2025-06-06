package com.lz.picture.model.dto.pictureRecommend;

import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * 用户兴趣模型
 */
@Data
public class UserInterestModel {
    private Map<String, Double> tagScores; // tag -> score
    private Map<String, Double> categoryScores; // categoryId -> score
    private Date updateTime;
}
