<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="记录编号" prop="logId">
        <el-input
            v-model="queryParams.logId"
            placeholder="请输入记录编号"
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
      <el-form-item label="给予用户" prop="giveUserId">
        <el-input
            v-model="queryParams.giveUserId"
            placeholder="请输入给予用户编号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="日志类型" prop="logType">
        <el-select v-model="queryParams.logType" style="width: 200px" placeholder="请选择日志类型" clearable>
          <el-option
              v-for="dict in po_points_usage_log_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="使用类型" prop="usageType">
        <el-select v-model="queryParams.usageType" style="width: 200px" placeholder="请选择使用类型" clearable>
          <el-option
              v-for="dict in po_points_usage_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="目标编号" prop="targetId">
        <el-input
            v-model="queryParams.targetId"
            placeholder="请输入目标编号"
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
            v-hasPermi="['points:pointsUsageLogInfo:add']"
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
            v-hasPermi="['points:pointsUsageLogInfo:edit']"
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
            v-hasPermi="['points:pointsUsageLogInfo:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['points:pointsUsageLogInfo:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="pointsUsageLogInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="记录编号" align="center" prop="logId" v-if="columns[0].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="用户编号" align="center" prop="userId" v-if="columns[1].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="给予用户编号" align="center" prop="giveUserId" v-if="columns[2].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="日志类型" align="center" prop="logType" v-if="columns[3].visible">
        <template #default="scope">
          <dict-tag :options="po_points_usage_log_type" :value="scope.row.logType"/>
        </template>
      </el-table-column>
      <el-table-column label="使用类型" align="center" prop="usageType" v-if="columns[4].visible">
        <template #default="scope">
          <dict-tag :options="po_points_usage_type" :value="scope.row.usageType"/>
        </template>
      </el-table-column>
      <el-table-column label="目标编号" align="center" prop="targetId" v-if="columns[5].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="使用前积分" align="center" prop="pointsBefore" v-if="columns[6].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="消费积分" align="center" prop="pointsUsed" v-if="columns[7].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="使用后积分" align="center" prop="pointsAfter" v-if="columns[8].visible"
                       :show-overflow-tooltip="true"/>
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
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" v-if="columns[15].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180" v-if="columns[16].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="删除" align="center" prop="isDelete" v-if="columns[17].visible">
        <template #default="scope">
          <dict-tag :options="common_delete" :value="scope.row.isDelete"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['points:pointsUsageLogInfo:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['points:pointsUsageLogInfo:remove']">删除
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

    <!-- 添加或修改积分使用记录对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="pointsUsageLogInfoRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户编号" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户编号"/>
        </el-form-item>
        <el-form-item label="给予用户编号" prop="giveUserId">
          <el-input v-model="form.giveUserId" placeholder="请输入给予用户编号"/>
        </el-form-item>
        <el-form-item label="日志类型" prop="logType">
          <el-select v-model="form.logType" placeholder="请选择日志类型">
            <el-option
                v-for="dict in po_points_usage_log_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="使用类型" prop="usageType">
          <el-select v-model="form.usageType" placeholder="请选择使用类型">
            <el-option
                v-for="dict in po_points_usage_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="目标编号" prop="targetId">
          <el-input v-model="form.targetId" placeholder="请输入目标编号"/>
        </el-form-item>
        <el-form-item label="使用前积分" prop="pointsBefore">
          <el-input v-model="form.pointsBefore" placeholder="请输入使用前积分"/>
        </el-form-item>
        <el-form-item label="消费积分" prop="pointsUsed">
          <el-input v-model="form.pointsUsed" placeholder="请输入消费积分"/>
        </el-form-item>
        <el-form-item label="使用后积分" prop="pointsAfter">
          <el-input v-model="form.pointsAfter" placeholder="请输入使用后积分"/>
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

<script setup name="PointsUsageLogInfo">
import {
  listPointsUsageLogInfo,
  getPointsUsageLogInfo,
  delPointsUsageLogInfo,
  addPointsUsageLogInfo,
  updatePointsUsageLogInfo
} from "@/api/points/pointsUsageLogInfo";

const {proxy} = getCurrentInstance();
const {
  common_delete,
  po_points_usage_log_type,
  po_points_usage_type
} = proxy.useDict('common_delete', 'po_points_usage_log_type', 'po_points_usage_type');

const pointsUsageLogInfoList = ref([]);
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

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    logId: null,
    userId: null,
    giveUserId: null,
    logType: null,
    usageType: null,
    targetId: null,
    deviceId: null,
    browser: null,
    os: null,
    platform: null,
    ipAddr: null,
    createTime: null,
    updateTime: null,
    isDelete: null
  },
  rules: {
    userId: [
      {required: true, message: "用户编号不能为空", trigger: "blur"}
    ],
    usageType: [
      {required: true, message: "使用类型不能为空", trigger: "change"}
    ],
    pointsBefore: [
      {required: true, message: "使用前积分不能为空", trigger: "blur"}
    ],
    pointsUsed: [
      {required: true, message: "消费积分不能为空", trigger: "blur"}
    ],
    pointsAfter: [
      {required: true, message: "使用后积分不能为空", trigger: "blur"}
    ],
    ipAddr: [
      {required: true, message: "IP地址不能为空", trigger: "blur"}
    ],
    createTime: [
      {required: true, message: "创建时间不能为空", trigger: "blur"}
    ],
    updateTime: [
      {required: true, message: "更新时间不能为空", trigger: "blur"}
    ],
    isDelete: [
      {required: true, message: "删除不能为空", trigger: "change"}
    ]
  },
  //表格展示列
  columns: [
    {key: 0, label: '记录编号', visible: false},
    {key: 1, label: '用户编号', visible: true},
    {key: 2, label: '给予用户编号', visible: false},
    {key: 3, label: '日志类型', visible: true},
    {key: 4, label: '使用类型', visible: true},
    {key: 5, label: '目标编号', visible: true},
    {key: 6, label: '使用前积分', visible: true},
    {key: 7, label: '消费积分', visible: true},
    {key: 8, label: '使用后积分', visible: true},
    {key: 9, label: '设备唯一标识', visible: false},
    {key: 10, label: '浏览器类型', visible: false},
    {key: 11, label: '操作系统', visible: false},
    {key: 12, label: '平台', visible: false},
    {key: 13, label: 'IP地址', visible: false},
    {key: 14, label: '备注', visible: false},
    {key: 15, label: '创建时间', visible: true},
    {key: 16, label: '更新时间', visible: false},
    {key: 17, label: '删除', visible: false},
  ],
});

