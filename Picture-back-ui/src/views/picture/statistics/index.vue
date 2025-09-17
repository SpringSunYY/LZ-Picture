<template>
  <div class="picture-statistics">
    <div style="height: 300px">
      <KeywordCharts/>
    </div>
    <div style="height: 300px">
      <LineAvgCharts/>
    </div>
    <div style="height: 300px">
      <BarAvgCharts/>
    </div>
    <div style="height: 300px">
      <WaterMapRotateProportionCharts/>
    </div>
    <div style="height: 300px">
      <WaterMapProportionCharts/>
    </div>
    <div style="height: 300px">
      <PieIntervalCharts/>
    </div>
    <div style="height: 300px">
      <PieLineCharts/>
    </div>
  </div>
</template>
<script setup lang="ts">
import KeywordCharts from "@/components/Statistics/KeywordCharts.vue";
import LineAvgCharts from "@/components/Statistics/LineAvgCharts.vue";
import BarAvgCharts from "@/components/Statistics/BarAvgCharts.vue";
import WaterMapRotateProportionCharts from "@/components/Statistics/WaterMapRotateProportionCharts.vue";
import WaterMapProportionCharts from "@/components/Statistics/WaterMapProportionCharts.vue";
import PieIntervalCharts from "@/components/Statistics/PieIntervalCharts.vue";
import PieLineCharts from "@/components/Statistics/PieLineCharts.vue";
</script>
<style scoped lang="scss">
.picture-statistics {
  //background-color: black;
}
</style>
