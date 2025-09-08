<template>
  <div>
    <PieCharts/>
    <BarRankingCharts/>
    <LineZoomCharts/>
    <AgeRadarCharts/>
    <MapCharts />
    <TableRanking/>
  </div>
</template>
<script setup>

import PieCharts from "@/components/Statistics/PieCharts";
import BarRankingCharts from "@/components/Statistics/BarRankingCharts";
import LineZoomCharts from "@/components/Statistics/LineZoomCharts.vue";
import AgeRadarCharts from "@/components/Statistics/AgeRadarCharts.vue";
import MapCharts from "@/components/Statistics/MapCharts.vue";
import TableRanking from "@/components/Statistics/TableRanking.vue";
</script>
<style scoped lang="scss">

</style>
