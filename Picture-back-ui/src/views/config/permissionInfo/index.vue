<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
                  <el-form-item label="权限名称" prop="permissionName">
                    <el-input
                        v-model="queryParams.permissionName"
                        placeholder="请输入权限名称"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                  </el-form-item>
                  <el-form-item label="父菜单" prop="parentId">
                    <el-input
                        v-model="queryParams.parentId"
                        placeholder="请输入父菜单"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                  </el-form-item>
                  <el-form-item label="是否使用" prop="status">
                    <el-select v-model="queryParams.status" placeholder="请选择是否使用" clearable>
                      <el-option
                          v-for="dict in c_permission_status"
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
            v-hasPermi="['config:permissionInfo:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="info"
            plain
            icon="Sort"
            @click="toggleExpandAll"
        >展开/折叠</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table
        v-if="refreshTable"
        v-loading="loading"
        :data="permissionInfoList"
        row-key="permissionId"
        :default-expand-all="isExpandAll"
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
                    <el-table-column label="权限名称" prop="permissionName" v-if="columns[1].visible" :show-overflow-tooltip="true"/>
                    <el-table-column label="父菜单" align="center" prop="parentId" v-if="columns[2].visible" :show-overflow-tooltip="true"/>
                    <el-table-column label="显示顺序" align="center" prop="orderNum" v-if="columns[3].visible" :show-overflow-tooltip="true"/>
                    <el-table-column label="权限标识" align="center" prop="permission" v-if="columns[4].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="是否使用" align="center" prop="status" v-if="columns[5].visible">
                <template #default="scope">
                      <dict-tag :options="c_permission_status" :value="scope.row.status"/>
                </template>
              </el-table-column>
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
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['config:permissionInfo:edit']">修改</el-button>
          <el-button link type="primary" icon="Plus" @click="handleAdd(scope.row)" v-hasPermi="['config:permissionInfo:add']">新增</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['config:permissionInfo:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改权限信息对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="permissionInfoRef" :model="form" :rules="rules" label-width="80px">
                        <el-form-item label="权限名称" prop="permissionName">
                          <el-input v-model="form.permissionName" placeholder="请输入权限名称" />
                        </el-form-item>
                        <el-form-item label="父菜单" prop="parentId">
                          <el-tree-select
                              v-model="form.parentId"
                              :data="permissionInfoOptions"
                              :props="{ value: 'permissionId', label: 'permissionName', children: 'children' }"
                              value-key="permissionId"
                              placeholder="请选择父菜单"
                              check-strictly
                          />
                        </el-form-item>
                        <el-form-item label="显示顺序" prop="orderNum">
                          <el-input v-model="form.orderNum" placeholder="请输入显示顺序" />
                        </el-form-item>
                        <el-form-item label="权限标识" prop="permission">
                          <el-input v-model="form.permission" placeholder="请输入权限标识" />
                        </el-form-item>
                        <el-form-item label="是否使用" prop="status">
                          <el-radio-group v-model="form.status">
                            <el-radio
                                v-for="dict in c_permission_status"
                                :key="dict.value"
                                :value="dict.value"
                            >{{dict.label}}</el-radio>
                          </el-radio-group>
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

<script setup name="PermissionInfo">
  import { listPermissionInfo, getPermissionInfo, delPermissionInfo, addPermissionInfo, updatePermissionInfo } from "@/api/config/permissionInfo";

  const { proxy } = getCurrentInstance();
      const { c_permission_status } = proxy.useDict('c_permission_status');

  const permissionInfoList = ref([]);
  const permissionInfoOptions = ref([]);
  const open = ref(false);
  const loading = ref(true);
  const showSearch = ref(true);
  const title = ref("");
  const isExpandAll = ref(true);
  const refreshTable = ref(true);
          const daterangeCreateTime = ref([]);
          const daterangeUpdateTime = ref([]);

  const data = reactive({
    form: {},
    queryParams: {
                    permissionName: null,
                    parentId: null,
                    status: null,
                    createBy: null,
                    createTime: null,
                    updateBy: null,
                    updateTime: null,
    },
    rules: {
                    permissionName: [
                { required: true, message: "权限名称不能为空", trigger: "blur" }
              ],
                    status: [
                { required: true, message: "是否使用不能为空", trigger: "change" }
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
                { key: 1, label: '权限名称', visible: true },
                { key: 2, label: '父菜单', visible: true },
                { key: 3, label: '显示顺序', visible: true },
                { key: 4, label: '权限标识', visible: true },
                { key: 5, label: '是否使用', visible: true },
                { key: 6, label: '创建人', visible: true },
                { key: 7, label: '创建时间', visible: true },
                { key: 8, label: '更新人', visible: true },
                { key: 9, label: '更新时间', visible: true },
                { key: 10, label: '备注', visible: true },
      ],
  });

  const { queryParams, form, rules,columns } = toRefs(data);

  /** 查询权限信息列表 */
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
    listPermissionInfo(queryParams.value).then(response => {
            permissionInfoList.value = proxy.handleTree(response.data, "permissionId", "parentId");
      loading.value = false;
    });
  }

  /** 查询权限信息下拉树结构 */
  function getTreeselect() {
    listPermissionInfo().then(response => {
            permissionInfoOptions.value = [];
      const data = { permissionId: 0, permissionName: '顶级节点', children: [] };
      data.children = proxy.handleTree(response.data, "permissionId", "parentId");
            permissionInfoOptions.value.push(data);
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
                    permissionId: null,
                    permissionName: null,
                    parentId: null,
                    orderNum: null,
                    permission: null,
                    status: null,
                    createBy: null,
                    createTime: null,
                    updateBy: null,
                    updateTime: null,
                    remark: null
    };
    proxy.resetForm("permissionInfoRef");
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
    handleQuery();
  }

  /** 新增按钮操作 */
  function handleAdd(row) {
    reset();
    getTreeselect();
    if (row != null && row.permissionId) {
      form.value.parentId = row.permissionId;
    } else {
      form.value.parentId = 0;
    }
    open.value = true;
    title.value = "添加权限信息";
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
      form.value.parentId = row.permissionId;
    }
    getPermissionInfo(row.permissionId).then(response => {
      form.value = response.data;
      open.value = true;
      title.value = "修改权限信息";
    });
  }

  /** 提交按钮 */
  function submitForm() {
    proxy.$refs["permissionInfoRef"].validate(valid => {
      if (valid) {
        if (form.value.permissionId != null) {
          updatePermissionInfo(form.value).then(response => {
            proxy.$modal.msgSuccess("修改成功");
            open.value = false;
            getList();
          });
        } else {
          addPermissionInfo(form.value).then(response => {
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
    proxy.$modal.confirm('是否确认删除权限信息编号为"' + row.permissionId + '"的数据项？').then(function() {
      return delPermissionInfo(row.permissionId);
    }).then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    }).catch(() => {});
  }

  getList();
</script>
