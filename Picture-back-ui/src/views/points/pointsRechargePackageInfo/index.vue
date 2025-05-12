<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="套餐编号" prop="packageId">
        <el-input
            v-model="queryParams.packageId"
            placeholder="请输入套餐编号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="套餐名称" prop="packageName">
        <el-input
            v-model="queryParams.packageName"
            placeholder="请输入套餐名称"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否长期" prop="isLongTerm">
        <el-select v-model="queryParams.isLongTerm" style="width: 200px" placeholder="请选择是否长期" clearable>
          <el-option
              v-for="dict in po_package_is_long_term"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="生效时间" style="width: 308px">
        <el-date-picker
            v-model="daterangeStartTime"
            value-format="YYYY-MM-DD"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="结束时间" style="width: 308px">
        <el-date-picker
            v-model="daterangeEndTime"
            value-format="YYYY-MM-DD"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="套餐状态" prop="packageStatus">
        <el-select v-model="queryParams.packageStatus" style="width: 200px" placeholder="请选择套餐状态" clearable>
          <el-option
              v-for="dict in po_package_status"
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
            v-hasPermi="['points:pointsRechargePackageInfo:add']"
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
            v-hasPermi="['points:pointsRechargePackageInfo:edit']"
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
            v-hasPermi="['points:pointsRechargePackageInfo:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['points:pointsRechargePackageInfo:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="pointsRechargePackageInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="套餐编号" align="center" prop="packageId" v-if="columns[0].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="套餐名称" align="center" prop="packageName" v-if="columns[1].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="套餐价格" align="center" prop="price" v-if="columns[2].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="积分数量" align="center" prop="points" v-if="columns[3].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="赠送积分" align="center" prop="pointsBonus" v-if="columns[4].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="套餐描述" align="center" prop="description" v-if="columns[5].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="是否长期" align="center" prop="isLongTerm" v-if="columns[6].visible">
        <template #default="scope">
          <dict-tag :options="po_package_is_long_term" :value="scope.row.isLongTerm"/>
        </template>
      </el-table-column>
      <el-table-column label="生效时间" align="center" prop="startTime" width="180" v-if="columns[7].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.startTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endTime" width="180" v-if="columns[8].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="套餐状态" align="center" prop="packageStatus" v-if="columns[9].visible">
        <template #default="scope">
          <dict-tag :options="po_package_status" :value="scope.row.packageStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" v-if="columns[10].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180" v-if="columns[11].visible"
                       :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" v-if="columns[12].visible"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['points:pointsRechargePackageInfo:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['points:pointsRechargePackageInfo:remove']">删除
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

    <!-- 添加或修改充值积分套餐对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="pointsRechargePackageInfoRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="套餐名称" prop="packageName">
          <el-input v-model="form.packageName" placeholder="请输入套餐名称"/>
        </el-form-item>
        <el-form-item label="套餐价格" prop="price">
          <el-input-number :min="0" :precision="2" v-model="form.price" placeholder="请输入套餐价格"
                           style="width: 100%"/>
        </el-form-item>
        <el-form-item label="积分数量" prop="points">
          <template #label>
           <span class="custom-label">
             积分数量
             <el-tooltip effect="light" placement="top" content="1元/100积分">
           <QuestionFilled class="tooltip-icon"/>
             </el-tooltip>
           </span>
          </template>
          <el-input-number :min="0" v-model="form.points" placeholder="请输入套餐积分数量" style="width: 100%"/>
        </el-form-item>
        <el-form-item label="赠送积分" prop="pointsBonus">
          <el-input-number :min="0" v-model="form.pointsBonus" placeholder="请输入套餐赠送积分" style="width: 100%"/>
        </el-form-item>
        <el-form-item label="套餐描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="是否长期" prop="isLongTerm">
          <el-radio-group v-model="form.isLongTerm">
            <el-radio
                v-for="dict in po_package_is_long_term"
                :key="dict.value"
                :value="dict.value"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="生效时间" prop="startTime">
          <el-date-picker clearable
                          v-model="form.startTime"
                          type="date"
                          value-format="YYYY-MM-DD"
                          placeholder="请选择套餐生效时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker clearable
                          v-model="form.endTime"
                          type="date"
                          value-format="YYYY-MM-DD"
                          placeholder="请选择套餐结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="套餐状态" prop="packageStatus">
          <el-radio-group v-model="form.packageStatus">
            <el-radio
                v-for="dict in po_package_status"
                :key="dict.value"
                :value="dict.value"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"/>
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

<script setup name="PointsRechargePackageInfo">
import {
  listPointsRechargePackageInfo,
  getPointsRechargePackageInfo,
  delPointsRechargePackageInfo,
  addPointsRechargePackageInfo,
  updatePointsRechargePackageInfo
} from "@/api/points/pointsRechargePackageInfo";
import {QuestionFilled} from "@element-plus/icons-vue";

