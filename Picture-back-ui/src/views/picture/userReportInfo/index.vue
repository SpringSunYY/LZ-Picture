<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="举报编号" prop="reportId">
        <el-input
            v-model="queryParams.reportId"
            placeholder="请输入举报编号"
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
      <el-form-item label="举报类型" prop="reportType">
        <el-select v-model="queryParams.reportType" style="width: 200px" placeholder="请选择举报类型" clearable>
          <el-option
              v-for="dict in p_report_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="目标类型" prop="targetType">
        <el-select v-model="queryParams.targetType" style="width: 200px" placeholder="请选择目标类型" clearable>
          <el-option
              v-for="dict in p_report_target_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="目标对象" prop="targetId">
        <el-input
            v-model="queryParams.targetId"
            placeholder="请输入目标对象编号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="举报时间" style="width: 308px">
        <el-date-picker
            v-model="daterangeCreateTime"
            value-format="YYYY-MM-DD"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="审核状态" prop="reviewStatus">
        <el-select v-model="queryParams.reviewStatus" style="width: 200px" placeholder="请选择审核状态" clearable>
          <el-option
              v-for="dict in p_report_review_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="审核人" prop="reviewUserId">
        <el-input
            v-model="queryParams.reviewUserId"
            placeholder="请输入审核人编号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="审核时间" style="width: 308px">
        <el-date-picker
            v-model="daterangeReviewTime"
            value-format="YYYY-MM-DD"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
        ></el-date-picker>
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
            v-hasPermi="['picture:userReportInfo:add']"
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
            v-hasPermi="['picture:userReportInfo:edit']"
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
            v-hasPermi="['picture:userReportInfo:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['picture:userReportInfo:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="userReportInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="举报编号" align="center" prop="reportId" v-if="columns[0].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="用户编号" align="center" prop="userId" v-if="columns[1].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="举报类型" align="center" prop="reportType" v-if="columns[2].visible">
        <template #default="scope">
          <dict-tag :options="p_report_type" :value="scope.row.reportType"/>
        </template>
      </el-table-column>
      <el-table-column label="目标类型" align="center" prop="targetType" v-if="columns[3].visible">
        <template #default="scope">
          <dict-tag :options="p_report_target_type" :value="scope.row.targetType"/>
        </template>
      </el-table-column>
      <el-table-column label="目标对象编号" align="center" prop="targetId" v-if="columns[4].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="封面快照" align="center" prop="targetCover" width="100" v-if="columns[5].visible">
        <template #default="scope">
          <image-preview :src="scope.row.targetCover" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="目标内容" align="center" prop="targetContent" v-if="columns[6].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="举报原因" align="center" prop="reason" v-if="columns[7].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="举报时间" align="center" prop="createTime" width="180" v-if="columns[8].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="审核状态" align="center" prop="reviewStatus" v-if="columns[9].visible">
        <template #default="scope">
          <dict-tag :options="p_report_review_status" :value="scope.row.reviewStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="审核信息" align="center" prop="reviewMessage" v-if="columns[10].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="审核人编号" align="center" prop="reviewUserId" v-if="columns[11].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="审核时间" align="center" prop="reviewTime" width="180" v-if="columns[12].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.reviewTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="设备唯一标识" align="center" prop="deviceId" v-if="columns[13].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="浏览器类型" align="center" prop="browser" v-if="columns[14].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="操作系统" align="center" prop="os" v-if="columns[15].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="平台" align="center" prop="platform" v-if="columns[16].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="IP地址" align="center" prop="ipAddr" v-if="columns[17].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="IP属地" align="center" prop="ipAddress" v-if="columns[18].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['picture:userReportInfo:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['picture:userReportInfo:remove']">删除
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

    <!-- 添加或修改用户举报信息对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="userReportInfoRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户编号" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户编号"/>
        </el-form-item>
        <el-form-item label="举报类型" prop="reportType">
          <el-select v-model="form.reportType" placeholder="请选择举报类型">
            <el-option
                v-for="dict in p_report_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="目标类型" prop="targetType">
          <el-select v-model="form.targetType" placeholder="请选择目标类型">
            <el-option
                v-for="dict in p_report_target_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="举报原因" prop="reason">
          <el-input v-model="form.reason" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="审核状态" prop="reviewStatus">
          <el-radio-group v-model="form.reviewStatus">
            <el-radio
                v-for="dict in p_report_review_status"
                :key="dict.value"
                :value="parseInt(dict.value)"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核信息" prop="reviewMessage">
          <el-input v-model="form.reviewMessage" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="审核时间" prop="reviewTime">
          <el-date-picker clearable
                          v-model="form.reviewTime"
                          type="date"
                          value-format="YYYY-MM-DD"
                          placeholder="请选择审核时间">
          </el-date-picker>
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

<script setup name="UserReportInfo">
import {
  listUserReportInfo,
  getUserReportInfo,
  delUserReportInfo,
  addUserReportInfo,
  updateUserReportInfo
} from "@/api/picture/userReportInfo";

const {proxy} = getCurrentInstance();
const {
  p_report_type,
  p_report_target_type,
  p_report_review_status
} = proxy.useDict('p_report_type', 'p_report_target_type', 'p_report_review_status');

const userReportInfoList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const daterangeCreateTime = ref([]);
const daterangeReviewTime = ref([]);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    reportId: null,
    userId: null,
    reportType: null,
    targetType: null,
    targetId: null,
    targetCover: null,
    reason: null,
    createTime: null,
    reviewStatus: null,
    reviewMessage: null,
    reviewUserId: null,
    reviewTime: null,
    deviceId: null,
    browser: null,
    os: null,
    platform: null,
    ipAddr: null,
    ipAddress: null
  },
  rules: {
    userId: [
      {required: true, message: "用户编号不能为空", trigger: "blur"}
    ],
    reportType: [
      {required: true, message: "举报类型不能为空", trigger: "change"}
    ],
    targetType: [
      {required: true, message: "目标类型不能为空", trigger: "change"}
    ],
    targetId: [
      {required: true, message: "目标对象编号不能为空", trigger: "blur"}
    ],
    reason: [
      {required: true, message: "举报原因不能为空", trigger: "blur"}
    ],
    createTime: [
      {required: true, message: "举报时间不能为空", trigger: "blur"}
    ],
    reviewStatus: [
      {required: true, message: "审核状态不能为空", trigger: "change"}
    ],
  },
  //表格展示列
  columns: [
    {key: 0, label: '举报编号', visible: false},
    {key: 1, label: '用户编号', visible: true},
    {key: 2, label: '举报类型', visible: true},
    {key: 3, label: '目标类型', visible: true},
    {key: 4, label: '目标对象', visible: true},
    {key: 5, label: '封面快照', visible: true},
    {key: 6, label: '目标内容', visible: true},
    {key: 7, label: '举报原因', visible: true},
    {key: 8, label: '举报时间', visible: true},
    {key: 9, label: '审核状态', visible: true},
    {key: 10, label: '审核信息', visible: true},
    {key: 11, label: '审核人编号', visible: true},
    {key: 12, label: '审核时间', visible: true},
    {key: 13, label: '设备唯一标识', visible: false},
    {key: 14, label: '浏览器类型', visible: false},
    {key: 15, label: '操作系统', visible: false},
    {key: 16, label: '平台', visible: false},
    {key: 17, label: 'IP地址', visible: false},
    {key: 18, label: 'IP属地', visible: false},
  ],
});

