<template>
    <div class="planIndex">
        <div class="planIndex-btn">
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-button type="success" size="small" icon="el-icon-plus" @click="addAll">添加</el-button>
                    <el-button type="danger" size="small" icon="el-icon-delete" @click="deleteAll">删除</el-button>
                </el-col>
                <el-col :span="12" class="el-col-query">
                    <el-input
                            placeholder="请输入任务名称/任务分组"
                            v-model="paramsList.searchText"
                            clearable>
                    </el-input>
                    <el-button class="buttom-btn buttom-query" type="primary" @click="queryAll">查询</el-button>
                </el-col>
            </el-row>
        </div>

        <div class="tableLabel">
            <el-table
                    ref="multipleTable"
                    :data="tableData"
                    size="small"
                    cell-class-name="cellName"
                    header-cell-class-name="headerTable"
                    @selection-change="handleSelectionChange"
                    style="width: 100%">
                <el-table-column
                        type="selection"
                        width="55">
                </el-table-column>
                <el-table-column
                        v-for="(item, index) in tableLabel"
                        :key="index"
                        :prop="item.prop"
                        :width="item.width"
                        :label="item.label">
                    <template slot-scope="scope">
                        <!--编辑与删除-->
                        <div v-if="item.isOpera">
                            <el-button type="primary" size="mini" icon="el-icon-edit"
                                       @click="editor(scope.row)"></el-button>
                            <el-button type="danger" size="mini" icon="el-icon-delete"
                                       @click="deleteItem(scope.row.id)"></el-button>
                        </div>
                        <!--开启与关闭-->
                        <div v-if="item.open">
                            <el-button type="primary" size="mini" v-if="scope.row.jobStatus == '0'"
                                       @click="open(scope.row.id,1)">开启
                            </el-button>
                            <el-button type="danger" size="mini" v-else @click="open(scope.row.id,2)">关闭</el-button>
                        </div>
                        <div v-else>{{scope.row[item.prop]}}</div>
                    </template>
                </el-table-column>
            </el-table>
            <!--分页-->
            <div class="block">
                <el-pagination
                        @size-change="handleSizeChange"
                        @current-change="handleCurrentChange"
                        :current-page="paramsList.pageNum"
                        :page-sizes="[10, 20, 30, 40,50,100]"
                        :page-size="paramsList.pageSize"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="total">
                </el-pagination>
            </div>
        </div>
        <!--弹出框-->
        <el-dialog @close="closeClick" v-el-drag-dialog :title="title" :visible.sync="dialogFormVisible"
                   :modal-append-to-body="false">
            <el-form :model="form" :rules="rules" ref="ruleForm" label-width="100px">
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="任务名称：" prop="jobName" :label-width="formLabelWidth">
                            <el-input v-model="form.jobName" autocomplete="off"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="任务分组：" prop="jobGroup" :label-width="formLabelWidth">
                            <el-input v-model="form.jobGroup" autocomplete="off"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="任务类：" prop="beanClass" :label-width="formLabelWidth">
                            <el-input v-model="form.beanClass" autocomplete="off"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="cron表达式：" prop="cronExpression" :label-width="formLabelWidth">
                            <el-input v-model="form.cronExpression" autocomplete="off"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="方法名：" prop="methodName" :label-width="formLabelWidth">
                            <el-input v-model="form.methodName" autocomplete="off"></el-input>
                        </el-form-item>
                    </el-col>

                    <el-col :span="24">
                        <el-form-item label="任务描述：" prop="description" :label-width="formLabelWidth">
                            <el-input
                                    type="textarea"
                                    :autosize="{ minRows: 2, maxRows: 4}"
                                    placeholder="请输入内容"
                                    v-model="form.description">
                            </el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="resetForm('ruleForm')">取 消</el-button>
                <el-button type="primary" @click="submit('ruleForm')">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import RouterTable from '../../component/tab/index'
    import elDragDialog from '@/directive/el-drag-dialog' // base on element-ui
    import {jobList, jobSave, jobRemove, jobUpdate, jobItemRemove, jobChangeJobStatus} from '@/api/dictionary'

    export default {
        name: 'planIndex',
        components: {
            RouterTable
        },
        directives: {elDragDialog},
        data() {
            return {
                tableLabel: [
                    {label: '序号', width: '', prop: 'index'},
                    {label: '任务名称', width: '', prop: 'jobName'},
                    {label: '任务分组', width: '', prop: 'jobGroup'},
                    {label: '任务类', width: '', prop: 'beanClass'},
                    {label: 'cron表达式', width: '', prop: 'cronExpression'},
                    {label: '停起操作', width: '', prop: 'type', open: true},
                    {label: '操作', width: '', isOpera: true},
                ],
                tableData: [],//列表数据
                input: '',
                dialogFormVisible: false,
                form: {
                    jobName: '',//任务名
                    jobGroup: '',//任务分组
                    methodName: '',//方法名
                    cronExpression: '',//cron表达式
                    description: '',//任务描述
                    beanClass: '',//任务类
                    id: '',
                },
                rules: {
                    jobName: [
                        {required: true, message: '请输入任务名', trigger: 'blur'},
                        {min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur'}
                    ],
                    jobGroup: [
                        {required: true, message: '请输入任务分组', trigger: 'blur'},
                        {min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur'}
                    ],
                    methodName: [
                        {required: true, message: '请输入方法名', trigger: 'blur'},
                        {min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur'}
                    ],
                    cronExpression: [
                        {required: true, message: '请输入cron表达式', trigger: 'blur'},
                        {min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur'}
                    ],
                    description: [
                        {required: true, message: '请输入任务描述', trigger: 'blur'},
                        {min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur'}
                    ],
                    beanClass: [
                        {required: true, message: '请输入任务类', trigger: 'blur'},
                        {min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur'}
                    ],

                },
                formLabelWidth: '120px',
                textarea2: '',// w文本域
                title: '',
                // 列表与筛选数据
                paramsList: {
                    pageSize: 10,//每页条数
                    pageNum: 1,//当前页
                    searchText: '',//任务调用的方法名
                },
                total: 0,//总条数
                ids: '',//勾选中的数据
            }
        },
        created() {
            this.jobList()
        },
        methods: {
            // 查询数据
            queryAll() {
                this.jobList()
            },
            // 数据列表
            async jobList() {
                let res = await jobList(this.paramsList)
                console.log(res, '@@@@');
                if (res.code == 0) {

                    this.tableData = res.data.list
                    this.total = parseInt(res.data.total)
                    this.tableData.forEach((v, i) => {
                        v.index = i + 1
                    })
                } else {
                    this.$message.error(res.msg);
                }
            },
            // 选择每页多少条
            handleSizeChange(val) {
                this.paramsList.pageSize = val
                this.jobList()
                console.log(`每页 ${val} 条`);
            },
            // 选择当前页
            handleCurrentChange(val) {
                this.paramsList.pageNum = val
                this.jobList()
                console.log(`当前页: ${val}`);
            },
            // 提交
            submit(formName) {
                this.$refs[formName].validate(async (valid) => {
                    if (valid) {
                        if (!this.form.id) {
                            let res = await jobSave(this.form)
                            if (res.code == 0) {
                                this.$message.success(res.msg);
                                this.jobList()
                                this.dialogFormVisible = false
                            } else {
                                this.$message.error(res.msg);
                            }
                        } else {
                            let res = await jobUpdate(this.form)
                            if (res.code == 0) {
                                this.$message.success(res.msg);
                                this.jobList()
                                this.dialogFormVisible = false
                            } else {
                                this.$message.error(res.msg);
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
                this.dialogFormVisible = false
            },
            closeClick() {
                console.log(123);
                this.$refs['ruleForm'].resetFields();
            },
            // 编辑
            editor(val) {
                this.dialogFormVisible = true
                this.title = '编辑'
                this.form = {
                    jobName: val.jobName,//任务名
                    jobGroup: val.jobGroup,//任务分组
                    methodName: val.methodName,//方法名
                    cronExpression: val.cronExpression,//cron表达式
                    description: val.description,//任务描述
                    beanClass: val.beanClass,//任务类
                    id: val.id
                }
            },
            // 添加
            addAll() {
                this.form = {
                    jobName: '',//任务名
                    jobGroup: '',//任务分组
                    methodName: '',//方法名
                    cronExpression: '',//cron表达式
                    description: '',//任务描述
                    beanClass: '',//任务类
                    id: ''
                }
                this.title = '添加'
                this.dialogFormVisible = true
            },
            // 开启与表单删除
            open(id, type) {
                let name = ""
                if (type == 1) {
                    name = '确认要开启任务吗？'
                } else {
                    name = '确认要停止任务吗？'
                }
                this.$confirm(name, '信息', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                }).then(async () => {
                    let params = {
                        id: id,
                        cmd: type == 1 ? 'start' : 'stop'
                    }
                    let res = await jobChangeJobStatus(params)
                    if (res.code == 0) {
                        this.$message.success(res.msg);
                        this.jobList()
                    } else {
                        this.$message.error(res.msg);
                    }
                }).catch(() => {

                });

            },
            // 选中
            handleSelectionChange(val) {
                this.ids = []
                val.forEach((v, i) => {
                    this.ids.push(v.id)
                })
            },
            async deleteItem(id) {
                this.$confirm('确认要删除任务吗？', '信息', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                }).then(async () => {
                    let params = {
                        id: id
                    }
                    let res = await jobItemRemove(params)
                    if (res.code == 0) {
                        this.$message.success(res.msg);
                        this.jobList()
                    } else {
                        this.$message.error(res.msg);
                    }
                }).catch(() => {

                });
            },
            // 删除全部
            async deleteAll() {
                if (this.ids.length < 1) {
                    this.$message.error('请勾选删除数据');
                    return
                }
                this.$confirm('确认要删除任务吗？', '信息', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                }).then(async () => {
                    let params = {
                        ids: this.ids
                    }
                    let res = await jobRemove(params)
                    if (res.code == 0) {
                        this.$message.success(res.msg);
                        this.jobList()
                    } else {
                        this.$message.error(res.msg);
                    }
                }).catch(() => {

                });
            }
        }
    }
</script>
<style lang="scss" scoped>
    .planIndex {
        .planIndex-btn {
            padding: 10px 0;
            border-bottom: 1px solid #dfe6ec;
        }
        .tableLabel {
            .block {
                margin-top: 10px;
                text-align: right;
            }
        }
    }
</style>
<style lang="scss">
    .planIndex {
        padding: 20px;
        .el-col-query {
            text-align: right;
            .el-input--medium {
                width: 230px;
                display: inline-block;
            }
        }
    }
</style>
