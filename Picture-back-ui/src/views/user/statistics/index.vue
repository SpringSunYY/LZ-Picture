<template>
  <div>
    <BorderBox1>
      <PieCharts/>
    </BorderBox1>
    <BarRankingCharts/>
    <LineZoomCharts/>
    <AgeRadarCharts/>
    <MapCharts/>
    <TableRanking/>
    <BarLineCharts/>
  </div>
</template>
<script setup>
import {BorderBox1} from '@kjgl77/datav-vue3'
import PieCharts from "@/components/Statistics/PieCharts";
import BarRankingCharts from "@/components/Statistics/BarRankingCharts";
import LineZoomCharts from "@/components/Statistics/LineZoomCharts.vue";
import AgeRadarCharts from "@/components/Statistics/AgeRadarCharts.vue";
import MapCharts from "@/components/Statistics/MapCharts.vue";
import TableRanking from "@/components/Statistics/TableRanking.vue";
import BarLineCharts from "@/components/Statistics/BarLineCharts.vue";
</script>
<style scoped lang="scss">

</style>
