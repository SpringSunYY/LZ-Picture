<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="编号" prop="infoId">
        <el-input
            v-model="queryParams.infoId"
            placeholder="请输入编号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户ID" prop="userId">
        <el-input
            v-model="queryParams.userId"
            placeholder="请输入用户ID"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户名" prop="userName">
        <el-input
            v-model="queryParams.userName"
            placeholder="请输入用户名"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="登录方式" prop="loginType">
        <el-select v-model="queryParams.loginType" style="width: 200px" placeholder="请选择登录方式" clearable>
          <el-option
              v-for="dict in u_login_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="IP地址" prop="ipaddr">
        <el-input
            v-model="queryParams.ipaddr"
            placeholder="请输入登录IP地址"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="登录地点" prop="loginLocation">
        <el-input
            v-model="queryParams.loginLocation"
            placeholder="请输入登录地点"
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
      <el-form-item label="登录平台" prop="platform">
        <el-input
            v-model="queryParams.platform"
            placeholder="请输入登录平台"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" style="width: 200px" placeholder="请选择状态" clearable>
          <el-option
              v-for="dict in u_login_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="错误码" prop="errorCode">
        <el-input
            v-model="queryParams.errorCode"
            placeholder="请输入错误码"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="提示消息" prop="msg">-->
<!--        <el-input-->
<!--            v-model="queryParams.msg"-->
<!--            placeholder="请输入提示消息"-->
<!--            clearable-->
<!--            @keyup.enter="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
      <el-form-item label="登录时间" style="width: 308px">
        <el-date-picker
            v-model="daterangeLoginTime"
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
            v-hasPermi="['user:loginLogInfo:add']"
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
            v-hasPermi="['user:loginLogInfo:edit']"
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
            v-hasPermi="['user:loginLogInfo:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['user:loginLogInfo:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table ref="tableRef" v-loading="loading" :data="loginLogInfoList" @selection-change="handleSelectionChange"  @sort-change="customSort">
            <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="序号" type="index" width="50"/>
      <el-table-column label="编号" align="center" prop="infoId" v-if="columns[0].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="用户ID" align="center" prop="userId" v-if="columns[1].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="用户名" align="center" prop="userName" v-if="columns[2].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="登录方式" align="center" prop="loginType" v-if="columns[3].visible">
        <template #default="scope">
          <dict-tag :options="u_login_type" :value="scope.row.loginType"/>
        </template>
      </el-table-column>
      <el-table-column label="匿名标识" align="center" prop="identifier" v-if="columns[4].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="登录IP地址" align="center" prop="ipaddr" v-if="columns[5].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="登录地点" align="center" prop="loginLocation" v-if="columns[6].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="浏览器类型" align="center" prop="browser" v-if="columns[7].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="操作系统" align="center" prop="os" v-if="columns[8].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="登录平台" align="center" prop="platform" v-if="columns[9].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="设备唯一标识" align="center" prop="deviceId" v-if="columns[10].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="状态" align="center" prop="status" v-if="columns[11].visible">
        <template #default="scope">
          <dict-tag :options="u_login_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="错误码" align="center" prop="errorCode" v-if="columns[12].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="提示消息" align="center" prop="msg" v-if="columns[13].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="登录时间" align="center" prop="loginTime" width="180" v-if="columns[14].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.loginTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['user:loginLogInfo:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['user:loginLogInfo:remove']">删除
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

    <!-- 添加或修改用户登录日志对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="loginLogInfoRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户ID"/>
        </el-form-item>
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="form.userName" placeholder="请输入用户名"/>
        </el-form-item>
        <el-form-item label="登录方式" prop="loginType">
          <el-select v-model="form.loginType" placeholder="请选择登录方式">
            <el-option
                v-for="dict in u_login_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="匿名标识" prop="identifier">
          <el-input v-model="form.identifier" placeholder="请输入匿名标识"/>
        </el-form-item>
        <el-form-item label="登录IP地址" prop="ipaddr">
          <el-input v-model="form.ipaddr" placeholder="请输入登录IP地址"/>
        </el-form-item>
        <el-form-item label="登录地点" prop="loginLocation">
          <el-input v-model="form.loginLocation" placeholder="请输入登录地点"/>
        </el-form-item>
        <el-form-item label="浏览器类型" prop="browser">
          <el-input v-model="form.browser" placeholder="请输入浏览器类型"/>
        </el-form-item>
        <el-form-item label="操作系统" prop="os">
          <el-input v-model="form.os" placeholder="请输入操作系统"/>
        </el-form-item>
        <el-form-item label="登录平台" prop="platform">
          <el-input v-model="form.platform" placeholder="请输入登录平台"/>
        </el-form-item>
        <el-form-item label="设备唯一标识" prop="deviceId">
          <el-input v-model="form.deviceId" placeholder="请输入设备唯一标识"/>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
                v-for="dict in u_login_status"
                :key="dict.value"
                :value="dict.value"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="错误码" prop="errorCode">
          <el-input v-model="form.errorCode" placeholder="请输入错误码"/>
        </el-form-item>
        <el-form-item label="提示消息" prop="msg">
          <el-input v-model="form.msg" placeholder="请输入提示消息"/>
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

<script setup name="LoginLogInfo">
import {
  listLoginLogInfo,
  getLoginLogInfo,
  delLoginLogInfo,
  addLoginLogInfo,
  updateLoginLogInfo
} from "@/api/user/loginLogInfo";

const {proxy} = getCurrentInstance();
const {u_login_type, u_login_status} = proxy.useDict('u_login_type', 'u_login_status');

const loginLogInfoList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const daterangeLoginTime = ref([]);

const isAsc = ref();
const orderByColumn = ref('');
const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    infoId: null,
    userId: null,
    userName: null,
    loginType: null,
    ipaddr: null,
    loginLocation: null,
    browser: null,
    os: null,
    platform: null,
    status: null,
    errorCode: null,
    msg: null,
    loginTime: null
  },
  rules: {
    loginType: [
      {required: true, message: "登录方式不能为空", trigger: "change"}
    ],
    ipaddr: [
      {required: true, message: "登录IP地址不能为空", trigger: "blur"}
    ],
    status: [
      {required: true, message: "状态不能为空", trigger: "change"}
    ],
    msg: [
      {required: true, message: "提示消息不能为空", trigger: "blur"}
    ],
    loginTime: [
      {required: true, message: "登录时间不能为空", trigger: "blur"}
    ]
  },
  //表格展示列
  columns: [
    {key: 0, label: '编号', visible: false},
    {key: 1, label: '用户ID', visible: true},
    {key: 2, label: '用户名', visible: true},
    {key: 3, label: '登录方式', visible: true},
    {key: 4, label: '匿名标识', visible: false},
    {key: 5, label: '登录IP地址', visible: true},
    {key: 6, label: '登录地点', visible: true},
    {key: 7, label: '浏览器类型', visible: true},
    {key: 8, label: '操作系统', visible: true},
    {key: 9, label: '登录平台', visible: true},
    {key: 10, label: '设备唯一标识', visible: false},
    {key: 11, label: '状态', visible: true},
    {key: 12, label: '错误码', visible: false},
    {key: 13, label: '提示消息', visible: true},
    {key: 14, label: '登录时间', visible: true},
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


/** 查询用户登录日志列表 */
function getList() {
  loading.value = true;
 queryParams.value.params = {}; if (orderByColumn.value != null && isAsc.value !== null) {
    queryParams.value.params["orderByColumn"] = orderByColumn.value;
    queryParams.value.params["isAsc"] = isAsc.value;
  }
  if (null != daterangeLoginTime && '' != daterangeLoginTime) {
    queryParams.value.params["beginLoginTime"] = daterangeLoginTime.value[0];
    queryParams.value.params["endLoginTime"] = daterangeLoginTime.value[1];
  }
  listLoginLogInfo(queryParams.value).then(response => {
    loginLogInfoList.value = response.rows;
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
    infoId: null,
    userId: null,
    userName: null,
    loginType: null,
    identifier: null,
    ipaddr: null,
    loginLocation: null,
    browser: null,
    os: null,
    platform: null,
    deviceId: null,
    status: null,
    errorCode: null,
    msg: null,
    loginTime: null
  };
  proxy.resetForm("loginLogInfoRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  daterangeLoginTime.value = [];
    orderByColumn.value = null
  isAsc.value = null;
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.infoId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加用户登录日志";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _infoId = row.infoId || ids.value
  getLoginLogInfo(_infoId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改用户登录日志";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["loginLogInfoRef"].validate(valid => {
    if (valid) {
      if (form.value.infoId != null) {
        updateLoginLogInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addLoginLogInfo(form.value).then(response => {
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
  const _infoIds = row.infoId || ids.value;
  proxy.$modal.confirm('是否确认删除用户登录日志编号为"' + _infoIds + '"的数据项？').then(function () {
    return delLoginLogInfo(_infoIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('user/loginLogInfo/export', {
    ...queryParams.value
  }, `loginLogInfo_${new Date().getTime()}.xlsx`)
}

getList();
</script>
