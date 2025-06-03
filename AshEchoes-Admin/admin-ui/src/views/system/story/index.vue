<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择类型" clearable style="width: 150px">
          <el-option
              v-for="dict in story_type"
              :key="dict.value"
              :label="dict.value"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="所属世界" prop="world">
        <el-select v-model="queryParams.world" placeholder="请选择所属世界" clearable style="width: 150px">
          <el-option
              v-for="dict in ae_world"
              :key="dict.value"
              :label="dict.value"
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
            v-hasPermi="['system:story:add']"
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
            v-hasPermi="['system:story:edit']"
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
            v-hasPermi="['system:story:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['system:story:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading"
              :data="storyList"
              row-key="id"
              :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
    >
      <el-table-column label="排序" align="center" prop="sort"/>
      <el-table-column label="故事" align="center" prop="title"/>
      <el-table-column label="封面" align="center" prop="banner" width="150">
        <template #default="scope">
          <image-preview :src="scope.row.banner" :width="100" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="类型" align="center" prop="type">
        <template #default="scope">
          <dict-tag :options="story_type" :value="scope.row.type"/>
        </template>
      </el-table-column>
      <el-table-column label="同调者" align="center" prop="chars"/>
      <el-table-column label="故事时间" align="center" prop="sTime"/>
      <el-table-column label="所属世界" align="center" prop="world">
        <template #default="scope">
          <dict-tag :options="ae_world" :value="scope.row.world"/>
        </template>
      </el-table-column>
      <el-table-column label="整理者" align="center" prop="author"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Plus" @click="handleAddChild(scope.row)"
                     v-hasPermi="['system:story:edit']">子章节
          </el-button>
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['system:story:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['system:story:remove']">删除
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

    <!-- 添加或修改故事线对话框 -->
    <el-dialog fullscreen :title="title" v-model="open" append-to-body>
      <el-form ref="storyRef" :model="form" :rules="rules" label-width="80px">
        <el-row :gutter="20">
          <el-col :xs="24" :sm="24" :md="12" :lg="8">
            <el-form-item label="梳理">
              <Toolbar
                  v-if="open"
                  style="border-bottom: 1px solid #ccc"
                  :editor="editorRef"
                  :defaultConfig="toolbarConfig"
                  mode="default"
              />
              <Editor
                  v-if="open"
                  :style="{'height':windowHeight()+'px','width':'100%'}"
                  v-model="form.otext"
                  :defaultConfig="editorConfig"
                  mode="default"
                  @onCreated="handleCreated"
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="12" :lg="8">
            <el-form-item label="类型" prop="type">
              <el-radio-group v-model="form.type">
                <el-radio
                    v-for="dict in story_type"
                    :key="dict.value"
                    :label="dict.value"
                >{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="所属世界" prop="world">
              <el-checkbox-group v-model="form.world">
                <el-checkbox
                    v-for="dict in ae_world"
                    :key="dict.value"
                    :label="dict.value">
                  {{ dict.label }}
                </el-checkbox>
              </el-checkbox-group>
            </el-form-item>
            <el-form-item label="标题" prop="title">
              <el-input v-model="form.title" placeholder="标题,角色故事就填写角色名"/>
            </el-form-item>
            <el-form-item label="森罗时间" prop="sTime">
              <el-input v-model="form.sTime" placeholder="请输入故事时间"/>
            </el-form-item>
            <el-form-item label="人物" prop="chars">
              <el-input v-model="form.chars" type="textarea" placeholder="该故事下出场的角色，多个角色使用英文逗号隔开"/>
            </el-form-item>
            <el-form-item label="整理者" prop="author">
              <el-tooltip
                  class="box-item"
                  effect="dark"
                  content="如果对本条故事线有修改，建议加上修改者自己的昵称。用英文逗号隔开即可"
                  placement="top-start"
              >
                <el-input v-model="form.author" placeholder="当前故事的整理者，如果多个请用逗号隔开"/>
              </el-tooltip>
            </el-form-item>
            <el-form-item label="排序" prop="sort">
              <el-input v-model="form.sort" placeholder="信息展示按照顺序展示"/>
            </el-form-item>
            <el-form-item label="封面" prop="banner">
              <image-upload v-model="form.banner"/>
            </el-form-item>
            <el-form-item label="引言">
              <Toolbar
                  v-if="open"
                  style="border-bottom: 1px solid #ccc"
                  :editor="editorRef2"
                  :defaultConfig="toolbarConfig"
                  mode="default"
              />
              <Editor
                  v-if="open"
                  :style="{'height':'300px','width':'100%'}"
                  v-model="form.desc"
                  :defaultConfig="editorConfig"
                  mode="default"
                  @onCreated="handleCreated2"
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="12" :lg="8">
            <el-form-item label="概览">
              <Toolbar
                  v-if="open"
                  style="border-bottom: 1px solid #ccc"
                  :editor="editorRef1"
                  :defaultConfig="toolbarConfig"
                  mode="default"
              />
              <Editor
                  v-if="open"
                  :style="{'height':windowHeight()+'px','width':'100%'}"
                  v-model="form.summary"
                  :defaultConfig="editorConfig"
                  mode="default"
                  @onCreated="handleCreated1"
              />
            </el-form-item>
          </el-col>
        </el-row>

      </el-form>
      <template #footer>
        <div class="dialog-footer" style="position: absolute;bottom: 10px;right: 10px">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Story">

import '@wangeditor/editor/dist/css/style.css' // 引入 css

import {onBeforeUnmount, ref, shallowRef, onMounted} from 'vue'
import {Editor, Toolbar} from '@wangeditor/editor-for-vue'

const commonConfig = {
  server: import.meta.env.VITE_APP_BASE_API + "/common/upload",
  fieldName: 'file',
  headers: {
    Authorization: "Bearer " + getToken()
  },
  customInsert(res, insertFn) {
    insertFn(res.fileName)
  }
}
// 编辑器实例，必须用 shallowRef
const editorRef = shallowRef()
const editorRef1 = shallowRef()
const editorRef2 = shallowRef()

const toolbarConfig = {}
const editorConfig = {
  MENU_CONF: {},
  placeholder: "请在这里输入"
}
editorConfig.MENU_CONF['uploadImage'] = {
  ...commonConfig
}
editorConfig.MENU_CONF['uploadVideo'] = {
  ...commonConfig
}
// 组件销毁时，也及时销毁编辑器
onBeforeUnmount(() => {
  destoryEditor()
})

function destoryEditor() {
  const editor = editorRef.value
  const editor1 = editorRef1.value
  const editor2 = editorRef2.value
  if (editor != null)
    editor.destroy()
  if (editor1 != null)
    editor1.destroy()
  if (editor2 != null)
    editor2.destroy()
}

const handleCreated = (editor) => {
  editorRef.value = editor // 记录 editor 实例，重要！
}
const handleCreated1 = (editor) => {
  editorRef1.value = editor // 记录 editor 实例，重要！
}
const handleCreated2 = (editor) => {
  editorRef2.value = editor // 记录 editor 实例，重要！
}

import {listStory, getStory, delStory, addStory, updateStory} from "@/api/system/story";
import {getToken} from "@/utils/auth.js";

const {proxy} = getCurrentInstance();
const {story_type, ae_world} = proxy.useDict('story_type', 'ae_world');
const storyList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");

function windowHeight() {
  return window.innerHeight - 300
}

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 99999,
    sort: null,
    title: null,
    type: null,
    chars: null,
    sTime: null,
    banner: null,
    world: null,
    otext: null,
    author: null,
    desc: null,
    summary: null
  },
  rules: {
    sort: [
      {required: true, message: "排序不能为空", trigger: "blur"}
    ],
    title: [
      {required: true, message: "故事标题不能为空", trigger: "blur"}
    ],
    type: [
      {required: true, message: "类型不能为空", trigger: "change"}
    ],
    sTime: [
      {required: true, message: "故事时间不能为空", trigger: "blur"}
    ],
    world: [
      {required: true, message: "所属世界不能为空", trigger: "change"}
    ],
  }
});

