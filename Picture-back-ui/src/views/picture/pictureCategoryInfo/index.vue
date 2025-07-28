<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="分类编号" prop="categoryId">
        <el-input
            v-model="queryParams.categoryId"
            placeholder="请输入分类编号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="父级分类" prop="parentId">
        <el-input
            v-model="queryParams.parentId"
            placeholder="请输入父级分类"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="分类名称" prop="name">
        <el-input
            v-model="queryParams.name"
            placeholder="请输入分类名称"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="分类状态" prop="categoryStatus">
        <el-select v-model="queryParams.categoryStatus" style="width: 200px" placeholder="请选择分类状态" clearable>
          <el-option
              v-for="dict in p_category_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="分类类型" prop="categoryType">
        <el-select v-model="queryParams.categoryType" style="width: 200px" placeholder="请选择分类类型" clearable>
          <el-option
              v-for="dict in p_category_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="查询状态" prop="queryStatus">
        <el-select v-model="queryParams.queryStatus" style="width: 200px" placeholder="请选择查询状态" clearable>
          <el-option
              v-for="dict in p_category_query_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
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
      <el-form-item label="删除标记" prop="isDelete">
        <el-select v-model="queryParams.isDelete" style="width: 200px" placeholder="请选择删除标记" clearable>
          <el-option
              v-for="dict in common_delete"
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
            v-hasPermi="['picture:pictureCategoryInfo:add']"
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
        :data="pictureCategoryInfoList"
        row-key="categoryId"
        :default-expand-all="isExpandAll"
        @sort-change="customSort"
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column label="父级分类" prop="parentId" v-if="columns[1].visible" :show-overflow-tooltip="true"/>
      <el-table-column label="祖级列表" align="center" prop="ancestors" v-if="columns[2].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="分类名称" align="center" prop="name" v-if="columns[3].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="封面图" align="center" prop="coverUrl" width="100" v-if="columns[4].visible">
        <template #default="scope">
          <image-preview :src="scope.row.coverUrl" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="封面图标" align="center" prop="categoryIcon" v-if="columns[5].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="显示顺序" align="center" prop="orderNum" sortable="custom" v-if="columns[6].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="分类描述" align="center" prop="categoryDesc" v-if="columns[8].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="分类状态" align="center" prop="categoryStatus" v-if="columns[9].visible">
        <template #default="scope">
          <dict-tag :options="p_category_status" :value="scope.row.categoryStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="分类类型" align="center" prop="categoryType" v-if="columns[9].visible">
        <template #default="scope">
          <dict-tag :options="p_category_type" :value="scope.row.categoryType"/>
        </template>
      </el-table-column>
      <el-table-column label="查询状态" align="center" prop="queryStatus" v-if="columns[10].visible">
        <template #default="scope">
          <dict-tag :options="p_category_query_status" :value="scope.row.queryStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="使用次数" align="center" prop="usageCount" sortable="custom" column-key="usage_count"
                       v-if="columns[11].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="查看次数" align="center" prop="lookCount" sortable="custom" column-key="look_count"
                       v-if="columns[12].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="下载次数" align="center" prop="downloadCount" sortable="custom"
                       column-key="download_count" v-if="columns[13].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="创建时间" align="center" prop="createTime" sortable="custom" column-key="create_time"
                       width="180" v-if="columns[14].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime" sortable="custom" column-key="update_time"
                       width="180" v-if="columns[15].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="删除标记" align="center" prop="isDelete" v-if="columns[16].visible">
        <template #default="scope">
          <dict-tag :options="common_delete" :value="scope.row.isDelete"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['picture:pictureCategoryInfo:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Plus" @click="handleAdd(scope.row)"
                     v-hasPermi="['picture:pictureCategoryInfo:add']">新增
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['picture:pictureCategoryInfo:remove']">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改图片分类信息对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="pictureCategoryInfoRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="父级分类" prop="parentId">
          <el-tree-select
              v-model="form.parentId"
              :data="pictureCategoryInfoOptions"
              :props="{ value: 'categoryId', label: 'name', children: 'children' }"
              value-key="categoryId"
              placeholder="请选择父级分类"
              check-strictly
          />
        </el-form-item>
        <el-form-item label="封面图" prop="coverUrl">
          <image-upload v-model="form.coverUrl"/>
        </el-form-item>
        <el-form-item label="封面图标" prop="categoryIcon">
          <el-input v-model="form.categoryIcon" placeholder="请输入封面图标"/>
        </el-form-item>
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入分类名称"/>
        </el-form-item>
        <el-form-item label="显示顺序" prop="orderNum">
          <el-input-number :min="0" :max="10" v-model="form.orderNum" controls-position="right"
                           placeholder="请输入显示顺序"/>
        </el-form-item>
        <el-form-item label="分类描述" prop="categoryDesc">
          <el-input v-model="form.categoryDesc" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="分类状态" prop="categoryStatus">
          <el-radio-group v-model="form.categoryStatus">
            <el-radio
                v-for="dict in p_category_status"
                :key="dict.value"
                :value="dict.value"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="分类类型" prop="categoryType">
          <el-select v-model="form.categoryType" placeholder="请选择分类类型">
            <el-option
                v-for="dict in p_category_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="查询状态" prop="queryStatus">
          <el-radio-group v-model="form.queryStatus">
            <el-radio
                v-for="dict in p_category_query_status"
                :key="dict.value"
                :value="dict.value"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <!--        <el-form-item label="使用次数" prop="usageCount">-->
        <!--          <el-input v-model="form.usageCount" placeholder="请输入使用次数"/>-->
        <!--        </el-form-item>-->
        <!--        <el-form-item label="查看次数" prop="lookCount">-->
        <!--          <el-input v-model="form.lookCount" placeholder="请输入查看次数"/>-->
        <!--        </el-form-item>-->
        <!--        <el-form-item label="下载次数" prop="downloadCount">-->
        <!--          <el-input v-model="form.downloadCount" placeholder="请输入下载次数"/>-->
        <!--        </el-form-item>-->
        <!--        <el-form-item label="删除标记" prop="isDelete">-->
        <!--          <el-radio-group v-model="form.isDelete">-->
        <!--            <el-radio-->
        <!--                v-for="dict in common_delete"-->
        <!--                :key="dict.value"-->
        <!--                :value="dict.value"-->
        <!--            >{{ dict.label }}-->
        <!--            </el-radio>-->
        <!--          </el-radio-group>-->
        <!--        </el-form-item>-->
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

<script setup name="PictureCategoryInfo">
import {
  listPictureCategoryInfo,
  getPictureCategoryInfo,
  delPictureCategoryInfo,
  addPictureCategoryInfo,
  updatePictureCategoryInfo
} from "@/api/picture/pictureCategoryInfo";

const {proxy} = getCurrentInstance();
const {
  common_delete,
  p_category_status,
  p_category_query_status,
  p_category_type
} = proxy.useDict('common_delete', 'p_category_status', 'p_category_query_status', 'p_category_type');

const pictureCategoryInfoList = ref([]);
const pictureCategoryInfoOptions = ref([]);
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
    categoryId: null,
    parentId: null,
    name: null,
    categoryStatus: null,
    categoryType: null,
    queryStatus: null,
    createTime: null,
    updateTime: null,
    isDelete: null
  },
  rules: {
    parentId: [
      {required: true, message: "父级分类不能为空", trigger: "blur"}
    ],
    ancestors: [
      {required: true, message: "祖级列表不能为空", trigger: "blur"}
    ],
    name: [
      {required: true, message: "分类名称不能为空", trigger: "blur"}
    ],
    categoryStatus: [
      {required: true, message: "分类状态不能为空", trigger: "change"}
    ],
    categoryType: [
      {required: true, message: "分类类型不能为空", trigger: "change"}
    ],
    queryStatus: [
      {required: true, message: "查询状态不能为空", trigger: "change"}
    ],
    usageCount: [
      {required: true, message: "使用次数不能为空", trigger: "blur"}
    ],
    lookCount: [
      {required: true, message: "查看次数不能为空", trigger: "blur"}
    ],
    downloadCount: [
      {required: true, message: "下载次数不能为空", trigger: "blur"}
    ],
    createTime: [
      {required: true, message: "创建时间不能为空", trigger: "blur"}
    ],
    updateTime: [
      {required: true, message: "更新时间不能为空", trigger: "blur"}
    ],
    isDelete: [
      {required: true, message: "删除标记不能为空", trigger: "change"}
    ]
  },
  //表格展示列
  columns: [
    {key: 0, label: '分类编号', visible: false},
    {key: 1, label: '父级分类', visible: false},
    {key: 2, label: '祖级列表', visible: false},
    {key: 3, label: '封面图', visible: true},
    {key: 4, label: '封面图标', visible: true},
    {key: 5, label: '分类名称', visible: true},
    {key: 6, label: '图标', visible: true},
    {key: 7, label: '分类描述', visible: false},
    {key: 8, label: '分类状态', visible: true},
    {key: 9, label: '分类类型', visible: true},
    {key: 10, label: '查询状态', visible: true},
    {key: 11, label: '使用次数', visible: true},
    {key: 12, label: '查看次数', visible: true},
    {key: 13, label: '下载次数', visible: true},
    {key: 14, label: '创建时间', visible: false},
    {key: 15, label: '更新时间', visible: false},
    {key: 16, label: '删除标记', visible: false},
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

/** 查询图片分类信息列表 */
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
  listPictureCategoryInfo(queryParams.value).then(response => {
    pictureCategoryInfoList.value = proxy.handleTree(response.data, "categoryId", "parentId");
    loading.value = false;
  });
}

