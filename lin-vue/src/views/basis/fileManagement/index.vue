<template>
    <div style="padding:10px 30px;position: relative">
        <div class="toolbar" >
            <div class="toolbar-left" style="width: 270px">
                <div slot="left">
                    <div class="rightImg-upload">
                        <el-button type="success" size="small" icon="el-icon-plus" @click="uploadItem">文件上传</el-button>
                    </div>
                </div>
            </div>
            <div class="toolbar-right">
                <div style="width:150px;display: inline-block; ">
                    <el-select v-model="tempFlag"  placeholder="请选择">
                        <el-option
                                v-for="item in treeData"
                                :key="item.type"
                                :label="item.name"
                                :value="item.type">
                        </el-option>
                    </el-select>
                </div>
                <el-button type="primary" size="small" @click="clickSearch">查询</el-button>
            </div>
            <right-img slot="right" :flag="flag" :Visible="Visible" @submitItem="submitItem" @cancelItem="cancelItem"
                       @closeClick="closeClick">
            </right-img>
        </div>
    </div>
</template>

<script>
    import MainLayoutLeftRight from "@/components/MainLayoutLeftRight"
    import rightImg from "./rightImg"
    export default {
        name: "index",
        data() {
            return {
                Visible: false,
                treeLoading: false,
                treeData: [
                    {
                        type: '',
                        name: '所有文件',
                    },
                    {
                        type: '0',
                        name: '图片',
                    },
                    {
                        type: '1',
                        name: '文档',
                    },
                    {
                        type: '2',
                        name: '视频',
                    },
                    {
                        type: '3',
                        name: '音乐',
                    },
                    {
                        type: '99',
                        name: '其他',
                    },

                ],
                defaultProps: {
                    children: 'children',
                    label: 'name'
                },
                tempFlag: '',
                flag: ''
            };

        },
        components: {
            MainLayoutLeftRight,
            rightImg,
        },
        methods: {
            closeClick() {
                this.Visible = false
            },
            submitItem() {
                this.Visible = false
            },
            // 取消
            cancelItem() {
                this.Visible = false
            },
            uploadItem() {
                this.Visible = true
            },
            clickSearch(){
                this.flag = this.tempFlag;
                console.log( this.flag, '@@@')
            }
        }
    }
</script>

<style lang="scss" scoped>

</style>
<style lang="scss">
    .fileManagement {

    .el-tree-node__expand-icon.is-leaf {
        color: #000;
    }

    .el-tree-node {
        margin-bottom: 10px;
    }

    .el-tree-node__label {
        font-size: 16px;
    }

    .el-tree-node__expand-icon {
        font-size: 18px;
    }

    .rightImg-upload {
        margin-bottom: 20px;
    }

    }
    .toolbar .toolbar-left{
        width : 100%
    }
</style>
