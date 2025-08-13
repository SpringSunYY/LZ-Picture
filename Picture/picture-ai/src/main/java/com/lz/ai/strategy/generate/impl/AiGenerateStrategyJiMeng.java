package com.lz.ai.strategy.generate.impl;

import com.alibaba.fastjson2.JSONObject;
import com.lz.ai.manage.AiAsyncManager;
import com.lz.ai.manage.factory.AiFileLogAsyncFactory;
import com.lz.ai.mapper.GenerateLogInfoMapper;
import com.lz.ai.model.domain.GenerateLogInfo;
import com.lz.ai.model.domain.ModelParamsInfo;
import com.lz.ai.model.enums.AiLogStatusEnum;
import com.lz.ai.model.enums.AiModelParamsTypeEnum;
import com.lz.ai.strategy.generate.AiGenerateStrategyConfig;
import com.lz.ai.strategy.generate.domain.dto.GenerateLogInfoDto;
import com.lz.ai.strategy.generate.domain.dto.JiMengResponse;
import com.lz.ai.strategy.generate.domain.params.JiMengParams;
import com.lz.ai.strategy.generate.domain.verify.JiMengVerify;
import com.lz.common.core.domain.DeviceInfo;
import com.lz.common.enums.CommonDeleteEnum;
import com.lz.common.enums.CommonHasStatisticsEnum;
import com.lz.common.manager.file.PictureUploadManager;
import com.lz.common.manager.file.model.FileResponse;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.bean.BeanUtils;
import com.lz.common.utils.uuid.IdUtils;
import com.lz.points.model.enums.PoPointsUsageLogTypeEnum;
import com.lz.points.model.enums.PoPointsUsageTypeEnum;
import com.lz.points.service.IPointsUsageLogInfoService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
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

    @Resource
    private IPointsUsageLogInfoService pointsUsageLogInfoService;

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

    //region 生成
    @Override
    public List<GenerateLogInfo> userGenerate(GenerateLogInfoDto info) {
        JiMengParams params = verify(info);
        //拼接参数
        String json = JSONObject.toJSONString(params);
        ArrayList<GenerateLogInfo> generateLogInfos = new ArrayList<>();
        List<Future<GenerateLogInfo>> futures = new ArrayList<>();
        for (int i = 0; i < info.getNumbers(); i++) {
            Future<GenerateLogInfo> future = threadPoolTaskExecutor.submit(() -> {
                return getGenerateLogInfo(info, params, json);
            });
            futures.add(future);
        }
        futures.forEach(future -> {
            try {
                GenerateLogInfo generateLogInfo = future.get();
                System.out.println("generateLogInfo = " + generateLogInfo);
                generateLogInfos.add(generateLogInfo);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        //处理结果
        return generateLogInfos;
    }

    private GenerateLogInfo getGenerateLogInfo(GenerateLogInfoDto info, JiMengParams params, String json) {
        long startTime = System.currentTimeMillis();
        JiMengResponse jiMengResponse = null;
        info.setWidth(params.getWidth());
        info.setHeight(params.getHeight());
        try {
            jiMengResponse = doRequest(params.getMethod(), new HashMap<>(), json.getBytes(UTF_8), new Date(),
                    params.getAction(), params.getVersion(), params.getRegion(), params.getService(),
                    params.getPath(), info.getApiKey(), info.getSecretKey(), params.getHost(), params.getSchema());
            long totalTime = System.currentTimeMillis() - startTime;
            GenerateLogInfo generateLogInfo = processResult(jiMengResponse, info, params, totalTime);
            generateLogInfoMapper.insert(generateLogInfo);
            return generateLogInfo;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private GenerateLogInfo processResult(JiMengResponse jiMengResponse, GenerateLogInfoDto infoDto, JiMengParams params, long totalTime) {
        GenerateLogInfo generateLogInfo = GenerateLogInfo(jiMengResponse, infoDto, params, totalTime);

        if (jiMengResponse.getCode() == 10000) {
            generateLogInfo.setLogStatus(AiLogStatusEnum.LOG_STATUS_1.getValue());
            JiMengResponse.DataContent data = jiMengResponse.getData();
            if (StringUtils.isNull(data)) {
                generateLogInfo.setLogStatus(AiLogStatusEnum.LOG_STATUS_2.getValue());
            }
            //如果是直接返回URL，则直接保存图片
            if (StringUtils.isNotNull(data.getImage_urls())) {
                saveGenerateLogInfoByImg(infoDto, data, generateLogInfo);
            } else if (StringUtils.isNotEmpty(data.getTask_id())) {
                saveGenerateLogInfoByTask(infoDto, data, generateLogInfo);
            }
            //执行成功直接扣除
            //添加文件日志
            DeviceInfo deviceInfo = new DeviceInfo();
            deviceInfo.setDeviceId(infoDto.getDeviceId());
            deviceInfo.setIpAddr(infoDto.getIpAddr());
            deviceInfo.setBrowser(infoDto.getBrowser());
            deviceInfo.setOs(infoDto.getOs());
            deviceInfo.setPlatform(infoDto.getPlatform());
            //扣除积分
            pointsUsageLogInfoService.updateAccountByPointsRechargeInfo(infoDto.getUserId(),
                    null,
                    PoPointsUsageLogTypeEnum.POINTS_USAGE_LOG_TYPE_1.getValue(),
                    PoPointsUsageTypeEnum.POINTS_USAGE_TYPE_2.getValue(),
                    generateLogInfo.getLogId(),
                    -generateLogInfo.getPointsUsed(),
                    deviceInfo);

        } else {
            generateLogInfo.setLogStatus(AiLogStatusEnum.LOG_STATUS_2.getValue());
        }
        return generateLogInfo;
    }

    /**
     * 根据任务保存图片
     *
     * @param info
     * @param data
     * @param generateLogInfo
     * @return void
     * @author: YY
     * @method: saveGenerateLogInfoByTask
     * @date: 2025/8/12 18:49
     **/
    private void saveGenerateLogInfoByTask(GenerateLogInfoDto info, JiMengResponse.DataContent data, GenerateLogInfo generateLogInfo) {
        generateLogInfo.setLogStatus(AiLogStatusEnum.LOG_STATUS_0.getValue());
    }

    /**
     * 根据图片保存图片
     *
     * @param infoDto
     * @param data
     * @param generateLogInfo
     * @return void
     * @author: YY
     * @method: saveGenerateLogInfoByImg
     * @date: 2025/8/12 18:49
     **/
    private void saveGenerateLogInfoByImg(GenerateLogInfoDto infoDto, JiMengResponse.DataContent data, GenerateLogInfo generateLogInfo) {
        List<String> imageUrls = data.getImage_urls();
        for (String imageUrl : imageUrls) {
            System.out.println("imageUrl = " + imageUrl);
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
                FileResponse fileResponse = pictureUploadManager.uploadUrlByAiGenerate(imageUrl, "generate", infoDto.getUsername());
                generateLogInfo.setWidth(Math.toIntExact(fileResponse.getPicWidth()));
                generateLogInfo.setHeight(Math.toIntExact(fileResponse.getPicHeight()));
                generateLogInfo.setFileUrls(fileResponse.getUrl() + COMMON_SEPARATOR + fileResponse.getThumbnailUrl());
                System.out.println("fileResponse = " + fileResponse);
                //添加文件日志
                DeviceInfo deviceInfo = new DeviceInfo();
                deviceInfo.setDeviceId(infoDto.getDeviceId());
                deviceInfo.setIpAddr(infoDto.getIpAddr());
                deviceInfo.setBrowser(infoDto.getBrowser());
                deviceInfo.setOs(infoDto.getOs());
                deviceInfo.setPlatform(infoDto.getPlatform());
                AiAsyncManager.me().execute(AiFileLogAsyncFactory.recordFileLog(fileResponse, infoDto.getUserId(), generateLogInfo.getTaskId(), generateLogInfo.getLogId(), deviceInfo));
            } catch (Exception e) {
                log.error("图片保存失败: " + e.getMessage());
            }
        }
    }

    /**
     * 设置日志信息
     *
     * @param jiMengResponse 响应
     * @param info           请求
     * @param params         请求参数
     * @param totalTime      总耗时
     * @return GenerateLogInfo
     * @author: YY
     * @method: GenerateLogInfo 获取日志
     * @date: 2025/8/11 16:16
     **/
    private GenerateLogInfo GenerateLogInfo(JiMengResponse jiMengResponse, GenerateLogInfoDto info, JiMengParams params, long totalTime) {
        GenerateLogInfo generateLogInfo = new GenerateLogInfo();
        generateLogInfo.setLogId(IdUtils.fastSimpleUUID());
        generateLogInfo.setUserId(info.getUserId());
        generateLogInfo.setModelKey(info.getModelKey());
        generateLogInfo.setModelType(info.getModelType());
        if (StringUtils.isNotEmpty(info.getInputFile()) && info.getInputFile().contains("http")) {
            generateLogInfo.setInputFile(info.getInputFile());
        } else {
            //如果是图片，不保存图片信息
            params.setBinary_data_base64(null);
            generateLogInfo.setInputParams(JSONObject.toJSONString(params));
        }
        generateLogInfo.setPrompt(info.getPrompt());
        generateLogInfo.setNegativePrompt(info.getNegativePrompt());
        generateLogInfo.setSeed(info.getSeed());
        generateLogInfo.setNumbers(info.getNumbers());
        generateLogInfo.setIpAddr(info.getIpAddr());
        generateLogInfo.setDeviceId(info.getDeviceId());
        generateLogInfo.setBrowser(info.getBrowser());
        generateLogInfo.setOs(info.getOs());
        generateLogInfo.setPlatform(info.getPlatform());
        if (StringUtils.isNotNull(jiMengResponse.getData()) && StringUtils.isNotEmpty(jiMengResponse.getData().getTask_id())) {
            generateLogInfo.setTaskId(jiMengResponse.getData().getTask_id());
        } else if (StringUtils.isNotNull(jiMengResponse.getRequest_id())) {
            generateLogInfo.setTaskId(jiMengResponse.getRequest_id());
        }
        generateLogInfo.setOutputResult(JSONObject.toJSONString(jiMengResponse));
        generateLogInfo.setWidth(info.getWidth());
        generateLogInfo.setHeight(info.getHeight());
        Date requestTime = new Date();
        generateLogInfo.setRequestTime(requestTime);
        generateLogInfo.setRequestDuration(totalTime);
        generateLogInfo.setPriceUsed(info.getPriceUse().multiply(BigDecimal.valueOf(info.getNumbers())));
        generateLogInfo.setPointsUsed(info.getPointsNeed() * info.getNumbers());
        generateLogInfo.setTargetId(info.getTargetId());
        generateLogInfo.setLogStatus(AiLogStatusEnum.LOG_STATUS_2.getValue());
        generateLogInfo.setAiStatusCode(String.valueOf(jiMengResponse.getCode()));
        generateLogInfo.setFailReason(jiMengResponse.getMessage());
        generateLogInfo.setHasStatistics(CommonHasStatisticsEnum.HAS_STATISTICS_0.getValue());
        generateLogInfo.setCreateTime(requestTime);
        generateLogInfo.setUpdateTime(requestTime);
        generateLogInfo.setIsDelete(CommonDeleteEnum.NORMAL.getValue());
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
        //如果是图生图
        if (info.getModelType().equals(AiModelParamsTypeEnum.MODEL_PARAMS_TYPE_2.getValue())) {
            //如果输入文件为空
            if (StringUtils.isEmpty(info.getInputFile())) {
                return jiMengParams;
            }
            //判断文件是url还是base64
            if (info.getInputFile().startsWith("http")) {
                jiMengParams.setImage_urls(new String[]{info.getInputFile()});
            } else if (info.getInputFile().startsWith("data:image")) {
                //base64 - 移除前缀，只保留base64编码部分
                //base64 - 移除前缀，只保留base64编码部分
                String base64Data = info.getInputFile().substring(info.getInputFile().indexOf(",") + 1);
                // 清理base64数据，移除可能的换行符和空格
                base64Data = base64Data.replaceAll("\\s+", "");
                jiMengParams.setBinary_data_base64(new String[]{base64Data});
                System.out.println("base64Data = " + base64Data.substring(0, 100));
            } else {
                return jiMengParams;
            }
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
        System.out.println("querySB = " + querySB);
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
        System.out.println("responseBody = " + responseBody);
        is.close();
        return JSONObject.parseObject(responseBody, JiMengResponse.class);
    }

    //endregion
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

    //region 查询
    public GenerateLogInfo query(GenerateLogInfo generateLogInfo, ModelParamsInfo modelParamsInfo, String username) {
//        System.out.println("generateLogInfo = " + generateLogInfo);
//        System.out.println("modelParamsInfo = " + modelParamsInfo);
        JiMengParams jiMengParams = new JiMengParams();
        jiMengParams = jiMengParams.jsonToObj(modelParamsInfo.getModelParams());
        jiMengParams.setTask_id(generateLogInfo.getTaskId());
        JiMengParams.ReqJson reqJson = new JiMengParams.ReqJson();
        reqJson.setReturn_url(true);
        String reqJsonStr = JSONObject.toJSONString(reqJson);
        jiMengParams.setReq_json(reqJsonStr);
        jiMengParams.setAction(jiMengParams.getQueryAction());
        jiMengParams.setWidth(null);
        jiMengParams.setHeight(null);
        jiMengParams.setPrompt(null);
        jiMengParams.setUse_pre_llm(null);
        String jsonString = JSONObject.toJSONString(jiMengParams);
        System.out.println("jsonString = " + jsonString);
        try {

            JiMengResponse jiMengResponse = doRequest(jiMengParams.getMethod(), new HashMap<>(), jsonString.getBytes(UTF_8), new Date(),
                    jiMengParams.getAction(), jiMengParams.getVersion(), jiMengParams.getRegion(), jiMengParams.getService(), jiMengParams.getPath(),
                    modelParamsInfo.getApiKey(), modelParamsInfo.getSecretKey(), jiMengParams.getHost(), jiMengParams.getSchema());
            System.out.println("jiMengResponse = " + jiMengResponse);
            if (jiMengResponse.getCode() == 10000) {
                JiMengResponse.DataContent data = jiMengResponse.getData();
                if (StringUtils.isNull(data)) {
                    generateLogInfo.setLogStatus(AiLogStatusEnum.LOG_STATUS_2.getValue());
                }
                //如果是直接返回URL，则直接保存图片
                GenerateLogInfoDto info = new GenerateLogInfoDto();
                info.setUsername(username);
                info.setUserId(generateLogInfo.getUserId());
                BeanUtils.copyProperties(generateLogInfo, info);
                if (StringUtils.isNotNull(data.getImage_urls())) {
                    saveGenerateLogInfoByImg(info, data, generateLogInfo);
                    generateLogInfo.setLogStatus(AiLogStatusEnum.LOG_STATUS_1.getValue());
                } else if (StringUtils.isNotEmpty(data.getTask_id())) {
                    saveGenerateLogInfoByTask(info, data, generateLogInfo);
                }
                //重新计算花费时间
                String timeElapsed = jiMengResponse.getTime_elapsed();
                if (StringUtils.isNotEmpty(timeElapsed)) {
                    String time = timeElapsed.split("\\.")[0];
                    generateLogInfo.setRequestDuration(Long.parseLong(time));
                }
                generateLogInfo.setOutputResult(JSONObject.toJSONString(jiMengResponse));
            } else {
                generateLogInfo.setLogStatus(AiLogStatusEnum.LOG_STATUS_2.getValue());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return generateLogInfo;
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
        // 参考接口文档Query参数
        String action = "CVProcess";
        String version = "2022-08-31";
        Date date = new Date();
        // 参考接口文档Body参数
        JSONObject body = new JSONObject();
        body.put("req_key", "jimeng_t2i_v30");
        body.put("task_id", "13583292287246105885");
        body.put("return_url", true);
        AiGenerateStrategyJiMeng aiGenerateStrategyJiMeng = new AiGenerateStrategyJiMeng();
        aiGenerateStrategyJiMeng.doRequest("POST", new HashMap<>(), body.toJSONString().getBytes(StandardCharsets.UTF_8), date, action, version, region, service, path, AccessKeyID, SecretAccessKey, endpoint, schema);
    }
    //endregion
}