const {queryParams, form, rules, columns} = toRefs(data);

/** 查询积分使用记录列表 */
function getList() {
  loading.value = true;
  queryParams.value.params = {};
  if (null != daterangeCreateTime && '' != daterangeCreateTime) {
    queryParams.value.params["beginCreateTime"] = daterangeCreateTime.value[0];
    queryParams.value.params["endCreateTime"] = daterangeCreateTime.value[1];
  }
  if (null != daterangeUpdateTime && '' != daterangeUpdateTime) {
    queryParams.value.params["beginUpdateTime"] = daterangeUpdateTime.value[0];
    queryParams.value.params["endUpdateTime"] = daterangeUpdateTime.value[1];
  }
  listPointsUsageLogInfo(queryParams.value).then(response => {
    pointsUsageLogInfoList.value = response.rows;
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
    giveUserId: null,
    logType: null,
    usageType: null,
    targetId: null,
    pointsBefore: null,
    pointsUsed: null,
    pointsAfter: null,
    deviceId: null,
    browser: null,
    os: null,
    platform: null,
    ipAddr: null,
    remark: null,
    createTime: null,
    updateTime: null,
    isDelete: null
  };
  proxy.resetForm("pointsUsageLogInfoRef");
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
  title.value = "添加积分使用记录";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _logId = row.logId || ids.value
  getPointsUsageLogInfo(_logId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改积分使用记录";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["pointsUsageLogInfoRef"].validate(valid => {
    if (valid) {
      if (form.value.logId != null) {
        updatePointsUsageLogInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addPointsUsageLogInfo(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除积分使用记录编号为"' + _logIds + '"的数据项？').then(function () {
    return delPointsUsageLogInfo(_logIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('points/pointsUsageLogInfo/export', {
    ...queryParams.value
  }, `pointsUsageLogInfo_${new Date().getTime()}.xlsx`)
}

getList();
</script>
