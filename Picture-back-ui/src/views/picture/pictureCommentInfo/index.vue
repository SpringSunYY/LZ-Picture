<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="评论编号" prop="commentId">
        <el-input
            v-model="queryParams.commentId"
            placeholder="请输入评论编号"
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
      <el-form-item label="父级评论编号" prop="parentId">
        <el-input
            v-model="queryParams.parentId"
            placeholder="请输入父级评论编号"
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
      <el-form-item label="评论时间" style="width: 308px">
        <el-date-picker
            v-model="daterangeCreateTime"
            value-format="YYYY-MM-DD"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="点赞数" prop="likeCount">
        <el-input
            v-model="queryParams.likeCount"
            placeholder="请输入点赞数"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="评论者IP属地" prop="ipAddress">
        <el-input
            v-model="queryParams.ipAddress"
            placeholder="请输入评论者IP属地"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="评论状态" prop="commentStatus">
        <el-select v-model="queryParams.commentStatus" style="width: 200px" placeholder="请选择评论状态" clearable>
          <el-option
              v-for="dict in p_comment_status"
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
            v-hasPermi="['picture:pictureCommentInfo:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="info"
            plain
            icon="Sort"
            @click="toggleExpandAll"
        >展开/折叠
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table
        v-if="refreshTable"
        v-loading="loading"
        :data="pictureCommentInfoList"
        row-key="commentId"
        :default-expand-all="isExpandAll"
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column label="用户编号" prop="userId" v-if="columns[1].visible" :show-overflow-tooltip="true"/>
      <el-table-column label="父级评论编号" align="center" prop="parentId" v-if="columns[2].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="图片编号" align="center" prop="pictureId" v-if="columns[3].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="图片分类" align="center" prop="categoryId" v-if="columns[4].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="图片标签" align="center" prop="tags" v-if="columns[5].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="评论内容" align="center" prop="content" v-if="columns[6].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="评论时间" align="center" prop="createTime" width="180" v-if="columns[7].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="点赞数" align="center" prop="likeCount" v-if="columns[8].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="评论者IP属地" align="center" prop="ipAddress" v-if="columns[9].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="评论图片URL" align="center" prop="pictureUrl" width="100" v-if="columns[10].visible">
        <template #default="scope">
          <image-preview :src="scope.row.pictureUrl" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="评论状态" align="center" prop="commentStatus" v-if="columns[11].visible">
        <template #default="scope">
          <dict-tag :options="p_comment_status" :value="scope.row.commentStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['picture:pictureCommentInfo:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Plus" @click="handleAdd(scope.row)"
                     v-hasPermi="['picture:pictureCommentInfo:add']">新增
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['picture:pictureCommentInfo:remove']">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改图片评论对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="pictureCommentInfoRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户编号" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户编号"/>
        </el-form-item>
        <el-form-item label="父级评论编号" prop="parentId">
          <el-tree-select
              v-model="form.parentId"
              :data="pictureCommentInfoOptions"
              :props="{ value: 'commentId', label: 'content', children: 'children' }"
              value-key="commentId"
              placeholder="请选择父级评论编号"
              check-strictly
          />
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
        <el-form-item label="评论内容">
          <editor v-model="form.content" :min-height="192"/>
        </el-form-item>
        <el-form-item label="点赞数" prop="likeCount">
          <el-input v-model="form.likeCount" placeholder="请输入点赞数"/>
        </el-form-item>
        <el-form-item label="评论者IP属地" prop="ipAddress">
          <el-input v-model="form.ipAddress" placeholder="请输入评论者IP属地"/>
        </el-form-item>
        <el-form-item label="评论图片URL" prop="pictureUrl">
          <image-upload v-model="form.pictureUrl"/>
        </el-form-item>
        <el-form-item label="评论状态" prop="commentStatus">
          <el-radio-group v-model="form.commentStatus">
            <el-radio
                v-for="dict in p_comment_status"
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

<script setup name="PictureCommentInfo">
import {
  listPictureCommentInfo,
  getPictureCommentInfo,
  delPictureCommentInfo,
  addPictureCommentInfo,
  updatePictureCommentInfo
} from "@/api/picture/pictureCommentInfo";

