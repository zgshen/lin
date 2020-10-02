<template>
  <el-form :model="form" :rules="rules" ref="ruleForm" label-width="100px" v-loading="Loading">
    <el-form-item label="角色名" prop="roleName">
      <el-input v-model="form.roleName"></el-input>
    </el-form-item>
    <el-form-item label="备注" prop="remark">
      <el-input v-model="form.remark"></el-input>
    </el-form-item>
    <el-form-item label="派发渠道:" prop="">
      <el-input v-model="form.channels" placeholder="请选择派发渠道" :disabled="true" style="width: 360px"></el-input>
      <el-button size="small" type="primary" icon="el-icon-plus" @click="showDialog('channels')"></el-button>
    </el-form-item>
    <el-form-item label="核销渠道:" prop="">
      <el-input v-model="form.useChannels" placeholder="请选择核销渠道" :disabled="true" style="width: 360px"></el-input>
      <el-button size="small" type="primary" icon="el-icon-plus" @click="showDialog('useChannels')"></el-button>
    </el-form-item>
    <el-form-item label="券标签:" prop="tags">
      <el-input v-model="form.tags" placeholder="请选择券标签" :disabled="true" style="width: 360px"></el-input>
      <el-button size="small" type="primary" icon="el-icon-plus" @click="showDialog('tags')"></el-button>
    </el-form-item>
    <el-form-item label="BU:" prop="tags">
      <el-input v-model="form.buType" placeholder="请选择 BU" :disabled="true" style="width: 360px"></el-input>
      <el-button size="small" type="primary" icon="el-icon-plus" @click="showDialog('bu')"></el-button>
    </el-form-item>
    <el-form-item label="菜单权限" prop="menuIds">
      <div class="form-tree">
        <el-tree
          ref="roleTree"
          :data="treeData"
          :default-expanded-keys="form.menuIds"
          show-checkbox
          node-key="id"
          :check-strictly="isCheck"
          :default-checked-keys="form.menuIds"
          :props="defaultProps">
        </el-tree>
      </div>
    </el-form-item>
    <el-dialog v-el-drag-dialog
               :visible.sync="dialogInfo.visible"
               :width="dialogInfo.width"
               :title="dialogInfo.title"
               append-to-body>
      <component
              v-if="dialogInfo.visible"
              :is="dialogInfo.component"
              :componentType="dialogInfo.componentType"
              :formData="dialogInfo.formData"
      ></component>
    </el-dialog>
    <el-form-item>
      <el-button type="primary" size="small" @click="submit('ruleForm')" style="margin-right: 10px">提交</el-button>
      <!--<el-button size="small" @click="resetForm('ruleForm')">取消</el-button>-->
    </el-form-item>
  </el-form>
</template>

