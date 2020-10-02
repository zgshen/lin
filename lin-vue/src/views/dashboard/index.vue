<template>
  <div class="dashboard-container">
    <div class="dashboard-text">Browser Version: {{ a }}</div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'Dashboard',
  data() {
    return {
          a: getBrowser()
    }
  }
}

function getBrowser(){
  var browser = {
    msie: false,
    firefox: false,
    opera: false,
    safari: false,
    chrome: false,
    netscape: false,
    appname: 'unknown',
    version: 0
  },
  ua = window.navigator.userAgent.toLowerCase();
  if (/(msie|firefox|opera|chrome|netscape)\D+(\d[\d.]*)/.test(ua)) {
    browser[RegExp.$1] = true;
    browser.appname = RegExp.$1;
    browser.version = RegExp.$2;
  } else if (/version\D+(\d[\d.]*).*safari/.test(ua)) {
    // safari
    browser.safari = true;
    browser.appname = 'safari';
    browser.version = RegExp.$2;
  }
  return browser.appname + ' ' + browser.version;
}

</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
</style>
