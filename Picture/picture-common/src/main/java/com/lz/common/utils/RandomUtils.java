package com.lz.common.utils;

import java.time.LocalDate;
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
        // 确保年份范围有效
        if (startYear > endYear) {
            throw new IllegalArgumentException("开始年份不能大于结束年份");
        }

        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();

        // 如果不允许超过当前时间，且结束年份大于当前年份，则将结束年份设为当前年份
        if (!allowFuture && endYear > currentYear) {
            endYear = currentYear;
            // 如果开始年份也大于当前年份，则将开始年份设为当前年份
            if (startYear > currentYear) {
                startYear = currentYear;
            }
        }

        // 随机生成年份
        int randomYear = startYear + (int) (Math.random() * (endYear - startYear + 1));

        // 如果不允许超过当前时间，且随机年份为当前年份，则月份不能超过当前月份
        int maxMonth = 12;
        if (!allowFuture && randomYear == currentYear) {
            maxMonth = currentDate.getMonthValue();
        }

        // 随机生成月份 (1-maxMonth)
        int randomMonth = 1 + (int) (Math.random() * maxMonth);

        // 生成该月份的随机日期
        LocalDate tempDate = LocalDate.of(randomYear, randomMonth, 1);

        // 如果不允许超过当前时间，且随机年份为当前年份，随机月份也为当前月份，则日期不能超过当前日期
        int maxDay = tempDate.lengthOfMonth();
        if (!allowFuture && randomYear == currentYear && randomMonth == currentDate.getMonthValue()) {
            maxDay = currentDate.getDayOfMonth();
        }

        int randomDay = 1 + (int) (Math.random() * maxDay);

        // 创建随机日期
        LocalDate randomDate = LocalDate.of(randomYear, randomMonth, randomDay);

        // 转换为Date类型并返回
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
     * 随机生成全国范围内的完整IP属地（省份+地级行政区）
     *
     * @return 格式为"省份 地级行政区"的字符串（如：广东省 东莞市、贵州省 黔东南苗族侗族自治州）
     */
    public static String generateRandomIPAddress() {
        Map<String, List<String>> provinceCityMap = new HashMap<>();
        initProvinceCityMap(provinceCityMap);
        // 1. 随机选择省份（所有省级行政区等概率）
        List<String> provinces = new ArrayList<>(provinceCityMap.keySet());
        Random random = new Random();
        String randomProvince = provinces.get(random.nextInt(provinces.size()));

        // 2. 随机选择该省份的地级行政区（按实际数量加权）
        List<String> cities = provinceCityMap.get(randomProvince);
        String randomCity = cities.get(random.nextInt(cities.size()));

        // 3. 拼接结果
        return randomProvince + " " + randomCity;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            System.out.println("i%3 = " + i % 3);
        }
    }

    private static void initProvinceCityMap(Map<String, List<String>> provinceCityMap) {
        // ======================== 直辖市（4个） ========================
        provinceCityMap.put("北京市", Arrays.asList(
                "东城区", "西城区", "朝阳区", "丰台区", "石景山区", "海淀区", "门头沟区",
                "房山区", "通州区", "顺义区", "昌平区", "大兴区", "怀柔区", "平谷区",
                "密云区", "延庆区"
        ));

        provinceCityMap.put("天津市", Arrays.asList(
                "和平区", "河东区", "河西区", "南开区", "河北区", "红桥区", "东丽区",
                "西青区", "津南区", "北辰区", "武清区", "宝坻区", "宁河区", "静海区",
                "蓟州区", "滨海新区"
        ));

        provinceCityMap.put("上海市", Arrays.asList(
                "黄浦区", "徐汇区", "长宁区", "静安区", "普陀区", "虹口区", "杨浦区",
                "闵行区", "宝山区", "嘉定区", "浦东新区", "金山区", "松江区", "青浦区",
                "奉贤区", "崇明区"
        ));

        provinceCityMap.put("重庆市", Arrays.asList(
                "万州区", "涪陵区", "渝中区", "大渡口区", "江北区", "沙坪坝区", "九龙坡区",
                "南岸区", "北碚区", "綦江区", "大足区", "渝北区", "巴南区", "黔江区",
                "长寿区", "江津区", "合川区", "永川区", "南川区", "璧山区", "铜梁区",
                "潼南区", "荣昌区", "开州区", "梁平区", "武隆区"
        ));

        // ======================== 自治区（5个） ========================
        provinceCityMap.put("内蒙古自治区", Arrays.asList(
                "呼和浩特市", "包头市", "乌海市", "赤峰市", "通辽市", "鄂尔多斯市",
                "呼伦贝尔市", "巴彦淖尔市", "乌兰察布市", "兴安盟", "锡林郭勒盟",
                "阿拉善盟"
        ));

        provinceCityMap.put("广西壮族自治区", Arrays.asList(
                "南宁市", "柳州市", "桂林市", "梧州市", "北海市", "防城港市", "钦州市",
                "贵港市", "玉林市", "百色市", "贺州市", "河池市", "来宾市", "崇左市"
        ));

        provinceCityMap.put("西藏自治区", Arrays.asList(
                "拉萨市", "日喀则市", "昌都市", "林芝市", "山南市", "那曲市", "阿里地区"
        ));

        provinceCityMap.put("宁夏回族自治区", Arrays.asList(
                "银川市", "石嘴山市", "吴忠市", "固原市", "中卫市"
        ));

        provinceCityMap.put("新疆维吾尔自治区", Arrays.asList(
                "乌鲁木齐市", "克拉玛依市", "吐鲁番市", "哈密市", "阿克苏地区",
                "喀什地区", "和田地区", "昌吉回族自治州", "博尔塔拉蒙古自治州",
                "巴音郭楞蒙古自治州", "克孜勒苏柯尔克孜自治州", "伊犁哈萨克自治州",
                "塔城地区", "阿勒泰地区"
        ));

        // ======================== 省份（23个） ========================
        provinceCityMap.put("河北省", Arrays.asList(
                "石家庄市", "唐山市", "秦皇岛市", "邯郸市", "邢台市", "保定市",
                "张家口市", "承德市", "沧州市", "廊坊市", "衡水市"
        ));

        provinceCityMap.put("山西省", Arrays.asList(
                "太原市", "大同市", "阳泉市", "长治市", "晋城市", "朔州市",
                "晋中市", "运城市", "忻州市", "临汾市", "吕梁市"
        ));

        provinceCityMap.put("辽宁省", Arrays.asList(
                "沈阳市", "大连市", "鞍山市", "抚顺市", "本溪市", "丹东市",
                "锦州市", "营口市", "阜新市", "辽阳市", "盘锦市", "铁岭市",
                "朝阳市", "葫芦岛市"
        ));

        provinceCityMap.put("吉林省", Arrays.asList(
                "长春市", "吉林市", "四平市", "辽源市", "通化市", "白山市",
                "松原市", "白城市", "延边朝鲜族自治州"
        ));

        provinceCityMap.put("黑龙江省", Arrays.asList(
                "哈尔滨市", "齐齐哈尔市", "鸡西市", "鹤岗市", "双鸭山市", "大庆市",
                "伊春市", "佳木斯市", "七台河市", "牡丹江市", "黑河市", "绥化市",
                "大兴安岭地区"
        ));

        provinceCityMap.put("江苏省", Arrays.asList(
                "南京市", "无锡市", "徐州市", "常州市", "苏州市", "南通市",
                "连云港市", "淮安市", "盐城市", "扬州市", "镇江市", "泰州市",
                "宿迁市"
        ));

        provinceCityMap.put("浙江省", Arrays.asList(
                "杭州市", "宁波市", "温州市", "嘉兴市", "湖州市", "绍兴市",
                "金华市", "衢州市", "舟山市", "台州市", "丽水市"
        ));

        provinceCityMap.put("安徽省", Arrays.asList(
                "合肥市", "芜湖市", "蚌埠市", "淮南市", "马鞍山市", "淮北市",
                "铜陵市", "安庆市", "黄山市", "滁州市", "阜阳市", "宿州市",
                "六安市", "亳州市", "池州市", "宣城市"
        ));

        provinceCityMap.put("福建省", Arrays.asList(
                "福州市", "厦门市", "莆田市", "三明市", "泉州市", "漳州市",
                "南平市", "龙岩市", "宁德市"
        ));

        provinceCityMap.put("江西省", Arrays.asList(
                "南昌市", "景德镇市", "萍乡市", "九江市", "新余市", "鹰潭市",
                "赣州市", "吉安市", "宜春市", "抚州市", "上饶市"
        ));

        provinceCityMap.put("山东省", Arrays.asList(
                "济南市", "青岛市", "淄博市", "枣庄市", "东营市", "烟台市",
                "潍坊市", "济宁市", "泰安市", "威海市", "日照市", "临沂市",
                "德州市", "聊城市", "滨州市", "菏泽市"
        ));

        provinceCityMap.put("河南省", Arrays.asList(
                "郑州市", "开封市", "洛阳市", "平顶山市", "安阳市", "鹤壁市",
                "新乡市", "焦作市", "濮阳市", "许昌市", "漯河市", "三门峡市",
                "南阳市", "商丘市", "信阳市", "周口市", "驻马店市"
        ));

        provinceCityMap.put("湖北省", Arrays.asList(
                "武汉市", "黄石市", "十堰市", "宜昌市", "襄阳市", "鄂州市",
                "荆门市", "孝感市", "荆州市", "黄冈市", "咸宁市", "随州市",
                "恩施土家族苗族自治州", "仙桃市", "潜江市", "天门市", "神农架林区"
        ));

        provinceCityMap.put("湖南省", Arrays.asList(
                "长沙市", "株洲市", "湘潭市", "衡阳市", "邵阳市", "岳阳市",
                "常德市", "张家界市", "益阳市", "郴州市", "永州市", "怀化市",
                "娄底市", "湘西土家族苗族自治州"
        ));

        provinceCityMap.put("广东省", Arrays.asList(
                "广州市", "深圳市", "珠海市", "汕头市", "佛山市", "韶关市",
                "湛江市", "肇庆市", "江门市", "茂名市", "惠州市", "梅州市",
                "汕尾市", "河源市", "阳江市", "清远市", "东莞市", "中山市",
                "潮州市", "揭阳市", "云浮市"
        ));

        provinceCityMap.put("海南省", Arrays.asList(
                "海口市", "三亚市", "三沙市", "儋州市", "五指山市", "琼海市",
                "文昌市", "万宁市", "东方市", "定安县", "屯昌县", "澄迈县",
                "临高县", "白沙黎族自治县", "昌江黎族自治县", "乐东黎族自治县",
                "陵水黎族自治县", "保亭黎族苗族自治县", "琼中黎族苗族自治县"
        ));

        provinceCityMap.put("四川省", Arrays.asList(
                "成都市", "自贡市", "攀枝花市", "泸州市", "德阳市", "绵阳市",
                "广元市", "遂宁市", "内江市", "乐山市", "南充市", "眉山市",
                "宜宾市", "广安市", "达州市", "雅安市", "巴中市", "资阳市",
                "阿坝藏族羌族自治州", "甘孜藏族自治州", "凉山彝族自治州"
        ));

        provinceCityMap.put("贵州省", Arrays.asList(
                "贵阳市", "六盘水市", "遵义市", "安顺市", "毕节市", "铜仁市",
                "黔西南布依族苗族自治州", "黔东南苗族侗族自治州", "黔南布依族苗族自治州"
        ));

        provinceCityMap.put("云南省", Arrays.asList(
                "昆明市", "曲靖市", "玉溪市", "保山市", "昭通市", "丽江市",
                "普洱市", "临沧市", "楚雄彝族自治州", "红河哈尼族彝族自治州",
                "文山壮族苗族自治州", "西双版纳傣族自治州", "大理白族自治州",
                "德宏傣族景颇族自治州", "怒江傈僳族自治州", "迪庆藏族自治州"
        ));

        provinceCityMap.put("陕西省", Arrays.asList(
                "西安市", "铜川市", "宝鸡市", "咸阳市", "渭南市", "延安市",
                "汉中市", "榆林市", "安康市", "商洛市"
        ));

        provinceCityMap.put("甘肃省", Arrays.asList(
                "兰州市", "嘉峪关市", "金昌市", "白银市", "天水市", "武威市",
                "张掖市", "平凉市", "酒泉市", "庆阳市", "定西市", "陇南市",
                "临夏回族自治州", "甘南藏族自治州"
        ));

        provinceCityMap.put("青海省", Arrays.asList(
                "西宁市", "海东市", "海北藏族自治州", "黄南藏族自治州",
                "海南藏族自治州", "果洛藏族自治州", "玉树藏族自治州",
                "海西蒙古族藏族自治州"
        ));

        provinceCityMap.put("台湾省", Arrays.asList(
                "台北市", "新北市", "桃园市", "台中市", "台南市", "高雄市",
                "基隆市", "新竹市", "嘉义市"
        ));

        // ======================== 特别行政区（2个） ========================
        provinceCityMap.put("香港特别行政区", Arrays.asList(
                "中西区", "湾仔区", "东区", "南区", "油尖旺区", "深水埗区",
                "九龙城区", "黄大仙区", "观塘区", "北区", "大埔区", "沙田区",
                "西贡区", "荃湾区", "屯门区", "元朗区"
        ));

        provinceCityMap.put("澳门特别行政区", Arrays.asList(
                "花地玛堂区", "圣安多尼堂区", "大堂区", "望德堂区", "风顺堂区",
                "嘉模堂区", "路氹填海区"
        ));
    }

}
