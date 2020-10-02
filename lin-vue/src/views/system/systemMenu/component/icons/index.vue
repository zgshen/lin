<template>
  <div class="icons-container">
    <el-tabs type="border-card">
      <el-tab-pane label="Icons">
        <div v-for="item of svgIcons" :key="item" @click="handleClipboard(item,'svg')">
          <div class="icon-item">
            <svg-icon :icon-class="item" class-name="disabled" />
            <span>{{ item }}</span>
          </div>
        </div>
      </el-tab-pane>
      <el-tab-pane label="Element-UI Icons">
        <div v-for="item of elementIcons" :key="item" @click="handleClipboard(item,'el')">
          <div class="icon-item">
            <i :class="'el-icon-' + item" />
            <span>{{ item }}</span>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
// import clipboard from '@/utils/clipboard'
import svgIcons from './svg-icons'
import elementIcons from './element-icons'

export default {
  name: 'Icons',
  data() {
    return {
      svgIcons,
      elementIcons
    }
  },
  methods: {
    generateIconCode(symbol) {
      return `<svg-icon icon-class="${symbol}" />`
    },
    generateElementIconCode(symbol) {
      return `<i class="el-icon-${symbol}" />`
    },
    handleClipboard(text,type) {
      // clipboard(text, event)
      console.log(text)
      this.$emit('getIco',{
        value:text,
        type
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.icons-container {
  overflow: hidden;

  .icon-item {
    margin: 0px;
    height: 66px;
    text-align: center;
    width: 100px;
    float: left;
    font-size: 20px;
    color: #24292e;
    cursor: pointer;
    padding: 5px 10px;
    border-radius: 6px;
    &:hover{
      background-color: #f5f5f5;
      transition: background-color .3s;
    }
  }

  span {
    display: block;
    font-size: 12px;
    margin-top: 10px;
  }

  .disabled {
    pointer-events: none;
  }
}
</style>
