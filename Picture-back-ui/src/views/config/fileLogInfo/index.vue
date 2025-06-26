<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="日志编号" prop="logId">
        <el-input
            v-model="queryParams.logId"
            placeholder="请输入日志编号"
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
      <el-form-item label="目标对象" prop="targetId">
        <el-input
            v-model="queryParams.targetId"
            placeholder="请输入目标对象"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="logStatus">
        <el-select v-model="queryParams.logStatus" style="width: 200px" placeholder="请选择状态" clearable>
          <el-option
              v-for="dict in c_file_log_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="存储类型" prop="ossType">
        <el-select v-model="queryParams.ossType" style="width: 200px" placeholder="请选择存储类型" clearable>
          <el-option
              v-for="dict in c_file_log_oss_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="日志类型" prop="logType">
        <el-select v-model="queryParams.logType" style="width: 200px" placeholder="请选择日志类型" clearable>
          <el-option
              v-for="dict in c_file_log_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="是否压缩" prop="isCompress">
        <el-select v-model="queryParams.isCompress" style="width: 200px" placeholder="请选择是否压缩" clearable>
          <el-option
              v-for="dict in c_file_log_is_compress"
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
      <el-form-item label="删除时间" style="width: 308px">
        <el-date-picker
            v-model="daterangeDeleteTime"
            value-format="YYYY-MM-DD"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="IP地址" prop="ipAddr">
        <el-input
            v-model="queryParams.ipAddr"
            placeholder="请输入IP地址"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="唯一标识" prop="deviceId">
        <el-input
            v-model="queryParams.deviceId"
            placeholder="请输入设备唯一标识"
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
      <el-form-item label="IP属地" prop="ipAddress">
        <el-input
            v-model="queryParams.ipAddress"
            placeholder="请输入IP属地"
            clearable
            @keyup.enter="handleQuery"
        />
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
            v-hasPermi="['config:fileLogInfo:add']"
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
            v-hasPermi="['config:fileLogInfo:edit']"
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
            v-hasPermi="['config:fileLogInfo:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['config:fileLogInfo:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="fileLogInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="日志编号" align="center" prop="logId" v-if="columns[0].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="用户编号" align="center" prop="userId" v-if="columns[1].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="目标对象" align="center" prop="targetId" v-if="columns[2].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="目标内容" align="center" prop="targetContent" v-if="columns[3].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="域名URL" align="center" prop="dnsUrl" v-if="columns[4].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="文件路径" align="center" prop="fileUrl" width="100" v-if="columns[5].visible">
        <template #default="scope">
          <image-preview :src="scope.row.fileUrl" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="文件类型" align="center" prop="fileType" v-if="columns[6].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="状态" align="center" prop="logStatus" v-if="columns[7].visible">
        <template #default="scope">
          <dict-tag :options="c_file_log_status" :value="scope.row.logStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="存储类型" align="center" prop="ossType" v-if="columns[8].visible">
        <template #default="scope">
          <dict-tag :options="c_file_log_oss_type" :value="scope.row.ossType"/>
        </template>
      </el-table-column>
      <el-table-column label="日志类型" align="center" prop="logType" v-if="columns[9].visible">
        <template #default="scope">
          <dict-tag :options="c_file_log_type" :value="scope.row.logType"/>
        </template>
      </el-table-column>
      <el-table-column label="是否压缩" align="center" prop="isCompress" v-if="columns[10].visible">
        <template #default="scope">
          <dict-tag :options="c_file_log_is_compress" :value="scope.row.isCompress"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" v-if="columns[11].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="删除时间" align="center" prop="deleteTime" width="180" v-if="columns[12].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.deleteTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="IP地址" align="center" prop="ipAddr" v-if="columns[13].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="设备唯一标识" align="center" prop="deviceId" v-if="columns[14].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="浏览器类型" align="center" prop="browser" v-if="columns[15].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="操作系统" align="center" prop="os" v-if="columns[16].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="平台" align="center" prop="platform" v-if="columns[17].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="IP属地" align="center" prop="ipAddress" v-if="columns[18].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['config:fileLogInfo:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['config:fileLogInfo:remove']">删除
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

    <!-- 添加或修改文件日志对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="fileLogInfoRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户编号" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户编号"/>
        </el-form-item>
        <el-form-item label="目标对象" prop="targetId">
          <el-input v-model="form.targetId" placeholder="请输入目标对象"/>
        </el-form-item>
        <el-form-item label="目标内容">
          <editor v-model="form.targetContent" :min-height="192"/>
        </el-form-item>
        <el-form-item label="域名URL" prop="dnsUrl">
          <el-input v-model="form.dnsUrl" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="文件路径" prop="fileUrl">
          <file-upload v-model="form.fileUrl"/>
        </el-form-item>
        <el-form-item label="文件类型" prop="fileType">
          <el-input v-model="form.fileType" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="状态" prop="logStatus">
          <el-radio-group v-model="form.logStatus">
            <el-radio
                v-for="dict in c_file_log_status"
                :key="dict.value"
                :value="dict.value"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="存储类型" prop="ossType">
          <el-select v-model="form.ossType" placeholder="请选择存储类型">
            <el-option
                v-for="dict in c_file_log_oss_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="日志类型" prop="logType">
          <el-select v-model="form.logType" placeholder="请选择日志类型">
            <el-option
                v-for="dict in c_file_log_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否压缩" prop="isCompress">
          <el-select v-model="form.isCompress" placeholder="请选择是否压缩">
            <el-option
                v-for="dict in c_file_log_is_compress"
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

