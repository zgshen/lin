<template>
  <Main-layout-left-right>
    <div slot="left">
      <el-tree
        v-loading="treeLoading"
        :data="treeData"
        default-expand-all
        highlight-current
        node-key="deptId"
        :expand-on-click-node="false"
        @node-click="nodeClick"
        :props="defaultProps">
      </el-tree>
    </div>
    <main-con slot="right" :cellData="cellData"></main-con>
  </Main-layout-left-right>
</template>

<script>
  import MainLayoutLeftRight from "@/components/MainLayoutLeftRight"
  import mainCon from './component/mainCon'
  import { getUserTree } from '@/api/system'
  export default {
    name: "userIndex",
    components: {
      MainLayoutLeftRight,
      mainCon
    },
    data() {
      return {
        treeLoading:true,
        treeData: [],
        defaultProps: {
          children: 'children',
          label: 'name'
        },
        cellData:{}
      };
    },
    methods: {
      nodeClick(cellData,node,component){
        this.cellData = cellData
        console.log(cellData)
      },
      //获取部门树结构
      async getUserTree(){
        this.treeLoading=true
        let res = await getUserTree()
        this.treeLoading=false
        if(res.code === 0){
          //默认顶级节点为deptId=''
          this.treeData=[{
            deptId: '',
            name:'顶级节点',
            children:res.data
          }]
        }
      }
    },
    created(){
      this.getUserTree()
    }
  }
</script>

<style lang="scss" scoped>
  .userIndex {
    min-height: calc(100vh - 124px);
    position: relative;
  }
</style>
