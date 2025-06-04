<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="图片编号" prop="pictureId">
        <el-input
            v-model="queryParams.pictureId"
            placeholder="请输入图片编号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="图片名称" prop="pictureName">
        <el-input
            v-model="queryParams.pictureName"
            placeholder="请输入图片名称"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="标签编号" prop="tagId">
        <el-input
            v-model="queryParams.tagId"
            placeholder="请输入标签编号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="标签名称" prop="tagName">
        <el-input
            v-model="queryParams.tagName"
            placeholder="请输入标签名称"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
            type="primary"
            plain
            icon="Plus"
            @click="handleAdd"
            v-hasPermi="['picture:pictureTagRelInfo:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="success"
            plain
            icon="Edit"
            :disabled="single"
            @click="handleUpdate"
            v-hasPermi="['picture:pictureTagRelInfo:edit']"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="danger"
            plain
            icon="Delete"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['picture:pictureTagRelInfo:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['picture:pictureTagRelInfo:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="pictureTagRelInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="图片编号" align="center" prop="pictureId" v-if="columns[0].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="图片名称" align="center" prop="pictureName" v-if="columns[1].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="标签编号" align="center" prop="tagId" v-if="columns[2].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="标签名称" align="center" prop="tagName" v-if="columns[3].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="查看次数" align="center" prop="lookCount" v-if="columns[4].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="收藏次数" align="center" prop="collectCount" v-if="columns[5].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="点赞次数" align="center" prop="likeCount" v-if="columns[6].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="分享次数" align="center" prop="shareCount" v-if="columns[7].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="下载次数" align="center" prop="downloadCount" v-if="columns[8].visible"
                       :show-overflow-tooltip="true"/>
<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">-->
<!--        <template #default="scope">-->
<!--          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"-->
<!--                     v-hasPermi="['picture:pictureTagRelInfo:edit']">修改-->
<!--          </el-button>-->
<!--          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"-->
<!--                     v-hasPermi="['picture:pictureTagRelInfo:remove']">删除-->
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

    <!-- 添加或修改图片标签关联对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="pictureTagRelInfoRef" :model="form" :rules="rules" label-width="80px">
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

<script setup name="PictureTagRelInfo">
import {
  listPictureTagRelInfo,
  getPictureTagRelInfo,
  delPictureTagRelInfo,
  addPictureTagRelInfo,
  updatePictureTagRelInfo
} from "@/api/picture/pictureTagRelInfo";

const {proxy} = getCurrentInstance();

const pictureTagRelInfoList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    pictureId: null,
    pictureName: null,
    tagId: null,
    tagName: null,
    lookCount: null,
    collectCount: null,
    likeCount: null,
    shareCount: null,
    downloadCount: null
  },
  rules: {
    pictureName: [
      {required: true, message: "图片名称不能为空", trigger: "blur"}
    ],
    tagName: [
      {required: true, message: "标签名称不能为空", trigger: "blur"}
    ],
    lookCount: [
      {required: true, message: "查看次数不能为空", trigger: "blur"}
    ],
    collectCount: [
      {required: true, message: "收藏次数不能为空", trigger: "blur"}
    ],
    likeCount: [
      {required: true, message: "点赞次数不能为空", trigger: "blur"}
    ],
    shareCount: [
      {required: true, message: "分享次数不能为空", trigger: "blur"}
    ],
    downloadCount: [
      {required: true, message: "下载次数不能为空", trigger: "blur"}
    ]
  },
  //表格展示列
  columns: [
    {key: 0, label: '图片编号', visible: true},
    {key: 1, label: '图片名称', visible: true},
    {key: 2, label: '标签编号', visible: false},
    {key: 3, label: '标签名称', visible: true},
    {key: 4, label: '查看次数', visible: true},
    {key: 5, label: '收藏次数', visible: true},
    {key: 6, label: '点赞次数', visible: true},
    {key: 7, label: '分享次数', visible: true},
    {key: 8, label: '下载次数', visible: true},
  ],
});

const {queryParams, form, rules, columns} = toRefs(data);

/** 查询图片标签关联列表 */
function getList() {
  loading.value = true;
  listPictureTagRelInfo(queryParams.value).then(response => {
    pictureTagRelInfoList.value = response.rows;
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
    pictureId: null,
    pictureName: null,
    tagId: null,
    tagName: null,
    lookCount: null,
    collectCount: null,
    likeCount: null,
    shareCount: null,
    downloadCount: null
  };
  proxy.resetForm("pictureTagRelInfoRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.pictureId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加图片标签关联";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _pictureId = row.pictureId || ids.value
  getPictureTagRelInfo(_pictureId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改图片标签关联";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["pictureTagRelInfoRef"].validate(valid => {
    if (valid) {
      if (form.value.pictureId != null) {
        updatePictureTagRelInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addPictureTagRelInfo(form.value).then(response => {
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
  const _pictureIds = row.pictureId || ids.value;
  proxy.$modal.confirm('是否确认删除图片标签关联编号为"' + _pictureIds + '"的数据项？').then(function () {
    return delPictureTagRelInfo(_pictureIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('picture/pictureTagRelInfo/export', {
    ...queryParams.value
  }, `pictureTagRelInfo_${new Date().getTime()}.xlsx`)
}

getList();
</script>
