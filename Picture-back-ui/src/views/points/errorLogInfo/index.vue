<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="异常编号" prop="errorId">
        <el-input
            v-model="queryParams.errorId"
            placeholder="请输入异常编号"
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
      <el-form-item label="订单类型" prop="orderType">
        <el-select v-model="queryParams.orderType" style="width: 200px" placeholder="请选择订单类型" clearable>
          <el-option
              v-for="dict in po_order_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="支付方式" prop="methodType">
        <el-select v-model="queryParams.methodType" style="width: 200px" placeholder="请选择支付方式" clearable>
          <el-option
              v-for="dict in po_payment_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="支付平台" prop="thirdParty">
        <el-input
            v-model="queryParams.thirdParty"
            placeholder="请输入第三方支付平台"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="平台订单" prop="thirdPartyOrder">
        <el-input
            v-model="queryParams.thirdPartyOrder"
            placeholder="请输入第三方支付平台订单号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="异常类型" prop="errorType">
        <el-select v-model="queryParams.errorType" style="width: 200px" placeholder="请选择异常类型" clearable>
          <el-option
              v-for="dict in po_error_log_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="Code" prop="errorCode">
        <el-input
            v-model="queryParams.errorCode"
            placeholder="请输入返回Code"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="Msg" prop="errorMsg">
        <el-input
            v-model="queryParams.errorMsg"
            placeholder="请输入返回Msg"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="订单编号" prop="relatedOrderId">
        <el-input
            v-model="queryParams.relatedOrderId"
            placeholder="请输入相关订单编号"
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
      <el-form-item label="IP地址" prop="ipAddr">
        <el-input
            v-model="queryParams.ipAddr"
            placeholder="请输入IP地址"
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
      <el-form-item label="解决状态" prop="resolveStatus">
        <el-select v-model="queryParams.resolveStatus" style="width: 200px" placeholder="请选择解决状态" clearable>
          <el-option
              v-for="dict in po_error_resolve_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="记录时间" style="width: 308px">
        <el-date-picker
            v-model="daterangeCreateTime"
            value-format="YYYY-MM-DD"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="解决时间" style="width: 308px">
        <el-date-picker
            v-model="daterangeResolveTime"
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
            v-hasPermi="['points:errorLogInfo:add']"
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
            v-hasPermi="['points:errorLogInfo:edit']"
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
            v-hasPermi="['points:errorLogInfo:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['points:errorLogInfo:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="errorLogInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="异常编号" align="center" prop="errorId" v-if="columns[0].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="用户编号" align="center" prop="userId" v-if="columns[1].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="订单类型" align="center" prop="orderType" v-if="columns[2].visible">
        <template #default="scope">
          <dict-tag :options="po_order_type" :value="scope.row.orderType"/>
        </template>
      </el-table-column>
      <el-table-column label="支付方式" align="center" prop="methodType" v-if="columns[3].visible">
        <template #default="scope">
          <dict-tag :options="po_payment_type" :value="scope.row.methodType"/>
        </template>
      </el-table-column>
      <el-table-column label="第三方支付平台" align="center" prop="thirdParty" v-if="columns[4].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="第三方支付平台订单号" align="center" prop="thirdPartyOrder" v-if="columns[5].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="异常类型" align="center" prop="errorType" v-if="columns[6].visible">
        <template #default="scope">
          <dict-tag :options="po_error_log_type" :value="scope.row.errorType"/>
        </template>
      </el-table-column>
      <el-table-column label="返回Code" align="center" prop="errorCode" v-if="columns[7].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="返回Msg" align="center" prop="errorMsg" v-if="columns[8].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="额外信息" align="center" prop="paymentExtend" v-if="columns[9].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="相关订单编号" align="center" prop="relatedOrderId" v-if="columns[10].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="异常记录时间" align="center" prop="createTime" width="180" v-if="columns[11].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="设备唯一标识" align="center" prop="deviceId" v-if="columns[12].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="浏览器类型" align="center" prop="browser" v-if="columns[13].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="操作系统" align="center" prop="os" v-if="columns[14].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="平台" align="center" prop="platform" v-if="columns[15].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="IP地址" align="center" prop="ipAddr" v-if="columns[16].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="IP属地" align="center" prop="ipAddress" v-if="columns[17].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="解决状态" align="center" prop="resolveStatus" v-if="columns[18].visible">
        <template #default="scope">
          <dict-tag :options="po_error_resolve_status" :value="scope.row.resolveStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="解决时间" align="center" prop="resolveTime" width="180" v-if="columns[19].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.resolveTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" v-if="columns[20].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['points:errorLogInfo:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['points:errorLogInfo:remove']">删除
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

    <!-- 添加或修改异常捕获对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="errorLogInfoRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户编号" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户编号"/>
        </el-form-item>
        <el-form-item label="订单类型" prop="orderType">
          <el-select v-model="form.orderType" placeholder="请选择订单类型">
            <el-option
                v-for="dict in po_order_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="支付方式" prop="methodType">
          <el-select v-model="form.methodType" placeholder="请选择支付方式">
            <el-option
                v-for="dict in po_payment_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="第三方支付平台" prop="thirdParty">
          <el-input v-model="form.thirdParty" placeholder="请输入第三方支付平台"/>
        </el-form-item>
        <el-form-item label="第三方支付平台订单号" prop="thirdPartyOrder">
          <el-input v-model="form.thirdPartyOrder" placeholder="请输入第三方支付平台订单号"/>
        </el-form-item>
        <el-form-item label="异常类型" prop="errorType">
          <el-input v-model="form.errorType" placeholder="请输入异常类型"/>
        </el-form-item>
        <el-form-item label="相关订单编号" prop="relatedOrderId">
          <el-input v-model="form.relatedOrderId" placeholder="请输入相关订单编号"/>
        </el-form-item>
        <el-form-item label="解决状态" prop="resolveStatus">
          <el-radio-group v-model="form.resolveStatus">
            <el-radio
                v-for="dict in po_error_resolve_status"
                :key="dict.value"
                :value="dict.value"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="解决时间" prop="resolveTime">
          <el-date-picker clearable
                          v-model="form.resolveTime"
                          type="date"
                          value-format="YYYY-MM-DD"
                          placeholder="请选择解决时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"/>
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

<script setup name="ErrorLogInfo">
import {
  listErrorLogInfo,
  getErrorLogInfo,
  delErrorLogInfo,
  addErrorLogInfo,
  updateErrorLogInfo
} from "@/api/points/errorLogInfo";

const {proxy} = getCurrentInstance();
const {
  po_error_resolve_status,
  po_payment_type,
  po_order_type,
  po_error_log_type
} = proxy.useDict('po_error_resolve_status', 'po_payment_type', 'po_order_type','po_error_log_type');

const errorLogInfoList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const daterangeCreateTime = ref([]);
const daterangeResolveTime = ref([]);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    errorId: null,
    userId: null,
    orderType: null,
    methodType: null,
    thirdParty: null,
    thirdPartyOrder: null,
    errorType: null,
    errorCode: null,
    errorMsg: null,
    relatedOrderId: null,
    createTime: null,
    deviceId: null,
    browser: null,
    os: null,
    platform: null,
    ipAddr: null,
    ipAddress: null,
    resolveStatus: null,
    resolveTime: null,
  },
  rules: {
    orderType: [
      {required: true, message: "订单类型不能为空", trigger: "change"}
    ],
    methodType: [
      {required: true, message: "支付方式不能为空", trigger: "change"}
    ],
    thirdParty: [
      {required: true, message: "第三方支付平台不能为空", trigger: "blur"}
    ],
    errorType: [
      {required: true, message: "异常类型不能为空", trigger: "blur"}
    ],
    createTime: [
      {required: true, message: "异常记录时间不能为空", trigger: "blur"}
    ],
    ipAddr: [
      {required: true, message: "IP地址不能为空", trigger: "blur"}
    ],
    resolveStatus: [
      {required: true, message: "解决状态不能为空", trigger: "change"}
    ],
  },
  //表格展示列
  columns: [
    {key: 0, label: '异常编号', visible: false},
    {key: 1, label: '用户编号', visible: true},
    {key: 2, label: '订单类型', visible: true},
    {key: 3, label: '支付方式', visible: true},
    {key: 4, label: '第三方支付平台', visible: true},
    {key: 5, label: '第三方支付平台订单号', visible: false},
    {key: 6, label: '异常类型', visible: true},
    {key: 7, label: '返回Code', visible: true},
    {key: 8, label: '返回Msg', visible: true},
    {key: 9, label: '额外信息', visible: false},
    {key: 10, label: '相关订单编号', visible: true},
    {key: 11, label: '异常记录时间', visible: true},
    {key: 12, label: '设备唯一标识', visible: false},
    {key: 13, label: '浏览器类型', visible: false},
    {key: 14, label: '操作系统', visible: false},
    {key: 15, label: '平台', visible: false},
    {key: 16, label: 'IP地址', visible: false},
    {key: 17, label: 'IP属地', visible: false},
    {key: 18, label: '解决状态', visible: true},
    {key: 19, label: '解决时间', visible: true},
    {key: 20, label: '备注', visible: false},
  ],
});

