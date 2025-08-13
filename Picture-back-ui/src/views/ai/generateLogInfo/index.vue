<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="记录编号" prop="logId">
        <el-input
            v-model="queryParams.logId"
            placeholder="请输入记录编号"
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
      <el-form-item label="模型KEY" prop="modelKey">
        <el-input
            v-model="queryParams.modelKey"
            placeholder="请输入模型KEY"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="随机种子" prop="seed">
        <el-input
            v-model="queryParams.seed"
            placeholder="请输入随机种子"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="数量" prop="numbers">
        <el-input
            v-model="queryParams.numbers"
            placeholder="请输入数量"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="任务编号" prop="taskId">
        <el-input
            v-model="queryParams.taskId"
            placeholder="请输入任务编号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="宽度" prop="width">
        <el-input
            v-model="queryParams.width"
            placeholder="请输入宽度"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="高度" prop="height">
        <el-input
            v-model="queryParams.height"
            placeholder="请输入高度"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="请求时长" prop="requestDuration">
        <el-input
            v-model="queryParams.requestDuration"
            placeholder="请输入请求时长"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="参考对象" prop="targetId">
        <el-input
            v-model="queryParams.targetId"
            placeholder="请输入参考对象"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="logStatus">
        <el-select v-model="queryParams.logStatus" style="width: 200px" placeholder="请选择状态" clearable>
          <el-option
              v-for="dict in ai_log_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="返回码" prop="aiStatusCode">
        <el-input
            v-model="queryParams.aiStatusCode"
            placeholder="请输入模型返回码"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否统计" prop="hasStatistics">
        <el-select v-model="queryParams.hasStatistics" style="width: 200px" placeholder="请选择是否统计" clearable>
          <el-option
              v-for="dict in common_has_statistics"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="IP地址" prop="ipAddr">
        <el-input
            v-model="queryParams.ipAddr"
            placeholder="请输入用户IP地址"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="唯一标识" prop="deviceId">
        <el-input
            v-model="queryParams.deviceId"
            placeholder="请输入用户设备唯一标识"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="浏览器" prop="browser">
        <el-input
            v-model="queryParams.browser"
            placeholder="请输入浏览器类型"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="操作系统" prop="os">
        <el-input
            v-model="queryParams.os"
            placeholder="请输入操作系统"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="平台" prop="platform">
        <el-input
            v-model="queryParams.platform"
            placeholder="请输入平台"
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
      <el-form-item label="删除" prop="isDelete">
        <el-select v-model="queryParams.isDelete" style="width: 200px" placeholder="请选择删除" clearable>
          <el-option
              v-for="dict in common_delete"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
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
            v-hasPermi="['ai:generateLogInfo:add']"
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
            v-hasPermi="['ai:generateLogInfo:edit']"
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
            v-hasPermi="['ai:generateLogInfo:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['ai:generateLogInfo:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table ref="tableRef" v-loading="loading" :data="generateLogInfoList" @selection-change="handleSelectionChange"
              @sort-change="customSort">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="记录编号" align="center" prop="logId" v-if="columns[0].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="用户编号" align="center" prop="userId" v-if="columns[1].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="模型KEY" align="center" prop="modelKey" v-if="columns[2].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="模型类型" align="center" prop="modelType" v-if="columns[3].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="输入文件" align="center" prop="inputFile" v-if="columns[4].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="提示词" align="center" prop="prompt" v-if="columns[5].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="负向提示词" align="center" prop="negativePrompt" v-if="columns[6].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="随机种子" align="center" prop="seed" v-if="columns[7].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="数量" align="center" prop="numbers" v-if="columns[8].visible"
                       sortable="custom" :show-overflow-tooltip="true"/>
      <el-table-column label="输入参数" align="center" prop="inputParams" v-if="columns[9].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="任务编号" align="center" prop="taskId" v-if="columns[10].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="返回结果" align="center" prop="outputResult" v-if="columns[11].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="文件地址" align="center" prop="fileUrls" v-if="columns[12].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <image-preview :src="scope.row.fileUrls" width="50" height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="宽度" align="center" prop="width" v-if="columns[13].visible"
                       sortable="custom" :show-overflow-tooltip="true"/>
      <el-table-column label="高度" align="center" prop="height" v-if="columns[14].visible"
                       sortable="custom" :show-overflow-tooltip="true"/>
      <el-table-column label="请求时间" align="center" prop="requestTime" width="180" v-if="columns[15].visible"
                       sortable="custom" :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.requestTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="请求时长" align="center" prop="requestDuration" v-if="columns[16].visible"
                       sortable="custom" :show-overflow-tooltip="true"/>
      <el-table-column label="价格" align="center" prop="priceUsed" v-if="columns[17].visible"
                       sortable="custom" :show-overflow-tooltip="true"/>
      <el-table-column label="消耗的积分" align="center" prop="pointsUsed" v-if="columns[18].visible"
                       sortable="custom" :show-overflow-tooltip="true"/>
      <el-table-column label="参考对象" align="center" prop="targetId" v-if="columns[19].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="状态" align="center" prop="logStatus" v-if="columns[20].visible">
        <template #default="scope">
          <dict-tag :options="ai_log_status" :value="scope.row.logStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="模型返回码" align="center" prop="aiStatusCode" v-if="columns[21].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="失败原因" align="center" prop="failReason" v-if="columns[22].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="是否统计" align="center" prop="hasStatistics" v-if="columns[23].visible">
        <template #default="scope">
          <dict-tag :options="common_has_statistics" :value="scope.row.hasStatistics"/>
        </template>
      </el-table-column>
      <el-table-column label="用户IP地址" align="center" prop="ipAddr" v-if="columns[24].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="用户设备唯一标识" align="center" prop="deviceId" v-if="columns[25].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="浏览器类型" align="center" prop="browser" v-if="columns[26].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="操作系统" align="center" prop="os" v-if="columns[27].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="平台" align="center" prop="platform" v-if="columns[28].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" v-if="columns[29].visible"
                       sortable="custom" :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180" v-if="columns[30].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="删除" align="center" prop="isDelete" v-if="columns[31].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <dict-tag :options="common_delete" :value="scope.row.isDelete"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['ai:generateLogInfo:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['ai:generateLogInfo:remove']">删除
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

    <!-- 添加或修改用户生成记录对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="generateLogInfoRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户编号" prop="userId">
          <el-input readonly v-model="form.userId" placeholder="请输入用户编号"/>
        </el-form-item>
        <el-form-item label="模型KEY" prop="modelKey">
          <el-input readonly v-model="form.modelKey" placeholder="请输入模型KEY"/>
        </el-form-item>
        <el-form-item label="状态" prop="logStatus">
          <el-radio-group v-model="form.logStatus">
            <el-radio
                v-for="dict in ai_log_status"
                :key="dict.value"
                :value="dict.value"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="是否统计" prop="hasStatistics">
          <el-radio-group v-model="form.hasStatistics">
            <el-radio
                v-for="dict in common_has_statistics"
                :key="dict.value"
                :value="dict.value"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="删除" prop="isDelete">
          <el-radio-group v-model="form.isDelete">
            <el-radio
                v-for="dict in common_delete"
                :key="dict.value"
                :value="dict.value"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
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

