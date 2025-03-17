<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="通知记录" prop="recordId">
        <el-input
            v-model="queryParams.recordId"
            placeholder="请输入通知记录"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="模板" prop="templateId">
        <el-input
            v-model="queryParams.templateId"
            placeholder="请输入模板"
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
      <el-form-item label="通知类型" prop="informType">
        <el-select v-model="queryParams.informType" style="width: 200px" placeholder="请选择通知类型" clearable>
          <el-option
              v-for="dict in c_template_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="发送状态" prop="status">
        <el-select v-model="queryParams.status" style="width: 200px" placeholder="请选择发送状态" clearable>
          <el-option
              v-for="dict in u_inform_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="是否已读" prop="isRead">
        <el-select v-model="queryParams.isRead" style="width: 200px" placeholder="请选择是否已读" clearable>
          <el-option
              v-for="dict in u_inform_is_read"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="读取时间" style="width: 308px">
        <el-date-picker
            v-model="daterangeReadTime"
            value-format="YYYY-MM-DD"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="发送时间" style="width: 308px">
        <el-date-picker
            v-model="daterangeSendTime"
            value-format="YYYY-MM-DD"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="逻辑删除标记" prop="isDeleted">
        <el-select v-model="queryParams.isDeleted" style="width: 200px" placeholder="请选择逻辑删除标记" clearable>
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
            v-hasPermi="['user:informInfo:add']"
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
            v-hasPermi="['user:informInfo:edit']"
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
            v-hasPermi="['user:informInfo:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['user:informInfo:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="informInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="通知记录" align="center" prop="recordId" v-if="columns[0].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="模板" align="center" prop="templateId" v-if="columns[1].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="用户" align="center" prop="userId" v-if="columns[2].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="实际发送内容" align="center" prop="content" v-if="columns[3].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="通知类型" align="center" prop="informType" v-if="columns[4].visible">
        <template #default="scope">
          <dict-tag :options="c_template_type" :value="scope.row.informType"/>
        </template>
      </el-table-column>
      <el-table-column label="发送状态" align="center" prop="status" v-if="columns[5].visible">
        <template #default="scope">
          <dict-tag :options="u_inform_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="是否已读" align="center" prop="isRead" v-if="columns[6].visible">
        <template #default="scope">
          <dict-tag :options="u_inform_is_read" :value="scope.row.isRead"/>
        </template>
      </el-table-column>
      <el-table-column label="读取时间" align="center" prop="readTime" width="180" v-if="columns[7].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.readTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="重试次数" align="center" prop="retryCount" v-if="columns[8].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="发送时间" align="center" prop="sendTime" width="180" v-if="columns[9].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.sendTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" v-if="columns[10].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="逻辑删除标记" align="center" prop="isDeleted" v-if="columns[11].visible">
        <template #default="scope">
          <dict-tag :options="common_delete" :value="scope.row.isDeleted"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['user:informInfo:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['user:informInfo:remove']">删除
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

    <!-- 添加或修改用户通知记录对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="informInfoRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="模板" prop="templateId">
          <el-input v-model="form.templateId" placeholder="请输入模板"/>
        </el-form-item>
        <el-form-item label="用户" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户"/>
        </el-form-item>
        <el-form-item label="实际发送内容" prop="content">
          <el-input v-model="form.content" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="通知类型" prop="informType">
          <el-select v-model="form.informType" placeholder="请选择通知类型">
            <el-option
                v-for="dict in c_template_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="发送状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
                v-for="dict in u_inform_status"
                :key="dict.value"
                :value="parseInt(dict.value)"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="是否已读" prop="isRead">
          <el-radio-group v-model="form.isRead">
            <el-radio
                v-for="dict in u_inform_is_read"
                :key="dict.value"
                :value="parseInt(dict.value)"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="读取时间" prop="readTime">
          <el-date-picker clearable
                          v-model="form.readTime"
                          type="date"
                          value-format="YYYY-MM-DD"
                          placeholder="请选择读取时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="发送时间" prop="sendTime">
          <el-date-picker clearable
                          v-model="form.sendTime"
                          type="date"
                          value-format="YYYY-MM-DD"
                          placeholder="请选择发送时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="逻辑删除标记" prop="isDeleted">
          <el-select v-model="form.isDeleted" placeholder="请选择逻辑删除标记">
            <el-option
                v-for="dict in common_delete"
                :key="dict.value"
                :label="dict.label"
                :value="parseInt(dict.value)"
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

<script setup name="InformInfo">
import {listInformInfo, getInformInfo, delInformInfo, addInformInfo, updateInformInfo} from "@/api/user/informInfo";

const {proxy} = getCurrentInstance();
const {
  common_delete,
  u_inform_status,
  c_template_type,
  u_inform_is_read
} = proxy.useDict('common_delete', 'u_inform_status', 'c_template_type', 'u_inform_is_read');

const informInfoList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const daterangeReadTime = ref([]);
const daterangeSendTime = ref([]);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    recordId: null,
    templateId: null,
    userId: null,
    informType: null,
    status: null,
    isRead: null,
    readTime: null,
    sendTime: null,
    isDeleted: null
  },
  rules: {
    templateId: [
      {required: true, message: "模板不能为空", trigger: "blur"}
    ],
    userId: [
      {required: true, message: "用户不能为空", trigger: "blur"}
    ],
    content: [
      {required: true, message: "实际发送内容不能为空", trigger: "blur"}
    ],
    informType: [
      {required: true, message: "通知类型不能为空", trigger: "change"}
    ],
    status: [
      {required: true, message: "发送状态不能为空", trigger: "change"}
    ],
    isRead: [
      {required: true, message: "是否已读不能为空", trigger: "change"}
    ],
    retryCount: [
      {required: true, message: "重试次数不能为空", trigger: "blur"}
    ],
    sendTime: [
      {required: true, message: "发送时间不能为空", trigger: "blur"}
    ],
    isDeleted: [
      {required: true, message: "逻辑删除标记不能为空", trigger: "change"}
    ]
  },
  //表格展示列
  columns: [
    {key: 0, label: '通知记录', visible: true},
    {key: 1, label: '模板', visible: true},
    {key: 2, label: '用户', visible: true},
    {key: 3, label: '实际发送内容', visible: true},
    {key: 4, label: '通知类型', visible: true},
    {key: 5, label: '发送状态', visible: true},
    {key: 6, label: '是否已读', visible: true},
    {key: 7, label: '读取时间', visible: true},
    {key: 8, label: '重试次数', visible: true},
    {key: 9, label: '发送时间', visible: true},
    {key: 10, label: '备注', visible: true},
    {key: 11, label: '逻辑删除标记', visible: true},
  ],
});

