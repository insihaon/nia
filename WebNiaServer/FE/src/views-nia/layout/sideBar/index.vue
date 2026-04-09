<template>
  <el-aside id="side-menu" class="p-0" :style="{ width: getWidth, 'z-index': isActive ? 1000 : 0 }">
    <el-scrollbar v-show="isActive" wrap-class="scrollbar-wrapper" class="w-full h-full">
      <el-menu class="h-full" menu-trigger="click" :collapse-transition="false" @open="handleOpen" @close="handleClose">
        <!--
          :collapse="!isActive"
          Maximum call stack size exceeded
          위의 error로 인해 사용 x
         -->
        <SideBarItem v-for="route in permission_routes" v-if="!route.hidden" :key="route.path" :base-path="route.path" :item="route" />
      </el-menu>
    </el-scrollbar>
  </el-aside>
</template>

<script>
import SideBarItem from './SideBarItem'
import { mapState, mapGetters } from 'vuex'

const routeName = 'SideBar'
export default {
  name: routeName,
  components: { SideBarItem },
  data() {
    return {
      bgColor: '#101827',
      textColor: '#bfcbd9',
    }
  },
  computed: {
    ...mapGetters(['sidebar', 'permission_routes']),
    ...mapState({
      showSidebar: (state) => state.settings.menuType === 'LEFT',
      fixSide: (state) => state.settings.fixSide,
    }),
    isActive() {
      return this.sidebar.opened
    },
    getWidth() {
      if (!this.fixSide && !this.isActive) {
        return '0px'
      } else {
        return this.isActive ? 'var(--open-sidebar-width)' : '0px' // 'var(--fix-sidebar-width)'
      }
    },
  },
  mounted() {
    console.log(this.permission_routes)
  },
  created() {
    this.$nextTick(() => {
      const header = document.querySelector('#sub-menu')
      const menuItem = document.querySelectorAll('#menu-item')
      menuItem?.forEach((el) => {
        el.addEventListener('mouseover', (event) => {
          header.classList.add('open')
        })
      })
      header?.addEventListener('mouseover', function (event) {
        header.classList.add('open')
      })
      header?.addEventListener('mouseout', function (event) {
        header.classList.remove('open')
      })
    })
  },
  methods: {
    handleButtonClick() {
      // Handle button click event
    },
    handleOpen() {},
    handleClose() {},
  },
}
</script>

<style lang="scss" scoped>
#side-menu {
  top: 0;
  left: 0;
  height: 100%;
  position: fixed;
  transition: width 0.3s;
  background-color: white;
  ::v-deep a,
  i {
    color: black;
  }
}
</style>