const {queryParams, form, rules} = toRefs(data);

/** 查询故事线列表 */
function getList() {
  loading.value = true;
  listStory(queryParams.value).then(response => {
    storyList.value = proxy.handleTree(response.rows, "id");
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
    parentId: null,
    sort: null,
    title: null,
    banner: null,
    type: null,
    chars: null,
    sTime: null,
    world: [],
    author: null,
    desc: null,
    otext: null,
    summary: null
  };
  proxy.resetForm("storyRef");
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

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加故事线";
}

/** 新增按钮操作 */
function handleAddChild(row) {
  reset();
  const _id = row.id || ids.value
  getStory(_id).then(response => {
    form.value.parentId = response.data.id;
    form.value.type = response.data.type;
    form.value.sort = response.data.sort;
    form.value.world = response.data.world.split(",");
    open.value = true;
    title.value = "添加故事线子节点";
  });
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getStory(_id).then(response => {
    form.value = response.data;
    form.value.world = form.value.world.split(",");
    open.value = true;
    title.value = "修改故事线";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["storyRef"].validate(valid => {
    if (valid) {
      form.value.world = form.value.world.join(",");
      if (form.value.id != null) {
        updateStory(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addStory(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除故事线编号为"' + _ids + '"的数据项？').then(function () {
    return delStory(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('system/story/export', {
    ...queryParams.value
  }, `story_${new Date().getTime()}.xlsx`)
}

getList();
</script>
