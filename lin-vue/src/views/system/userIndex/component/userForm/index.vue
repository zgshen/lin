<template>
  <el-form :model="form" :rules="rules" ref="ruleForm" label-width="100px" v-loading="formLoading">
    <el-form-item label="登录名" prop="username">
      <el-input v-model.trim="form.username" :disabled="componentType === 'editUser'"></el-input>
    </el-form-item>
    <el-form-item label="用户名" prop="name">
      <el-input v-model="form.name" :disabled="componentType === 'editUser'"></el-input>
    </el-form-item>
    <el-form-item label="工号" prop="empNo">
      <el-input v-model.trim="form.empNo"></el-input>
    </el-form-item>
    <el-form-item label="密码" prop="password" v-if="componentType !== 'editUser'">
      <el-input v-model.trim="form.password" type="password"></el-input>
    </el-form-item>
    <!--    <el-form-item label="部门" prop="deptName">-->
    <!--      <el-input v-model="form.deptName" readonly placeholder="请选择部门" @focus="showDialog"></el-input>-->
    <!--    </el-form-item> -->
    <el-form-item label="手机号" prop="mobile">
      <el-input v-model.trim="form.mobile"></el-input>
    </el-form-item>
    <el-form-item label="E-mail" prop="email">
      <el-input v-model.trim="form.email"></el-input>
    </el-form-item>
    <el-form-item label="性别" prop="sex">
      <el-radio-group v-model="form.sex">
        <el-radio :label="0">男</el-radio>
        <el-radio :label="1">女</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="状态" prop="status">
      <el-radio-group v-model="form.status">
        <el-radio :label="1">正常</el-radio>
        <el-radio :label="0">禁用</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="角色" prop="roleIds">
      <el-checkbox-group v-model="form.roleIds">
        <el-checkbox :label="roleItem.roleId" name="roleIds" v-for="(roleItem,roleKey) in roleList" :key="roleKey">
          {{roleItem.roleName}}
        </el-checkbox>
      </el-checkbox-group>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" size="small" @click="submit('ruleForm')" style="margin-right: 10px">提交</el-button>
      <!--<el-button size="small" @click="resetForm('ruleForm')">取消</el-button>-->
    </el-form-item>
    <el-dialog
      v-el-drag-dialog
      width="300px"
      title="选择部门"
      :visible.sync="innerVisible"
      :modal="false">
      <el-tree
        v-loading="treeLoading"
        :data="treeData"
        default-expand-all
        highlight-current
        node-key="deptId"
        :expand-on-click-node="false"
        @node-click="nodeClick"
        :props="defaultProps">
      </el-tree>
    </el-dialog>
  </el-form>
</template>

