<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="申请编号" prop="applyId">
        <el-input
            v-model="queryParams.applyId"
            placeholder="请输入申请编号"
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
      <el-form-item label="图片名称" prop="pictureName">
        <el-input
            v-model="queryParams.pictureName"
            placeholder="请输入图片名称"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="申请类型" prop="applyType">
        <el-select v-model="queryParams.applyType" style="width: 200px" placeholder="请选择申请类型" clearable>
          <el-option
              v-for="dict in p_picture_apply_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="用户" prop="userId">
        <el-input
            v-model="queryParams.userId"
            placeholder="请输入用户"
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
      <el-form-item label="审核状态" prop="reviewStatus">
        <el-select v-model="queryParams.reviewStatus" style="width: 200px" placeholder="请选择审核状态" clearable>
          <el-option
              v-for="dict in p_picture_apply_status"
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
            v-hasPermi="['picture:pictureApplyInfo:add']"
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
            v-hasPermi="['picture:pictureApplyInfo:edit']"
        >修改/审核
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="danger"
            plain
            icon="Delete"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['picture:pictureApplyInfo:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['picture:pictureApplyInfo:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="pictureApplyInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="申请编号" align="center" prop="applyId" v-if="columns[0].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="图片编号" align="center" prop="pictureId" v-if="columns[1].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="图片名称" align="center" prop="pictureName" v-if="columns[2].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="缩略图 URL" align="center" prop="thumbnailUrl" width="100" v-if="columns[3].visible">
        <template #default="scope">
          <image-preview :src="scope.row.thumbnailUrl" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="申请类型" align="center" prop="applyType" v-if="columns[4].visible">
        <template #default="scope">
          <dict-tag :options="p_picture_apply_type" :value="scope.row.applyType"/>
        </template>
      </el-table-column>
      <el-table-column label="申请理由" align="center" prop="applyReason" v-if="columns[5].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="证明图片" align="center" prop="applyImage" width="100" v-if="columns[6].visible">
        <template #default="scope">
          <image-preview :src="scope.row.applyImage" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="证明文件" align="center" prop="applyFile" v-if="columns[7].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="联系方式" align="center" prop="contact" v-if="columns[8].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="所需积分" align="center" prop="pointsNeed" v-if="columns[9].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="所需金额" align="center" prop="priceNeed" v-if="columns[10].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="用户" align="center" prop="userId" v-if="columns[11].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" v-if="columns[12].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180" v-if="columns[13].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="审核状态" align="center" prop="reviewStatus" v-if="columns[14].visible">
        <template #default="scope">
          <dict-tag :options="p_picture_apply_status" :value="scope.row.reviewStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="审核信息" align="center" prop="reviewMessage" v-if="columns[15].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="审核人编号" align="center" prop="reviewUserId" v-if="columns[16].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="审核时间" align="center" prop="reviewTime" width="180" v-if="columns[17].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.reviewTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['picture:pictureApplyInfo:edit']">修改/审核
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['picture:pictureApplyInfo:remove']">删除
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

    <!-- 添加或修改图片申请信息对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="pictureApplyInfoRef" :model="form" :rules="rules" label-width="80px">
        <!--        <el-form-item label="图片编号" prop="pictureId">-->
        <!--          <el-input v-model="form.pictureId" placeholder="请输入图片编号"/>-->
        <!--        </el-form-item>-->
        <el-form-item label="图片名称" prop="pictureName">
          <el-input v-model="form.pictureName" placeholder="请输入图片名称"/>
        </el-form-item>
        <el-form-item label="缩略图 URL" prop="thumbnailUrl">
          <image-upload v-model="form.thumbnailUrl"/>
        </el-form-item>
        <el-form-item label="申请类型" prop="applyType">
          <el-select v-model="form.applyType" placeholder="请选择申请类型">
            <el-option
                v-for="dict in p_picture_apply_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="申请理由" prop="applyReason">
          <el-input v-model="form.applyReason" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="证明图片" prop="applyImage" v-if="form.applyImage!==''">
          <image-preview :src="form.applyImage"/>
        </el-form-item>
        <el-form-item label="证明文件" prop="applyFile" v-if="form.applyFile!==''">
          <file-upload v-model="form.applyFile"/>
        </el-form-item>
        <el-form-item label="联系方式" prop="contact">
          <el-input v-model="form.contact" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="所需积分" prop="pointsNeed"
                      v-if="form.applyType !== '' && form.applyType !== '0'">
          <el-input-number :min="0" v-model="form.pointsNeed" placeholder="请输入所需积分"/>
        </el-form-item>
        <el-form-item label="所需金额" prop="priceNeed" v-if="form.applyType === '0'">
          <el-input-number :min="0" :precision="2" v-model="form.priceNeed" placeholder="请输入所需金额"/>
        </el-form-item>
        <el-form-item label="审核状态" prop="reviewStatus">
          <el-radio-group v-model="form.reviewStatus">
            <el-radio
                v-for="dict in p_picture_apply_status"
                :key="dict.value"
                :value="dict.value"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核信息" prop="reviewMessage">
          <el-input v-model="form.reviewMessage" type="textarea" placeholder="请输入内容"/>
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

<script setup name="PictureApplyInfo">
import {
  listPictureApplyInfo,
  getPictureApplyInfo,
  delPictureApplyInfo,
  addPictureApplyInfo,
  updatePictureApplyInfo
} from "@/api/picture/pictureApplyInfo";
import ImagePreview from "@/components/ImagePreview/index.vue";

const {proxy} = getCurrentInstance();
const {p_picture_apply_status, p_picture_apply_type} = proxy.useDict('p_picture_apply_status', 'p_picture_apply_type');

const pictureApplyInfoList = ref([]);
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
const daterangeReviewTime = ref([]);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    applyId: null,
    pictureId: null,
    pictureName: null,
    applyType: null,
    userId: null,
    createTime: null,
    updateTime: null,
    reviewStatus: null,
    reviewUserId: null,
    reviewTime: null
  },
  rules: {
    pictureId: [
      {required: true, message: "图片编号不能为空", trigger: "blur"}
    ],
    pictureName: [
      {required: true, message: "图片名称不能为空", trigger: "blur"}
    ],
    thumbnailUrl: [
      {required: true, message: "缩略图 URL不能为空", trigger: "blur"}
    ],
    applyType: [
      {required: true, message: "申请类型不能为空", trigger: "change"}
    ],
    applyReason: [
      {required: true, message: "申请理由不能为空", trigger: "blur"}
    ],
    contact: [
      {required: true, message: "联系方式不能为空", trigger: "blur"}
    ],
    pointsNeed: [
      {required: true, message: "所需积分不能为空", trigger: "blur"}
    ],
    userId: [
      {required: true, message: "用户不能为空", trigger: "blur"}
    ],
    createTime: [
      {required: true, message: "创建时间不能为空", trigger: "blur"}
    ],
    reviewStatus: [
      {required: true, message: "审核状态不能为空", trigger: "change"}
    ],
  },
  //表格展示列
  columns: [
    {key: 0, label: '申请编号', visible: false},
    {key: 1, label: '图片编号', visible: false},
    {key: 2, label: '图片名称', visible: true},
    {key: 3, label: '缩略图 URL', visible: true},
    {key: 4, label: '申请类型', visible: true},
    {key: 5, label: '申请理由', visible: true},
    {key: 6, label: '证明图片', visible: true},
    {key: 7, label: '证明文件', visible: true},
    {key: 8, label: '联系方式', visible: true},
    {key: 9, label: '所需积分', visible: true},
    {key: 10, label: '所需金额', visible: true},
    {key: 11, label: '用户', visible: false},
    {key: 12, label: '创建时间', visible: true},
    {key: 13, label: '更新时间', visible: false},
    {key: 14, label: '审核状态', visible: true},
    {key: 15, label: '审核信息', visible: false},
    {key: 16, label: '审核人编号', visible: false},
    {key: 17, label: '审核时间', visible: true},
  ],
});

