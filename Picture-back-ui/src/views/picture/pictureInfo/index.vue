<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="图片" prop="pictureId">
        <el-input
            v-model="queryParams.pictureId"
            placeholder="请输入图片"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="图片名称" prop="name">
        <el-input
            v-model="queryParams.name"
            placeholder="请输入图片名称"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="域名" prop="description">
        <el-input
            v-model="queryParams.dnsUrl"
            placeholder="请输入域名URL"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="分类" prop="categoryId">
        <el-input
            v-model="queryParams.categoryId"
            placeholder="请输入分类"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="图片体积" prop="picSize">
        <el-input
            v-model="queryParams.picSize"
            placeholder="请输入图片体积"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="图片宽度" prop="picWidth">
        <el-input
            v-model="queryParams.picWidth"
            placeholder="请输入图片宽度"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="图片高度" prop="picHeight">
        <el-input
            v-model="queryParams.picHeight"
            placeholder="请输入图片高度"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="宽高比例" prop="picScale">
        <el-input
            v-model="queryParams.picScale"
            placeholder="请输入宽高比例"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="图片格式" prop="picFormat">
        <el-input
            v-model="queryParams.picFormat"
            placeholder="请输入图片格式"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="上传用户" prop="userId">
        <el-input
            v-model="queryParams.userId"
            placeholder="请输入上传用户"
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
      <el-form-item label="编辑时间" style="width: 308px">
        <el-date-picker
            v-model="daterangeEditTime"
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
      <el-form-item label="图片状态" prop="pictureStatus">
        <el-select v-model="queryParams.pictureStatus" style="width: 200px" placeholder="请选择图片状态" clearable>
          <el-option
              v-for="dict in p_picture_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="所属空间" prop="spaceId">
        <el-input
            v-model="queryParams.spaceId"
            placeholder="请输入所属空间"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="文件夹" prop="folderId">
        <el-input
            v-model="queryParams.folderId"
            placeholder="请输入所属文件夹"
            clearable
            @keyup.enter="handleQuery"
        />
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
      <el-form-item label="删除时间" style="width: 308px">
        <el-date-picker
            v-model="daterangeDeletedTime"
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
            v-hasPermi="['picture:pictureInfo:add']"
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
            v-hasPermi="['picture:pictureInfo:edit']"
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
            v-hasPermi="['picture:pictureInfo:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['picture:pictureInfo:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="pictureInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="图片" align="center" prop="pictureId" v-if="columns[0].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="图片" align="center" prop="pictureUrl" width="100" v-if="columns[1].visible">
        <template #default="scope">
          <image-preview :src="scope.row.pictureUrl" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="缩略图" align="center" prop="thumbnailUrl" width="100" v-if="columns[2].visible">
        <template #default="scope">
          <image-preview :src="scope.row.thumbnailUrl" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="图片名称" align="center" prop="name" v-if="columns[3].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="域名URL" align="center" prop="dnsUrl" v-if="columns[4].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="简介" align="center" prop="introduction" v-if="columns[5].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="分类" align="center" prop="categoryId" v-if="columns[6].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="图片体积" align="center" prop="picSize" v-if="columns[7].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          {{ formatSize(scope.row.picSize) }}
        </template>
      </el-table-column>
      <el-table-column label="图片宽度" align="center" prop="picWidth" v-if="columns[8].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="图片高度" align="center" prop="picHeight" v-if="columns[9].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="宽高比例" align="center" prop="picScale" v-if="columns[10].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="图片格式" align="center" prop="picFormat" v-if="columns[11].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="上传用户" align="center" prop="userId" v-if="columns[12].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" v-if="columns[13].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="编辑时间" align="center" prop="editTime" width="180" v-if="columns[14].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.editTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180" v-if="columns[15].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="图片状态" align="center" prop="pictureStatus" v-if="columns[16].visible">
        <template #default="scope">
          <dict-tag :options="p_picture_status" :value="scope.row.pictureStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="查看次数" align="center" prop="lookCount" v-if="columns[17].visible"
                       :show-overflow-tooltip="true"/>

      <el-table-column label="收藏次数" align="center" prop="collectCount" v-if="columns[18].visible"
                       :show-overflow-tooltip="true"/>

      <el-table-column label="点赞次数" align="center" prop="likeCount" v-if="columns[19].visible"
                       :show-overflow-tooltip="true"/>

      <el-table-column label="分享次数" align="center" prop="shareCount" v-if="columns[20].visible"
                       :show-overflow-tooltip="true"/>

      <el-table-column label="下载次数" align="center" prop="downloadCount" v-if="columns[21].visible"
                       :show-overflow-tooltip="true"/>

      <el-table-column label="所属空间" align="center" prop="spaceId" v-if="columns[22].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="文件夹" align="center" prop="folderId" v-if="columns[23].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="更多信息" align="center" prop="moreInfo" v-if="columns[24].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="删除" align="center" prop="isDelete" v-if="columns[25].visible">
        <template #default="scope">
          <dict-tag :options="common_delete" :value="scope.row.isDelete"/>
        </template>
      </el-table-column>
      <el-table-column label="删除时间" align="center" prop="deletedTime" width="180" v-if="columns[26].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.deletedTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['picture:pictureInfo:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['picture:pictureInfo:remove']">删除
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

    <!-- 添加或修改图片信息对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="pictureInfoRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="图片" prop="pictureUrl">
          <image-upload v-model="form.thumbnailUrl"/>
        </el-form-item>
        <el-form-item label="图片名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入图片名称"/>
        </el-form-item>
        <el-form-item label="域名URL" prop="dnsUrl">
          <el-input v-model="form.dnsUrl" readonly type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="简介" prop="introduction">
          <el-input v-model="form.introduction" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="分类" prop="categoryId">
          <el-input v-model="form.categoryId" placeholder="请输入分类"/>
        </el-form-item>
        <!--        <el-form-item label="图片体积" prop="picSize">-->
        <!--          <el-input v-model="form.picSize" placeholder="请输入图片体积"/>-->
        <!--        </el-form-item>-->
        <!--        <el-form-item label="图片宽度" prop="picWidth">-->
        <!--          <el-input v-model="form.picWidth" placeholder="请输入图片宽度"/>-->
        <!--        </el-form-item>-->
        <!--        <el-form-item label="图片高度" prop="picHeight">-->
        <!--          <el-input v-model="form.picHeight" placeholder="请输入图片高度"/>-->
        <!--        </el-form-item>-->
        <!--        <el-form-item label="宽高比例" prop="picScale">-->
        <!--          <el-input v-model="form.picScale" placeholder="请输入宽高比例"/>-->
        <!--        </el-form-item>-->
        <!--        <el-form-item label="图片格式" prop="picFormat">-->
        <!--          <el-input v-model="form.picFormat" placeholder="请输入图片格式"/>-->
        <!--        </el-form-item>-->
        <el-form-item label="更多信息" prop="moreInfo">
          <el-input v-model="form.moreInfo" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <!--        <el-form-item label="上传用户" prop="userId">-->
        <!--          <el-input v-model="form.userId" placeholder="请输入上传用户"/>-->
        <!--        </el-form-item>-->
        <el-form-item label="图片状态" prop="pictureStatus">
          <el-radio-group v-model="form.pictureStatus">
            <el-radio
                v-for="dict in p_picture_status"
                :key="dict.value"
                :value="dict.value"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="查看次数" prop="lookCount">
          <el-input-number :min="0" v-model="form.lookCount" placeholder="请输入查看次数"/>
        </el-form-item>
        <el-form-item label="收藏次数" prop="collectCount">
          <el-input-number :min="0" v-model="form.collectCount" placeholder="请输入收藏次数"/>
        </el-form-item>
        <el-form-item label="点赞次数" prop="likeCount">
          <el-input-number :min="0" v-model="form.likeCount" placeholder="请输入点赞次数"/>
        </el-form-item>
        <el-form-item label="分享次数" prop="shareCount">
          <el-input-number :min="0" v-model="form.shareCount" placeholder="请输入分享次数"/>
        </el-form-item>
        <el-form-item label="下载次数" prop="downloadCount">
          <el-input-number :min="0" v-model="form.downloadCount" placeholder="请输入下载次数"/>
        </el-form-item>
        <!--        <el-form-item label="审核人" prop="reviewUserId">-->
        <!--          <el-input v-model="form.reviewUserId" placeholder="请输入审核人"/>-->
        <!--        </el-form-item>-->
        <!--        <el-form-item label="审核时间" prop="reviewTime">-->
        <!--          <el-date-picker clearable-->
        <!--                          v-model="form.reviewTime"-->
        <!--                          type="date"-->
        <!--                          value-format="YYYY-MM-DD"-->
        <!--                          placeholder="请选择审核时间">-->
        <!--          </el-date-picker>-->
        <!--        </el-form-item>-->
        <!--        <el-form-item label="缩略图" prop="thumbnailUrl">-->
        <!--          <image-upload v-model="form.thumbnailUrl"/>-->
        <!--        </el-form-item>-->
        <!--        <el-form-item label="所属空间" prop="spaceId">-->
        <!--          <el-input v-model="form.spaceId" placeholder="请输入所属空间"/>-->
        <!--        </el-form-item>-->
        <!--        <el-form-item label="文件夹" prop="folderId">-->
        <!--          <el-input v-model="form.folderId" placeholder="请输入文件夹"/>-->
        <!--        </el-form-item>-->
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

