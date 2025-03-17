<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="封禁记录编号" prop="bannedId">
        <el-input
            v-model="queryParams.bannedId"
            placeholder="请输入封禁记录编号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="权限名称" prop="permissionName">
        <el-input
            v-model="queryParams.permissionName"
            placeholder="请输入权限名称"
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
      <el-form-item label="开始时间" style="width: 308px">
        <el-date-picker
            v-model="daterangeStartTime"
            value-format="YYYY-MM-DD"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="结束时间" style="width: 308px">
        <el-date-picker
            v-model="daterangeEndTime"
            value-format="YYYY-MM-DD"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" style="width: 200px" placeholder="请选择状态" clearable>
          <el-option
              v-for="dict in u_banned_permission_status"
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
            v-hasPermi="['user:bannedPermissionInfo:add']"
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
            v-hasPermi="['user:bannedPermissionInfo:edit']"
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
            v-hasPermi="['user:bannedPermissionInfo:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['user:bannedPermissionInfo:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="bannedPermissionInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="封禁记录编号" align="center" prop="bannedId" v-if="columns[0].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="权限名称" align="center" prop="permissionName" v-if="columns[1].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="用户" align="center" prop="userId" v-if="columns[2].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="开始时间" align="center" prop="startTime" width="180" v-if="columns[3].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.startTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endTime" width="180" v-if="columns[4].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" v-if="columns[5].visible">
        <template #default="scope">
          <dict-tag :options="u_banned_permission_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="封禁原因" align="center" prop="cause" v-if="columns[6].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['user:bannedPermissionInfo:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['user:bannedPermissionInfo:remove']">删除
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

    <!-- 添加或修改用户封禁权限对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="bannedPermissionInfoRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="权限名称" prop="permissionName">
          <el-input v-model="form.permissionName" placeholder="请输入权限名称"/>
        </el-form-item>
        <el-form-item label="用户" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户"/>
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker clearable
                          v-model="form.startTime"
                          type="date"
                          value-format="YYYY-MM-DD"
                          placeholder="请选择开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker clearable
                          v-model="form.endTime"
                          type="date"
                          value-format="YYYY-MM-DD"
                          placeholder="请选择结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
                v-for="dict in u_banned_permission_status"
                :key="dict.value"
                :value="dict.value"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="封禁原因" prop="cause">
          <el-input v-model="form.cause" type="textarea" placeholder="请输入内容"/>
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

<script setup name="BannedPermissionInfo">
import {
  listBannedPermissionInfo,
  getBannedPermissionInfo,
  delBannedPermissionInfo,
  addBannedPermissionInfo,
  updateBannedPermissionInfo
} from "@/api/user/bannedPermissionInfo";

const {proxy} = getCurrentInstance();
const {u_banned_permission_status} = proxy.useDict('u_banned_permission_status');

const bannedPermissionInfoList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const daterangeStartTime = ref([]);
const daterangeEndTime = ref([]);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    bannedId: null,
    permissionName: null,
    userId: null,
    startTime: null,
    endTime: null,
    status: null,
  },
  rules: {
    permissionName: [
      {required: true, message: "权限名称不能为空", trigger: "blur"}
    ],
    userId: [
      {required: true, message: "用户不能为空", trigger: "blur"}
    ],
    startTime: [
      {required: true, message: "开始时间不能为空", trigger: "blur"}
    ],
    endTime: [
      {required: true, message: "结束时间不能为空", trigger: "blur"}
    ],
    status: [
      {required: true, message: "状态不能为空", trigger: "change"}
    ],
  },
  //表格展示列
  columns: [
    {key: 0, label: '封禁记录编号', visible: true},
    {key: 1, label: '权限名称', visible: true},
    {key: 2, label: '用户', visible: true},
    {key: 3, label: '开始时间', visible: true},
    {key: 4, label: '结束时间', visible: true},
    {key: 5, label: '状态', visible: true},
    {key: 6, label: '封禁原因', visible: true},
  ],
});

const {queryParams, form, rules, columns} = toRefs(data);

/** 查询用户封禁权限列表 */
function getList() {
  loading.value = true;
  queryParams.value.params = {};
  if (null != daterangeStartTime && '' != daterangeStartTime) {
    queryParams.value.params["beginStartTime"] = daterangeStartTime.value[0];
    queryParams.value.params["endStartTime"] = daterangeStartTime.value[1];
  }
  if (null != daterangeEndTime && '' != daterangeEndTime) {
    queryParams.value.params["beginEndTime"] = daterangeEndTime.value[0];
    queryParams.value.params["endEndTime"] = daterangeEndTime.value[1];
  }
  listBannedPermissionInfo(queryParams.value).then(response => {
    bannedPermissionInfoList.value = response.rows;
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
    bannedId: null,
    permissionName: null,
    userId: null,
    startTime: null,
    endTime: null,
    status: null,
    cause: null
  };
  proxy.resetForm("bannedPermissionInfoRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  daterangeStartTime.value = [];
  daterangeEndTime.value = [];
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.bannedId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加用户封禁权限";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _bannedId = row.bannedId || ids.value
  getBannedPermissionInfo(_bannedId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改用户封禁权限";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["bannedPermissionInfoRef"].validate(valid => {
    if (valid) {
      if (form.value.bannedId != null) {
        updateBannedPermissionInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addBannedPermissionInfo(form.value).then(response => {
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
  const _bannedIds = row.bannedId || ids.value;
  proxy.$modal.confirm('是否确认删除用户封禁权限编号为"' + _bannedIds + '"的数据项？').then(function () {
    return delBannedPermissionInfo(_bannedIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('user/bannedPermissionInfo/export', {
    ...queryParams.value
  }, `bannedPermissionInfo_${new Date().getTime()}.xlsx`)
}

getList();
</script>
