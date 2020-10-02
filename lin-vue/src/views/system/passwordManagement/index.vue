<template>
    <div class="passwordManagement">
        <div class="toolbar">
            <div class="toolbar-left">
                <el-button type="primary" size="small" icon="el-icon-edit" @click="clickEdit" v-if="isDisabled">编辑
                </el-button>
                <el-button type="info" size="small" icon="el-icon-refresh" @click="clickUndo" v-if="!isDisabled">撤销
                </el-button>
                <el-button type="success" size="small" icon="el-icon-success" v-if="!isDisabled" @click="save">保存
                </el-button>

            </div>
        </div>
        <div class="passwordManagement-content-item">
            <div class="item-left">
                用户密码有效期
            </div>
            <div class="item-right">
                <el-select v-model="form.expirationDate" filterable placeholder="请选择" ref="itemSelect"
                           :disabled="isDisabled">
                    <el-option
                            v-for="item in options"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>
            </div>
        </div>

        <div class="passwordManagement-content-item">
            <div class="item-left">
                强制密码历史
            </div>
            <div class="item-right">
                <el-select v-model="form.passwordHistory" filterable placeholder="请选择" ref="itemSelect"
                           :disabled="isDisabled">
                    <el-option
                            v-for="item in options1"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>
            </div>
        </div>

        <div class="passwordManagement-content-item">
            <div class="item-left">
                密码最小长度
            </div>
            <div class="item-right">
                <el-select v-model="form.minLength" filterable placeholder="请选择" ref="itemSelect"
                           :disabled="isDisabled">
                    <el-option
                            v-for="item in options2"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>
            </div>
        </div>
        <div class="passwordManagement-content-item">
            <div class="item-left">
                密码复杂性要求
            </div>
            <div class="item-right">
                <el-select v-model="form.complexity" filterable placeholder="请选择" ref="itemSelect"
                           :disabled="isDisabled">
                    <el-option
                            v-for="item in options3"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>
            </div>
        </div>
        <div class="passwordManagement-content-item">
            <div class="item-left">
                最大无效登录尝试次数
            </div>
            <div class="item-right">
                <el-select v-model="form.retryTime" filterable placeholder="请选择" ref="itemSelect"
                           :disabled="isDisabled">
                    <el-option
                            v-for="item in options4"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>
            </div>
        </div>
        <div class="passwordManagement-content-item">
            <div class="item-left">
                锁定有效时间
            </div>
            <div class="item-right">
                <el-select v-model="form.lockingTime" filterable placeholder="请选择" ref="itemSelect"
                           :disabled="isDisabled">
                    <el-option
                            v-for="item in options5"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>
            </div>
        </div>

    </div>
</template>

