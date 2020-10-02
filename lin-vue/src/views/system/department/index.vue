<template>
  <div class="department">
    <div class="toolbar">
      <div class="toolbar-left">
        <el-button type="success" size="small" icon="el-icon-plus" @click="showDialog('add',0)">添加</el-button>
      </div>
      <div class="toolbar-right">
        <div style="width:230px;display: inline-block">
          <el-input
            placeholder="部门名称"
            v-model="departmentParams.searchText"
            size="small"
            clearable>
          </el-input>
        </div>
        <el-button type="primary" size="small" @click="departmentList">查询</el-button>
      </div>
    </div>
    <div class="tablebar">
      <div>
        <el-table
          ref="multipleTable"
          :data="tableData"
          style="width: 100%;margin-bottom: 20px;"
          cell-class-name="cellName"
          header-cell-class-name="headerTable"
          row-key="deptId"
          default-expand-all
          :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
          <el-table-column
            v-for="(item, index) in tableLabel"
            :key="index"
            :prop="item.prop"
            :width="item.width"
            :label="item.label">
            <template slot-scope="scope">
              <div v-if="item.prop==='status'">
                <el-tag v-if="scope.row.delFlag == 1" type="success"
                        size="mini">正常
                </el-tag>
                <el-tag v-else type="danger"
                        size="mini">禁用
                </el-tag>
              </div>

              <span v-else>{{scope.row[item.prop]}}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="item.prop"
            width=""
            label="操作">
            <template slot-scope="scope">
              <el-button type="primary" icon="el-icon-edit" size="mini"
                         @click="showDialog('editor',scope.row)"></el-button>
              <el-button type="success" icon="el-icon-plus" size="mini"
                         @click="showDialog('add',scope.row)"></el-button>
              <el-button type="danger" icon="el-icon-delete" size="mini"
                         @click="deleteItem(scope.row.deptId)"></el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <el-dialog v-el-drag-dialog :modal-append-to-body="false" :visible.sync="dialogInfo.visible"
               :width="dialogInfo.width" :title="dialogInfo.title" @close="close" @dragDialog="handleDrag">
      <component :is="dialogInfo.component" :componentType="dialogInfo.componentType"
                 :name="name"
                 :departmentParams="departmentParams"
                 @cancel="cancel"
                 @submit="submit"></component>
    </el-dialog>


  </div>
</template>

<script>
  import elDragDialog from '@/directive/el-drag-dialog' // base on element-ui
  import departmentForm from '@/views/component/departmentForm/departmentForm.vue'
  import {departmentList, departmentSave, departmentRemove, getgetParent, departmentUpdate} from '@/api/system'

  export default {
    name: "department",
    directives: {elDragDialog},
    components: {
      departmentForm
    },
    data() {
      return {
        flag: '',//标记他是编辑还是添加
        name: '',
        dialogInfo: {
          component: '',
          title: '',
          visible: false,
          width: '',
          componentType: ''
        },
        tableLabel: [
          // {label: '编号', width: '200', prop: 'deptId'},
          {label: '部门名称', width: '', prop: 'name'},
          {label: '排序', width: '', prop: 'orderNum'},
          {label: '状态', width: '', prop: 'status'},
        ],
        tableData: [],
        currentPage4: 1,
        departmentParams: {
          delFlag: '',//是否已删除-1是已经删除，0是正常
          deptId: '',//部门id
          name: '',//部门名称
          orderNum: '',//排序
          parentId: '',//上级部门ID，一级部门为0
          searchText: '',
        }
      };
    },
    created() {
      this.departmentList()
    },
    methods: {
      // 关闭弹框
      close() {
        this.dialogInfo = false
        this.departmentParams = {
          delFlag: '',//是否已删除-1是已经删除，0是正常
          deptId: '',//部门id
          name: '',//部门名称
          orderNum: '',//排序
          parentId: '',//上级部门ID，一级部门为0

        }
      },
      // 删除
      deleteItem(id) {
        this.$confirm('此操作将永久删除该部门, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          let params = {deptId: id}
          let res = await departmentRemove(params)
          if (res.code == 0) {
            this.$message.success(res.msg);
            this.departmentList()
          } else {
            this.$message.error(res.msg);
          }
        }).catch(() => {

        });
      },
      // 取消
      cancel() {
        this.dialogInfo = false
        this.departmentParams = {
          delFlag: '',//是否已删除-1是已经删除，0是正常
          deptId: '',//部门id
          name: '',//部门名称
          orderNum: '',//排序
          parentId: '',//上级部门ID，一级部门为0

        }
      },
      // 提交
      async submit(val) {

        this.departmentParams.delFlag = val.status
        this.departmentParams.name = val.departmentName
        this.departmentParams.orderNum = val.order
        // departmentUpdate
        let res;
        if (this.flag == 'add') {
          res = await departmentSave(this.departmentParams)
        } else {
          res = await departmentUpdate(this.departmentParams)
        }

        if (res.code == 0) {
          this.dialogInfo = false
          this.$message.success(res.msg);
          this.departmentParams = {
            delFlag: '',//是否已删除-1是已经删除，0是正常
            deptId: '',//部门id
            name: '',//部门名称
            orderNum: '',//排序
            parentId: '',//上级部门ID，一级部门为0

          }
          this.departmentList()
        } else {
          this.$message.error(res.msg);
        }
      },
      // 数据列表
      async departmentList() {
        let res = await departmentList(this.departmentParams)
        if (res.code == 0) {
          this.tableData = res.data
        } else {
          this.$message.error(res.msg);
        }
      },
      async showDialog(type, i) {
        this.flag = type
        if (type == 'add') {
          if (i == 0) {
            this.name = '总部门'
            this.departmentParams.parentId = i
          } else {
            this.departmentParams.parentId = i.deptId
            this.name = i.name
          }
        } else if (type == 'editor') {
          if (i.parentId == 0) {
            this.name = '总部门'
            this.departmentParams.parentId = i.parentId
            this.departmentParams.delFlag = i.delFlag.toString()
            this.departmentParams.name = i.name
            this.departmentParams.orderNum = i.orderNum
            this.departmentParams.deptId = i.deptId
            this.departmentParams.status = i.status
          } else {
            let params = {parentId: i.parentId}
            let res = await getgetParent(params)
            if (res.code == 0) {
              console.log(res, '@@@@@');
              this.name = res.data.pName
              this.departmentParams.parentId = i.parentId
              this.departmentParams.delFlag = i.delFlag.toString()
              this.departmentParams.name = i.name
              this.departmentParams.orderNum = i.orderNum
              this.departmentParams.deptId = i.deptId
              this.departmentParams.status = i.status
            } else {
              this.$message.error(res.msg);
            }
          }

        }
        switch (type) {
          case 'add':
            this.dialogInfo = {
              component: departmentForm,
              title: '新增部门',
              visible: true,
              width: '700px',
              componentType: 'addUser'
            }
            break
          case 'editor':
            this.dialogInfo = {
              component: departmentForm,
              title: '编辑部门',
              visible: true,
              width: '700px',
              componentType: 'addUser'
            }
            break
        }
      },
      handleClose(done) {
      },
      handleDrag() {
        console.log(123);
      },
      handleSizeChange(val) {
        console.log(`每页 ${val} 条`);
      },
      handleCurrentChange(val) {
        console.log(`当前页: ${val}`);
      },
    }
  }
</script>

<style lang="scss" scoped>
  .department {
    padding: 20px;
  }
</style>
