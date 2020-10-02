<template>
  <el-form :model="form" :rules="rules" ref="ruleForm" label-width="100px">
    <el-form-item label="上级菜单" prop="parentName">
      <el-input v-model="form.parentName" disabled></el-input>
    </el-form-item>
    <el-form-item label="菜单类型" prop="type">
      <el-radio-group v-model="form.type">
        <el-radio :label="0" v-if="formMenuType === 0">目录</el-radio>
        <el-radio :label="1"  v-if="formMenuType === 1">菜单</el-radio>
        <el-radio :label="2"  v-if="formMenuType === 2">按钮</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="菜单名称" prop="name">
      <el-input v-model="form.name"></el-input>
    </el-form-item>
    <el-form-item label="链接地址" prop="vueUrl">
      <el-input v-model="form.vueUrl"></el-input>
    </el-form-item>
    <el-form-item label="权限标识" prop="perms">
      <el-input v-model="form.perms"></el-input>
    </el-form-item>
    <el-form-item label="排序" prop="orderNum">
      <el-input v-model="form.orderNum"></el-input>
    </el-form-item>
    <el-form-item label="图标" prop="vueIcon">
      <el-input style="width: 200px" v-model="form.vueIcon"></el-input>
      <el-button type="primary" @click="innerVisible=true">选择图标</el-button>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" size="small" @click="submit('ruleForm')" style="margin-right: 10px">提交</el-button>
      <!--<el-button size="small" @click="resetForm('ruleForm')">取消</el-button>-->
    </el-form-item>
    <el-dialog
      v-el-drag-dialog
      width="600px"
      title="选择图标"
      :visible.sync="innerVisible"
      :modal="false">
      <div>
        <icons @getIco="getIco"></icons>
      </div>
    </el-dialog>
  </el-form>
</template>

<script>
  import elDragDialog from '@/directive/el-drag-dialog' // base on element-ui
  import icons from '../icons'
  import {addMenu, editMenu,getMenuDetail} from '@/api/system'

  export default {
    name: "systemMenuForm",
    directives: {elDragDialog},
    components: {
      icons
    },
    props: {
      'componentType': {
        default: '',
        type: String
      },
      'formData': {
        default() {
          return {}
        },
        type: Object
      },
      'formMenuType':{
        default:0
      }
    },
    data() {
      // vueIcon
      // vueUrl
      return {
        treeLoading: false,
        innerVisible: false,
        form: {
          parentId: '',
          parentName: '',
          type: '',
          name: '',
          url: '',
          perms: '',
          orderNum: '',
          icon: '',
          vueIcon:'',
          vueUrl:'',
        },
        rules: {
          name:[
            {required: true, message: '请填写菜单名称', trigger: 'blur'},
          ],
          type:[
            {required: true, message: '请勾选类型', trigger: 'blur'},
          ],
          parentId:[
            {required: true, message: '请选择上级菜单', trigger: 'blur'},
          ],
          prentDepart: [
            {required: true, message: '请输入用户名', trigger: 'blur'},
            {min: 1, max: 25, message: '长度在 1 到 25 个字符', trigger: 'blur'}
          ],
          departmentName: [
            {required: true, message: '请输入用户名', trigger: 'blur'},
            {min: 1, max: 25, message: '长度在 1 到 25 个字符', trigger: 'blur'}
          ],
          order: [],
          status: [
            {required: true, message: '请选择状态', trigger: 'change'}
          ],
        }
      }
    },
    methods: {
      getIco(item) {
        switch (item.type){
          case 'el':
            this.form.vueIcon = 'el-icon-'+item.value
            break
          case 'svg':
            this.form.vueIcon = item.value
            break
        }

        this.innerVisible = false
      },
      submit(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            console.log(this.form)
            this.subUserForm()
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      },
      //  添加或修改
      async subUserForm() {
        let res, params;
        if (this.componentType === 'addSystemMenu' || this.componentType === 'addChildSystemMenu') {
          params = {
            name: this.form.name,
            deptId: '',
            gmtModified: '',
            icon: this.form.icon,
            gmtCreate: '',
            orderNum: this.form.orderNum,
            parentId: this.form.parentId,
            perms: this.form.perms,
            type: this.form.type,
            url: this.form.url,
            vueIcon:this.form.vueIcon,
            vueUrl:this.form.vueUrl,
          }
          res = await addMenu(params)
          if (res.code === 0) {
            this.$message({
              type: 'success',
              message: '添加成功'
            });
            this.$parent.$parent.getMenuList();
            this.$parent.$parent.dialogInfo.visible = false
          }
        }
        if (this.componentType === 'editSystemMenu') {
          res = await editMenu(this.form)
          if (res.code === 0) {
            this.$message({
              type: 'success',
              message: '修改成功'
            });
            this.$parent.$parent.getMenuList();
            this.$parent.$parent.dialogInfo.visible = false
          }

        }
      },
      async getMenuDetail(id){
        let res = await getMenuDetail({id})
        if(res.code === 0){
          Object.assign(this.form, {
            ...res.data.menu,
            parentId: res.data.pId,
            parentName:  res.data.pName,
            type:this.formMenuType
          })
        }
      }
    },
    async mounted() {
      if (this.componentType === 'addSystemMenu') {
        Object.assign(this.form, {
          parentId: 0,
          parentName: '根目录',
          type:this.formMenuType
        })
      }
      if (this.componentType === 'addChildSystemMenu') {
        Object.assign(this.form, {
          parentId: this.formData.menuId,
          parentName: this.formData.name,
          type:this.formMenuType
        })
      }
      if (this.componentType === 'editSystemMenu') {
        await this.getMenuDetail(this.formData.menuId)
      }
      console.log('componentType', this.form)
    }
  }
</script>

<style lang="scss">
  .systemMenu {
    .el-input__prefix {
      color: black;
    }
  }

</style>