<template>
  <div>
    <PieCharts/>
    <BarRankingCharts/>
    <LineZoomCharts/>
    <AgeRadarCharts/>
  </div>
</template>
<script setup>

import PieCharts from "@/components/echarts/PieCharts";
import BarRankingCharts from "@/components/echarts/BarRankingCharts";
import LineZoomCharts from "@/components/echarts/LineZoomCharts.vue";
import AgeRadarCharts from "@/components/echarts/AgeRadarCharts.vue";
</script>
<style scoped lang="scss">

</style>
