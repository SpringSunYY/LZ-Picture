<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
                  <el-form-item label="日志编号" prop="actionId">
                    <el-input
                        v-model="queryParams.actionId"
                        placeholder="请输入日志编号"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                  </el-form-item>
                  <el-form-item label="搜索记录编号" prop="searchId">
                    <el-input
                        v-model="queryParams.searchId"
                        placeholder="请输入搜索记录编号"
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
                  <el-form-item label="行为类型" prop="actionType">
                    <el-select v-model="queryParams.actionType" style="width: 200px" placeholder="请选择行为类型" clearable>
                      <el-option
                          v-for="dict in p_action_log_type"
                          :key="dict.value"
                          :label="dict.label"
                          :value="dict.value"
                      />
                    </el-select>
                  </el-form-item>
                  <el-form-item label="目标类型" prop="targetType">
                    <el-select v-model="queryParams.targetType" style="width: 200px" placeholder="请选择目标类型" clearable>
                      <el-option
                          v-for="dict in p_action_log_target_type"
                          :key="dict.value"
                          :label="dict.label"
                          :value="dict.value"
                      />
                    </el-select>
                  </el-form-item>
                  <el-form-item label="目标对象编号" prop="targetId">
                    <el-input
                        v-model="queryParams.targetId"
                        placeholder="请输入目标对象编号"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                  </el-form-item>
                  <el-form-item label="行为时间" style="width: 308px">
                    <el-date-picker
                        v-model="daterangeCreateTime"
                        value-format="YYYY-MM-DD"
                        type="daterange"
                        range-separator="-"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                    ></el-date-picker>
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
            v-hasPermi="['picture:userActionLogInfo:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="success"
            plain
            icon="Edit"
            :disabled="single"
            @click="handleUpdate"
            v-hasPermi="['picture:userActionLogInfo:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="danger"
            plain
            icon="Delete"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['picture:userActionLogInfo:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['picture:userActionLogInfo:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="userActionLogInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
              <el-table-column label="日志编号" align="center" prop="actionId" v-if="columns[0].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="搜索记录编号" align="center" prop="searchId" v-if="columns[1].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="用户编号" align="center" prop="userId" v-if="columns[2].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="行为类型" align="center" prop="actionType" v-if="columns[3].visible">
                <template #default="scope">
                      <dict-tag :options="p_action_log_type" :value="scope.row.actionType"/>
                </template>
              </el-table-column>
                <el-table-column label="目标类型" align="center" prop="targetType" v-if="columns[4].visible">
                <template #default="scope">
                      <dict-tag :options="p_action_log_target_type" :value="scope.row.targetType"/>
                </template>
              </el-table-column>
                <el-table-column label="目标对象编号" align="center" prop="targetId" v-if="columns[5].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="行为时间" align="center" prop="createTime" width="180" v-if="columns[6].visible" :show-overflow-tooltip="true">
                <template #default="scope">
                  <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
                </template>
              </el-table-column>
                <el-table-column label="设备唯一标识" align="center" prop="deviceId" v-if="columns[7].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="浏览器类型" align="center" prop="browser" v-if="columns[8].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="操作系统" align="center" prop="os" v-if="columns[9].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="平台" align="center" prop="platform" v-if="columns[10].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="IP属地" align="center" prop="ipAddress" v-if="columns[11].visible" :show-overflow-tooltip="true"/>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['picture:userActionLogInfo:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['picture:userActionLogInfo:remove']">删除</el-button>
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

    <!-- 添加或修改用户行为日志对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="userActionLogInfoRef" :model="form" :rules="rules" label-width="80px">
                        <el-form-item label="搜索记录编号" prop="searchId">
                          <el-input v-model="form.searchId" placeholder="请输入搜索记录编号" />
                        </el-form-item>
                        <el-form-item label="用户编号" prop="userId">
                          <el-input v-model="form.userId" placeholder="请输入用户编号" />
                        </el-form-item>
                        <el-form-item label="行为类型" prop="actionType">
                          <el-select v-model="form.actionType" placeholder="请选择行为类型">
                            <el-option
                                v-for="dict in p_action_log_type"
                                :key="dict.value"
                                :label="dict.label"
                                :value="dict.value"
                            ></el-option>
                          </el-select>
                        </el-form-item>
                        <el-form-item label="目标类型" prop="targetType">
                          <el-select v-model="form.targetType" placeholder="请选择目标类型">
                            <el-option
                                v-for="dict in p_action_log_target_type"
                                :key="dict.value"
                                :label="dict.label"
                                :value="dict.value"
                            ></el-option>
                          </el-select>
                        </el-form-item>
                        <el-form-item label="目标对象编号" prop="targetId">
                          <el-input v-model="form.targetId" placeholder="请输入目标对象编号" />
                        </el-form-item>
                        <el-form-item label="设备唯一标识" prop="deviceId">
                          <el-input v-model="form.deviceId" placeholder="请输入设备唯一标识" />
                        </el-form-item>
                        <el-form-item label="浏览器类型" prop="browser">
                          <el-input v-model="form.browser" placeholder="请输入浏览器类型" />
                        </el-form-item>
                        <el-form-item label="操作系统" prop="os">
                          <el-input v-model="form.os" placeholder="请输入操作系统" />
                        </el-form-item>
                        <el-form-item label="平台" prop="platform">
                          <el-input v-model="form.platform" placeholder="请输入平台" />
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

<script setup name="UserActionLogInfo">
  import { listUserActionLogInfo, getUserActionLogInfo, delUserActionLogInfo, addUserActionLogInfo, updateUserActionLogInfo } from "@/api/picture/userActionLogInfo";

  const { proxy } = getCurrentInstance();
      const { p_action_log_target_type, p_action_log_type } = proxy.useDict('p_action_log_target_type', 'p_action_log_type');

  const userActionLogInfoList = ref([]);
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
                    actionId: null,
                    searchId: null,
                    userId: null,
                    actionType: null,
                    targetType: null,
                    targetId: null,
                    createTime: null,
                    deviceId: null,
                    browser: null,
                    os: null,
                    platform: null,
                    ipAddress: null
    },
    rules: {
                    actionType: [
                { required: true, message: "行为类型不能为空", trigger: "change" }
              ],
                    targetType: [
                { required: true, message: "目标类型不能为空", trigger: "change" }
              ],
                    targetId: [
                { required: true, message: "目标对象编号不能为空", trigger: "blur" }
              ],
                    createTime: [
                { required: true, message: "行为时间不能为空", trigger: "blur" }
              ],
    },
    //表格展示列
    columns: [
              { key: 0, label: '日志编号', visible: true },
                { key: 1, label: '搜索记录编号', visible: true },
                { key: 2, label: '用户编号', visible: true },
                { key: 3, label: '行为类型', visible: true },
                { key: 4, label: '目标类型', visible: true },
                { key: 5, label: '目标对象编号', visible: true },
                { key: 6, label: '行为时间', visible: true },
                { key: 7, label: '设备唯一标识', visible: true },
                { key: 8, label: '浏览器类型', visible: true },
                { key: 9, label: '操作系统', visible: true },
                { key: 10, label: '平台', visible: true },
                { key: 11, label: 'IP属地', visible: true },
      ],
  });

  const { queryParams, form, rules,columns } = toRefs(data);

  /** 查询用户行为日志列表 */
  function getList() {
    loading.value = true;
            queryParams.value.params = {};
            if (null != daterangeCreateTime && '' != daterangeCreateTime) {
              queryParams.value.params["beginCreateTime"] = daterangeCreateTime.value[0];
              queryParams.value.params["endCreateTime"] = daterangeCreateTime.value[1];
            }
    listUserActionLogInfo(queryParams.value).then(response => {
            userActionLogInfoList.value = response.rows;
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
                    actionId: null,
                    searchId: null,
                    userId: null,
                    actionType: null,
                    targetType: null,
                    targetId: null,
                    createTime: null,
                    deviceId: null,
                    browser: null,
                    os: null,
                    platform: null,
                    ipAddress: null
    };
    proxy.resetForm("userActionLogInfoRef");
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
    ids.value = selection.map(item => item.actionId);
    single.value = selection.length != 1;
    multiple.value = !selection.length;
  }

  /** 新增按钮操作 */
  function handleAdd() {
    reset();
    open.value = true;
    title.value = "添加用户行为日志";
  }

  /** 修改按钮操作 */
  function handleUpdate(row) {
    reset();
    const _actionId = row.actionId || ids.value
    getUserActionLogInfo(_actionId).then(response => {
      form.value = response.data;
      open.value = true;
      title.value = "修改用户行为日志";
    });
  }

  /** 提交按钮 */
  function submitForm() {
    proxy.$refs["userActionLogInfoRef"].validate(valid => {
      if (valid) {
        if (form.value.actionId != null) {
          updateUserActionLogInfo(form.value).then(response => {
            proxy.$modal.msgSuccess("修改成功");
            open.value = false;
            getList();
          });
        } else {
          addUserActionLogInfo(form.value).then(response => {
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
    const _actionIds = row.actionId || ids.value;
    proxy.$modal.confirm('是否确认删除用户行为日志编号为"' + _actionIds + '"的数据项？').then(function() {
      return delUserActionLogInfo(_actionIds);
    }).then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    }).catch(() => {});
  }

  /** 导出按钮操作 */
  function handleExport() {
    proxy.download('picture/userActionLogInfo/export', {
      ...queryParams.value
    }, `userActionLogInfo_${new Date().getTime()}.xlsx`)
  }

  getList();
</script>
