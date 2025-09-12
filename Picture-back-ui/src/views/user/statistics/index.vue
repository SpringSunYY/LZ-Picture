<template>
  <div class="user-statistics">
    <el-row :gutter="1">
      <el-col :span="6">
        <BorderBox4 :color="border4Color" class="default-border" title="每日登录">
          <!--每日注册-->
          <LineZoomCharts height="100%" :chart-name="userRegisterStatisticsName"
                          :chart-data="userRegisterStatisticsData"/>
        </BorderBox4>
        <BorderBox6 class="default-border">
          <!--消息排行-->
          <BarRankingCharts :chart-name="userInformTypeStatisticsName" :chart-data="userInformTypeStatisticsData"/>
        </BorderBox6>
        <div class="default-border">
          <!--消息信息-->
          <TableRanking/>
        </div>
      </el-col>

      <el-col :span="12">
        <!-- 标题和装饰 -->
        <div class="title-wrapper">
          <p class="title-text">用户数据监控统计</p>
          <Decoration5 class="title-decoration"/>
        </div>

        <div class="map-border">
          <MapCharts height="100%"/>
        </div>
        <BorderBox7 class="dashboard-border">
          <!--用户信息-->
          <el-row>
            <el-col :span="12" class="dashboard-border">
              <DashboardRotateProportionCharts/>
            </el-col>

            <el-col :span="12" class="dashboard-border">
              <DashboardRotateTotalCharts/>
            </el-col>
          </el-row>
        </BorderBox7>
      </el-col>

      <el-col :span="6">
        <BorderBox4 :reverse="true" :color="border4Color" class="default-border">
          <!--年龄分布-->
          <RadarCharts :chart-data="userAgeStatisticsData" :chart-name="userAgeStatisticsName"/>
        </BorderBox4>
        <BorderBox8 class="default-border">
          <!--性别分布-->
          <PieCharts :chart-name="userSexStatisticsName" :chart-data="userSexStatisticsData"/>
        </BorderBox8>
        <!--登录-->
        <div class="default-border">
          <BarLineCharts :chart-data="userLoginStatisticsData" :chart-name="userLoginStatisticsName"/>
        </div>
      </el-col>
    </el-row>
    <DateRangePicker
        @change="onDateChange"
        top="6vh"
        right="45vh"
    />
  </div>
</template>

<script setup>
import {BorderBox4, BorderBox6, BorderBox7, BorderBox8, Decoration5} from '@kjgl77/datav-vue3'
import PieCharts from "@/components/Statistics/PieCharts";
import BarRankingCharts from "@/components/Statistics/BarRankingCharts";
import LineZoomCharts from "@/components/Statistics/LineZoomCharts.vue";
import RadarCharts from "@/components/Statistics/RadarCharts.vue";
import MapCharts from "@/components/Statistics/MapCharts.vue";
import TableRanking from "@/components/Statistics/TableRanking.vue";
import BarLineCharts from "@/components/Statistics/BarLineCharts.vue";
import DashboardRotateProportionCharts from "@/components/Statistics/DashboardRotateProportionCharts.vue";
import DashboardRotateTotalCharts from "@/components/Statistics/DashboardRotateTotalCharts.vue";
import DateRangePicker from "@/components/Statistics/DateRangePicker";
import {ref} from "vue"
import {
  userAgeStatistics, userInformTypeStatistics, userLoginStatistics,
  userRegisterStatistics, userSexStatistics
} from "@/api/user/uStatisticsInfo";
import dayjs from "dayjs";

const border4Color = ['#545fac', '#545fac']
const defaultStart = dayjs().subtract(14, "day").format("YYYY-MM-DD")
const defaultEnd = dayjs().format("YYYY-MM-DD")
const query = ref({
  startDate: defaultStart,
  endDate: defaultEnd
})
// 用户注册
const userRegisterStatisticsData = ref({})
const userRegisterStatisticsName = ref('用户注册统计')
const onDateChange = (val) => {
  query.value.startDate = val?.[0] || ''
  query.value.endDate = val?.[1] || ''
  getStatistics()
}

const getUserRegisterStatistics = () => {
  userRegisterStatistics(query.value).then(res => {
    userRegisterStatisticsData.value.chartXData = res.data.names
    userRegisterStatisticsData.value.chartYData = []
    userRegisterStatisticsData.value.chartYData.push({value: res.data.totals, name: userRegisterStatisticsName.value})
    console.log(userRegisterStatisticsData.value)
  })
}

//用户登录
const userLoginStatisticsData = ref({})
const userLoginStatisticsName = ref('用户登录统计')
const getUserLoginStatistics = () => {
  userLoginStatistics(query.value).then(res => {
    userLoginStatisticsData.value = res.data
  })
}
//用户信息
const userInformTypeStatisticsData = ref({})
const userInformTypeStatisticsName = ref('消息发送统计')
const getUserInformTypeStatistics = () => {
  userInformTypeStatistics(query.value).then(res => {
    userInformTypeStatisticsData.value = res.data
  })
}
// 用户性别
const userSexStatisticsData = ref({})
const userSexStatisticsName = ref('用户性别比例')
const getUserSexStatistics = () => {
  userSexStatistics().then(res => {
    userSexStatisticsData.value = res.data.datas
  })
}
getUserSexStatistics()
// 用户年龄
const userAgeStatisticsName = ref('用户年龄比例')
const userAgeStatisticsData = ref({})
const getUserAgeStatistics = () => {
  userAgeStatistics().then(res => {
    userAgeStatisticsData.value = res.data
  })
}
getUserAgeStatistics()
const getStatistics = () => {
  getUserRegisterStatistics()
  getUserLoginStatistics()
  getUserInformTypeStatistics()
}
getStatistics()
</script>

<style scoped lang="scss">
.user-statistics {
  background-image: url("/src/assets/images/user-statistics-bg.png");
  min-height: 100vh;
  max-width: 100%;

  .default-border {
    margin-top: 0.5vh;
    height: 32.5vh;
  }

  .map-border {
    height: 65vh;
  }

  .dashboard-border {
    height: 28vh;
  }

  .title-wrapper {
    position: relative;
    width: 100%;
    height: 7vh;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .title-text {
    font-size: 4vh;
    color: white;
    text-align: center;
    margin: 0;
    z-index: 2; // 保证文字在装饰上面
  }

  .title-decoration {
    position: absolute;
    bottom: 0; // 装饰条紧贴容器底部
    left: 0;
    top: 2vh;
    width: 100%;
    height: 7vh;
    z-index: 1;
  }
}
</style>
