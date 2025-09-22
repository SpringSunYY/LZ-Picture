<template>
  <div class="points-statistics">
    <MapCharts/>
    <LineAvgCharts/>
    <BarAxisRankingCharts chart-direction="right"/>
    <BarRankingCharts/>
    <LineZoomCharts/>
    <PieLineCharts/>
    <PieIntervalCharts/>
    <BarAutoCarouselCharts/>
  </div>
</template>
<script setup>

import MapCharts from "@/components/Statistics/MapCharts.vue";
import LineAvgCharts from "@/components/Statistics/LineAvgCharts.vue";
import BarAxisRankingCharts from "@/components/Statistics/BarAxisRankingCharts.vue";
import LineZoomCharts from "@/components/Statistics/LineZoomCharts.vue";
import PieLineCharts from "@/components/Statistics/PieLineCharts.vue";
import BarRankingCharts from "@/components/Statistics/BarRankingCharts.vue";
import PieIntervalCharts from "@/components/Statistics/PieIntervalCharts.vue";
import BarAutoCarouselCharts from "@/components/Statistics/BarAutoCarouselCharts.vue";
</script>
<style scoped lang="scss">
.points-statistics {
  width: 100%;
  height: 100%;
}
</style>
