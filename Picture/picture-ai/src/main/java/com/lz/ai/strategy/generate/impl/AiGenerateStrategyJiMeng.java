package com.lz.ai.strategy.generate.impl;


import com.alibaba.fastjson2.JSONObject;
import com.lz.ai.model.domain.GenerateLogInfo;
import com.lz.ai.model.dto.generateLogInfo.GenerateLogInfoDto;
import com.lz.ai.strategy.generate.AiGenerateStrategyConfig;
import com.lz.ai.strategy.generate.domain.dto.JiMengResponse;
import com.lz.ai.strategy.generate.domain.params.JiMengParams;
import com.lz.ai.strategy.generate.domain.verify.JiMengVerify;
import com.lz.common.utils.StringUtils;
import jakarta.annotation.Resource;
import org.apache.commons.codec.binary.Hex;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Future;

/**
 * 即梦生成
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-08-09  00:11
 * @Version: 1.0
 */
@AiGenerateStrategyConfig(model = "JiMeng")
public class AiGenerateStrategyJiMeng extends AiGenerateStrategyTemplate {
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
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
            for (Integer i = 0; i < info.getNumbers(); i++) {
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
                    processResult(jiMengResponse, info, totalTime);
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

    private GenerateLogInfo processResult(JiMengResponse jiMengResponse, GenerateLogInfoDto info, long totalTime) {
        GenerateLogInfo generateLogInfo = new GenerateLogInfo();

        if (jiMengResponse.getCode() == 10000) {
            JiMengResponse.DataContent data = jiMengResponse.getData();
            List<String> imageUrls = data.getImage_urls();
            for (String imageUrl : imageUrls) {
                try {
                    // 下载图片
                    URL urlImage = new URL(imageUrl);
                    try (InputStream in = urlImage.openStream();
                         FileOutputStream fos = new FileOutputStream("E:/Project/Picture/files/AIgenerate/generated_image" + System.currentTimeMillis() + ".jpg")) {
                        byte[] bufferImage = new byte[1024];
                        int bytesReadImage;
                        while ((bytesReadImage = in.read(bufferImage)) != -1) {
                            fos.write(bufferImage, 0, bytesReadImage);
                        }
                        System.out.println("图片已保存为 generated_image.jpg");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
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
//        String signature = Hex.encodeHexString(Utils.hmacSHA256(signKey, signString));

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
