<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="行为编号" prop="behaviorId">
        <el-input
            v-model="queryParams.behaviorId"
            placeholder="请输入行为编号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="行为类型" prop="behaviorType">
        <el-select v-model="queryParams.behaviorType" style="width: 200px" placeholder="请选择行为类型" clearable>
          <el-option
              v-for="dict in p_user_behavior_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="用户编号" prop="userId">
        <el-input
            v-model="queryParams.userId"
            placeholder="请输入用户编号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="目标类型" prop="targetType">
        <el-select v-model="queryParams.targetType" style="width: 200px" placeholder="请选择目标类型" clearable>
          <el-option
              v-for="dict in p_user_behavior_target_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="目标对象" prop="targetId">
        <el-input
            v-model="queryParams.targetId"
            placeholder="请输入目标对象"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <!--      <el-form-item label="分数" prop="score">-->
      <!--        <el-input-->
      <!--            v-model="queryParams.score"-->
      <!--            placeholder="请输入分数"-->
      <!--            clearable-->
      <!--            @keyup.enter="handleQuery"-->
      <!--        />-->
      <!--      </el-form-item>-->
      <el-form-item label="图片分类" prop="categoryId">
        <el-input
            v-model="queryParams.categoryId"
            placeholder="请输入图片分类"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="空间" prop="spaceId">
        <el-input
            v-model="queryParams.spaceId"
            placeholder="请输入空间"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <!--      <el-form-item label="图片标签" prop="tags">-->
      <!--        <el-input-->
      <!--            v-model="queryParams.tags"-->
      <!--            placeholder="请输入图片标签"-->
      <!--            clearable-->
      <!--            @keyup.enter="handleQuery"-->
      <!--        />-->
      <!--      </el-form-item>-->
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
      <el-form-item label="是否统计" prop="hasStatistics">
        <el-select v-model="queryParams.hasStatistics" style="width: 200px" placeholder="请选择是否统计" clearable>
          <el-option
              v-for="dict in common_has_statistics"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="唯一标识" prop="deviceId">
        <el-input
            v-model="queryParams.deviceId"
            placeholder="请输入设备唯一标识"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="浏览器" prop="browser">
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
      <el-form-item label="IP地址" prop="ipAddr">
        <el-input
            v-model="queryParams.ipAddr"
            placeholder="请输入IP地址"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="IP属地" prop="ipAddress">
        <el-input
            v-model="queryParams.ipAddress"
            placeholder="请输入IP属地"
            clearable
            @keyup.enter="handleQuery"
        />
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
            v-hasPermi="['picture:userBehaviorInfo:add']"
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
            v-hasPermi="['picture:userBehaviorInfo:edit']"
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
            v-hasPermi="['picture:userBehaviorInfo:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['picture:userBehaviorInfo:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="userBehaviorInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="行为编号" align="center" prop="behaviorId" v-if="columns[0].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="行为类型" align="center" prop="behaviorType" v-if="columns[1].visible">
        <template #default="scope">
          <dict-tag :options="p_user_behavior_type" :value="scope.row.behaviorType"/>
        </template>
      </el-table-column>
      <el-table-column label="用户编号" align="center" prop="userId" v-if="columns[2].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="目标类型" align="center" prop="targetType" v-if="columns[3].visible">
        <template #default="scope">
          <dict-tag :options="p_user_behavior_target_type" :value="scope.row.targetType"/>
        </template>
      </el-table-column>
      <el-table-column label="目标对象" align="center" prop="targetId" v-if="columns[4].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="目标内容" align="center" prop="targetContent" v-if="columns[5].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="分数" align="center" prop="score" v-if="columns[6].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="分享链接" align="center" prop="shareLink" v-if="columns[7].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="图片分类" align="center" prop="categoryId" v-if="columns[8].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="空间" align="center" prop="spaceId" v-if="columns[9].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="图片标签" align="center" prop="tags" v-if="columns[10].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="封面" align="center" prop="targetCover" width="100" v-if="columns[11].visible">
        <template #default="scope">
          <image-preview :src="scope.row.targetCover" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" v-if="columns[12].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="是否统计" align="center" prop="hasStatistics" v-if="columns[13].visible">
        <template #default="scope">
          <dict-tag :options="common_has_statistics" :value="scope.row.hasStatistics"/>
        </template>
      </el-table-column>
      <el-table-column label="设备唯一标识" align="center" prop="deviceId" v-if="columns[14].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="浏览器类型" align="center" prop="browser" v-if="columns[15].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="操作系统" align="center" prop="os" v-if="columns[16].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="平台" align="center" prop="platform" v-if="columns[17].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="IP地址" align="center" prop="ipAddr" v-if="columns[18].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="IP属地" align="center" prop="ipAddress" v-if="columns[19].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['picture:userBehaviorInfo:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['picture:userBehaviorInfo:remove']">删除
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

    <!-- 添加或修改用户行为对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="userBehaviorInfoRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="行为类型" prop="behaviorType">
          <el-select v-model="form.behaviorType" placeholder="请选择行为类型">
            <el-option
                v-for="dict in p_user_behavior_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="用户编号" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户编号"/>
        </el-form-item>
        <el-form-item label="目标类型" prop="targetType">
          <el-select v-model="form.targetType" placeholder="请选择目标类型">
            <el-option
                v-for="dict in p_user_behavior_target_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="目标对象" prop="targetId">
          <el-input v-model="form.targetId" placeholder="请输入目标对象"/>
        </el-form-item>
        <el-form-item label="目标内容">
          <editor v-model="form.targetContent" :min-height="192"/>
        </el-form-item>
        <el-form-item label="分数" prop="score">
          <el-input v-model="form.score" placeholder="请输入分数"/>
        </el-form-item>
        <el-form-item label="分享链接" prop="shareLink">
          <el-input v-model="form.shareLink" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="图片分类" prop="categoryId">
          <el-input v-model="form.categoryId" placeholder="请输入图片分类"/>
        </el-form-item>
        <el-form-item label="空间" prop="spaceId">
          <el-input v-model="form.spaceId" placeholder="请输入空间"/>
        </el-form-item>
        <el-form-item label="图片标签" prop="tags">
          <el-input v-model="form.tags" placeholder="请输入图片标签"/>
        </el-form-item>
        <el-form-item label="封面" prop="targetCover">
          <image-upload v-model="form.targetCover"/>
        </el-form-item>
        <el-form-item label="是否统计" prop="hasStatistics">
          <el-radio-group v-model="form.hasStatistics">
            <el-radio
                v-for="dict in common_has_statistics"
                :key="dict.value"
                :value="dict.value"
            >{{dict.label}}</el-radio>
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

<script setup name="UserBehaviorInfo">
import {
  listUserBehaviorInfo,
  getUserBehaviorInfo,
  delUserBehaviorInfo,
  addUserBehaviorInfo,
  updateUserBehaviorInfo
} from "@/api/picture/userBehaviorInfo";

const {proxy} = getCurrentInstance();
const {
  p_user_behavior_type,
  p_user_behavior_target_type,
  common_has_statistics
} = proxy.useDict('p_user_behavior_type', 'p_user_behavior_target_type','common_has_statistics');

const userBehaviorInfoList = ref([]);
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
    behaviorId: null,
    behaviorType: null,
    userId: null,
    targetType: null,
    targetId: null,
    targetContent: null,
    score: null,
    categoryId: null,
    spaceId: null,
    tags: null,
    createTime: null,
    hasStatistics: null,
    deviceId: null,
    browser: null,
    os: null,
    platform: null,
    ipAddr: null,
    ipAddress: null
  },
  rules: {
    behaviorType: [
      {required: true, message: "行为类型不能为空", trigger: "change"}
    ],
    userId: [
      {required: true, message: "用户编号不能为空", trigger: "blur"}
    ],
    targetType: [
      {required: true, message: "目标类型不能为空", trigger: "change"}
    ],
    targetId: [
      {required: true, message: "目标对象不能为空", trigger: "blur"}
    ],
    score: [
      {required: true, message: "分数不能为空", trigger: "blur"}
    ],
    createTime: [
      {required: true, message: "创建时间不能为空", trigger: "blur"}
    ],
  },
  //表格展示列
  columns: [
    {key: 0, label: '行为编号', visible: false},
    {key: 1, label: '行为类型', visible: true},
    {key: 2, label: '用户编号', visible: true},
    {key: 3, label: '目标类型', visible: true},
    {key: 4, label: '目标对象', visible: true},
    {key: 5, label: '目标内容', visible: true},
    {key: 6, label: '分数', visible: true},
    {key: 7, label: '分享链接', visible: false},
    {key: 8, label: '图片分类', visible: false},
    {key: 9, label: '空间', visible: false},
    {key: 10, label: '图片标签', visible: false},
    {key: 11, label: '封面', visible: true},
    {key: 12, label: '创建时间', visible: true},
    {key: 13, label: '是否统计', visible: false},
    {key: 14, label: '设备唯一标识', visible: false},
    {key: 15, label: '浏览器类型', visible: false},
    {key: 16, label: '操作系统', visible: false},
    {key: 17, label: '平台', visible: false},
    {key: 18, label: 'IP地址', visible: false},
    {key: 19, label: 'IP属地', visible: false},
  ],
});