/** 查询图片分类信息下拉树结构 */
function getTreeselect() {
  listPictureCategoryInfo().then(response => {
    pictureCategoryInfoOptions.value = [];
    const data = {categoryId: 0, name: '顶级节点', children: []};
    data.children = proxy.handleTree(response.data, "categoryId", "parentId");
    pictureCategoryInfoOptions.value.push(data);
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
    categoryId: null,
    parentId: null,
    ancestors: null,
    coverUrl: null,
    categoryIcon: null,
    name: null,
    orderNum: null,
    categoryDesc: null,
    categoryStatus: null,
    categoryType: null,
    queryStatus: null,
    usageCount: null,
    lookCount: null,
    downloadCount: null,
    createTime: null,
    updateTime: null,
    isDelete: null
  };
  proxy.resetForm("pictureCategoryInfoRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  daterangeCreateTime.value = [];
  daterangeUpdateTime.value = [];
  proxy.resetForm("queryRef");
  proxy.$refs.tableRef.clearSort();
  orderByColumn.value = null
  isAsc.value = null;
  handleQuery();
}

/** 新增按钮操作 */
function handleAdd(row) {
  reset();
  getTreeselect();
  if (row != null && row.categoryId) {
    form.value.parentId = row.categoryId;
  } else {
    form.value.parentId = 0;
  }
  open.value = true;
  title.value = "添加图片分类信息";
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
    form.value.parentId = row.categoryId;
  }
  getPictureCategoryInfo(row.categoryId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改图片分类信息";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["pictureCategoryInfoRef"].validate(valid => {
    if (valid) {
      if (form.value.categoryId != null) {
        updatePictureCategoryInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addPictureCategoryInfo(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除图片分类信息编号为"' + row.categoryId + '"的数据项？').then(function () {
    return delPictureCategoryInfo(row.categoryId);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

getList();
</script>
