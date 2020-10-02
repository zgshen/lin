<template>
  <div style="padding:10px 30px;position: relative">
    <div class="toolbar">
      <div class="toolbar-left">
        <el-button type="success" size="small" icon="el-icon-plus" @click="showDialog('add')">添加</el-button>
        <el-button type="danger" size="small" icon="el-icon-delete" @click="showDialog('deletes')">删除
        </el-button>
      </div>
      <div class="toolbar-right">
        <div style="width:230px;display: inline-block">
          <el-input
            v-model="searchValue"
            placeholder="用户名/登录名/邮箱/手机号"
            size="small"
            clearable>
          </el-input>
        </div>
        <el-button type="primary" size="small" @click="clickSearch">查询</el-button>
      </div>
    </div>
    <div class="tablebar">
      <div>
        <el-table
          ref="multipleTable"
          v-loading="listLoading"
          :data="tableData"
          size="medium"
          cell-class-name="cellName"
          header-cell-class-name="headerTable"
          @select="selectCell"
          @select-all="selectAll"
          style="width: 100%">
          <el-table-column
            type="selection"
            width="55">
          </el-table-column>
          <el-table-column
            type="index"
            label="序号"
            width="50">
          </el-table-column>
          <el-table-column
            v-for="(item, index) in tableLabel"
            :key="index"
            :prop="item.prop"
            :width="item.width"
            :label="item.label">
            <template slot-scope="scope">
              <el-tag v-if="item.prop==='status'" :type="scope.row[item.prop]?'success':'danger'"
                      effect="dark" size="mini">{{scope.row[item.prop]?'正常':'禁用'}}
              </el-tag>
              <div v-else-if="item.prop==='sex'">{{scope.row[item.prop] ===0 ?'男':scope.row[item.prop] ===1 ?'女':''}}
              </div>
              <div v-else>{{scope.row[item.prop]}}</div>
            </template>
          </el-table-column>
          <el-table-column
            prop="item.prop"
            width=""
            label="操作">
            <template slot-scope="scope">
              <el-button type="primary" icon="el-icon-edit" size="mini"
                         @click="showDialog('edit',scope.row)"></el-button>
              <el-button type="danger" icon="el-icon-delete" size="mini"
                         @click="showDialog('delete',scope.row)"></el-button>
              <el-button type="success" size="mini" icon="el-icon-key"
                         @click="showDialog('reset',scope.row)"></el-button>
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
          @pagination="getPagination"/>
      </div>
    </div>
    <el-dialog v-el-drag-dialog
               modal-append-to-body
               append-to-body
               :visible.sync="dialogInfo.visible"
               :width="dialogInfo.width"
               :title="dialogInfo.title">
      <component
        v-if="dialogInfo.visible"
        :is="dialogInfo.component"
        :componentType="dialogInfo.componentType"
        :formData="dialogInfo.formData"
        @cancel="cancel"></component>
    </el-dialog>
  </div>
</template>

<script>
  import elDragDialog from '@/directive/el-drag-dialog' // base on element-ui
  import userForm from '../userForm'
  import passwordEdit from '../passwordEdit'
  import {getUserList, deleteUsers} from '@/api/system'
  import Pagination from '@/components/Pagination'
  import {mapState} from 'vuex'

  export default {
    name: "mainCon",
    directives: {elDragDialog},
    components: {
      userForm,
      Pagination,
      passwordEdit
    },
    computed: {
      ...mapState({
        userInfo: state => state.user.userInfo,
      }),
    },
    props: {
      'cellData': {
        default() {
          return {}
        },
        type: Object
      }
    },
    data() {
      return {
        listLoading: true,
        dialogInfo: {
          component: '',
          title: '',
          visible: false,
          width: '',
          componentType: '',
          formData: {}
        },
        searchValue: '',
        tableLabel: [
          {label: '用户名', width: '', prop: 'name'},
          {label: '登录名', width: '', prop: 'username'},
          {label: '性别', width: '', prop: 'sex'},
          {label: '邮箱', width: '', prop: 'email'},
          {label: '手机号', width: '', prop: 'mobile'},
          {label: '状态', width: '', prop: 'status'},
        ],
        tableData: [],
        currentPage4: 1,
        total: 0,
        paginationParams: {
          total: 0,
          pageSize: 10,
          currentPage: 1,
          pageSizes: [10, 50, 100, 150],
        },
        userFormData: {},
        selectUserIds: [],
      };
    },
    watch: {
      cellData() {
        this.paginationParams = {
          total: 0,
          pageSize: 10,
          currentPage: 1,
          pageSizes: [10, 50, 100, 150],
        }
        this.getUserList()
      }
    },
    methods: {
      cancel() {
        this.dialogInfo.visible = false
      },
      clickSearch() {
        this.paginationParams = {
          total: 0,
          pageSize: 10,
          currentPage: 1,
          pageSizes: [10, 50, 100, 150],
        }
        this.getUserList()
      },
      //分页
      getPagination() {
        this.getUserList()
      },
      //表格全选
      selectAll(selection) {
        this.selectUserIds = selection.map(v => v.userId)
        console.log(this.selectUserIds)
      },
      //单个表格选择
      selectCell(selection, row) {
        this.selectUserIds = selection.map(v => v.userId)
        console.log(this.selectUserIds)
      },
      showDialog(type, cellData) {
        switch (type) {
          case 'add':
            this.dialogInfo = {
              component: userForm,
              title: '新增用户',
              visible: true,
              width: '700px',
              componentType: 'addUser',
              formData: {}
            }
            break
          case 'edit':
            this.dialogInfo = {
              component: userForm,
              title: '编辑用户',
              visible: true,
              width: '700px',
              componentType: 'editUser',
              formData: cellData
            }
            break
          case 'delete':
            this.$confirm('此操作将永久删除该用户, 是否继续?', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              this.deleteUsers({ids: [cellData.userId]})
            }).catch(() => {
              this.$message({
                type: 'info',
                message: '已取消删除'
              });
            });
            break
          case 'deletes':
            if (this.selectUserIds.length === 0) {
              this.$message({
                type: 'warning',
                message: '请先勾选用户'
              });
              return
            }
            this.$confirm('此操作将永久删除勾选用户, 是否继续?', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              this.deleteUsers({ids: this.selectUserIds})
            }).catch(() => {
              this.$message({
                type: 'info',
                message: '已取消删除'
              });
            });
            break
          case 'reset':
            if(this.userInfo.username === 'admin' || cellData.username === this.userInfo.username){
              this.dialogInfo = {
                component: passwordEdit,
                title: '重置密码',
                visible: true,
                width: '400px',
                componentType: 'passwordEdit',
                formData: cellData
              }
            }else {
              this.$message({
                type: 'warning',
                message: '您无权限修改其他人的密码'
              });
            }

            break
        }
      },
      //获取用户列表
      async getUserList() {
        let listParams = {
          pageSize: this.paginationParams.pageSize,
          pageNum: this.paginationParams.currentPage,
          deptId: this.cellData.deptId,
          // name: this.searchValue,
          searchText: this.searchValue,
        }
        this.listLoading = true
        let res = await getUserList(listParams)
        this.listLoading = false
        this.tableData = res.data.list
        this.paginationParams.total = parseInt(res.data.total)
        this.selectUserIds = []
      },
      //删除用户
      async deleteUsers(ids) {
        let res = await deleteUsers(ids)
        if (res.code === 0) {
          this.$message({
            type: 'success',
            message: '删除成功'
          });
          this.getUserList()
        }
      }
    },
    async mounted() {
      this.getUserList()
    }
  }
</script>
