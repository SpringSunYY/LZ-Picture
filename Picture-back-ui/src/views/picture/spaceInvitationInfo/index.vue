<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="邀请编号" prop="invitationId">
        <el-input
            v-model="queryParams.invitationId"
            placeholder="请输入邀请编号"
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
      <el-form-item label="空间名称" prop="spaceName">
        <el-input
            v-model="queryParams.spaceName"
            placeholder="请输入空间名称"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="邀请角色" prop="roleType">
        <el-select v-model="queryParams.roleType" style="width: 200px" placeholder="请选择邀请角色" clearable>
          <el-option
              v-for="dict in p_space_role"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="邀请状态" prop="invitationStatus">
        <el-select v-model="queryParams.invitationStatus" style="width: 200px" placeholder="请选择邀请状态" clearable>
          <el-option
              v-for="dict in p_space_invitation_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="邀请人" prop="invitationUserId">
        <el-input
            v-model="queryParams.invitationUserId"
            placeholder="请输入邀请人编号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="过期时间" style="width: 308px">
        <el-date-picker
            v-model="daterangeExpireTime"
            value-format="YYYY-MM-DD"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
        ></el-date-picker>
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
      <el-form-item label="被邀用户" prop="userId">
        <el-input
            v-model="queryParams.userId"
            placeholder="请输入被邀请用户编号"
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
            v-hasPermi="['picture:spaceInvitationInfo:add']"
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
            v-hasPermi="['picture:spaceInvitationInfo:edit']"
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
            v-hasPermi="['picture:spaceInvitationInfo:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['picture:spaceInvitationInfo:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="spaceInvitationInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="邀请编号" align="center" prop="invitationId" v-if="columns[0].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="空间编号" align="center" prop="spaceId" v-if="columns[1].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="空间名称" align="center" prop="spaceName" v-if="columns[2].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="空间封面URL" align="center" prop="spaceAvatar" width="100" v-if="columns[3].visible">
        <template #default="scope">
          <image-preview :src="scope.row.spaceAvatar" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="邀请角色" align="center" prop="roleType" v-if="columns[4].visible">
        <template #default="scope">
          <dict-tag :options="p_space_role" :value="scope.row.roleType"/>
        </template>
      </el-table-column>
      <el-table-column label="邀请状态" align="center" prop="invitationStatus" v-if="columns[5].visible">
        <template #default="scope">
          <dict-tag :options="p_space_invitation_status" :value="scope.row.invitationStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="邀请链接" align="center" prop="invitationUrl" v-if="columns[6].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="邀请理由" align="center" prop="invitation" v-if="columns[7].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="邀请人编号" align="center" prop="invitationUserId" v-if="columns[8].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="过期时间" align="center" prop="expireTime" width="180" v-if="columns[9].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.expireTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" v-if="columns[10].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="被邀请用户编号" align="center" prop="userId" v-if="columns[11].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['picture:spaceInvitationInfo:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['picture:spaceInvitationInfo:remove']">删除
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

    <!-- 添加或修改空间成员邀请记录对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="spaceInvitationInfoRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="空间编号" prop="spaceId">
          <el-input v-model="form.spaceId" placeholder="请输入空间编号"/>
        </el-form-item>
        <el-form-item label="空间名称" prop="spaceName">
          <el-input v-model="form.spaceName" placeholder="请输入空间名称"/>
        </el-form-item>
        <el-form-item label="空间封面URL" prop="spaceAvatar">
          <image-upload v-model="form.spaceAvatar"/>
        </el-form-item>
        <el-form-item label="邀请角色" prop="roleType">
          <el-select v-model="form.roleType" placeholder="请选择邀请角色">
            <el-option
                v-for="dict in p_space_role"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="邀请状态" prop="invitationStatus">
          <el-radio-group v-model="form.invitationStatus">
            <el-radio
                v-for="dict in p_space_invitation_status"
                :key="dict.value"
                :value="dict.value"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="邀请链接" prop="invitationUrl">
          <el-input v-model="form.invitationUrl" placeholder="请输入邀请链接"/>
        </el-form-item>
        <el-form-item label="邀请理由" prop="invitation">
          <el-input v-model="form.invitation" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="邀请人编号" prop="invitationUserId">
          <el-input v-model="form.invitationUserId" placeholder="请输入邀请人编号"/>
        </el-form-item>
        <el-form-item label="过期时间" prop="expireTime">
          <el-date-picker clearable
                          v-model="form.expireTime"
                          type="date"
                          value-format="YYYY-MM-DD"
                          placeholder="请选择过期时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="被邀请用户编号" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入被邀请用户编号"/>
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

<script setup name="SpaceInvitationInfo">
import {
  listSpaceInvitationInfo,
  getSpaceInvitationInfo,
  delSpaceInvitationInfo,
  addSpaceInvitationInfo,
  updateSpaceInvitationInfo
} from "@/api/picture/spaceInvitationInfo";

const {proxy} = getCurrentInstance();
const {p_space_role, p_space_invitation_status} = proxy.useDict('p_space_role', 'p_space_invitation_status');

const spaceInvitationInfoList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const daterangeExpireTime = ref([]);
const daterangeCreateTime = ref([]);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    invitationId: null,
    spaceId: null,
    spaceName: null,
    roleType: null,
    invitationStatus: null,
    invitationUserId: null,
    expireTime: null,
    createTime: null,
    userId: null
  },
  rules: {
    spaceId: [
      {required: true, message: "空间编号不能为空", trigger: "blur"}
    ],
    spaceName: [
      {required: true, message: "空间名称不能为空", trigger: "blur"}
    ],
    roleType: [
      {required: true, message: "邀请角色不能为空", trigger: "change"}
    ],
    invitationStatus: [
      {required: true, message: "邀请状态不能为空", trigger: "change"}
    ],
    invitationUserId: [
      {required: true, message: "邀请人编号不能为空", trigger: "blur"}
    ],
    expireTime: [
      {required: true, message: "过期时间不能为空", trigger: "blur"}
    ],
    createTime: [
      {required: true, message: "创建时间不能为空", trigger: "blur"}
    ],
    userId: [
      {required: true, message: "被邀请用户编号不能为空", trigger: "blur"}
    ]
  },
  //表格展示列
  columns: [
    {key: 0, label: '邀请编号', visible: false},
    {key: 1, label: '空间编号', visible: false},
    {key: 2, label: '空间名称', visible: true},
    {key: 3, label: '空间封面URL', visible: true},
    {key: 4, label: '邀请角色', visible: true},
    {key: 5, label: '邀请状态', visible: true},
    {key: 6, label: '邀请链接', visible: false},
    {key: 7, label: '邀请理由', visible: false},
    {key: 8, label: '邀请人编号', visible: true},
    {key: 9, label: '过期时间', visible: true},
    {key: 10, label: '创建时间', visible: true},
    {key: 11, label: '被邀请用户编号', visible: false},
  ],
});

