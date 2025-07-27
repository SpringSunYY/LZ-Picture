<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="模型编号" prop="modelId">
        <el-input
            v-model="queryParams.modelId"
            placeholder="请输入模型编号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="模型名称" prop="modelName">
        <el-input
            v-model="queryParams.modelName"
            placeholder="请输入模型名称"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="模型类型" prop="modelType">
        <el-select v-model="queryParams.modelType" style="width: 200px" placeholder="请选择模型类型" clearable>
          <el-option
              v-for="dict in ai_model_params_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="paramsStatus">
        <el-select v-model="queryParams.paramsStatus" style="width: 200px" placeholder="请选择状态" clearable>
          <el-option
              v-for="dict in ai_model_params_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="管理员编号" prop="userId">
        <el-input
            v-model="queryParams.userId"
            placeholder="请输入管理员编号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建人" prop="createBy">
        <el-input
            v-model="queryParams.createBy"
            placeholder="请输入创建人"
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
      <el-form-item label="更新人" prop="updateBy">
        <el-input
            v-model="queryParams.updateBy"
            placeholder="请输入更新人"
            clearable
            @keyup.enter="handleQuery"
        />
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
            v-hasPermi="['ai:modelParamsInfo:add']"
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
            v-hasPermi="['ai:modelParamsInfo:edit']"
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
            v-hasPermi="['ai:modelParamsInfo:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['ai:modelParamsInfo:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table ref="tableRef" v-loading="loading" :data="modelParamsInfoList" @selection-change="handleSelectionChange"
              @sort-change="customSort">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="序号" type="index" width="50"/>
      <el-table-column label="模型编号" align="center" prop="modelId" v-if="columns[0].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="模型名称" align="center" prop="modelName" v-if="columns[1].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="模型类型" align="center" prop="modelType" v-if="columns[2].visible">
        <template #default="scope">
          <dict-tag :options="ai_model_params_type" :value="scope.row.modelType"/>
        </template>
      </el-table-column>
      <el-table-column label="安全密钥" align="center" prop="apiKey" v-if="columns[3].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="安全KEY" align="center" prop="secretKey" v-if="columns[4].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="模型参数" align="center" prop="modelParams" v-if="columns[5].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="模型介绍" align="center" prop="modelDescription" v-if="columns[6].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="平均使用tokens/次" align="center" prop="tokensAvg" v-if="columns[7].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="累计消耗Tokens" align="center" prop="tokensTotal" v-if="columns[8].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="使用次数" align="center" prop="usageCount" v-if="columns[9].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="积分消耗比例" align="center" prop="pointsNeed" v-if="columns[10].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="扩展配置" align="center" prop="extendConfig" v-if="columns[11].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="状态" align="center" prop="paramsStatus" v-if="columns[12].visible">
        <template #default="scope">
          <dict-tag :options="ai_model_params_status" :value="scope.row.paramsStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="管理员编号" align="center" prop="userId" v-if="columns[13].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="创建人" align="center" prop="createBy" v-if="columns[14].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" v-if="columns[15].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新人" align="center" prop="updateBy" v-if="columns[16].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180" v-if="columns[17].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" v-if="columns[18].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['ai:modelParamsInfo:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['ai:modelParamsInfo:remove']">删除
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

    <!-- 添加或修改AI模型参数配置对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="modelParamsInfoRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="模型名称" prop="modelName">
          <el-input v-model="form.modelName" placeholder="请输入模型名称"/>
        </el-form-item>
        <el-form-item label="模型类型" prop="modelType">
          <el-select v-model="form.modelType" placeholder="请选择模型类型">
            <el-option
                v-for="dict in ai_model_params_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="安全密钥" prop="apiKey">
          <el-input v-model="form.apiKey" placeholder="请输入安全密钥"/>
        </el-form-item>
        <el-form-item label="安全KEY" prop="secretKey">
          <el-input v-model="form.secretKey" placeholder="请输入安全KEY"/>
        </el-form-item>
        <el-form-item label="模型参数" prop="modelParams">
          <el-input v-model="form.modelParams" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="模型介绍" prop="modelDescription">
          <el-input v-model="form.modelDescription" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="平均使用tokens/次" prop="tokensAvg">
          <el-input v-model="form.tokensAvg" placeholder="请输入平均使用tokens/次"/>
        </el-form-item>
        <el-form-item label="累计消耗Tokens" prop="tokensTotal">
          <el-input v-model="form.tokensTotal" placeholder="请输入累计消耗Tokens"/>
        </el-form-item>
        <el-form-item label="使用次数" prop="usageCount">
          <el-input v-model="form.usageCount" placeholder="请输入使用次数"/>
        </el-form-item>
        <el-form-item label="积分消耗比例" prop="pointsNeed">
          <el-input v-model="form.pointsNeed" placeholder="请输入积分消耗比例"/>
        </el-form-item>
        <el-form-item label="扩展配置" prop="extendConfig">
          <el-input v-model="form.extendConfig" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="状态" prop="paramsStatus">
          <el-radio-group v-model="form.paramsStatus">
            <el-radio
                v-for="dict in ai_model_params_status"
                :key="dict.value"
                :value="dict.value"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="管理员编号" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入管理员编号"/>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"/>
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