const {queryParams, form, rules, columns} = toRefs(data);

/** 查询图片申请信息列表 */
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
  if (null != daterangeReviewTime && '' != daterangeReviewTime) {
    queryParams.value.params["beginReviewTime"] = daterangeReviewTime.value[0];
    queryParams.value.params["endReviewTime"] = daterangeReviewTime.value[1];
  }
  listPictureApplyInfo(queryParams.value).then(response => {
    pictureApplyInfoList.value = response.rows;
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
    applyId: null,
    pictureId: null,
    pictureName: null,
    thumbnailUrl: null,
    applyType: null,
    applyReason: null,
    applyImage: null,
    applyFile: null,
    contact: null,
    pointsNeed: null,
    priceNeed: null,
    userId: null,
    createTime: null,
    updateTime: null,
    reviewStatus: null,
    reviewMessage: null,
    reviewUserId: null,
    reviewTime: null
  };
  proxy.resetForm("pictureApplyInfoRef");
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
  daterangeReviewTime.value = [];
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.applyId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加图片申请信息";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _applyId = row.applyId || ids.value
  getPictureApplyInfo(_applyId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改图片申请信息";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["pictureApplyInfoRef"].validate(valid => {
    if (valid) {
      if (form.value.applyId != null) {
        updatePictureApplyInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addPictureApplyInfo(form.value).then(response => {
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
  const _applyIds = row.applyId || ids.value;
  proxy.$modal.confirm('是否确认删除图片申请信息编号为"' + _applyIds + '"的数据项？').then(function () {
    return delPictureApplyInfo(_applyIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('picture/pictureApplyInfo/export', {
    ...queryParams.value
  }, `pictureApplyInfo_${new Date().getTime()}.xlsx`)
}

getList();
</script>
