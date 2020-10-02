<template>
  <div class="systemMenu">
    <div class="toolbar">
      <div class="toolbar-left">
        <el-button type="success" size="small" icon="el-icon-plus" @click="showDialog('add')">添加</el-button>
        <!--<el-button type="danger" size="small" icon="el-icon-delete">删除</el-button>-->
      </div>
      <!--<div class="toolbar-right">
        <div style="width:230px;display: inline-block">
          <el-input
            placeholder="请输入内容"
            v-model="searchValue"
            size="small"
            clearable>
          </el-input>
        </div>
        <el-button type="primary" size="small">查询</el-button>
      </div>-->
    </div>
    <div class="tablebar">
      <div>
        <el-table
          v-loading="listLoading"
          ref="multipleTable"
          :data="tableData"
          row-key="menuId"
          size="small"
          cell-class-name="cellName"
          header-cell-class-name="headerTable"
          :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
          style="width: 100%">
          <!--<el-table-column
            type="selection"
            width="55">
          </el-table-column>-->
          <el-table-column
            v-for="(item, index) in tableLabel"
            :key="index"
            :prop="item.prop"
            :width="item.width"
            :label="item.label">
            <template slot-scope="scope">
              <div v-if="item.prop==='vueIcon'">
                <svg-icon v-if="judgeIco(scope.row[item.prop],'svg')" :icon-class="scope.row[item.prop]||''"/>
                <i v-else :class="scope.row[item.prop]||''"/>
              </div>
              <el-tag effect="dark" size="mini" :type="typesTexts[scope.row[item.prop]].icon"
                      v-else-if="item.prop==='type'">{{typesTexts[scope.row[item.prop]].text}}
              </el-tag>
              <span v-else>{{scope.row[item.prop]}}</span>
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
              <el-button v-if="scope.row['type'] !== 2" type="success" icon="el-icon-plus" size="mini"
                         @click="showDialog('addChild',scope.row)"></el-button>
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
    <el-dialog v-el-drag-dialog :modal-append-to-body="false" :visible.sync="dialogInfo.visible"
               :width="dialogInfo.width" :title="dialogInfo.title">
      <component v-if="dialogInfo.visible" :is="dialogInfo.component" :componentType="dialogInfo.componentType"
                 :formData="dialogInfo.formData" :formMenuType="formMenuType"></component>
    </el-dialog>
  </div>
</template>

<script>
  import elDragDialog from '@/directive/el-drag-dialog'
  import systemMenuForm from './component/systemMenuForm'
  import Pagination from '@/components/Pagination'
  import {getMenuList, deleteMenu} from '@/api/system'

  export default {
    name: "systemMenu",
    directives: {elDragDialog},
    components: {
      systemMenuForm,
      Pagination
    },
    data() {
      return {
        typesTexts: [{icon: 'success', text: '目录'}, {icon: '', text: '菜单'}, {icon: 'warning', text: '按钮'}],
        listLoading: true,
        paginationParams: {
          total: 0,
          pageSize: 10,
          currentPage: 1,
          pageSizes: [10, 50, 100, 150],
        },
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
          {label: '名称', width: '', prop: 'name'},
          {label: '图标', width: '', prop: 'vueIcon'},
          {label: '类型', width: '', prop: 'type'},
          {label: '地址', width: '', prop: 'vueUrl'},
          {label: '权限标识', width: '', prop: 'perms'},
        ],
        tableData: [],
        currentPage4: 1,
        formMenuType:0
      };
    },
    methods: {
      judgeIco(i, type) {
        let item = i || ''
        switch (type) {
          case 'svg':
            return item.indexOf('-') === -1 ? true : false
            break
          default:
            return false
        }
      },
      clickable(scope) {
        console.log(scope)
      },
      async showDialog(type, cellData) {
        this.formMenuType =cellData?cellData.type?cellData.type:0:0
        switch (type) {
          case 'add':
            this.dialogInfo = {
              component: systemMenuForm,
              title: '新增菜单',
              visible: true,
              width: '700px',
              componentType: 'addSystemMenu',
              formData: {}
            }
            break
          case 'addChild':
            this.formMenuType+=1
            this.dialogInfo = {
              component: systemMenuForm,
              title: '新增菜单',
              visible: true,
              width: '700px',
              componentType: 'addChildSystemMenu',
              formData: cellData
            }
            break
          case 'edit':
            this.dialogInfo = {
              component: systemMenuForm,
              title: '新增菜单',
              visible: true,
              width: '700px',
              componentType: 'editSystemMenu',
              formData: cellData
            }
            break
          case 'delete':
            this.$confirm('此操作将永久删除改菜单, 是否继续?', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(async () => {
              let res = await deleteMenu({id: cellData.menuId})
              if (res.code === 0) {
                this.$message({
                  type: 'success',
                  message: '删除成功'
                });
                this.getMenuList();
              }
            }).catch(() => {
              this.$message({
                type: 'info',
                message: '已取消删除'
              });
            });

            break
        }
      },
      getPagination(val) {
        console.log(val);
      },

      async getMenuList() {
        let params = {
          gmtCreate: "",
          gmtModified: "",
          menuId: "",
          name: "",
          parentId: "",
          url: "",
        }
        this.listLoading = true
        let res = await getMenuList(params)
        this.listLoading = false
        if (res.code === 0) {
          this.tableData = this.menuListTree(res.data)
        }
      },
      menuListTree(data) {
        let list = data || [], types = [0, 1, 2], objArr = {};
        types.forEach(v => {
          objArr[v] = []
          list.forEach(k => {
            if (v === k.type) {
              objArr[v].push(Object.assign({children: []}, k))
            }
          })
        })
        objArr[1].forEach(v => {
          objArr[2].forEach(k => {
            if (v.menuId === k.parentId) {
              v.children.push(k)
            }
          })
        })
        objArr[0].forEach(v => {
          objArr[1].forEach(k => {
            if (v.menuId === k.parentId) {
              v.children.push(k)
            }
          })
        })

        return objArr[0]
      }
    },
    created() {
      this.getMenuList()
    }
  }
</script>

<style lang="scss" scoped>
  .systemMenu {
    padding: 10px 30px;
  }

</style>
