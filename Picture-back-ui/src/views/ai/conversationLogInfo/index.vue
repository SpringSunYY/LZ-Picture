<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="对话记录编号" prop="conversationId">
        <el-input
            v-model="queryParams.conversationId"
            placeholder="请输入对话记录编号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="会话编号" prop="sessionId">
        <el-input
            v-model="queryParams.sessionId"
            placeholder="请输入会话编号"
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
      <el-form-item label="响应时间" style="width: 308px">
        <el-date-picker
            v-model="daterangeResponseTime"
            value-format="YYYY-MM-DD"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="状态" prop="conversationStatus">
        <el-select v-model="queryParams.conversationStatus" style="width: 200px" placeholder="请选择状态" clearable>
          <el-option
              v-for="dict in ai_conversation_status"
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
      <el-form-item label="对话类型" prop="conversationType">
        <el-select v-model="queryParams.conversationType" style="width: 200px" placeholder="请选择对话类型" clearable>
          <el-option
              v-for="dict in ai_conversation_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
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
            v-hasPermi="['ai:conversationLogInfo:add']"
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
            v-hasPermi="['ai:conversationLogInfo:edit']"
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
            v-hasPermi="['ai:conversationLogInfo:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['ai:conversationLogInfo:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="conversationLogInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="序号" type="index" width="50"/>
      <el-table-column label="对话记录编号" align="center" prop="conversationId" v-if="columns[0].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="会话编号" align="center" prop="sessionId" v-if="columns[1].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="用户编号" align="center" prop="userId" v-if="columns[2].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="用户输入文本" align="center" prop="inputText" v-if="columns[3].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="AI返回文本" align="center" prop="outputText" v-if="columns[4].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="请求时间" align="center" prop="requestTime" width="180" v-if="columns[5].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.requestTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="响应时间" align="center" prop="responseTime" width="180" v-if="columns[6].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.responseTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="消耗Tokens数量" align="center" prop="tokensUsed" v-if="columns[7].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="消耗积分" align="center" prop="pointsUsed" v-if="columns[8].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="状态" align="center" prop="conversationStatus" v-if="columns[9].visible">
        <template #default="scope">
          <dict-tag :options="ai_conversation_status" :value="scope.row.conversationStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="模型返回码" align="center" prop="aiStatusCode" v-if="columns[10].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="失败原因" align="center" prop="failReason" v-if="columns[11].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="对话类型" align="center" prop="conversationType" v-if="columns[12].visible">
        <template #default="scope">
          <dict-tag :options="ai_conversation_type" :value="scope.row.conversationType"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" v-if="columns[13].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['ai:conversationLogInfo:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['ai:conversationLogInfo:remove']">删除
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

    <!-- 添加或修改AI对话明细记录对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="conversationLogInfoRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="会话编号" prop="sessionId">
          <el-input v-model="form.sessionId" placeholder="请输入会话编号"/>
        </el-form-item>
        <el-form-item label="用户编号" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户编号"/>
        </el-form-item>
        <el-form-item label="用户输入文本" prop="inputText">
          <el-input v-model="form.inputText" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="AI返回文本" prop="outputText">
          <el-input v-model="form.outputText" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="请求时间" prop="requestTime">
          <el-date-picker clearable
                          v-model="form.requestTime"
                          type="date"
                          value-format="YYYY-MM-DD"
                          placeholder="请选择请求时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="响应时间" prop="responseTime">
          <el-date-picker clearable
                          v-model="form.responseTime"
                          type="date"
                          value-format="YYYY-MM-DD"
                          placeholder="请选择响应时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="消耗Tokens数量" prop="tokensUsed">
          <el-input v-model="form.tokensUsed" placeholder="请输入消耗Tokens数量"/>
        </el-form-item>
        <el-form-item label="消耗积分" prop="pointsUsed">
          <el-input v-model="form.pointsUsed" placeholder="请输入消耗积分"/>
        </el-form-item>
        <el-form-item label="状态" prop="conversationStatus">
          <el-radio-group v-model="form.conversationStatus">
            <el-radio
                v-for="dict in ai_conversation_status"
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
        <el-form-item label="对话类型" prop="conversationType">
          <el-select v-model="form.conversationType" placeholder="请选择对话类型">
            <el-option
                v-for="dict in ai_conversation_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            ></el-option>
          </el-select>
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

<script setup name="ConversationLogInfo">
import {
  listConversationLogInfo,
  getConversationLogInfo,
  delConversationLogInfo,
  addConversationLogInfo,
  updateConversationLogInfo
} from "@/api/ai/conversationLogInfo";

const {proxy} = getCurrentInstance();
const {ai_conversation_type, ai_conversation_status} = proxy.useDict('ai_conversation_type', 'ai_conversation_status');

const conversationLogInfoList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const daterangeRequestTime = ref([]);
const daterangeResponseTime = ref([]);
const daterangeCreateTime = ref([]);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    conversationId: null,
    sessionId: null,
    userId: null,
    requestTime: null,
    responseTime: null,
    conversationStatus: null,
    aiStatusCode: null,
    conversationType: null,
    createTime: null
  },
  rules: {
    sessionId: [
      {required: true, message: "会话编号不能为空", trigger: "blur"}
    ],
    userId: [
      {required: true, message: "用户编号不能为空", trigger: "blur"}
    ],
    inputText: [
      {required: true, message: "用户输入文本不能为空", trigger: "blur"}
    ],
    outputText: [
      {required: true, message: "AI返回文本不能为空", trigger: "blur"}
    ],
    requestTime: [
      {required: true, message: "请求时间不能为空", trigger: "blur"}
    ],
    responseTime: [
      {required: true, message: "响应时间不能为空", trigger: "blur"}
    ],
    tokensUsed: [
      {required: true, message: "消耗Tokens数量不能为空", trigger: "blur"}
    ],
    pointsUsed: [
      {required: true, message: "消耗积分不能为空", trigger: "blur"}
    ],
    conversationStatus: [
      {required: true, message: "状态不能为空", trigger: "change"}
    ],
    conversationType: [
      {required: true, message: "对话类型不能为空", trigger: "change"}
    ],
    createTime: [
      {required: true, message: "创建时间不能为空", trigger: "blur"}
    ]
  },
  //表格展示列
  columns: [
    {key: 0, label: '对话记录编号', visible: true},
    {key: 1, label: '会话编号', visible: true},
    {key: 2, label: '用户编号', visible: true},
    {key: 3, label: '用户输入文本', visible: true},
    {key: 4, label: 'AI返回文本', visible: true},
    {key: 5, label: '请求时间', visible: true},
    {key: 6, label: '响应时间', visible: true},
    {key: 7, label: '消耗Tokens数量', visible: true},
    {key: 8, label: '消耗积分', visible: true},
    {key: 9, label: '状态', visible: true},
    {key: 10, label: '模型返回码', visible: true},
    {key: 11, label: '失败原因', visible: true},
    {key: 12, label: '对话类型', visible: true},
    {key: 13, label: '创建时间', visible: true},
  ],
});

