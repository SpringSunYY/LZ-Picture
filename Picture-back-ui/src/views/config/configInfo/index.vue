<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
                  <el-form-item label="配置名称" prop="configName">
                    <el-input
                        v-model="queryParams.configName"
                        placeholder="请输入配置名称"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                  </el-form-item>
                  <el-form-item label="配置键名" prop="configKey">
                    <el-input
                        v-model="queryParams.configKey"
                        placeholder="请输入配置键名"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                  </el-form-item>
                  <el-form-item label="配置类型" prop="configType">
                    <el-select v-model="queryParams.configType" style="width: 200px" placeholder="请选择配置类型" clearable>
                      <el-option
                          v-for="dict in c_config_type"
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
            v-hasPermi="['config:configInfo:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="success"
            plain
            icon="Edit"
            :disabled="single"
            @click="handleUpdate"
            v-hasPermi="['config:configInfo:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="danger"
            plain
            icon="Delete"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['config:configInfo:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['config:configInfo:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="configInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
              <el-table-column label="编号" align="center" prop="configId" v-if="columns[0].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="配置名称" align="center" prop="configName" v-if="columns[1].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="配置键名" align="center" prop="configKey" v-if="columns[2].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="配置键值" align="center" prop="configValue" v-if="columns[3].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="配置类型" align="center" prop="configType" v-if="columns[4].visible">
                <template #default="scope">
                      <dict-tag :options="c_config_type" :value="scope.row.configType"/>
                </template>
              </el-table-column>
                <el-table-column label="配置排序" align="center" prop="orderNum" v-if="columns[5].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="创建人" align="center" prop="createBy" v-if="columns[6].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="创建时间" align="center" prop="createTime" width="180" v-if="columns[7].visible" :show-overflow-tooltip="true">
                <template #default="scope">
                  <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
                </template>
              </el-table-column>
                <el-table-column label="更新人" align="center" prop="updateBy" v-if="columns[8].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="更新时间" align="center" prop="updateTime" width="180" v-if="columns[9].visible" :show-overflow-tooltip="true">
                <template #default="scope">
                  <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
                </template>
              </el-table-column>
                <el-table-column label="备注" align="center" prop="remark" v-if="columns[10].visible" :show-overflow-tooltip="true"/>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['config:configInfo:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['config:configInfo:remove']">删除</el-button>
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

    <!-- 添加或修改配置信息对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="configInfoRef" :model="form" :rules="rules" label-width="80px">
                        <el-form-item label="配置名称" prop="configName">
                          <el-input v-model="form.configName" placeholder="请输入配置名称" />
                        </el-form-item>
                        <el-form-item label="配置键名" prop="configKey">
                          <el-input v-model="form.configKey" placeholder="请输入配置键名" />
                        </el-form-item>
                        <el-form-item label="配置键值" prop="configValue">
                          <el-input v-model="form.configValue" type="textarea" placeholder="请输入内容" />
                        </el-form-item>
                        <el-form-item label="配置类型" prop="configType">
                          <el-radio-group v-model="form.configType">
                            <el-radio
                                v-for="dict in c_config_type"
                                :key="dict.value"
                                :value="dict.value"
                            >{{dict.label}}</el-radio>
                          </el-radio-group>
                        </el-form-item>
                        <el-form-item label="配置排序" prop="orderNum">
                          <el-input v-model="form.orderNum" placeholder="请输入配置排序" />
                        </el-form-item>
                        <el-form-item label="备注" prop="remark">
                          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
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

<script setup name="ConfigInfo">
  import { listConfigInfo, getConfigInfo, delConfigInfo, addConfigInfo, updateConfigInfo } from "@/api/config/configInfo";

  const { proxy } = getCurrentInstance();
      const { c_config_type } = proxy.useDict('c_config_type');

  const configInfoList = ref([]);
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
    form: {},
    queryParams: {
      pageNum: 1,
      pageSize: 10,
                    configName: null,
                    configKey: null,
                    configValue: null,
                    configType: null,
                    createBy: null,
                    createTime: null,
                    updateBy: null,
                    updateTime: null,
    },
    rules: {
                    configName: [
                { required: true, message: "配置名称不能为空", trigger: "blur" }
              ],
                    configKey: [
                { required: true, message: "配置键名不能为空", trigger: "blur" }
              ],
                    configValue: [
                { required: true, message: "配置键值不能为空", trigger: "blur" }
              ],
                    configType: [
                { required: true, message: "配置类型不能为空", trigger: "change" }
              ],
                    orderNum: [
                { required: true, message: "配置排序不能为空", trigger: "blur" }
              ],
                    createBy: [
                { required: true, message: "创建人不能为空", trigger: "blur" }
              ],
                    createTime: [
                { required: true, message: "创建时间不能为空", trigger: "blur" }
              ],
    },
    //表格展示列
    columns: [
              { key: 0, label: '编号', visible: true },
                { key: 1, label: '配置名称', visible: true },
                { key: 2, label: '配置键名', visible: true },
                { key: 3, label: '配置键值', visible: true },
                { key: 4, label: '配置类型', visible: true },
                { key: 5, label: '配置排序', visible: true },
                { key: 6, label: '创建人', visible: true },
                { key: 7, label: '创建时间', visible: true },
                { key: 8, label: '更新人', visible: true },
                { key: 9, label: '更新时间', visible: true },
                { key: 10, label: '备注', visible: true },
      ],
  });

  const { queryParams, form, rules,columns } = toRefs(data);

  /** 查询配置信息列表 */
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
    listConfigInfo(queryParams.value).then(response => {
            configInfoList.value = response.rows;
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
                    configId: null,
                    configName: null,
                    configKey: null,
                    configValue: null,
                    configType: null,
                    orderNum: null,
                    createBy: null,
                    createTime: null,
                    updateBy: null,
                    updateTime: null,
                    remark: null
    };
    proxy.resetForm("configInfoRef");
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
    ids.value = selection.map(item => item.configId);
    single.value = selection.length != 1;
    multiple.value = !selection.length;
  }

  /** 新增按钮操作 */
  function handleAdd() {
    reset();
    open.value = true;
    title.value = "添加配置信息";
  }

  /** 修改按钮操作 */
  function handleUpdate(row) {
    reset();
    const _configId = row.configId || ids.value
    getConfigInfo(_configId).then(response => {
      form.value = response.data;
      open.value = true;
      title.value = "修改配置信息";
    });
  }

  /** 提交按钮 */
  function submitForm() {
    proxy.$refs["configInfoRef"].validate(valid => {
      if (valid) {
        if (form.value.configId != null) {
          updateConfigInfo(form.value).then(response => {
            proxy.$modal.msgSuccess("修改成功");
            open.value = false;
            getList();
          });
        } else {
          addConfigInfo(form.value).then(response => {
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
    const _configIds = row.configId || ids.value;
    proxy.$modal.confirm('是否确认删除配置信息编号为"' + _configIds + '"的数据项？').then(function() {
      return delConfigInfo(_configIds);
    }).then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    }).catch(() => {});
  }

  /** 导出按钮操作 */
  function handleExport() {
    proxy.download('config/configInfo/export', {
      ...queryParams.value
    }, `configInfo_${new Date().getTime()}.xlsx`)
  }

  getList();
</script>
