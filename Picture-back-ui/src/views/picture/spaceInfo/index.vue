<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="空间编号" prop="spaceId">
        <el-input
            v-model="queryParams.spaceId"
            placeholder="请输入空间编号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="空间名称" prop="spaceName">
        <el-input
            v-model="queryParams.spaceName"
            placeholder="请输入空间名称"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="存储类型" prop="ossType">
        <el-select v-model="queryParams.ossType" style="width: 200px" placeholder="请选择存储类型" clearable>
          <el-option
              v-for="dict in p_space_oss_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="所属用户" prop="userId">
        <el-input
            v-model="queryParams.userId"
            placeholder="请输入所属用户"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="空间状态" prop="spaceStatus">
        <el-select v-model="queryParams.spaceStatus" style="width: 200px" placeholder="请选择空间状态" clearable>
          <el-option
              v-for="dict in p_space_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="空间类型" prop="spaceType">
        <el-select v-model="queryParams.spaceType" style="width: 200px" placeholder="请选择空间类型" clearable>
          <el-option
              v-for="dict in p_space_type"
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
<!--      <el-form-item label="最后上传时间" style="width: 308px">-->
<!--        <el-date-picker-->
<!--            v-model="daterangeLastUpdateTime"-->
<!--            value-format="YYYY-MM-DD"-->
<!--            type="daterange"-->
<!--            range-separator="-"-->
<!--            start-placeholder="开始日期"-->
<!--            end-placeholder="结束日期"-->
<!--        ></el-date-picker>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="最后更新时间" style="width: 308px">-->
<!--        <el-date-picker-->
<!--            v-model="daterangeUpdateTime"-->
<!--            value-format="YYYY-MM-DD"-->
<!--            type="daterange"-->
<!--            range-separator="-"-->
<!--            start-placeholder="开始日期"-->
<!--            end-placeholder="结束日期"-->
<!--        ></el-date-picker>-->
<!--      </el-form-item>-->
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
            v-hasPermi="['picture:spaceInfo:add']"
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
            v-hasPermi="['picture:spaceInfo:edit']"
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
            v-hasPermi="['picture:spaceInfo:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['picture:spaceInfo:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="spaceInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="空间编号" align="center" prop="spaceId" v-if="columns[0].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="空间名称" align="center" prop="spaceName" v-if="columns[1].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="空间封面" align="center" prop="spaceAvatar" width="100" v-if="columns[2].visible">
        <template #default="scope">
          <image-preview :src="scope.row.spaceAvatar" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="存储类型" align="center" prop="ossType" v-if="columns[3].visible">
        <template #default="scope">
          <dict-tag :options="p_space_oss_type" :value="scope.row.ossType"/>
        </template>
      </el-table-column>
      <el-table-column label="存储配置" align="center" prop="ossConfig" v-if="columns[4].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="最大容量" align="center" prop="maxSize" v-if="columns[5].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="最大文件数" align="center" prop="maxCount" v-if="columns[6].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="已用容量" align="center" prop="totalSize" v-if="columns[7].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="文件总数" align="center" prop="totalCount" v-if="columns[8].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="所属用户" align="center" prop="userId" v-if="columns[9].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="空间描述" align="center" prop="spaceDesc" v-if="columns[10].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="空间状态" align="center" prop="spaceStatus" v-if="columns[11].visible">
        <template #default="scope">
          <dict-tag :options="p_space_status" :value="scope.row.spaceStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="空间类型" align="center" prop="spaceType" v-if="columns[12].visible">
        <template #default="scope">
          <dict-tag :options="p_space_type" :value="scope.row.spaceType"/>
        </template>
      </el-table-column>
      <el-table-column label="成员上限" align="center" prop="memberLimit" v-if="columns[13].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="当前成员数" align="center" prop="currentMembers" v-if="columns[14].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" v-if="columns[15].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="最后上传时间" align="center" prop="lastUpdateTime" width="180" v-if="columns[16].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.lastUpdateTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="最后更新时间" align="center" prop="updateTime" width="180" v-if="columns[17].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="删除" align="center" prop="isDelete" v-if="columns[18].visible">
        <template #default="scope">
          <dict-tag :options="common_delete" :value="scope.row.isDelete"/>
        </template>
      </el-table-column>
      <el-table-column label="删除时间" align="center" prop="deletedTime" width="180" v-if="columns[19].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.deletedTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['picture:spaceInfo:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['picture:spaceInfo:remove']">删除
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

    <!-- 添加或修改空间信息对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form ref="spaceInfoRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="空间名称" prop="spaceName">
          <el-input v-model="form.spaceName" placeholder="请输入空间名称"/>
        </el-form-item>
        <!--        <el-form-item label="空间封面" prop="spaceAvatar">-->
        <!--          <image-upload v-model="form.spaceAvatar"/>-->
        <!--        </el-form-item>-->
        <el-form-item label="存储类型" prop="ossType">
          <el-select v-model="form.ossType" placeholder="请选择存储类型">
            <el-option
                v-for="dict in p_space_oss_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="存储配置" prop="ossConfig">
          <el-input v-model="form.ossConfig" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <!--        <el-form-item label="最大容量" prop="maxSize">-->
        <!--          <el-input v-model="form.maxSize" placeholder="请输入最大容量"/>-->
        <!--        </el-form-item>-->
        <!--        <el-form-item label="最大文件数" prop="maxCount">-->
        <!--          <el-input v-model="form.maxCount" placeholder="请输入最大文件数"/>-->
        <!--        </el-form-item>-->
        <!--        <el-form-item label="已用容量" prop="totalSize">-->
        <!--          <el-input v-model="form.totalSize" placeholder="请输入已用容量"/>-->
        <!--        </el-form-item>-->
        <!--        <el-form-item label="文件总数" prop="totalCount">-->
        <!--          <el-input v-model="form.totalCount" placeholder="请输入文件总数"/>-->
        <!--        </el-form-item>-->
        <el-form-item label="所属用户" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入所属用户"/>
        </el-form-item>
        <el-form-item label="空间描述" prop="spaceDesc">
          <el-input v-model="form.spaceDesc" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="空间状态" prop="spaceStatus">
          <el-select v-model="form.spaceStatus" placeholder="请选择空间状态">
            <el-option
                v-for="dict in p_space_status"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="空间类型" prop="spaceType">
          <el-select v-model="form.spaceType" placeholder="请选择空间类型">
            <el-option
                v-for="dict in p_space_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="成员上限" prop="memberLimit">
          <el-input v-model="form.memberLimit" placeholder="请输入成员上限"/>
        </el-form-item>
        <!--        <el-form-item label="当前成员数" prop="currentMembers">-->
        <!--          <el-input v-model="form.currentMembers" placeholder="请输入当前成员数"/>-->
        <!--        </el-form-item>-->
        <!--        <el-form-item label="最后上传时间" prop="lastUpdateTime">-->
        <!--          <el-date-picker clearable-->
        <!--                          v-model="form.lastUpdateTime"-->
        <!--                          type="date"-->
        <!--                          value-format="YYYY-MM-DD"-->
        <!--                          placeholder="请选择最后上传时间">-->
        <!--          </el-date-picker>-->
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