const {proxy} = getCurrentInstance();
const {po_package_status, po_package_is_long_term} = proxy.useDict('po_package_status', 'po_package_is_long_term');

const pointsRechargePackageInfoList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const daterangeStartTime = ref([]);
const daterangeEndTime = ref([]);
const daterangeCreateTime = ref([]);
const daterangeUpdateTime = ref([]);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    packageId: null,
    packageName: null,
    isLongTerm: null,
    startTime: null,
    endTime: null,
    packageStatus: null,
    createTime: null,
    updateTime: null,
  },
  rules: {
    packageName: [
      {required: true, message: "套餐名称不能为空", trigger: "blur"}
    ],
    price: [
      {required: true, message: "套餐价格不能为空", trigger: "blur"}
    ],
    points: [
      {required: true, message: "套餐积分数量不能为空", trigger: "blur"}
    ],
    isLongTerm: [
      {required: true, message: "是否长期不能为空", trigger: "change"}
    ],
    packageStatus: [
      {required: true, message: "套餐状态不能为空", trigger: "change"}
    ],
    createTime: [
      {required: true, message: "创建时间不能为空", trigger: "blur"}
    ],
  },
  //表格展示列
  columns: [
    {key: 0, label: '套餐编号', visible: false},
    {key: 1, label: '套餐名称', visible: true},
    {key: 2, label: '套餐价格', visible: true},
    {key: 3, label: '套餐积分数量', visible: true},
    {key: 4, label: '套餐赠送积分', visible: true},
    {key: 5, label: '套餐描述', visible: true},
    {key: 6, label: '是否长期', visible: true},
    {key: 7, label: '套餐生效时间', visible: true},
    {key: 8, label: '套餐结束时间', visible: true},
    {key: 9, label: '套餐状态', visible: true},
    {key: 10, label: '创建时间', visible: true},
    {key: 11, label: '更新时间', visible: false},
    {key: 12, label: '备注', visible: false},
  ],
});

const {queryParams, form, rules, columns} = toRefs(data);

/** 查询充值积分套餐列表 */
function getList() {
  loading.value = true;
  queryParams.value.params = {};
  if (null != daterangeStartTime && '' != daterangeStartTime) {
    queryParams.value.params["beginStartTime"] = daterangeStartTime.value[0];
    queryParams.value.params["endStartTime"] = daterangeStartTime.value[1];
  }
  if (null != daterangeEndTime && '' != daterangeEndTime) {
    queryParams.value.params["beginEndTime"] = daterangeEndTime.value[0];
    queryParams.value.params["endEndTime"] = daterangeEndTime.value[1];
  }
  if (null != daterangeCreateTime && '' != daterangeCreateTime) {
    queryParams.value.params["beginCreateTime"] = daterangeCreateTime.value[0];
    queryParams.value.params["endCreateTime"] = daterangeCreateTime.value[1];
  }
  if (null != daterangeUpdateTime && '' != daterangeUpdateTime) {
    queryParams.value.params["beginUpdateTime"] = daterangeUpdateTime.value[0];
    queryParams.value.params["endUpdateTime"] = daterangeUpdateTime.value[1];
  }
  listPointsRechargePackageInfo(queryParams.value).then(response => {
    pointsRechargePackageInfoList.value = response.rows;
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
    packageId: null,
    packageName: null,
    price: null,
    points: null,
    pointsBonus: null,
    description: null,
    isLongTerm: null,
    startTime: null,
    endTime: null,
    packageStatus: null,
    createTime: null,
    updateTime: null,
    remark: null
  };
  proxy.resetForm("pointsRechargePackageInfoRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  daterangeStartTime.value = [];
  daterangeEndTime.value = [];
  daterangeCreateTime.value = [];
  daterangeUpdateTime.value = [];
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.packageId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加充值积分套餐";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _packageId = row.packageId || ids.value
  getPointsRechargePackageInfo(_packageId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改充值积分套餐";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["pointsRechargePackageInfoRef"].validate(valid => {
    if (valid) {
      if (form.value.packageId != null) {
        updatePointsRechargePackageInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addPointsRechargePackageInfo(form.value).then(response => {
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
  const _packageIds = row.packageId || ids.value;
  proxy.$modal.confirm('是否确认删除充值积分套餐编号为"' + _packageIds + '"的数据项？').then(function () {
    return delPointsRechargePackageInfo(_packageIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('points/pointsRechargePackageInfo/export', {
    ...queryParams.value
  }, `pointsRechargePackageInfo_${new Date().getTime()}.xlsx`)
}

getList();
</script>

<style scoped>
.custom-label {
  display: inline-flex; /* 保持内联弹性布局 */
  align-items: center;  /* 垂直居中 */
}
.tooltip-icon {
  width: 14px;
  height: 14px;
  color: #F56C6C;
  margin-left: 5px;
}
</style>
