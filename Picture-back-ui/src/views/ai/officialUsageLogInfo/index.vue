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
      <el-form-item label="管理员编号" prop="userId">
        <el-input
            v-model="queryParams.userId"
            placeholder="请输入管理员编号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="模型编号" prop="modelId">
        <el-input
            v-model="queryParams.modelId"
            placeholder="请输入模型编号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="操作类型" prop="operationType">
        <el-select v-model="queryParams.operationType" style="width: 200px" placeholder="请选择操作类型" clearable>
          <el-option
              v-for="dict in ai_official_usage_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="请求时间" style="width: 308px">
        <el-date-picker
            v-model="daterangeRequestTime"
            value-format="YYYY-MM-DD"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
        ></el-date-picker>
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
      <el-form-item label="模型返回状态码" prop="aiStatusCode">
        <el-input
            v-model="queryParams.aiStatusCode"
            placeholder="请输入模型返回状态码"
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
      <el-form-item label="更新时间" prop="updateTime">
        <el-date-picker clearable
                        v-model="queryParams.updateTime"
                        type="date"
                        value-format="YYYY-MM-DD"
                        placeholder="请选择更新时间">
        </el-date-picker>
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
            v-hasPermi="['ai:officialUsageLogInfo:add']"
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
            v-hasPermi="['ai:officialUsageLogInfo:edit']"
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
            v-hasPermi="['ai:officialUsageLogInfo:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['ai:officialUsageLogInfo:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="officialUsageLogInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="序号" type="index" width="50"/>
      <el-table-column label="记录编号" align="center" prop="logId" v-if="columns[0].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="管理员编号" align="center" prop="userId" v-if="columns[1].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="模型编号" align="center" prop="modelId" v-if="columns[2].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="操作类型" align="center" prop="operationType" v-if="columns[3].visible">
        <template #default="scope">
          <dict-tag :options="ai_official_usage_type" :value="scope.row.operationType"/>
        </template>
      </el-table-column>
      <el-table-column label="输入参数" align="center" prop="inputParams" v-if="columns[4].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="模型返回结果" align="center" prop="outputResult" v-if="columns[5].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="请求时间" align="center" prop="requestTime" width="180" v-if="columns[6].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.requestTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="请求时长" align="center" prop="requestDuration" v-if="columns[7].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="消耗Tokens数量" align="center" prop="tokensUsed" v-if="columns[8].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="状态" align="center" prop="logStatus" v-if="columns[9].visible">
        <template #default="scope">
          <dict-tag :options="ai_log_status" :value="scope.row.logStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="模型返回状态码" align="center" prop="aiStatusCode" v-if="columns[10].visible">
        <template #default="scope">
          <dict-tag :options="ai_model_params_status" :value="scope.row.aiStatusCode"/>
        </template>
      </el-table-column>
      <el-table-column label="失败原因" align="center" prop="failReason" v-if="columns[11].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="备注" align="center" prop="remark" v-if="columns[12].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" v-if="columns[13].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180" v-if="columns[14].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="删除" align="center" prop="isDelete" v-if="columns[15].visible">
        <template #default="scope">
          <dict-tag :options="common_delete" :value="scope.row.isDelete"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['ai:officialUsageLogInfo:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['ai:officialUsageLogInfo:remove']">删除
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

    <!-- 添加或修改官方AI操作日志对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="officialUsageLogInfoRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="管理员编号" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入管理员编号"/>
        </el-form-item>
        <el-form-item label="模型编号" prop="modelId">
          <el-input v-model="form.modelId" placeholder="请输入模型编号"/>
        </el-form-item>
        <el-form-item label="操作类型" prop="operationType">
          <el-select v-model="form.operationType" placeholder="请选择操作类型">
            <el-option
                v-for="dict in ai_official_usage_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="输入参数" prop="inputParams">
          <el-input v-model="form.inputParams" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="模型返回结果" prop="outputResult">
          <el-input v-model="form.outputResult" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="请求时间" prop="requestTime">
          <el-date-picker clearable
                          v-model="form.requestTime"
                          type="date"
                          value-format="YYYY-MM-DD"
                          placeholder="请选择请求时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="请求时长" prop="requestDuration">
          <el-input v-model="form.requestDuration" placeholder="请输入请求时长"/>
        </el-form-item>
        <el-form-item label="消耗Tokens数量" prop="tokensUsed">
          <el-input v-model="form.tokensUsed" placeholder="请输入消耗Tokens数量"/>
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
        <el-form-item label="模型返回状态码" prop="aiStatusCode">
          <el-input v-model="form.aiStatusCode" placeholder="请输入模型返回状态码"/>
        </el-form-item>
        <el-form-item label="失败原因" prop="failReason">
          <el-input v-model="form.failReason" placeholder="请输入失败原因"/>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"/>
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

<script setup name="OfficialUsageLogInfo">
import {
  listOfficialUsageLogInfo,
  getOfficialUsageLogInfo,
  delOfficialUsageLogInfo,
  addOfficialUsageLogInfo,
  updateOfficialUsageLogInfo
} from "@/api/ai/officialUsageLogInfo";

const {proxy} = getCurrentInstance();
const {
  ai_official_usage_type,
  common_delete,
  ai_log_status
} = proxy.useDict('ai_official_usage_type', 'common_delete', 'ai_log_status');

const officialUsageLogInfoList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const daterangeRequestTime = ref([]);
const daterangeCreateTime = ref([]);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    logId: null,
    userId: null,
    modelId: null,
    operationType: null,
    inputParams: null,
    outputResult: null,
    requestTime: null,
    logStatus: null,
    aiStatusCode: null,
    createTime: null,
    updateTime: null,
    isDelete: null
  },
  rules: {
    modelId: [
      {required: true, message: "模型编号不能为空", trigger: "blur"}
    ],
    operationType: [
      {required: true, message: "操作类型不能为空", trigger: "change"}
    ],
    requestTime: [
      {required: true, message: "请求时间不能为空", trigger: "blur"}
    ],
    tokensUsed: [
      {required: true, message: "消耗Tokens数量不能为空", trigger: "blur"}
    ],
    logStatus: [
      {required: true, message: "状态不能为空", trigger: "change"}
    ],
    createTime: [
      {required: true, message: "创建时间不能为空", trigger: "blur"}
    ],
    updateTime: [
      {required: true, message: "更新时间不能为空", trigger: "blur"}
    ],
    isDelete: [
      {required: true, message: "删除不能为空", trigger: "change"}
    ]
  },
  //表格展示列
  columns: [
    {key: 0, label: '记录编号', visible: true},
    {key: 1, label: '管理员编号', visible: true},
    {key: 2, label: '模型编号', visible: true},
    {key: 3, label: '操作类型', visible: true},
    {key: 4, label: '输入参数', visible: true},
    {key: 5, label: '模型返回结果', visible: true},
    {key: 6, label: '请求时间', visible: true},
    {key: 7, label: '请求时长', visible: true},
    {key: 8, label: '消耗Tokens数量', visible: true},
    {key: 9, label: '状态', visible: true},
    {key: 10, label: '模型返回状态码', visible: true},
    {key: 11, label: '失败原因', visible: true},
    {key: 12, label: '备注', visible: true},
    {key: 13, label: '创建时间', visible: true},
    {key: 14, label: '更新时间', visible: true},
    {key: 15, label: '删除', visible: true},
  ],
});