<script setup name="SpaceInfo">
import {listSpaceInfo, getSpaceInfo, delSpaceInfo, addSpaceInfo, updateSpaceInfo} from "@/api/picture/spaceInfo";

const {proxy} = getCurrentInstance();
const {
  common_delete,
  p_space_status,
  p_space_type,
  p_space_oss_type
} = proxy.useDict('common_delete', 'p_space_status', 'p_space_type', 'p_space_oss_type');

const spaceInfoList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const daterangeCreateTime = ref([]);
const daterangeLastUpdateTime = ref([]);
const daterangeUpdateTime = ref([]);
const daterangeDeletedTime = ref([]);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    spaceId: null,
    spaceName: null,
    ossType: null,
    userId: null,
    spaceStatus: null,
    spaceType: null,
    createTime: null,
    lastUpdateTime: null,
    updateTime: null,
    isDelete: null,
    deletedTime: null
  },
  rules: {
    spaceName: [
      {required: true, message: "空间名称不能为空", trigger: "blur"}
    ],
    ossType: [
      {required: true, message: "存储类型不能为空", trigger: "change"}
    ],
    userId: [
      {required: true, message: "所属用户不能为空", trigger: "blur"}
    ],
    spaceStatus: [
      {required: true, message: "空间状态不能为空", trigger: "change"}
    ],
    spaceType: [
      {required: true, message: "空间类型不能为空", trigger: "change"}
    ],
    createTime: [
      {required: true, message: "创建时间不能为空", trigger: "blur"}
    ],
    isDelete: [
      {required: true, message: "删除不能为空", trigger: "change"}
    ],
  },
  //表格展示列
  columns: [
    {key: 0, label: '空间编号', visible: false},
    {key: 1, label: '空间名称', visible: true},
    {key: 2, label: '空间封面', visible: true},
    {key: 3, label: '存储类型', visible: true},
    {key: 4, label: '存储配置', visible: false},
    {key: 5, label: '最大容量', visible: false},
    {key: 6, label: '最大文件数', visible: false},
    {key: 7, label: '已用容量', visible: true},
    {key: 8, label: '文件总数', visible: true},
    {key: 9, label: '所属用户', visible: true},
    {key: 10, label: '空间描述', visible: false},
    {key: 11, label: '空间状态', visible: true},
    {key: 12, label: '空间类型', visible: true},
    {key: 13, label: '成员上限', visible: false},
    {key: 14, label: '当前成员数', visible: false},
    {key: 15, label: '创建时间', visible: true},
    {key: 16, label: '最后上传时间', visible: true},
    {key: 17, label: '最后更新时间', visible: false},
    {key: 18, label: '删除', visible: false},
    {key: 19, label: '删除时间', visible: false},
  ],
});

