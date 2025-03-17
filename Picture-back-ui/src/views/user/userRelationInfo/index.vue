<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="关系ID" prop="relationId">
        <el-input
            v-model="queryParams.relationId"
            placeholder="请输入关系ID"
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
      <el-form-item label="关联用户" prop="relationUserId">
        <el-input
            v-model="queryParams.relationUserId"
            placeholder="请输入关联用户"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="关系类型" prop="relationType">
        <el-select v-model="queryParams.relationType" style="width: 200px" placeholder="请选择关系类型" clearable>
          <el-option
              v-for="dict in u_relation_type"
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
            v-hasPermi="['user:userRelationInfo:add']"
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
            v-hasPermi="['user:userRelationInfo:edit']"
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
            v-hasPermi="['user:userRelationInfo:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['user:userRelationInfo:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="userRelationInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="关系ID" align="center" prop="relationId" v-if="columns[0].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="用户" align="center" prop="userId" v-if="columns[1].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="关联用户" align="center" prop="relationUserId" v-if="columns[2].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="关系类型" align="center" prop="relationType" v-if="columns[3].visible">
        <template #default="scope">
          <dict-tag :options="u_relation_type" :value="scope.row.relationType"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" v-if="columns[4].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['user:userRelationInfo:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['user:userRelationInfo:remove']">删除
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

    <!-- 添加或修改用户关系对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="userRelationInfoRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户"/>
        </el-form-item>
        <el-form-item label="关联用户" prop="relationUserId">
          <el-input v-model="form.relationUserId" placeholder="请输入关联用户"/>
        </el-form-item>
        <el-form-item label="关系类型" prop="relationType">
          <el-select v-model="form.relationType" placeholder="请选择关系类型">
            <el-option
                v-for="dict in u_relation_type"
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

<script setup name="UserRelationInfo">
import {
  listUserRelationInfo,
  getUserRelationInfo,
  delUserRelationInfo,
  addUserRelationInfo,
  updateUserRelationInfo
} from "@/api/user/userRelationInfo";

const {proxy} = getCurrentInstance();
const {u_relation_type} = proxy.useDict('u_relation_type');

const userRelationInfoList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const daterangeCreateTime = ref([]);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    relationId: null,
    userId: null,
    relationUserId: null,
    relationType: null,
    createTime: null
  },
  rules: {
    userId: [
      {required: true, message: "用户不能为空", trigger: "blur"}
    ],
    relationUserId: [
      {required: true, message: "关联用户不能为空", trigger: "blur"}
    ],
    relationType: [
      {required: true, message: "关系类型不能为空", trigger: "change"}
    ],
    createTime: [
      {required: true, message: "创建时间不能为空", trigger: "blur"}
    ]
  },
  //表格展示列
  columns: [
    {key: 0, label: '关系ID', visible: true},
    {key: 1, label: '用户', visible: true},
    {key: 2, label: '关联用户', visible: true},
    {key: 3, label: '关系类型', visible: true},
    {key: 4, label: '创建时间', visible: true},
  ],
});

const {queryParams, form, rules, columns} = toRefs(data);

/** 查询用户关系列表 */
function getList() {
  loading.value = true;
  queryParams.value.params = {};
  if (null != daterangeCreateTime && '' != daterangeCreateTime) {
    queryParams.value.params["beginCreateTime"] = daterangeCreateTime.value[0];
    queryParams.value.params["endCreateTime"] = daterangeCreateTime.value[1];
  }
  listUserRelationInfo(queryParams.value).then(response => {
    userRelationInfoList.value = response.rows;
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
    relationId: null,
    userId: null,
    relationUserId: null,
    relationType: null,
    createTime: null
  };
  proxy.resetForm("userRelationInfoRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  daterangeCreateTime.value = [];
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.relationId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加用户关系";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _relationId = row.relationId || ids.value
  getUserRelationInfo(_relationId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改用户关系";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["userRelationInfoRef"].validate(valid => {
    if (valid) {
      if (form.value.relationId != null) {
        updateUserRelationInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addUserRelationInfo(form.value).then(response => {
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
  const _relationIds = row.relationId || ids.value;
  proxy.$modal.confirm('是否确认删除用户关系编号为"' + _relationIds + '"的数据项？').then(function () {
    return delUserRelationInfo(_relationIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('user/userRelationInfo/export', {
    ...queryParams.value
  }, `userRelationInfo_${new Date().getTime()}.xlsx`)
}

getList();
</script>