<script>
  import { getMenuTree,addRole,editRole,getRoleInfo } from '@/api/system'
  import elDragDialog from '@/directive/el-drag-dialog'

  export default {
    name: "roleForm",
    directives: {
      elDragDialog
    },
    props:{
      'componentType':{
        default:'',
        type:String
      },
      'formData': {
        default() {
          return {}
        },
        type: Object
      }
    },
    data() {
      return {
        isCheck:true,
        Loading:true,
        treeData: [
        ],
        defaultProps: {
          children: 'children',
          label: 'text'
        },
        dialogInfo: {
          component: '',
          title: '',
          visible: false,
          width: '',
          componentType: ''
        },
        form: {

        },
        rules: {
          roleName: [
            { required: true, message: '请输入角色名', trigger: 'blur' },
            { min: 1, max: 25, message: '长度在 1 到 25 个字符', trigger: 'blur' }
          ],
          remark: [],
          menuIds: [],
        }
      }
    },
    methods: {
      showDialog: function (type, cellData) {
        switch (type) {
          case 'channels':
            if (this.form.channelInfo == undefined || this.form.channelInfo == null || (this.form.channelInfo.constructor === Array && this.form.channelInfo.length == 0)) {
              this.$message({
                type: 'warning',
                message: '当前没有可选渠道'
              });
              break;
            }
            this.dialogInfo = {
              component: selectChannel,
              title: '选择派发渠道',
              visible: true,
              width: '900px',
              componentType: 'channels',
              formData: this.form.channelInfo ? this.form.channelInfo : []
            };
            break;
          case 'useChannels':
            if (this.form.useChannelInfo == undefined || this.form.useChannelInfo == null || (this.form.useChannelInfo.constructor === Array && this.form.useChannelInfo.length == 0)) {
              this.$message({
                type: 'warning',
                message: '当前没有可选渠道'
              });
              break;
            }
            this.dialogInfo = {
              component: selectUseChannel,
              title: '选择核销渠道',
              visible: true,
              width: '900px',
              componentType: 'useChannels',
              formData: this.form.useChannelInfo ? this.form.useChannelInfo : []
            };
            break;
          case 'tags':
            if (this.form.tagInfo == undefined || this.form.tagInfo == null || (this.form.tagInfo.constructor === Array && this.form.tagInfo.length == 0)) {
              this.$message({
                type: 'warning',
                message: '当前没有可选标签'
              });
              break;
            }
            this.dialogInfo = {
              component: selectTag,
              title: '选择券标签',
              visible: true,
              width: '900px',
              componentType: 'tags',
              formData: this.form.tagInfo ? this.form.tagInfo : []
            };
            break;
          case 'bu':
            if (this.form.tagInfo == undefined || this.form.tagInfo == null || (this.form.tagInfo.constructor === Array && this.form.tagInfo.length == 0)) {
              this.$message({
                type: 'warning',
                message: '当前没有可选标签'
              });
              break;
            }
            this.dialogInfo = {
              component: selectBU,
              title: '选择 BU',
              visible: true,
              width: '900px',
              componentType: 'bus',
              formData: this.form.buInfo ? this.form.buInfo : []
            };
            break;
        }
      },
      submit(formName) {
        this.form.menuIds = this.$refs.roleTree.getCheckedKeys().concat(this.$refs.roleTree.getHalfCheckedKeys())
        console.log(this.$refs.roleTree.getHalfCheckedKeys())
        console.log(this.$refs.roleTree.getCheckedKeys())
        this.$refs[formName].validate((valid) => {
          if (valid) {
            console.log(this.form)
            this.subUserForm()
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      },
      //添加或修改角色
      async subUserForm(){
        let res;
        if(this.componentType === 'addRole'){
          res = await addRole(this.form.menuIds||[],this.form)
          if(res.code === 0){
            this.$message({
              type: 'success',
              message: '添加成功'
            });
            this.$parent.$parent.getRoleList();
            this.$parent.$parent.dialogInfo.visible = false
          }
          console.log(res)
        }
        if(this.componentType === 'editRole'){
          res = await editRole(this.form.menuIds||[],this.form)
          if(res.code === 0){
            this.$message({
              type: 'success',
              message: '编辑成功'
            });
            this.$parent.$parent.getRoleList();
            this.$parent.$parent.dialogInfo.visible = false
          }
        }
      },
      async getMenuList(){
        let res = await getMenuTree()
        if(res.code === 0){
          this.treeData = [res.data]
        }
      },
      async getRoleInfo(roleId){
        let res = await getRoleInfo({roleId})
        if(res.code === 0){
          this.$nextTick(() => {
            this.form = res.data;
            // 派发渠道
            if (this.form.channelInfo != undefined && this.form.channelInfo != null && this.form.channelInfo.length > 0) {
              this.form.channels = '';
              // 没有选 默认选中全部
              let isCheck=this.form.channelInfo.find(v=>{return v.checked==true});

              for (let i = 0; i < this.form.channelInfo.length; i++) {
                if (!isCheck) {
                  this.form.channelInfo[i].checked = true;
                }
                if (this.form.channelInfo[i].checked == true) {
                  this.form.channels += this.form.channelInfo[i].systemName + ",";
                }
              }
              this.form.channels = this.form.channels.substring(0, this.form.channels.length - 1);
            }

            // 核销渠道
            if (this.form.useChannelInfo != undefined && this.form.useChannelInfo != null && this.form.useChannelInfo.length > 0) {
              this.form.useChannels = '';
              // 没有选 默认选中全部
              let isCheck=this.form.useChannelInfo.find(v=>{return v.checked==true});

              for (let i = 0; i < this.form.useChannelInfo.length; i++) {
                if (!isCheck) {
                  this.form.useChannelInfo[i].checked = true;
                }
                if (this.form.useChannelInfo[i].checked == true) {
                  this.form.useChannels += this.form.useChannelInfo[i].systemName + ",";
                }
              }
              this.form.useChannels = this.form.useChannels.substring(0, this.form.useChannels.length - 1);
            }

            // 标签
            if (this.form.tagInfo != undefined && this.form.tagInfo != null && this.form.tagInfo.length > 0) {
              this.form.tags = '';
              // 没有选 默认选中全部
              let isCheck=this.form.tagInfo.find(v=>{return v.checked==true});

              for (let i = 0; i < this.form.tagInfo.length; i++) {
                if (!isCheck) {
                  this.form.tagInfo[i].checked = true;
                }
                if (this.form.tagInfo[i].checked == true) {
                  this.form.tags += this.form.tagInfo[i].tag + ",";
                }
              }
              this.form.tags = this.form.tags.substring(0, this.form.tags.length - 1);
            }

            // BU
            if (this.form.buInfo != undefined && this.form.buInfo != null && this.form.buInfo.length > 0) {
              this.form.buType = '';
              // 没有选 默认选中全部
              let isCheck=this.form.buInfo.find(v=>{return v.checked==true});

              for (let i = 0; i < this.form.buInfo.length; i++) {
                if (!isCheck) {
                  this.form.buInfo[i].checked = true;
                }
                if (this.form.buInfo[i].checked == true) {
                  this.form.buType += this.form.buInfo[i].buType + ",";
                }
              }
              this.form.buType = this.form.buType.substring(0, this.form.buType.length - 1);
            }
            this.isCheck=false;
          });

        }
      }
    },
    async created(){
      this.Loading=true;
      await this.getMenuList();
      await this.getRoleInfo(this.formData.roleId);
      this.Loading=false;
    }
  }
</script>

<style lang="scss" scoped>
  .form-tree{
    padding-top: 5px;
  }
</style>
