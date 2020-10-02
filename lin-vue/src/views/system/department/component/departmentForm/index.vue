<template>
  <el-form :model="form" :rules="rules" ref="ruleForm" label-width="100px">
    <el-form-item label="上级部门" prop="prentDepart">
      <el-input v-model="form.prentDepart"></el-input>
    </el-form-item>
    <el-form-item label="部门名称" prop="departmentName">
      <el-input v-model="form.departmentName"></el-input>
    </el-form-item>
    <el-form-item label="排序" prop="order">
      <el-input v-model="form.order" ></el-input>
    </el-form-item>
    <el-form-item label="状态" prop="status">
      <el-radio-group v-model="form.status">
        <el-radio :label="1">正常</el-radio>
        <el-radio :label="2">禁用</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item>
      <el-button size="small" type="primary" @click="submit('ruleForm')" style="margin-right: 10px">提交</el-button>
      <!--<el-button size="small" @click="resetForm('ruleForm')">取消</el-button>-->
    </el-form-item>
  </el-form>
</template>

<script>
  export default {
    name: "departmentForm",
    props:{
      'componentType':{
        default:'',
        type:String
      }
    },
    data() {
      return {
        form: {
          prentDepart: '',
          departmentName:'',
          order:'',
          status:'',
        },
        rules: {
          prentDepart: [
            { required: true, message: '请输入用户名', trigger: 'blur' },
            { min: 1, max: 25, message: '长度在 1 到 25 个字符', trigger: 'blur' }
          ],
          departmentName: [
            { required: true, message: '请输入用户名', trigger: 'blur' },
            { min: 1, max: 25, message: '长度在 1 到 25 个字符', trigger: 'blur' }
          ],
          order: [],
          status: [
            { required: true, message: '请选择状态', trigger: 'change' }
          ],
        }
      }
    },
    methods: {
      submit(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            console.log(this.form)
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      }
    },
    mounted(){
      console.log('componentType',this.componentType)
    }
  }
</script>

<style scoped>

</style>
