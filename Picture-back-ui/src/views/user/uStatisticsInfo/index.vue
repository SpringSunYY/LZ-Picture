<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="统计编号" prop="statisticsId">
        <el-input
            v-model="queryParams.statisticsId"
            placeholder="请输入统计编号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="统计类型" prop="type">
        <el-select v-model="queryParams.type" style="width: 200px" placeholder="请选择统计类型" clearable>
          <el-option
              v-for="dict in u_statistics_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="统计名称" prop="statisticsName">
        <el-input
            v-model="queryParams.statisticsName"
            placeholder="请输入统计名称"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="KEY" prop="commonKey">
        <el-input
            v-model="queryParams.commonKey"
            placeholder="请输入公共KEY"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="KEY" prop="statisticsKey">
        <el-input
            v-model="queryParams.statisticsKey"
            placeholder="请输入KEY"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="期数" prop="stages">
        <el-input
            v-model="queryParams.stages"
            placeholder="请输入期数"
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
            v-hasPermi="['user:uStatisticsInfo:add']"
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
            v-hasPermi="['user:uStatisticsInfo:edit']"
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
            v-hasPermi="['user:uStatisticsInfo:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['user:uStatisticsInfo:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table ref="tableRef" v-loading="loading" :data="uStatisticsInfoList" @selection-change="handleSelectionChange"
              @sort-change="customSort">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="统计编号" align="center" prop="statisticsId" v-if="columns[0].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="统计类型" align="center" prop="type" v-if="columns[1].visible">
        <template #default="scope">
          <dict-tag :options="u_statistics_type" :value="scope.row.type"/>
        </template>
      </el-table-column>
      <el-table-column label="统计名称" align="center" prop="statisticsName" sortable="custom" v-if="columns[2].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="公共KEY" align="center" prop="commonKey" sortable="custom" v-if="columns[3].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="KEY" align="center" prop="statisticsKey" sortable="custom" v-if="columns[4].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="期数" align="center" prop="stages" sortable="custom" v-if="columns[5].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="统计内容" align="center" prop="content" v-if="columns[6].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="统计内容" align="center" prop="extendContent" v-if="columns[7].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="描述" align="center" prop="remark" v-if="columns[8].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="创建时间" align="center" prop="createTime" sortable="custom" width="180"
                       v-if="columns[9].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['user:uStatisticsInfo:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['user:uStatisticsInfo:remove']">删除
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

    <!-- 添加或修改统计信息对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="uStatisticsInfoRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="统计类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择统计类型">
            <el-option
                v-for="dict in u_statistics_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="统计名称" prop="statisticsName">
          <el-input v-model="form.statisticsName" placeholder="请输入统计名称"/>
        </el-form-item>
        <el-form-item label="公共KEY" prop="commonKey">
          <el-input v-model="form.commonKey" placeholder="请输入公共KEY"/>
        </el-form-item>
        <el-form-item label="KEY" prop="statisticsKey">
          <el-input v-model="form.statisticsKey" placeholder="请输入KEY"/>
        </el-form-item>
        <el-form-item label="期数" prop="stages">
          <el-input v-model="form.stages" placeholder="请输入期数"/>
        </el-form-item>
        <el-form-item label="统计内容">
          <editor v-model="form.content" :min-height="192"/>
        </el-form-item>
        <el-form-item label="统计内容">
          <editor v-model="form.extendContent" :min-height="192"/>
        </el-form-item>
        <el-form-item label="描述" prop="remark">
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

<script setup name="UStatisticsInfo">
import {
  listUStatisticsInfo,
  getUStatisticsInfo,
  delUStatisticsInfo,
  addUStatisticsInfo,
  updateUStatisticsInfo
} from "@/api/user/uStatisticsInfo";

const {proxy} = getCurrentInstance();
const {u_statistics_type} = proxy.useDict('u_statistics_type');

const uStatisticsInfoList = ref([]);
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
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    statisticsId: null,
    type: null,
    statisticsName: null,
    commonKey: null,
    statisticsKey: null,
    stages: null,
    createTime: null
  },
  rules: {
    type: [
      {required: true, message: "统计类型不能为空", trigger: "change"}
    ],
    statisticsName: [
      {required: true, message: "统计名称不能为空", trigger: "blur"}
    ],
    commonKey: [
      {required: true, message: "公共KEY不能为空", trigger: "blur"}
    ],
    statisticsKey: [
      {required: true, message: "KEY不能为空", trigger: "blur"}
    ],
    createTime: [
      {required: true, message: "创建时间不能为空", trigger: "blur"}
    ]
  },
  //表格展示列
  columns: [
    {key: 0, label: '统计编号', visible: false},
    {key: 1, label: '统计类型', visible: true},
    {key: 2, label: '统计名称', visible: true},
    {key: 3, label: '公共KEY', visible: false},
    {key: 4, label: 'KEY', visible: false},
    {key: 5, label: '期数', visible: true},
    {key: 6, label: '统计内容', visible: true},
    {key: 7, label: '统计内容', visible: false},
    {key: 8, label: '描述', visible: false},
    {key: 9, label: '创建时间', visible: true},
  ],
});

const {queryParams, form, rules, columns} = toRefs(data);

/** 查询统计信息列表 */
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
  listUStatisticsInfo(queryParams.value).then(response => {
    uStatisticsInfoList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

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

// 取消按钮
function cancel() {
  open.value = false;
  reset();
}

// 表单重置
function reset() {
  form.value = {
    statisticsId: null,
    type: null,
    statisticsName: null,
    commonKey: null,
    statisticsKey: null,
    stages: null,
    content: null,
    extendContent: null,
    remark: null,
    createTime: null
  };
  proxy.resetForm("uStatisticsInfoRef");
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
  proxy.$refs.tableRef.clearSort();
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.statisticsId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加统计信息";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _statisticsId = row.statisticsId || ids.value
  getUStatisticsInfo(_statisticsId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改统计信息";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["uStatisticsInfoRef"].validate(valid => {
    if (valid) {
      if (form.value.statisticsId != null) {
        updateUStatisticsInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addUStatisticsInfo(form.value).then(response => {
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
  const _statisticsIds = row.statisticsId || ids.value;
  proxy.$modal.confirm('是否确认删除统计信息编号为"' + _statisticsIds + '"的数据项？').then(function () {
    return delUStatisticsInfo(_statisticsIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('user/uStatisticsInfo/export', {
    ...queryParams.value
  }, `uStatisticsInfo_${new Date().getTime()}.xlsx`)
}

getList();
</script>
