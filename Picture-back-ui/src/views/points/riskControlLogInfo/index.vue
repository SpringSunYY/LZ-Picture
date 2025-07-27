<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="风控日志编号" prop="logId">
        <el-input
            v-model="queryParams.logId"
            placeholder="请输入风控日志编号"
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
      <el-form-item label="支付方式" prop="methodId">
        <el-input
            v-model="queryParams.methodId"
            placeholder="请输入支付方式"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="风险类型" prop="riskType">
        <el-input
            v-model="queryParams.riskType"
            placeholder="请输入风险类型"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="风险等级" prop="riskLevel">
        <el-select v-model="queryParams.riskLevel" style="width: 200px" placeholder="请选择风险等级" clearable>
          <el-option
              v-for="dict in po_risk_level"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="措施时间" style="width: 308px">
        <el-date-picker
            v-model="daterangeActionTime"
            value-format="YYYY-MM-DD"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
        ></el-date-picker>
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
      <el-form-item label="设备唯一标识" prop="deviceId">
        <el-input
            v-model="queryParams.deviceId"
            placeholder="请输入设备唯一标识"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="浏览器类型" prop="browser">
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
            v-hasPermi="['points:riskControlLogInfo:add']"
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
            v-hasPermi="['points:riskControlLogInfo:edit']"
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
            v-hasPermi="['points:riskControlLogInfo:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['points:riskControlLogInfo:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table ref="tableRef" v-loading="loading" :data="riskControlLogInfoList" @selection-change="handleSelectionChange"  @sort-change="customSort">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="序号" type="index" width="50"/>
      <el-table-column label="风控日志编号" align="center" prop="logId" v-if="columns[0].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="用户编号" align="center" prop="userId" v-if="columns[1].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="支付方式" align="center" prop="methodId" v-if="columns[2].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="风险类型" align="center" prop="riskType" v-if="columns[3].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="风险等级" align="center" prop="riskLevel" v-if="columns[4].visible">
        <template #default="scope">
          <dict-tag :options="po_risk_level" :value="scope.row.riskLevel"/>
        </template>
      </el-table-column>
      <el-table-column label="风险描述" align="center" prop="riskDescription" v-if="columns[5].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="采取措施" align="center" prop="actionTaken" v-if="columns[6].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="措施时间" align="center" prop="actionTime" width="180" v-if="columns[7].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.actionTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="记录时间" align="center" prop="createTime" width="180" v-if="columns[8].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="设备唯一标识" align="center" prop="deviceId" v-if="columns[9].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="浏览器类型" align="center" prop="browser" v-if="columns[10].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="操作系统" align="center" prop="os" v-if="columns[11].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="平台" align="center" prop="platform" v-if="columns[12].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="IP地址" align="center" prop="ipAddr" v-if="columns[13].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="备注" align="center" prop="remark" v-if="columns[14].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['points:riskControlLogInfo:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['points:riskControlLogInfo:remove']">删除
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

    <!-- 添加或修改风控日志对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="riskControlLogInfoRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户编号" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户编号"/>
        </el-form-item>
        <el-form-item label="支付方式" prop="methodId">
          <el-input v-model="form.methodId" placeholder="请输入支付方式"/>
        </el-form-item>
        <el-form-item label="风险类型" prop="riskType">
          <el-input v-model="form.riskType" placeholder="请输入风险类型"/>
        </el-form-item>
        <el-form-item label="风险等级" prop="riskLevel">
          <el-select v-model="form.riskLevel" placeholder="请选择风险等级">
            <el-option
                v-for="dict in po_risk_level"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="风险描述" prop="riskDescription">
          <el-input v-model="form.riskDescription" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="采取措施" prop="actionTaken">
          <el-input v-model="form.actionTaken" placeholder="请输入采取措施"/>
        </el-form-item>
        <el-form-item label="措施时间" prop="actionTime">
          <el-date-picker clearable
                          v-model="form.actionTime"
                          type="date"
                          value-format="YYYY-MM-DD"
                          placeholder="请选择措施时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="设备唯一标识" prop="deviceId">
          <el-input v-model="form.deviceId" placeholder="请输入设备唯一标识"/>
        </el-form-item>
        <el-form-item label="浏览器类型" prop="browser">
          <el-input v-model="form.browser" placeholder="请输入浏览器类型"/>
        </el-form-item>
        <el-form-item label="操作系统" prop="os">
          <el-input v-model="form.os" placeholder="请输入操作系统"/>
        </el-form-item>
        <el-form-item label="平台" prop="platform">
          <el-input v-model="form.platform" placeholder="请输入平台"/>
        </el-form-item>
        <el-form-item label="IP地址" prop="ipAddr">
          <el-input v-model="form.ipAddr" placeholder="请输入IP地址"/>
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

<script setup name="RiskControlLogInfo">
import {
  listRiskControlLogInfo,
  getRiskControlLogInfo,
  delRiskControlLogInfo,
  addRiskControlLogInfo,
  updateRiskControlLogInfo
} from "@/api/points/riskControlLogInfo";

const {proxy} = getCurrentInstance();
const {po_risk_level} = proxy.useDict('po_risk_level');

const riskControlLogInfoList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const daterangeActionTime = ref([]);
const daterangeCreateTime = ref([]);

const isAsc = ref();
const orderByColumn = ref('');
const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    logId: null,
    userId: null,
    methodId: null,
    riskType: null,
    riskLevel: null,
    actionTime: null,
    createTime: null,
    deviceId: null,
    browser: null,
    os: null,
    platform: null,
    ipAddr: null,
  },
  rules: {
    userId: [
      {required: true, message: "用户编号不能为空", trigger: "blur"}
    ],
    methodId: [
      {required: true, message: "支付方式不能为空", trigger: "blur"}
    ],
    riskType: [
      {required: true, message: "风险类型不能为空", trigger: "blur"}
    ],
    riskLevel: [
      {required: true, message: "风险等级不能为空", trigger: "change"}
    ],
    riskDescription: [
      {required: true, message: "风险描述不能为空", trigger: "blur"}
    ],
    actionTaken: [
      {required: true, message: "采取措施不能为空", trigger: "blur"}
    ],
    actionTime: [
      {required: true, message: "措施时间不能为空", trigger: "blur"}
    ],
    createTime: [
      {required: true, message: "记录时间不能为空", trigger: "blur"}
    ],
    ipAddr: [
      {required: true, message: "IP地址不能为空", trigger: "blur"}
    ],
  },
  //表格展示列
  columns: [
    {key: 0, label: '风控日志编号', visible: true},
    {key: 1, label: '用户编号', visible: true},
    {key: 2, label: '支付方式', visible: true},
    {key: 3, label: '风险类型', visible: true},
    {key: 4, label: '风险等级', visible: true},
    {key: 5, label: '风险描述', visible: true},
    {key: 6, label: '采取措施', visible: true},
    {key: 7, label: '措施时间', visible: true},
    {key: 8, label: '记录时间', visible: true},
    {key: 9, label: '设备唯一标识', visible: true},
    {key: 10, label: '浏览器类型', visible: true},
    {key: 11, label: '操作系统', visible: true},
    {key: 12, label: '平台', visible: true},
    {key: 13, label: 'IP地址', visible: true},
    {key: 14, label: '备注', visible: true},
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


/** 查询风控日志列表 */
function getList() {
  loading.value = true;
 queryParams.value.params = {}; if (orderByColumn.value != null && isAsc.value !== null) {
    queryParams.value.params["orderByColumn"] = orderByColumn.value;
    queryParams.value.params["isAsc"] = isAsc.value;
  }
  if (null != daterangeActionTime && '' != daterangeActionTime) {
    queryParams.value.params["beginActionTime"] = daterangeActionTime.value[0];
    queryParams.value.params["endActionTime"] = daterangeActionTime.value[1];
  }
  if (null != daterangeCreateTime && '' != daterangeCreateTime) {
    queryParams.value.params["beginCreateTime"] = daterangeCreateTime.value[0];
    queryParams.value.params["endCreateTime"] = daterangeCreateTime.value[1];
  }
  listRiskControlLogInfo(queryParams.value).then(response => {
    riskControlLogInfoList.value = response.rows;
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
    methodId: null,
    riskType: null,
    riskLevel: null,
    riskDescription: null,
    actionTaken: null,
    actionTime: null,
    createTime: null,
    deviceId: null,
    browser: null,
    os: null,
    platform: null,
    ipAddr: null,
    remark: null
  };
  proxy.resetForm("riskControlLogInfoRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  daterangeActionTime.value = [];
  daterangeCreateTime.value = [];
    orderByColumn.value = null
  isAsc.value = null;
     proxy.$refs.tableRef.clearSort();
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
  title.value = "添加风控日志";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _logId = row.logId || ids.value
  getRiskControlLogInfo(_logId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改风控日志";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["riskControlLogInfoRef"].validate(valid => {
    if (valid) {
      if (form.value.logId != null) {
        updateRiskControlLogInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addRiskControlLogInfo(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除风控日志编号为"' + _logIds + '"的数据项？').then(function () {
    return delRiskControlLogInfo(_logIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('points/riskControlLogInfo/export', {
    ...queryParams.value
  }, `riskControlLogInfo_${new Date().getTime()}.xlsx`)
}

getList();
</script>