const {queryParams, form, rules, columns} = toRefs(data);

/** 查询空间成员邀请记录列表 */
function getList() {
  loading.value = true;
  queryParams.value.params = {};
  if (null != daterangeExpireTime && '' != daterangeExpireTime) {
    queryParams.value.params["beginExpireTime"] = daterangeExpireTime.value[0];
    queryParams.value.params["endExpireTime"] = daterangeExpireTime.value[1];
  }
  if (null != daterangeCreateTime && '' != daterangeCreateTime) {
    queryParams.value.params["beginCreateTime"] = daterangeCreateTime.value[0];
    queryParams.value.params["endCreateTime"] = daterangeCreateTime.value[1];
  }
  listSpaceInvitationInfo(queryParams.value).then(response => {
    spaceInvitationInfoList.value = response.rows;
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
    invitationId: null,
    spaceId: null,
    spaceName: null,
    spaceAvatar: null,
    roleType: null,
    invitationStatus: null,
    invitationUrl: null,
    invitation: null,
    invitationUserId: null,
    expireTime: null,
    createTime: null,
    userId: null
  };
  proxy.resetForm("spaceInvitationInfoRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  daterangeExpireTime.value = [];
  daterangeCreateTime.value = [];
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.invitationId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加空间成员邀请记录";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _invitationId = row.invitationId || ids.value
  getSpaceInvitationInfo(_invitationId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改空间成员邀请记录";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["spaceInvitationInfoRef"].validate(valid => {
    if (valid) {
      if (form.value.invitationId != null) {
        updateSpaceInvitationInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addSpaceInvitationInfo(form.value).then(response => {
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
  const _invitationIds = row.invitationId || ids.value;
  proxy.$modal.confirm('是否确认删除空间成员邀请记录编号为"' + _invitationIds + '"的数据项？').then(function () {
    return delSpaceInvitationInfo(_invitationIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('picture/spaceInvitationInfo/export', {
    ...queryParams.value
  }, `spaceInvitationInfo_${new Date().getTime()}.xlsx`)
}

getList();
</script>
