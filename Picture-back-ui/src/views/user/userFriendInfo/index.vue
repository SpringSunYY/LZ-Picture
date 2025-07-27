<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="关系" prop="relationId">
        <el-input
            v-model="queryParams.relationId"
            placeholder="请输入关系"
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
      <el-form-item label="好友用户" prop="friendUserId">
        <el-input
            v-model="queryParams.friendUserId"
            placeholder="请输入好友用户"
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
            v-hasPermi="['user:userFriendInfo:add']"
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
            v-hasPermi="['user:userFriendInfo:edit']"
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
            v-hasPermi="['user:userFriendInfo:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['user:userFriendInfo:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table ref="tableRef" v-loading="loading" :data="userFriendInfoList" @selection-change="handleSelectionChange"
              @sort-change="customSort">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="序号" type="index" width="50"/>
      <el-table-column label="关系" align="center" prop="relationId" v-if="columns[0].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="用户" align="center" prop="userId" v-if="columns[1].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="好友用户" align="center" prop="friendUserId" v-if="columns[2].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" v-if="columns[3].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['user:userFriendInfo:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['user:userFriendInfo:remove']">删除
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

    <!-- 添加或修改用户好友关系对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="userFriendInfoRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户"/>
        </el-form-item>
        <el-form-item label="好友用户" prop="friendUserId">
          <el-input v-model="form.friendUserId" placeholder="请输入好友用户"/>
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

<script setup name="UserFriendInfo">
import {
  listUserFriendInfo,
  getUserFriendInfo,
  delUserFriendInfo,
  addUserFriendInfo,
  updateUserFriendInfo
} from "@/api/user/userFriendInfo";

const {proxy} = getCurrentInstance();

const userFriendInfoList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const daterangeCreateTime = ref([]);

const isAsc = ref();
const orderByColumn = ref('');
const data = reactive({
      form: {}
      ,
      queryParams: {
        pageNum: 1,
        pageSize:
            10,
        relationId:
            null,
        userId:
            null,
        friendUserId:
            null,
        createTime:
            null
      }
      ,
      rules: {
        userId: [
          {required: true, message: "用户不能为空", trigger: "blur"}
        ],
        friendUserId:
            [
              {required: true, message: "好友用户不能为空", trigger: "blur"}
            ],
        createTime:
            [
              {required: true, message: "创建时间不能为空", trigger: "blur"}
            ]
      }
      ,
//表格展示列
      columns: [
        {key: 0, label: '关系', visible: true},
        {key: 1, label: '用户', visible: true},
        {key: 2, label: '好友用户', visible: true},
        {key: 3, label: '创建时间', visible: true},
      ],
    })
;

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


/** 查询用户好友关系列表 */
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
  listUserFriendInfo(queryParams.value).then(response => {
    userFriendInfoList.value = response.rows;
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
    friendUserId: null,
    createTime: null
  };
  proxy.resetForm("userFriendInfoRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  daterangeCreateTime.value = [];
  orderByColumn.value = null
  isAsc.value = null;
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
  title.value = "添加用户好友关系";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _relationId = row.relationId || ids.value
  getUserFriendInfo(_relationId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改用户好友关系";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["userFriendInfoRef"].validate(valid => {
    if (valid) {
      if (form.value.relationId != null) {
        updateUserFriendInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addUserFriendInfo(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除用户好友关系编号为"' + _relationIds + '"的数据项？').then(function () {
    return delUserFriendInfo(_relationIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('user/userFriendInfo/export', {
    ...queryParams.value
  }, `userFriendInfo_${new Date().getTime()}.xlsx`)
}

getList();
</script>
