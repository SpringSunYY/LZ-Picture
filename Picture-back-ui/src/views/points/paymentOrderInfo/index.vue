<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
                  <el-form-item label="订单编号" prop="orderId">
                    <el-input
                        v-model="queryParams.orderId"
                        placeholder="请输入订单编号"
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
                  <el-form-item label="订单类型" prop="orderType">
                    <el-select v-model="queryParams.orderType" style="width: 200px" placeholder="请选择订单类型" clearable>
                      <el-option
                          v-for="dict in po_order_type"
                          :key="dict.value"
                          :label="dict.label"
                          :value="dict.value"
                      />
                    </el-select>
                  </el-form-item>
                  <el-form-item label="订单状态" prop="orderStatus">
                    <el-select v-model="queryParams.orderStatus" style="width: 200px" placeholder="请选择订单状态" clearable>
                      <el-option
                          v-for="dict in po_order_status"
                          :key="dict.value"
                          :label="dict.label"
                          :value="dict.value"
                      />
                    </el-select>
                  </el-form-item>
                  <el-form-item label="订单总金额" prop="amount">
                    <el-input
                        v-model="queryParams.amount"
                        placeholder="请输入订单总金额"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                  </el-form-item>
                  <el-form-item label="支付方式编号" prop="paymentId">
                    <el-input
                        v-model="queryParams.paymentId"
                        placeholder="请输入支付方式编号"
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
                  <el-form-item label="第三方支付平台订单号" prop="thirdPartyOrder">
                    <el-input
                        v-model="queryParams.thirdPartyOrder"
                        placeholder="请输入第三方支付平台订单号"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                  </el-form-item>
                  <el-form-item label="支付时间" style="width: 308px">
                    <el-date-picker
                        v-model="daterangePaymentTime"
                        value-format="YYYY-MM-DD"
                        type="daterange"
                        range-separator="-"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                    ></el-date-picker>
                  </el-form-item>
                  <el-form-item label="支付状态" prop="paymentStatus">
                    <el-select v-model="queryParams.paymentStatus" style="width: 200px" placeholder="请选择支付状态" clearable>
                      <el-option
                          v-for="dict in po_payment_status"
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
                  <el-form-item label="IP地址" prop="ipAddr">
                    <el-input
                        v-model="queryParams.ipAddr"
                        placeholder="请输入IP地址"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                  </el-form-item>
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
            v-hasPermi="['points:paymentOrderInfo:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="success"
            plain
            icon="Edit"
            :disabled="single"
            @click="handleUpdate"
            v-hasPermi="['points:paymentOrderInfo:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="danger"
            plain
            icon="Delete"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['points:paymentOrderInfo:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['points:paymentOrderInfo:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="paymentOrderInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
              <el-table-column label="订单编号" align="center" prop="orderId" v-if="columns[0].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="用户编号" align="center" prop="userId" v-if="columns[1].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="订单类型" align="center" prop="orderType" v-if="columns[2].visible">
                <template #default="scope">
                      <dict-tag :options="po_order_type" :value="scope.row.orderType"/>
                </template>
              </el-table-column>
                <el-table-column label="订单状态" align="center" prop="orderStatus" v-if="columns[3].visible">
                <template #default="scope">
                      <dict-tag :options="po_order_status" :value="scope.row.orderStatus"/>
                </template>
              </el-table-column>
                <el-table-column label="订单总金额" align="center" prop="amount" v-if="columns[4].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="支付方式编号" align="center" prop="paymentId" v-if="columns[5].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="第三方支付平台" align="center" prop="thirdParty" v-if="columns[6].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="第三方支付平台订单号" align="center" prop="thirdPartyOrder" v-if="columns[7].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="支付时间" align="center" prop="paymentTime" width="180" v-if="columns[8].visible" :show-overflow-tooltip="true">
                <template #default="scope">
                  <span>{{ parseTime(scope.row.paymentTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
                </template>
              </el-table-column>
                <el-table-column label="支付状态" align="center" prop="paymentStatus" v-if="columns[9].visible">
                <template #default="scope">
                      <dict-tag :options="po_payment_status" :value="scope.row.paymentStatus"/>
                </template>
              </el-table-column>
                <el-table-column label="支付手续费" align="center" prop="transactionFee" v-if="columns[10].visible" :show-overflow-tooltip="true"/>
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
                <el-table-column label="支付失败原因" align="center" prop="failReason" v-if="columns[13].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="设备唯一标识" align="center" prop="deviceId" v-if="columns[14].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="浏览器类型" align="center" prop="browser" v-if="columns[15].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="操作系统" align="center" prop="os" v-if="columns[16].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="平台" align="center" prop="platform" v-if="columns[17].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="IP地址" align="center" prop="ipAddr" v-if="columns[18].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="删除" align="center" prop="isDelete" v-if="columns[19].visible">
                <template #default="scope">
                      <dict-tag :options="common_delete" :value="scope.row.isDelete"/>
                </template>
              </el-table-column>
                <el-table-column label="备注" align="center" prop="remark" v-if="columns[20].visible" :show-overflow-tooltip="true"/>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['points:paymentOrderInfo:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['points:paymentOrderInfo:remove']">删除</el-button>
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

    <!-- 添加或修改支付订单对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="paymentOrderInfoRef" :model="form" :rules="rules" label-width="80px">
                        <el-form-item label="用户编号" prop="userId">
                          <el-input v-model="form.userId" placeholder="请输入用户编号" />
                        </el-form-item>
                        <el-form-item label="订单类型" prop="orderType">
                          <el-select v-model="form.orderType" placeholder="请选择订单类型">
                            <el-option
                                v-for="dict in po_order_type"
                                :key="dict.value"
                                :label="dict.label"
                                :value="dict.value"
                            ></el-option>
                          </el-select>
                        </el-form-item>
                        <el-form-item label="订单状态" prop="orderStatus">
                          <el-radio-group v-model="form.orderStatus">
                            <el-radio
                                v-for="dict in po_order_status"
                                :key="dict.value"
                                :value="dict.value"
                            >{{dict.label}}</el-radio>
                          </el-radio-group>
                        </el-form-item>
                        <el-form-item label="订单总金额" prop="amount">
                          <el-input v-model="form.amount" placeholder="请输入订单总金额" />
                        </el-form-item>
                        <el-form-item label="支付方式编号" prop="paymentId">
                          <el-input v-model="form.paymentId" placeholder="请输入支付方式编号" />
                        </el-form-item>
                        <el-form-item label="第三方支付平台" prop="thirdParty">
                          <el-input v-model="form.thirdParty" placeholder="请输入第三方支付平台" />
                        </el-form-item>
                        <el-form-item label="第三方支付平台订单号" prop="thirdPartyOrder">
                          <el-input v-model="form.thirdPartyOrder" placeholder="请输入第三方支付平台订单号" />
                        </el-form-item>
                        <el-form-item label="支付时间" prop="paymentTime">
                          <el-date-picker clearable
                                          v-model="form.paymentTime"
                                          type="date"
                                          value-format="YYYY-MM-DD"
                                          placeholder="请选择支付时间">
                          </el-date-picker>
                        </el-form-item>
                        <el-form-item label="支付状态" prop="paymentStatus">
                          <el-radio-group v-model="form.paymentStatus">
                            <el-radio
                                v-for="dict in po_payment_status"
                                :key="dict.value"
                                :value="dict.value"
                            >{{dict.label}}</el-radio>
                          </el-radio-group>
                        </el-form-item>
                        <el-form-item label="支付手续费" prop="transactionFee">
                          <el-input v-model="form.transactionFee" placeholder="请输入支付手续费" />
                        </el-form-item>
                        <el-form-item label="支付失败原因" prop="failReason">
                          <el-input v-model="form.failReason" type="textarea" placeholder="请输入内容" />
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
                        <el-form-item label="IP地址" prop="ipAddr">
                          <el-input v-model="form.ipAddr" placeholder="请输入IP地址" />
                        </el-form-item>
                        <el-form-item label="删除" prop="isDelete">
                          <el-radio-group v-model="form.isDelete">
                            <el-radio
                                v-for="dict in common_delete"
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

<script setup name="PaymentOrderInfo">
  import { listPaymentOrderInfo, getPaymentOrderInfo, delPaymentOrderInfo, addPaymentOrderInfo, updatePaymentOrderInfo } from "@/api/points/paymentOrderInfo";

  const { proxy } = getCurrentInstance();
      const { common_delete, po_payment_status, po_order_status, po_order_type } = proxy.useDict('common_delete', 'po_payment_status', 'po_order_status', 'po_order_type');

  const paymentOrderInfoList = ref([]);
  const open = ref(false);
  const loading = ref(true);
  const showSearch = ref(true);
  const ids = ref([]);
  const single = ref(true);
  const multiple = ref(true);
  const total = ref(0);
  const title = ref("");
          const daterangePaymentTime = ref([]);
          const daterangeCreateTime = ref([]);
          const daterangeUpdateTime = ref([]);

  const data = reactive({
    form: {},
    queryParams: {
      pageNum: 1,
      pageSize: 10,
                    orderId: null,
                    userId: null,
                    orderType: null,
                    orderStatus: null,
                    amount: null,
                    paymentId: null,
                    thirdParty: null,
                    thirdPartyOrder: null,
                    paymentTime: null,
                    paymentStatus: null,
                    createTime: null,
                    updateTime: null,
                    deviceId: null,
                    browser: null,
                    os: null,
                    platform: null,
                    ipAddr: null,
                    isDelete: null,
    },
    rules: {
                    userId: [
                { required: true, message: "用户编号不能为空", trigger: "blur" }
              ],
                    orderType: [
                { required: true, message: "订单类型不能为空", trigger: "change" }
              ],
                    orderStatus: [
                { required: true, message: "订单状态不能为空", trigger: "change" }
              ],
                    amount: [
                { required: true, message: "订单总金额不能为空", trigger: "blur" }
              ],
                    paymentId: [
                { required: true, message: "支付方式编号不能为空", trigger: "blur" }
              ],
                    thirdParty: [
                { required: true, message: "第三方支付平台不能为空", trigger: "blur" }
              ],
                    paymentStatus: [
                { required: true, message: "支付状态不能为空", trigger: "change" }
              ],
                    createTime: [
                { required: true, message: "创建时间不能为空", trigger: "blur" }
              ],
                    ipAddr: [
                { required: true, message: "IP地址不能为空", trigger: "blur" }
              ],
                    isDelete: [
                { required: true, message: "删除不能为空", trigger: "change" }
              ],
    },
    //表格展示列
    columns: [
              { key: 0, label: '订单编号', visible: true },
                { key: 1, label: '用户编号', visible: true },
                { key: 2, label: '订单类型', visible: true },
                { key: 3, label: '订单状态', visible: true },
                { key: 4, label: '订单总金额', visible: true },
                { key: 5, label: '支付方式编号', visible: true },
                { key: 6, label: '第三方支付平台', visible: true },
                { key: 7, label: '第三方支付平台订单号', visible: true },
                { key: 8, label: '支付时间', visible: true },
                { key: 9, label: '支付状态', visible: true },
                { key: 10, label: '支付手续费', visible: true },
                { key: 11, label: '创建时间', visible: true },
                { key: 12, label: '更新时间', visible: true },
                { key: 13, label: '支付失败原因', visible: true },
                { key: 14, label: '设备唯一标识', visible: true },
                { key: 15, label: '浏览器类型', visible: true },
                { key: 16, label: '操作系统', visible: true },
                { key: 17, label: '平台', visible: true },
                { key: 18, label: 'IP地址', visible: true },
                { key: 19, label: '删除', visible: true },
                { key: 20, label: '备注', visible: true },
      ],
  });

  const { queryParams, form, rules,columns } = toRefs(data);

  /** 查询支付订单列表 */
  function getList() {
    loading.value = true;
            queryParams.value.params = {};
            if (null != daterangePaymentTime && '' != daterangePaymentTime) {
              queryParams.value.params["beginPaymentTime"] = daterangePaymentTime.value[0];
              queryParams.value.params["endPaymentTime"] = daterangePaymentTime.value[1];
            }
            if (null != daterangeCreateTime && '' != daterangeCreateTime) {
              queryParams.value.params["beginCreateTime"] = daterangeCreateTime.value[0];
              queryParams.value.params["endCreateTime"] = daterangeCreateTime.value[1];
            }
            if (null != daterangeUpdateTime && '' != daterangeUpdateTime) {
              queryParams.value.params["beginUpdateTime"] = daterangeUpdateTime.value[0];
              queryParams.value.params["endUpdateTime"] = daterangeUpdateTime.value[1];
            }
    listPaymentOrderInfo(queryParams.value).then(response => {
            paymentOrderInfoList.value = response.rows;
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
                    orderId: null,
                    userId: null,
                    orderType: null,
                    orderStatus: null,
                    amount: null,
                    paymentId: null,
                    thirdParty: null,
                    thirdPartyOrder: null,
                    paymentTime: null,
                    paymentStatus: null,
                    transactionFee: null,
                    createTime: null,
                    updateTime: null,
                    failReason: null,
                    deviceId: null,
                    browser: null,
                    os: null,
                    platform: null,
                    ipAddr: null,
                    isDelete: null,
                    remark: null
    };
    proxy.resetForm("paymentOrderInfoRef");
  }

  /** 搜索按钮操作 */
  function handleQuery() {
    queryParams.value.pageNum = 1;
    getList();
  }

  /** 重置按钮操作 */
  function resetQuery() {
            daterangePaymentTime.value = [];
            daterangeCreateTime.value = [];
            daterangeUpdateTime.value = [];
    proxy.resetForm("queryRef");
    handleQuery();
  }

  // 多选框选中数据
  function handleSelectionChange(selection) {
    ids.value = selection.map(item => item.orderId);
    single.value = selection.length != 1;
    multiple.value = !selection.length;
  }

  /** 新增按钮操作 */
  function handleAdd() {
    reset();
    open.value = true;
    title.value = "添加支付订单";
  }

  /** 修改按钮操作 */
  function handleUpdate(row) {
    reset();
    const _orderId = row.orderId || ids.value
    getPaymentOrderInfo(_orderId).then(response => {
      form.value = response.data;
      open.value = true;
      title.value = "修改支付订单";
    });
  }

  /** 提交按钮 */
  function submitForm() {
    proxy.$refs["paymentOrderInfoRef"].validate(valid => {
      if (valid) {
        if (form.value.orderId != null) {
          updatePaymentOrderInfo(form.value).then(response => {
            proxy.$modal.msgSuccess("修改成功");
            open.value = false;
            getList();
          });
        } else {
          addPaymentOrderInfo(form.value).then(response => {
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
    const _orderIds = row.orderId || ids.value;
    proxy.$modal.confirm('是否确认删除支付订单编号为"' + _orderIds + '"的数据项？').then(function() {
      return delPaymentOrderInfo(_orderIds);
    }).then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    }).catch(() => {});
  }

  /** 导出按钮操作 */
  function handleExport() {
    proxy.download('points/paymentOrderInfo/export', {
      ...queryParams.value
    }, `paymentOrderInfo_${new Date().getTime()}.xlsx`)
  }

  getList();
</script>