const {queryParams, form, rules, columns} = toRefs(data);

/** 查询AI对话明细记录列表 */
function getList() {
  loading.value = true;
  queryParams.value.params = {};
  if (null != daterangeRequestTime && '' != daterangeRequestTime) {
    queryParams.value.params["beginRequestTime"] = daterangeRequestTime.value[0];
    queryParams.value.params["endRequestTime"] = daterangeRequestTime.value[1];
  }
  if (null != daterangeResponseTime && '' != daterangeResponseTime) {
    queryParams.value.params["beginResponseTime"] = daterangeResponseTime.value[0];
    queryParams.value.params["endResponseTime"] = daterangeResponseTime.value[1];
  }
  if (null != daterangeCreateTime && '' != daterangeCreateTime) {
    queryParams.value.params["beginCreateTime"] = daterangeCreateTime.value[0];
    queryParams.value.params["endCreateTime"] = daterangeCreateTime.value[1];
  }
  listConversationLogInfo(queryParams.value).then(response => {
    conversationLogInfoList.value = response.rows;
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
    conversationId: null,
    sessionId: null,
    userId: null,
    inputText: null,
    outputText: null,
    requestTime: null,
    responseTime: null,
    tokensUsed: null,
    pointsUsed: null,
    conversationStatus: null,
    aiStatusCode: null,
    failReason: null,
    conversationType: null,
    createTime: null
  };
  proxy.resetForm("conversationLogInfoRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  daterangeRequestTime.value = [];
  daterangeResponseTime.value = [];
  daterangeCreateTime.value = [];
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.conversationId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加AI对话明细记录";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _conversationId = row.conversationId || ids.value
  getConversationLogInfo(_conversationId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改AI对话明细记录";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["conversationLogInfoRef"].validate(valid => {
    if (valid) {
      if (form.value.conversationId != null) {
        updateConversationLogInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addConversationLogInfo(form.value).then(response => {
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
  const _conversationIds = row.conversationId || ids.value;
  proxy.$modal.confirm('是否确认删除AI对话明细记录编号为"' + _conversationIds + '"的数据项？').then(function () {
    return delConversationLogInfo(_conversationIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('ai/conversationLogInfo/export', {
    ...queryParams.value
  }, `conversationLogInfo_${new Date().getTime()}.xlsx`)
}

getList();
</script>
