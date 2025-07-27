<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户ID" prop="userId">
        <el-input
            v-model="queryParams.userId"
            placeholder="请输入用户ID"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户名" prop="userName">
        <el-input
            v-model="queryParams.userName"
            placeholder="请输入用户名"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input
            v-model="queryParams.phone"
            placeholder="请输入手机号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="国家代码" prop="countryCode">
        <el-input
            v-model="queryParams.countryCode"
            placeholder="请输入国家代码"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="昵称" prop="nickName">
        <el-input
            v-model="queryParams.nickName"
            placeholder="请输入昵称"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" style="width: 200px" placeholder="请选择状态" clearable>
          <el-option
              v-for="dict in u_user_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <!--      <el-form-item label="加密盐" prop="salt">-->
      <!--        <el-input-->
      <!--            v-model="queryParams.salt"-->
      <!--            placeholder="请输入加密盐"-->
      <!--            clearable-->
      <!--            @keyup.enter="handleQuery"-->
      <!--        />-->
      <!--      </el-form-item>-->
      <el-form-item label="性别" prop="sex">
        <el-select v-model="queryParams.sex" style="width: 200px" placeholder="请选择性别" clearable>
          <el-option
              v-for="dict in u_user_sex"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="生日" style="width: 308px">
        <el-date-picker
            v-model="daterangeBirthday"
            value-format="YYYY-MM-DD"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="职业" prop="occupation">
        <el-input
            v-model="queryParams.occupation"
            placeholder="请输入职业"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="偏好语言" prop="preferredLanguageLocale">
        <el-input
            v-model="queryParams.preferredLanguageLocale"
            placeholder="请输入偏好语言"
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
      <!--      <el-form-item label="最后登录时间" style="width: 308px">-->
      <!--        <el-date-picker-->
      <!--            v-model="daterangeLastLoginTime"-->
      <!--            value-format="YYYY-MM-DD"-->
      <!--            type="daterange"-->
      <!--            range-separator="-"-->
      <!--            start-placeholder="开始日期"-->
      <!--            end-placeholder="结束日期"-->
      <!--        ></el-date-picker>-->
      <!--      </el-form-item>-->
      <!--      <el-form-item label="最后登录IP" prop="lastLoginIp">-->
      <!--        <el-input-->
      <!--            v-model="queryParams.lastLoginIp"-->
      <!--            placeholder="请输入最后登录IP"-->
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
            v-hasPermi="['user:userInfo:add']"
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
            v-hasPermi="['user:userInfo:edit']"
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
            v-hasPermi="['user:userInfo:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['user:userInfo:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table ref="tableRef" v-loading="loading" :data="userInfoList" @selection-change="handleSelectionChange"
              @sort-change="customSort">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="序号" type="index" width="50"/>
      <el-table-column label="用户ID" align="center" prop="userId" v-if="columns[0].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="用户名" align="center" prop="userName" v-if="columns[1].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="手机号" align="center" prop="phone" v-if="columns[2].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="国家代码" align="center" prop="countryCode" v-if="columns[3].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="昵称" align="center" prop="nickName" v-if="columns[4].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="头像地址" align="center" prop="avatarUrl" width="100" v-if="columns[5].visible">
        <template #default="scope">
          <image-preview :src="scope.row.avatarUrl" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="密码" align="center" prop="password" v-if="columns[6].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="状态" align="center" prop="status" v-if="columns[7].visible">
        <template #default="scope">
          <dict-tag :options="u_user_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="加密盐" align="center" prop="salt" v-if="columns[8].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="性别" align="center" prop="sex" v-if="columns[9].visible">
        <template #default="scope">
          <dict-tag :options="u_user_sex" :value="scope.row.sex"/>
        </template>
      </el-table-column>
      <el-table-column label="生日" align="center" prop="birthday" width="180" sortable="custom"
                       v-if="columns[10].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.birthday, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="职业" align="center" prop="occupation" v-if="columns[11].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="偏好语言" align="center" prop="preferredLanguageLocale" v-if="columns[12].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="个人简介" align="center" prop="introductory" v-if="columns[13].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="IP属地" align="center" prop="ipAddress" v-if="columns[14].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="最后登录时间" align="center" prop="lastLoginTime" sortable="custom" width="180"
                       v-if="columns[15].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.lastLoginTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="最后登录IP" align="center" prop="lastLoginIp" v-if="columns[16].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="创建时间" align="center" prop="createTime" sortable="custom" width="180"
                       v-if="columns[17].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180" sortable="custom"
                       v-if="columns[18].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="删除标记" align="center" prop="isDelete" v-if="columns[19].visible">
        <template #default="scope">
          <dict-tag :options="common_delete" :value="scope.row.isDelete"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['user:userInfo:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['user:userInfo:remove']">删除
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

    <!-- 添加或修改用户信息对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="userInfoRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="form.userName" placeholder="请输入用户名"/>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号"/>
        </el-form-item>
        <el-form-item label="国家代码" prop="countryCode">
          <el-input v-model="form.countryCode" placeholder="请输入国家代码"/>
        </el-form-item>
        <el-form-item label="昵称" prop="nickName">
          <el-input v-model="form.nickName" placeholder="请输入昵称"/>
        </el-form-item>
        <el-form-item label="头像地址" prop="avatarUrl">
          <el-input v-model="form.avatarUrl" placeholder="请输入头像地址"/>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" placeholder="请输入密码"/>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
                v-for="dict in u_user_status"
                :key="dict.value"
                :value="dict.value"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="加密盐" prop="salt">
          <el-input v-model="form.salt" placeholder="请输入加密盐"/>
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-select v-model="form.sex" placeholder="请选择性别">
            <el-option
                v-for="dict in u_user_sex"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="生日" prop="birthday">
          <el-date-picker clearable
                          v-model="form.birthday"
                          type="date"
                          value-format="YYYY-MM-DD"
                          placeholder="请选择生日">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="职业" prop="occupation">
          <el-input v-model="form.occupation" placeholder="请输入职业"/>
        </el-form-item>
        <el-form-item label="偏好语言" prop="preferredLanguageLocale">
          <el-input v-model="form.preferredLanguageLocale" placeholder="请输入偏好语言"/>
        </el-form-item>
        <el-form-item label="个人简介" prop="introductory">
          <el-input v-model="form.introductory" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="IP属地" prop="ipAddress">
          <el-input v-model="form.ipAddress" placeholder="请输入IP属地"/>
        </el-form-item>
        <el-form-item label="删除标记" prop="isDelete">
          <el-input v-model="form.isDelete" placeholder="请输入删除标记"/>
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

<script setup name="UserInfo">
import {listUserInfo, getUserInfo, delUserInfo, addUserInfo, updateUserInfo} from "@/api/user/userInfo";

const {proxy} = getCurrentInstance();
const {common_delete, u_user_status, u_user_sex} = proxy.useDict('common_delete', 'u_user_status', 'u_user_sex');

const userInfoList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const daterangeBirthday = ref([]);
const daterangeLastLoginTime = ref([]);
const daterangeCreateTime = ref([]);
const daterangeUpdateTime = ref([]);

const isAsc = ref();
const orderByColumn = ref('');
const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    userId: null,
    userName: null,
    phone: null,
    countryCode: null,
    nickName: null,
    status: null,
    salt: null,
    sex: null,
    birthday: null,
    occupation: null,
    preferredLanguageLocale: null,
    ipAddress: null,
    lastLoginTime: null,
    lastLoginIp: null,
    createTime: null,
    updateTime: null,
    isDelete: null
  },
  rules: {
    userName: [
      {required: true, message: "用户名不能为空", trigger: "blur"}
    ],
    phone: [
      {required: true, message: "手机号不能为空", trigger: "blur"}
    ],
    countryCode: [
      {required: true, message: "国家代码不能为空", trigger: "blur"}
    ],
    nickName: [
      {required: true, message: "昵称不能为空", trigger: "blur"}
    ],
    status: [
      {required: true, message: "状态不能为空", trigger: "change"}
    ],
    createTime: [
      {required: true, message: "创建时间不能为空", trigger: "blur"}
    ],
    isDelete: [
      {required: true, message: "删除标记不能为空", trigger: "blur"}
    ]
  },
  //表格展示列
  columns: [
    {key: 0, label: '用户ID', visible: false},
    {key: 1, label: '用户名', visible: true},
    {key: 2, label: '手机号', visible: true},
    {key: 3, label: '国家代码', visible: true},
    {key: 4, label: '昵称', visible: true},
    {key: 5, label: '头像地址', visible: true},
    {key: 6, label: '密码', visible: false},
    {key: 7, label: '状态', visible: true},
    {key: 8, label: '加密盐', visible: false},
    {key: 9, label: '性别', visible: true},
    {key: 10, label: '生日', visible: true},
    {key: 11, label: '职业', visible: false},
    {key: 12, label: '偏好语言', visible: false},
    {key: 13, label: '个人简介', visible: false},
    {key: 14, label: 'IP属地', visible: true},
    {key: 15, label: '最后登录时间', visible: true},
    {key: 16, label: '最后登录IP', visible: false},
    {key: 17, label: '创建时间', visible: true},
    {key: 18, label: '更新时间', visible: false},
    {key: 19, label: '删除标记', visible: false},
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


/** 查询用户信息列表 */
function getList() {
  loading.value = true;
  queryParams.value.params = {};
  if (orderByColumn.value != null && isAsc.value !== null) {
    queryParams.value.params["orderByColumn"] = orderByColumn.value;
    queryParams.value.params["isAsc"] = isAsc.value;
  }
  if (null != daterangeBirthday && '' != daterangeBirthday) {
    queryParams.value.params["beginBirthday"] = daterangeBirthday.value[0];
    queryParams.value.params["endBirthday"] = daterangeBirthday.value[1];
  }
  if (null != daterangeLastLoginTime && '' != daterangeLastLoginTime) {
    queryParams.value.params["beginLastLoginTime"] = daterangeLastLoginTime.value[0];
    queryParams.value.params["endLastLoginTime"] = daterangeLastLoginTime.value[1];
  }
  if (null != daterangeCreateTime && '' != daterangeCreateTime) {
    queryParams.value.params["beginCreateTime"] = daterangeCreateTime.value[0];
    queryParams.value.params["endCreateTime"] = daterangeCreateTime.value[1];
  }
  if (null != daterangeUpdateTime && '' != daterangeUpdateTime) {
    queryParams.value.params["beginUpdateTime"] = daterangeUpdateTime.value[0];
    queryParams.value.params["endUpdateTime"] = daterangeUpdateTime.value[1];
  }
  listUserInfo(queryParams.value).then(response => {
    userInfoList.value = response.rows;
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
    userId: null,
    userName: null,
    phone: null,
    countryCode: null,
    nickName: null,
    avatarUrl: null,
    password: null,
    status: null,
    salt: null,
    sex: null,
    birthday: null,
    occupation: null,
    preferredLanguageLocale: null,
    introductory: null,
    ipAddress: null,
    lastLoginTime: null,
    lastLoginIp: null,
    createTime: null,
    updateTime: null,
    isDelete: null
  };
  proxy.resetForm("userInfoRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  daterangeBirthday.value = [];
  daterangeLastLoginTime.value = [];
  daterangeCreateTime.value = [];
  daterangeUpdateTime.value = [];
  orderByColumn.value = null
  isAsc.value = null;
  proxy.$refs.tableRef.clearSort();
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.userId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加用户信息";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _userId = row.userId || ids.value
  getUserInfo(_userId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改用户信息";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["userInfoRef"].validate(valid => {
    if (valid) {
      if (form.value.userId != null) {
        updateUserInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addUserInfo(form.value).then(response => {
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
  const _userIds = row.userId || ids.value;
  proxy.$modal.confirm('是否确认删除用户信息编号为"' + _userIds + '"的数据项？').then(function () {
    return delUserInfo(_userIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('user/userInfo/export', {
    ...queryParams.value
  }, `userInfo_${new Date().getTime()}.xlsx`)
}

getList();
</script>