<script>
  import elDragDialog from '@/directive/el-drag-dialog' // base on element-ui
  import {addNewUser, updateUser, getRoleList,getUserInfo,getUserTree,getSign} from '@/api/system'
  import {encrypt} from '@/utils/validate'

  export default {
    name: "userForm",
    directives: {elDragDialog},
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
      }
    },
    data() {
      const isPhone=(rule, phone, callback)=>{
        if (!phone) {
          callback(new Error('请输入手机号'))
        } else {
          let myreg = /^[1][3,4,5,6,7,8,9][0-9]{9}$/;
          if(!myreg.test(phone)){
            callback(new Error('手机号不合法'))
          }else {
            callback()
          }
        }
      }
      return {
        treeLoading: false,
        formLoading: false,
        treeData: [],
        defaultProps: {
          children: 'children',
          label: 'name'
        },
        innerVisible: false,
        roleList: [],
        form: {
          name: '',
          username: '',
          password: '',
          deptId: '',
          deptName: '',
          email: '',
          roleIds: [],
          status: '',
          sex:'',
          mobile:'',
          empNo:'',
        },
        rules: {
          name: [
            {required: true, message: '请输入用户名', trigger: 'blur'},
            {min: 1, max: 25, message: '长度在 1 到 25 个字符', trigger: 'blur'}
          ],
          username: [
            {required: true, message: '请输入登录名', trigger: 'blur'},
            {min: 1, max: 25, message: '长度在 1 到 25 个字符', trigger: 'blur'}
          ],
          password: [
            {required: true, message: '请输入密码', trigger: 'blur'},
            {min: 6, max: 25, message: '长度在 6 到 25 个字符', trigger: 'blur'}
          ],
          empNo:[
            {required: true, message: '请输入工号', trigger: 'blur'},
            {min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur'}
          ],
          deptId: [
            {required: false, message: '请选择部门', trigger: 'change'}
          ],
          // email: [
          //   {required: true, message: '请输入邮箱地址', trigger: 'blur'},
          //   {type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur']}
          // ],
          roleIds: [
            {type: 'array', required: false, message: '请至少选择角色', trigger: 'change'}
          ],
          status: [
            {required: false, message: '请选择状态', trigger: 'change'}
          ],
        },
        sign:'',
      }
    },
    methods: {
      nodeClick(cellData, node, component) {
        this.form.deptName = cellData.name
        this.form.deptId = cellData.deptId
        console.log(cellData)
        this.innerVisible = false
      },
      showDialog() {
        this.innerVisible = true
        this.getUserTree()
      },
      async submit(formName) {
       await this.getSign()
        this.$refs[formName].validate((valid) => {
          if (valid) {
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
      // 获取签名
      async getSign() {
          let res = await getSign()
          if (res.code == 0) {
          this.sign = res.data.sign
          } else {
          this.$message.error(res.msg);
          }
      },
      //  添加或修改
      async subUserForm() {
        let res, roleIds = {roleIds: this.form.roleIds}, formData;
        if (this.componentType === 'addUser') {
          formData = {
            name: this.form.name,
            username: this.form.username,
            password: encrypt(this.form.password,this.sign),
            deptId: this.form.deptId,
            email: this.form.email,
            status: this.form.status,
            sex:this.form.sex,
            mobile:this.form.mobile,
            empNo:this.form.empNo,
          };
          res = await addNewUser(roleIds, formData)
          if (res.code === 0) {
            this.$message({
              type: 'success',
              message: '添加成功'
            });
            this.$parent.$parent.getUserList();
            this.$parent.$parent.dialogInfo.visible = false
          }
        }
        if (this.componentType === 'editUser') {
          formData = {
            name: this.form.name,
            username: this.form.username,
            deptId: this.form.deptId,
            email: this.form.email,
            status: this.form.status,
            userId: this.form.userId,
            sex:this.form.sex,
            mobile:this.form.mobile,
            empNo:this.form.empNo,
          };
          res = await updateUser(roleIds, formData)
          if (res.code === 0) {
            this.$message({
              type: 'success',
              message: '编辑成功'
            });
            this.$parent.$parent.getUserList();
            this.$parent.$parent.dialogInfo.visible = false
          }
        }
      },

      //获取角色列表
      async getRoleList() {
        let params = {
          pageNum: 1,//当前页
          pageSize: 10000,//每页条数
          remark: '',//备注
          roleName: '',//角色名称
          roleSign: '',//角色标识
          userIdCreate: '',//创建人id
        };
        let res = await getRoleList(params)
        if (res.code === 0) {
          this.roleList = res.data.list
        }
      },
      //获取部门树结构
      async getUserTree() {
        this.treeLoading = true
        let res = await getUserTree()
        this.treeLoading = false
        if (res.code === 0) {
          //默认顶级节点为deptId=''
          this.treeData = [{
            deptId: '',
            name: '顶级节点',
            children: res.data
          }]
        }
      },
      //获取用户信息
      async getUserInfo(userId){
        let res = await getUserInfo({userId})
        if (res.code === 0) {
          this.form = {
            name: res.data.user.name,
            username: res.data.user.username,
            deptId:res.data.user.deptId,
            deptName:res.data.user.deptName,
            email: res.data.user.email,
            roleIds: res.data.user.roleIds || [],
            status: res.data.user.status,
            userId: res.data.user.userId,
            sex:res.data.user.sex,
            mobile:res.data.user.mobile,
            empNo:res.data.user.empNo,
          };
          this.form.roleIds=[];
          if(res.data.user.roleIds.length>0){
            res.data.user.roleIds.forEach(v=>{
                this.form.roleIds.push(v.toString())
            })
          }
        }
      }
    },
    async created() {
      // await getSign();
      this.formLoading = true
      await this.getRoleList()
      this.formLoading = false
      if (this.componentType === 'addUser') {
        this.form = {
          name: '',
          username: '',
          password: '',
          deptId: '',
          deptName:'',
          email: '',
          roleIds: [],
          status: '',
          sex:'',
          mobile:'',
          empNo:'',
        }
      }
      if (this.componentType === 'editUser') {
        this.rules.password[0].required = false
        this.getUserInfo(this.formData.userId)
        console.log('componentType')
      }

    }
  }
</script>

<style scoped>

</style>
