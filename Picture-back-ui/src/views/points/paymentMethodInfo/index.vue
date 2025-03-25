<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
                  <el-form-item label="支付方式编号" prop="methodId">
                    <el-input
                        v-model="queryParams.methodId"
                        placeholder="请输入支付方式编号"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                  </el-form-item>
                  <el-form-item label="名称" prop="methodName">
                    <el-input
                        v-model="queryParams.methodName"
                        placeholder="请输入名称"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                  </el-form-item>
                  <el-form-item label="第三方支付平台" prop="thirdParty">
                    <el-input
                        v-model="queryParams.thirdParty"
                        placeholder="请输入第三方支付平台"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                  </el-form-item>
                  <el-form-item label="类型" prop="methodType">
                    <el-select v-model="queryParams.methodType" style="width: 200px" placeholder="请选择类型" clearable>
                      <el-option
                          v-for="dict in po_method_type"
                          :key="dict.value"
                          :label="dict.label"
                          :value="dict.value"
                      />
                    </el-select>
                  </el-form-item>
                  <el-form-item label="支付接口URL" prop="apiUrl">
                    <el-input
                        v-model="queryParams.apiUrl"
                        placeholder="请输入支付接口URL"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                  </el-form-item>
                  <el-form-item label="商户号" prop="merchantId">
                    <el-input
                        v-model="queryParams.merchantId"
                        placeholder="请输入商户号"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                  </el-form-item>
                  <el-form-item label="状态" prop="methodStatus">
                    <el-select v-model="queryParams.methodStatus" style="width: 200px" placeholder="请选择状态" clearable>
                      <el-option
                          v-for="dict in po_method_status"
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
                  <el-form-item label="更新时间" prop="updateTime">
                    <el-date-picker clearable
                                    v-model="queryParams.updateTime"
                                    type="date"
                                    value-format="YYYY-MM-DD"
                                    placeholder="请选择更新时间">
                    </el-date-picker>
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
            v-hasPermi="['points:paymentMethodInfo:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="success"
            plain
            icon="Edit"
            :disabled="single"
            @click="handleUpdate"
            v-hasPermi="['points:paymentMethodInfo:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="danger"
            plain
            icon="Delete"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['points:paymentMethodInfo:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['points:paymentMethodInfo:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="paymentMethodInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
              <el-table-column label="支付方式编号" align="center" prop="methodId" v-if="columns[0].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="名称" align="center" prop="methodName" v-if="columns[1].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="第三方支付平台" align="center" prop="thirdParty" v-if="columns[2].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="类型" align="center" prop="methodType" v-if="columns[3].visible">
                <template #default="scope">
                      <dict-tag :options="po_method_type" :value="scope.row.methodType"/>
                </template>
              </el-table-column>
                <el-table-column label="支付接口URL" align="center" prop="apiUrl" v-if="columns[4].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="商户号" align="center" prop="merchantId" v-if="columns[5].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="应用编号" align="center" prop="appId" v-if="columns[6].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="秘钥" align="center" prop="secretKey" v-if="columns[7].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="联系方式" align="center" prop="contactInformation" v-if="columns[8].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="扩展配置" align="center" prop="extendConfig" v-if="columns[9].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="状态" align="center" prop="methodStatus" v-if="columns[10].visible">
                <template #default="scope">
                      <dict-tag :options="po_method_status" :value="scope.row.methodStatus"/>
                </template>
              </el-table-column>
                <el-table-column label="创建时间" align="center" prop="createTime" width="180" v-if="columns[11].visible" :show-overflow-tooltip="true">
                <template #default="scope">
                  <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
                </template>
              </el-table-column>
                <el-table-column label="更新时间" align="center" prop="updateTime" width="180" v-if="columns[12].visible" :show-overflow-tooltip="true">
                <template #default="scope">
                  <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
                </template>
              </el-table-column>
                <el-table-column label="备注" align="center" prop="remark" v-if="columns[13].visible" :show-overflow-tooltip="true"/>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['points:paymentMethodInfo:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['points:paymentMethodInfo:remove']">删除</el-button>
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

    <!-- 添加或修改支付方式对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="paymentMethodInfoRef" :model="form" :rules="rules" label-width="80px">
                        <el-form-item label="名称" prop="methodName">
                          <el-input v-model="form.methodName" placeholder="请输入名称" />
                        </el-form-item>
                        <el-form-item label="第三方支付平台" prop="thirdParty">
                          <el-input v-model="form.thirdParty" placeholder="请输入第三方支付平台" />
                        </el-form-item>
                        <el-form-item label="类型" prop="methodType">
                          <el-select v-model="form.methodType" placeholder="请选择类型">
                            <el-option
                                v-for="dict in po_method_type"
                                :key="dict.value"
                                :label="dict.label"
                                :value="dict.value"
                            ></el-option>
                          </el-select>
                        </el-form-item>
                        <el-form-item label="支付接口URL" prop="apiUrl">
                          <el-input v-model="form.apiUrl" placeholder="请输入支付接口URL" />
                        </el-form-item>
                        <el-form-item label="商户号" prop="merchantId">
                          <el-input v-model="form.merchantId" placeholder="请输入商户号" />
                        </el-form-item>
                        <el-form-item label="应用编号" prop="appId">
                          <el-input v-model="form.appId" placeholder="请输入应用编号" />
                        </el-form-item>
                        <el-form-item label="秘钥" prop="secretKey">
                          <el-input v-model="form.secretKey" type="textarea" placeholder="请输入内容" />
                        </el-form-item>
                        <el-form-item label="联系方式" prop="contactInformation">
                          <el-input v-model="form.contactInformation" type="textarea" placeholder="请输入内容" />
                        </el-form-item>
                        <el-form-item label="扩展配置" prop="extendConfig">
                          <el-input v-model="form.extendConfig" type="textarea" placeholder="请输入内容" />
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

<script setup name="PaymentMethodInfo">
  import { listPaymentMethodInfo, getPaymentMethodInfo, delPaymentMethodInfo, addPaymentMethodInfo, updatePaymentMethodInfo } from "@/api/points/paymentMethodInfo";

  const { proxy } = getCurrentInstance();
      const { po_method_type, po_method_status } = proxy.useDict('po_method_type', 'po_method_status');

  const paymentMethodInfoList = ref([]);
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
                    methodId: null,
                    methodName: null,
                    thirdParty: null,
                    methodType: null,
                    apiUrl: null,
                    merchantId: null,
                    methodStatus: null,
                    createTime: null,
                    updateTime: null,
    },
    rules: {
                    methodName: [
                { required: true, message: "名称不能为空", trigger: "blur" }
              ],
                    thirdParty: [
                { required: true, message: "第三方支付平台不能为空", trigger: "blur" }
              ],
                    methodType: [
                { required: true, message: "类型不能为空", trigger: "change" }
              ],
                    methodStatus: [
                { required: true, message: "状态不能为空", trigger: "change" }
              ],
                    createTime: [
                { required: true, message: "创建时间不能为空", trigger: "blur" }
              ],
    },
    //表格展示列
    columns: [
              { key: 0, label: '支付方式编号', visible: true },
                { key: 1, label: '名称', visible: true },
                { key: 2, label: '第三方支付平台', visible: true },
                { key: 3, label: '类型', visible: true },
                { key: 4, label: '支付接口URL', visible: true },
                { key: 5, label: '商户号', visible: true },
                { key: 6, label: '应用编号', visible: true },
                { key: 7, label: '秘钥', visible: true },
                { key: 8, label: '联系方式', visible: true },
                { key: 9, label: '扩展配置', visible: true },
                { key: 10, label: '状态', visible: true },
                { key: 11, label: '创建时间', visible: true },
                { key: 12, label: '更新时间', visible: true },
                { key: 13, label: '备注', visible: true },
      ],
  });

  const { queryParams, form, rules,columns } = toRefs(data);

  /** 查询支付方式列表 */
  function getList() {
    loading.value = true;
            queryParams.value.params = {};
            if (null != daterangeCreateTime && '' != daterangeCreateTime) {
              queryParams.value.params["beginCreateTime"] = daterangeCreateTime.value[0];
              queryParams.value.params["endCreateTime"] = daterangeCreateTime.value[1];
            }
    listPaymentMethodInfo(queryParams.value).then(response => {
            paymentMethodInfoList.value = response.rows;
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
                    methodId: null,
                    methodName: null,
                    thirdParty: null,
                    methodType: null,
                    apiUrl: null,
                    merchantId: null,
                    appId: null,
                    secretKey: null,
                    contactInformation: null,
                    extendConfig: null,
                    methodStatus: null,
                    createTime: null,
                    updateTime: null,
                    remark: null
    };
    proxy.resetForm("paymentMethodInfoRef");
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
    ids.value = selection.map(item => item.methodId);
    single.value = selection.length != 1;
    multiple.value = !selection.length;
  }

  /** 新增按钮操作 */
  function handleAdd() {
    reset();
    open.value = true;
    title.value = "添加支付方式";
  }

  /** 修改按钮操作 */
  function handleUpdate(row) {
    reset();
    const _methodId = row.methodId || ids.value
    getPaymentMethodInfo(_methodId).then(response => {
      form.value = response.data;
      open.value = true;
      title.value = "修改支付方式";
    });
  }

  /** 提交按钮 */
  function submitForm() {
    proxy.$refs["paymentMethodInfoRef"].validate(valid => {
      if (valid) {
        if (form.value.methodId != null) {
          updatePaymentMethodInfo(form.value).then(response => {
            proxy.$modal.msgSuccess("修改成功");
            open.value = false;
            getList();
          });
        } else {
          addPaymentMethodInfo(form.value).then(response => {
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
    const _methodIds = row.methodId || ids.value;
    proxy.$modal.confirm('是否确认删除支付方式编号为"' + _methodIds + '"的数据项？').then(function() {
      return delPaymentMethodInfo(_methodIds);
    }).then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    }).catch(() => {});
  }

  /** 导出按钮操作 */
  function handleExport() {
    proxy.download('points/paymentMethodInfo/export', {
      ...queryParams.value
    }, `paymentMethodInfo_${new Date().getTime()}.xlsx`)
  }

  getList();
</script>