<script setup name="ModelParamsInfo">
import {
  listModelParamsInfo,
  getModelParamsInfo,
  delModelParamsInfo,
  addModelParamsInfo,
  updateModelParamsInfo
} from "@/api/ai/modelParamsInfo";

const {proxy} = getCurrentInstance();
const {ai_model_params_status, ai_model_params_type} = proxy.useDict('ai_model_params_status', 'ai_model_params_type');

const modelParamsInfoList = ref([]);
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

const isAsc = ref();
const orderByColumn = ref('');
const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    modelId: null,
    modelName: null,
    modelType: null,
    paramsStatus: null,
    userId: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
  },
  rules: {
    modelName: [
      {required: true, message: "模型名称不能为空", trigger: "blur"}
    ],
    modelType: [
      {required: true, message: "模型类型不能为空", trigger: "change"}
    ],
    modelParams: [
      {required: true, message: "模型参数不能为空", trigger: "blur"}
    ],
    tokensAvg: [
      {required: true, message: "平均使用tokens/次不能为空", trigger: "blur"}
    ],
    tokensTotal: [
      {required: true, message: "累计消耗Tokens不能为空", trigger: "blur"}
    ],
    usageCount: [
      {required: true, message: "使用次数不能为空", trigger: "blur"}
    ],
    paramsStatus: [
      {required: true, message: "状态不能为空", trigger: "change"}
    ],
    createBy: [
      {required: true, message: "创建人不能为空", trigger: "blur"}
    ],
    createTime: [
      {required: true, message: "创建时间不能为空", trigger: "blur"}
    ],
  },
  //表格展示列
  columns: [
    {key: 0, label: '模型编号', visible: true},
    {key: 1, label: '模型名称', visible: true},
    {key: 2, label: '模型类型', visible: true},
    {key: 3, label: '安全密钥', visible: true},
    {key: 4, label: '安全KEY', visible: true},
    {key: 5, label: '模型参数', visible: true},
    {key: 6, label: '模型介绍', visible: true},
    {key: 7, label: '平均使用tokens/次', visible: true},
    {key: 8, label: '累计消耗Tokens', visible: true},
    {key: 9, label: '使用次数', visible: true},
    {key: 10, label: '积分消耗比例', visible: true},
    {key: 11, label: '扩展配置', visible: true},
    {key: 12, label: '状态', visible: true},
    {key: 13, label: '管理员编号', visible: true},
    {key: 14, label: '创建人', visible: true},
    {key: 15, label: '创建时间', visible: true},
    {key: 16, label: '更新人', visible: true},
    {key: 17, label: '更新时间', visible: true},
    {key: 18, label: '备注', visible: true},
  ],
});

const {queryParams, form, rules, columns} = toRefs(data);

//自定义排序
function customSort({column, prop, order}) {
  if (prop !== undefined && prop !== '' && order !== null && order !== '') {
    orderByColumn.value = prop;
    isAsc.value = order === "ascending";
  } else {
    orderByColumn.value = null;
    isAsc.value = null;
  }
  queryParams.value.pageNum = 1;
  getList();
}


/** 查询AI模型参数配置列表 */
function getList() {
  queryParams.value.params = {};
  if (orderByColumn.value != null && isAsc.value !== null) {
    queryParams.value.params["orderByColumn"] = orderByColumn.value;
    queryParams.value.params["isAsc"] = isAsc.value;
  }
  if (null != daterangeCreateTime && '' != daterangeCreateTime) {
    queryParams.value.params["beginCreateTime"] = daterangeCreateTime.value[0];
    queryParams.value.params["endCreateTime"] = daterangeCreateTime.value[1];
  }
  if (null != daterangeUpdateTime && '' != daterangeUpdateTime) {
    queryParams.value.params["beginUpdateTime"] = daterangeUpdateTime.value[0];
    queryParams.value.params["endUpdateTime"] = daterangeUpdateTime.value[1];
  }
  listModelParamsInfo(queryParams.value).then(response => {
    modelParamsInfoList.value = response.rows;
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
    modelId: null,
    modelName: null,
    modelType: null,
    apiKey: null,
    secretKey: null,
    modelParams: null,
    modelDescription: null,
    tokensAvg: null,
    tokensTotal: null,
    usageCount: null,
    pointsNeed: null,
    extendConfig: null,
    paramsStatus: null,
    userId: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
    remark: null
  };
  proxy.resetForm("modelParamsInfoRef");
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
  orderByColumn.value = null
  isAsc.value = null;
  proxy.$refs.tableRef.clearSort();
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.modelId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加AI模型参数配置";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _modelId = row.modelId || ids.value
  getModelParamsInfo(_modelId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改AI模型参数配置";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["modelParamsInfoRef"].validate(valid => {
    if (valid) {
      if (form.value.modelId != null) {
        updateModelParamsInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addModelParamsInfo(form.value).then(response => {
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
  const _modelIds = row.modelId || ids.value;
  proxy.$modal.confirm('是否确认删除AI模型参数配置编号为"' + _modelIds + '"的数据项？').then(function () {
    return delModelParamsInfo(_modelIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('ai/modelParamsInfo/export', {
    ...queryParams.value
  }, `modelParamsInfo_${new Date().getTime()}.xlsx`)
}

getList();
</script>
