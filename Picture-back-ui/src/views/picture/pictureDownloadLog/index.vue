<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="下载编号" prop="downloadId">
        <el-input
            v-model="queryParams.downloadId"
            placeholder="请输入下载编号"
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
      <el-form-item label="图片编号" prop="pictureId">
        <el-input
            v-model="queryParams.pictureId"
            placeholder="请输入图片编号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="图片分类" prop="categoryId">
        <el-input
            v-model="queryParams.categoryId"
            placeholder="请输入图片分类"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="图片标签" prop="tags">
        <el-input
            v-model="queryParams.tags"
            placeholder="请输入图片标签"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="空间编号" prop="spaceId">
        <el-input
            v-model="queryParams.spaceId"
            placeholder="请输入空间编号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="下载IP地址" prop="downloadIp">
        <el-input
            v-model="queryParams.downloadIp"
            placeholder="请输入下载IP地址"
            clearable
            @keyup.enter="handleQuery"
        />
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
      <el-form-item label="消耗积分" prop="pointsCost">
        <el-input
            v-model="queryParams.pointsCost"
            placeholder="请输入消耗积分"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否免费" prop="isFree">
        <el-select v-model="queryParams.isFree" style="width: 200px" placeholder="请选择是否免费" clearable>
          <el-option
              v-for="dict in p_download_is_free"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="分成比例" prop="proportion">
        <el-input
            v-model="queryParams.proportion"
            placeholder="请输入分成比例"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="下载时间" style="width: 308px">
        <el-date-picker
            v-model="daterangeCreateTime"
            value-format="YYYY-MM-DD"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="下载状态" prop="downloadStatus">
        <el-select v-model="queryParams.downloadStatus" style="width: 200px" placeholder="请选择下载状态" clearable>
          <el-option
              v-for="dict in p_download_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="下载方式" prop="downloadType">
        <el-select v-model="queryParams.downloadType" style="width: 200px" placeholder="请选择下载方式" clearable>
          <el-option
              v-for="dict in p_download_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="来源" prop="referSource">
        <el-select v-model="queryParams.referSource" style="width: 200px" placeholder="请选择来源" clearable>
          <el-option
              v-for="dict in p_download_refer_source"
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
            v-hasPermi="['picture:pictureDownloadLog:add']"
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
            v-hasPermi="['picture:pictureDownloadLog:edit']"
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
            v-hasPermi="['picture:pictureDownloadLog:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['picture:pictureDownloadLog:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="pictureDownloadLogList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="下载编号" align="center" prop="downloadId" v-if="columns[0].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="用户编号" align="center" prop="userId" v-if="columns[1].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="图片编号" align="center" prop="pictureId" v-if="columns[2].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="图片分类" align="center" prop="categoryId" v-if="columns[3].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="图片标签" align="center" prop="tags" v-if="columns[4].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="空间编号" align="center" prop="spaceId" v-if="columns[5].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="下载IP地址" align="center" prop="downloadIp" v-if="columns[6].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="设备唯一标识" align="center" prop="deviceId" v-if="columns[7].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="浏览器类型" align="center" prop="browser" v-if="columns[8].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="操作系统" align="center" prop="os" v-if="columns[9].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="平台" align="center" prop="platform" v-if="columns[10].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="消耗积分" align="center" prop="pointsCost" v-if="columns[11].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="是否免费" align="center" prop="isFree" v-if="columns[12].visible">
        <template #default="scope">
          <dict-tag :options="p_download_is_free" :value="scope.row.isFree"/>
        </template>
      </el-table-column>
      <el-table-column label="作者分成积分" align="center" prop="pointsAuthorGain" v-if="columns[13].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="平台分成积分" align="center" prop="pointsOfficialGain" v-if="columns[14].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="空间分成积分" align="center" prop="pointsSpaceGain" v-if="columns[15].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="分成比例" align="center" prop="proportion" v-if="columns[16].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="下载时间" align="center" prop="createTime" width="180" v-if="columns[17].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="下载状态" align="center" prop="downloadStatus" v-if="columns[18].visible">
        <template #default="scope">
          <dict-tag :options="p_download_status" :value="scope.row.downloadStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="失败原因" align="center" prop="failReason" v-if="columns[19].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="下载方式" align="center" prop="downloadType" v-if="columns[20].visible">
        <template #default="scope">
          <dict-tag :options="p_download_type" :value="scope.row.downloadType"/>
        </template>
      </el-table-column>
      <el-table-column label="来源" align="center" prop="referSource" v-if="columns[21].visible">
        <template #default="scope">
          <dict-tag :options="p_download_refer_source" :value="scope.row.referSource"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['picture:pictureDownloadLog:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['picture:pictureDownloadLog:remove']">删除
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

    <!-- 添加或修改图片下载记录对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="pictureDownloadLogRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户编号" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户编号"/>
        </el-form-item>
        <el-form-item label="图片编号" prop="pictureId">
          <el-input v-model="form.pictureId" placeholder="请输入图片编号"/>
        </el-form-item>
        <el-form-item label="图片分类" prop="categoryId">
          <el-input v-model="form.categoryId" placeholder="请输入图片分类"/>
        </el-form-item>
        <el-form-item label="图片标签" prop="tags">
          <el-input v-model="form.tags" placeholder="请输入图片标签"/>
        </el-form-item>
        <el-form-item label="空间编号" prop="spaceId">
          <el-input v-model="form.spaceId" placeholder="请输入空间编号"/>
        </el-form-item>
        <el-form-item label="下载IP地址" prop="downloadIp">
          <el-input v-model="form.downloadIp" placeholder="请输入下载IP地址"/>
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
        <el-form-item label="消耗积分" prop="pointsCost">
          <el-input v-model="form.pointsCost" placeholder="请输入消耗积分"/>
        </el-form-item>
        <el-form-item label="是否免费" prop="isFree">
          <el-radio-group v-model="form.isFree">
            <el-radio
                v-for="dict in p_download_is_free"
                :key="dict.value"
                :value="dict.value"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="作者分成积分" prop="pointsAuthorGain">
          <el-input v-model="form.pointsAuthorGain" placeholder="请输入作者分成积分"/>
        </el-form-item>
        <el-form-item label="平台分成积分" prop="pointsOfficialGain">
          <el-input v-model="form.pointsOfficialGain" placeholder="请输入平台分成积分"/>
        </el-form-item>
        <el-form-item label="空间分成积分" prop="pointsSpaceGain">
          <el-input v-model="form.pointsSpaceGain" placeholder="请输入空间分成积分"/>
        </el-form-item>
        <el-form-item label="分成比例" prop="proportion">
          <el-input v-model="form.proportion" placeholder="请输入分成比例"/>
        </el-form-item>
        <el-form-item label="下载状态" prop="downloadStatus">
          <el-radio-group v-model="form.downloadStatus">
            <el-radio
                v-for="dict in p_download_status"
                :key="dict.value"
                :value="dict.value"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="失败原因" prop="failReason">
          <el-input v-model="form.failReason" placeholder="请输入失败原因"/>
        </el-form-item>
        <el-form-item label="下载方式" prop="downloadType">
          <el-select v-model="form.downloadType" placeholder="请选择下载方式">
            <el-option
                v-for="dict in p_download_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="来源" prop="referSource">
          <el-select v-model="form.referSource" placeholder="请选择来源">
            <el-option
                v-for="dict in p_download_refer_source"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            ></el-option>
          </el-select>
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

<script setup name="PictureDownloadLog">
import {
  listPictureDownloadLog,
  getPictureDownloadLog,
  delPictureDownloadLog,
  addPictureDownloadLog,
  updatePictureDownloadLog
} from "@/api/picture/pictureDownloadLog";

const {proxy} = getCurrentInstance();
const {
  p_download_type,
  p_download_refer_source,
  p_download_is_free,
  p_download_status
} = proxy.useDict('p_download_type', 'p_download_refer_source', 'p_download_is_free', 'p_download_status');

const pictureDownloadLogList = ref([]);
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
    downloadId: null,
    userId: null,
    pictureId: null,
    categoryId: null,
    tags: null,
    spaceId: null,
    downloadIp: null,
    deviceId: null,
    browser: null,
    os: null,
    platform: null,
    pointsCost: null,
    isFree: null,
    proportion: null,
    createTime: null,
    downloadStatus: null,
    downloadType: null,
    referSource: null
  },
  rules: {
    userId: [
      {required: true, message: "用户编号不能为空", trigger: "blur"}
    ],
    pictureId: [
      {required: true, message: "图片编号不能为空", trigger: "blur"}
    ],
    categoryId: [
      {required: true, message: "图片分类不能为空", trigger: "blur"}
    ],
    downloadIp: [
      {required: true, message: "下载IP地址不能为空", trigger: "blur"}
    ],
    pointsCost: [
      {required: true, message: "消耗积分不能为空", trigger: "blur"}
    ],
    isFree: [
      {required: true, message: "是否免费不能为空", trigger: "change"}
    ],
    pointsAuthorGain: [
      {required: true, message: "作者分成积分不能为空", trigger: "blur"}
    ],
    pointsOfficialGain: [
      {required: true, message: "平台分成积分不能为空", trigger: "blur"}
    ],
    createTime: [
      {required: true, message: "下载时间不能为空", trigger: "blur"}
    ],
    downloadStatus: [
      {required: true, message: "下载状态不能为空", trigger: "change"}
    ],
    downloadType: [
      {required: true, message: "下载方式不能为空", trigger: "change"}
    ],
  },
  //表格展示列
  columns: [
    {key: 0, label: '下载编号', visible: true},
    {key: 1, label: '用户编号', visible: true},
    {key: 2, label: '图片编号', visible: true},
    {key: 3, label: '图片分类', visible: true},
    {key: 4, label: '图片标签', visible: true},
    {key: 5, label: '空间编号', visible: true},
    {key: 6, label: '下载IP地址', visible: true},
    {key: 7, label: '设备唯一标识', visible: true},
    {key: 8, label: '浏览器类型', visible: true},
    {key: 9, label: '操作系统', visible: true},
    {key: 10, label: '平台', visible: true},
    {key: 11, label: '消耗积分', visible: true},
    {key: 12, label: '是否免费', visible: true},
    {key: 13, label: '作者分成积分', visible: true},
    {key: 14, label: '平台分成积分', visible: true},
    {key: 15, label: '空间分成积分', visible: true},
    {key: 16, label: '分成比例', visible: true},
    {key: 17, label: '下载时间', visible: true},
    {key: 18, label: '下载状态', visible: true},
    {key: 19, label: '失败原因', visible: true},
    {key: 20, label: '下载方式', visible: true},
    {key: 21, label: '来源', visible: true},
  ],
});