const {queryParams, form, rules, columns} = toRefs(data);

/** 查询用户行为列表 */
function getList() {
  loading.value = true;
  queryParams.value.params = {};
  if (null != daterangeCreateTime && '' != daterangeCreateTime) {
    queryParams.value.params["beginCreateTime"] = daterangeCreateTime.value[0];
    queryParams.value.params["endCreateTime"] = daterangeCreateTime.value[1];
  }
  listUserBehaviorInfo(queryParams.value).then(response => {
    userBehaviorInfoList.value = response.rows;
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
    behaviorId: null,
    behaviorType: null,
    userId: null,
    targetType: null,
    targetId: null,
    targetContent: null,
    score: null,
    shareLink: null,
    categoryId: null,
    spaceId: null,
    tags: null,
    targetCover: null,
    createTime: null,
    hasStatistics: null,
    deviceId: null,
    browser: null,
    os: null,
    platform: null,
    ipAddress: null,
    ipAddr: null
  };
  proxy.resetForm("userBehaviorInfoRef");
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
  ids.value = selection.map(item => item.behaviorId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加用户行为";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _behaviorId = row.behaviorId || ids.value
  getUserBehaviorInfo(_behaviorId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改用户行为";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["userBehaviorInfoRef"].validate(valid => {
    if (valid) {
      if (form.value.behaviorId != null) {
        updateUserBehaviorInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addUserBehaviorInfo(form.value).then(response => {
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
  const _behaviorIds = row.behaviorId || ids.value;
  proxy.$modal.confirm('是否确认删除用户行为编号为"' + _behaviorIds + '"的数据项？').then(function () {
    return delUserBehaviorInfo(_behaviorIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('picture/userBehaviorInfo/export', {
    ...queryParams.value
  }, `userBehaviorInfo_${new Date().getTime()}.xlsx`)
}

getList();
</script>
