<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
                  <el-form-item label="编号" prop="infoId">
                    <el-input
                        v-model="queryParams.infoId"
                        placeholder="请输入编号"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                  </el-form-item>
                  <el-form-item label="名称" prop="name">
                    <el-input
                        v-model="queryParams.name"
                        placeholder="请输入名称"
                        clearable
                        @keyup.enter="handleQuery"
                    />
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
            v-hasPermi="['ai:promptInfo:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="success"
            plain
            icon="Edit"
            :disabled="single"
            @click="handleUpdate"
            v-hasPermi="['ai:promptInfo:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="danger"
            plain
            icon="Delete"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['ai:promptInfo:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['ai:promptInfo:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="promptInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
              <el-table-column label="编号" align="center" prop="infoId" v-if="columns[0].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="名称" align="center" prop="name" v-if="columns[1].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="提示内容" align="center" prop="content" v-if="columns[2].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="排序" align="center" prop="orderNum" v-if="columns[3].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="创建人" align="center" prop="createBy" v-if="columns[4].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="创建时间" align="center" prop="createTime" width="180" v-if="columns[5].visible" :show-overflow-tooltip="true">
                <template #default="scope">
                  <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
                </template>
              </el-table-column>
                <el-table-column label="更新人" align="center" prop="updateBy" v-if="columns[6].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="更新时间" align="center" prop="updateTime" width="180" v-if="columns[7].visible" :show-overflow-tooltip="true">
                <template #default="scope">
                  <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
                </template>
              </el-table-column>
                <el-table-column label="备注" align="center" prop="remark" v-if="columns[8].visible" :show-overflow-tooltip="true"/>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['ai:promptInfo:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['ai:promptInfo:remove']">删除</el-button>
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

    <!-- 添加或修改提示词信息对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="promptInfoRef" :model="form" :rules="rules" label-width="80px">
                        <el-form-item label="名称" prop="name">
                          <el-input v-model="form.name" placeholder="请输入名称" />
                        </el-form-item>
                        <el-form-item label="提示内容">
                          <editor v-model="form.content" :min-height="192"/>
                        </el-form-item>
                        <el-form-item label="排序" prop="orderNum">
                          <el-input v-model="form.orderNum" placeholder="请输入排序" />
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

<script setup name="PromptInfo">
  import { listPromptInfo, getPromptInfo, delPromptInfo, addPromptInfo, updatePromptInfo } from "@/api/ai/promptInfo";

  const { proxy } = getCurrentInstance();

  const promptInfoList = ref([]);
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
                    infoId: null,
                    name: null,
                    createBy: null,
                    createTime: null,
                    updateBy: null,
                    updateTime: null,
    },
    rules: {
                    name: [
                { required: true, message: "名称不能为空", trigger: "blur" }
              ],
                    content: [
                { required: true, message: "提示内容不能为空", trigger: "blur" }
              ],
                    orderNum: [
                { required: true, message: "排序不能为空", trigger: "blur" }
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
                { key: 1, label: '名称', visible: true },
                { key: 2, label: '提示内容', visible: true },
                { key: 3, label: '排序', visible: true },
                { key: 4, label: '创建人', visible: true },
                { key: 5, label: '创建时间', visible: true },
                { key: 6, label: '更新人', visible: true },
                { key: 7, label: '更新时间', visible: true },
                { key: 8, label: '备注', visible: true },
      ],
  });

  const { queryParams, form, rules,columns } = toRefs(data);

  /** 查询提示词信息列表 */
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
    listPromptInfo(queryParams.value).then(response => {
            promptInfoList.value = response.rows;
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
                    infoId: null,
                    name: null,
                    content: null,
                    orderNum: null,
                    createBy: null,
                    createTime: null,
                    updateBy: null,
                    updateTime: null,
                    remark: null
    };
    proxy.resetForm("promptInfoRef");
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
    ids.value = selection.map(item => item.infoId);
    single.value = selection.length != 1;
    multiple.value = !selection.length;
  }

  /** 新增按钮操作 */
  function handleAdd() {
    reset();
    open.value = true;
    title.value = "添加提示词信息";
  }

  /** 修改按钮操作 */
  function handleUpdate(row) {
    reset();
    const _infoId = row.infoId || ids.value
    getPromptInfo(_infoId).then(response => {
      form.value = response.data;
      open.value = true;
      title.value = "修改提示词信息";
    });
  }

  /** 提交按钮 */
  function submitForm() {
    proxy.$refs["promptInfoRef"].validate(valid => {
      if (valid) {
        if (form.value.infoId != null) {
          updatePromptInfo(form.value).then(response => {
            proxy.$modal.msgSuccess("修改成功");
            open.value = false;
            getList();
          });
        } else {
          addPromptInfo(form.value).then(response => {
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
    const _infoIds = row.infoId || ids.value;
    proxy.$modal.confirm('是否确认删除提示词信息编号为"' + _infoIds + '"的数据项？').then(function() {
      return delPromptInfo(_infoIds);
    }).then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    }).catch(() => {});
  }

  /** 导出按钮操作 */
  function handleExport() {
    proxy.download('ai/promptInfo/export', {
      ...queryParams.value
    }, `promptInfo_${new Date().getTime()}.xlsx`)
  }

  getList();
</script>