const {proxy} = getCurrentInstance();
const {p_comment_status} = proxy.useDict('p_comment_status');

const pictureCommentInfoList = ref([]);
const pictureCommentInfoOptions = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const title = ref("");
const isExpandAll = ref(true);
const refreshTable = ref(true);
const daterangeCreateTime = ref([]);

const data = reactive({
  form: {},
  queryParams: {
    commentId: null,
    userId: null,
    parentId: null,
    pictureId: null,
    categoryId: null,
    tags: null,
    content: null,
    createTime: null,
    likeCount: null,
    ipAddress: null,
    commentStatus: null
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
    createTime: [
      {required: true, message: "评论时间不能为空", trigger: "blur"}
    ],
    likeCount: [
      {required: true, message: "点赞数不能为空", trigger: "blur"}
    ],
    commentStatus: [
      {required: true, message: "评论状态不能为空", trigger: "change"}
    ]
  },
  //表格展示列
  columns: [
    {key: 0, label: '评论编号', visible: true},
    {key: 1, label: '用户编号', visible: true},
    {key: 2, label: '父级评论编号', visible: true},
    {key: 3, label: '图片编号', visible: true},
    {key: 4, label: '图片分类', visible: true},
    {key: 5, label: '图片标签', visible: true},
    {key: 6, label: '评论内容', visible: true},
    {key: 7, label: '评论时间', visible: true},
    {key: 8, label: '点赞数', visible: true},
    {key: 9, label: '评论者IP属地', visible: true},
    {key: 10, label: '评论图片URL', visible: true},
    {key: 11, label: '评论状态', visible: true},
  ],
});

const {queryParams, form, rules, columns} = toRefs(data);

/** 查询图片评论列表 */
function getList() {
  loading.value = true;
  queryParams.value.params = {};
  if (null != daterangeCreateTime && '' != daterangeCreateTime) {
    queryParams.value.params["beginCreateTime"] = daterangeCreateTime.value[0];
    queryParams.value.params["endCreateTime"] = daterangeCreateTime.value[1];
  }
  listPictureCommentInfo(queryParams.value).then(response => {
    pictureCommentInfoList.value = proxy.handleTree(response.data, "commentId", "parentId");
    loading.value = false;
  });
}

/** 查询图片评论下拉树结构 */
function getTreeselect() {
  listPictureCommentInfo().then(response => {
    pictureCommentInfoOptions.value = [];
    const data = {commentId: 0, content: '顶级节点', children: []};
    data.children = proxy.handleTree(response.data, "commentId", "parentId");
    pictureCommentInfoOptions.value.push(data);
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
    commentId: null,
    userId: null,
    parentId: null,
    pictureId: null,
    categoryId: null,
    tags: null,
    content: null,
    createTime: null,
    likeCount: null,
    ipAddress: null,
    pictureUrl: null,
    commentStatus: null
  };
  proxy.resetForm("pictureCommentInfoRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  daterangeCreateTime.value = [];
  proxy.resetForm("queryRef");
  handleQuery();
}

/** 新增按钮操作 */
function handleAdd(row) {
  reset();
  getTreeselect();
  if (row != null && row.commentId) {
    form.value.parentId = row.commentId;
  } else {
    form.value.parentId = 0;
  }
  open.value = true;
  title.value = "添加图片评论";
}

/** 展开/折叠操作 */
function toggleExpandAll() {
  refreshTable.value = false;
  isExpandAll.value = !isExpandAll.value;
  nextTick(() => {
    refreshTable.value = true;
  });
}

/** 修改按钮操作 */
async function handleUpdate(row) {
  reset();
  await getTreeselect();
  if (row != null) {
    form.value.parentId = row.commentId;
  }
  getPictureCommentInfo(row.commentId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改图片评论";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["pictureCommentInfoRef"].validate(valid => {
    if (valid) {
      if (form.value.commentId != null) {
        updatePictureCommentInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addPictureCommentInfo(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除图片评论编号为"' + row.commentId + '"的数据项？').then(function () {
    return delPictureCommentInfo(row.commentId);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

getList();
</script>
