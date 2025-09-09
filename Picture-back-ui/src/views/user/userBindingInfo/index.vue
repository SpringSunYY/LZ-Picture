<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="绑定ID" prop="bindingId">
        <el-input
            v-model="queryParams.bindingId"
            placeholder="请输入绑定ID"
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
      <el-form-item label="绑定类型" prop="bindingType">
        <el-select v-model="queryParams.bindingType" style="width: 200px" placeholder="请选择绑定类型" clearable>
          <el-option
              v-for="dict in u_binding_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="绑定时间" style="width: 308px">
        <el-date-picker
            v-model="daterangeBindingTime"
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
            v-hasPermi="['user:userBindingInfo:add']"
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
            v-hasPermi="['user:userBindingInfo:edit']"
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
            v-hasPermi="['user:userBindingInfo:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['user:userBindingInfo:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table ref="tableRef" v-loading="loading" :data="userBindingInfoList" @selection-change="handleSelectionChange"
              @sort-change="customSort">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="序号" type="index" width="50"/>
      <el-table-column label="绑定ID" align="center" prop="bindingId" v-if="columns[0].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="用户ID" align="center" prop="userId" v-if="columns[1].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="绑定类型" align="center" prop="bindingType" v-if="columns[2].visible">
        <template #default="scope">
          <dict-tag :options="u_binding_type" :value="scope.row.bindingType"/>
        </template>
      </el-table-column>
      <el-table-column label="第三方唯一标识" align="center" prop="identifier" v-if="columns[3].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="扩展配置" align="center" prop="extendConfig" v-if="columns[4].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="绑定时间" align="center" prop="bindingTime" width="180" v-if="columns[5].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.bindingTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['user:userBindingInfo:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['user:userBindingInfo:remove']">删除
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

    <!-- 添加或修改用户第三方账号绑定对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="userBindingInfoRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户ID"/>
        </el-form-item>
        <el-form-item label="绑定类型" prop="bindingType">
          <el-select v-model="form.bindingType" placeholder="请选择绑定类型">
            <el-option
                v-for="dict in u_binding_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="第三方唯一标识" prop="identifier">
          <el-input v-model="form.identifier" placeholder="请输入第三方唯一标识"/>
        </el-form-item>
        <el-form-item label="扩展配置" prop="extendConfig">
          <el-input v-model="form.extendConfig" type="textarea" placeholder="请输入内容"/>
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

<script setup name="UserBindingInfo">
import {
  listUserBindingInfo,
  getUserBindingInfo,
  delUserBindingInfo,
  addUserBindingInfo,
  updateUserBindingInfo
} from "@/api/user/userBindingInfo";

const {proxy} = getCurrentInstance();
const {u_binding_type} = proxy.useDict('u_binding_type');

const userBindingInfoList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const daterangeBindingTime = ref([]);

const isAsc = ref();
const orderByColumn = ref('');
const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    bindingId: null,
    userId: null,
    bindingType: null,
    bindingTime: null
  },
  rules: {
    userId: [
      {required: true, message: "用户ID不能为空", trigger: "blur"}
    ],
    identifier: [
      {required: true, message: "第三方唯一标识不能为空", trigger: "blur"}
    ],
    bindingTime: [
      {required: true, message: "绑定时间不能为空", trigger: "blur"}
    ]
  },
  //表格展示列
  columns: [
    {key: 0, label: '绑定ID', visible: true},
    {key: 1, label: '用户ID', visible: true},
    {key: 2, label: '绑定类型', visible: true},
    {key: 3, label: '第三方唯一标识', visible: true},
    {key: 4, label: '扩展配置', visible: true},
    {key: 5, label: '绑定时间', visible: true},
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


/** 查询用户第三方账号绑定列表 */
function getList() {
  loading.value = true;
  queryParams.value.params = {};
  if (orderByColumn.value != null && isAsc.value !== null) {
    queryParams.value.params["orderByColumn"] = orderByColumn.value;
    queryParams.value.params["isAsc"] = isAsc.value;
  }
  if (null != daterangeBindingTime && '' != daterangeBindingTime) {
    queryParams.value.params["beginBindingTime"] = daterangeBindingTime.value[0];
    queryParams.value.params["endBindingTime"] = daterangeBindingTime.value[1];
  }
  listUserBindingInfo(queryParams.value).then(response => {
    userBindingInfoList.value = response.rows;
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
    bindingId: null,
    userId: null,
    bindingType: null,
    identifier: null,
    extendConfig: null,
    bindingTime: null
  };
  proxy.resetForm("userBindingInfoRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  daterangeBindingTime.value = [];
  orderByColumn.value = null
  isAsc.value = null;
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.bindingId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加用户第三方账号绑定";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _bindingId = row.bindingId || ids.value
  getUserBindingInfo(_bindingId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改用户第三方账号绑定";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["userBindingInfoRef"].validate(valid => {
    if (valid) {
      if (form.value.bindingId != null) {
        updateUserBindingInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addUserBindingInfo(form.value).then(response => {
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
  const _bindingIds = row.bindingId || ids.value;
  proxy.$modal.confirm('是否确认删除用户第三方账号绑定编号为"' + _bindingIds + '"的数据项？').then(function () {
    return delUserBindingInfo(_bindingIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('user/userBindingInfo/export', {
    ...queryParams.value
  }, `userBindingInfo_${new Date().getTime()}.xlsx`)
}

getList();
</script>
