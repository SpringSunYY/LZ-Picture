<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
                  <el-form-item label="提现订单编号" prop="withdrawalId">
                    <el-input
                        v-model="queryParams.withdrawalId"
                        placeholder="请输入提现订单编号"
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
                  <el-form-item label="提现方式" prop="withdrawalMethod">
                    <el-input
                        v-model="queryParams.withdrawalMethod"
                        placeholder="请输入提现方式"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                  </el-form-item>
                  <el-form-item label="提现账户" prop="withdrawalAccount">
                    <el-input
                        v-model="queryParams.withdrawalAccount"
                        placeholder="请输入提现账户"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                  </el-form-item>
                  <el-form-item label="提现状态" prop="withdrawalStatus">
                    <el-select v-model="queryParams.withdrawalStatus" style="width: 200px" placeholder="请选择提现状态" clearable>
                      <el-option
                          v-for="dict in po_withdrawal_status"
                          :key="dict.value"
                          :label="dict.label"
                          :value="dict.value"
                      />
                    </el-select>
                  </el-form-item>
                  <el-form-item label="提现平台订单号" prop="withdrawalPlatformOrder">
                    <el-input
                        v-model="queryParams.withdrawalPlatformOrder"
                        placeholder="请输入提现平台订单号"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                  </el-form-item>
                  <el-form-item label="交易编号" prop="transactionId">
                    <el-input
                        v-model="queryParams.transactionId"
                        placeholder="请输入交易编号"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                  </el-form-item>
                  <el-form-item label="审核状态" prop="reviewStatus">
                    <el-select v-model="queryParams.reviewStatus" style="width: 200px" placeholder="请选择审核状态" clearable>
                      <el-option
                          v-for="dict in po_approval_status"
                          :key="dict.value"
                          :label="dict.label"
                          :value="dict.value"
                      />
                    </el-select>
                  </el-form-item>
                  <el-form-item label="审核时间" style="width: 308px">
                    <el-date-picker
                        v-model="daterangeReviewTime"
                        value-format="YYYY-MM-DD"
                        type="daterange"
                        range-separator="-"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                    ></el-date-picker>
                  </el-form-item>
                  <el-form-item label="审核人编号" prop="reviewUserId">
                    <el-input
                        v-model="queryParams.reviewUserId"
                        placeholder="请输入审核人编号"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                  </el-form-item>
                  <el-form-item label="完成时间" style="width: 308px">
                    <el-date-picker
                        v-model="daterangeAccomplishTime"
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
            v-hasPermi="['points:withdrawalOrderInfo:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="success"
            plain
            icon="Edit"
            :disabled="single"
            @click="handleUpdate"
            v-hasPermi="['points:withdrawalOrderInfo:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="danger"
            plain
            icon="Delete"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['points:withdrawalOrderInfo:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['points:withdrawalOrderInfo:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="withdrawalOrderInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
              <el-table-column label="提现订单编号" align="center" prop="withdrawalId" v-if="columns[0].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="用户编号" align="center" prop="userId" v-if="columns[1].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="提现积分" align="center" prop="pointsWithdrawal" v-if="columns[2].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="提现金额" align="center" prop="amountWithdrawal" v-if="columns[3].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="平台抽成金额" align="center" prop="platformFee" v-if="columns[4].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="用户实际收到金额" align="center" prop="userReceivedAmount" v-if="columns[5].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="提现方式" align="center" prop="withdrawalMethod" v-if="columns[6].visible">
                <template #default="scope">
                      <dict-tag :options="po_withdrawal_method" :value="scope.row.withdrawalMethod"/>
                </template>
              </el-table-column>
                <el-table-column label="提现账户" align="center" prop="withdrawalAccount" v-if="columns[7].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="提现状态" align="center" prop="withdrawalStatus" v-if="columns[8].visible">
                <template #default="scope">
                      <dict-tag :options="po_withdrawal_status" :value="scope.row.withdrawalStatus"/>
                </template>
              </el-table-column>
                <el-table-column label="提现平台订单号" align="center" prop="withdrawalPlatformOrder" v-if="columns[9].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="交易编号" align="center" prop="transactionId" v-if="columns[10].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="审核状态" align="center" prop="reviewStatus" v-if="columns[11].visible">
                <template #default="scope">
                      <dict-tag :options="po_approval_status" :value="scope.row.reviewStatus"/>
                </template>
              </el-table-column>
                <el-table-column label="审核时间" align="center" prop="reviewTime" width="180" v-if="columns[12].visible" :show-overflow-tooltip="true">
                <template #default="scope">
                  <span>{{ parseTime(scope.row.reviewTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
                </template>
              </el-table-column>
                <el-table-column label="审核人编号" align="center" prop="reviewUserId" v-if="columns[13].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="审核建议" align="center" prop="reviewRemark" v-if="columns[14].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="完成时间" align="center" prop="accomplishTime" width="180" v-if="columns[15].visible" :show-overflow-tooltip="true">
                <template #default="scope">
                  <span>{{ parseTime(scope.row.accomplishTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
                </template>
              </el-table-column>
                <el-table-column label="设备唯一标识" align="center" prop="deviceId" v-if="columns[16].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="浏览器类型" align="center" prop="browser" v-if="columns[17].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="操作系统" align="center" prop="os" v-if="columns[18].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="平台" align="center" prop="platform" v-if="columns[19].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="IP地址" align="center" prop="ipAddr" v-if="columns[20].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="创建时间" align="center" prop="createTime" width="180" v-if="columns[21].visible" :show-overflow-tooltip="true">
                <template #default="scope">
                  <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
                </template>
              </el-table-column>
                <el-table-column label="更新时间" align="center" prop="updateTime" width="180" v-if="columns[22].visible" :show-overflow-tooltip="true">
                <template #default="scope">
                  <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
                </template>
              </el-table-column>
                <el-table-column label="提现失败原因" align="center" prop="failReason" v-if="columns[23].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="删除" align="center" prop="isDelete" v-if="columns[24].visible">
                <template #default="scope">
                      <dict-tag :options="common_delete" :value="scope.row.isDelete"/>
                </template>
              </el-table-column>
                <el-table-column label="备注" align="center" prop="remark" v-if="columns[25].visible" :show-overflow-tooltip="true"/>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['points:withdrawalOrderInfo:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['points:withdrawalOrderInfo:remove']">删除</el-button>
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

    <!-- 添加或修改用户提现记录对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="withdrawalOrderInfoRef" :model="form" :rules="rules" label-width="80px">
                        <el-form-item label="用户编号" prop="userId">
                          <el-input v-model="form.userId" placeholder="请输入用户编号" />
                        </el-form-item>
                        <el-form-item label="提现积分" prop="pointsWithdrawal">
                          <el-input v-model="form.pointsWithdrawal" placeholder="请输入提现积分" />
                        </el-form-item>
                        <el-form-item label="提现金额" prop="amountWithdrawal">
                          <el-input v-model="form.amountWithdrawal" placeholder="请输入提现金额" />
                        </el-form-item>
                        <el-form-item label="平台抽成金额" prop="platformFee">
                          <el-input v-model="form.platformFee" placeholder="请输入平台抽成金额" />
                        </el-form-item>
                        <el-form-item label="用户实际收到金额" prop="userReceivedAmount">
                          <el-input v-model="form.userReceivedAmount" placeholder="请输入用户实际收到金额" />
                        </el-form-item>
                        <el-form-item label="提现方式" prop="withdrawalMethod">
                          <el-input v-model="form.withdrawalMethod" placeholder="请输入提现方式" />
                        </el-form-item>
                        <el-form-item label="提现账户" prop="withdrawalAccount">
                          <el-input v-model="form.withdrawalAccount" placeholder="请输入提现账户" />
                        </el-form-item>
                        <el-form-item label="提现状态" prop="withdrawalStatus">
                          <el-radio-group v-model="form.withdrawalStatus">
                            <el-radio
                                v-for="dict in po_withdrawal_status"
                                :key="dict.value"
                                :value="dict.value"
                            >{{dict.label}}</el-radio>
                          </el-radio-group>
                        </el-form-item>
                        <el-form-item label="提现平台订单号" prop="withdrawalPlatformOrder">
                          <el-input v-model="form.withdrawalPlatformOrder" placeholder="请输入提现平台订单号" />
                        </el-form-item>
                        <el-form-item label="交易编号" prop="transactionId">
                          <el-input v-model="form.transactionId" placeholder="请输入交易编号" />
                        </el-form-item>
                        <el-form-item label="审核状态" prop="reviewStatus">
                          <el-radio-group v-model="form.reviewStatus">
                            <el-radio
                                v-for="dict in po_approval_status"
                                :key="dict.value"
                                :value="dict.value"
                            >{{dict.label}}</el-radio>
                          </el-radio-group>
                        </el-form-item>
                        <el-form-item label="审核时间" prop="reviewTime">
                          <el-date-picker clearable
                                          v-model="form.reviewTime"
                                          type="date"
                                          value-format="YYYY-MM-DD"
                                          placeholder="请选择审核时间">
                          </el-date-picker>
                        </el-form-item>
                        <el-form-item label="审核人编号" prop="reviewUserId">
                          <el-input v-model="form.reviewUserId" placeholder="请输入审核人编号" />
                        </el-form-item>
                        <el-form-item label="审核建议" prop="reviewRemark">
                          <el-input v-model="form.reviewRemark" type="textarea" placeholder="请输入内容" />
                        </el-form-item>
                        <el-form-item label="完成时间" prop="accomplishTime">
                          <el-date-picker clearable
                                          v-model="form.accomplishTime"
                                          type="date"
                                          value-format="YYYY-MM-DD"
                                          placeholder="请选择完成时间">
                          </el-date-picker>
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
                        <el-form-item label="提现失败原因" prop="failReason">
                          <el-input v-model="form.failReason" type="textarea" placeholder="请输入内容" />
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

<script setup name="WithdrawalOrderInfo">
  import { listWithdrawalOrderInfo, getWithdrawalOrderInfo, delWithdrawalOrderInfo, addWithdrawalOrderInfo, updateWithdrawalOrderInfo } from "@/api/points/withdrawalOrderInfo";

  const { proxy } = getCurrentInstance();
      const { common_delete, po_approval_status, po_withdrawal_status } = proxy.useDict('common_delete', 'po_approval_status', 'po_withdrawal_status');

  const withdrawalOrderInfoList = ref([]);
  const open = ref(false);
  const loading = ref(true);
  const showSearch = ref(true);
  const ids = ref([]);
  const single = ref(true);
  const multiple = ref(true);
  const total = ref(0);
  const title = ref("");
          const daterangeReviewTime = ref([]);
          const daterangeAccomplishTime = ref([]);
          const daterangeCreateTime = ref([]);
          const daterangeUpdateTime = ref([]);

  const data = reactive({
    form: {},
    queryParams: {
      pageNum: 1,
      pageSize: 10,
                    withdrawalId: null,
                    userId: null,
                    withdrawalMethod: null,
                    withdrawalAccount: null,
                    withdrawalStatus: null,
                    withdrawalPlatformOrder: null,
                    transactionId: null,
                    reviewStatus: null,
                    reviewTime: null,
                    reviewUserId: null,
                    accomplishTime: null,
                    deviceId: null,
                    browser: null,
                    os: null,
                    platform: null,
                    ipAddr: null,
                    createTime: null,
                    updateTime: null,
                    isDelete: null,
    },
    rules: {
                    pointsWithdrawal: [
                { required: true, message: "提现积分不能为空", trigger: "blur" }
              ],
                    amountWithdrawal: [
                { required: true, message: "提现金额不能为空", trigger: "blur" }
              ],
                    platformFee: [
                { required: true, message: "平台抽成金额不能为空", trigger: "blur" }
              ],
                    userReceivedAmount: [
                { required: true, message: "用户实际收到金额不能为空", trigger: "blur" }
              ],
                    withdrawalMethod: [
                { required: true, message: "提现方式不能为空", trigger: "blur" }
              ],
                    withdrawalAccount: [
                { required: true, message: "提现账户不能为空", trigger: "blur" }
              ],
                    withdrawalStatus: [
                { required: true, message: "提现状态不能为空", trigger: "change" }
              ],
                    reviewStatus: [
                { required: true, message: "审核状态不能为空", trigger: "change" }
              ],
                    ipAddr: [
                { required: true, message: "IP地址不能为空", trigger: "blur" }
              ],
                    createTime: [
                { required: true, message: "创建时间不能为空", trigger: "blur" }
              ],
                    updateTime: [
                { required: true, message: "更新时间不能为空", trigger: "blur" }
              ],
                    isDelete: [
                { required: true, message: "删除不能为空", trigger: "change" }
              ],
    },
    //表格展示列
    columns: [
              { key: 0, label: '提现订单编号', visible: true },
                { key: 1, label: '用户编号', visible: true },
                { key: 2, label: '提现积分', visible: true },
                { key: 3, label: '提现金额', visible: true },
                { key: 4, label: '平台抽成金额', visible: true },
                { key: 5, label: '用户实际收到金额', visible: true },
                { key: 6, label: '提现方式', visible: true },
                { key: 7, label: '提现账户', visible: true },
                { key: 8, label: '提现状态', visible: true },
                { key: 9, label: '提现平台订单号', visible: true },
                { key: 10, label: '交易编号', visible: true },
                { key: 11, label: '审核状态', visible: true },
                { key: 12, label: '审核时间', visible: true },
                { key: 13, label: '审核人编号', visible: true },
                { key: 14, label: '审核建议', visible: true },
                { key: 15, label: '完成时间', visible: true },
                { key: 16, label: '设备唯一标识', visible: true },
                { key: 17, label: '浏览器类型', visible: true },
                { key: 18, label: '操作系统', visible: true },
                { key: 19, label: '平台', visible: true },
                { key: 20, label: 'IP地址', visible: true },
                { key: 21, label: '创建时间', visible: true },
                { key: 22, label: '更新时间', visible: true },
                { key: 23, label: '提现失败原因', visible: true },
                { key: 24, label: '删除', visible: true },
                { key: 25, label: '备注', visible: true },
      ],
  });

  const { queryParams, form, rules,columns } = toRefs(data);

  /** 查询用户提现记录列表 */
  function getList() {
    loading.value = true;
            queryParams.value.params = {};
            if (null != daterangeReviewTime && '' != daterangeReviewTime) {
              queryParams.value.params["beginReviewTime"] = daterangeReviewTime.value[0];
              queryParams.value.params["endReviewTime"] = daterangeReviewTime.value[1];
            }
            if (null != daterangeAccomplishTime && '' != daterangeAccomplishTime) {
              queryParams.value.params["beginAccomplishTime"] = daterangeAccomplishTime.value[0];
              queryParams.value.params["endAccomplishTime"] = daterangeAccomplishTime.value[1];
            }
            if (null != daterangeCreateTime && '' != daterangeCreateTime) {
              queryParams.value.params["beginCreateTime"] = daterangeCreateTime.value[0];
              queryParams.value.params["endCreateTime"] = daterangeCreateTime.value[1];
            }
            if (null != daterangeUpdateTime && '' != daterangeUpdateTime) {
              queryParams.value.params["beginUpdateTime"] = daterangeUpdateTime.value[0];
              queryParams.value.params["endUpdateTime"] = daterangeUpdateTime.value[1];
            }
    listWithdrawalOrderInfo(queryParams.value).then(response => {
            withdrawalOrderInfoList.value = response.rows;
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
                    withdrawalId: null,
                    userId: null,
                    pointsWithdrawal: null,
                    amountWithdrawal: null,
                    platformFee: null,
                    userReceivedAmount: null,
                    withdrawalMethod: null,
                    withdrawalAccount: null,
                    withdrawalStatus: null,
                    withdrawalPlatformOrder: null,
                    transactionId: null,
                    reviewStatus: null,
                    reviewTime: null,
                    reviewUserId: null,
                    reviewRemark: null,
                    accomplishTime: null,
                    deviceId: null,
                    browser: null,
                    os: null,
                    platform: null,
                    ipAddr: null,
                    createTime: null,
                    updateTime: null,
                    failReason: null,
                    isDelete: null,
                    remark: null
    };
    proxy.resetForm("withdrawalOrderInfoRef");
  }

  /** 搜索按钮操作 */
  function handleQuery() {
    queryParams.value.pageNum = 1;
    getList();
  }

  /** 重置按钮操作 */
  function resetQuery() {
            daterangeReviewTime.value = [];
            daterangeAccomplishTime.value = [];
            daterangeCreateTime.value = [];
            daterangeUpdateTime.value = [];
    proxy.resetForm("queryRef");
    handleQuery();
  }

  // 多选框选中数据
  function handleSelectionChange(selection) {
    ids.value = selection.map(item => item.withdrawalId);
    single.value = selection.length != 1;
    multiple.value = !selection.length;
  }

  /** 新增按钮操作 */
  function handleAdd() {
    reset();
    open.value = true;
    title.value = "添加用户提现记录";
  }

  /** 修改按钮操作 */
  function handleUpdate(row) {
    reset();
    const _withdrawalId = row.withdrawalId || ids.value
    getWithdrawalOrderInfo(_withdrawalId).then(response => {
      form.value = response.data;
      open.value = true;
      title.value = "修改用户提现记录";
    });
  }

  /** 提交按钮 */
  function submitForm() {
    proxy.$refs["withdrawalOrderInfoRef"].validate(valid => {
      if (valid) {
        if (form.value.withdrawalId != null) {
          updateWithdrawalOrderInfo(form.value).then(response => {
            proxy.$modal.msgSuccess("修改成功");
            open.value = false;
            getList();
          });
        } else {
          addWithdrawalOrderInfo(form.value).then(response => {
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
    const _withdrawalIds = row.withdrawalId || ids.value;
    proxy.$modal.confirm('是否确认删除用户提现记录编号为"' + _withdrawalIds + '"的数据项？').then(function() {
      return delWithdrawalOrderInfo(_withdrawalIds);
    }).then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    }).catch(() => {});
  }

  /** 导出按钮操作 */
  function handleExport() {
    proxy.download('points/withdrawalOrderInfo/export', {
      ...queryParams.value
    }, `withdrawalOrderInfo_${new Date().getTime()}.xlsx`)
  }

  getList();
</script>
