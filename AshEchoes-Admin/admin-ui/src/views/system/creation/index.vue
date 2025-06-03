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
      <el-form-item label="下一章的故事ID" prop="nextScId">
        <el-input
          v-model="queryParams.nextScId"
          placeholder="请输入下一章的故事ID"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="上一章的故事ID" prop="lastScId">
        <el-input
          v-model="queryParams.lastScId"
          placeholder="请输入上一章的故事ID"
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
          v-hasPermi="['system:creation:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:creation:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:creation:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['system:creation:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="creationList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="${comment}" align="center" prop="id" />
      <el-table-column label="所属故事" align="center" prop="storyId" />
      <el-table-column label="下一章的故事ID" align="center" prop="nextScId" />
      <el-table-column label="上一章的故事ID" align="center" prop="lastScId" />
      <el-table-column label="内容" align="center" prop="content" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:creation:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['system:creation:remove']">删除</el-button>
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

    <!-- 添加或修改故事二创对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="creationRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="所属故事" prop="storyId">
          <el-input v-model="form.storyId" placeholder="请输入所属故事" />
        </el-form-item>
        <el-form-item label="下一章的故事ID" prop="nextScId">
          <el-input v-model="form.nextScId" placeholder="请输入下一章的故事ID" />
        </el-form-item>
        <el-form-item label="上一章的故事ID" prop="lastScId">
          <el-input v-model="form.lastScId" placeholder="请输入上一章的故事ID" />
        </el-form-item>
        <el-form-item label="内容">
          <editor v-model="form.content" :min-height="192"/>
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

<script setup name="Creation">
import { listCreation, getCreation, delCreation, addCreation, updateCreation } from "@/api/system/creation";

const { proxy } = getCurrentInstance();

const creationList = ref([]);
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
    nextScId: null,
    lastScId: null,
    content: null
  },
  rules: {
    storyId: [
      { required: true, message: "所属故事不能为空", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询故事二创列表 */
function getList() {
  loading.value = true;
  listCreation(queryParams.value).then(response => {
    creationList.value = response.rows;
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
    nextScId: null,
    lastScId: null,
    content: null
  };
  proxy.resetForm("creationRef");
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
  title.value = "添加故事二创";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getCreation(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改故事二创";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["creationRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateCreation(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addCreation(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除故事二创编号为"' + _ids + '"的数据项？').then(function() {
    return delCreation(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('system/creation/export', {
    ...queryParams.value
  }, `creation_${new Date().getTime()}.xlsx`)
}

getList();
</script>
