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
      <el-form-item label="模型编号" prop="modelId">
        <el-input
            v-model="queryParams.modelId"
            placeholder="请输入模型编号"
            clearable
            @keyup.enter="handleQuery"
        />
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
      <el-form-item label="使用类型" prop="usageType">
        <el-select v-model="queryParams.usageType" style="width: 200px" placeholder="请选择使用类型" clearable>
          <el-option
              v-for="dict in po_points_usage_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="目标编号" prop="targetId">
        <el-input
            v-model="queryParams.targetId"
            placeholder="请输入目标编号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="logStatus">
        <el-select v-model="queryParams.logStatus" style="width: 200px" placeholder="请选择状态" clearable>
          <el-option
              v-for="dict in ai_log_usage_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="模型返回码" prop="aiStatusCode">
        <el-input
            v-model="queryParams.aiStatusCode"
            placeholder="请输入模型返回码"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户IP地址" prop="ipAddr">
        <el-input
            v-model="queryParams.ipAddr"
            placeholder="请输入用户IP地址"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备唯一标识" prop="deviceId">
        <el-input
            v-model="queryParams.deviceId"
            placeholder="请输入设备唯一标识"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="浏览器类型" prop="browser">
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
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker clearable
                        v-model="queryParams.createTime"
                        type="date"
                        value-format="YYYY-MM-DD"
                        placeholder="请选择创建时间">
        </el-date-picker>
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
            v-hasPermi="['ai:userUsageLogInfo:add']"
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
            v-hasPermi="['ai:userUsageLogInfo:edit']"
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
            v-hasPermi="['ai:userUsageLogInfo:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['ai:userUsageLogInfo:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="userUsageLogInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="序号" type="index" width="50"/>
      <el-table-column label="记录编号" align="center" prop="logId" v-if="columns[0].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="用户编号" align="center" prop="userId" v-if="columns[1].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="模型编号" align="center" prop="modelId" v-if="columns[2].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="输入参数" align="center" prop="inputParams" v-if="columns[3].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="返回结果" align="center" prop="outputResult" v-if="columns[4].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="请求时间" align="center" prop="requestTime" width="180" v-if="columns[5].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.requestTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="请求时长" align="center" prop="requestDuration" v-if="columns[6].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="消耗Tokens数量" align="center" prop="tokensUsed" v-if="columns[7].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="消耗积分" align="center" prop="pointsUsed" v-if="columns[8].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="使用类型" align="center" prop="usageType" v-if="columns[9].visible">
        <template #default="scope">
          <dict-tag :options="po_points_usage_type" :value="scope.row.usageType"/>
        </template>
      </el-table-column>
      <el-table-column label="目标编号" align="center" prop="targetId" v-if="columns[10].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="状态" align="center" prop="logStatus" v-if="columns[11].visible">
        <template #default="scope">
          <dict-tag :options="ai_log_usage_type" :value="scope.row.logStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="模型返回码" align="center" prop="aiStatusCode" v-if="columns[12].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="失败原因" align="center" prop="failReason" v-if="columns[13].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="用户IP地址" align="center" prop="ipAddr" v-if="columns[14].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="设备唯一标识" align="center" prop="deviceId" v-if="columns[15].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="浏览器类型" align="center" prop="browser" v-if="columns[16].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="操作系统" align="center" prop="os" v-if="columns[17].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="平台" align="center" prop="platform" v-if="columns[18].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" v-if="columns[19].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180" v-if="columns[20].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="删除" align="center" prop="isDelete" v-if="columns[21].visible">
        <template #default="scope">
          <dict-tag :options="common_delete" :value="scope.row.isDelete"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['ai:userUsageLogInfo:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['ai:userUsageLogInfo:remove']">删除
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

    <!-- 添加或修改用户AI使用记录对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="userUsageLogInfoRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户编号" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户编号"/>
        </el-form-item>
        <el-form-item label="模型编号" prop="modelId">
          <el-input v-model="form.modelId" placeholder="请输入模型编号"/>
        </el-form-item>
        <el-form-item label="输入参数" prop="inputParams">
          <el-input v-model="form.inputParams" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="返回结果" prop="outputResult">
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
        <el-form-item label="消耗积分" prop="pointsUsed">
          <el-input v-model="form.pointsUsed" placeholder="请输入消耗积分"/>
        </el-form-item>
        <el-form-item label="使用类型" prop="usageType">
          <el-select v-model="form.usageType" placeholder="请选择使用类型">
            <el-option
                v-for="dict in po_points_usage_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="目标编号" prop="targetId">
          <el-input v-model="form.targetId" placeholder="请输入目标编号"/>
        </el-form-item>
        <el-form-item label="状态" prop="logStatus">
          <el-radio-group v-model="form.logStatus">
            <el-radio
                v-for="dict in ai_log_usage_type"
                :key="dict.value"
                :value="dict.value"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="模型返回码" prop="aiStatusCode">
          <el-input v-model="form.aiStatusCode" placeholder="请输入模型返回码"/>
        </el-form-item>
        <el-form-item label="失败原因" prop="failReason">
          <el-input v-model="form.failReason" placeholder="请输入失败原因"/>
        </el-form-item>
        <el-form-item label="用户IP地址" prop="ipAddr">
          <el-input v-model="form.ipAddr" placeholder="请输入用户IP地址"/>
        </el-form-item>
        <el-form-item label="设备唯一标识" prop="deviceId">
          <el-input v-model="form.deviceId" placeholder="请输入设备唯一标识"/>
        </el-form-item>
        <el-form-item label="浏览器类型" prop="browser">
          <el-input v-model="form.browser" placeholder="请输入浏览器类型"/>
        </el-form-item>
        <el-form-item label="操作系统" prop="os">
          <el-input v-model="form.os" placeholder="请输入操作系统"/>
        </el-form-item>
        <el-form-item label="平台" prop="platform">
          <el-input v-model="form.platform" placeholder="请输入平台"/>
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

<script setup name="UserUsageLogInfo">
import {
  listUserUsageLogInfo,
  getUserUsageLogInfo,
  delUserUsageLogInfo,
  addUserUsageLogInfo,
  updateUserUsageLogInfo
} from "@/api/ai/userUsageLogInfo";

const {proxy} = getCurrentInstance();
const {
  common_delete,
  po_points_usage_type,
  ai_log_usage_type
} = proxy.useDict('common_delete', 'po_points_usage_type', 'ai_log_usage_type');

const userUsageLogInfoList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const daterangeRequestTime = ref([]);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    logId: null,
    userId: null,
    modelId: null,
    requestTime: null,
    usageType: null,
    targetId: null,
    logStatus: null,
    aiStatusCode: null,
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
    modelId: [
      {required: true, message: "模型编号不能为空", trigger: "blur"}
    ],
    requestTime: [
      {required: true, message: "请求时间不能为空", trigger: "blur"}
    ],
    tokensUsed: [
      {required: true, message: "消耗Tokens数量不能为空", trigger: "blur"}
    ],
    pointsUsed: [
      {required: true, message: "消耗积分不能为空", trigger: "blur"}
    ],
    usageType: [
      {required: true, message: "使用类型不能为空", trigger: "change"}
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
    {key: 1, label: '用户编号', visible: true},
    {key: 2, label: '模型编号', visible: true},
    {key: 3, label: '输入参数', visible: true},
    {key: 4, label: '返回结果', visible: true},
    {key: 5, label: '请求时间', visible: true},
    {key: 6, label: '请求时长', visible: true},
    {key: 7, label: '消耗Tokens数量', visible: true},
    {key: 8, label: '消耗积分', visible: true},
    {key: 9, label: '使用类型', visible: true},
    {key: 10, label: '目标编号', visible: true},
    {key: 11, label: '状态', visible: true},
    {key: 12, label: '模型返回码', visible: true},
    {key: 13, label: '失败原因', visible: true},
    {key: 14, label: '用户IP地址', visible: true},
    {key: 15, label: '设备唯一标识', visible: true},
    {key: 16, label: '浏览器类型', visible: true},
    {key: 17, label: '操作系统', visible: true},
    {key: 18, label: '平台', visible: true},
    {key: 19, label: '创建时间', visible: true},
    {key: 20, label: '更新时间', visible: true},
    {key: 21, label: '删除', visible: true},
  ],
});