<script setup name="FileLogInfo">
import {
  listFileLogInfo,
  getFileLogInfo,
  delFileLogInfo,
  addFileLogInfo,
  updateFileLogInfo
} from "@/api/config/fileLogInfo";
import FileUpload from "@/components/FileUpload/index.vue";

const {proxy} = getCurrentInstance();
const {
  c_file_log_is_compress,
  c_file_log_status,
  c_file_log_type,
  c_file_log_oss_type
} = proxy.useDict('c_file_log_is_compress', 'c_file_log_status', 'c_file_log_type', 'c_file_log_oss_type');

const fileLogInfoList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const daterangeCreateTime = ref([]);
const daterangeDeleteTime = ref([]);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    logId: null,
    userId: null,
    targetId: null,
    targetContent: null,
    dnsUrl: null,
    fileType: null,
    logStatus: null,
    ossType: null,
    logType: null,
    isCompress: null,
    createTime: null,
    deleteTime: null,
    ipAddr: null,
    deviceId: null,
    browser: null,
    os: null,
    platform: null,
    ipAddress: null
  },
  rules: {
    userId: [
      {required: true, message: "用户编号不能为空", trigger: "blur"}
    ],
    targetId: [
      {required: true, message: "目标对象不能为空", trigger: "blur"}
    ],
    dnsUrl: [
      {required: true, message: "域名URL不能为空", trigger: "blur"}
    ],
    fileUrl: [
      {required: true, message: "文件路径不能为空", trigger: "blur"}
    ],
    fileType: [
      {required: true, message: "文件类型不能为空", trigger: "blur"}
    ],
    logStatus: [
      {required: true, message: "状态不能为空", trigger: "change"}
    ],
    ossType: [
      {required: true, message: "存储类型不能为空", trigger: "change"}
    ],
    logType: [
      {required: true, message: "日志类型不能为空", trigger: "change"}
    ],
    isCompress: [
      {required: true, message: "是否压缩不能为空", trigger: "change"}
    ],
    createTime: [
      {required: true, message: "创建时间不能为空", trigger: "blur"}
    ],
    ipAddr: [
      {required: true, message: "IP地址不能为空", trigger: "blur"}
    ],
  },
  //表格展示列
  columns: [
    {key: 0, label: '日志编号', visible: false},
    {key: 1, label: '用户编号', visible: true},
    {key: 2, label: '目标对象', visible: false},
    {key: 3, label: '目标内容', visible: true},
    {key: 4, label: '域名URL', visible: false},
    {key: 5, label: '文件路径', visible: true},
    {key: 6, label: '文件类型', visible: true},
    {key: 7, label: '状态', visible: true},
    {key: 8, label: '存储类型', visible: true},
    {key: 9, label: '日志类型', visible: true},
    {key: 10, label: '是否压缩', visible: true},
    {key: 11, label: '创建时间', visible: true},
    {key: 12, label: '删除时间', visible: true},
    {key: 13, label: 'IP地址', visible: false},
    {key: 14, label: '设备唯一标识', visible: false},
    {key: 15, label: '浏览器类型', visible: false},
    {key: 16, label: '操作系统', visible: false},
    {key: 17, label: '平台', visible: false},
    {key: 18, label: 'IP属地', visible: false},
  ],
});

const {queryParams, form, rules, columns} = toRefs(data);

/** 查询文件日志列表 */
function getList() {
  loading.value = true;
  queryParams.value.params = {};
  if (null != daterangeCreateTime && '' != daterangeCreateTime) {
    queryParams.value.params["beginCreateTime"] = daterangeCreateTime.value[0];
    queryParams.value.params["endCreateTime"] = daterangeCreateTime.value[1];
  }
  if (null != daterangeDeleteTime && '' != daterangeDeleteTime) {
    queryParams.value.params["beginDeleteTime"] = daterangeDeleteTime.value[0];
    queryParams.value.params["endDeleteTime"] = daterangeDeleteTime.value[1];
  }
  listFileLogInfo(queryParams.value).then(response => {
    fileLogInfoList.value = response.rows;
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
    targetId: null,
    targetContent: null,
    dnsUrl: null,
    fileUrl: null,
    fileType: null,
    logStatus: null,
    ossType: null,
    logType: null,
    isCompress: null,
    createTime: null,
    deleteTime: null,
    ipAddr: null,
    deviceId: null,
    browser: null,
    os: null,
    platform: null,
    ipAddress: null
  };
  proxy.resetForm("fileLogInfoRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  daterangeCreateTime.value = [];
  daterangeDeleteTime.value = [];
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
  title.value = "添加文件日志";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _logId = row.logId || ids.value
  getFileLogInfo(_logId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改文件日志";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["fileLogInfoRef"].validate(valid => {
    if (valid) {
      if (form.value.logId != null) {
        updateFileLogInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addFileLogInfo(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除文件日志编号为"' + _logIds + '"的数据项？').then(function () {
    return delFileLogInfo(_logIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('config/fileLogInfo/export', {
    ...queryParams.value
  }, `fileLogInfo_${new Date().getTime()}.xlsx`)
}

getList();
</script>
