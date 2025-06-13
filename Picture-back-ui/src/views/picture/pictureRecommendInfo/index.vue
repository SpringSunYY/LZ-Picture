<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="推荐编号" prop="recommendId">
        <el-input
            v-model="queryParams.recommendId"
            placeholder="请输入推荐编号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户编号" prop="userId">
        <el-input
            v-model="queryParams.userId"
            placeholder="请输入用户编号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间" style="width: 308px">
        <el-date-picker
            v-model="daterangeCreateTime"
            value-format="YYYY-MM-DD"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!--      <el-col :span="1.5">-->
      <!--        <el-button-->
      <!--            type="primary"-->
      <!--            plain-->
      <!--            icon="Plus"-->
      <!--            @click="handleAdd"-->
      <!--            v-hasPermi="['picture:pictureRecommendInfo:add']"-->
      <!--        >新增-->
      <!--        </el-button>-->
      <!--      </el-col>-->
      <!--      <el-col :span="1.5">-->
      <!--        <el-button-->
      <!--            type="success"-->
      <!--            plain-->
      <!--            icon="Edit"-->
      <!--            :disabled="single"-->
      <!--            @click="handleUpdate"-->
      <!--            v-hasPermi="['picture:pictureRecommendInfo:edit']"-->
      <!--        >修改-->
      <!--        </el-button>-->
      <!--      </el-col>-->
      <el-col :span="1.5">
        <el-button
            type="danger"
            plain
            icon="Delete"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['picture:pictureRecommendInfo:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['picture:pictureRecommendInfo:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="pictureRecommendInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="推荐编号" align="center" prop="recommendId" v-if="columns[0].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="分类分数" align="center" prop="categoryScores" v-if="columns[1].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="高兴趣分类" align="center" prop="topCategories" v-if="columns[2].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="归一化分类分数" align="center" prop="normalizedCategoryScores" v-if="columns[3].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="标签分数" align="center" prop="tagScores" v-if="columns[4].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="高兴趣标签" align="center" prop="topTags" v-if="columns[5].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="归一化标签分数" align="center" prop="normalizedTagScores" v-if="columns[6].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="更多信息" align="center" prop="moreInfo" v-if="columns[7].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="用户编号" align="center" prop="userId" v-if="columns[8].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" v-if="columns[9].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">-->
      <!--        <template #default="scope">-->
      <!--          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"-->
      <!--                     v-hasPermi="['picture:pictureRecommendInfo:edit']">修改-->
      <!--          </el-button>-->
      <!--          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"-->
      <!--                     v-hasPermi="['picture:pictureRecommendInfo:remove']">删除-->
      <!--          </el-button>-->
      <!--        </template>-->
      <!--      </el-table-column>-->
    </el-table>

    <pagination
        v-show="total>0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
    />

    <!-- 添加或修改用户图片推荐模型对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="pictureRecommendInfoRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="分类分数" prop="categoryScores">
          <el-input v-model="form.categoryScores" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="高兴趣分类" prop="topCategories">
          <el-input v-model="form.topCategories" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="归一化分类分数" prop="normalizedCategoryScores">
          <el-input v-model="form.normalizedCategoryScores" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="标签分数" prop="tagScores">
          <el-input v-model="form.tagScores" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="高兴趣标签" prop="topTags">
          <el-input v-model="form.topTags" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="归一化标签分数" prop="normalizedTagScores">
          <el-input v-model="form.normalizedTagScores" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="更多信息" prop="moreInfo">
          <el-input v-model="form.moreInfo" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="用户编号" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户编号"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="PictureRecommendInfo">
import {
  listPictureRecommendInfo,
  getPictureRecommendInfo,
  delPictureRecommendInfo,
  addPictureRecommendInfo,
  updatePictureRecommendInfo
} from "@/api/picture/pictureRecommendInfo";

const {proxy} = getCurrentInstance();

const pictureRecommendInfoList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const daterangeCreateTime = ref([]);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    recommendId: null,
    userId: null,
    createTime: null
  },
  rules: {
    userId: [
      {required: true, message: "用户编号不能为空", trigger: "blur"}
    ],
    createTime: [
      {required: true, message: "创建时间不能为空", trigger: "blur"}
    ]
  },
  //表格展示列
  columns: [
    {key: 0, label: '推荐编号', visible: false},
    {key: 1, label: '分类分数', visible: true},
    {key: 2, label: '高兴趣分类', visible: true},
    {key: 3, label: '归一化分类分数', visible: true},
    {key: 4, label: '标签分数', visible: true},
    {key: 5, label: '高兴趣标签', visible: true},
    {key: 6, label: '归一化标签分数', visible: true},
    {key: 7, label: '更多信息', visible: true},
    {key: 8, label: '用户编号', visible: true},
    {key: 9, label: '创建时间', visible: true},
  ],
});

const {queryParams, form, rules, columns} = toRefs(data);

/** 查询用户图片推荐模型列表 */
function getList() {
  loading.value = true;
  queryParams.value.params = {};
  if (null != daterangeCreateTime && '' != daterangeCreateTime) {
    queryParams.value.params["beginCreateTime"] = daterangeCreateTime.value[0];
    queryParams.value.params["endCreateTime"] = daterangeCreateTime.value[1];
  }
  listPictureRecommendInfo(queryParams.value).then(response => {
    pictureRecommendInfoList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

// 取消按钮
function cancel() {
  open.value = false;
  reset();
}

// 表单重置
function reset() {
  form.value = {
    recommendId: null,
    categoryScores: null,
    topCategories: null,
    normalizedCategoryScores: null,
    tagScores: null,
    topTags: null,
    normalizedTagScores: null,
    moreInfo: null,
    userId: null,
    createTime: null
  };
  proxy.resetForm("pictureRecommendInfoRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  daterangeCreateTime.value = [];
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.recommendId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加用户图片推荐模型";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _recommendId = row.recommendId || ids.value
  getPictureRecommendInfo(_recommendId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改用户图片推荐模型";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["pictureRecommendInfoRef"].validate(valid => {
    if (valid) {
      if (form.value.recommendId != null) {
        updatePictureRecommendInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addPictureRecommendInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _recommendIds = row.recommendId || ids.value;
  proxy.$modal.confirm('是否确认删除用户图片推荐模型编号为"' + _recommendIds + '"的数据项？').then(function () {
    return delPictureRecommendInfo(_recommendIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('picture/pictureRecommendInfo/export', {
    ...queryParams.value
  }, `pictureRecommendInfo_${new Date().getTime()}.xlsx`)
}

getList();
</script>
