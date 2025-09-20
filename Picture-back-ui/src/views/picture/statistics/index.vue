<template>
  <PictureScreenBorder class="picture-statistics">
    <el-row :gutter="20">
      <el-col :span="6">
        <div class="base-height">
          <KeywordCharts :chart-name="searchKeywordName" :chart-data="searchKeywordData"/>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="top-count">
          <p>总计：{{ pictureTotal }}张图片</p>
        </div>
        <el-row :gutter="20">
          <el-col :span="12">
            <BorderBox8 class="top-center">
              <PieLineCharts :chart-name="spaceFileSizeName" :chart-data="spaceFileSizeData" data-unit="GB"/>
            </BorderBox8>
          </el-col>
          <el-col :span="12">
            <BorderBox8 :reverse="true" class="top-center">
              <PieLineCharts :chart-name="spaceFileTotalName" :chart-data="spaceFileTotalData" data-unit="个"/>
            </BorderBox8>
          </el-col>
        </el-row>
      </el-col>
      <el-col :span="6">
        <div class="base-height">
          <KeywordCharts :chart-name="tagKeywordName" :chart-data="tagKeywordData"/>
        </div>
      </el-col>
    </el-row>
    <BorderBox7 class="center-height-1">
      <el-row :gutter="20" class="center-height-1">
        <el-col :span="4" :offset="2">
          <WaterMapRotateProportionCharts :chart-name="pictureStatusNormalName" :total="pictureTotal"
                                          :current="pictureStatusNormalTotal"/>
        </el-col>
        <el-col :span="4" v-for="item in pictureTypeData">
          <WaterMapProportionCharts :chart-name="item.name" :current="Number(item.value)" :total="pictureTotal"/>
        </el-col>
        <el-col :span="4">
          <WaterMapRotateProportionCharts :chart-name="pictureStatusPrivateName" :total="pictureTotal"
                                          :current="pictureStatusPrivateTotal"/>
        </el-col>
      </el-row>
    </BorderBox7>
    <el-row :gutter="20" class="center-height-2">
      <el-col :span="8" class="center-height-2">
        <TableRanking/>
      </el-col>
      <el-col :span="8">
        <PieIntervalCharts :chart-name="userBehaviorName" :chart-data="userBehaviorData"/>
      </el-col>
      <el-col :span="8" class="center-height-2">
        <BarAxisRankingCharts chartDirection="right"/>
      </el-col>
    </el-row>
    <Decoration2 style="width:100%; height:5px;"/>
    <el-row :gutter="20" class="bottom-height">
      <el-col :span="7">
        <LineAvgCharts :chart-data="pictureDownloadData" :chart-name="pictureDownloadName"/>
      </el-col>
      <el-col :span="10">
        <LineZoomCharts :chart-name="pictureName" :chart-data="pictureData"/>
      </el-col>
      <el-col :span="7">
        <BarAvgCharts :chart-data="spaceData" :chart-name="spaceName"/>
      </el-col>
    </el-row>
    <DateRangePicker
        @change="onDateChange"
        top="1%"
        right="25%"
    />
  </PictureScreenBorder>
</template>
<script setup>
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
import DateRangePicker from "@/components/Statistics/DateRangePicker.vue";
import dayjs from "dayjs";
import {ref} from "vue";
import {
  pictureDownloadStatistics, pictureStatistics,
  pictureStatusStatistics,
  pictureUploadTypeStatistics,
  searchKeywordStatistics, spaceFileSizeStatistics, spaceFileTotalStatistics, spaceStatistics,
  tagKeywordStatistics, userBehaviorStatistics
} from "@/api/picture/statisticsInfo.js";
import {formatSizeToGB} from "@/utils/ruoyi.js";
import BarRankingCharts from "@/components/Statistics/BarRankingCharts.vue";
import BarAxisRankingCharts from "@/components/Statistics/BarAxisRankingCharts.vue";

const defaultStart = dayjs().subtract(14, "day").format("YYYY-MM-DD")
const defaultEnd = dayjs().format("YYYY-MM-DD")
const query = ref({
  startDate: defaultStart,
  endDate: defaultEnd
})
// 用户注册
const onDateChange = (val) => {
  query.value.startDate = val?.[0] || ''
  query.value.endDate = val?.[1] || ''
  getStatistics()
}
const getStatistics = () => {
  getSearchKeywordData()
  getTagKeywordData()
  getUserBehaviorData()
  getPictureDownloadData()
  getSpaceData()
  getPictureData()
}
//搜索关键词统计
const searchKeywordData = ref([])
const searchKeywordName = ref('热门搜索')
const getSearchKeywordData = () => {
  searchKeywordStatistics({startDate: query.value.startDate, endDate: query.value.endDate, size: 50}).then(res => {
    searchKeywordData.value = res.data
  })
}
//标签关键词统计
const tagKeywordData = ref([])
const tagKeywordName = ref('热门标签')
const getTagKeywordData = () => {
  tagKeywordStatistics({startDate: query.value.startDate, endDate: query.value.endDate, size: 50}).then(res => {
    tagKeywordData.value = res.data
  })
}
//图片下载
const pictureDownloadData = ref([])
const pictureDownloadName = ref('图片下载')
const getPictureDownloadData = () => {
  pictureDownloadStatistics(query.value).then(res => {
    pictureDownloadData.value = res.data
  })
}
//空间统计
const spaceData = ref([])
const spaceName = ref('空间统计')
const getSpaceData = () => {
  spaceStatistics(query.value).then(res => {
    spaceData.value = res.data
  })
}
//图片统计
const pictureData = ref([])
const pictureName = ref('图片统计')
const getPictureData = () => {
  pictureStatistics(query.value).then(res => {
    pictureData.value = res.data
  })
}
//图片状态
const pictureTotal = ref(0)
const pictureStatusNormalTotal = ref(0)
const pictureStatusNormalName = ref('公共图片')
const pictureStatusPrivateTotal = ref(0)
const pictureStatusPrivateName = ref('私有图片')
const getPictureStatusData = () => {
  pictureStatusStatistics().then(res => {
    if (res.data) {
      res.data.map(item => {
        const value = Number(item.value)
        if (item.name === "公共") {
          pictureStatusNormalTotal.value = value
        }
        if (item.name === "私有") {
          pictureStatusPrivateTotal.value = value
        }
        pictureTotal.value += value
      })
    }
  })
}
getPictureStatusData()
//图片上传类型
const pictureTypeData = ref([])
const getPictureTypeData = () => {
  pictureUploadTypeStatistics().then(res => {
    pictureTypeData.value = res.data
  })
}
getPictureTypeData()

//空间文件大小
const spaceFileSizeData = ref([])
const spaceFileSizeName = ref('空间文件大小')
const getSpaceFileSizeData = () => {
  spaceFileSizeStatistics().then(res => {
    spaceFileSizeData.value = res.data.map(item => {
      return {
        name: item.name,
        value: formatSizeToGB(item.value)
      }
    })
  })
}
getSpaceFileSizeData()
//空间文件总数
const spaceFileTotalData = ref([])
const spaceFileTotalName = ref('空间文件总数')
const getSpaceFileTotalData = () => {
  spaceFileTotalStatistics().then(res => {
    spaceFileTotalData.value = res.data
  })
}
getSpaceFileTotalData()

//用户行文
const userBehaviorData = ref([])
const userBehaviorName = ref('用户行为')
const getUserBehaviorData = () => {
  userBehaviorStatistics(query.value).then(res => {
    userBehaviorData.value = res.data
  })
}
getStatistics()
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
