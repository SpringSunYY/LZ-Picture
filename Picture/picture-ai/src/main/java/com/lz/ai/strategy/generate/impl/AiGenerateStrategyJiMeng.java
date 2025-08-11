package com.lz.ai.strategy.generate.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.lz.ai.mapper.GenerateLogInfoMapper;
import com.lz.ai.model.domain.GenerateLogInfo;
import com.lz.ai.model.dto.generateLogInfo.GenerateLogInfoDto;
import com.lz.ai.model.enums.AiLogStatusEnum;
import com.lz.ai.strategy.generate.AiGenerateStrategyConfig;
import com.lz.ai.strategy.generate.domain.dto.JiMengResponse;
import com.lz.ai.strategy.generate.domain.params.JiMengParams;
import com.lz.ai.strategy.generate.domain.verify.JiMengVerify;
import com.lz.common.enums.CommonDeleteEnum;
import com.lz.common.enums.CommonHasStatisticsEnum;
import com.lz.common.manager.file.PictureUploadManager;
import com.lz.common.manager.file.model.FileResponse;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.uuid.IdUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Future;

import static com.lz.common.constant.Constants.COMMON_SEPARATOR;

/**
 * 即梦生成
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-08-09  00:11
 * @Version: 1.0
 */
@Slf4j
@AiGenerateStrategyConfig(model = "JiMeng")
public class AiGenerateStrategyJiMeng extends AiGenerateStrategyTemplate {
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    private PictureUploadManager pictureUploadManager;

    @Resource
    private GenerateLogInfoMapper generateLogInfoMapper;

    private static final BitSet URLENCODER = new BitSet(256);
    private static final String CONST_ENCODE = "0123456789ABCDEF";
    public static final Charset UTF_8 = StandardCharsets.UTF_8;


    static {
        int i;
        for (i = 97; i <= 122; ++i) {
            URLENCODER.set(i);
        }
        for (i = 65; i <= 90; ++i) {
            URLENCODER.set(i);
        }
        for (i = 48; i <= 57; ++i) {
            URLENCODER.set(i);
        }
        URLENCODER.set('-');
        URLENCODER.set('_');
        URLENCODER.set('.');
        URLENCODER.set('~');
    }

