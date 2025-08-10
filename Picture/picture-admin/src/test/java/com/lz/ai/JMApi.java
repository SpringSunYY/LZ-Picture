package com.lz.ai;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Copyright (year) Beijing Volcano Engine Technology Ltd.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class JMApi {
    private static final BitSet URLENCODER = new BitSet(256);
    private static final String CONST_ENCODE = "0123456789ABCDEF";
    public static final Charset UTF_8 = StandardCharsets.UTF_8;
    private final String region;
    private final String service;
    private final String schema;
    private final String host;
    private final String path;
    private final String ak;
    private final String sk;

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

    public static void main(String[] args) throws Exception {
        // 火山官网密钥信息, 注意sk结尾有==
        String AccessKeyID = "";
        String SecretAccessKey = "";
        // 请求域名
        String endpoint = "visual.volcengineapi.com";
        String path = "/"; // 路径，不包含 Query// 请求接口信息
        String service = "cv";
        String region = "cn-north-1";
        String schema = "https";
        JMApi JMApi = new JMApi(region, service, schema, endpoint, path, AccessKeyID, SecretAccessKey);
        // 参考接口文档Query参数
        String action = "CVProcess";
        String version = "2022-08-31";
        Date date = new Date();
        // 参考接口文档Body参数
        JSONObject req = new JSONObject();
        req.put("req_key", "jimeng_high_aes_general_v21_L");
        ArrayList<String> imageUrls = new ArrayList<String>();
//        imageUrls.add("******");
        req.put("image_urls", imageUrls);
        req.put("return_url", true);
        req.put("use_sr", true);
        req.put("width",768);
        req.put("height", 768);
        req.put("prompt", "采用细腻的半厚涂画风，展现一位气质阴郁的男子形象。他的浅金色中长发显得有些散乱，在俯视且近距离的视角构图中，画面聚焦于他的面部，对其面部细节精心描绘，尤其是那浓密纤长的睫毛十分吸睛，给人一种神秘的感觉。瞳孔是浅蓝色");
        JMApi.doRequest("POST", new HashMap(), req.toJSONString().getBytes(), date, action, version);
    }

    public JMApi(String region, String service, String schema, String host, String path, String ak, String sk) {
        this.region = region;
        this.service = service;
        this.host = host;
        this.schema = schema;
        this.path = path;
        this.ak = ak;
        this.sk = sk;
    }

    public void doRequest(String method, Map<String, String> queryList, byte[] body,
                          Date date, String action, String version) throws Exception {
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

        System.out.println(responseCode);
        System.out.println(responseBody);
        // 在响应处理部分添加
        JSONObject responseJson = JSONObject.parseObject(responseBody);
        if (responseJson.getInteger("code") == 10000) {
            JSONObject data = responseJson.getJSONObject("data");
            String imageUrl = data.getJSONArray("image_urls").getString(0);

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
        }
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
