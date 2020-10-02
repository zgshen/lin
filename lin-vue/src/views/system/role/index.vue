<template>
  <div style="padding:10px 30px;position: relative">
    <div class="toolbar">
      <div class="toolbar-left">
        <el-button type="success" size="small" icon="el-icon-plus" @click="showDialog('add')">添加</el-button>
<!--        <el-button type="danger" size="small" icon="el-icon-delete"  @click="showDialog('deletes')">删除</el-button>-->
      </div>
      <!--<div class="toolbar-right">
        &lt;!&ndash;<div style="width:230px;display: inline-block">&ndash;&gt;
          &lt;!&ndash;<el-input&ndash;&gt;
            &lt;!&ndash;placeholder="请输入内容"&ndash;&gt;
            &lt;!&ndash;v-model="searchValue"&ndash;&gt;
            &lt;!&ndash;size="small"&ndash;&gt;
            &lt;!&ndash;clearable>&ndash;&gt;
          &lt;!&ndash;</el-input>&ndash;&gt;
        &lt;!&ndash;</div>&ndash;&gt;
        <el-dropdown trigger="click" :hide-on-click="false" style="display: inline-block;vertical-align: middle" type="info">
          <el-button type="info" size="mini" plain style="padding: 6px 5px">
            <i class="el-icon-menu" style="font-size: 22px"></i>
            <i class="el-icon-arrow-down" style="font-size: 16px;margin-left: 0"></i>
          </el-button>

          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item v-for="(item,index) in tableLabel" :key="index">
              <el-checkbox v-model="item.checked" :disabled="item.isDisabled"
                           @change="checkedItem(index)">
                {{item.label}}
              </el-checkbox>
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>-->
    </div>
    <div class="tablebar">
      <div>
        <el-table
          v-loading="listLoading"
          ref="multipleTable"
          :data="tableData"
          cell-class-name="cellName"
          header-cell-class-name="headerTable"
          @select="selectCell"
          @select-all="selectAll"
          style="width: 100%">
<!--          <el-table-column-->
<!--            type="selection"-->
<!--            width="55">-->
<!--          </el-table-column>-->
          <el-table-column
            type="index"
            label="序号"
            width="50">
          </el-table-column>
          <el-table-column
            v-if="item.checked"
            v-for="(item, index) in tableLabel"
            :key="index"
            :prop="item.prop"
            :width="item.width"
            :label="item.label">
            <template slot-scope="scope">
              <!--删除-->
              <div v-if="item.operation">
                <el-button type="primary" icon="el-icon-edit" size="mini" @click="showDialog('edit',scope.row)"></el-button>
                <el-button type="danger" icon="el-icon-delete" size="mini" @click="showDialog('delete',scope.row)"></el-button>
              </div>
              <div v-else>{{scope.row[item.prop]}}</div>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div style="text-align: right;">
        <pagination
          :hidden="paginationParams.total<1"
          :pageSizes="paginationParams.pageSizes"
          :total="paginationParams.total"
          :page.sync="paginationParams.currentPage"
          :limit.sync="paginationParams.pageSize"
          @pagination="getPagination" />
      </div>
    </div>
    <el-dialog v-el-drag-dialog
               :modal-append-to-body="false"
               :visible.sync="dialogInfo.visible"
               :width="dialogInfo.width"
               :title="dialogInfo.title">
      <component
        v-if="dialogInfo.visible"
        :is="dialogInfo.component"
        :componentType="dialogInfo.componentType"
        :formData="dialogInfo.formData"></component>
    </el-dialog>
  </div>
</template>

