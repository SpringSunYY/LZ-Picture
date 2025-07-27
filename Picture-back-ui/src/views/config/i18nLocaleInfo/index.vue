<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="国家地区" prop="localeName">
        <el-input
            v-model="queryParams.localeName"
            placeholder="请输入国家地区"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="简称" prop="locale">
        <el-input
            v-model="queryParams.locale"
            placeholder="请输入简称"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="localeStatus">
        <el-select v-model="queryParams.localeStatus" style="width: 200px" placeholder="请选择状态" clearable>
          <el-option
              v-for="dict in c_locale_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="创建人" prop="createBy">
        <el-input
            v-model="queryParams.createBy"
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
      <el-form-item label="更新人" prop="updateBy">
        <el-input
            v-model="queryParams.updateBy"
            placeholder="请输入更新人"
            clearable
            @keyup.enter="handleQuery"
        />
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
            v-hasPermi="['config:i18nLocaleInfo:add']"
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
            v-hasPermi="['config:i18nLocaleInfo:edit']"
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
            v-hasPermi="['config:i18nLocaleInfo:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['config:i18nLocaleInfo:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="i18nLocaleInfoList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="序号" type="index" width="50"/>
      <el-table-column label="编号" align="center" prop="localeId" v-if="columns[0].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="国家地区" align="center" prop="localeName" v-if="columns[1].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="简称" align="center" prop="locale" v-if="columns[2].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="状态" align="center" prop="localeStatus" v-if="columns[3].visible">
        <template #default="scope">
          <dict-tag :options="c_locale_status" :value="scope.row.localeStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="创建人" align="center" prop="createBy" v-if="columns[4].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" v-if="columns[5].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新人" align="center" prop="updateBy" v-if="columns[6].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180" v-if="columns[7].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" v-if="columns[8].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['config:i18nLocaleInfo:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['config:i18nLocaleInfo:remove']">删除
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

    <!-- 添加或修改国际化国家对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="i18nLocaleInfoRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="国家地区" prop="localeName">
          <el-input v-model="form.localeName" placeholder="请输入国家地区"/>
        </el-form-item>
        <el-form-item label="简称" prop="locale">
          <el-input v-model="form.locale" placeholder="请输入简称"/>
        </el-form-item>
        <el-form-item label="状态" prop="localeStatus">
          <el-radio-group v-model="form.localeStatus">
            <el-radio
                v-for="dict in c_locale_status"
                :key="dict.value"
                :value="dict.value"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
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

<script setup name="I18nLocaleInfo">
import {
  listI18nLocaleInfo,
  getI18nLocaleInfo,
  delI18nLocaleInfo,
  addI18nLocaleInfo,
  updateI18nLocaleInfo
} from "@/api/config/i18nLocaleInfo";

const {proxy} = getCurrentInstance();
const {c_locale_status} = proxy.useDict('c_locale_status');

const i18nLocaleInfoList = ref([]);
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
    localeName: null,
    locale: null,
    localeStatus: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
  },
  rules: {
    localeName: [
      {required: true, message: "国家地区不能为空", trigger: "blur"}
    ],
    locale: [
      {required: true, message: "简称不能为空", trigger: "blur"}
    ],
    localeStatus: [
      {required: true, message: "状态不能为空", trigger: "change"}
    ],
    createBy: [
      {required: true, message: "创建人不能为空", trigger: "blur"}
    ],
    createTime: [
      {required: true, message: "创建时间不能为空", trigger: "blur"}
    ],
  },
  //表格展示列
  columns: [
    {key: 0, label: '编号', visible: false},
    {key: 1, label: '国家地区', visible: true},
    {key: 2, label: '简称', visible: true},
    {key: 3, label: '状态', visible: true},
    {key: 4, label: '创建人', visible: true},
    {key: 5, label: '创建时间', visible: false},
    {key: 6, label: '更新人', visible: false},
    {key: 7, label: '更新时间', visible: false},
    {key: 8, label: '备注', visible: false},
  ],
});

const {queryParams, form, rules, columns} = toRefs(data);

/** 查询国际化国家列表 */
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
  listI18nLocaleInfo(queryParams.value).then(response => {
    i18nLocaleInfoList.value = response.rows;
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
    localeId: null,
    localeName: null,
    locale: null,
    localeStatus: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
    remark: null
  };
  proxy.resetForm("i18nLocaleInfoRef");
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
  ids.value = selection.map(item => item.localeId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加国际化国家";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _localeId = row.localeId || ids.value
  getI18nLocaleInfo(_localeId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改国际化国家";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["i18nLocaleInfoRef"].validate(valid => {
    if (valid) {
      if (form.value.localeId != null) {
        updateI18nLocaleInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addI18nLocaleInfo(form.value).then(response => {
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
  const _localeIds = row.localeId || ids.value;
  proxy.$modal.confirm('是否确认删除国际化国家编号为"' + _localeIds + '"的数据项？').then(function () {
    return delI18nLocaleInfo(_localeIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('config/i18nLocaleInfo/export', {
    ...queryParams.value
  }, `i18nLocaleInfo_${new Date().getTime()}.xlsx`)
}

getList();
</script>
