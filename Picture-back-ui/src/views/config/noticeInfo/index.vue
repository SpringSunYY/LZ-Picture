<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="公告编号" prop="noticeId">
        <el-input
            v-model="queryParams.noticeId"
            placeholder="请输入公告编号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="语言" prop="locale">
        <el-select
            v-model="queryParams.locale"
            filterable
            remote
            reserve-keyword
            placeholder="请输入简称"
            remote-show-suffix
            :remote-method="remoteGetLocaleList"
            :loading="localeLoading"
            style="width: 200px"
        >
          <el-option
              v-for="item in localeList"
              :key="item.localeId"
              :label="item.locale"
              :value="item.locale"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="通知平台" prop="platform">
        <el-select v-model="queryParams.platform" style="width: 200px" placeholder="请选择通知平台" clearable>
          <el-option
              v-for="dict in u_notice_platform"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="公告类型" prop="noticeType">
        <el-select v-model="queryParams.noticeType" style="width: 200px" placeholder="请选择公告类型" clearable>
          <el-option
              v-for="dict in u_notice_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="是否展示" prop="isExhibit">
        <el-select v-model="queryParams.isExhibit" style="width: 200px" placeholder="请选择是否展示" clearable>
          <el-option
              v-for="dict in u_notice_is_exhibit"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="公告标题" prop="noticeTitle">
        <el-input
            v-model="queryParams.noticeTitle"
            placeholder="请输入公告标题"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="公告状态" prop="noticeStatus">
        <el-select v-model="queryParams.noticeStatus" style="width: 200px" placeholder="请选择公告状态" clearable>
          <el-option
              v-for="dict in u_notice_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="创建人" prop="userId">
        <el-input
            v-model="queryParams.userId"
            placeholder="请输入创建人"
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
            v-hasPermi="['config:noticeInfo:add']"
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
            v-hasPermi="['config:noticeInfo:edit']"
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
            v-hasPermi="['config:noticeInfo:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['config:noticeInfo:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table ref="tableRef" v-loading="loading" :data="noticeInfoList" @selection-change="handleSelectionChange"
              @sort-change="customSort">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="序号" type="index" width="50"/>
      <el-table-column label="公告编号" align="center" prop="noticeId" v-if="columns[0].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="语言" align="center" prop="locale" v-if="columns[1].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="通知平台" align="center" prop="platform" v-if="columns[2].visible">
        <template #default="scope">
          <dict-tag :options="u_notice_platform" :value="scope.row.platform"/>
        </template>
      </el-table-column>
      <el-table-column label="公告类型" align="center" prop="noticeType" v-if="columns[3].visible">
        <template #default="scope">
          <dict-tag :options="u_notice_type" :value="scope.row.noticeType"/>
        </template>
      </el-table-column>
      <el-table-column label="是否展示" align="center" prop="isExhibit" v-if="columns[4].visible">
        <template #default="scope">
          <dict-tag :options="u_notice_is_exhibit" :value="scope.row.isExhibit"/>
        </template>
      </el-table-column>
      <el-table-column label="公告标题" align="center" prop="noticeTitle" v-if="columns[5].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="公告内容" align="center" prop="content" v-if="columns[6].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="排序" align="center" prop="orderNum" v-if="columns[7].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="公告状态" align="center" prop="noticeStatus" v-if="columns[8].visible">
        <template #default="scope">
          <dict-tag :options="u_notice_status" :value="scope.row.noticeStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="创建人" align="center" prop="userName" v-if="columns[9].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="创建时间" align="center" prop="createTime" sortable="custom" width="180"
                       v-if="columns[10].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime" sortable="custom" width="180"
                       v-if="columns[11].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" v-if="columns[12].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['config:noticeInfo:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['config:noticeInfo:remove']">删除
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

    <!-- 添加或修改用户公告对话框 -->
    <el-dialog :title="title" v-model="open" width="800px" append-to-body>
      <el-form ref="noticeInfoRef" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="语言" prop="locale">
              <el-select
                  v-model="form.locale"
                  filterable
                  remote
                  reserve-keyword
                  placeholder="请输入简称"
                  remote-show-suffix
                  :remote-method="remoteGetLocaleList"
                  :loading="localeLoading"
                  style="width: 240px"
              >
                <el-option
                    v-for="item in localeList"
                    :key="item.localeId"
                    :label="item.locale"
                    :value="item.locale"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="通知平台" prop="platform">
              <el-radio-group v-model="form.platform">
                <el-radio
                    v-for="dict in u_notice_platform"
                    :key="dict.value"
                    :value="dict.value"
                >{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="公告类型" prop="noticeType">
              <el-select v-model="form.noticeType" placeholder="请选择公告类型">
                <el-option
                    v-for="dict in u_notice_type"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否展示" prop="isExhibit">
              <el-radio-group v-model="form.isExhibit">
                <el-radio
                    v-for="dict in u_notice_is_exhibit"
                    :key="dict.value"
                    :value="dict.value"
                >{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="公告状态" prop="noticeStatus">
              <el-radio-group v-model="form.noticeStatus">
                <el-radio
                    v-for="dict in u_notice_status"
                    :key="dict.value"
                    :value="dict.value"
                >{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="排序" prop="orderNum">
              <el-input-number :min="0" :max="10" v-model="form.orderNum" placeholder="请输入排序"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="公告标题" prop="noticeTitle">
              <el-input v-model="form.noticeTitle" placeholder="请输入公告标题"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="公告内容">
              <MarkdownEditor v-model="form.content" theme="light"
                              previewTheme="github"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"/>
            </el-form-item>
          </el-col>
        </el-row>
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

<script setup name="NoticeInfo">
import {listNoticeInfo, getNoticeInfo, delNoticeInfo, addNoticeInfo, updateNoticeInfo} from "@/api/config/noticeInfo";
import {listI18nLocaleInfo} from "@/api/config/i18nLocaleInfo.js";
import MarkdownEditor from "@/components/MarkdownEditor/index.vue";

const {proxy} = getCurrentInstance();
const {
  u_notice_is_exhibit,
  u_notice_type,
  u_notice_platform,
  u_notice_status
} = proxy.useDict('u_notice_is_exhibit', 'u_notice_type', 'u_notice_platform', 'u_notice_status');

const noticeInfoList = ref([]);
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
  localeList: [],
  localeLoading: false,
  localeQueryParams: {
    pageNum: 1,
    pageSize: 10,
    locale: '',
    localeStatus: '0',
  },
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    noticeId: null,
    locale: null,
    platform: null,
    noticeType: null,
    isExhibit: null,
    noticeTitle: null,
    noticeStatus: null,
    userId: null,
    createTime: null,
    updateTime: null,
  },
  rules: {
    locale: [
      {required: true, message: "语言 默认zh-CN不能为空", trigger: "blur"}
    ],
    platform: [
      {required: true, message: "通知平台不能为空", trigger: "change"}
    ],
    noticeType: [
      {required: true, message: "公告类型不能为空", trigger: "change"}
    ],
    isExhibit: [
      {required: true, message: "是否展示不能为空", trigger: "change"}
    ],
    noticeTitle: [
      {required: true, message: "公告标题不能为空", trigger: "blur"}
    ],
    content: [
      {required: true, message: "公告内容不能为空", trigger: "blur"}
    ],
    orderNum: [
      {required: true, message: "排序不能为空", trigger: "blur"}
    ],
    noticeStatus: [
      {required: true, message: "公告状态不能为空", trigger: "change"}
    ],
    userId: [
      {required: true, message: "创建人不能为空", trigger: "blur"}
    ],
    createTime: [
      {required: true, message: "创建时间不能为空", trigger: "blur"}
    ],
    updateTime: [
      {required: true, message: "更新时间不能为空", trigger: "blur"}
    ],
  },
  //表格展示列
  columns: [
    {key: 0, label: '公告编号', visible: false},
    {key: 1, label: '语言', visible: true},
    {key: 2, label: '通知平台', visible: true},
    {key: 3, label: '公告类型', visible: true},
    {key: 4, label: '是否展示', visible: true},
    {key: 5, label: '公告标题', visible: true},
    {key: 6, label: '公告内容', visible: true},
    {key: 7, label: '排序', visible: false},
    {key: 8, label: '公告状态', visible: true},
    {key: 9, label: '创建人', visible: true},
    {key: 10, label: '创建时间', visible: true},
    {key: 11, label: '更新时间', visible: false},
    {key: 12, label: '备注', visible: false},
  ],
});

