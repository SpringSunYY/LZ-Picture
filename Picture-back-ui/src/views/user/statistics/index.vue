<template>
  <div>
    <PieCharts/>
    <BarRankingCharts/>
    <LineZoomCharts/>
    <AgeRadarCharts/>
    <MapCharts />
  </div>
</template>
<script setup>

import PieCharts from "@/components/Echarts/PieCharts";
import BarRankingCharts from "@/components/Echarts/BarRankingCharts";
import LineZoomCharts from "@/components/Echarts/LineZoomCharts.vue";
import AgeRadarCharts from "@/components/Echarts/AgeRadarCharts.vue";
import MapCharts from "@/components/Echarts/MapCharts.vue";
</script>
<style scoped lang="scss">

</style>