const {queryParams, form, rules, columns} = toRefs(data);

/** 查询图片下载记录列表 */
function getList() {
  loading.value = true;
  queryParams.value.params = {};
  if (null != daterangeCreateTime && '' != daterangeCreateTime) {
    queryParams.value.params["beginCreateTime"] = daterangeCreateTime.value[0];
    queryParams.value.params["endCreateTime"] = daterangeCreateTime.value[1];
  }
  listPictureDownloadLog(queryParams.value).then(response => {
    pictureDownloadLogList.value = response.rows;
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
    downloadId: null,
    userId: null,
    pictureId: null,
    categoryId: null,
    tags: null,
    spaceId: null,
    downloadIp: null,
    deviceId: null,
    browser: null,
    os: null,
    platform: null,
    pointsCost: null,
    isFree: null,
    pointsAuthorGain: null,
    pointsOfficialGain: null,
    pointsSpaceGain: null,
    proportion: null,
    createTime: null,
    downloadStatus: null,
    failReason: null,
    downloadType: null,
    referSource: null
  };
  proxy.resetForm("pictureDownloadLogRef");
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
  ids.value = selection.map(item => item.downloadId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加图片下载记录";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _downloadId = row.downloadId || ids.value
  getPictureDownloadLog(_downloadId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改图片下载记录";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["pictureDownloadLogRef"].validate(valid => {
    if (valid) {
      if (form.value.downloadId != null) {
        updatePictureDownloadLog(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addPictureDownloadLog(form.value).then(response => {
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
  const _downloadIds = row.downloadId || ids.value;
  proxy.$modal.confirm('是否确认删除图片下载记录编号为"' + _downloadIds + '"的数据项？').then(function () {
    return delPictureDownloadLog(_downloadIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('picture/pictureDownloadLog/export', {
    ...queryParams.value
  }, `pictureDownloadLog_${new Date().getTime()}.xlsx`)
}

getList();
</script>
