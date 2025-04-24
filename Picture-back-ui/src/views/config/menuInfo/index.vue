<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="编号" prop="menuId">
        <el-input
            v-model="queryParams.menuId"
            placeholder="请输入编号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="菜单名称" prop="menuName">
        <el-input
            v-model="queryParams.menuName"
            placeholder="请输入菜单名称"
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
      <el-form-item label="路由名称" prop="routeName">
        <el-input
            v-model="queryParams.routeName"
            placeholder="请输入路由名称"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="显示位置" prop="menuAddress">
        <el-select v-model="queryParams.menuAddress" style="width: 200px" placeholder="请选择显示位置" clearable>
          <el-option
              v-for="dict in c_menu_address"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="是否外链" prop="isFrame">
        <el-select v-model="queryParams.isFrame" style="width: 200px" placeholder="请选择是否外链" clearable>
          <el-option
              v-for="dict in c_menu_is_frame"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="是否缓存" prop="isCache">
        <el-select v-model="queryParams.isCache" style="width: 200px" placeholder="请选择是否缓存" clearable>
          <el-option
              v-for="dict in c_menu_is_chache"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="菜单类型" prop="menuType">
        <el-select v-model="queryParams.menuType" style="width: 200px" placeholder="请选择菜单类型" clearable>
          <el-option
              v-for="dict in c_menu_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="是否显示" prop="visible">
        <el-select v-model="queryParams.visible" style="width: 200px" placeholder="请选择是否显示" clearable>
          <el-option
              v-for="dict in c_menu_visible"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="菜单状态" prop="status">
        <el-select v-model="queryParams.status" style="width: 200px" placeholder="请选择菜单状态" clearable>
          <el-option
              v-for="dict in sys_show_hide"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="权限标识" prop="perms">
        <el-input
            v-model="queryParams.perms"
            placeholder="请输入权限标识"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="菜单图标" prop="icon">
        <el-input
            v-model="queryParams.icon"
            placeholder="请输入菜单图标"
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
            v-hasPermi="['config:menuInfo:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="danger"
            plain
            icon="Refresh"
            @click="handleRefreshCache"
            v-hasPermi="['system:menuInfo:add']"
        >刷新缓存
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
        :data="menuInfoList"
        row-key="menuId"
        :default-expand-all="isExpandAll"
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column label="菜单名称" prop="menuName" v-if="columns[1].visible" :show-overflow-tooltip="true"/>
      <el-table-column label="父菜单" align="center" prop="parentId" v-if="columns[2].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="显示顺序" align="center" prop="orderNum" v-if="columns[3].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="路由地址" align="center" prop="path" v-if="columns[4].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="组件路径" align="center" prop="component" v-if="columns[5].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="路由参数" align="center" prop="query" v-if="columns[6].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="路由名称" align="center" prop="routeName" v-if="columns[7].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="显示位置" align="center" prop="menuAddress" v-if="columns[8].visible">
        <template #default="scope">
          <dict-tag :options="c_menu_address" :value="scope.row.menuAddress"/>
        </template>
      </el-table-column>
      <el-table-column label="是否外链" align="center" prop="isFrame" v-if="columns[9].visible">
        <template #default="scope">
          <dict-tag :options="c_menu_is_frame" :value="scope.row.isFrame"/>
        </template>
      </el-table-column>
      <el-table-column label="是否缓存" align="center" prop="isCache" v-if="columns[10].visible">
        <template #default="scope">
          <dict-tag :options="c_menu_is_chache" :value="scope.row.isCache"/>
        </template>
      </el-table-column>
      <el-table-column label="菜单类型" align="center" prop="menuType" v-if="columns[11].visible">
        <template #default="scope">
          <dict-tag :options="c_menu_type" :value="scope.row.menuType"/>
        </template>
      </el-table-column>
      <el-table-column label="是否显示" align="center" prop="visible" v-if="columns[12].visible">
        <template #default="scope">
          <dict-tag :options="c_menu_visible" :value="scope.row.visible"/>
        </template>
      </el-table-column>
      <el-table-column label="菜单状态" align="center" prop="status" v-if="columns[13].visible">
        <template #default="scope">
          <dict-tag :options="sys_show_hide" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="权限标识" align="center" prop="perms" v-if="columns[14].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="菜单图标" align="center" prop="icon" v-if="columns[15].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="创建人" align="center" prop="createBy" v-if="columns[16].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" v-if="columns[17].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新人" align="center" prop="updateBy" v-if="columns[18].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180" v-if="columns[19].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" v-if="columns[20].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['config:menuInfo:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Plus" @click="handleAdd(scope.row)" v-hasPermi="['config:menuInfo:add']">
            新增
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['config:menuInfo:remove']">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改菜单信息对话框 -->
    <el-dialog :title="title" v-model="open" width="650px" append-to-body>
      <el-form ref="menuInfoRef" :model="form" :rules="rules" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="父菜单" prop="parentId">
              <el-tree-select
                  v-model="form.parentId"
                  :data="menuInfoOptions"
                  :props="{ value: 'menuId', label: 'menuName', children: 'children' }"
                  value-key="menuId"
                  placeholder="请选择父菜单"
                  check-strictly
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="菜单类型" prop="menuType">
              <el-radio-group v-model="form.menuType">
                <el-radio
                    v-for="dict in c_menu_type"
                    :key="dict.value"
                    :value="dict.value"
                >{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="菜单名称" prop="menuName">
              <el-input v-model="form.menuName" placeholder="请输入菜单名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示顺序" prop="orderNum">
              <el-input-number :min="0" style="width: 100%" v-model="form.orderNum" placeholder="请输入显示顺序"/>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType === 'C' ||form.menuType==='M'">
            <el-form-item label="路由地址" prop="path">
              <el-input v-model="form.path" placeholder="请输入路由地址"/>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType==='C'">
            <el-form-item label="组件路径" prop="component">
              <el-input v-model="form.component" placeholder="请输入组件路径"/>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType==='C'">
            <el-form-item label="路由参数" prop="query">
              <el-input v-model="form.query" placeholder="请输入路由参数"/>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType === 'C' ||form.menuType==='M'">
            <el-form-item label="路由名称" prop="routeName">
              <el-input v-model="form.routeName" placeholder="请输入路由名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示位置" prop="menuAddress">
              <el-select v-model="form.menuAddress" placeholder="请选择显示位置">
                <el-option
                    v-for="dict in c_menu_address"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType==='C'">
            <el-form-item label="是否外链" prop="isFrame">
              <el-radio-group v-model="form.isFrame">
                <el-radio
                    v-for="dict in c_menu_is_frame"
                    :key="dict.value"
                    :value="dict.value"
                >{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType==='C'">
            <el-form-item label="是否缓存" prop="isCache">
              <el-radio-group v-model="form.isCache">
                <el-radio
                    v-for="dict in c_menu_is_chache"
                    :key="dict.value"
                    :value="dict.value"
                >{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否显示" prop="visible">
              <el-radio-group v-model="form.visible">
                <el-radio
                    v-for="dict in c_menu_visible"
                    :key="dict.value"
                    :value="dict.value"
                >{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="菜单状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio
                    v-for="dict in sys_show_hide"
                    :key="dict.value"
                    :value="dict.value"
                >{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="权限标识" prop="perms">
              <el-input v-model="form.perms" placeholder="请输入权限标识"/>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType === 'C' ||form.menuType==='M'">
            <el-form-item label="菜单图标" prop="icon">
              <el-input v-model="form.icon" placeholder="请输入菜单图标"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"/>
            </el-form-item>
          </el-col>
        </el-row>
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

<script setup name="MenuInfo">
import {
  listMenuInfo,
  getMenuInfo,
  delMenuInfo,
  addMenuInfo,
  updateMenuInfo,
  initMenuInfoCache
} from "@/api/config/menuInfo";
import {initConfigInfoCache} from "@/api/config/configInfo.js";

const {proxy} = getCurrentInstance();
const {
  c_menu_visible,
  c_menu_address,
  c_menu_is_chache,
  c_menu_type,
  sys_show_hide,
  c_menu_is_frame
} = proxy.useDict('c_menu_visible', 'c_menu_address', 'c_menu_is_chache', 'c_menu_type', 'sys_show_hide', 'c_menu_is_frame');

const menuInfoList = ref([]);
const menuInfoOptions = ref([]);
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
    menuId: null,
    menuName: null,
    parentId: null,
    routeName: null,
    menuAddress: null,
    isFrame: null,
    isCache: null,
    menuType: null,
    visible: null,
    status: null,
    perms: null,
    icon: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
  },
  rules: {
    menuName: [
      {required: true, message: "菜单名称不能为空", trigger: "blur"}
    ],
    menuAddress: [
      {required: true, message: "显示位置不能为空", trigger: "change"}
    ],
    isFrame: [
      {required: true, message: "是否外链不能为空", trigger: "change"}
    ],
    isCache: [
      {required: true, message: "是否缓存不能为空", trigger: "change"}
    ],
    menuType: [
      {required: true, message: "菜单类型不能为空", trigger: "change"}
    ],
    createBy: [
      {required: true, message: "创建人不能为空", trigger: "blur"}
    ],
    createTime: [
      {required: true, message: "创建时间不能为空", trigger: "blur"}
    ],
  },
  //表格展示列
  columns: [
    {key: 0, label: '编号', visible: false},
    {key: 1, label: '菜单名称', visible: true},
    {key: 2, label: '父菜单', visible: false},
    {key: 3, label: '显示顺序', visible: true},
    {key: 4, label: '路由地址', visible: true},
    {key: 5, label: '组件路径', visible: false},
    {key: 6, label: '路由参数', visible: false},
    {key: 7, label: '路由名称', visible: false},
    {key: 8, label: '显示位置', visible: true},
    {key: 9, label: '是否外链', visible: false},
    {key: 10, label: '是否缓存', visible: false},
    {key: 11, label: '菜单类型', visible: true},
    {key: 12, label: '是否显示', visible: true},
    {key: 13, label: '菜单状态', visible: true},
    {key: 14, label: '权限标识', visible: true},
    {key: 15, label: '菜单图标', visible: false},
    {key: 16, label: '创建人', visible: false},
    {key: 17, label: '创建时间', visible: false},
    {key: 18, label: '更新人', visible: false},
    {key: 19, label: '更新时间', visible: false},
    {key: 20, label: '备注', visible: false},
  ],
});

const {queryParams, form, rules, columns} = toRefs(data);

/** 查询菜单信息列表 */
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
  listMenuInfo(queryParams.value).then(response => {
    menuInfoList.value = proxy.handleTree(response.data, "menuId", "parentId");
    loading.value = false;
  });
}

/** 查询菜单信息下拉树结构 */
function getTreeselect() {
  listMenuInfo().then(response => {
    menuInfoOptions.value = [];
    const data = {menuId: 0, menuName: '顶级节点', children: []};
    data.children = proxy.handleTree(response.data, "menuId", "parentId");
    menuInfoOptions.value.push(data);
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
    menuId: null,
    menuName: null,
    parentId: null,
    orderNum: null,
    path: null,
    component: null,
    query: null,
    routeName: null,
    menuAddress: null,
    isFrame: null,
    isCache: null,
    menuType: 'M',
    visible: null,
    status: null,
    perms: null,
    icon: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
    remark: null
  };
  proxy.resetForm("menuInfoRef");
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
  if (row != null && row.menuId) {
    form.value.parentId = row.menuId;
  } else {
    form.value.parentId = 0;
  }
  open.value = true;
  title.value = "添加菜单信息";
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
    form.value.parentId = row.menuId;
  }
  getMenuInfo(row.menuId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改菜单信息";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["menuInfoRef"].validate(valid => {
    if (valid) {
      if (form.value.menuId != null) {
        updateMenuInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addMenuInfo(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除菜单信息编号为"' + row.menuId + '"的数据项？').then(function () {
    return delMenuInfo(row.menuId);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

function handleRefreshCache() {
  initMenuInfoCache().then(res => {
    proxy.$modal.msgSuccess("刷新缓存成功");
  })
}

getList();
</script>
