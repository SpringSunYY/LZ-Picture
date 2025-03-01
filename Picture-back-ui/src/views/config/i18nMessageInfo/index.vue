<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="键" prop="messageKey">
        <el-select
            v-model="queryParams.messageKey"
            filterable
            remote
            reserve-keyword
            placeholder="请输入key"
            remote-show-suffix
            :remote-method="remoteGetMessageKeyList"
            :loading="messageKeyLoading"
            style="width: 200px"
        >
          <el-option
              v-for="item in messageKeyList"
              :key="item.keyName"
              :label="item.keyName"
              :value="item.keyName"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="简称" prop="locale">
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
            v-hasPermi="['config:i18nMessageInfo:add']"
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
            v-hasPermi="['config:i18nMessageInfo:edit']"
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
            v-hasPermi="['config:i18nMessageInfo:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['config:i18nMessageInfo:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="i18nMessageInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="主键" align="center" prop="messageId" v-if="columns[0].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="键" align="center" prop="messageKey" v-if="columns[1].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="简称" align="center" prop="locale" v-if="columns[2].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="消息" align="center" prop="message" v-if="columns[3].visible"
                       :show-overflow-tooltip="true"/>
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
                     v-hasPermi="['config:i18nMessageInfo:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['config:i18nMessageInfo:remove']">删除
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

    <!-- 添加或修改国际化信息对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="i18nMessageInfoRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="键" prop="messageKey">
          <el-select
              v-model="form.messageKey"
              filterable
              remote
              reserve-keyword
              allow-create
              placeholder="请输入key"
              remote-show-suffix
              :remote-method="remoteGetMessageKeyList"
              :loading="messageKeyLoading"
              style="width: 240px"
          >
            <el-option
                v-for="item in messageKeyList"
                :key="item.keyName"
                :label="item.keyName"
                :value="item.keyName"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="简称" prop="locale">
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
        <el-form-item label="消息" prop="message">
          <el-input v-model="form.message" type="textarea" placeholder="请输入内容"/>
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

<script setup name="I18nMessageInfo">
import {
  listI18nMessageInfo,
  getI18nMessageInfo,
  delI18nMessageInfo,
  addI18nMessageInfo,
  updateI18nMessageInfo
} from "@/api/config/i18nMessageInfo";
import {listI18nKeyInfo} from "@/api/config/i18nKeyInfo.js";
import {listI18nLocaleInfo} from "@/api/config/i18nLocaleInfo.js";

const {proxy} = getCurrentInstance();

const i18nMessageInfoList = ref([]);
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
  localeList: [],
  localeLoading: false,
  localeQueryParams: {
    pageNum: 1,
    pageSize: 10,
    locale: '',
    localeStatus: '0',
  },
  messageKeyList: [],
  messageKeyLoading: false,
  messageKeyQueryParams: {
    pageNum: 1,
    pageSize: 10,
    keyName: ''
  },
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    messageKey: null,
    locale: null,
    message: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
  },
  rules: {
    messageKey: [
      {required: true, message: "键不能为空", trigger: "blur"}
    ],
    locale: [
      {required: true, message: "简称不能为空", trigger: "blur"}
    ],
    message: [
      {required: true, message: "消息不能为空", trigger: "blur"}
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
    {key: 0, label: '主键', visible: false},
    {key: 1, label: '键', visible: true},
    {key: 2, label: '简称', visible: true},
    {key: 3, label: '消息', visible: true},
    {key: 4, label: '创建人', visible: true},
    {key: 5, label: '创建时间', visible: false},
    {key: 6, label: '更新人', visible: false},
    {key: 7, label: '更新时间', visible: false},
    {key: 8, label: '备注', visible: false},
  ],
});

const {
  queryParams,
  form,
  rules,
  columns,
  localeQueryParams,
  localeList,
  localeLoading,
  messageKeyQueryParams,
  messageKeyList,
  messageKeyLoading
} = toRefs(data);

/**
 * 远程获取国际化键名
 * @param query
 */
const remoteGetMessageKeyList = (query) => {
  if (query) {
    // console.log(query)
    messageKeyLoading.value = true;

    messageKeyQueryParams.value.keyName = query;
    setTimeout(() => {
      getMessageKeyList()
    }, 200)
  } else {
    if (form.value.locale) {
      messageKeyQueryParams.value.keyName = form.value.messageKey;
    } else {
      messageKeyQueryParams.value.keyName = ''
    }
    getMessageKeyList()
  }
}

/**
 * 获取国际化键名列表
 */
function getMessageKeyList() {
  listI18nKeyInfo(messageKeyQueryParams.value).then(response => {
    messageKeyList.value = response.rows;
    messageKeyLoading.value = false
  })
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


/** 查询国际化信息列表 */
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
  listI18nMessageInfo(queryParams.value).then(response => {
    i18nMessageInfoList.value = response.rows;
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
    messageId: null,
    messageKey: null,
    locale: null,
    message: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
    remark: null
  };
  proxy.resetForm("i18nMessageInfoRef");
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
  ids.value = selection.map(item => item.messageId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加国际化信息";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _messageId = row.messageId || ids.value
  getI18nMessageInfo(_messageId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改国际化信息";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["i18nMessageInfoRef"].validate(valid => {
    if (valid) {
      if (form.value.messageId != null) {
        updateI18nMessageInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addI18nMessageInfo(form.value).then(response => {
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
  const _messageIds = row.messageId || ids.value;
  proxy.$modal.confirm('是否确认删除国际化信息编号为"' + _messageIds + '"的数据项？').then(function () {
    return delI18nMessageInfo(_messageIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('config/i18nMessageInfo/export', {
    ...queryParams.value
  }, `i18nMessageInfo_${new Date().getTime()}.xlsx`)
}

//初始化获取基本信息
getList();
getMessageKeyList()
getLocaleList()
</script>
