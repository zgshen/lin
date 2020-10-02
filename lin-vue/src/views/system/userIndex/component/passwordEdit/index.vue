<template>
  <el-form :model="form" :rules="rules" ref="ruleForm" label-width="100px" v-loading="formLoading">
    <el-form-item label="登录名" style="margin-bottom: 15px">
      <span>{{formData.username}}</span>
    </el-form-item>
    <template v-if="isAdmin">
      <el-form-item label="密码" prop="pwdNew">
        <el-input v-model="form.pwdNew" type="password"></el-input>
      </el-form-item>
    </template>
    <template v-else>
      <el-form-item label="旧密码" prop="pwdOld" >
        <el-input v-model="form.pwdOld" type="password"></el-input>
      </el-form-item>
      <el-form-item label="新密码" prop="pwdNew" >
        <el-input v-model="form.pwdNew" type="password"></el-input>
      </el-form-item>
    </template>
    <el-form-item>
      <el-button type="primary" size="small" @click="submit('ruleForm')" style="margin-right: 10px">提交
      </el-button>
      <el-button size="small" @click="resetForm('ruleForm')">取消</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
  import {updatePwd_user, updatePwd_admin,getSign} from '@/api/system'
  import {mapState} from 'vuex'
  import {encrypt} from '@/utils/validate'

  export default {
    name: "passwordEdit",
    data() {
      return {
        formLoading:false,
        form: {
          'pwdNew': '',
          'pwdOld':'',
          'userDO.userId': ''
        },
        rules: {
          pwdNew: [
            {required: true, message: '请输入密码', trigger: 'blur'},
            {min: 0, max: 20, message: '长度最多20个字符', trigger: 'blur'}
          ],
          pwdOld: [
            {required: true, message: '请输入旧密码', trigger: 'blur'},
            {min: 0, max: 20, message: '长度最多20个字符', trigger: 'blur'}
          ],
        },
        sign:''
      }
    },
    computed: {
      ...mapState({
        userInfo: state => state.user.userInfo,
      }),
      isAdmin(){
        return this.userInfo.username === 'admin'
      },
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
      }
    },
    methods: {
      // 获取签名
      async getSign() {
          let res = await getSign()
          if (res.code == 0) {
          this.sign = res.data.sign
          } else {
          this.$message.error(res.msg);
          }
      },
      async submit(formName) {
        await this.getSign()
        this.$refs[formName].validate(async (valid) => {
          if (valid) {
            console.log(this.userInfo)
            let res
            if (this.userInfo.username === 'admin') {
              this.formLoading = true
              res = await updatePwd_admin({
                'pwdNew': encrypt(this.form.pwdNew,this.sign),
                'userDO.userId': this.form['userDO.userId']
              })
              this.formLoading = false
            } else {
              this.formLoading = true
              this.form.pwdNew=encrypt(this.form.pwdNew,this.sign)
              res = await updatePwd_user(this.form)
              this.formLoading = false
            }
            if (res.code === 0) {
              this.$message({
                type: 'success',
                message: '密码修改成功'
              });
              this.$parent.$parent.getUserList();
              this.$parent.$parent.dialogInfo.visible = false
            }
            console.log(res)
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
        this.$emit('cancel')
      }
    },
    async created() {
      this.form = {
        'pwdNew': '',
        'userDO.userId': this.formData.userId
      }
      console.log(this.formData.userId)
    }
  }
</script>

<style scoped>

</style>