<script setup name="GenerateLogInfo">
import {
  listGenerateLogInfo,
  getGenerateLogInfo,
  delGenerateLogInfo,
  addGenerateLogInfo,
  updateGenerateLogInfo
} from "@/api/ai/generateLogInfo";

const {proxy} = getCurrentInstance();
const {
  ai_log_status,
  common_has_statistics,
  common_delete
} = proxy.useDict('ai_log_status', 'common_has_statistics', 'common_delete');

const generateLogInfoList = ref([]);
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
    logId: null,
    userId: null,
    modelKey: null,
    modelType: null,
    prompt: null,
    negativePrompt: null,
    seed: null,
    numbers: null,
    taskId: null,
    width: null,
    height: null,
    requestDuration: null,
    targetId: null,
    logStatus: null,
    aiStatusCode: null,
    hasStatistics: null,
    ipAddr: null,
    deviceId: null,
    browser: null,
    os: null,
    platform: null,
    createTime: null,
    updateTime: null,
    isDelete: null
  },
  rules: {
    userId: [
      {required: true, message: "用户编号不能为空", trigger: "blur"}
    ],
    modelKey: [
      {required: true, message: "模型KEY不能为空", trigger: "blur"}
    ],
    modelType: [
      {required: true, message: "模型类型不能为空", trigger: "change"}
    ],
    prompt: [
      {required: true, message: "提示词不能为空", trigger: "blur"}
    ],
    taskId: [
      {required: true, message: "任务编号不能为空", trigger: "blur"}
    ],
    requestTime: [
      {required: true, message: "请求时间不能为空", trigger: "blur"}
    ],
    priceUsed: [
      {required: true, message: "价格不能为空", trigger: "blur"}
    ],
    pointsUsed: [
      {required: true, message: "消耗的积分不能为空", trigger: "blur"}
    ],
    logStatus: [
      {required: true, message: "状态不能为空", trigger: "change"}
    ],
    ipAddr: [
      {required: true, message: "用户IP地址不能为空", trigger: "blur"}
    ],
    createTime: [
      {required: true, message: "创建时间不能为空", trigger: "blur"}
    ],
    isDelete: [
      {required: true, message: "删除不能为空", trigger: "blur"}
    ]
  },
  //表格展示列
  columns: [
    {key: 0, label: '记录编号', visible: false},
    {key: 1, label: '用户编号', visible: true},
    {key: 2, label: '模型KEY', visible: true},
    {key: 3, label: '模型类型', visible: true},
    {key: 4, label: '输入文件', visible: true},
    {key: 5, label: '提示词', visible: true},
    {key: 6, label: '负向提示词', visible: false},
    {key: 7, label: '随机种子', visible: true},
    {key: 8, label: '数量', visible: true},
    {key: 9, label: '输入参数', visible: false},
    {key: 10, label: '任务编号', visible: true},
    {key: 11, label: '返回结果', visible: false},
    {key: 12, label: '文件地址', visible: true},
    {key: 13, label: '宽度', visible: true},
    {key: 14, label: '高度', visible: true},
    {key: 15, label: '请求时间', visible: true},
    {key: 16, label: '请求时长', visible: true},
    {key: 17, label: '价格', visible: false},
    {key: 18, label: '消耗的积分', visible: true},
    {key: 19, label: '参考对象', visible: false},
    {key: 20, label: '状态', visible: true},
    {key: 21, label: '模型返回码', visible: false},
    {key: 22, label: '失败原因', visible: false},
    {key: 23, label: '是否统计', visible: false},
    {key: 24, label: '用户IP地址', visible: false},
    {key: 25, label: '用户设备唯一标识', visible: false},
    {key: 26, label: '浏览器类型', visible: false},
    {key: 27, label: '操作系统', visible: false},
    {key: 28, label: '平台', visible: false},
    {key: 29, label: '创建时间', visible: true},
    {key: 30, label: '更新时间', visible: false},
    {key: 31, label: '删除', visible: false},
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

/** 查询用户生成记录列表 */
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
  listGenerateLogInfo(queryParams.value).then(response => {
    generateLogInfoList.value = response.rows;
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
    logId: null,
    userId: null,
    modelKey: null,
    modelType: null,
    inputFile: null,
    prompt: null,
    negativePrompt: null,
    seed: null,
    numbers: null,
    inputParams: null,
    taskId: null,
    outputResult: null,
    fileUrls: null,
    width: null,
    height: null,
    requestTime: null,
    requestDuration: null,
    priceUsed: null,
    pointsUsed: null,
    targetId: null,
    logStatus: null,
    aiStatusCode: null,
    failReason: null,
    hasStatistics: null,
    ipAddr: null,
    deviceId: null,
    browser: null,
    os: null,
    platform: null,
    createTime: null,
    updateTime: null,
    isDelete: null
  };
  proxy.resetForm("generateLogInfoRef");
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
  ids.value = selection.map(item => item.logId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加用户生成记录";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _logId = row.logId || ids.value
  getGenerateLogInfo(_logId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改用户生成记录";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["generateLogInfoRef"].validate(valid => {
    if (valid) {
      if (form.value.logId != null) {
        updateGenerateLogInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addGenerateLogInfo(form.value).then(response => {
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
  const _logIds = row.logId || ids.value;
  proxy.$modal.confirm('是否确认删除用户生成记录编号为"' + _logIds + '"的数据项？').then(function () {
    return delGenerateLogInfo(_logIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('ai/generateLogInfo/export', {
    ...queryParams.value
  }, `generateLogInfo_${new Date().getTime()}.xlsx`)
}

getList();
</script>
