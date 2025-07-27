<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="成员编号" prop="memberId">
        <el-input
            v-model="queryParams.memberId"
            placeholder="请输入成员编号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="空间编号" prop="spaceId">
        <el-input
            v-model="queryParams.spaceId"
            placeholder="请输入空间编号"
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
      <el-form-item label="角色" prop="roleType">
        <el-select v-model="queryParams.roleType" style="width: 200px" placeholder="请选择角色" clearable>
          <el-option
              v-for="dict in p_space_role"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="操作时间" style="width: 308px">
        <el-date-picker
            v-model="daterangeLastActiveTime"
            value-format="YYYY-MM-DD"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="加入时间" style="width: 308px">
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
      <el-form-item label="邀请人" prop="inviterUserId">
        <el-input
            v-model="queryParams.inviterUserId"
            placeholder="请输入邀请人编号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="加入方式" prop="joinType">
        <el-select v-model="queryParams.joinType" style="width: 200px" placeholder="请选择加入方式" clearable>
          <el-option
              v-for="dict in p_space_join_type"
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
            v-hasPermi="['picture:spaceMemberInfo:add']"
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
            v-hasPermi="['picture:spaceMemberInfo:edit']"
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
            v-hasPermi="['picture:spaceMemberInfo:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['picture:spaceMemberInfo:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table ref="tableRef" v-loading="loading" :data="spaceMemberInfoList" @selection-change="handleSelectionChange"
              @sort-change="customSort">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="序号" type="index" width="50"/>
      <el-table-column label="成员编号" align="center" prop="memberId" v-if="columns[0].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="空间编号" align="center" prop="spaceId" v-if="columns[1].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="用户编号" align="center" prop="userId" v-if="columns[2].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="角色" align="center" prop="roleType" v-if="columns[3].visible">
        <template #default="scope">
          <dict-tag :options="p_space_role" :value="scope.row.roleType"/>
        </template>
      </el-table-column>
      <el-table-column label="最后操作时间" align="center" prop="lastActiveTime" width="180" v-if="columns[4].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.lastActiveTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="加入时间" align="center" prop="createTime" width="180" v-if="columns[5].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180" v-if="columns[6].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="邀请人编号" align="center" prop="inviterUserId" v-if="columns[7].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="加入方式" align="center" prop="joinType" v-if="columns[8].visible">
        <template #default="scope">
          <dict-tag :options="p_space_join_type" :value="scope.row.joinType"/>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" v-if="columns[9].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['picture:spaceMemberInfo:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['picture:spaceMemberInfo:remove']">删除
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

    <!-- 添加或修改空间成员信息对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="spaceMemberInfoRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="空间编号" prop="spaceId">
          <el-input v-model="form.spaceId" placeholder="请输入空间编号"/>
        </el-form-item>
        <el-form-item label="用户编号" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户编号"/>
        </el-form-item>
        <el-form-item label="角色" prop="roleType">
          <el-select v-model="form.roleType" placeholder="请选择角色">
            <el-option
                v-for="dict in p_space_role"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="最后操作时间" prop="lastActiveTime">
          <el-date-picker clearable
                          v-model="form.lastActiveTime"
                          type="date"
                          value-format="YYYY-MM-DD"
                          placeholder="请选择最后操作时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="邀请人编号" prop="inviterUserId">
          <el-input v-model="form.inviterUserId" placeholder="请输入邀请人编号"/>
        </el-form-item>
        <el-form-item label="加入方式" prop="joinType">
          <el-select v-model="form.joinType" placeholder="请选择加入方式">
            <el-option
                v-for="dict in p_space_join_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注"/>
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

<script setup name="SpaceMemberInfo">
import {
  listSpaceMemberInfo,
  getSpaceMemberInfo,
  delSpaceMemberInfo,
  addSpaceMemberInfo,
  updateSpaceMemberInfo
} from "@/api/picture/spaceMemberInfo";

const {proxy} = getCurrentInstance();
const {p_space_join_type, p_space_role} = proxy.useDict('p_space_join_type', 'p_space_role');

const spaceMemberInfoList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const daterangeLastActiveTime = ref([]);
const daterangeCreateTime = ref([]);
const daterangeUpdateTime = ref([]);

const isAsc = ref();
const orderByColumn = ref('');
const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    memberId: null,
    spaceId: null,
    userId: null,
    roleType: null,
    lastActiveTime: null,
    createTime: null,
    updateTime: null,
    inviterUserId: null,
    joinType: null,
  },
  rules: {
    spaceId: [
      {required: true, message: "空间编号不能为空", trigger: "blur"}
    ],
    userId: [
      {required: true, message: "用户编号不能为空", trigger: "blur"}
    ],
    roleType: [
      {required: true, message: "角色不能为空", trigger: "change"}
    ],
    createTime: [
      {required: true, message: "加入时间不能为空", trigger: "blur"}
    ],
    joinType: [
      {required: true, message: "加入方式不能为空", trigger: "change"}
    ],
  },
  //表格展示列
  columns: [
    {key: 0, label: '成员编号', visible: false},
    {key: 1, label: '空间编号', visible: true},
    {key: 2, label: '用户编号', visible: true},
    {key: 3, label: '角色', visible: true},
    {key: 4, label: '最后操作时间', visible: true},
    {key: 5, label: '加入时间', visible: true},
    {key: 6, label: '更新时间', visible: false},
    {key: 7, label: '邀请人编号', visible: true},
    {key: 8, label: '加入方式', visible: true},
    {key: 9, label: '备注', visible: false},
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


/** 查询空间成员信息列表 */
function getList() {
  loading.value = true;
 queryParams.value.params = {}; if (orderByColumn.value != null && isAsc.value !== null) {
    queryParams.value.params["orderByColumn"] = orderByColumn.value;
    queryParams.value.params["isAsc"] = isAsc.value;
  }
  if (null != daterangeLastActiveTime && '' != daterangeLastActiveTime) {
    queryParams.value.params["beginLastActiveTime"] = daterangeLastActiveTime.value[0];
    queryParams.value.params["endLastActiveTime"] = daterangeLastActiveTime.value[1];
  }
  if (null != daterangeCreateTime && '' != daterangeCreateTime) {
    queryParams.value.params["beginCreateTime"] = daterangeCreateTime.value[0];
    queryParams.value.params["endCreateTime"] = daterangeCreateTime.value[1];
  }
  if (null != daterangeUpdateTime && '' != daterangeUpdateTime) {
    queryParams.value.params["beginUpdateTime"] = daterangeUpdateTime.value[0];
    queryParams.value.params["endUpdateTime"] = daterangeUpdateTime.value[1];
  }
  listSpaceMemberInfo(queryParams.value).then(response => {
    spaceMemberInfoList.value = response.rows;
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
    memberId: null,
    spaceId: null,
    userId: null,
    roleType: null,
    lastActiveTime: null,
    createTime: null,
    updateTime: null,
    inviterUserId: null,
    joinType: null,
    remark: null
  };
  proxy.resetForm("spaceMemberInfoRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  daterangeLastActiveTime.value = [];
  daterangeCreateTime.value = [];
  daterangeUpdateTime.value = [];
    orderByColumn.value = null
  isAsc.value = null;
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.memberId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加空间成员信息";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _memberId = row.memberId || ids.value
  getSpaceMemberInfo(_memberId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改空间成员信息";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["spaceMemberInfoRef"].validate(valid => {
    if (valid) {
      if (form.value.memberId != null) {
        updateSpaceMemberInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addSpaceMemberInfo(form.value).then(response => {
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
  const _memberIds = row.memberId || ids.value;
  proxy.$modal.confirm('是否确认删除空间成员信息编号为"' + _memberIds + '"的数据项？').then(function () {
    return delSpaceMemberInfo(_memberIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('picture/spaceMemberInfo/export', {
    ...queryParams.value
  }, `spaceMemberInfo_${new Date().getTime()}.xlsx`)
}

getList();
</script>