const {queryParams, form, rules, columns} = toRefs(data);

/** 查询用户AI使用记录列表 */
function getList() {
  loading.value = true;
  queryParams.value.params = {};
  if (null != daterangeRequestTime && '' != daterangeRequestTime) {
    queryParams.value.params["beginRequestTime"] = daterangeRequestTime.value[0];
    queryParams.value.params["endRequestTime"] = daterangeRequestTime.value[1];
  }
  listUserUsageLogInfo(queryParams.value).then(response => {
    userUsageLogInfoList.value = response.rows;
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
    inputParams: null,
    outputResult: null,
    requestTime: null,
    requestDuration: null,
    tokensUsed: null,
    pointsUsed: null,
    usageType: null,
    targetId: null,
    logStatus: null,
    aiStatusCode: null,
    failReason: null,
    ipAddr: null,
    deviceId: null,
    browser: null,
    os: null,
    platform: null,
    createTime: null,
    updateTime: null,
    isDelete: null
  };
  proxy.resetForm("userUsageLogInfoRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  daterangeRequestTime.value = [];
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
  title.value = "添加用户AI使用记录";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _logId = row.logId || ids.value
  getUserUsageLogInfo(_logId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改用户AI使用记录";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["userUsageLogInfoRef"].validate(valid => {
    if (valid) {
      if (form.value.logId != null) {
        updateUserUsageLogInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addUserUsageLogInfo(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除用户AI使用记录编号为"' + _logIds + '"的数据项？').then(function () {
    return delUserUsageLogInfo(_logIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('ai/userUsageLogInfo/export', {
    ...queryParams.value
  }, `userUsageLogInfo_${new Date().getTime()}.xlsx`)
}

getList();
</script>
