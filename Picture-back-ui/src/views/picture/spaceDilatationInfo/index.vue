<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="申请编号" prop="dilatationId">
        <el-input
            v-model="queryParams.dilatationId"
            placeholder="请输入申请编号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="KEY" prop="dilatationKey">
        <el-input
            v-model="queryParams.dilatationKey"
            placeholder="请输入扩容KEY"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="空间编号" prop="spaceId">
        <el-input
            v-model="queryParams.spaceId"
            placeholder="请输入空间编号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="空间名称" prop="spaceName">
        <el-input
            v-model="queryParams.spaceName"
            placeholder="请输入空间名称"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="扩容类型" prop="dilatationType">
        <el-select v-model="queryParams.dilatationType" style="width: 200px" placeholder="请选择扩容类型" clearable>
          <el-option
              v-for="dict in p_space_dilatation_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="消耗积分" prop="pointsTotal">
        <el-input
            v-model="queryParams.pointsTotal"
            placeholder="请输入消耗积分"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户" prop="userId">
        <el-input
            v-model="queryParams.userId"
            placeholder="请输入用户"
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
      <el-col :span="1.5">
        <el-button
            type="primary"
            plain
            icon="Plus"
            @click="handleAdd"
            v-hasPermi="['picture:spaceDilatationInfo:add']"
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
            v-hasPermi="['picture:spaceDilatationInfo:edit']"
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
            v-hasPermi="['picture:spaceDilatationInfo:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['picture:spaceDilatationInfo:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="spaceDilatationInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="申请编号" align="center" prop="dilatationId" v-if="columns[0].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="扩容KEY" align="center" prop="dilatationKey" v-if="columns[1].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="空间编号" align="center" prop="spaceId" v-if="columns[2].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="空间名称" align="center" prop="spaceName" v-if="columns[3].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="缩略图 URL" align="center" prop="thumbnailUrl" width="100" v-if="columns[4].visible">
        <template #default="scope">
          <image-preview :src="scope.row.thumbnailUrl" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="扩容类型" align="center" prop="dilatationType" v-if="columns[5].visible">
        <template #default="scope">
          <dict-tag :options="p_space_dilatation_type" :value="scope.row.dilatationType"/>
        </template>
      </el-table-column>
      <el-table-column label="扩容单价" align="center" prop="dilatationUnit" v-if="columns[6].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="扩容总数" align="center" prop="dilatationTotal" v-if="columns[7].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="消耗积分" align="center" prop="pointsTotal" v-if="columns[8].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="用户" align="center" prop="userId" v-if="columns[9].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" v-if="columns[10].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['picture:spaceDilatationInfo:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['picture:spaceDilatationInfo:remove']">删除
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

    <!-- 添加或修改空间扩容信息对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="spaceDilatationInfoRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="扩容KEY" prop="dilatationKey">
          <el-input readonly v-model="form.dilatationKey" placeholder="请输入扩容KEY"/>
        </el-form-item>
<!--        <el-form-item label="空间编号" prop="spaceId">-->
<!--          <el-input v-model="form.spaceId" placeholder="请输入空间编号"/>-->
<!--        </el-form-item>-->
        <el-form-item label="空间名称" prop="spaceName">
          <el-input v-model="form.spaceName" placeholder="请输入空间名称"/>
        </el-form-item>
        <el-form-item label="缩略图 URL" prop="thumbnailUrl">
          <image-upload v-model="form.thumbnailUrl"/>
        </el-form-item>
        <el-form-item label="扩容类型" prop="dilatationType">
          <el-select v-model="form.dilatationType" placeholder="请选择扩容类型">
            <el-option
                :disabled="true"
                v-for="dict in p_space_dilatation_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="扩容单价" prop="dilatationUnit">
          <el-input readonly v-model="form.dilatationUnit" placeholder="请输入扩容单价"/>
        </el-form-item>
        <el-form-item label="扩容总数" prop="dilatationTotal">
          <el-input readonly v-model="form.dilatationTotal" placeholder="请输入扩容总数"/>
        </el-form-item>
        <el-form-item label="消耗积分" prop="pointsTotal">
          <el-input readonly v-model="form.pointsTotal" placeholder="请输入消耗积分"/>
        </el-form-item>
        <el-form-item label="用户" prop="userId">
          <el-input readonly v-model="form.userId" placeholder="请输入用户"/>
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

<script setup name="SpaceDilatationInfo">
import {
  listSpaceDilatationInfo,
  getSpaceDilatationInfo,
  delSpaceDilatationInfo,
  addSpaceDilatationInfo,
  updateSpaceDilatationInfo
} from "@/api/picture/spaceDilatationInfo";

const {proxy} = getCurrentInstance();
const {p_space_dilatation_type} = proxy.useDict('p_space_dilatation_type');

const spaceDilatationInfoList = ref([]);
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
    dilatationId: null,
    dilatationKey: null,
    spaceId: null,
    spaceName: null,
    dilatationType: null,
    pointsTotal: null,
    userId: null,
    createTime: null
  },
  rules: {
    dilatationKey: [
      {required: true, message: "扩容KEY不能为空", trigger: "blur"}
    ],
    spaceId: [
      {required: true, message: "空间编号不能为空", trigger: "blur"}
    ],
    spaceName: [
      {required: true, message: "空间名称不能为空", trigger: "blur"}
    ],
    thumbnailUrl: [
      {required: true, message: "缩略图 URL不能为空", trigger: "blur"}
    ],
    dilatationType: [
      {required: true, message: "扩容类型不能为空", trigger: "change"}
    ],
    dilatationUnit: [
      {required: true, message: "扩容单价不能为空", trigger: "blur"}
    ],
    dilatationTotal: [
      {required: true, message: "扩容总数不能为空", trigger: "blur"}
    ],
    userId: [
      {required: true, message: "用户不能为空", trigger: "blur"}
    ],
    createTime: [
      {required: true, message: "创建时间不能为空", trigger: "blur"}
    ]
  },
  //表格展示列
  columns: [
    {key: 0, label: '申请编号', visible: true},
    {key: 1, label: '扩容KEY', visible: false},
    {key: 2, label: '空间编号', visible: true},
    {key: 3, label: '空间名称', visible: true},
    {key: 4, label: '缩略图 URL', visible: true},
    {key: 5, label: '扩容类型', visible: true},
    {key: 6, label: '扩容单价', visible: true},
    {key: 7, label: '扩容总数', visible: true},
    {key: 8, label: '消耗积分', visible: true},
    {key: 9, label: '用户', visible: true},
    {key: 10, label: '创建时间', visible: true},
  ],
});

