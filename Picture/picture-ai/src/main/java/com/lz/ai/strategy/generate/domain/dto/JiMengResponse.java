package com.lz.ai.strategy.generate.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * 即梦返回
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-08-09  22:11
 * @Version: 1.0
 */
@Data
public class JiMengResponse {
    private int code;
    private DataContent data;
    private String message;
    private String request_id;
    private int status;
    private String time_elapsed;

    @Data
    public static class DataContent {
        private AlgorithmBaseResp algorithm_base_resp;
        private List<String> binary_data_base64;
        private List<String> image_urls;
        private String llm_result;
        private String mood_board_llm_result;
        private String mood_board_llm_tag;
        private String pe_result;
        private String predict_tags_result;
        private String rephraser_result;
        private String request_id;
        private String vlm_result;
        private String task_id;
    }

    @Data
    public static class AlgorithmBaseResp {
        private int status_code;
        private String status_message;
    }
}
