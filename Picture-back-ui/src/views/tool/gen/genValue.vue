<template>
  <el-card>
    <el-tabs :tab-position="tabPosition" type="border-card" style="margin-bottom: 30px;min-height: 600px">
      <el-tab-pane
          v-for="(column, index) in columns"
          :key="column?.columnId"
          :label="column?.columnName"
          :v-if="column?.pk"
      >
        <template #label>
                 <span slot="label"><svg-icon v-if="column.required" icon-class="star" style="color: red"/> {{
                     column.columnName
                   }}</span>
        </template>
        <el-form :ref="'form' + index" :model="tableColumnValues[index]" label-width="80px"
                 style="width: 500px; margin: 0 auto;">
          <el-form-item label="字段名称">
            <span>{{ tableColumnValues[index].columnName }}</span>
          </el-form-item>
          <el-form-item label="字段描述">
            <span>{{ tableColumnValues[index].columnComment }}</span>
          </el-form-item>
          <el-form-item label="物理类型">
            <span>{{ tableColumnValues[index].columnType }}</span>
          </el-form-item>
          <el-form-item label="Java类型">
            <span>{{ tableColumnValues[index].javaType }}</span>
          </el-form-item>
          <el-form-item label="Java属性">
            <span>{{ tableColumnValues[index].javaField }}</span>
          </el-form-item>
          <el-form-item label="是否唯一">
            <el-radio-group v-model="tableColumnValues[index].isSole">
              <el-radio :value="0">否</el-radio>
              <el-radio :value="1">是</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item v-if="column.htmlType === 'textarea'"
                        label="生成数值"
                        :rules="[
                          { required: column.required, message: `${column.columnComment} 不能为空`, trigger: 'blur' }
                        ]"
          >
            <el-input v-model="tableColumnValues[index].value" type="textarea" placeholder="请输入生成数值"/>
          </el-form-item>
          <el-form-item v-if="column.htmlType === 'datetime'"
                        label="生成数值"
                        :rules="[
                          { required: column.required, message: `${column.columnComment} 不能为空`, trigger: 'blur' }
                        ]"
          >
            <el-date-picker clearable
                            v-model="tableColumnValues[index].value"
                            type="datetime"
                            value-format="YYYY-MM-MM HH:mm:ss"
                            placeholder="请选择日期">
            </el-date-picker>
          </el-form-item>
          <el-form-item v-if="column.htmlType === 'imageUpload'"
                        label="生成数值"
                        :rules="[
                          { required: column.required, message: `${column.columnComment} 不能为空`, trigger: 'blur' }
                        ]"
          >
            <image-upload v-model="tableColumnValues[index].value"/>
          </el-form-item>
          <el-form-item v-if="column.htmlType === 'fileUpload'"
                        label="生成数值"
                        :rules="[
                          { required: column.required, message: `${column.columnComment} 不能为空`, trigger: 'blur' }
                        ]"
          >
            <file-upload v-model="tableColumnValues[index].value"/>
          </el-form-item>
          <el-form-item v-if="column.htmlType === 'editor'"
                        label="生成数值"
                        :rules="[
                          { required: column.required, message: `${column.columnComment} 不能为空`, trigger: 'blur' }
                        ]"
          >
            <editor v-model="tableColumnValues[index].value"/>
          </el-form-item>
          <el-form-item
              label="生成数值"
              :rules="[
                          { required: column.required, message: `${column.columnComment} 不能为空`, trigger: 'blur' }
                        ]"
          >
            <el-input v-model="tableColumnValues[index].value" placeholder="请输入生成数值"/>
          </el-form-item>
        </el-form>
        <div> {{ tableColumnValues[index] }}</div>
      </el-tab-pane>
    </el-tabs>
    <el-form label-width="1000px" :inline="true" class="demo-form-inline">
      <el-form-item label="生成总数">
        <el-input-number :min="1" v-model="genInfo.genNumbers" placeholder="请输入生成总数"></el-input-number>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm">提交</el-button>
        <el-button @click="close">返回</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>
<script setup name="GenAddValue">
import {ref} from 'vue';
import {genValue, getGenTable} from '@/api/tool/gen';
import {useRoute} from "vue-router";

const {proxy} = getCurrentInstance();
const route = useRoute();
const columns = ref([]);
//生成信息
const genInfo = ref({
  tableName: '',
  genNumbers: 1,
  tableColumnValues: []
});
const tabPosition = ref('top')
//生成字段数据
const tableColumnValues = ref([]);

/** 提交按钮 */
const submitForm = () => {
  genInfo.tableColumnValues = []
  console.log('tableColumnValues:', tableColumnValues.value); // 打印 tableColumnValues 的值
  console.log('genInfo.tableColumnValues:', genInfo.tableColumnValues); // 打印 genInfo.tableColumnValues 的值

  // 校验是否有必须需要值但是为空的
  for (const index in tableColumnValues.value) {
    const value = tableColumnValues.value[index]?.value; // 获取对应的值
    const column = tableColumnValues.value[index]; // 获取对应的列信息
    if (column.required && (!value || value.trim() === '') && !column.pk) {
      // 如果 required 为 true 且值不存在或为空
      proxy.$message.error(`${column.columnComment}不能为空`);
      return; // 直接返回
    }
  }

// 深拷贝 tableColumnValues 到 genInfo.tableColumnValues
  genInfo.value.tableColumnValues = JSON.parse(JSON.stringify(tableColumnValues.value));

  console.log('提交前 genInfo.tableColumnValues:', genInfo.tableColumnValues); // 打印提交前的数据
  genValue(genInfo.value).then((res) => {
    proxy.$message.success('成功');
  });
};

function close() {
  const obj = {path: "/tool/gen"};
  proxy.$tab.closeOpenPage(obj);
}

(
    () => {
      const tableId = route?.params?.tableId;
      console.log(tableId)
      if (tableId) {
        getGenTable(tableId).then(res => {
          columns.value = res.data.rows;
          tableColumnValues.value = columns.value.map(item => ({
            columnName: item?.columnName,
            columnComment: item?.columnComment,
            columnType: item?.columnType,
            pk: item?.pk,
            javaType: item?.javaType,
            javaField: item?.javaField,
            required: item?.required,
            isSole: 0
          }));
          genInfo.value.tableName = res.data.info.tableName;
        });
      }
    }
)
();
</script>
