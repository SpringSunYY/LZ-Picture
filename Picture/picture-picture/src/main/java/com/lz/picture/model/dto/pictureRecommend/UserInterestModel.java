package com.lz.picture.model.dto.pictureRecommend;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户兴趣模型
 */
@Data
public class UserInterestModel {
    private Map<String, Double> tagScores; // tag -> score
    private Map<String, Double> categoryScores; // categoryId -> score
    private Date updateTime;
    private List<String> topTags;
    private List<String> topCategories;

    private Map<String, Double> normalizedTagScores; // 存储标签归一化结果
    private Map<String, Double> normalizedCategoryScores; // 存储分类归一化结果

    // 归一化方法（补充到UserInterestModel类）
    public void normalizeScores() {
        double maxCategory = categoryScores.values().stream().max(Double::compare).orElse(1.0);
        double maxTag = tagScores.values().stream().max(Double::compare).orElse(1.0);

        // 分类归一化：映射到[0,1]
        normalizedCategoryScores = categoryScores.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue() / maxCategory
                ));

        // 标签归一化（同理）
        normalizedTagScores = tagScores.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue() / maxTag
                ));
    }
}