<script>
    import {getPasswordStrategy, passwordStrategySave} from '@/api/system'

    export default {
        name: "passwordManagement",
        data() {
            return {
                options: [
                    {
                        value: '30',
                        label: '30天'
                    }, {
                        value: '60',
                        label: '60天'
                    }, {
                        value: '90',
                        label: '90天'
                    }, {
                        value: '180',
                        label: '180天'
                    }, {
                        value: '365',
                        label: '一年'
                    }, {
                        value: '0',
                        label: '永不过期'
                    }
                ],
                options1: [
                    {
                        value: '0',
                        label: '不记住任何密码'
                    }, {
                        value: '1',
                        label: '记住1个密码'
                    }, {
                        value: '2',
                        label: '记住2个密码'
                    }, {
                        value: '3',
                        label: '记住3个密码'
                    }, {
                        value: '4',
                        label: '记住4个密码'
                    }, {
                        value: '5',
                        label: '记住5个密码'
                    }, {
                        value: '6',
                        label: '记住6个密码'
                    }, {
                        value: '7',
                        label: '记住7个密码'
                    }, {
                        value: '8',
                        label: '记住8个密码'
                    }, {
                        value: '9',
                        label: '记住9个密码'
                    }, {
                        value: '10',
                        label: '记住10个密码'
                    }
                ],
                options2: [
                    {
                        value: '5',
                        label: '5个字符'
                    }, {
                        value: '6',
                        label: '6个字符'
                    }, {
                        value: '7',
                        label: '7个字符'
                    }, {
                        value: '8',
                        label: '8个字符'
                    }, {
                        value: '9',
                        label: '9个字符'
                    }, {
                        value: '10',
                        label: '10个字符'
                    },
                ],
                options3: [
                    {
                        value: '1',
                        label: '必须混合使用字母和数字'
                    }, {
                        value: '2',
                        label: '必须混合使用大小写字母和数字'
                    }, {
                        value: '3',
                        label: '必须混合使用大小写字母和数字和特殊字符'
                    }, {
                        value: '0',
                        label: '无限制'
                    }
                ],
                options4: [
                    {
                        value: '1',
                        label: '1次'
                    }, {
                        value: '2',
                        label: '2次'
                    }, {
                        value: '3',
                        label: '3次'
                    }, {
                        value: '4',
                        label: '4次'
                    }, {
                        value: '5',
                        label: '5次'
                    }, {
                        value: '6',
                        label: '6次'
                    }, {
                        value: '7',
                        label: '7次'
                    }, {
                        value: '8',
                        label: '8次'
                    }, {
                        value: '9',
                        label: '9次'
                    }, {
                        value: '10',
                        label: '10次'
                    }, {
                        value: '0',
                        label: '无限制'
                    }
                ],
                options5: [
                    {
                        value: '15',
                        label: '15分钟'
                    }, {
                        value: '30',
                        label: '30分钟'
                    }, {
                        value: '60',
                        label: '60分钟'
                    }, {
                        value: '0',
                        label: '始终(必须管理员重置)'
                    },
                ],
                form: {
                    expirationDate: '',//用户密码有效期
                    passwordHistory: '',//密码最小长度
                    minLength: '',//密码最小长度
                    complexity: '',//密码复杂程度
                    retryTime: '',//  最大无效登录尝试次数
                    lockingTime: '',//锁定有效期间
                    id: ''
                },
                isDisabled: true
            }
        },
        created() {
            this.getPasswordStrategy()
        },
        methods: {
            // 保存
            async save() {
                let res = await passwordStrategySave(this.form)
                if (res.code === 0) {
                    this.isDisabled = !this.isDisabled
                    this.$message.success(res.msg);
                } else {
                    this.$message.error(res.msg);
                }
            },
            clickEdit() {
                this.isDisabled = !this.isDisabled
            },
            clickUndo() {
                this.isDisabled = !this.isDisabled
                this.getPasswordStrategy()
            },
            async getPasswordStrategy() {
                console.log(123);
                let res = await getPasswordStrategy()
                if (res.code === 0) {
                    console.log(res, '&*&*&*&&*&*&*&');
                    this.form.expirationDate = res.data.expirationDate.toString()
                    this.form.passwordHistory = res.data.passwordHistory.toString()
                    this.form.retryTime = res.data.retryTime.toString()
                    this.form.lockingTime = res.data.lockingTime.toString()
                    this.form.complexity = res.data.complexity.toString()
                    this.form.minLength = res.data.minLength.toString()
                    this.form.id = res.data.id
                }
            },
        }
    }
</script>

<style lang="scss" scoped>
    .passwordManagement {
        height: 100%;
        padding: 10px 30px;
        position: relative;
        .passwordManagement-content-item {
            height: 60px;
            border-bottom: 1px solid #dfe6ec;
            .item-left {
                width: 200px;
                height: 60px;
                line-height: 60px;
                float: left;
                text-align: right;
                font-size: 14px;
                color: #606266;
            }
            .item-right {
                padding-top: 12px;
                padding-left: 230px;
            }
        }

    }
</style>
<style lang="scss">
    .passwordManagement {
        .item-right {
            .el-select {
                width: 400px;
            }
        }
    }
</style>
