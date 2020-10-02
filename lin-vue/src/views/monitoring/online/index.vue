<template>
    <div class="online">
        <div class="planIndex-btn">
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-button type="danger" size="small" icon="el-icon-delete" @click="deleteAll">删除</el-button>
                </el-col>
                <el-col :span="12" class="el-col-query">
                    <el-input
                            placeholder="用户名"
                            v-model="paramsList.username"
                            clearable>
                    </el-input>
                    <el-button class="buttom-btn buttom-query" type="primary" @click="logList">查询</el-button>
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
                        <!--删除-->
                        <div v-if="item.operation" class="deleteItem">
                            <el-button type="danger" icon="el-icon-delete" size="mini"
                                       @click="deleteItem(scope.row.id)"></el-button>
                        </div>
                        <div v-else-if="item.prop ==='gmtCreate'">
                            {{scope.row[item.prop] | formatDate}}
                        </div>
                        <div v-else>{{scope.row[item.prop]}}</div>
                    </template>
                </el-table-column>
            </el-table>
            <!--分页-->
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
    </div>
</template>

<script>

    import {logList, logBatchRemove, logRemove} from '@/api/dictionary'
    import {formatDate} from '@/filters'

    export default {
        name: 'online',
        components: {},
        data() {
            return {
                tableLabel: [
                    {label: '序号', width: '', prop: 'index'},
                    // {label: '用户Id', width: '', prop: 'userId'},
                    {label: '用户名', width: '', prop: 'username'},
                    {label: '操作', width: '', prop: 'operation'},
                    {label: '用时', width: '', prop: 'time'},
                    {label: '方法', width: '350', prop: 'method'},
                    {label: '参数', width: '350', prop: 'params'},
                    {label: 'IP地址', width: '', prop: 'ip'},
                    {label: '创建时间', width: '180', prop: 'gmtCreate'},
                    {label: '操作', width: '', operation: true},
                ],
                tableData: [],
                // 列表与筛选数据
                paramsList: {
                    pageSize: 10,//每页条数
                    pageNum: 1,//当前页
                    id: '',//日志 ID
                    username: ''//用户名
                },
                total: 0,//总条数
                ids: '',
            }
        },
        filters: {
            formatDate
        },
        created() {
            this.logList()
        },
        methods: {
            // 数据列表
            async logList() {
                let res = await logList(this.paramsList)

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
                this.logList()
                console.log(`每页 ${val} 条`);
            },
            // 选择当前页
            handleCurrentChange(val) {
                this.paramsList.pageNum = val
                this.logList()
                console.log(`当前页: ${val}`);
            },
            // 批量删除
            deleteAll() {
                if (this.ids.length < 1) {
                    this.$message.error('请勾选删除数据');
                    return
                }
                this.$confirm('确认要删除该数据？', '信息', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                }).then(async () => {
                    let params = {
                        ids: this.ids
                    }
                    let res = await logBatchRemove(params)
                    if (res.code == 0) {
                        this.$message.success(res.msg);
                        this.logList()
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
            // 开启与表单删除
            deleteItem(id) {
                this.$confirm('确认要删除任务吗？', '信息', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                }).then(async () => {
                    let params = {
                        id: id
                    }
                    let res = await logRemove(params)
                    if (res.code == 0) {
                        this.$message.success(res.msg);
                        this.logList()
                    } else {
                        this.$message.error(res.msg);
                    }
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
        }
    }
</script>
<style lang="scss" scoped>
    .online {
        padding: 20px;
        .planIndex-btn {
            padding: 10px 0;
            border-bottom: 1px solid #dfe6ec;
        }
        .block {
            margin-top: 10px;
            text-align: right;
        }
    }
</style>
<style lang="scss">
    .online {
        .el-col-query {
            text-align: right;
            .el-input--medium {
                width: 230px;
                display: inline-block;
            }
        }

    }
</style>