<script setup name="PictureInfo">
import {
  listPictureInfo,
  getPictureInfo,
  delPictureInfo,
  addPictureInfo,
  updatePictureInfo
} from "@/api/picture/pictureInfo";
import {formatSize} from "@/utils/ruoyi.js";

const {proxy} = getCurrentInstance();
const {
  common_delete,
  p_picture_status
} = proxy.useDict('common_delete', 'p_picture_status');

const pictureInfoList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const daterangeCreateTime = ref([]);
const daterangeEditTime = ref([]);
const daterangeUpdateTime = ref([]);
const daterangeDeletedTime = ref([]);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    pictureId: null,
    name: null,
    dnsUrl: null,
    categoryId: null,
    picSize: null,
    picWidth: null,
    picHeight: null,
    picScale: null,
    picFormat: null,
    userId: null,
    createTime: null,
    editTime: null,
    updateTime: null,
    spaceId: null,
    folderId: null,
    isDelete: null,
    deletedTime: null
  },
  rules: {
    pictureUrl: [
      {required: true, message: "图片不能为空", trigger: "blur"}
    ],
    name: [
      {required: true, message: "图片名称不能为空", trigger: "blur"}
    ],
    categoryId: [
      {required: true, message: "分类不能为空", trigger: "blur"}
    ],
    userId: [
      {required: true, message: "上传用户不能为空", trigger: "blur"}
    ],
    createTime: [
      {required: true, message: "创建时间不能为空", trigger: "blur"}
    ],
    pictureStatus: [
      {required: true, message: "图片状态不能为空", trigger: "change"}
    ],
    isDelete: [
      {required: true, message: "删除不能为空", trigger: "change"}
    ],
  },
  //表格展示列
  columns: [
    {key: 0, label: '图片', visible: false},
    {key: 1, label: '原图', visible: false},
    {key: 2, label: '缩略图', visible: true},
    {key: 3, label: '图片名称', visible: true},
    {key: 4, label: '域名URL', visible: false},
    {key: 5, label: '简介', visible: false},
    {key: 6, label: '分类', visible: true},
    {key: 7, label: '图片体积', visible: true},
    {key: 8, label: '图片宽度', visible: false},
    {key: 9, label: '图片高度', visible: false},
    {key: 10, label: '宽高比例', visible: false},
    {key: 11, label: '图片格式', visible: true},
    {key: 12, label: '上传用户', visible: false},
    {key: 13, label: '创建时间', visible: true},
    {key: 14, label: '编辑时间', visible: false},
    {key: 15, label: '更新时间', visible: false},
    {key: 16, label: '图片状态', visible: true},
    {key: 17, label: '查看次数', visible: true},
    {key: 18, label: '收藏次数', visible: true},
    {key: 19, label: '点赞次数', visible: true},
    {key: 20, label: '分享次数', visible: true},
    {key: 21, label: '下载次数', visible: true},
    {key: 22, label: '所属空间', visible: false},
    {key: 23, label: '文件夹', visible: false},
    {key: 24, label: '更多信息', visible: false},
    {key: 25, label: '删除', visible: false},
    {key: 26, label: '删除时间', visible: false},
  ],
});

