<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="标签编号" prop="tagId">
        <el-input
            v-model="queryParams.tagId"
            placeholder="请输入标签编号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="标签名称" prop="name">
        <el-input
            v-model="queryParams.name"
            placeholder="请输入标签名称"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="标签状态" prop="tagsStatus">
        <el-select v-model="queryParams.tagsStatus" style="width: 200px" placeholder="请选择标签状态" clearable>
          <el-option
              v-for="dict in p_tag_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="所属用户" prop="userId">
        <el-input
            v-model="queryParams.userId"
            placeholder="请输入所属用户"
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
      <el-form-item label="更新时间" style="width: 308px">
        <el-date-picker
            v-model="daterangeUpdateTime"
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
      <el-col :span="1.5">
        <el-button
            type="primary"
            plain
            icon="Plus"
            @click="handleAdd"
            v-hasPermi="['picture:pictureTagInfo:add']"
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
            v-hasPermi="['picture:pictureTagInfo:edit']"
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
            v-hasPermi="['picture:pictureTagInfo:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['picture:pictureTagInfo:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="pictureTagInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="标签编号" align="center" prop="tagId" v-if="columns[0].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="标签名称" align="center" prop="name" v-if="columns[1].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="标签状态" align="center" prop="tagsStatus" v-if="columns[2].visible">
        <template #default="scope">
          <dict-tag :options="p_tag_status" :value="scope.row.tagsStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="标签描述" align="center" prop="tagDesc" v-if="columns[3].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="使用次数" align="center" prop="usageCount" v-if="columns[4].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="查看次数" align="center" prop="lookCount" v-if="columns[5].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="下载次数" align="center" prop="downloadCount" v-if="columns[6].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="所属用户" align="center" prop="userId" v-if="columns[7].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" v-if="columns[8].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180" v-if="columns[9].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['picture:pictureTagInfo:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['picture:pictureTagInfo:remove']">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
        v-show="total>0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
    />

    <!-- 添加或修改图片标签信息对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="pictureTagInfoRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="标签名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入标签名称"/>
        </el-form-item>
        <el-form-item label="标签状态" prop="tagsStatus">
          <el-radio-group v-model="form.tagsStatus">
            <el-radio
                v-for="dict in p_tag_status"
                :key="dict.value"
                :value="dict.value"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="标签描述" prop="tagDesc">
          <el-input v-model="form.tagDesc" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="使用次数" prop="usageCount">
          <el-input v-model="form.usageCount" placeholder="请输入使用次数"/>
        </el-form-item>
        <el-form-item label="查看次数" prop="lookCount">
          <el-input v-model="form.lookCount" placeholder="请输入查看次数"/>
        </el-form-item>
        <el-form-item label="下载次数" prop="downloadCount">
          <el-input v-model="form.downloadCount" placeholder="请输入下载次数"/>
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

<script setup name="PictureTagInfo">
import {
  listPictureTagInfo,
  getPictureTagInfo,
  delPictureTagInfo,
  addPictureTagInfo,
  updatePictureTagInfo
} from "@/api/picture/pictureTagInfo";

const {proxy} = getCurrentInstance();
const {p_tag_status} = proxy.useDict('p_tag_status');

const pictureTagInfoList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const daterangeCreateTime = ref([]);
const daterangeUpdateTime = ref([]);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    tagId: null,
    name: null,
    tagsStatus: null,
    userId: null,
    createTime: null,
    updateTime: null
  },
  rules: {
    name: [
      {required: true, message: "标签名称不能为空", trigger: "blur"}
    ],
    tagsStatus: [
      {required: true, message: "标签状态不能为空", trigger: "change"}
    ],
    usageCount: [
      {required: true, message: "使用次数不能为空", trigger: "blur"}
    ],
    lookCount: [
      {required: true, message: "查看次数不能为空", trigger: "blur"}
    ],
    downloadCount: [
      {required: true, message: "下载次数不能为空", trigger: "blur"}
    ],
    userId: [
      {required: true, message: "所属用户不能为空", trigger: "blur"}
    ],
    createTime: [
      {required: true, message: "创建时间不能为空", trigger: "blur"}
    ],
    updateTime: [
      {required: true, message: "更新时间不能为空", trigger: "blur"}
    ]
  },
  //表格展示列
  columns: [
    {key: 0, label: '标签编号', visible: true},
    {key: 1, label: '标签名称', visible: true},
    {key: 2, label: '标签状态', visible: true},
    {key: 3, label: '标签描述', visible: true},
    {key: 4, label: '使用次数', visible: true},
    {key: 5, label: '查看次数', visible: true},
    {key: 6, label: '下载次数', visible: true},
    {key: 7, label: '所属用户', visible: true},
    {key: 8, label: '创建时间', visible: true},
    {key: 9, label: '更新时间', visible: true},
  ],
});

const {queryParams, form, rules, columns} = toRefs(data);

/** 查询图片标签信息列表 */
function getList() {
  loading.value = true;
  queryParams.value.params = {};
  if (null != daterangeCreateTime && '' != daterangeCreateTime) {
    queryParams.value.params["beginCreateTime"] = daterangeCreateTime.value[0];
    queryParams.value.params["endCreateTime"] = daterangeCreateTime.value[1];
  }
  if (null != daterangeUpdateTime && '' != daterangeUpdateTime) {
    queryParams.value.params["beginUpdateTime"] = daterangeUpdateTime.value[0];
    queryParams.value.params["endUpdateTime"] = daterangeUpdateTime.value[1];
  }
  listPictureTagInfo(queryParams.value).then(response => {
    pictureTagInfoList.value = response.rows;
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
    tagId: null,
    name: null,
    tagsStatus: null,
    tagDesc: null,
    usageCount: null,
    lookCount: null,
    downloadCount: null,
    userId: null,
    createTime: null,
    updateTime: null
  };
  proxy.resetForm("pictureTagInfoRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  daterangeCreateTime.value = [];
  daterangeUpdateTime.value = [];
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.tagId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加图片标签信息";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _tagId = row.tagId || ids.value
  getPictureTagInfo(_tagId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改图片标签信息";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["pictureTagInfoRef"].validate(valid => {
    if (valid) {
      if (form.value.tagId != null) {
        updatePictureTagInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addPictureTagInfo(form.value).then(response => {
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
  const _tagIds = row.tagId || ids.value;
  proxy.$modal.confirm('是否确认删除图片标签信息编号为"' + _tagIds + '"的数据项？').then(function () {
    return delPictureTagInfo(_tagIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('picture/pictureTagInfo/export', {
    ...queryParams.value
  }, `pictureTagInfo_${new Date().getTime()}.xlsx`)
}

getList();
</script>
