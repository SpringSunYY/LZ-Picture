<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="编号" prop="folderId">
        <el-input
            v-model="queryParams.folderId"
            placeholder="请输入文件夹编号"
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
      <el-form-item label="父编号" prop="parentId">
        <el-input
            v-model="queryParams.parentId"
            placeholder="请输入父文件夹编号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="文件夹名" prop="folderName">
        <el-input
            v-model="queryParams.folderName"
            placeholder="请输入文件夹名称"
            clearable
            @keyup.enter="handleQuery"
        />
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
      <!--      <el-form-item label="更新时间" style="width: 308px">-->
      <!--        <el-date-picker-->
      <!--            v-model="daterangeUpdateTime"-->
      <!--            value-format="YYYY-MM-DD"-->
      <!--            type="daterange"-->
      <!--            range-separator="-"-->
      <!--            start-placeholder="开始日期"-->
      <!--            end-placeholder="结束日期"-->
      <!--        ></el-date-picker>-->
      <!--      </el-form-item>-->
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
            v-hasPermi="['picture:spaceFolderInfo:add']"
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
        :data="spaceFolderInfoList"
        row-key="folderId"
        :default-expand-all="isExpandAll"
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column label="空间编号" prop="spaceId" v-if="columns[1].visible" :show-overflow-tooltip="true"
                       @sort-change="customSort"/>
      <el-table-column label="父文件夹编号" align="center" prop="parentId" v-if="columns[2].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="祖级列表" align="center" prop="ancestors" v-if="columns[3].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="文件夹名称" align="center" prop="folderName" v-if="columns[4].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="完整路径" align="center" prop="fullPath" v-if="columns[5].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="层级" align="center" prop="folderLevel" v-if="columns[6].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="创建人" align="center" prop="userId" v-if="columns[7].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="排序权重" align="center" prop="sortOrder" v-if="columns[8].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" v-if="columns[9].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180" v-if="columns[10].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" v-if="columns[11].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['picture:spaceFolderInfo:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Plus" @click="handleAdd(scope.row)"
                     v-hasPermi="['picture:spaceFolderInfo:add']">新增
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['picture:spaceFolderInfo:remove']">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改空间文件夹对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="spaceFolderInfoRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="空间编号" prop="spaceId">
          <el-input v-model="form.spaceId" placeholder="请输入空间编号"/>
        </el-form-item>
        <el-form-item label="父文件夹编号" prop="parentId">
          <el-tree-select
              v-model="form.parentId"
              :data="spaceFolderInfoOptions"
              :props="{ value: 'folderId', label: 'folderName', children: 'children' }"
              value-key="folderId"
              placeholder="请选择父文件夹编号"
              check-strictly
          />
        </el-form-item>
        <el-form-item label="文件夹名称" prop="folderName">
          <el-input v-model="form.folderName" placeholder="请输入文件夹名称"/>
        </el-form-item>
        <el-form-item label="完整路径" prop="fullPath">
          <el-input v-model="form.fullPath" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="创建人" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入创建人"/>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注"/>
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

<script setup name="SpaceFolderInfo">
import {
  listSpaceFolderInfo,
  getSpaceFolderInfo,
  delSpaceFolderInfo,
  addSpaceFolderInfo,
  updateSpaceFolderInfo
} from "@/api/picture/spaceFolderInfo";

const {proxy} = getCurrentInstance();

const spaceFolderInfoList = ref([]);
const spaceFolderInfoOptions = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const title = ref("");
const isExpandAll = ref(true);
const refreshTable = ref(true);
const daterangeCreateTime = ref([]);
const daterangeUpdateTime = ref([]);

const isAsc = ref();
const orderByColumn = ref('');
const data = reactive({
  form: {},
  queryParams: {
    folderId: null,
    spaceId: null,
    parentId: null,
    folderName: null,
    folderLevel: null,
    userId: null,
    createTime: null,
    updateTime: null,
  },
  rules: {
    spaceId: [
      {required: true, message: "空间编号不能为空", trigger: "blur"}
    ],
    parentId: [
      {required: true, message: "父文件夹编号不能为空", trigger: "blur"}
    ],
    ancestors: [
      {required: true, message: "祖级列表不能为空", trigger: "blur"}
    ],
    folderName: [
      {required: true, message: "文件夹名称不能为空", trigger: "blur"}
    ],
    fullPath: [
      {required: true, message: "完整路径不能为空", trigger: "blur"}
    ],
    folderLevel: [
      {required: true, message: "层级不能为空", trigger: "blur"}
    ],
    userId: [
      {required: true, message: "创建人不能为空", trigger: "blur"}
    ],
    sortOrder: [
      {required: true, message: "排序权重不能为空", trigger: "blur"}
    ],
    createTime: [
      {required: true, message: "创建时间不能为空", trigger: "blur"}
    ],
  },
  //表格展示列
  columns: [
    {key: 0, label: '文件夹编号', visible: true},
    {key: 1, label: '空间编号', visible: false},
    {key: 2, label: '父文件夹编号', visible: true},
    {key: 3, label: '祖级列表', visible: false},
    {key: 4, label: '文件夹名称', visible: true},
    {key: 5, label: '完整路径', visible: true},
    {key: 6, label: '层级', visible: true},
    {key: 7, label: '创建人', visible: true},
    {key: 8, label: '排序权重', visible: true},
    {key: 9, label: '创建时间', visible: true},
    {key: 10, label: '更新时间', visible: false},
    {key: 11, label: '备注', visible: false},
  ],
});

const {queryParams, form, rules, columns} = toRefs(data);

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


/** 查询空间文件夹列表 */
function getList() {
  loading.value = true;
 queryParams.value.params = {}; if (orderByColumn.value != null && isAsc.value !== null) {
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
  listSpaceFolderInfo(queryParams.value).then(response => {
    spaceFolderInfoList.value = proxy.handleTree(response.data, "folderId", "parentId");
    loading.value = false;
  });
}

/** 查询空间文件夹下拉树结构 */
function getTreeselect() {
  listSpaceFolderInfo().then(response => {
    spaceFolderInfoOptions.value = [];
    const data = {folderId: 0, folderName: '顶级节点', children: []};
    data.children = proxy.handleTree(response.data, "folderId", "parentId");
    spaceFolderInfoOptions.value.push(data);
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
    folderId: null,
    spaceId: null,
    parentId: null,
    ancestors: null,
    folderName: null,
    fullPath: null,
    folderLevel: null,
    userId: null,
    sortOrder: null,
    createTime: null,
    updateTime: null,
    remark: null
  };
  proxy.resetForm("spaceFolderInfoRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  daterangeCreateTime.value = [];
  daterangeUpdateTime.value = [];
    orderByColumn.value = null
  isAsc.value = null;
  proxy.resetForm("queryRef");
  handleQuery();
}

/** 新增按钮操作 */
function handleAdd(row) {
  reset();
  getTreeselect();
  if (row != null && row.folderId) {
    form.value.parentId = row.folderId;
  } else {
    form.value.parentId = 0;
  }
  open.value = true;
  title.value = "添加空间文件夹";
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
    form.value.parentId = row.folderId;
  }
  getSpaceFolderInfo(row.folderId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改空间文件夹";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["spaceFolderInfoRef"].validate(valid => {
    if (valid) {
      if (form.value.folderId != null) {
        updateSpaceFolderInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addSpaceFolderInfo(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除空间文件夹编号为"' + row.folderId + '"的数据项？').then(function () {
    return delSpaceFolderInfo(row.folderId);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

getList();
</script>
