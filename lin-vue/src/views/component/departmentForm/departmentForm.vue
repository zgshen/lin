<template>
    <el-form :model="form" :rules="rules" ref="ruleForm" label-width="100px">
        <el-form-item label="上级部门" prop="prentDepart">
            <el-input v-model="form.prentDepart" disabled></el-input>
        </el-form-item>
        <el-form-item label="部门名称" prop="departmentName">
            <el-input v-model="form.departmentName"></el-input>
        </el-form-item>
        <el-form-item label="排序" prop="order">
            <el-input v-model="form.order"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="status">
            <el-radio-group v-model="form.status">
                <el-radio :label="1">正常</el-radio>
                <el-radio :label="2">禁用</el-radio>
            </el-radio-group>
        </el-form-item>
        <el-form-item>
            <el-button size="small" type="primary" @click="submit('ruleForm')" style="margin-right: 10px">提交
            </el-button>
            <el-button size="small" @click="resetForm('ruleForm')">取消</el-button>
        </el-form-item>
    </el-form>
</template>

<script>
    export default {
        name: "departmentForm",
        props: {
            'componentType': {
                default: '',
                type: String
            },
            'name': {
                default: '',
                type: String
            },
            'departmentParams': {
                default: '',
                type: Object
            },
            flag: {
                default: '',
            }
        },
        data() {
            return {
                form: {
                    prentDepart: this.name,
                    departmentName: this.departmentParams.name,
                    order: this.departmentParams.orderNum,
                    status: this.departmentParams.delFlag ? Number(this.departmentParams.delFlag) : 1,
                },
                rules: {
                    prentDepart: [
                        {required: true, message: '请输入上级部门', trigger: 'blur'},
                        {min: 1, max: 25, message: '长度在 1 到 25 个字符', trigger: 'blur'}
                    ],
                    departmentName: [
                        {required: true, message: '请输入部门名称', trigger: 'blur'},
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
            submit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.$emit('submit', this.form)
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
        mounted() {
            console.log(this.componentType)
            console.log(this.parentId);
        }
    }
</script>

<style scoped>

</style>