const {queryParams, form, rules, columns} = toRefs(data);

/** 查询图片信息列表 */
function getList() {
  loading.value = true;
  queryParams.value.params = {};
  if (null != daterangeCreateTime && '' != daterangeCreateTime) {
    queryParams.value.params["beginCreateTime"] = daterangeCreateTime.value[0];
    queryParams.value.params["endCreateTime"] = daterangeCreateTime.value[1];
  }
  if (null != daterangeEditTime && '' != daterangeEditTime) {
    queryParams.value.params["beginEditTime"] = daterangeEditTime.value[0];
    queryParams.value.params["endEditTime"] = daterangeEditTime.value[1];
  }
  if (null != daterangeUpdateTime && '' != daterangeUpdateTime) {
    queryParams.value.params["beginUpdateTime"] = daterangeUpdateTime.value[0];
    queryParams.value.params["endUpdateTime"] = daterangeUpdateTime.value[1];
  }
  if (null != daterangeDeletedTime && '' != daterangeDeletedTime) {
    queryParams.value.params["beginDeletedTime"] = daterangeDeletedTime.value[0];
    queryParams.value.params["endDeletedTime"] = daterangeDeletedTime.value[1];
  }
  listPictureInfo(queryParams.value).then(response => {
    pictureInfoList.value = response.rows;
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
    pictureId: null,
    pictureUrl: null,
    dnsUrl: null,
    name: null,
    introduction: null,
    categoryId: null,
    picSize: null,
    picWidth: null,
    picHeight: null,
    picScale: null,
    picFormat: null,
    userId: null,
    createTime: null,
    editTime: null,
    updateTime: null,
    pictureStatus: null,
    thumbnailUrl: null,
    lookCount: null,
    collectCount: null,
    likeCount: null,
    shareCount: null,
    downloadCount: null,
    spaceId: null,
    folderId: null,
    moreInfo: null,
    isDelete: null,
    deletedTime: null
  };
  proxy.resetForm("pictureInfoRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  daterangeCreateTime.value = [];
  daterangeEditTime.value = [];
  daterangeUpdateTime.value = [];
  daterangeDeletedTime.value = [];
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.pictureId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加图片信息";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _pictureId = row.pictureId || ids.value
  getPictureInfo(_pictureId).then(response => {
    form.value = response.data;
    form.value.pointsNeed = Number(response.data.pointsNeed);
    open.value = true;
    title.value = "修改图片信息";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["pictureInfoRef"].validate(valid => {
    if (valid) {
      if (form.value.pictureId != null) {
        updatePictureInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addPictureInfo(form.value).then(response => {
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
  const _pictureIds = row.pictureId || ids.value;
  proxy.$modal.confirm('是否确认删除图片信息为"' + _pictureIds + '"的数据项？').then(function () {
    return delPictureInfo(_pictureIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('picture/pictureInfo/export', {
    ...queryParams.value
  }, `pictureInfo_${new Date().getTime()}.xlsx`)
}

getList();
</script>
