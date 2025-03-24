<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
                  <el-form-item label="点赞编号" prop="likeId">
                    <el-input
                        v-model="queryParams.likeId"
                        placeholder="请输入点赞编号"
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
                  <el-form-item label="图片编号" prop="pictureId">
                    <el-input
                        v-model="queryParams.pictureId"
                        placeholder="请输入图片编号"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                  </el-form-item>
                  <el-form-item label="图片分类" prop="categoryId">
                    <el-input
                        v-model="queryParams.categoryId"
                        placeholder="请输入图片分类"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                  </el-form-item>
                  <el-form-item label="图片标签" prop="tags">
                    <el-input
                        v-model="queryParams.tags"
                        placeholder="请输入图片标签"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                  </el-form-item>
                  <el-form-item label="点赞时间" style="width: 308px">
                    <el-date-picker
                        v-model="daterangeCreateTime"
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
            v-hasPermi="['picture:pictureLikeInfo:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="success"
            plain
            icon="Edit"
            :disabled="single"
            @click="handleUpdate"
            v-hasPermi="['picture:pictureLikeInfo:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="danger"
            plain
            icon="Delete"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['picture:pictureLikeInfo:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['picture:pictureLikeInfo:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="pictureLikeInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
              <el-table-column label="点赞编号" align="center" prop="likeId" v-if="columns[0].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="用户编号" align="center" prop="userId" v-if="columns[1].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="图片编号" align="center" prop="pictureId" v-if="columns[2].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="图片分类" align="center" prop="categoryId" v-if="columns[3].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="图片标签" align="center" prop="tags" v-if="columns[4].visible" :show-overflow-tooltip="true"/>
                <el-table-column label="封面URL" align="center" prop="targetCover" width="100" v-if="columns[5].visible">
                <template #default="scope">
                  <image-preview :src="scope.row.targetCover" :width="50" :height="50"/>
                </template>
              </el-table-column>
                <el-table-column label="点赞时间" align="center" prop="createTime" width="180" v-if="columns[6].visible" :show-overflow-tooltip="true">
                <template #default="scope">
                  <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
                </template>
              </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['picture:pictureLikeInfo:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['picture:pictureLikeInfo:remove']">删除</el-button>
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

    <!-- 添加或修改图片点赞记录对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="pictureLikeInfoRef" :model="form" :rules="rules" label-width="80px">
                        <el-form-item label="用户编号" prop="userId">
                          <el-input v-model="form.userId" placeholder="请输入用户编号" />
                        </el-form-item>
                        <el-form-item label="图片编号" prop="pictureId">
                          <el-input v-model="form.pictureId" placeholder="请输入图片编号" />
                        </el-form-item>
                        <el-form-item label="图片分类" prop="categoryId">
                          <el-input v-model="form.categoryId" placeholder="请输入图片分类" />
                        </el-form-item>
                        <el-form-item label="图片标签" prop="tags">
                          <el-input v-model="form.tags" placeholder="请输入图片标签" />
                        </el-form-item>
                        <el-form-item label="封面URL" prop="targetCover">
                          <image-upload v-model="form.targetCover"/>
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

<script setup name="PictureLikeInfo">
  import { listPictureLikeInfo, getPictureLikeInfo, delPictureLikeInfo, addPictureLikeInfo, updatePictureLikeInfo } from "@/api/picture/pictureLikeInfo";

  const { proxy } = getCurrentInstance();

  const pictureLikeInfoList = ref([]);
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
                    likeId: null,
                    userId: null,
                    pictureId: null,
                    categoryId: null,
                    tags: null,
                    createTime: null
    },
    rules: {
                    userId: [
                { required: true, message: "用户编号不能为空", trigger: "blur" }
              ],
                    pictureId: [
                { required: true, message: "图片编号不能为空", trigger: "blur" }
              ],
                    categoryId: [
                { required: true, message: "图片分类不能为空", trigger: "blur" }
              ],
                    createTime: [
                { required: true, message: "点赞时间不能为空", trigger: "blur" }
              ]
    },
    //表格展示列
    columns: [
              { key: 0, label: '点赞编号', visible: true },
                { key: 1, label: '用户编号', visible: true },
                { key: 2, label: '图片编号', visible: true },
                { key: 3, label: '图片分类', visible: true },
                { key: 4, label: '图片标签', visible: true },
                { key: 5, label: '封面URL', visible: true },
                { key: 6, label: '点赞时间', visible: true },
      ],
  });

  const { queryParams, form, rules,columns } = toRefs(data);

  /** 查询图片点赞记录列表 */
  function getList() {
    loading.value = true;
            queryParams.value.params = {};
            if (null != daterangeCreateTime && '' != daterangeCreateTime) {
              queryParams.value.params["beginCreateTime"] = daterangeCreateTime.value[0];
              queryParams.value.params["endCreateTime"] = daterangeCreateTime.value[1];
            }
    listPictureLikeInfo(queryParams.value).then(response => {
            pictureLikeInfoList.value = response.rows;
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
                    likeId: null,
                    userId: null,
                    pictureId: null,
                    categoryId: null,
                    tags: null,
                    targetCover: null,
                    createTime: null
    };
    proxy.resetForm("pictureLikeInfoRef");
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
    ids.value = selection.map(item => item.likeId);
    single.value = selection.length != 1;
    multiple.value = !selection.length;
  }

  /** 新增按钮操作 */
  function handleAdd() {
    reset();
    open.value = true;
    title.value = "添加图片点赞记录";
  }

  /** 修改按钮操作 */
  function handleUpdate(row) {
    reset();
    const _likeId = row.likeId || ids.value
    getPictureLikeInfo(_likeId).then(response => {
      form.value = response.data;
      open.value = true;
      title.value = "修改图片点赞记录";
    });
  }

  /** 提交按钮 */
  function submitForm() {
    proxy.$refs["pictureLikeInfoRef"].validate(valid => {
      if (valid) {
        if (form.value.likeId != null) {
          updatePictureLikeInfo(form.value).then(response => {
            proxy.$modal.msgSuccess("修改成功");
            open.value = false;
            getList();
          });
        } else {
          addPictureLikeInfo(form.value).then(response => {
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
    const _likeIds = row.likeId || ids.value;
    proxy.$modal.confirm('是否确认删除图片点赞记录编号为"' + _likeIds + '"的数据项？').then(function() {
      return delPictureLikeInfo(_likeIds);
    }).then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    }).catch(() => {});
  }

  /** 导出按钮操作 */
  function handleExport() {
    proxy.download('picture/pictureLikeInfo/export', {
      ...queryParams.value
    }, `pictureLikeInfo_${new Date().getTime()}.xlsx`)
  }

  getList();
</script>
