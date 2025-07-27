<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
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
      <el-form-item label="AI会话编号" prop="conversationId">
        <el-input
            v-model="queryParams.conversationId"
            placeholder="请输入AI会话编号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="对话名称" prop="sessionName">
        <el-input
            v-model="queryParams.sessionName"
            placeholder="请输入对话名称"
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
            v-hasPermi="['ai:conversationSessionInfo:add']"
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
            v-hasPermi="['ai:conversationSessionInfo:edit']"
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
            v-hasPermi="['ai:conversationSessionInfo:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['ai:conversationSessionInfo:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table ref="tableRef" v-loading="loading" :data="conversationSessionInfoList"
              @selection-change="handleSelectionChange" @sort-change="customSort">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="序号" type="index" width="50"/>
      <el-table-column label="会话编号" align="center" prop="sessionId" v-if="columns[0].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="用户编号" align="center" prop="userId" v-if="columns[1].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="AI会话编号" align="center" prop="conversationId" v-if="columns[2].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="对话名称" align="center" prop="sessionName" v-if="columns[3].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="累计消耗Tokens" align="center" prop="tokensTotalUsed" v-if="columns[4].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="累计消耗积分" align="center" prop="pointsTotalUsed" v-if="columns[5].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="备注" align="center" prop="remark" v-if="columns[6].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="用户IP地址" align="center" prop="ipAddr" v-if="columns[7].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="设备唯一标识" align="center" prop="deviceId" v-if="columns[8].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="浏览器类型" align="center" prop="browser" v-if="columns[9].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="操作系统" align="center" prop="os" v-if="columns[10].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="平台" align="center" prop="platform" v-if="columns[11].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" v-if="columns[12].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180" v-if="columns[13].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="删除" align="center" prop="isDelete" v-if="columns[14].visible">
        <template #default="scope">
          <dict-tag :options="common_delete" :value="scope.row.isDelete"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['ai:conversationSessionInfo:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['ai:conversationSessionInfo:remove']">删除
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

    <!-- 添加或修改AI会话管理对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="conversationSessionInfoRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户编号" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户编号"/>
        </el-form-item>
        <el-form-item label="AI会话编号" prop="conversationId">
          <el-input v-model="form.conversationId" placeholder="请输入AI会话编号"/>
        </el-form-item>
        <el-form-item label="对话名称" prop="sessionName">
          <el-input v-model="form.sessionName" placeholder="请输入对话名称"/>
        </el-form-item>
        <el-form-item label="累计消耗Tokens" prop="tokensTotalUsed">
          <el-input v-model="form.tokensTotalUsed" placeholder="请输入累计消耗Tokens"/>
        </el-form-item>
        <el-form-item label="累计消耗积分" prop="pointsTotalUsed">
          <el-input v-model="form.pointsTotalUsed" placeholder="请输入累计消耗积分"/>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"/>
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

<script setup name="ConversationSessionInfo">
import {
  listConversationSessionInfo,
  getConversationSessionInfo,
  delConversationSessionInfo,
  addConversationSessionInfo,
  updateConversationSessionInfo
} from "@/api/ai/conversationSessionInfo";

const {proxy} = getCurrentInstance();
const {common_delete} = proxy.useDict('common_delete');

const conversationSessionInfoList = ref([]);
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
    sessionId: null,
    userId: null,
    conversationId: null,
    sessionName: null,
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
    tokensTotalUsed: [
      {required: true, message: "累计消耗Tokens不能为空", trigger: "blur"}
    ],
    pointsTotalUsed: [
      {required: true, message: "累计消耗积分不能为空", trigger: "blur"}
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
    {key: 0, label: '会话编号', visible: true},
    {key: 1, label: '用户编号', visible: true},
    {key: 2, label: 'AI会话编号', visible: true},
    {key: 3, label: '对话名称', visible: true},
    {key: 4, label: '累计消耗Tokens', visible: true},
    {key: 5, label: '累计消耗积分', visible: true},
    {key: 6, label: '备注', visible: true},
    {key: 7, label: '用户IP地址', visible: true},
    {key: 8, label: '设备唯一标识', visible: true},
    {key: 9, label: '浏览器类型', visible: true},
    {key: 10, label: '操作系统', visible: true},
    {key: 11, label: '平台', visible: true},
    {key: 12, label: '创建时间', visible: true},
    {key: 13, label: '更新时间', visible: true},
    {key: 14, label: '删除', visible: true},
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


/** 查询AI会话管理列表 */
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
  listConversationSessionInfo(queryParams.value).then(response => {
    conversationSessionInfoList.value = response.rows;
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
    sessionId: null,
    userId: null,
    conversationId: null,
    sessionName: null,
    tokensTotalUsed: null,
    pointsTotalUsed: null,
    remark: null,
    ipAddr: null,
    deviceId: null,
    browser: null,
    os: null,
    platform: null,
    createTime: null,
    updateTime: null,
    isDelete: null
  };
  proxy.resetForm("conversationSessionInfoRef");
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
  ids.value = selection.map(item => item.sessionId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加AI会话管理";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _sessionId = row.sessionId || ids.value
  getConversationSessionInfo(_sessionId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改AI会话管理";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["conversationSessionInfoRef"].validate(valid => {
    if (valid) {
      if (form.value.sessionId != null) {
        updateConversationSessionInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addConversationSessionInfo(form.value).then(response => {
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
  const _sessionIds = row.sessionId || ids.value;
  proxy.$modal.confirm('是否确认删除AI会话管理编号为"' + _sessionIds + '"的数据项？').then(function () {
    return delConversationSessionInfo(_sessionIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('ai/conversationSessionInfo/export', {
    ...queryParams.value
  }, `conversationSessionInfo_${new Date().getTime()}.xlsx`)
}

getList();
</script>
