<template>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
        <el-tabs type="border-card" @tab-click="isTab()" ref="borderTab">
            <el-tab-pane label="基本资料">
                <div v-if="type==0">
                    <el-form-item label="登录名" prop="loginName">
                        <el-col :span="20">
                            <span>{{loginName}}</span>
                            <!--<el-input v-model="ruleForm.name"></el-input>-->
                        </el-col>
                    </el-form-item>
                    <el-form-item label="用户名" prop="name">
                        <el-col :span="20">
                            <span>{{ruleForm.name}}</span>
<!--                            <el-input v-model="ruleForm.name" clearable></el-input>-->
                        </el-col>
                    </el-form-item>
                    <el-form-item label="工号" prop="empNo">
                        <el-col :span="20">
                            <span>{{ruleForm.empNo}}</span>
                        </el-col>
                    </el-form-item>
                    <el-form-item label="性别" prop="gender">
                        <el-radio-group v-model="ruleForm.gender">
                            <el-radio :label="1">男</el-radio>
                            <el-radio :label="2">女</el-radio>
                        </el-radio-group>
                    </el-form-item>

                    <el-form-item label="手机" prop="mobile">
                        <el-col :span="20">
                            <el-input v-model="ruleForm.mobile" clearable></el-input>
                        </el-col>
                    </el-form-item>

                    <el-form-item label="邮箱" prop="email">
                        <el-col :span="20">
                            <el-input v-model="ruleForm.email" clearable></el-input>
                        </el-col>
                    </el-form-item>
                </div>
            </el-tab-pane>

            <el-tab-pane label="头像修改">
                <div v-if="type==1">
                    <el-upload
                            class="avatar-uploader"
                            :action="actionUrl"
                            :show-file-list="false"
                            :headers="importHeaders"
                            :on-success="handleAvatarSuccess"
                            :before-upload="beforeAvatarUpload">
                        <img v-if="imageUrl" :src="imageUrl" class="avatar">
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                </div>

            </el-tab-pane>

            <el-tab-pane label="修改密码">
                <div v-if="type==2">
                    <el-form-item label="旧密码" prop="oldPassword">
                        <el-col :span="20">
                            <el-input v-model="ruleForm.oldPassword" show-password></el-input>
                        </el-col>
                    </el-form-item>
                    <el-form-item label="新密码" prop="newPassword">
                        <el-col :span="20">
                            <el-input v-model="ruleForm.newPassword" show-password></el-input>
                        </el-col>
                    </el-form-item>
                    <el-form-item label="确认密码" prop="confirmPassword">
                        <el-col :span="20">
                            <el-input v-model="ruleForm.confirmPassword" show-password></el-input>
                        </el-col>
                    </el-form-item>
                </div>

            </el-tab-pane>

            <el-form-item class="el-form-submitForm">
                <el-button type="primary" @click="submit('ruleForm')">保存</el-button>
                <el-button @click="resetForm('ruleForm')">取消</el-button>
            </el-form-item>

        </el-tabs>
    </el-form>
</template>

