<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="主键" prop="templateId">
        <el-input
            v-model="queryParams.templateId"
            placeholder="请输入主键"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="模版名称" prop="templateName">
        <el-input
            v-model="queryParams.templateName"
            placeholder="请输入模版名称"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="模版KEY" prop="templateKey">
        <el-input
            v-model="queryParams.templateKey"
            placeholder="请输入模版KEY"
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
            placeholder="请输入语言简称"
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
      <el-form-item label="渠道" prop="channel">
        <el-select v-model="queryParams.channel" style="width: 200px" placeholder="请选择渠道" clearable>
          <el-option
              v-for="dict in c_template_channel"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="模版类型" prop="templateType">
        <el-select v-model="queryParams.templateType" style="width: 200px" placeholder="请选择模版类型" clearable>
          <el-option
              v-for="dict in c_template_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="模版ID" prop="serviceTemplateId">
        <el-input
            v-model="queryParams.serviceTemplateId"
            placeholder="请输入服务商模版ID"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="签名" prop="serviceSignName">
        <el-input
            v-model="queryParams.serviceSignName"
            placeholder="请输入服务商签名"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="通知标题" prop="informTitle">
        <el-input
            v-model="queryParams.informTitle"
            placeholder="请输入通知标题"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" style="width: 200px" placeholder="请选择状态" clearable>
          <el-option
              v-for="dict in c_template_status"
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
            v-hasPermi="['config:informTemplateInfo:add']"
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
            v-hasPermi="['config:informTemplateInfo:edit']"
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
            v-hasPermi="['config:informTemplateInfo:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['config:informTemplateInfo:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="informTemplateInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="主键" align="center" prop="templateId" v-if="columns[0].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="模版名称" align="center" prop="templateName" v-if="columns[1].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="模版KEY" align="center" prop="templateKey" v-if="columns[2].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="语言" align="center" prop="locale" v-if="columns[3].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="渠道" align="center" prop="channel" v-if="columns[4].visible">
        <template #default="scope">
          <dict-tag :options="c_template_channel" :value="scope.row.channel"/>
        </template>
      </el-table-column>
      <el-table-column label="模版类型" align="center" prop="templateType" v-if="columns[5].visible">
        <template #default="scope">
          <dict-tag :options="c_template_type" :value="scope.row.templateType"/>
        </template>
      </el-table-column>
      <el-table-column label="服务商模版ID" align="center" prop="serviceTemplateId" v-if="columns[6].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="服务商签名" align="center" prop="serviceSignName" v-if="columns[7].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="通知标题" align="center" prop="informTitle" v-if="columns[8].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="扩展配置" align="center" prop="extendConfig" v-if="columns[9].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="版本" align="center" prop="templateVersion" v-if="columns[10].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="历史版本" align="center" prop="templateVersionHistory" v-if="columns[11].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="内容" align="center" prop="content" v-if="columns[12].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="事例" align="center" prop="example" v-if="columns[13].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="变量列表" align="center" prop="variables" v-if="columns[14].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="模版样式图" align="center" prop="templateImage" width="100" v-if="columns[15].visible">
        <template #default="scope">
          <image-preview :src="scope.row.templateImage" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" v-if="columns[16].visible">
        <template #default="scope">
          <dict-tag :options="c_template_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="创建人" align="center" prop="createBy" v-if="columns[17].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" v-if="columns[18].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新人" align="center" prop="updateBy" v-if="columns[19].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180" v-if="columns[20].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" v-if="columns[21].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['config:informTemplateInfo:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['config:informTemplateInfo:remove']">删除
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

    <!-- 添加或修改通知模版对话框 -->
    <el-dialog :title="title" v-model="open" width="1200px" append-to-body>
      <el-form ref="informTemplateInfoRef" :model="form" :rules="rules" label-width="80px">
        <el-row :gutter="40">
          <el-col :span="6">
            <el-form-item label="模版名称" prop="templateName">
              <el-input v-model="form.templateName" placeholder="请输入模版名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="模版KEY" prop="templateKey">
              <el-input v-model="form.templateKey" placeholder="请输入模版KEY"/>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="语言" prop="locale">
              <el-select
                  v-model="form.locale"
                  filterable
                  remote
                  reserve-keyword
                  placeholder="请输入语言简称"
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
          <el-col :span="6">
            <el-form-item label="模版类型" prop="templateType">
              <el-select v-model="form.templateType" placeholder="请选择模版类型">
                <el-option
                    v-for="dict in c_template_type"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="渠道" prop="channel">
              <el-select v-model="form.channel" placeholder="请选择渠道">
                <el-option
                    v-for="dict in c_template_channel"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="模版ID" prop="serviceTemplateId">
              <el-input v-model="form.serviceTemplateId" placeholder="请输入服务商模版ID"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="签名" prop="serviceSignName">
              <el-input v-model="form.serviceSignName" placeholder="请输入服务商签名"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="通知标题" prop="informTitle">
              <el-input v-model="form.informTitle" placeholder="请输入通知标题"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio
                    v-for="dict in c_template_status"
                    :key="dict.value"
                    :value="dict.value"
                >{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="内容" prop="content">
              <template #label>
               <span class="custom-label">
                 内容
                 <el-tooltip effect="light" placement="top"
                             content="请输入内容，例：例：你好我是${userName},我想对你说${tell}">
                   <QuestionFilled class="tooltip-icon"/>
                 </el-tooltip>
               </span>
              </template>
              <!--              <el-input type="textarea" placeholder="请输入内容，例：例：你好我是${userName},我想对你说${tell}" :rows="5"-->
              <!--                        v-model="form.content" :min-height="192"/>-->
              <MarkdownEditor v-model="form.content" theme="light"
                              previewTheme="github"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="变量列表" prop="variables">
              <template #label>
               <span class="custom-label">
                 变量列表
                 <el-tooltip effect="light" placement="top"
                             content="请输入变量列表,例：{&quot;userName&quot;: &quot;YY&quot;, &quot;tell&quot;: &quot;你好&quot;}">
                   <QuestionFilled class="tooltip-icon"/>
                 </el-tooltip>
               </span>
              </template>
              <el-input v-model="form.variables" :rows="5" type="textarea"
                        placeholder="请输入变量列表,例：{&quot;userName&quot;: &quot;YY&quot;, &quot;tell&quot;: &quot;你好&quot;}"/>
            </el-form-item>
            <el-form-item label="事例" prop="example">
              <MarkdownBaseEditor v-model="form.example"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="扩展配置" prop="extendConfig">
              <el-input v-model="form.extendConfig" type="textarea" :rows="5"
                        placeholder="请输入内容，请根据实际内容填写json对象"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" :rows="5" type="textarea" placeholder="请输入备注"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模版样式图" prop="templateImage">
              <image-upload v-model="form.templateImage"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-select
              placeholder="请选择版本"
              size="large"
              style="width: 240px;margin-right: 10px"
              v-model="templateVersion"
              @change="changeTemplateVersion"
          >
            <el-option
                v-for="item in filteredTemplateVersions"
                :key="item"
                :label="item"
                :value="item"
            />
          </el-select>
          <el-switch
              v-model="form.saveVersion"
              size="large"
              active-text="保存版本"
              inactive-text="不保存"
          />
          <el-button @click="getTemplateExample">获取事例</el-button>
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="InformTemplateInfo">
import {
  addInformTemplateInfo,
  delInformTemplateInfo,
  getExample,
  getInformTemplateInfo,
  getInformTemplateInfoByVersion,
  listInformTemplateInfo,
  updateInformTemplateInfo
} from "@/api/config/informTemplateInfo";
import {listI18nLocaleInfo} from "@/api/config/i18nLocaleInfo.js";
import MarkdownEditor from "@/components/MarkdownEditor/index.vue";
import {QuestionFilled} from "@element-plus/icons-vue";
import MarkdownBaseEditor from "@/components/MarkdownBaseEditor/MarkdownBaseEditor.vue";

const {proxy} = getCurrentInstance();
const {
  c_template_status,
  c_template_channel,
  c_template_type
} = proxy.useDict('c_template_status', 'c_template_channel', 'c_template_type');

const informTemplateInfoList = ref([]);
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
  //版本
  templateVersion: null,
  markdownKey: null,
  currentVersion: null,
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
    templateId: null,
    templateName: null,
    locale: null,
    channel: null,
    templateType: null,
    serviceTemplateId: null,
    serviceSignName: null,
    status: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
  },
  rules: {
    templateName: [
      {required: true, message: "模版名称不能为空", trigger: "blur"}
    ],
    templateKey: [
      {required: true, message: "模版KEY不能为空", trigger: "blur"}
    ],
    locale: [
      {required: true, message: "语言不能为空", trigger: "blur"}
    ],
    templateType: [
      {required: true, message: "模版类型不能为空", trigger: "change"}
    ],
    templateVersion: [
      {required: true, message: "版本不能为空", trigger: "blur"}
    ],
    templateVersionHistory: [
      {required: true, message: "历史版本不能为空", trigger: "blur"}
    ],
    content: [
      {required: true, message: "内容不能为空", trigger: "blur"}
    ],
    status: [
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
    {key: 0, label: '主键', visible: false},
    {key: 1, label: '模版名称', visible: true},
    {key: 2, label: '模版KEY', visible: true},
    {key: 3, label: '语言', visible: true},
    {key: 4, label: '渠道', visible: true},
    {key: 5, label: '模版类型', visible: true},
    {key: 6, label: '服务商模版ID', visible: false},
    {key: 7, label: '服务商签名', visible: false},
    {key: 8, label: '通知标题', visible: false},
    {key: 9, label: '扩展配置', visible: false},
    {key: 10, label: '版本', visible: true},
    {key: 11, label: '历史版本', visible: false},
    {key: 12, label: '内容', visible: false},
    {key: 13, label: '事例', visible: false},
    {key: 14, label: '变量列表', visible: false},
    {key: 15, label: '模版样式图', visible: true},
    {key: 16, label: '状态', visible: true},
    {key: 17, label: '创建人', visible: false},
    {key: 18, label: '创建时间', visible: false},
    {key: 19, label: '更新人', visible: false},
    {key: 20, label: '更新时间', visible: false},
    {key: 21, label: '备注', visible: false},
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
  templateVersion,
  markdownKey,
  currentVersion
} = toRefs(data);

// 计算属性，过滤小于最大版本的选项
const filteredTemplateVersions = computed(() => {
  var versions = [];
  if (form.value.templateVersion) {
    var maxVersion = parseInt(form.value.templateVersion);
    for (var i = 1; i <= maxVersion; i++) {
      versions.push(i);
    }
  }
  return versions;
})

function changeTemplateVersion() {
  const query = {
    templateId: form.value.templateId,
    templateVersion: templateVersion.value
  }
  getInformTemplateInfoByVersion(query).then(res => {
    const version = form.value.templateVersion;
    form.value = res.data;
    form.value.templateVersion = version;
    form.value.saveVersion = false;
    markdownKey.value = form.value.templateId + templateVersion.value
  })
}

function getTemplateExample() {
  getExample(form.value).then(res => {
    form.value.example = res.msg;
  })
}

/** 查询通知模版列表 */
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
  listInformTemplateInfo(queryParams.value).then(response => {
    informTemplateInfoList.value = response.rows;
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
    templateId: null,
    templateName: null,
    templateKey: null,
    locale: null,
    channel: null,
    templateType: null,
    serviceTemplateId: null,
    serviceSignName: null,
    extendConfig: null,
    templateVersion: 1,
    templateVersionHistory: null,
    content: '',
    example: '',
    variables: null,
    templateImage: null,
    status: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
    remark: null,
    saveVersion: false
  };
  templateVersion.value = form.value.templateVersion
  proxy.resetForm("informTemplateInfoRef");
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
  ids.value = selection.map(item => item.templateId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  markdownKey.value = form.value.templateId
  open.value = true;
  title.value = "添加通知模版";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _templateId = row.templateId || ids.value
  getInformTemplateInfo(_templateId).then(response => {
    form.value = response.data;
    templateVersion.value = response.data.templateVersion;
    currentVersion.value = response.data.templateVersion;
    open.value = true;
    title.value = "修改通知模版";
    markdownKey.value = form.value.templateId
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["informTemplateInfoRef"].validate(valid => {
    if (valid) {
      if (form.value.templateId != null) {
        updateInformTemplateInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addInformTemplateInfo(form.value).then(response => {
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
  const _templateIds = row.templateId || ids.value;
  proxy.$modal.confirm('是否确认删除通知模版编号为"' + _templateIds + '"的数据项？').then(function () {
    return delInformTemplateInfo(_templateIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('config/informTemplateInfo/export', {
    ...queryParams.value
  }, `informTemplateInfo_${new Date().getTime()}.xlsx`)
}

/**
 * 远程获取国际化简称
 * @param query
 */
const remoteGetLocaleList = (query) => {
  if (query) {
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
  localeList.value = []; // 直接清空
  listI18nLocaleInfo(localeQueryParams.value).then(response => {
    localeList.value = response.rows;
    localeLoading.value = false;
  });
}

getLocaleList();
getList();
</script>
<style>
.custom-label {
  display: inline-flex; /* 保持内联弹性布局 */
  align-items: center; /* 垂直居中 */
}

.tooltip-icon {
  width: 14px;
  height: 14px;
  color: #F56C6C;
  margin-left: 5px;
}
</style>