const {queryParams, form, rules, columns} = toRefs(data);

/** 查询异常捕获列表 */
function getList() {
  loading.value = true;
  queryParams.value.params = {};
  if (null != daterangeCreateTime && '' != daterangeCreateTime) {
    queryParams.value.params["beginCreateTime"] = daterangeCreateTime.value[0];
    queryParams.value.params["endCreateTime"] = daterangeCreateTime.value[1];
  }
  if (null != daterangeResolveTime && '' != daterangeResolveTime) {
    queryParams.value.params["beginResolveTime"] = daterangeResolveTime.value[0];
    queryParams.value.params["endResolveTime"] = daterangeResolveTime.value[1];
  }
  listErrorLogInfo(queryParams.value).then(response => {
    errorLogInfoList.value = response.rows;
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
    errorId: null,
    userId: null,
    orderType: null,
    methodType: null,
    thirdParty: null,
    thirdPartyOrder: null,
    errorType: null,
    errorCode: null,
    errorMsg: null,
    paymentExtend: null,
    relatedOrderId: null,
    createTime: null,
    deviceId: null,
    browser: null,
    os: null,
    platform: null,
    ipAddr: null,
    ipAddress: null,
    resolveStatus: null,
    resolveTime: null,
    remark: null
  };
  proxy.resetForm("errorLogInfoRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  daterangeCreateTime.value = [];
  daterangeResolveTime.value = [];
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.errorId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加异常捕获";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _errorId = row.errorId || ids.value
  getErrorLogInfo(_errorId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改异常捕获";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["errorLogInfoRef"].validate(valid => {
    if (valid) {
      if (form.value.errorId != null) {
        updateErrorLogInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addErrorLogInfo(form.value).then(response => {
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
  const _errorIds = row.errorId || ids.value;
  proxy.$modal.confirm('是否确认删除异常捕获编号为"' + _errorIds + '"的数据项？').then(function () {
    return delErrorLogInfo(_errorIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('points/errorLogInfo/export', {
    ...queryParams.value
  }, `errorLogInfo_${new Date().getTime()}.xlsx`)
}

getList();
</script>