    @Override
    public String userGenerate(GenerateLogInfoDto info) {
        JiMengParams params = verify(info);
        //拼接参数
        String json = JSONObject.toJSONString(params);
        try {
            System.out.println("json = " + json);
            List<Future<JiMengResponse>> futures = new ArrayList<>();
            for (int i = 0; i < info.getNumbers(); i++) {
                Future<JiMengResponse> future = threadPoolTaskExecutor.submit(() -> {
                    long startTime = System.currentTimeMillis();
                    JiMengResponse jiMengResponse = null;
                    try {
                        jiMengResponse = doRequest(params.getMethod(), new HashMap<>(), json.getBytes(UTF_8), new Date(),
                                params.getAction(), params.getVersion(), params.getRegion(), params.getService(),
                                params.getPath(), info.getApiKey(), info.getSecretKey(), params.getHost(), params.getSchema());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    long totalTime = System.currentTimeMillis() - startTime;
                    GenerateLogInfo generateLogInfo = processResult(jiMengResponse, info, json, totalTime);
                    generateLogInfoMapper.insert(generateLogInfo);
                    System.out.println("generateLogInfo = " + generateLogInfo);
                    return jiMengResponse;
                });
                futures.add(future);
            }
            futures.forEach(future -> {
                try {
                    JiMengResponse jiMengResponse = future.get();
                    System.out.println("jiMengResponse = " + jiMengResponse);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            //处理结果
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return info.getTargetId();
    }

    private GenerateLogInfo processResult(JiMengResponse jiMengResponse, GenerateLogInfoDto info, String json, long totalTime) {
        GenerateLogInfo generateLogInfo = GenerateLogInfo(jiMengResponse, info, json, totalTime);

        if (jiMengResponse.getCode() == 10000) {
            generateLogInfo.setLogStatus(AiLogStatusEnum.LOG_STATUS_1.getValue());
            JiMengResponse.DataContent data = jiMengResponse.getData();
            List<String> imageUrls = data.getImage_urls();
            for (String imageUrl : imageUrls) {
                try {
//                    // 下载图片
//                    URL urlImage = new URL(imageUrl);
//                    try (InputStream in = urlImage.openStream();
//                         FileOutputStream fos = new FileOutputStream("E:/Project/Picture/files/AIgenerate/generated_image" + System.currentTimeMillis() + ".jpg")) {
//                        byte[] bufferImage = new byte[1024];
//                        int bytesReadImage;
//                        while ((bytesReadImage = in.read(bufferImage)) != -1) {
//                            fos.write(bufferImage, 0, bytesReadImage);
//                        }
//                        System.out.println("图片已保存为 generated_image.jpg");
//                    }
                    //上传图片
                    FileResponse fileResponse = pictureUploadManager.uploadUrlByAiGenerate(imageUrl, "generate", info.getUsername());
                    generateLogInfo.setFileUrls(fileResponse.getUrl() + COMMON_SEPARATOR + fileResponse.getThumbnailUrl());
                    System.out.println("fileResponse = " + fileResponse);
                } catch (Exception e) {
                    log.error("图片保存失败: " + e.getMessage());
                }
            }
        } else {
            generateLogInfo.setLogStatus(AiLogStatusEnum.LOG_STATUS_2.getValue());
        }
        return generateLogInfo;
    }

    /**
     * 设置日志信息
     *
     * @param jiMengResponse 响应
     * @param info           请求
     * @param json           请求参数
     * @param totalTime      总耗时
     * @return GenerateLogInfo
     * @author: YY
     * @method: GenerateLogInfo 获取日志
     * @date: 2025/8/11 16:16
     **/
    private GenerateLogInfo GenerateLogInfo(JiMengResponse jiMengResponse, GenerateLogInfoDto info, String json, long totalTime) {
        GenerateLogInfo generateLogInfo = new GenerateLogInfo();
        generateLogInfo.setLogId(IdUtils.fastSimpleUUID());
        generateLogInfo.setUserId(info.getUserId());
        generateLogInfo.setModelKey(info.getModelKey());
        generateLogInfo.setModelType(info.getModelType());
        generateLogInfo.setInputFile(info.getInputFile());
        generateLogInfo.setPrompt(info.getPrompt());
        generateLogInfo.setNegativePrompt(info.getNegativePrompt());
        generateLogInfo.setSeed(info.getSeed());
        generateLogInfo.setNumbers(info.getNumbers());
        generateLogInfo.setIpAddr(info.getIpAddr());
        generateLogInfo.setDeviceId(info.getDeviceId());
        generateLogInfo.setBrowser(info.getBrowser());
        generateLogInfo.setOs(info.getOs());
        generateLogInfo.setPlatform(info.getPlatform());
        generateLogInfo.setInputParams(json);
        generateLogInfo.setTaskId(jiMengResponse.getRequest_id());
        generateLogInfo.setOutputResult(JSON.toJSONString(jiMengResponse));
        generateLogInfo.setWidth(info.getWidth());
        generateLogInfo.setHeight(info.getHeight());
        generateLogInfo.setRequestTime(new Date());
        generateLogInfo.setRequestDuration(totalTime);
        generateLogInfo.setPriceUsed(info.getPriceUse().multiply(BigDecimal.valueOf(info.getNumbers())));
        generateLogInfo.setPointsUsed(info.getPointsNeed() * info.getNumbers());
        generateLogInfo.setTargetId(info.getTargetId());
        generateLogInfo.setLogStatus(AiLogStatusEnum.LOG_STATUS_2.getValue());
        generateLogInfo.setAiStatusCode(String.valueOf(jiMengResponse.getCode()));
        generateLogInfo.setFailReason(jiMengResponse.getMessage());
        generateLogInfo.setHasStatistics(CommonHasStatisticsEnum.HAS_STATISTICS_0.getValue());
        generateLogInfo.setCreateTime(new Date());
        generateLogInfo.setUpdateTime(new Date());
        generateLogInfo.setIsDelete(CommonDeleteEnum.DELETED.getValue());
        return generateLogInfo;
    }

    public JiMengParams verify(GenerateLogInfoDto info) {
        JiMengVerify jiMengVerify = new JiMengVerify();
        jiMengVerify = jiMengVerify.jsonToObj(info.getExtendConfig());
        JiMengParams jiMengParams = new JiMengParams();
        jiMengParams = jiMengParams.jsonToObj(info.getModelParams());
        //判断参数 宽高
        Integer width = info.getWidth();
        Integer height = info.getHeight();
        Integer maxWidth = jiMengVerify.getMaxWidth();
        Integer minWidth = jiMengVerify.getMinWidth();
        Integer maxHeight = jiMengVerify.getMaxHeight();
        Integer minHeight = jiMengVerify.getMinHeight();
        commonVerify(info, width, minWidth, maxWidth, height, minHeight, maxHeight, jiMengParams, jiMengVerify);
        Integer numbers = info.getNumbers();
        if (numbers > jiMengVerify.getMaxNumbers()) {
            info.setNumbers(jiMengVerify.getMaxNumbers());
        }
        if (StringUtils.isNotNull(info.getSeed())) {
            jiMengParams.setSeed(info.getSeed().intValue());
        }
        return jiMengParams;
    }

    public JiMengResponse doRequest(String method, Map<String, String> queryList, byte[] body,
                                    Date date, String action, String version,
                                    String region, String service, String path,
                                    String ak, String sk, String host, String schema) throws Exception {
        if (body == null) {
            body = new byte[0];
        }
        String xContentSha256 = hashSHA256(body);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        String xDate = sdf.format(date);
        String shortXDate = xDate.substring(0, 8);
        String contentType = "application/json";
        String signHeader = "host;x-date;x-content-sha256;content-type";

        SortedMap<String, String> realQueryList = new TreeMap<>(queryList);
        realQueryList.put("Action", action);
        realQueryList.put("Version", version);
        StringBuilder querySB = new StringBuilder();
        for (String key : realQueryList.keySet()) {
            querySB.append(signStringEncoder(key)).append("=").append(signStringEncoder(realQueryList.get(key))).append("&");
        }
        querySB.deleteCharAt(querySB.length() - 1);

        String canonicalStringBuilder = method + "\n" + path + "\n" + querySB + "\n" +
                "host:" + host + "\n" +
                "x-date:" + xDate + "\n" +
                "x-content-sha256:" + xContentSha256 + "\n" +
                "content-type:" + contentType + "\n" +
                "\n" +
                signHeader + "\n" +
                xContentSha256;

        System.out.println(canonicalStringBuilder);

        String hashcanonicalString = hashSHA256(canonicalStringBuilder.getBytes());
        String credentialScope = shortXDate + "/" + region + "/" + service + "/request";
        String signString = "HMAC-SHA256" + "\n" + xDate + "\n" + credentialScope + "\n" + hashcanonicalString;

        byte[] signKey = genSigningSecretKeyV4(sk, shortXDate, region, service);
        String signature = HexFormat.of().formatHex(hmacSHA256(signKey, signString));

        URL url = new URL(schema + "://" + host + path + "?" + querySB);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Host", host);
        conn.setRequestProperty("X-Date", xDate);
        conn.setRequestProperty("X-Content-Sha256", xContentSha256);
        conn.setRequestProperty("Content-Type", contentType);
        conn.setRequestProperty("Authorization", "HMAC-SHA256" +
                " Credential=" + ak + "/" + credentialScope +
                ", SignedHeaders=" + signHeader +
                ", Signature=" + signature);
        if (!Objects.equals(conn.getRequestMethod(), "GET")) {
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            os.write(body);
            os.flush();
            os.close();
        }
        conn.connect();

        int responseCode = conn.getResponseCode();

        InputStream is;
        if (responseCode == 200) {
            is = conn.getInputStream();
        } else {
            is = conn.getErrorStream();
        }
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int bytesRead;
        byte[] imageData = new byte[8192];
        while ((bytesRead = is.read(imageData, 0, imageData.length)) != -1) {
            buffer.write(imageData, 0, bytesRead);
        }
        String responseBody = buffer.toString(StandardCharsets.UTF_8);
        is.close();
        return JSONObject.parseObject(responseBody, JiMengResponse.class);
    }

    private String signStringEncoder(String source) {
        if (source == null) {
            return null;
        }
        StringBuilder buf = new StringBuilder(source.length());
        ByteBuffer bb = UTF_8.encode(source);
        while (bb.hasRemaining()) {
            int b = bb.get() & 255;
            if (URLENCODER.get(b)) {
                buf.append((char) b);
            } else if (b == 32) {
                buf.append("%20");
            } else {
                buf.append("%");
                char hex1 = CONST_ENCODE.charAt(b >> 4);
                char hex2 = CONST_ENCODE.charAt(b & 15);
                buf.append(hex1);
                buf.append(hex2);
            }
        }

        return buf.toString();
    }

    public static String hashSHA256(byte[] content) throws Exception {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");


            return Hex.encodeHexString(md.digest(content));
        } catch (Exception e) {
            throw new Exception(
                    "Unable to compute hash while signing request: "
                            + e.getMessage(), e);
        }
    }

    public static byte[] hmacSHA256(byte[] key, String content) throws Exception {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key, "HmacSHA256"));
            return mac.doFinal(content.getBytes());
        } catch (Exception e) {
            throw new Exception(
                    "Unable to calculate a request signature: "
                            + e.getMessage(), e);
        }
    }

    private byte[] genSigningSecretKeyV4(String secretKey, String date, String region, String service) throws Exception {
        byte[] kDate = hmacSHA256((secretKey).getBytes(), date);
        byte[] kRegion = hmacSHA256(kDate, region);
        byte[] kService = hmacSHA256(kRegion, service);
        return hmacSHA256(kService, "request");
    }
}
