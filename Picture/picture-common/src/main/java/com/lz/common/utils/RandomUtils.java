package com.lz.common.utils;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * 随机生成器
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-09-14  16:03
 * @Version: 1.0
 */
public class RandomUtils {
    private static final SecureRandom secureRandom = new SecureRandom();

    /**
     * 生成中国手机号码
     * <p>
     * 中国手机号码规则:
     * 130-139: 中国联通
     * 145, 147: 中国联通
     * 150-159: 中国移动和中国联通
     * 170, 176: 虚拟运营商和中国联通
     * 180-189: 中国电信
     * 190-199: 中国广电和中国电信
     *
     * @return 有效的中国手机号码
     */
    public static String generateChinesePhoneNumber() {
        String[] prefixes = {
                "130", "131", "132", "133", "134", "135", "136", "137", "138", "139",
                "145", "147",
                "150", "151", "152", "153", "155", "156", "157", "158", "159",
                "170", "176", "177", "178",
                "180", "181", "182", "183", "184", "185", "186", "187", "188", "189",
                "190", "191", "192", "193", "195", "196", "197", "198", "199"
        };

        Random random = new Random();
        String prefix = prefixes[random.nextInt(prefixes.length)];
        StringBuilder phoneNumber = new StringBuilder(prefix);

        // 生成剩余的8位数字
        for (int i = 0; i < 8; i++) {
            phoneNumber.append(random.nextInt(10));
        }

        return phoneNumber.toString();
    }

    /**
     * 生成指定国家的手机号码
     *
     * @param countryCode 国家代码，如"CN"代表中国
     * @return 有效的手机号码
     */
    public static String generatePhoneNumber(String countryCode) {
        if (countryCode.equalsIgnoreCase("CN")) {
            return generateChinesePhoneNumber();
        }// 默认生成中国手机号码
        return generateChinesePhoneNumber();
    }

    /**
     * 生成随机有效的手机号码（默认为中国）
     *
     * @return 有效的手机号码
     */
    public static String generatePhoneNumber() {
        return generateChinesePhoneNumber();
    }

    // ==================== 时间分布生成器 ====================

