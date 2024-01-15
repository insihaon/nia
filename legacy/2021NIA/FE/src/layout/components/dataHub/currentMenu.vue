<template>
  <div class="currentMenu">
    <breadcrumb id="breadcrumb-container" class="breadcrumb-container" />
  </div>
</template>

<script>
import { Base } from '@/min/Base.min'
import Breadcrumb from '@/components/Breadcrumb'

export default {
  components: {
    Breadcrumb
  },
  extends: Base,
  computed: {
  },
  methods: {
    // toggleSideBar() {
    //   this.$store.dispatch('app/toggleSideBar')
    // }

  }
}
</script>

<style lang="scss" scoped>
.currentMenu {
  height: 35px;

  overflow: hidden;
  position: relative;
  background: #b5c6e07a;
  box-shadow: 0 1px 4px rgba(218, 224, 230, 0.08);
  padding-left: 15px;

  .breadcrumb-container {
    float: left;
    line-height: 35px;

  }

}
</style>
