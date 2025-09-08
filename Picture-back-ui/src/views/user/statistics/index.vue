<template>
  <div class="user-statistics">
    <el-row :gutter="6">
      <el-col :span="6">
        <BorderBox4 :color="border4Color" class="default-border" title="每日登录">
          <!--每日注册-->
          <LineZoomCharts height="350px" chart-name="每日登录"/>
        </BorderBox4>
        <BorderBox1 class="default-border">
          <!--消息排行-->
          <BarRankingCharts chart-name="消息类型发送"/>
        </BorderBox1>
        <div class="default-border">
          <!--消息信息-->
          <TableRanking/>
        </div>
      </el-col>

      <el-col :span="12">
        <!-- 标题和装饰 -->
        <div class="title-wrapper">
          <p class="title-text">用户数据监控统计</p>
          <Decoration5 class="title-decoration"/>
        </div>

        <div class="map-border">
          <MapCharts height="100%"/>
        </div>
      </el-col>

      <el-col :span="6">
        <BorderBox4 :reverse="true" :color="border4Color" class="default-border">
          <!--年龄分布-->
          <RadarCharts/>
        </BorderBox4>
        <BorderBox1 class="default-border">
          <!--性别分布-->
          <PieCharts/>
        </BorderBox1>
        <!--登录-->
        <div class="default-border">
          <BarLineCharts/>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import {BorderBox1, BorderBox4, Decoration5} from '@kjgl77/datav-vue3'
import PieCharts from "@/components/Statistics/PieCharts";
import BarRankingCharts from "@/components/Statistics/BarRankingCharts";
import LineZoomCharts from "@/components/Statistics/LineZoomCharts.vue";
import RadarCharts from "@/components/Statistics/RadarCharts.vue";
import MapCharts from "@/components/Statistics/MapCharts.vue";
import TableRanking from "@/components/Statistics/TableRanking.vue";
import BarLineCharts from "@/components/Statistics/BarLineCharts.vue";

const border4Color = ['#545fac', '#545fac']

</script>

<style scoped lang="scss">
.user-statistics {
  background-image: url("/src/assets/images/user-statistics-bg.png");

  .default-border {
    height: 33vh;
  }

  .map-border {
    height: 70vh;
  }

  .title-wrapper {
    position: relative;
    width: 100%;
    height: 10vh;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .title-text {
    font-size: 4vh;
    color: white;
    text-align: center;
    margin: 0;
    z-index: 2; // 保证文字在装饰上面
  }

  .title-decoration {
    position: absolute;
    bottom: 0; // 装饰条紧贴容器底部
    left: 0;
    top: 2vh;
    width: 100%;
    height: 10vh;
    z-index: 1;
  }
}
</style>
