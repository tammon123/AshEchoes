<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="所属故事" prop="storyId">
        <el-input
          v-model="queryParams.storyId"
          placeholder="请输入所属故事"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="关键字" prop="keyword">
        <el-input
          v-model="queryParams.keyword"
          placeholder="请输入关键字"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="跳转链接" prop="redirectUrl">
        <el-input
          v-model="queryParams.redirectUrl"
          placeholder="请输入跳转链接"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="banner" prop="bannel">
        <el-input
          v-model="queryParams.bannel"
          placeholder="请输入banner"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="关联ID" prop="connId">
        <el-input
          v-model="queryParams.connId"
          placeholder="请输入关联ID"
          clearable
          @keyup.enter="handleQuery"
        />
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
          v-hasPermi="['system:keyword:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:keyword:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:keyword:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['system:keyword:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="keywordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="${comment}" align="center" prop="id" />
      <el-table-column label="所属故事" align="center" prop="storyId" />
      <el-table-column label="关键字" align="center" prop="keyword" />
      <el-table-column label="跳转链接" align="center" prop="redirectUrl" />
      <el-table-column label="banner" align="center" prop="bannel" />
      <el-table-column label="关联类型" align="center" prop="connType" />
      <el-table-column label="关联ID" align="center" prop="connId" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:keyword:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['system:keyword:remove']">删除</el-button>
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

    <!-- 添加或修改故事关键字对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="keywordRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="所属故事" prop="storyId">
          <el-input v-model="form.storyId" placeholder="请输入所属故事" />
        </el-form-item>
        <el-form-item label="关键字" prop="keyword">
          <el-input v-model="form.keyword" placeholder="请输入关键字" />
        </el-form-item>
        <el-form-item label="跳转链接" prop="redirectUrl">
          <el-input v-model="form.redirectUrl" placeholder="请输入跳转链接" />
        </el-form-item>
        <el-form-item label="banner" prop="bannel">
          <el-input v-model="form.bannel" placeholder="请输入banner" />
        </el-form-item>
        <el-form-item label="关联ID" prop="connId">
          <el-input v-model="form.connId" placeholder="请输入关联ID" />
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

<script setup name="Keyword">
import { listKeyword, getKeyword, delKeyword, addKeyword, updateKeyword } from "@/api/system/keyword";

const { proxy } = getCurrentInstance();

const keywordList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    storyId: null,
    keyword: null,
    redirectUrl: null,
    bannel: null,
    connType: null,
    connId: null
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询故事关键字列表 */
function getList() {
  loading.value = true;
  listKeyword(queryParams.value).then(response => {
    keywordList.value = response.rows;
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
    id: null,
    storyId: null,
    keyword: null,
    redirectUrl: null,
    bannel: null,
    connType: null,
    connId: null
  };
  proxy.resetForm("keywordRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加故事关键字";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getKeyword(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改故事关键字";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["keywordRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateKeyword(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addKeyword(form.value).then(response => {
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
  const _ids = row.id || ids.value;
  proxy.$modal.confirm('是否确认删除故事关键字编号为"' + _ids + '"的数据项？').then(function() {
    return delKeyword(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('system/keyword/export', {
    ...queryParams.value
  }, `keyword_${new Date().getTime()}.xlsx`)
}

getList();
</script>
