<template>
  <div class="app-wrapper">
    <Navbar />
    <app-main />
  </div>
</template>

<script>
/* eslint-disable */
import AppMain from './components/AppMain'
import Navbar from './components/Navbar'

export default {
  name: 'Layout',
  components: {
    Navbar,
    AppMain
  }
}
</script>

<style lang="scss" scoped>
  .app-wrapper {
    position: relative;
    height: calc( 100vh );
    width: 100%;
  }
  .tsdn-header {
    position: sticky;
    z-index: 9;
    width: 100%;
    transition: width 0.28s;
  }
  .external-menu-wrap{
    position:fixed;
    top:0; width:100%; left:0;
    color:#d2d2d2 !important;
    background-color:#ffffff;
    transition:all 0.25s;
    z-index: 10;
    text-align: center;
    color:#FFFFFF;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 24px;
  }
  .externalMenuShow{
    top: 80px;
  }
  .content-wrap{
    position:absolute;
    overflow: hidden;
    top:60px;
    width:100%;
    box-sizing: border-box;
    padding:20px;
    transition: all 0.25s;
    &-top{
      display: flex;
      flex-direction: row;
      align-items: center;
      justify-content: space-between;
      flex-wrap: nowrap;
      height:40px;
      gap:20px;
      padding-right:20px;
      box-sizing: border-box;
      >div:first-child{
        box-sizing: border-box;
        width: calc(100% - 400px);
      }
      >div:last-child{
        min-width: 400px;
      }
    }
  }
</style>