const {queryParams, form, rules, columns} = toRefs(data);

/** 查询官方AI操作日志列表 */
function getList() {
  loading.value = true;
  queryParams.value.params = {};
  if (null != daterangeRequestTime && '' != daterangeRequestTime) {
    queryParams.value.params["beginRequestTime"] = daterangeRequestTime.value[0];
    queryParams.value.params["endRequestTime"] = daterangeRequestTime.value[1];
  }
  if (null != daterangeCreateTime && '' != daterangeCreateTime) {
    queryParams.value.params["beginCreateTime"] = daterangeCreateTime.value[0];
    queryParams.value.params["endCreateTime"] = daterangeCreateTime.value[1];
  }
  listOfficialUsageLogInfo(queryParams.value).then(response => {
    officialUsageLogInfoList.value = response.rows;
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
    modelId: null,
    operationType: null,
    inputParams: null,
    outputResult: null,
    requestTime: null,
    requestDuration: null,
    tokensUsed: null,
    logStatus: null,
    aiStatusCode: null,
    failReason: null,
    remark: null,
    createTime: null,
    updateTime: null,
    isDelete: null
  };
  proxy.resetForm("officialUsageLogInfoRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  daterangeRequestTime.value = [];
  daterangeCreateTime.value = [];
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
  title.value = "添加官方AI操作日志";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _logId = row.logId || ids.value
  getOfficialUsageLogInfo(_logId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改官方AI操作日志";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["officialUsageLogInfoRef"].validate(valid => {
    if (valid) {
      if (form.value.logId != null) {
        updateOfficialUsageLogInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addOfficialUsageLogInfo(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除官方AI操作日志编号为"' + _logIds + '"的数据项？').then(function () {
    return delOfficialUsageLogInfo(_logIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('ai/officialUsageLogInfo/export', {
    ...queryParams.value
  }, `officialUsageLogInfo_${new Date().getTime()}.xlsx`)
}

getList();
</script>