const {queryParams, form, rules, columns} = toRefs(data);

/** 查询用户举报信息列表 */
function getList() {
  loading.value = true;
  queryParams.value.params = {};
  if (null != daterangeCreateTime && '' != daterangeCreateTime) {
    queryParams.value.params["beginCreateTime"] = daterangeCreateTime.value[0];
    queryParams.value.params["endCreateTime"] = daterangeCreateTime.value[1];
  }
  if (null != daterangeReviewTime && '' != daterangeReviewTime) {
    queryParams.value.params["beginReviewTime"] = daterangeReviewTime.value[0];
    queryParams.value.params["endReviewTime"] = daterangeReviewTime.value[1];
  }
  listUserReportInfo(queryParams.value).then(response => {
    userReportInfoList.value = response.rows;
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
    reportId: null,
    userId: null,
    reportType: null,
    targetType: null,
    targetId: null,
    targetCover: null,
    reason: null,
    createTime: null,
    reviewStatus: null,
    reviewMessage: null,
    reviewUserId: null,
    reviewTime: null,
    deviceId: null,
    browser: null,
    os: null,
    platform: null,
    ipAddr: null,
    ipAddress: null
  };
  proxy.resetForm("userReportInfoRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  daterangeCreateTime.value = [];
  daterangeReviewTime.value = [];
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.reportId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加用户举报信息";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _reportId = row.reportId || ids.value
  getUserReportInfo(_reportId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改用户举报信息";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["userReportInfoRef"].validate(valid => {
    if (valid) {
      if (form.value.reportId != null) {
        updateUserReportInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addUserReportInfo(form.value).then(response => {
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
  const _reportIds = row.reportId || ids.value;
  proxy.$modal.confirm('是否确认删除用户举报信息编号为"' + _reportIds + '"的数据项？').then(function () {
    return delUserReportInfo(_reportIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('picture/userReportInfo/export', {
    ...queryParams.value
  }, `userReportInfo_${new Date().getTime()}.xlsx`)
}

getList();
</script>
