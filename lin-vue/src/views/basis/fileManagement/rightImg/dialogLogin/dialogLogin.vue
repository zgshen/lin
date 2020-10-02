<template>
    <el-form ref="ruleForm" label-width="100px" class="demo-ruleForm">
        <div>
            <el-upload
                    class="avatar-uploader"
                    :action="actionUrl"
                    :show-file-list="false"
                    :on-success="handleAvatarSuccess"
                    :headers="importHeaders"
                    :before-upload="beforeAvatarUpload">
                <img v-if="imageUrl" :src="imageUrl" class="avatar">
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
        </div>
        <el-form-item class="el-form-submitForm">
            <el-button type="primary" @click="submitForm()">保存</el-button>
            <el-button @click="resetForm()">取消</el-button>
        </el-form-item>

    </el-form>
</template>

<script>
    import {imgUpload} from '@/api/dictionary'
    import {baseURL} from '@/settings'
    import {getToken} from '@/utils/auth'
    export default {
        name: "dialogLogin",
        data() {
            return {
                actionUrl: baseURL+'/sys/user/uploadImg',
                imgSrc: '',
                imageUrl: '',
                importHeaders:{
                    token: getToken()
                }
            };
        },
        methods: {
            async submitForm() {
                let params = {
                    file: this.imgSrc
                }
                let res = await imgUpload(params)
                if (res.code == 0) {
                    this.$message.success(res.msg);
                    this.$emit('submit')
                } else {
                    this.$message.error(res.msg);
                }
            },
            resetForm(formName) {
                this.$emit('cancel')
            },
            handleAvatarSuccess(res, file) {
                this.imgSrc = file.raw
                this.imageUrl = URL.createObjectURL(file.raw);
            },
            beforeAvatarUpload(file) {
                const isLt2M = file.size / 1024 / 1024 < 2;
                if (!isLt2M) {
                    this.$message.error('上传文件大小不能超过 2MB!');
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