const {queryParams, form, rules, columns} = toRefs(data);

/** 查询空间信息列表 */
function getList() {
  loading.value = true;
  queryParams.value.params = {};
  if (null != daterangeCreateTime && '' != daterangeCreateTime) {
    queryParams.value.params["beginCreateTime"] = daterangeCreateTime.value[0];
    queryParams.value.params["endCreateTime"] = daterangeCreateTime.value[1];
  }
  if (null != daterangeLastUpdateTime && '' != daterangeLastUpdateTime) {
    queryParams.value.params["beginLastUpdateTime"] = daterangeLastUpdateTime.value[0];
    queryParams.value.params["endLastUpdateTime"] = daterangeLastUpdateTime.value[1];
  }
  if (null != daterangeUpdateTime && '' != daterangeUpdateTime) {
    queryParams.value.params["beginUpdateTime"] = daterangeUpdateTime.value[0];
    queryParams.value.params["endUpdateTime"] = daterangeUpdateTime.value[1];
  }
  if (null != daterangeDeletedTime && '' != daterangeDeletedTime) {
    queryParams.value.params["beginDeletedTime"] = daterangeDeletedTime.value[0];
    queryParams.value.params["endDeletedTime"] = daterangeDeletedTime.value[1];
  }
  listSpaceInfo(queryParams.value).then(response => {
    spaceInfoList.value = response.rows;
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
    spaceId: null,
    spaceName: null,
    spaceAvatar: null,
    ossType: null,
    ossConfig: null,
    maxSize: null,
    maxCount: null,
    totalSize: null,
    totalCount: null,
    userId: null,
    spaceDesc: null,
    spaceStatus: null,
    spaceType: null,
    memberLimit: null,
    currentMembers: null,
    createTime: null,
    lastUpdateTime: null,
    updateTime: null,
    isDelete: null,
    deletedTime: null
  };
  proxy.resetForm("spaceInfoRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  daterangeCreateTime.value = [];
  daterangeLastUpdateTime.value = [];
  daterangeUpdateTime.value = [];
  daterangeDeletedTime.value = [];
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.spaceId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加空间信息";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _spaceId = row.spaceId || ids.value
  getSpaceInfo(_spaceId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改空间信息";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["spaceInfoRef"].validate(valid => {
    if (valid) {
      if (form.value.spaceId != null) {
        updateSpaceInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addSpaceInfo(form.value).then(response => {
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
  const _spaceIds = row.spaceId || ids.value;
  proxy.$modal.confirm('是否确认删除空间信息编号为"' + _spaceIds + '"的数据项？').then(function () {
    return delSpaceInfo(_spaceIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('picture/spaceInfo/export', {
    ...queryParams.value
  }, `spaceInfo_${new Date().getTime()}.xlsx`)
}

getList();
</script>
