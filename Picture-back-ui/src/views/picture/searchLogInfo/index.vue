<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="记录编号" prop="searchId">
        <el-input
            v-model="queryParams.searchId"
            placeholder="请输入搜索记录编号"
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
      <el-form-item label="关键词" prop="keyword">
        <el-input
            v-model="queryParams.keyword"
            placeholder="请输入搜索关键词"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="搜索类型" prop="searchType">
        <el-select v-model="queryParams.searchType" style="width: 200px" placeholder="请选择搜索类型" clearable>
          <el-option
              v-for="dict in p_search_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="搜索来源" prop="referSource">
        <el-select v-model="queryParams.referSource" style="width: 200px" placeholder="请选择搜索来源" clearable>
          <el-option
              v-for="dict in p_search_refer_source"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="搜索状态" prop="searchStatus">
        <el-select v-model="queryParams.searchStatus" style="width: 200px" placeholder="请选择搜索状态" clearable>
          <el-option
              v-for="dict in p_search_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="返回数量" prop="resultCount">
        <el-input
            v-model="queryParams.resultCount"
            placeholder="请输入返回数量"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="搜索时间" style="width: 308px">
        <el-date-picker
            v-model="daterangeCreateTime"
            value-format="YYYY-MM-DD"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="搜索时长" prop="searchDuration">
        <el-input
            v-model="queryParams.searchDuration"
            placeholder="请输入搜索时长"
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
      <el-form-item label="IP属地" prop="ipAddress">
        <el-input
            v-model="queryParams.ipAddress"
            placeholder="请输入IP属地"
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
            v-hasPermi="['picture:searchLogInfo:add']"
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
            v-hasPermi="['picture:searchLogInfo:edit']"
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
            v-hasPermi="['picture:searchLogInfo:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['picture:searchLogInfo:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="searchLogInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="搜索记录编号" align="center" prop="searchId" v-if="columns[0].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="用户编号" align="center" prop="userId" v-if="columns[1].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="搜索关键词" align="center" prop="keyword" v-if="columns[2].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="搜索类型" align="center" prop="searchType" v-if="columns[3].visible">
        <template #default="scope">
          <dict-tag :options="p_search_type" :value="scope.row.searchType"/>
        </template>
      </el-table-column>
      <el-table-column label="搜索来源" align="center" prop="referSource" v-if="columns[4].visible">
        <template #default="scope">
          <dict-tag :options="p_search_refer_source" :value="scope.row.referSource"/>
        </template>
      </el-table-column>
      <el-table-column label="搜索状态" align="center" prop="searchStatus" v-if="columns[5].visible">
        <template #default="scope">
          <dict-tag :options="p_search_status" :value="scope.row.searchStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="失败原因" align="center" prop="failReason" v-if="columns[6].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="返回数量" align="center" prop="resultCount" v-if="columns[7].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="搜索时间" align="center" prop="createTime" width="180" v-if="columns[8].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="搜索时长" align="center" prop="searchDuration" v-if="columns[9].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="设备唯一标识" align="center" prop="deviceId" v-if="columns[10].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="浏览器类型" align="center" prop="browser" v-if="columns[11].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="操作系统" align="center" prop="os" v-if="columns[12].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="平台" align="center" prop="platform" v-if="columns[13].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="IP属地" align="center" prop="ipAddress" v-if="columns[14].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="IP地址" align="center" prop="ipAddr" v-if="columns[15].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['picture:searchLogInfo:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['picture:searchLogInfo:remove']">删除
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

    <!-- 添加或修改用户搜索记录对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="searchLogInfoRef" :model="form" :rules="rules" label-width="80px">
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

<script setup name="SearchLogInfo">
import {
  listSearchLogInfo,
  getSearchLogInfo,
  delSearchLogInfo,
  addSearchLogInfo,
  updateSearchLogInfo
} from "@/api/picture/searchLogInfo";

const {proxy} = getCurrentInstance();
const {
  p_search_type,
  p_search_status,
  p_search_refer_source
} = proxy.useDict('p_search_type', 'p_search_status', 'p_search_refer_source');

const searchLogInfoList = ref([]);
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
    searchId: null,
    userId: null,
    keyword: null,
    searchType: null,
    referSource: null,
    searchStatus: null,
    resultCount: null,
    createTime: null,
    searchDuration: null,
    deviceId: null,
    browser: null,
    os: null,
    platform: null,
    ipAddress: null,
    ipAddr: null
  },
  rules: {
    keyword: [
      {required: true, message: "搜索关键词不能为空", trigger: "blur"}
    ],
    searchType: [
      {required: true, message: "搜索类型不能为空", trigger: "change"}
    ],
    searchStatus: [
      {required: true, message: "搜索状态不能为空", trigger: "change"}
    ],
    resultCount: [
      {required: true, message: "返回数量不能为空", trigger: "blur"}
    ],
    createTime: [
      {required: true, message: "搜索时间不能为空", trigger: "blur"}
    ],
  },
  //表格展示列
  columns: [
    {key: 0, label: '搜索记录编号', visible: false},
    {key: 1, label: '用户编号', visible: true},
    {key: 2, label: '搜索关键词', visible: true},
    {key: 3, label: '搜索类型', visible: true},
    {key: 4, label: '搜索来源', visible: true},
    {key: 5, label: '搜索状态', visible: true},
    {key: 6, label: '失败原因', visible: false},
    {key: 7, label: '返回数量', visible: true},
    {key: 8, label: '搜索时间', visible: true},
    {key: 9, label: '搜索时长', visible: true},
    {key: 10, label: '设备唯一标识', visible: false},
    {key: 11, label: '浏览器类型', visible: false},
    {key: 12, label: '操作系统', visible: false},
    {key: 13, label: '平台', visible: false},
    {key: 14, label: 'IP属地', visible: false},
    {key: 15, label: 'IP地址', visible: false},
  ],
});

const {queryParams, form, rules, columns} = toRefs(data);

/** 查询用户搜索记录列表 */
function getList() {
  loading.value = true;
  queryParams.value.params = {};
  if (null != daterangeCreateTime && '' != daterangeCreateTime) {
    queryParams.value.params["beginCreateTime"] = daterangeCreateTime.value[0];
    queryParams.value.params["endCreateTime"] = daterangeCreateTime.value[1];
  }
  listSearchLogInfo(queryParams.value).then(response => {
    searchLogInfoList.value = response.rows;
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
    searchId: null,
    userId: null,
    keyword: null,
    searchType: null,
    referSource: null,
    searchStatus: null,
    failReason: null,
    resultCount: null,
    createTime: null,
    searchDuration: null,
    deviceId: null,
    browser: null,
    os: null,
    platform: null,
    ipAddress: null,
    ipAddr: null
  };
  proxy.resetForm("searchLogInfoRef");
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
  ids.value = selection.map(item => item.searchId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加用户搜索记录";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _searchId = row.searchId || ids.value
  getSearchLogInfo(_searchId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改用户搜索记录";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["searchLogInfoRef"].validate(valid => {
    if (valid) {
      if (form.value.searchId != null) {
        updateSearchLogInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addSearchLogInfo(form.value).then(response => {
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
  const _searchIds = row.searchId || ids.value;
  proxy.$modal.confirm('是否确认删除用户搜索记录编号为"' + _searchIds + '"的数据项？').then(function () {
    return delSearchLogInfo(_searchIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('picture/searchLogInfo/export', {
    ...queryParams.value
  }, `searchLogInfo_${new Date().getTime()}.xlsx`)
}

getList();
</script>
