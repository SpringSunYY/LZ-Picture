<template>
  <BorderBox11 class="points-statistics">
    <el-row :gutter="1">
      <el-col :span="6">
        <div class="base-charts" style="padding: 3vh">
          <RadarCharts/>
        </div>
        <Decoration10 style="width:90%; height:5px;"/>
        <div class="base-charts">
          <BarRankingCharts/>
        </div>
        <Decoration2 style="width:100%; height:5px;"/>
        <div class="bottom-charts">
          <LineAvgCharts/>
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
import RadarCharts from "@/components/Statistics/RadarCharts.vue";
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
