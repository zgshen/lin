<template>
    <div class="tab-item">
        <el-table
                ref="multipleTable"
                :data="tableData"
                size="small"
                cell-class-name="cellName"
                header-cell-class-name="headerTable"
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
                    <div>
                        <el-button type="primary" icon="el-icon-edit" circle v-if="item.isOpera"
                                   @click="editor(1)"></el-button>
                        <el-button type="danger" icon="el-icon-delete" circle v-if="item.isOpera"
                                   @click="open(1)"></el-button>
                        <el-button type="success" icon="el-icon-plus" circle v-if="item.isOpera"
                                   @click="editor(2)"></el-button>
                    </div>
                    <!--开启与关闭-->
                    <div v-if="item.open">
                        <el-button type="primary" v-if="scope.row[item.prop] == 1" @click="open(2)">开启</el-button>
                        <el-button type="danger" v-if="scope.row[item.prop] == 2" @click="open(3)">关闭</el-button>
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
                    :current-page="currentPage4"
                    :page-sizes="[100, 200, 300, 400]"
                    :page-size="100"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="400">
            </el-pagination>
        </div>
    </div>
</template>

<script>
    export default {
        name: "index",
        props: {
            tableData: {
                type: Array,
                default: []
            },
            tableLabel: {
                type: Array,
                default: () => {
                    return []
                }
            },
        },
        data() {
            return {
                currentPage4: 4
            };
        },
        methods: {
            handleSizeChange(val) {
                console.log(`每页 ${val} 条`);
            },
            handleCurrentChange(val) {
                console.log(`当前页: ${val}`);
            },
            // 编辑
            editor() {
                this.$emit('editor', true)
            },
            // 提示框
            open(type) {
                this.$emit('open', type)
            }
        },
    }
</script>

<style lang="scss" scoped>
    .tab-item {
        .el-button--primary {
            width: 46px;
            text-align: center;
            padding: 0;
            height: 22px;
            font-size: 12px;
            line-height: 20px;
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
        .block {
            margin-top: 10px;
            text-align: right;
        }
    }


</style>
<style>
    .headerTable {
        font-weight: bold;
        color: black;
        font-size: 14px;
    }

    .cellName {
        font-size: 14px;
    }
</style>
