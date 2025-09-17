<template>
  <div class="picture-statistics">
    <div style="height: 300px">
      <KeywordCharts/>
    </div>
    <div style="height: 300px">
      <LineAvgStatistics/>
    </div>
    <div style="height: 300px">
      <BarAvgStatistics/>
    </div>
    <div style="height: 300px">
      <WaterMapRotateProportionStatistics/>
    </div>
    <div style="height: 300px">
      <WaterMapProportionStatistics/>
    </div>
  </div>
</template>
<script setup lang="ts">
import KeywordCharts from "@/components/Statistics/KeywordCharts.vue";
import LineAvgStatistics from "@/components/Statistics/LineAvgStatistics.vue";
import BarAvgStatistics from "@/components/Statistics/BarAvgStatistics.vue";
import WaterMapRotateProportionStatistics from "@/components/Statistics/WaterMapRotateProportionStatistics.vue";
import WaterMapProportionStatistics from "@/components/Statistics/WaterMapProportionStatistics.vue";
</script>
<style scoped lang="scss">
.picture-statistics {
  //background-color: black;
}
</style>