    /**
     * 生成符合增长趋势的日期序列。
     * 序号越大，生成的日期整体越晚，模拟注册量自然增长的现象。
     *
     * @param startYear          起始年份
     * @param endYear            结束年份
     * @param index              当前序号（从0开始）
     * @param total              总序号数
     * @param growthRatePerBatch 增长幂次（值越大前期越稀疏、后期越密集）
     * @param allowFuture        是否允许超过当前时间，false 则上限为 now（默认 false）
     * @return 符合增长趋势的日期
     */
    public static Date generateGrowthTrendDate(int startYear, int endYear, int index, int total, double growthRatePerBatch, boolean allowFuture) {
        LocalDate start = LocalDate.of(startYear, 1, 1);
        LocalDate endBase = LocalDate.of(endYear, 12, 31);
        LocalDate nowDate = LocalDate.now();

        LocalDate end = allowFuture ? endBase : (endBase.isAfter(nowDate) ? nowDate : endBase);
        long totalDays = ChronoUnit.DAYS.between(start, end);
        if (totalDays <= 0) {
            return Date.from(start.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }

        double progress = total > 1 ? (double) index / (total - 1) : 0.0;
        double biased = Math.pow(progress, growthRatePerBatch);

        // 基础日期 = 增长曲线映射到 [0, totalDays-1]
        long baseDay = (long) (biased * (totalDays - 1));

        // ±2% 对称浮动
        long maxOffset = Math.max(1, (long) (totalDays * 0.02));
        long dayOffset = -maxOffset + secureRandom.nextInt((int) (maxOffset * 2 + 1));
        long targetDay = Math.max(0, Math.min(totalDays - 1, baseDay + dayOffset));

        LocalDate targetDate = start.plusDays(targetDay);
        int hour = secureRandom.nextInt(24);
        int minute = secureRandom.nextInt(60);
        int second = secureRandom.nextInt(60);

        return Date.from(targetDate.atTime(hour, minute, second).atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 生成符合增长趋势的日期序列。
     * 序号越大，生成的日期整体越晚，模拟注册量自然增长的现象。
     * 不允许超过当前时间。
     *
     * @param startYear          起始年份
     * @param endYear            结束年份
     * @param index              当前序号（从0开始）
     * @param total              总序号数
     * @param growthRatePerBatch 增长幂次（值越大前期越稀疏、后期越密集）
     * @return 符合增长趋势的日期
     */
    public static Date generateGrowthTrendDate(int startYear, int endYear, int index, int total, double growthRatePerBatch) {
        return generateGrowthTrendDate(startYear, endYear, index, total, growthRatePerBatch, false);
    }


    /**
     * 生成符合日活跃周期分布的时间，模拟用户日常登录行为。
     * 晚间（19-23点）活跃度最高，凌晨最低，周末整体偏高。
     *
     * @param baseDate    参考基准日期（用于限定年份范围，可为 null）
     * @param allowFuture 是否允许超过当前时间，false 则上限为 now（默认 false）
     * @return 符合周期性分布的时间
     */
    public static Date generatePeriodicLoginTime(Date baseDate, boolean allowFuture) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        if (baseDate != null) {
            cal.setTime(baseDate);
        }
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
        cal.set(java.util.Calendar.MINUTE, 0);
        cal.set(java.util.Calendar.SECOND, 0);
        cal.set(java.util.Calendar.MILLISECOND, 0);
        cal.add(java.util.Calendar.DAY_OF_MONTH, secureRandom.nextInt(365));

        int dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK);
        boolean isWeekend = (dayOfWeek == java.util.Calendar.SATURDAY || dayOfWeek == java.util.Calendar.SUNDAY);

        int[] hourWeights = {
                1, 1, 1, 1, 2, 3, 5,   // 0-6: 深夜到凌晨权重极低
                8, 12, 15, 18,         // 7-10: 上午逐渐活跃
                16, 14, 12, 10,        // 11-14: 中午高峰后回落
                8, 10, 12, 14,         // 15-18: 下午到傍晚
                18, 22, 20, 15, 8, 4   // 19-23: 晚间黄金时段最高
        };

        if (isWeekend) {
            for (int i = 9; i < hourWeights.length; i++) {
                hourWeights[i] = (int) Math.ceil(hourWeights[i] * 1.2);
            }
        }

        int totalWeight = 0;
        for (int w : hourWeights) {
            totalWeight += w;
        }
        int randomWeight = secureRandom.nextInt(totalWeight);
        int cumulativeWeight = 0;
        int hour = 12;
        for (int i = 0; i < hourWeights.length; i++) {
            cumulativeWeight += hourWeights[i];
            if (randomWeight < cumulativeWeight) {
                hour = i;
                break;
            }
        }
        cal.set(java.util.Calendar.HOUR_OF_DAY, hour);
        cal.set(java.util.Calendar.MINUTE, secureRandom.nextInt(60));
        cal.set(java.util.Calendar.SECOND, secureRandom.nextInt(60));

        Date result = cal.getTime();

        if (!allowFuture && result.after(new Date())) {
            java.util.Calendar nowCal = java.util.Calendar.getInstance();
            nowCal.set(java.util.Calendar.HOUR_OF_DAY, 0);
            nowCal.set(java.util.Calendar.MINUTE, 0);
            nowCal.set(java.util.Calendar.SECOND, 0);
            nowCal.set(java.util.Calendar.MILLISECOND, 0);
            // 回到今天零点附近，在最近 30 天内均匀分布
            long maxBackMs = 30L * 24 * 3600 * 1000;
            long offsetMs = secureRandom.nextLong(maxBackMs + 1);
            nowCal.add(java.util.Calendar.MILLISECOND, -(int) offsetMs);
            nowCal.set(java.util.Calendar.HOUR_OF_DAY, hour);
            nowCal.set(java.util.Calendar.MINUTE, secureRandom.nextInt(60));
            nowCal.set(java.util.Calendar.SECOND, secureRandom.nextInt(60));
            result = nowCal.getTime();
        }

        return result;
    }

    /**
     * 生成符合日活跃周期分布的时间，模拟用户日常登录行为。
     * 不允许超过当前时间。
     */
    public static Date generatePeriodicLoginTime(Date baseDate) {
        return generatePeriodicLoginTime(baseDate, false);
    }

    /**
     * 生成充值金额，符合小额高频、大额低频的真实充值规律。
     * 分布：6元(50%)、30元(25%)、68元(15%)、128元(7%)、328元(2.5%)、648元(0.5%)
     *
     * @return 符合真实充值分布的金额
     */
    public static BigDecimal generateRechargeAmount() {
        double rand = secureRandom.nextDouble() * 100;
        if (rand < 50.0) {
            return new BigDecimal("6.00");
        } else if (rand < 75.0) {
            return new BigDecimal("30.00");
        } else if (rand < 90.0) {
            return new BigDecimal("68.00");
        } else if (rand < 97.0) {
            return new BigDecimal("128.00");
        } else if (rand < 99.5) {
            return new BigDecimal("328.00");
        } else {
            return new BigDecimal("648.00");
        }
    }

    /**
     * 生成在基准时间之后的随机时间。
     *
     * @param baseTime         基准时间（必须不为 null）
     * @param minOffsetMinutes 最小偏移（分钟）
     * @param maxOffsetMinutes 最大偏移（分钟）
     * @param allowFuture      是否允许超过当前时间，false 则上限为 now（默认 false）
     * @return 基准时间之后的随机时间
     */
    public static Date generateDateAfter(Date baseTime, int minOffsetMinutes, int maxOffsetMinutes, boolean allowFuture) {
        Calendar nowCal = Calendar.getInstance();
        Date now = nowCal.getTime();

        int maxOffset = maxOffsetMinutes;
        if (!allowFuture) {
            long diffMs = now.getTime() - baseTime.getTime();
            if (diffMs < 0) {
                return now;
            }
            int diffMinutes = (int) (diffMs / 60000);
            if (maxOffsetMinutes > diffMinutes) {
                maxOffset = diffMinutes;
            }
        }
        if (maxOffset < minOffsetMinutes) {
            return baseTime;
        }

        double baseOffset = (minOffsetMinutes + maxOffset) / 2.0;
        double floatRange = baseOffset * 0.02;
        double offsetMinutes = baseOffset + (-floatRange + secureRandom.nextDouble() * floatRange * 2);
        offsetMinutes = Math.max(minOffsetMinutes, Math.min(maxOffsetMinutes, offsetMinutes));

        Calendar cal = Calendar.getInstance();
        cal.setTime(baseTime);
        cal.add(Calendar.MINUTE, (int) Math.round(offsetMinutes));
        return cal.getTime();
    }

    /**
     * 生成在基准时间之后的随机时间。默认不允许超过当前时间。
     */
    public static Date generateDateAfter(Date baseTime, int minOffsetMinutes, int maxOffsetMinutes) {
        return generateDateAfter(baseTime, minOffsetMinutes, maxOffsetMinutes, true);
    }

    /**
     * 生成在基准时间之后的随机时间（秒级精度）。
     *
     * @param baseTime         基准时间（必须不为 null）
     * @param minOffsetSeconds 最小偏移（秒）
     * @param maxOffsetSeconds 最大偏移（秒）
     * @param allowFuture      是否允许超过当前时间，false 则上限为 now（默认 false）
     * @return 基准时间之后的随机时间
     */
    public static Date generateDateAfterSeconds(Date baseTime, int minOffsetSeconds, int maxOffsetSeconds, boolean allowFuture) {
        Date now = new Date();

        int maxOffset = maxOffsetSeconds;
        if (!allowFuture) {
            long diffMs = now.getTime() - baseTime.getTime();
            if (diffMs < 0) {
                return now;
            }
            int diffSeconds = (int) (diffMs / 1000);
            if (maxOffsetSeconds > diffSeconds) {
                maxOffset = diffSeconds;
            }
        }
        if (maxOffset < minOffsetSeconds) {
            return baseTime;
        }

        double baseOffset = (minOffsetSeconds + maxOffsetSeconds) / 2.0;
        double floatRange = baseOffset * 0.02;
        double offsetSeconds = baseOffset + (-floatRange + secureRandom.nextDouble() * floatRange * 2);
        offsetSeconds = Math.max(minOffsetSeconds, Math.min(maxOffsetSeconds, offsetSeconds));

        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(baseTime);
        cal.add(java.util.Calendar.SECOND, (int) Math.round(offsetSeconds));
        return cal.getTime();
    }

    /**
     * 生成在基准时间之后的随机时间（秒级精度）。默认不允许超过当前时间。
     */
    public static Date generateDateAfterSeconds(Date baseTime, int minOffsetSeconds, int maxOffsetSeconds) {
        return generateDateAfterSeconds(baseTime, minOffsetSeconds, maxOffsetSeconds, false);
    }

    /**
     * 按小额高频、大额低频的真实充值规律，从价格列表中随机选择一个价格。
     * <p>
     * 权重计算方式：weight = 1 / √price，即价格越低的套餐权重越高、被选中的概率越大，
     * 价格越高的套餐权重越低、被选中的概率越小，完全契合"小额高频、大额低频"的真实充值分布。
     * 若价格列表为空则返回 null。
     *
     * @param prices 系统中全部可用充值套餐的价格列表（按套餐下标一一对应）
     * @return 按加权概率选中的价格，未找到有效价格时返回 null
     */
    public static BigDecimal selectPriceByDistribution(List<BigDecimal> prices) {
        if (prices == null || prices.isEmpty()) {
            return null;
        }

        // 过滤掉 null 和 <= 0 的价格
        List<BigDecimal> validPrices = new ArrayList<>();
        for (BigDecimal price : prices) {
            if (price != null && price.compareTo(BigDecimal.ZERO) > 0) {
                validPrices.add(price);
            }
        }
        if (validPrices.isEmpty()) {
            return null;
        }

        // 计算权重：权重 = 1 / √price，价格越高权重越小
        double[] weights = new double[validPrices.size()];
        double totalWeight = 0.0;
        for (int i = 0; i < validPrices.size(); i++) {
            double w = 1.0 / Math.sqrt(validPrices.get(i).doubleValue());
            weights[i] = w;
            totalWeight += w;
        }
        if (totalWeight <= 0) {
            return validPrices.get(secureRandom.nextInt(validPrices.size()));
        }

        // 归一化后加权随机抽取
        double rand = secureRandom.nextDouble();
        double cumulative = 0.0;
        for (int i = 0; i < weights.length; i++) {
            cumulative += weights[i] / totalWeight;
            if (rand <= cumulative) {
                return validPrices.get(i);
            }
        }
        return validPrices.getLast();
    }

    /**
     * 根据开始结束年份生成指定时间范围
     *
     * @param startYear 开始年份
     * @param endYear   结束年份
     * @return
     */
    public static Date generateDate(Integer startYear, Integer endYear) {
        return generateDate(startYear, endYear, false);
    }

    /**
     * 根据开始结束年份生成指定时间范围
     *
     * @param startYear   开始年份
     * @param endYear     结束年份
     * @param allowFuture 是否允许超过当前时间
     * @return
     */
    public static Date generateDate(Integer startYear, Integer endYear, boolean allowFuture) {
        if (startYear > endYear) {
            throw new IllegalArgumentException("开始年份不能大于结束年份");
        }

        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();

        if (!allowFuture && endYear > currentYear) {
            endYear = currentYear;
            if (startYear > currentYear) {
                startYear = currentYear;
            }
        }

        int yearRange = endYear - startYear + 1;
        int randomYear = startYear + secureRandom.nextInt(yearRange);

        int maxMonth = 12;
        if (!allowFuture && randomYear == currentYear) {
            maxMonth = currentDate.getMonthValue();
        }
        int randomMonth = 1 + secureRandom.nextInt(maxMonth);

        LocalDate monthStart = LocalDate.of(randomYear, randomMonth, 1);
        int maxDay = monthStart.lengthOfMonth();

        if (!allowFuture && randomYear == currentYear && randomMonth == currentDate.getMonthValue()) {
            maxDay = currentDate.getDayOfMonth();
        }

        int randomDay = 1 + secureRandom.nextInt(maxDay);

        LocalDate randomDate = LocalDate.of(randomYear, randomMonth, randomDay);
        return DateUtils.toDate(randomDate);
    }


    /**
     * 生成随机IPv4地址
     *
     * @return 随机IPv4地址字符串
     */
    public static String generateRandomIpAddr() {
        Random random = new Random();

        // 生成4个0-255之间的数字
        int[] ipParts = new int[4];
        for (int i = 0; i < 4; i++) {
            ipParts[i] = random.nextInt(256);
        }

        // 确保不是保留的特殊IP地址
        // 不能是 0.x.x.x (本网络)
        if (ipParts[0] == 0) {
            ipParts[0] = random.nextInt(255) + 1; // 改为1-255
        }

        // 不能是 127.x.x.x (回环地址)
        if (ipParts[0] == 127) {
            ipParts[0] = random.nextInt(255);
            if (ipParts[0] >= 127) {
                ipParts[0] += 1; // 跳过127
            }
        }

        // 不能是 255.255.255.255 (广播地址)
        if (ipParts[0] == 255 && ipParts[1] == 255 && ipParts[2] == 255 && ipParts[3] == 255) {
            ipParts[3] = random.nextInt(255); // 修改最后一个数字
        }

        return String.format("%d.%d.%d.%d", ipParts[0], ipParts[1], ipParts[2], ipParts[3]);
    }

    /**
     * 随机生成全国范围内的完整IP属地（省份+地级行政区）。
     * 按省份在 LinkedHashMap 中的 put 顺序（越靠前权重越高）和城市在列表中的顺序
     * （越靠前权重越高）进行倒序加权，模拟经济发达地区用户量更大的真实情况。
     *
     * @return 格式为"省份 地级行政区"的字符串（如：广东省 东莞市、贵州省 黔东南苗族侗族自治州）
     */
    public static String generateRandomIPAddress() {
        LinkedHashMap<String, List<String>> provinceCityMap = new LinkedHashMap<>();
        initProvinceCityMap(provinceCityMap);

        // 省份倒序加权（按 LinkedHashMap 插入顺序，越靠前权重越高）
        List<String> provinces = new ArrayList<>(provinceCityMap.keySet());
        int provinceCount = provinces.size();
        int totalProvinceWeight = (provinceCount * (provinceCount + 1)) / 2;
        int provinceRand = secureRandom.nextInt(totalProvinceWeight);
        int provinceCumulative = 0;
        String randomProvince = provinces.get(provinceCount - 1);
        for (int i = 0; i < provinceCount; i++) {
            provinceCumulative += (provinceCount - i);
            if (provinceRand < provinceCumulative) {
                randomProvince = provinces.get(i);
                break;
            }
        }

        // 城市倒序加权（按列表顺序，越靠前权重越高）
        List<String> cities = provinceCityMap.get(randomProvince);
        int cityCount = cities.size();
        int totalCityWeight = (cityCount * (cityCount + 1)) / 2;
        int cityRand = secureRandom.nextInt(totalCityWeight);
        int cityCumulative = 0;
        String randomCity = cities.get(cityCount - 1);
        for (int i = 0; i < cityCount; i++) {
            cityCumulative += (cityCount - i);
            if (cityRand < cityCumulative) {
                randomCity = cities.get(i);
                break;
            }
        }

        return randomProvince + " " + randomCity;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            System.out.println("i%3 = " + i % 3);
        }
    }