<script>
    import {getUpdatePeronal, getUploadImg, resetUserPwd,getSign} from '@/api/system'
    import {getToken} from '@/utils/auth'
    import {baseURL} from '@/settings'
    import {encrypt} from '@/utils/validate'
    export default {
        name: "dialogLogin",
        props: ['userArr'],
        data() {
            return {
                importHeaders:{
                    token: getToken()
                },
                loginName: this.userArr.username,
                imgSrc: '',
                type: 0,
                actionUrl:baseURL+'/sys/user/uploadImg',
                imageUrl: '',
                ruleForm: {
                    name: this.userArr.name,//用户名
                    empNo: this.userArr.empNo,//工号
                    email: this.userArr.email,//邮箱
                    gender: this.userArr.sex == 1 ? 1 : 2,//性别
                    mobile: this.userArr.mobile,//手机号码
                    oldPassword: '',//旧密码
                    newPassword: '',//新密码
                    confirmPassword: '',//确认密码
                },
                rules: {
                    // name: [
                    //     {required: true, message: '请输入用户名', trigger: 'blur'},
                    //     {min: 1, max: 15, message: '长度在 1 到 15 个字符', trigger: 'blur'}
                    // ],
                    gender: [
                        {required: true, message: '请勾选性别', trigger: 'blur'},
                    ],
                    mobile: [
                        {required: true, message: '请输入手机号码', trigger: 'blur'},
                    ],
                    date1: [
                        {required: true, message: '请选择生日', trigger: 'blur'},
                    ],
                    email: [
                        {required: true, message: '请输入邮箱', trigger: 'change'}
                    ],
                    gender: [
                        {required: true, message: '请选择性别', trigger: 'change'}
                    ],
                    oldPassword: [
                        {required: true, message: '请输入旧密码', trigger: 'change'}
                    ],
                    newPassword: [
                        {required: true, message: '请输入新密码', trigger: 'change'}
                    ],
                    confirmPassword: [
                        {required: true, message: '请输入确认密码', trigger: 'change'}
                    ],
                },
                p(s) {
                    return s < 10 ? '0' + s : s
                },
                sign: '',
            };
        },
        async created() {
            console.log(this.userArr.birth, '******');
        },
        watch: {
            userArr() {
                this.$refs['ruleForm'].resetFields();
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
            isTab() {
                this.type = this.$refs.borderTab.currentName
            },
            async submit(formName) {
                await this.getSign()
                this.$refs[formName].validate(async (valid) => {
                    if (valid) {
                        if (this.type == 0) {
                            if (!(/^1[3456789]\d{9}$/.test(this.ruleForm.mobile))) {
                                this.$message.error("手机号码有误，请重填");
                                return false;
                            }
                            if (!/^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{2,6}$/.test(this.ruleForm.email)) {
                                this.$message.error("邮箱有误，请重填");
                                return false;
                            }

                            if (this.ruleForm.date1) {
                                const d = new Date(this.ruleForm.date1)
                                this.ruleForm.date1 = d.getFullYear() + '-' + this.p((d.getMonth() + 1)) + '-' + this.p(d.getDate())
                            }
                            let params = {
                                name: this.ruleForm.name,//用户名
                                email: this.ruleForm.email,//邮箱
                                birth: this.ruleForm.date1 ? this.ruleForm.date1 : '',//生日
                                sex: this.ruleForm.gender,//性别
                                mobile: this.ruleForm.mobile,//手机号码
                                userId: this.$store.state.user.userInfo.userId
                            }
                            console.log(params);
                            let res = await getUpdatePeronal(params)
                            console.log(res);
                            if (res.code == 0) {
                                this.$message.success(res.msg);
                                this.$emit('submit')
                                // this.$router.go(0)
                            } else {
                                this.$message.error(res.msg);
                            }
                        }
                        else if (this.type == 1) {
                            console.log(this.imgSrc, '@#@#@##@#');
                            let params = {
                                avatar_file: this.imgSrc
                            }
                            let res = await getUploadImg(params)
                            if (res.code == 0) {
                                this.$message.success(res.msg);
                                this.$emit('submit')
                            } else {
                                this.$message.error(res.msg);
                            }

                        } else if (this.type == 2) {
                            if (this.ruleForm.newPassword != this.ruleForm.confirmPassword) {
                                this.$message.error("新密码和确认密码不一样，请重填");
                                return false;
                            }
                            let params = {
                                pwdOld: encrypt(this.ruleForm.oldPassword, this.sign),//旧密码
                                pwdNew: encrypt(this.ruleForm.newPassword, this.sign),//新密码
                                'userDO.userId': this.$store.state.user.userInfo.userId
                            }
                            let res = await resetUserPwd(params)
                            if (res.code == 0) {
                                this.$message.success(res.msg);
                                this.$emit('submit')
                            }
                        }
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
                this.$emit('cancel')
            },
            handleAvatarSuccess(res, file) {
                this.imgSrc = file.raw
                this.imageUrl = URL.createObjectURL(file.raw);
            },
            beforeAvatarUpload(file) {
                console.log(file.type,'******');
                const isJPG = file.type === 'image/jpeg';
                const isLt2M = file.size / 1024 / 1024 < 2;

                if (!isJPG) {
                    this.$message.error('上传头像图片只能是 JPG 格式!');
                }
                return isLt2M;
            }
        }
    }
</script>

<style scoped>

</style>
<style>
    .avatar-uploader .el-upload {
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
    }

    .avatar-uploader .el-upload:hover {
        border-color: #409EFF;
    }

    .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 178px;
        height: 178px;
        line-height: 178px;
        text-align: center;
    }

    .avatar {
        width: 178px;
        height: 178px;
        display: block;
    }

    .el-form-submitForm {
        margin-top: 20px;
    }
</style>
