<template>
    <div class="basisData">
        <div class="planIndex-btn">
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-button type="success" size="small" icon="el-icon-plus" @click="addAll">添加</el-button>
                    <el-button type="danger" size="small" icon="el-icon-delete" @click="batchRemove">删除</el-button>
                </el-col>
                <el-col :span="12" class="el-col-query">
                    <el-select v-model="paramsList.type" clearable filterable placeholder="请选择">
                        <el-option
                                v-for="(item,k) in arrData"
                                :Key="k"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>
                    <el-button class="buttom-btn buttom-query" type="primary" @click="screening">查询</el-button>
                </el-col>
            </el-row>
        </div>


        <div class="tablebar">
            <el-table
                    ref="multipleTable"
                    :data="tableData"
                    size="small"
                    cell-class-name="cellName"
                    header-cell-class-name="headerTable"
                    @selection-change="handleSelectionChange"
                    :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
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
                            <el-button type="primary" icon="el-icon-edit" size="mini"
                                       @click="editor(scope.row)"></el-button>
                            <el-button type="danger" icon="el-icon-delete" size="mini"
                                       @click="itemRemove(scope.row.id)"></el-button>
                            <el-button type="success" icon="el-icon-plus" size="mini"
                                       @click="addItem(scope.row)"></el-button>
                        </div>
                        <div>{{scope.row[item.prop]}}</div>
                    </template>
                </el-table-column>
            </el-table>
        </div>
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
        <!--弹出框-->
        <el-dialog :title="title" :visible.sync="dialogFormVisible" @close="resetForm('ruleForm')">
            <el-form :model="form" :rules="rules" ref="ruleForm">
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="标签名：" prop="name" :label-width="formLabelWidth">
                            <el-input v-model="form.name"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="数据值：" prop="value" :label-width="formLabelWidth">
                            <el-input v-model="form.value"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="类型：" prop="type" :label-width="formLabelWidth">
                            <el-input v-model="form.type"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="描述：" prop="description" :label-width="formLabelWidth">
                            <el-input v-model="form.description"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="排序（升序）：" :label-width="formLabelWidth">
                            <el-input v-model="form.sort"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="备注信息：" :label-width="formLabelWidth">
                            <el-input v-model="form.remarks"></el-input>
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
    import {dictionaryList, dictType, dictSave, batchRemove, itemRemove, itemUpdate} from '@/api/dictionary'

    export default {
        name: 'basisData',
        components: {
            RouterTable
        },
        data() {
            return {
                rules: {
                    name: [
                        {required: true, message: '请输入标签名', trigger: 'blur'},
                    ],
                    value: [
                        {required: true, message: '请输入数据值', trigger: 'blur'},
                    ],
                    type: [
                        {required: true, message: '请输入类型', trigger: 'blur'},
                    ],
                    description: [
                        {required: true, message: '请输入描述', trigger: 'blur'},
                    ],
                },
                tableLabel: [
                    {label: '序号', width: '', prop: 'index'},
                    {label: '标签名', width: '', prop: 'name'},
                    {label: '数据值', width: '', prop: 'value'},
                    {label: '类型', width: '', prop: 'type'},
                    {label: '描述', width: '', prop: 'description'},
                    {label: '备注信息', width: '', prop: 'remarks'},
                    {label: '操作', width: '', isOpera: true},
                ],
                tableData: [],//列表数据
                arrData: [],//下拉数据
                currentPage4: 4,//当前页
                dialogFormVisible: false,
                form: {
                    name: '',//标签名
                    value: '',//数据值
                    type: '',//类型
                    description: '',//描述
                    sort: '',//排序（升序）
                    remarks: '',//备注信息
                    id: ''
                },
                formLabelWidth: '120px',
                textarea2: '',// w文本域
                title: '',
                // 列表与筛选数据
                paramsList: {
                    pageSize: 10,//每页条数
                    pageNum: 1,//当前页
                    delFlag: '',
                    description: '',
                    id: '',
                    name: '',
                    parentId: '',
                    sort: '',
                    type: '',//类别筛选值
                    value: '',
                },
                total: 0,//总条数
                ids: '',//批量删除id
            }
        },
        created() {
            this.dictionaryList()
        },
        mounted() {
            this.dictType()
        },
        methods: {
            // 提交
            async submit(formName) {
                this.$refs[formName].validate(async (valid) => {
                    console.log(valid);
                    if (valid) {
                        if (!this.form.id) {
                            let res = await dictSave(this.form)
                            if (res.code == 0) {
                                this.$message.success(res.msg);
                                this.dictionaryList()
                                this.dialogFormVisible = false
                            } else {
                                this.$message.error(res.msg);
                            }
                        } else {
                            let res = await itemUpdate(this.form)
                            if (res.code == 0) {
                                this.$message.success(res.msg);
                                this.dictionaryList()
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
            // 选择每页多少条
            handleSizeChange(val) {
                this.paramsList.pageSize = val
                this.dictionaryList()
                console.log(`每页 ${val} 条`);
            },
            // 选择当前页
            handleCurrentChange(val) {
                this.paramsList.pageNum = val
                this.pageSize = val
                this.dictionaryList()
                console.log(`当前页: ${val}`);
            },
            // 查询
            async screening() {
                this.dictionaryList()
            },
            // 数据列表
            async dictionaryList() {
                let res = await dictionaryList(this.paramsList)
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
            // 下拉数据
            async dictType() {
                let res = await dictType(this.params)
                if (res.code == 0) {
                    res.data.forEach((v, i) => {
                        let list = {
                            value: v.type,
                            label: v.description
                        }
                        this.arrData.push(list)
                    })
                } else {
                    this.$message.error(res.msg);
                }
            },
            // 编辑
            editor(item) {
                console.log(item);
                this.form.name = item.name
                this.form.value = item.value
                this.form.type = item.type
                this.form.description = item.description
                this.form.sort = item.sort
                this.form.remarks = item.remarks
                this.form.id = item.id
                this.dialogFormVisible = true
                this.title = '编辑'
            },
            // 添加
            addAll() {
                this.form = {
                    name: '',//标签名
                    value: '',//数据值
                    type: '',//类型
                    description: '',//描述
                    sort: '',//排序（升序）
                    remarks: '',//备注信息
                    id: ''
                },
                    this.title = '添加'
                this.dialogFormVisible = true
            },
            addItem(row) {
                this.form = {
                    name: '',//标签名
                    value: '',//数据值
                    type: row.type,//类型
                    description: row.description,//描述
                    sort: '',//排序（升序）
                    remarks: '',//备注信息
                    id: ''
                },
                    this.title = '添加'
                this.dialogFormVisible = true
            },
            // 删除
            itemRemove(id) {
                this.$confirm('确认要删除该数据吗？', '信息', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                }).then(async () => {
                    let params = {
                        id: id
                    }
                    let res = await itemRemove(params)
                    if (res.code == 0) {
                        this.$message.success(res.msg);
                        this.dictionaryList()
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
            // 批量删除
            batchRemove() {
                if (this.ids.length < 1) {
                    this.$message.error('请勾选删除数据');
                    return
                }
                this.$confirm('确认要删除该数据吗？', '信息', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                }).then(async () => {
                    let params = {
                        ids: this.ids
                    }
                    let res = await batchRemove(params)
                    if (res.code == 0) {
                        this.$message.success(res.msg);
                        this.dictionaryList()
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
    .basisData {
        .planIndex-btn {
            padding: 10px 0;
            border-bottom: 1px solid #dfe6ec;
        }
    }
</style>
<style lang="scss">
    .basisData {
        padding: 20px;
        .el-col-query {
            text-align: right;
            .el-input--medium {
                width: 230px;
                display: inline-block;
            }
        }
        .block {
            margin-top: 10px;
            text-align: right;
        }
    }
</style>
