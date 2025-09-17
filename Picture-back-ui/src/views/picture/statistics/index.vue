<template>
  <PictureScreenBorder class="picture-statistics">
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
  </PictureScreenBorder>
</template>
<script setup lang="ts">
import KeywordCharts from "@/components/Statistics/KeywordCharts.vue";
import LineAvgCharts from "@/components/Statistics/LineAvgCharts.vue";
import BarAvgCharts from "@/components/Statistics/BarAvgCharts.vue";
import WaterMapRotateProportionCharts from "@/components/Statistics/WaterMapRotateProportionCharts.vue";
import WaterMapProportionCharts from "@/components/Statistics/WaterMapProportionCharts.vue";
import PieIntervalCharts from "@/components/Statistics/PieIntervalCharts.vue";
import PieLineCharts from "@/components/Statistics/PieLineCharts.vue";
import PictureScreenBorder from "@/components/Border/PictureScreenBorder.vue";
</script>
<style scoped lang="scss">
.picture-statistics {
  min-height: 100vh;
  max-width: 100%;
}
</style>
