<template>
    <div class="rightImg">
        <!--图片区域-->
        <div class="tablebar">
            <div>
                <el-table
                        v-loading="listLoading"
                        :data="dataList"
                        cell-class-name="cellName"
                        header-cell-class-name="headerTable"
                        style="width: 100%">
                    <el-table-column
                            type="index"
                            label="序号"
                            width="50"
                            align="center">
                    </el-table-column>
                    <el-table-column
                            v-for="(item, index) in tableLabel"
                            v-if="item.checked"
                            :key="index"
                            :prop="item.prop"
                            :width="item.width"
                            :label="item.label"
                            :align="item.align">
                        <template slot-scope="scope">
                            <div v-if="item.prop ==='dirType'">
                                <el-tag v-if="scope.row.type == 0" type="warning"
                                        size="mini">图片
                                </el-tag>
                                <el-tag v-else-if="scope.row.type == 1" type="warning"
                                        size="mini">文档
                                </el-tag>
                                <el-tag v-else-if="scope.row.type == 2" type="warning"
                                        size="mini">视频
                                </el-tag>
                                <el-tag v-else-if="scope.row.type == 3" type="warning"
                                        size="mini">音乐
                                </el-tag>
                                <el-tag v-else-if="scope.row.type == 99" type="warning"
                                        size="mini">其他
                                </el-tag>
                            </div>
                            <div v-if="item.prop ==='img'">
                                <div style="font-size: 56px;">
                                    <img v-if="scope.row.type == 0" :src="baseURL+scope.row.url" height="50" width="50"/>
                                    <el-icon circle  v-else-if="scope.row.type == 1" class="el-icon-document"></el-icon>
                                    <el-icon  v-else-if="scope.row.type == 2" class="el-icon-video-camera-solid"></el-icon>
                                    <el-icon  v-else-if="scope.row.type == 3" class="el-icon-headset"></el-icon>
                                    <el-icon  v-else-if="scope.row.type == 99" class="el-icon-box"></el-icon>
                                </div>
                            </div>
                            <!--删除-->
                            <div v-if="item.operation">
                                <el-tooltip class="item" effect="dark" content="复制文件URL" placement="top-start">
                                    <el-button type="primary" size="small" @click="onCopy(scope.row.url,$event)">复制
                                    </el-button>
                                </el-tooltip>
                                <el-tooltip class="item" effect="dark" content="删除" placement="top-start">
                                    <el-button size="small" type="danger" @click="open(scope.row.id)">删除</el-button>
                                </el-tooltip>
                            </div>
                            <div v-else>{{scope.row[item.prop]}}</div>
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
                        :total="total"
                        @pagination="getPagination"
                >
                </el-pagination>
            </div>
        </div>
        <!--上传-->
        <el-dialog v-el-drag-dialog title="文件上传" :visible.sync="dialogFormVisible" :modal-append-to-body="false"
                   @close="closeClick">
            <dialogLogin @cancel="cancel" @submit="submit"></dialogLogin>
        </el-dialog>
    </div>
</template>

<script>
    import {imgList, imgRemove} from '@/api/dictionary'
    import clipboard from '@/utils/clipboard'
    import {baseURL} from "@/settings.js"
    import dialogLogin from './dialogLogin/dialogLogin.vue'
    import elDragDialog from '@/directive/el-drag-dialog' // base on element-ui
    export default {
        name: "rightImg",
        props: ['flag', 'Visible'],
        data() {
            return {
                baseURL,
                listLoading: false,
                dialogFormVisible: false,
                dataList: [],
                // 列表与筛选数据
                paramsList: {
                    pageSize: 10,//每页条数
                    pageNum: 1,//当前页
                    type: ''
                },
                tableLabel: [
                    {label: '预览', width: '', prop: 'img', checked: true, isDisabled: false, align: 'center',},
                    {label: 'URL', width: '400px', prop: 'url', checked: true, isDisabled: false},
                    {label: '文件类型', width: '', prop: 'dirType', checked: true, isDisabled: false, align: 'center',},
                    {
                        label: '上传时间',
                        align: 'center',
                        width: '180px',
                        prop: 'createDate',
                        checked: true,
                        isDisabled: false
                    },
                    {
                        label: '操作',
                        width: '',
                        prop: '',
                        operation: true,
                        checked: true,
                        isDisabled: false,
                        align: 'center',
                        width: '220px'
                    },
                ],
                total: 0,//总条数
                imageUrl: ''
            }
        },
        components: {
            dialogLogin
        },
        directives: {elDragDialog},
        watch: {
            flag() {
                this.paramsList.type = this.flag
                this.imgList()
            },
            Visible(val) {
                this.dialogFormVisible = val
            }
        },
        created() {
            this.imgList()
        },
        methods: {
            // 关闭弹框
            closeClick() {
                this.$emit('closeClick', false)
            },
            // 图片提交成功
            submit() {
                this.$emit('submitItem')
                this.imgList()
            },
            // 取消
            cancel() {
                this.$emit('cancelItem')
            },
            getPagination() {
                this.imgList()
            },
            // 列表
            async imgList() {
                this.listLoading = true
                let res = await imgList(this.paramsList)
                this.listLoading = false
                if (res.code === 0) {
                    this.dataList = res.data.list
                    this.total = parseInt(res.data.total)
                }

            },
            onCopy(url, event) {
                clipboard(url, event)
            },
            open(id) {
                this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(async () => {
                    let params = {
                        id: id
                    }
                    let res = await imgRemove(params)
                    if (res.code === 0) {
                        this.$message.success(res.msg);
                        this.imgList()
                    }
                }).catch(() => {

                });
            },
            // 选择每页多少条
            handleSizeChange(val) {
                this.paramsList.pageSize = val
                this.imgList()
                console.log(`每页 ${val} 条`);
            },
            // 选择当前页
            handleCurrentChange(val) {
                this.paramsList.pageNum = val
                this.imgList()
                console.log(`当前页: ${val}`);
            },
            async handleAvatarSuccess(da, file) {
                this.imageUrl = file.raw
                console.log(this.imageUrl);
                this.imgList()
            },
            // beforeAvatarUpload(file) {
            //     const isLt2M = file.size / 1024 / 1024 < 2;
            //     if (!isLt2M) {
            //         this.$message.error('上传大小不能超过 2MB!');
            //     }
            //     return isLt2M;
            // }
        }
    }
</script>

<style lang="scss" scoped>
    .rightImg {
        .rightImg-content {
            text-align: center;
            border: 1px solid #e7eaec;
            padding: 0;
            background-color: #ffffff;
            position: relative;
            margin-bottom: 20px;
            margin-right: 20px;

            .top {
                img {
                    width: 100%;
                    height: 100px;
                    overflow: hidden;
                }
            }

            .middle {
                padding: 10px;
                background-color: #f8f8f8;
                border-top: 1px solid #e7eaec;

                span {
                    color: #676a6c;
                    font-size: 14px;
                }
            }

            .bottom {
                margin: 10px 0;
            }
        }

        .block {
            margin-top: 10px;
            text-align: right;
        }
    }
</style>
