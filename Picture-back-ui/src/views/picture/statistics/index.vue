<template>
  <PictureScreenBorder class="picture-statistics">
    <el-row gutter="20">
      <el-col :span="6">
        <div class="base-height">
          <KeywordCharts/>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="top-count">
          <p>总计：512312张图片</p>
        </div>
        <el-row :gutter="20">
          <el-col :span="12">
            <BorderBox8 class="top-center">
              <PieLineCharts/>
            </BorderBox8>
          </el-col>
          <el-col :span="12">
            <BorderBox8 :reverse="true" class="top-center">
              <PieLineCharts/>
            </BorderBox8>
          </el-col>
        </el-row>
      </el-col>
      <el-col :span="6">
        <div class="base-height">
          <KeywordCharts/>
        </div>
      </el-col>
    </el-row>
    <BorderBox7 class="center-height-1">
      <el-row :gutter="20" class="center-height-1">
        <el-col :span="4" :offset="2">
          <WaterMapRotateProportionCharts/>
        </el-col>
        <el-col :span="4">
          <WaterMapProportionCharts/>
        </el-col>
        <el-col :span="4">
          <WaterMapProportionCharts/>
        </el-col>
        <el-col :span="4">
          <WaterMapProportionCharts/>
        </el-col>
        <el-col :span="4">
          <WaterMapRotateProportionCharts/>
        </el-col>
      </el-row>
    </BorderBox7>
    <el-row :gutter="20" class="center-height-2">
      <el-col :span="8" class="center-height-2">
        <TableRanking/>
      </el-col>
      <el-col :span="8">
        <PieIntervalCharts/>
      </el-col>
      <el-col :span="8" class="center-height-2">
        <TableRanking/>
      </el-col>
    </el-row>
    <Decoration2 style="width:100%; height:5px;"/>
    <el-row :gutter="20" class="bottom-height">
      <el-col :span="7">
        <LineAvgCharts/>
      </el-col>
      <el-col :span="10">
        <LineZoomCharts/>
      </el-col>
      <el-col :span="7">
        <BarAvgCharts/>
      </el-col>
    </el-row>
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
import {BorderBox7, BorderBox8, Decoration2} from '@kjgl77/datav-vue3'
import TableRanking from "@/components/Statistics/TableRanking.vue";
import LineZoomCharts from "@/components/Statistics/LineZoomCharts.vue";
</script>
<style scoped lang="scss">
.picture-statistics {
  min-height: 100vh;
  max-width: 100%;

  .base-height {
    height: 35vh;
  }

  .top-count {
    margin-top: 8vh;
    height: 8vh;
    font-size: 3vh;
    text-align: center;
    color: white;
    font-weight: bold;
  }

  .top-center {
    height: 17vh;
  }

  .center-height-1 {
    margin: 0 auto;
    height: 15vh;
  }

  .center-height-2 {
    margin: 2px auto;
    height: 18vh;
  }

  .bottom-height {
    margin: 0 auto;
    height: 30vh;
  }
}
</style>