const {queryParams, form, rules, columns} = toRefs(data);

/** 查询空间扩容信息列表 */
function getList() {
  loading.value = true;
  queryParams.value.params = {};
  if (null != daterangeCreateTime && '' != daterangeCreateTime) {
    queryParams.value.params["beginCreateTime"] = daterangeCreateTime.value[0];
    queryParams.value.params["endCreateTime"] = daterangeCreateTime.value[1];
  }
  listSpaceDilatationInfo(queryParams.value).then(response => {
    spaceDilatationInfoList.value = response.rows;
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
    dilatationId: null,
    dilatationKey: null,
    spaceId: null,
    spaceName: null,
    thumbnailUrl: null,
    dilatationType: null,
    dilatationUnit: null,
    dilatationTotal: null,
    pointsTotal: null,
    userId: null,
    createTime: null
  };
  proxy.resetForm("spaceDilatationInfoRef");
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
  ids.value = selection.map(item => item.dilatationId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加空间扩容信息";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _dilatationId = row.dilatationId || ids.value
  getSpaceDilatationInfo(_dilatationId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改空间扩容信息";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["spaceDilatationInfoRef"].validate(valid => {
    if (valid) {
      if (form.value.dilatationId != null) {
        updateSpaceDilatationInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addSpaceDilatationInfo(form.value).then(response => {
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
  const _dilatationIds = row.dilatationId || ids.value;
  proxy.$modal.confirm('是否确认删除空间扩容信息编号为"' + _dilatationIds + '"的数据项？').then(function () {
    return delSpaceDilatationInfo(_dilatationIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('picture/spaceDilatationInfo/export', {
    ...queryParams.value
  }, `spaceDilatationInfo_${new Date().getTime()}.xlsx`)
}

getList();
</script>
