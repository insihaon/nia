<template>
  <section class="app-main">
    <transition name="fade-transform" mode="out-in">
      <router-view :key="key" />
    </transition>
  </section>
</template>

<script>
/* eslint-disable */
export default {
  name: 'AppMain',
  data() {
    return {
    }
  },
  created() {
  },
  computed: {
    key() {
      return this.$route.path
    }
  },
  methods: {
  },
}
</script>

<style scoped>
.app-main {
  position: relative;
  padding:30px 30px;
  box-sizing: border-box;
  border: none;
  overflow: auto;
  transition:all 0.25s !important;
}
.app-main::-webkit-scrollbar {
  width: 20px; height:20px;
}
.app-main::-webkit-scrollbar-thumb {
  background-color: rgba(0,0,0,0.1);
  border-radius: 10px;
  background-clip: padding-box;
  border: 6px solid transparent;
  transition: all 0.25s;
}
.app-main::-webkit-scrollbar-thumb:hover{
  background-color:#406AFF;
}
.app-main::-webkit-scrollbar-track {
  background-color: transparent;
  border-radius: 5px;
}


.fixed-header+.app-main {
  padding-top: 70px;
}
.externalMenuShow{
  /* padding-top : calc(70px + 70px); */
  transition:all 0.25s !important;
}
</style>

<style lang="scss">
// fix css style bug in open el-dialog
.el-popup-parent--hidden {
  .fixed-header {
    padding-right: 15px;
  }
}
.el-table::before{
  z-index:0;
}
</style>
