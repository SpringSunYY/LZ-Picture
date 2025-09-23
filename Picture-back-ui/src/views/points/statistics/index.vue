<template>
  <BorderBox11 class="points-statistics">
    <el-row :gutter="1">
      <el-col :span="6">
        <div class="base-charts" style="padding: 3vh">
          <!--          <RadarCharts :chart-data="pointsUsageTypeDate" :chart-name="pointsUsageTypeName"/>-->
          <PieGradientCharts :chart-data="pointsUsageTypeDate" :chart-name="pointsUsageTypeName"/>
        </div>
        <Decoration10 style="width:90%; height:5px;"/>
        <div class="base-charts">
          <BarRankingCharts :chart-data="pointsOrderRankDate" :chart-name="pointsOrderRankName"/>
        </div>
        <Decoration2 style="width:100%; height:5px;"/>
        <div class="bottom-charts">
          <LineAvgCharts :chart-data="pointsUsageDate" :chart-name="pointsUsageName" :chart-title="pointsUsageTitle"/>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="map-charts">
          <MapCharts/>
        </div>
        <BorderBox10 class="bottom-center-charts">
          <LineZoomCharts/>
        </BorderBox10>
      </el-col>
      <el-col :span="6">
        <div class="base-charts">
          <BarAutoCarouselCharts/>
        </div>
        <BorderBox13 class="base-charts">
          <BarAxisRankingCharts chart-direction="right"/>
        </BorderBox13>
        <div class="bottom-charts" style="padding: 3vh">
          <PieLineCharts/>
        </div>
      </el-col>
    </el-row>

    <DateRangePicker
        @change="onDateChange"
        top="3%"
        left="20%"
    />
  </BorderBox11>
</template>
<script setup>

import MapCharts from "@/components/Statistics/MapCharts.vue";
import LineAvgCharts from "@/components/Statistics/LineAvgCharts.vue";
import BarAxisRankingCharts from "@/components/Statistics/BarAxisRankingCharts.vue";
import LineZoomCharts from "@/components/Statistics/LineZoomCharts.vue";
import PieLineCharts from "@/components/Statistics/PieLineCharts.vue";
import BarRankingCharts from "@/components/Statistics/BarRankingCharts.vue";
import BarAutoCarouselCharts from "@/components/Statistics/BarAutoCarouselCharts.vue";
import {BorderBox10, BorderBox11, BorderBox13, Decoration10, Decoration2} from "@kjgl77/datav-vue3";
import DateRangePicker from "@/components/Statistics/DateRangePicker.vue";
import dayjs from "dayjs";
import {ref} from "vue";
import {
  pointsOrderRankStatistics,
  pointsUsageStatistics,
  pointsUsageTypeStatistics
} from "@/api/points/statisticsInfo.js";
import PieGradientCharts from "@/components/Statistics/PieGradientCharts.vue";

const defaultStart = dayjs().subtract(14, "day").format("YYYY-MM-DD")
const defaultEnd = dayjs().format("YYYY-MM-DD")
const query = ref({
  startDate: defaultStart,
  endDate: defaultEnd
})
const onDateChange = (val) => {
  query.value.startDate = val?.[0] || ''
  query.value.endDate = val?.[1] || ''
  getStatistics()
}

//积分使用类型
const pointsUsageTypeDate = ref([])
const pointsUsageTypeName = ref('积分使用')
const getPointsUsageType = () => {
  pointsUsageTypeStatistics(query.value).then(res => {
    pointsUsageTypeDate.value = res.data
  })
}

//用户充值排行
const pointsOrderRankDate = ref([])
const pointsOrderRankName = ref('用户充值排行')
const getPointsOrderRank = () => {
  pointsOrderRankStatistics(query.value).then(res => {
    pointsOrderRankDate.value = res.data
  })
}

//积分使用统计
const pointsUsageName = ref('积分使用')
const pointsUsageDate = ref([])
const pointsUsageTitle= ref(['积分总量', '平均值'])
const getPointsUsage = () => {
  pointsUsageStatistics(query.value).then(res => {
    pointsUsageDate.value = res.data
  })
}
const getStatistics = async () => {
  getPointsUsage()
  getPointsUsageType()
  getPointsOrderRank()
}
getStatistics()
</script>
<style scoped lang="scss">
.points-statistics {
  width: 100%;
  height: 100%;
  padding: 1vh;
  background-image: url("/src/assets/images/points-statistics-bg.png");
  background-size: cover;
  background-position: center;
  min-height: 100vh;
  max-width: 100%;

  .base-charts {
    height: 33.5vh;
  }

  .map-charts {
    height: 67vh;
  }

  .bottom-center-charts {
    height: 31vh;
  }

  .bottom-charts {
    height: 30vh;
  }
}
</style>
