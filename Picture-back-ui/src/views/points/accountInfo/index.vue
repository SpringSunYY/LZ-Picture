<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="账户编号" prop="accountId">
        <el-input
            v-model="queryParams.accountId"
            placeholder="请输入账户编号"
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
            v-hasPermi="['points:accountInfo:add']"
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
            v-hasPermi="['points:accountInfo:edit']"
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
            v-hasPermi="['points:accountInfo:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['points:accountInfo:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table ref="tableRef" v-loading="loading" :data="accountInfoList" @selection-change="handleSelectionChange"
              @sort-change="customSort">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="序号" type="index" width="50"/>
      <el-table-column label="账户编号" align="center" prop="accountId" v-if="columns[0].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="用户编号" align="center" prop="userId" v-if="columns[1].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="支付密码" align="center" prop="password" v-if="columns[2].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="加密方式" align="center" prop="salt" v-if="columns[3].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="赚取总积分" align="center" prop="pointsEarned" sortable="custom" v-if="columns[4].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="使用总积分" align="center" prop="pointsUsed" sortable="custom" v-if="columns[5].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="充值总金额" align="center" prop="rechargeAmount" sortable="custom"
                       v-if="columns[6].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="账户状态" align="center" prop="accountStatus" v-if="columns[7].visible">
        <template #default="scope">
          <dict-tag :options="po_account_status" :value="scope.row.accountStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="积分余额" align="center" prop="pointsBalance" sortable="custom" v-if="columns[8].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="创建时间" align="center" prop="createTime" sortable="custom" width="180"
                       v-if="columns[9].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime" sortable="custom" width="180"
                       v-if="columns[10].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" v-if="columns[11].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="删除" align="center" prop="isDelete" v-if="columns[12].visible">
        <template #default="scope">
          <dict-tag :options="common_delete" :value="scope.row.isDelete"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['points:accountInfo:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['points:accountInfo:remove']">删除
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

    <!-- 添加或修改积分账户对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="accountInfoRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户编号" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户编号"/>
        </el-form-item>
        <el-form-item label="支付密码" prop="password">
          <el-input v-model="form.password" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="加密方式" prop="salt">
          <el-input v-model="form.salt" placeholder="请输入加密方式"/>
        </el-form-item>
        <el-form-item label="赚取总积分" prop="pointsEarned">
          <el-input v-model="form.pointsEarned" placeholder="请输入赚取总积分"/>
        </el-form-item>
        <el-form-item label="使用总积分" prop="pointsUsed">
          <el-input v-model="form.pointsUsed" placeholder="请输入使用总积分"/>
        </el-form-item>
        <el-form-item label="充值总金额" prop="rechargeAmount">
          <el-input v-model="form.rechargeAmount" placeholder="请输入充值总金额"/>
        </el-form-item>
        <el-form-item label="账户状态" prop="accountStatus">
          <el-radio-group v-model="form.accountStatus">
            <el-radio
                v-for="dict in po_account_status"
                :key="dict.value"
                :value="dict.value"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="积分余额" prop="pointsBalance">
          <el-input v-model="form.pointsBalance" placeholder="请输入积分余额"/>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"/>
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

<script setup name="AccountInfo">
import {
  listAccountInfo,
  getAccountInfo,
  delAccountInfo,
  addAccountInfo,
  updateAccountInfo
} from "@/api/points/accountInfo";

const {proxy} = getCurrentInstance();
const {common_delete, po_account_status} = proxy.useDict('common_delete', 'po_account_status');

const accountInfoList = ref([]);
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
    accountId: null,
    userId: null,
    createTime: null,
    updateTime: null,
    isDelete: null
  },
  rules: {
    userId: [
      {required: true, message: "用户编号不能为空", trigger: "blur"}
    ],
    password: [
      {required: true, message: "支付密码不能为空", trigger: "blur"}
    ],
    salt: [
      {required: true, message: "加密方式不能为空", trigger: "blur"}
    ],
    pointsEarned: [
      {required: true, message: "赚取总积分不能为空", trigger: "blur"}
    ],
    pointsUsed: [
      {required: true, message: "使用总积分不能为空", trigger: "blur"}
    ],
    rechargeAmount: [
      {required: true, message: "充值总金额不能为空", trigger: "blur"}
    ],
    accountStatus: [
      {required: true, message: "账户状态不能为空", trigger: "change"}
    ],
    pointsBalance: [
      {required: true, message: "积分余额不能为空", trigger: "blur"}
    ],
    createTime: [
      {required: true, message: "创建时间不能为空", trigger: "blur"}
    ],
    isDelete: [
      {required: true, message: "删除不能为空", trigger: "change"}
    ]
  },
  //表格展示列
  columns: [
    {key: 0, label: '账户编号', visible: false},
    {key: 1, label: '用户编号', visible: true},
    {key: 2, label: '支付密码', visible: false},
    {key: 3, label: '加密方式', visible: false},
    {key: 4, label: '赚取总积分', visible: true},
    {key: 5, label: '使用总积分', visible: true},
    {key: 6, label: '充值总金额', visible: true},
    {key: 7, label: '账户状态', visible: true},
    {key: 8, label: '积分余额', visible: true},
    {key: 9, label: '创建时间', visible: true},
    {key: 10, label: '更新时间', visible: false},
    {key: 11, label: '备注', visible: false},
    {key: 12, label: '删除', visible: false},
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


/** 查询积分账户列表 */
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
  listAccountInfo(queryParams.value).then(response => {
    accountInfoList.value = response.rows;
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
    accountId: null,
    userId: null,
    password: null,
    salt: null,
    pointsEarned: null,
    pointsUsed: null,
    rechargeAmount: null,
    accountStatus: null,
    pointsBalance: null,
    createTime: null,
    updateTime: null,
    remark: null,
    isDelete: null
  };
  proxy.resetForm("accountInfoRef");
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
  proxy.resetForm("queryRef");
  proxy.$refs.tableRef.clearSort();
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.accountId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加积分账户";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _accountId = row.accountId || ids.value
  getAccountInfo(_accountId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改积分账户";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["accountInfoRef"].validate(valid => {
    if (valid) {
      if (form.value.accountId != null) {
        updateAccountInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addAccountInfo(form.value).then(response => {
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
  const _accountIds = row.accountId || ids.value;
  proxy.$modal.confirm('是否确认删除积分账户编号为"' + _accountIds + '"的数据项？').then(function () {
    return delAccountInfo(_accountIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('points/accountInfo/export', {
    ...queryParams.value
  }, `accountInfo_${new Date().getTime()}.xlsx`)
}

getList();
</script>