const {queryParams, form, rules, columns} = toRefs(data);

/** 查询用户通知记录列表 */
function getList() {
  loading.value = true;
  queryParams.value.params = {};
  if (null != daterangeReadTime && '' != daterangeReadTime) {
    queryParams.value.params["beginReadTime"] = daterangeReadTime.value[0];
    queryParams.value.params["endReadTime"] = daterangeReadTime.value[1];
  }
  if (null != daterangeSendTime && '' != daterangeSendTime) {
    queryParams.value.params["beginSendTime"] = daterangeSendTime.value[0];
    queryParams.value.params["endSendTime"] = daterangeSendTime.value[1];
  }
  listInformInfo(queryParams.value).then(response => {
    informInfoList.value = response.rows;
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
    recordId: null,
    templateId: null,
    userId: null,
    content: null,
    informType: null,
    status: null,
    isRead: null,
    readTime: null,
    retryCount: null,
    sendTime: null,
    remark: null,
    isDeleted: null
  };
  proxy.resetForm("informInfoRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  daterangeReadTime.value = [];
  daterangeSendTime.value = [];
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.recordId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加用户通知记录";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _recordId = row.recordId || ids.value
  getInformInfo(_recordId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改用户通知记录";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["informInfoRef"].validate(valid => {
    if (valid) {
      if (form.value.recordId != null) {
        updateInformInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addInformInfo(form.value).then(response => {
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
  const _recordIds = row.recordId || ids.value;
  proxy.$modal.confirm('是否确认删除用户通知记录编号为"' + _recordIds + '"的数据项？').then(function () {
    return delInformInfo(_recordIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('user/informInfo/export', {
    ...queryParams.value
  }, `informInfo_${new Date().getTime()}.xlsx`)
}

getList();
</script>
