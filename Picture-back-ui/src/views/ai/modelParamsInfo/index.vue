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
      <el-form-item label="KEY" prop="modelKey">
        <el-input
            v-model="queryParams.modelKey"
            placeholder="请输入模型KEY"
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
      <el-form-item label="模型" prop="model">
        <el-input
            v-model="queryParams.model"
            placeholder="请输入模型"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="名称" prop="modelLabel">
        <el-input
            v-model="queryParams.modelLabel"
            placeholder="请输入名称"
            clearable
            @keyup.enter="handleQuery"
        />
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
      <el-table-column label="模型KEY" align="center" prop="modelKey" v-if="columns[1].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="模型名称" align="center" prop="modelName" v-if="columns[2].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="模型类型" align="center" prop="modelType" v-if="columns[3].visible">
        <template #default="scope">
          <dict-tag :options="ai_model_params_type" :value="scope.row.modelType"/>
        </template>
      </el-table-column>
      <el-table-column label="模型" align="center" prop="model" v-if="columns[4].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="名称" align="center" prop="modelLabel" v-if="columns[5].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="API地址" align="center" prop="apiUrl" v-if="columns[6].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="安全密钥" align="center" prop="apiKey" v-if="columns[7].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="安全KEY" align="center" prop="secretKey" v-if="columns[8].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="价格" align="center" sortable="custom" prop="priceUse" v-if="columns[9].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="模型参数" align="center" prop="modelParams" v-if="columns[10].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="模型介绍" align="center" prop="modelDescription" v-if="columns[11].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="使用次数" align="center" prop="usageCount" v-if="columns[12].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="赚取积分" align="center" sortable="custom" prop="pointsEarned" v-if="columns[13].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="积分" align="center" sortable="custom" prop="pointsNeed" v-if="columns[14].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="扩展配置" align="center" prop="extendConfig" v-if="columns[15].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="状态" align="center" prop="paramsStatus" v-if="columns[16].visible">
        <template #default="scope">
          <dict-tag :options="ai_model_params_status" :value="scope.row.paramsStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="排序" align="center" sortable="custom" prop="orderNum" v-if="columns[17].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="创建人" align="center" prop="createBy" v-if="columns[18].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="创建时间" align="center" sortable="custom" prop="createTime" width="180"
                       v-if="columns[19].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新人" align="center" prop="updateBy" v-if="columns[20].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="更新时间" align="center" sortable="custom" prop="updateTime" width="180"
                       v-if="columns[21].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" v-if="columns[22].visible"
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
    <el-dialog :title="title" v-model="open" width="1000px" append-to-body>
      <el-form ref="modelParamsInfoRef" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="模型KEY" prop="modelKey">
              <template #label>
                <span class="custom-label">
                 模型KEY
                 <el-tooltip effect="light" placement="top"
                             content="模型KEY，用户其他信息绑定该模型的KEY，也就是外键，例：模型_模型KEY">
                   <QuestionFilled class="tooltip-icon"/>
                 </el-tooltip>
               </span>
              </template>
              <el-input v-model="form.modelKey" placeholder="请输入模型KEY"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模型名称" prop="modelName">
              <template #label>
                <span class="custom-label">
                 模型名称
                 <el-tooltip effect="light" placement="top" content="模型名称，大模型平台上对应的模型名">
                   <QuestionFilled class="tooltip-icon"/>
                 </el-tooltip>
               </span>
              </template>
              <el-input v-model="form.modelName" placeholder="请输入模型名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
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
          </el-col>
          <el-col :span="12">
            <el-form-item label="模型" prop="model">
              <template #label>
               <span class="custom-label">
                 模型
                 <el-tooltip effect="light" placement="top" content="模型的哪个平台，例如：AliYun">
                   <QuestionFilled class="tooltip-icon"/>
                 </el-tooltip>
               </span>
              </template>
              <el-input v-model="form.model" placeholder="请输入模型"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="名称" prop="modelLabel">
              <template #label>
               <span class="custom-label">
                 名称
                 <el-tooltip effect="light" placement="top" content="用户端展示给用户的模型名称，例：通义 2.1">
                   <QuestionFilled class="tooltip-icon"/>
                 </el-tooltip>
               </span>
              </template>
              <el-input v-model="form.modelLabel" placeholder="请输入名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
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

          </el-col>
          <el-col :span="12">
            <el-form-item label="安全密钥" prop="apiKey">
              <el-input type="password" show-password v-model="form.apiKey" placeholder="请输入安全密钥"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="安全KEY" prop="secretKey">
              <el-input type="password" show-password v-model="form.secretKey" placeholder="请输入安全KEY"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="价格" prop="priceUse">
              <el-input-number style="width: 100%;" :min="0" :precision="2" :step="0.1" v-model="form.priceUse"
                               placeholder="请输入价格"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="积分" prop="pointsNeed">
              <el-input-number style="width: 100%;" :min="0" :step="10" v-model="form.pointsNeed"
                               placeholder="请输入积分"/>
            </el-form-item>

          </el-col>
          <el-col :span="8">
            <el-form-item label="排序" prop="orderNum">
              <el-input-number style="width: 100%;" :min="0" :step="1" v-model="form.orderNum"
                               placeholder="请输入排序"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="API地址" prop="apiUrl">
              <el-input v-model="form.apiUrl" :rows="5" type="textarea" placeholder="请输入内容"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模型介绍" prop="modelDescription">
              <template #label>
               <span class="custom-label">
                 模型介绍
                 <el-tooltip effect="light" placement="top" content="模型介绍，用于给用户介绍该模型的基本信息">
                   <QuestionFilled class="tooltip-icon"/>
                 </el-tooltip>
               </span>
              </template>
              <el-input v-model="form.modelDescription" :rows="5" type="textarea" placeholder="请输入内容"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模型参数" prop="modelParams">
              <template #label>
               <span class="custom-label">
                 模型参数
                 <el-tooltip effect="light" placement="top" content="JSON对象，模型的基本参数">
                   <QuestionFilled class="tooltip-icon"/>
                 </el-tooltip>
               </span>
              </template>
              <el-input v-model="form.modelParams" :rows="5" type="textarea" placeholder="请输入内容"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="扩展配置" prop="extendConfig">
              <template #label>
               <span class="custom-label">
                 扩展配置
                 <el-tooltip effect="light" placement="top" content="JSON对象，模型扩展配置，例如一些校验">
                   <QuestionFilled class="tooltip-icon"/>
                 </el-tooltip>
               </span>
              </template>
              <el-input v-model="form.extendConfig" :rows="5" type="textarea" placeholder="请输入内容"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" :rows="5" type="textarea" placeholder="请输入内容"/>
            </el-form-item>
          </el-col>
        </el-row>


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
import {QuestionFilled} from "@element-plus/icons-vue";

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
    modelKey: null,
    modelName: null,
    modelType: null,
    model: null,
    modelLabel: null,
    paramsStatus: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
  },
  rules: {
    modelKey: [
      {required: true, message: "模型KEY不能为空", trigger: "blur"}
    ],
    modelName: [
      {required: true, message: "模型名称不能为空", trigger: "blur"}
    ],
    modelType: [
      {required: true, message: "模型类型不能为空", trigger: "change"}
    ],
    model: [
      {required: true, message: "模型不能为空", trigger: "blur"}
    ],
    modelLabel: [
      {required: true, message: "名称不能为空", trigger: "blur"}
    ],
    apiUrl: [
      {required: true, message: "API地址不能为空", trigger: "blur"}
    ],
    priceUse: [
      {required: true, message: "价格不能为空", trigger: "blur"}
    ],
    modelParams: [
      {required: true, message: "模型参数不能为空", trigger: "blur"}
    ],
    usageCount: [
      {required: true, message: "使用次数不能为空", trigger: "blur"}
    ],
    pointsEarned: [
      {required: true, message: "赚取积分不能为空", trigger: "blur"}
    ],
    paramsStatus: [
      {required: true, message: "状态不能为空", trigger: "change"}
    ],
    orderNum: [
      {required: true, message: "排序不能为空", trigger: "blur"}
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
    {key: 0, label: '模型编号', visible: false},
    {key: 1, label: '模型KEY', visible: true},
    {key: 2, label: '模型名称', visible: true},
    {key: 3, label: '模型类型', visible: true},
    {key: 4, label: '模型', visible: true},
    {key: 5, label: '名称', visible: true},
    {key: 6, label: 'API地址', visible: false},
    {key: 7, label: '安全密钥', visible: false},
    {key: 8, label: '安全KEY', visible: false},
    {key: 9, label: '价格', visible: true},
    {key: 10, label: '模型参数', visible: false},
    {key: 11, label: '模型介绍', visible: false},
    {key: 12, label: '使用次数', visible: true},
    {key: 13, label: '赚取积分', visible: true},
    {key: 14, label: '积分', visible: true},
    {key: 15, label: '扩展配置', visible: false},
    {key: 16, label: '状态', visible: true},
    {key: 17, label: '排序', visible: false},
    {key: 18, label: '创建人', visible: true},
    {key: 19, label: '创建时间', visible: true},
    {key: 20, label: '更新人', visible: false},
    {key: 21, label: '更新时间', visible: false},
    {key: 22, label: '备注', visible: false},
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
  loading.value = true;
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
    modelKey: null,
    modelName: null,
    modelType: null,
    model: null,
    modelLabel: null,
    apiUrl: null,
    apiKey: null,
    secretKey: null,
    priceUse: null,
    modelParams: null,
    modelDescription: null,
    usageCount: null,
    pointsEarned: null,
    pointsNeed: null,
    extendConfig: null,
    paramsStatus: null,
    orderNum: null,
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
  proxy.resetForm("queryRef");
  proxy.$refs.tableRef.clearSort();
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
<style scoped lang="scss">
.custom-label {
  display: inline-flex; /* 保持内联弹性布局 */
  align-items: center; /* 垂直居中 */
}

.tooltip-icon {
  width: 14px;
  height: 14px;
  color: #F56C6C;
  margin-left: 5px;
}
</style>
