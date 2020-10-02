<template>
  <div class="MainLayoutLeftRight" :style="!shrinkBar?shrinkStyle.mainStyle:''">
    <div class="MainLayoutLeftRight_left" :style="!shrinkBar?shrinkStyle.leftStyle:''">
      <slot name="left"></slot>
    </div>
    <div class="MainLayoutLeftRight_right">
      <slot name="right"></slot>
    </div>
    <div class="shrinkBar" @click="handShrinkBar" :style="!shrinkBar?shrinkStyle.shrinkStyle:''" :title="shrinkBar?'收缩':'展开'">
      <i :class="shrinkBar?'el-icon-s-fold':'el-icon-s-unfold'"></i>
    </div>
  </div>
</template>

<script>
  import {mapState} from 'vuex'

  export default {
    name: "MainLayoutLeftRight",
    data() {
      return {
        animationLeft: 'transition: left 0s',
        shrinkBar: true,
        shrinkStyle:{
          mainStyle:{
            'padding-left':'20px',
            'transition': 'padding-left .28s'
          },
          leftStyle:{
            'left': '-250px',
            'transition': 'left .28s'
          },
          shrinkStyle:{
            'left': '0px',
            'transition': 'left .28s'
          }
        }
      }
    },
    computed: {
      ...mapState({
        sidebar: state => state.app.sidebar,
        device: state => state.app.device,
        fixedHeader: state => state.settings.fixedHeader
      }),
    },
    watch: {
      'sidebar.opened'() {
        this.animationLeft = 'transition: left .28s'
        let time = setTimeout(v => {
          this.animationLeft = 'transition: left 0s'
          clearTimeout(time)
          time = null
        }, 280)
      }
    },
    methods: {
      handShrinkBar() {
        this.shrinkBar = !this.shrinkBar
      }
    }
  }
</script>

<style lang="scss" scoped>
  .MainLayoutLeftRight {
    min-height: calc(100vh - 84px);
    position: relative;
    padding-top: 10px;
    padding-right: 20px;
    padding-bottom: 10px;
    padding-left: 266px;
    box-sizing: border-box;
    overflow-x: hidden;
    transition: padding-left .28s;
    transform: rotateZ(0);
    .MainLayoutLeftRight_left {
      position: absolute;
      left: 0px;
      top: 0px;
      bottom: 0;
      width: 240px;
      background: white;
      z-index: 1;
      min-height: calc(100vh - 84px);
      transition: left 0.28s;
      padding-top: 20px;
      padding-left: 15px;
      border-right: 1px solid #dfe6ec;
      transform: rotateZ(0);
    }
    .MainLayoutLeftRight_right {
      /*min-height: calc(100vh - 104px);*/
      /*height: 2000px;*/
      background: white;
    }
    .shrinkBar {
      position: absolute;
      z-index: 2;
      width: 24px;
      height: 50px;
      box-shadow: 2px 0px 4px rgba(0, 0, 0, .12), 4px 0 8px rgba(0, 0, 0, .04);
      left: 240px;
      top: calc(40vh - 30px);
      border: 1px solid #dfe6ec;
      border-left: 0px;
      background-color: white;
      border-bottom-right-radius: 4px;
      border-top-right-radius: 4px;
      cursor: pointer;
      line-height: 50px;
      text-align: center;
      font-size: 18px;
      color: black;
      transform: rotateZ(0);
      transition: left 0.28s;
      &:hover{
        background: rgba(0, 0, 0, 0.02);
      }
    }

  }
</style>