const {
  queryParams, form, rules, columns,
  localeQueryParams, localeList, localeLoading,
} = toRefs(data);

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

/**
 * 远程获取国际化简称
 * @param query
 */
const remoteGetLocaleList = (query) => {
  if (query) {
    console.log(query)
    localeLoading.value = true;
    localeQueryParams.value.locale = query;
    setTimeout(() => {
      getLocaleList()
    }, 200)
  } else {
    if (form.value.locale) {
      localeQueryParams.value.locale = form.value.locale;
    } else {
      localeQueryParams.value.locale = ''
    }
    getLocaleList()
  }
}

/**
 * 获取国际化简称列表
 */
function getLocaleList() {
  listI18nLocaleInfo(localeQueryParams.value).then(response => {
    localeList.value = response.rows;
    localeLoading.value = false
  })
}

getLocaleList()

/** 查询用户公告列表 */
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
  listNoticeInfo(queryParams.value).then(response => {
    noticeInfoList.value = response.rows;
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
    noticeId: null,
    locale: null,
    platform: null,
    noticeType: null,
    isExhibit: null,
    noticeTitle: null,
    content: '',
    orderNum: null,
    noticeStatus: null,
    userId: null,
    createTime: null,
    updateTime: null,
    remark: null
  };
  proxy.resetForm("noticeInfoRef");
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
  proxy.$refs.tableRef.clearSort();
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.noticeId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加用户公告";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _noticeId = row.noticeId || ids.value
  getNoticeInfo(_noticeId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改用户公告";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["noticeInfoRef"].validate(valid => {
    if (valid) {
      if (form.value.noticeId != null) {
        updateNoticeInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addNoticeInfo(form.value).then(response => {
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
  const _noticeIds = row.noticeId || ids.value;
  proxy.$modal.confirm('是否确认删除用户公告编号为"' + _noticeIds + '"的数据项？').then(function () {
    return delNoticeInfo(_noticeIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('config/noticeInfo/export', {
    ...queryParams.value
  }, `noticeInfo_${new Date().getTime()}.xlsx`)
}

getList();
</script>
