<template>
    <div class="notes">
        <div class="planIndex-btn">
            <el-row :gutter="20">
                <!--<el-col :span="24" class="el-col-query">-->
                <!--<el-input-->
                <!--placeholder="请输入内容"-->
                <!--v-model="paramsList.name"-->
                <!--clearable>-->
                <!--</el-input>-->
                <!--<el-button class="buttom-btn buttom-query" type="primary" @click="onlinelist">查询</el-button>-->
                <!--</el-col>-->
            </el-row>
        </div>
        <div class="tableLabel">
            <el-table
                    ref="multipleTable"
                    :data="tableData"
                    size="small"
                    cell-class-name="cellName"
                    header-cell-class-name="headerTable"
                    style="width: 100%">
                <!--<el-table-column-->
                <!--type="selection"-->
                <!--width="55">-->
                <!--</el-table-column>-->
                <el-table-column
                        v-for="(item, index) in tableLabel"
                        :key="index"
                        :prop="item.prop"
                        :width="item.width"
                        :label="item.label">
                    <template slot-scope="scope">
                        <!--删除-->
                        <div v-if="item.operation">
                            <el-button type="danger" icon="el-icon-delete" circle
                                       @click="deleteItem(scope.row.id)"></el-button>
                        </div>
                        <div v-if="item.isState" class="online">
                            <!--在线与离线-->
                            <el-button v-if="scope.row.status == 'on_line'" type="primary">
                                在线
                            </el-button>
                            <el-button v-else type="danger">
                                离线
                            </el-button>
                        </div>
                        <div v-else-if="item.prop ==='startTimestamp' || item.prop ==='lastAccessTime'">
                            {{scope.row[item.prop] | formatDate}}
                        </div>
                        <div v-else>{{scope.row[item.prop]}}</div>
                    </template>
                </el-table-column>
            </el-table>
        </div>
    </div>
</template>

<script>
    import RouterTable from '../../component/tab/index'
    import {onlinelist, forceLogout} from '@/api/dictionary'
    import {formatDate} from '@/filters'

    export default {
        name: 'notes',
        components: {
            RouterTable
        },
        data() {
            return {
                tableLabel: [
                    {label: '序号', width: '', prop: 'index'},
                    {label: '用户名', width: '', prop: 'username'},
                    {label: '主机', width: '', prop: 'host'},
                    {label: '登录时间', width: '', prop: 'startTimestamp'},
                    {label: '最后访问时间', width: '', prop: 'lastAccessTime'},
                    {label: '过期时间', width: '', prop: 'timeout'},
                    {label: '状态', width: '', isState: true},
                    {label: '操作', width: '', operation: true},
                ],
                tableData: [],
                paramsList: {
                    name: ''
                }
            }
        },
        filters: {
            formatDate
        },
        created() {
            this.onlinelist()
        },
        methods: {
            // 数据列表
            async onlinelist() {
                let res = await onlinelist(this.paramsList)
                if (res.code == 0) {
                    this.tableData = res.data
                    this.tableData.forEach((v, i) => {
                        v.index = i + 1
                    })
                } else {
                    this.$message.error(res.msg);
                }
            },
            // 开启与表单删除
            deleteItem(id) {
                this.$confirm('确认要删除任务吗？', '信息', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                }).then(async () => {
                    let res = await forceLogout(id)
                    if (res.code == 0) {
                        this.$message.success(res.msg);
                        this.onlinelist()
                    } else {
                        this.$message.error(res.msg);
                    }
                }).catch(() => {

                });
            },
        }
    }
</script>
<style lang="scss" scoped>
    .notes {
        .planIndex-btn {
            padding: 10px 0;
            border-bottom: 1px solid #dfe6ec;
        }
    }
</style>
<style lang="scss">
    .notes {
        padding: 20px;
        .el-col-query {
            text-align: right;
            .el-input--medium {
                width: 230px;
                display: inline-block;
            }
        }
        .online {
            .el-button--primary {
                width: 46px;
                text-align: center;
                padding: 0;
                height: 22px;
                font-size: 12px;
                line-height: 20px;
            }
        }
        .el-button--danger {
            width: 46px;
            text-align: center;
            padding: 0;
            height: 22px;
            font-size: 12px;
            line-height: 20px;
        }
        .is-circle {
            width: 34px;
            text-align: center;
            padding: 0;
            height: 30px;
            font-size: 12px;
            line-height: 30px;
            border-radius: 6px;
        }
    }
</style>