    private static void initProvinceCityMap(LinkedHashMap<String, List<String>> provinceCityMap) {
        // ======================== 按put顺序权重从高到低 ========================

        provinceCityMap.put("广东省", Arrays.asList(
                "深圳市", "广州市", "东莞市", "佛山市", "珠海市", "中山市",
                "惠州市", "湛江市", "江门市", "肇庆市", "汕头市", "韶关市",
                "茂名市", "梅州市", "汕尾市", "河源市", "阳江市", "清远市",
                "潮州市", "揭阳市", "云浮市"
        ));

        provinceCityMap.put("贵州省", Arrays.asList(
                "贵阳市", "遵义市", "毕节市", "黔南布依族苗族自治州", "黔东南苗族侗族自治州",
                "六盘水市", "铜仁市", "黔西南布依族苗族自治州", "安顺市"
        ));

        provinceCityMap.put("江苏省", Arrays.asList(
                "苏州市", "南京市", "无锡市", "常州市", "南通市", "徐州市",
                "盐城市", "扬州市", "泰州市", "镇江市", "淮安市", "连云港市",
                "宿迁市"
        ));

        provinceCityMap.put("山东省", Arrays.asList(
                "青岛市", "济南市", "烟台市", "潍坊市", "临沂市", "济宁市",
                "淄博市", "威海市", "德州市", "东营市", "泰安市", "枣庄市",
                "日照市", "滨州市", "聊城市", "菏泽市"
        ));

        provinceCityMap.put("浙江省", Arrays.asList(
                "杭州市", "宁波市", "温州市", "嘉兴市", "绍兴市", "金华市",
                "台州市", "湖州市", "丽水市", "衢州市", "舟山市"
        ));

        provinceCityMap.put("河南省", Arrays.asList(
                "郑州市", "洛阳市", "南阳市", "新乡市", "许昌市", "焦作市",
                "开封市", "周口市", "商丘市", "信阳市", "驻马店市", "平顶山市",
                "三门峡市", "漯河市", "濮阳市", "鹤壁市", "安阳市"
        ));

        provinceCityMap.put("四川省", Arrays.asList(
                "成都市", "绵阳市", "宜宾市", "泸州市", "南充市", "达州市",
                "乐山市", "德阳市", "内江市", "自贡市", "遂宁市", "眉山市",
                "广安市", "资阳市", "雅安市", "广元市", "巴中市",
                "甘孜藏族自治州", "阿坝藏族羌族自治州", "凉山彝族自治州"
        ));

        provinceCityMap.put("湖北省", Arrays.asList(
                "武汉市", "襄阳市", "宜昌市", "荆州市", "黄冈市", "孝感市",
                "十堰市", "咸宁市", "荆门市", "鄂州市", "随州市",
                "恩施土家族苗族自治州", "仙桃市", "潜江市", "天门市", "神农架林区"
        ));

        provinceCityMap.put("福建省", Arrays.asList(
                "福州市", "厦门市", "泉州市", "漳州市", "莆田市", "宁德市",
                "三明市", "南平市", "龙岩市"
        ));

        provinceCityMap.put("湖南省", Arrays.asList(
                "长沙市", "岳阳市", "常德市", "衡阳市", "株洲市", "湘潭市",
                "邵阳市", "郴州市", "永州市", "怀化市", "娄底市", "益阳市",
                "张家界市", "湘西土家族苗族自治州"
        ));

        provinceCityMap.put("上海市", Arrays.asList(
                "浦东新区", "黄浦区", "徐汇区", "长宁区", "静安区", "普陀区",
                "虹口区", "杨浦区", "闵行区", "宝山区", "嘉定区", "松江区",
                "青浦区", "奉贤区", "金山区", "崇明区"
        ));

        provinceCityMap.put("安徽省", Arrays.asList(
                "合肥市", "芜湖市", "蚌埠市", "马鞍山市", "阜阳市", "安庆市",
                "滁州市", "宿州市", "六安市", "宣城市", "淮南市", "淮北市",
                "铜陵市", "池州市", "亳州市", "黄山市"
        ));

        provinceCityMap.put("北京市", Arrays.asList(
                "朝阳区", "海淀区", "西城区", "东城区", "丰台区", "石景山区",
                "通州区", "顺义区", "大兴区", "昌平区", "房山区", "门头沟区",
                "怀柔区", "平谷区", "密云区", "延庆区"
        ));

        provinceCityMap.put("河北省", Arrays.asList(
                "石家庄市", "唐山市", "保定市", "廊坊市", "沧州市", "邯郸市",
                "秦皇岛市", "邢台市", "张家口市", "承德市", "衡水市"
        ));

        provinceCityMap.put("陕西省", Arrays.asList(
                "西安市", "榆林市", "宝鸡市", "咸阳市", "渭南市", "延安市",
                "汉中市", "安康市", "商洛市", "铜川市"
        ));

        provinceCityMap.put("江西省", Arrays.asList(
                "南昌市", "赣州市", "九江市", "宜春市", "吉安市", "上饶市",
                "抚州市", "新余市", "景德镇市", "萍乡市", "鹰潭市"
        ));

        provinceCityMap.put("辽宁省", Arrays.asList(
                "沈阳市", "大连市", "鞍山市", "营口市", "锦州市", "盘锦市",
                "辽阳市", "丹东市", "阜新市", "朝阳市", "铁岭市", "抚顺市",
                "本溪市", "葫芦岛市"
        ));

        provinceCityMap.put("重庆市", Arrays.asList(
                "渝北区", "江北区", "沙坪坝区", "九龙坡区", "南岸区", "渝中区",
                "巴南区", "北碚区", "大渡口区", "涪陵区", "万州区", "璧山区",
                "永川区", "合川区", "江津区", "长寿区", "南川区", "綦江区",
                "铜梁区", "大足区", "荣昌区", "黔江区", "开州区", "梁平区",
                "武隆区", "潼南区"
        ));

        provinceCityMap.put("天津市", Arrays.asList(
                "滨海新区", "西青区", "津南区", "北辰区", "东丽区", "武清区",
                "宝坻区", "静海区", "蓟州区", "宁河区", "河西区", "南开区",
                "河北区", "河东区", "红桥区", "和平区"
        ));

        provinceCityMap.put("山西省", Arrays.asList(
                "太原市", "运城市", "临汾市", "长治市", "大同市", "晋城市",
                "晋中市", "吕梁市", "忻州市", "朔州市", "阳泉市"
        ));

        provinceCityMap.put("内蒙古自治区", Arrays.asList(
                "鄂尔多斯市", "呼和浩特市", "包头市", "赤峰市", "呼伦贝尔市",
                "通辽市", "巴彦淖尔市", "乌兰察布市", "兴安盟", "锡林郭勒盟",
                "阿拉善盟", "乌海市"
        ));

        provinceCityMap.put("黑龙江省", Arrays.asList(
                "哈尔滨市", "大庆市", "齐齐哈尔市", "牡丹江市", "佳木斯市",
                "绥化市", "鸡西市", "双鸭山市", "伊春市", "七台河市", "鹤岗市",
                "黑河市", "大兴安岭地区"
        ));

        provinceCityMap.put("吉林省", Arrays.asList(
                "长春市", "吉林市", "四平市", "松原市", "辽源市", "通化市",
                "白山市", "白城市", "延边朝鲜族自治州"
        ));

        provinceCityMap.put("广西壮族自治区", Arrays.asList(
                "南宁市", "柳州市", "桂林市", "玉林市", "北海市", "贵港市",
                "梧州市", "百色市", "钦州市", "河池市", "来宾市", "贺州市",
                "防城港市", "崇左市"
        ));

        provinceCityMap.put("云南省", Arrays.asList(
                "昆明市", "曲靖市", "玉溪市", "红河哈尼族彝族自治州", "大理白族自治州",
                "丽江市", "德宏傣族景颇族自治州", "西双版纳傣族自治州", "保山市",
                "昭通市", "普洱市", "临沧市", "楚雄彝族自治州", "文山壮族苗族自治州",
                "怒江傈僳族自治州", "迪庆藏族自治州"
        ));

        provinceCityMap.put("海南省", Arrays.asList(
                "海口市", "三亚市", "儋州市", "琼海市", "文昌市", "万宁市",
                "东方市", "五指山市", "澄迈县", "临高县", "定安县", "屯昌县",
                "陵水黎族自治县", "乐东黎族自治县", "保亭黎族苗族自治县",
                "琼中黎族苗族自治县", "昌江黎族自治县", "白沙黎族自治县", "三沙市"
        ));

        provinceCityMap.put("甘肃省", Arrays.asList(
                "兰州市", "天水市", "定西市", "陇南市", "白银市", "庆阳市",
                "平凉市", "酒泉市", "张掖市", "武威市", "临夏回族自治州",
                "甘南藏族自治州", "嘉峪关市", "金昌市"
        ));

        provinceCityMap.put("新疆维吾尔自治区", Arrays.asList(
                "乌鲁木齐市", "克拉玛依市", "昌吉回族自治州", "巴音郭楞蒙古自治州",
                "阿克苏地区", "喀什地区", "伊犁哈萨克自治州", "塔城地区",
                "阿勒泰地区", "博尔塔拉蒙古自治州", "哈密市", "和田地区",
                "吐鲁番市", "克孜勒苏柯尔克孜自治州"
        ));

        provinceCityMap.put("宁夏回族自治区", Arrays.asList(
                "银川市", "石嘴山市", "吴忠市", "固原市", "中卫市"
        ));

        provinceCityMap.put("青海省", Arrays.asList(
                "西宁市", "海东市", "海南藏族自治州", "海北藏族自治州", "海西蒙古族藏族自治州",
                "黄南藏族自治州", "果洛藏族自治州", "玉树藏���自治州"
        ));

        provinceCityMap.put("西藏自治区", Arrays.asList(
                "拉萨市", "日喀则市", "林芝市", "昌都市", "山南市", "那曲市", "阿里地区"
        ));

        provinceCityMap.put("台湾省", Arrays.asList(
                "新北市", "台北市", "桃园市", "台中市", "高雄市", "台南市",
                "基隆市", "新竹市", "嘉义市"
        ));

        provinceCityMap.put("香港特别行政区", Arrays.asList(
                "沙田区", "东区", "南区", "观塘区", "九龙城区", "黄大仙区",
                "油尖旺区", "深水埗区", "中西区", "湾仔区", "北区", "大埔区",
                "西贡区", "荃湾区", "屯门区", "元朗区"
        ));

        provinceCityMap.put("澳门特别行政区", Arrays.asList(
                "花地玛堂区", "圣安多尼堂区", "大堂区", "望德堂区", "风顺堂区",
                "嘉模堂区", "路氹填海区"
        ));
    }

}