<script>
  import { getRoleList,deleteRole } from '@/api/system'
  import Pagination from '@/components/Pagination'
  import elDragDialog from '@/directive/el-drag-dialog' // base on element-ui
  import roleForm from './component/roleForm'
  export default {
    name: 'roleIndex',
    directives: {elDragDialog},
    components: {
      Pagination,
      roleForm,
    },
    data() {
      return {
        listLoading:false,
        dialogInfo: {
          component: '',
          title: '',
          visible: false,
          width: '',
          componentType: ''
        },
        paginationParams:{
          total:0,
          pageSize:10,
          currentPage:1,
          pageSizes:[10,50,100,150],
        },
        currentPage4: 4,
        tableLabel: [
          {label: '角色名', width: '', prop: 'roleName', checked: true, isDisabled: false},
          {label: '备注', width: '', prop: 'remark', checked: true, isDisabled: false},
          {label: '权限', width: '', prop: 'roleSign', checked: true, isDisabled: false},
          {label: '操作', width: '', prop: 'area', operation: true, checked: true, isDisabled: false},
        ],
        tableData: [
        ],
        input: '',
        title: '',
        selectRoleIds:[]
      }
    },
    created() {
      this.getRoleList()
    },
    methods: {
      //表格全选
      selectAll(selection){
        this.selectRoleIds=selection.map(v=>v.roleId)
        console.log(this.selectRoleIds)
      },
      //单个表格选择
      selectCell(selection, row){
        this.selectRoleIds=selection.map(v=>v.roleId)
        console.log(this.selectRoleIds)
      },
      showDialog(type,cellData) {
        switch (type) {
          case 'add':
            this.dialogInfo = {
              component: roleForm,
              title: '新增角色',
              visible: true,
              width: '700px',
              componentType: 'addRole',
              formData:{}
            }
            break
          case 'edit':
            this.dialogInfo = {
              component: roleForm,
              title: '角色修改',
              visible: true,
              width: '700px',
              componentType: 'editRole',
              formData:cellData
            }
            break
          case 'delete':
            this.$confirm('此操作将永久删除该角色, 是否继续?', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              this.deleteRole({ids:[cellData.roleId]})
            }).catch(() => {
              this.$message({
                type: 'info',
                message: '已取消删除'
              });
            });
            break
          case 'deletes':
            if(this.selectRoleIds.length === 0){
              this.$message({
                type: 'warning',
                message: '请先勾选用户'
              });
              return
            }
            this.$confirm('此操作将永久删除勾选角色, 是否继续?', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              this.deleteRole({ids:this.selectRoleIds})
            }).catch(() => {
              this.$message({
                type: 'info',
                message: '已取消删除'
              });
            });
            break
          case 'reset':
            this.dialogInfo = {
              component: passwordEdit,
              title: '重置密码',
              visible: true,
              width: '400px',
              componentType: 'passwordEdit',
              formData:cellData
            }
            break
        }
      },
      getPagination(){
        this.getRoleList()
      },
      // 数据列表
      async getRoleList() {
        this.listLoading=true
        let roleParams = {
          pageNum: this.paginationParams.currentPage,//当前页
          pageSize: this.paginationParams.pageSize,//每页条数
          remark: '',//备注
          roleName: '',//角色名称
          roleSign: '',//角色标识
          userIdCreate: '',//创建人id
        }
        let res = await getRoleList(roleParams)
        this.listLoading=false
        if(res.code === 0){
          this.tableData = res.data.list
          this.paginationParams.total = parseInt(res.data.total)
        }
        console.log(res, 'res')
      },
      checkedItem(i) {
        let index = 0
        this.tableLabel.forEach((v, i) => {
          if (!v.checked) {
            index++
          }
        })
        if (index === this.tableLabel.length - 1) {
          this.tableLabel.forEach((v, i) => {
            if (v.checked) {
              v.isDisabled = true
            }
          })
        } else {
          this.tableLabel.forEach((v, i) => {
            v.isDisabled = false
          })
        }
      },
      //删除
      async deleteRole(ids){
        let res = await deleteRole(ids)
        if(res.code === 0){
          this.$message({
            type: 'success',
            message: '删除成功!'
          });
          this.getRoleList()
        }
      },
    }
  }
</script>
