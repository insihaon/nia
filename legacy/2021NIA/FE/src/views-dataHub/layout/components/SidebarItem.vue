
<template>
  <div v-if="hasPermission(item)">
    <template v-if="hasOneShowingChild(item.children,item) && (!onlyOneChild.children||onlyOneChild.noShowingChildren)&&!item.alwaysShow">
      <app-link v-if="onlyOneChild.meta" :to="resolvePath(onlyOneChild.path)">
        <el-menu-item :index="resolvePath(onlyOneChild.path)" :class="{'submenu-title-noDropdown':!isNest}">
          <item :icon="onlyOneChild.meta.icon||(item.meta&&item.meta.icon)" :title="$t(onlyOneChild.meta.i18n) || onlyOneChild.meta.title" />
        </el-menu-item>
      </app-link>
    </template>

    <el-submenu v-else ref="subMenu" :index="resolvePath(item.path)" popper-append-to-body>

      <template slot="title">
        <item v-if="item.meta" :icon="item.meta && item.meta.icon" :title="$t(item.meta.i18n) || item.meta.title" />
      </template>

      <SidebarItem
        v-for="child in item.children"
        :key="child.path"
        :is-nest="true"
        :item="child"
        :base-path="resolvePath(child.path)"
        class="nest-menu"
      />

    </el-submenu>
  </div>
</template>

<script>
import path from 'path'
import { isExternal } from '@/utils/validate'
import Item from '@/layout/components/aam/Sidebar/Item'
import AppLink from '@/layout/components/aam/Sidebar/Link'
import FixiOSBug from '@/layout/components/dataHub/FixiOSBug'
import { mapState } from 'vuex'

function findIntersection(arr1, arr2) {
    return arr1.reduce((result, item) => {
        if (arr2.includes(item)) {
            result.push(item)
        }
        return result
    }, [])
}

export default {
  name: 'SidebarItem',
  components: { Item, AppLink },
  mixins: [FixiOSBug],
  props: {
    // route object
    item: {
      type: Object,
      required: true
    },
    isNest: {
      type: Boolean,
      default: false
    },
    basePath: {
      type: String,
      default: ''
    }
  },
  data() {
    // To fix https://github.com/PanJiaChen/vue-admin-template/issues/237
    // TODO: refactor with render function
    this.onlyOneChild = null
    return {}
  },
  computed: {
    test() {
     return this.item
    },
    ...mapState({
      roles: state => state.user.roles,
    })
  },
  methods: {
    hasPermission(item) {
      // if (item?.meta?.grant) {
      //   console.log(item.path)
      // }

      const menuRoles = item?.meta?.grant ?? ['ROLE_ADMIN', 'ROLE_USER']
      const myRoles = this.roles ?? ['ROLE_USER']

      const intersection = findIntersection(menuRoles, myRoles)
      return intersection.length > 0
    },
    hasOneShowingChild(children = [], parent) {
      const showingChildren = children.filter(item => {
        if (item.hidden) {
          return false
        } else {
          // Temp set(will be used if only has one showing child)
          this.onlyOneChild = item
          return true
        }
      })

      // When there is only one child router, the child router is displayed by default
      if (showingChildren.length === 1) {
        return true
      }

      // Show parent if there are no child router to display
      if (showingChildren.length === 0) {
        this.onlyOneChild = { ... parent, path: '', noShowingChildren: true }
        return true
      }

      return false
    },
    resolvePath(routePath) {
      if (isExternal(routePath)) {
        return routePath
      }
      if (isExternal(this.basePath)) {
        return this.basePath
      }
      return path.resolve(this.basePath, routePath)
    }
  }
}
</script>

<style lang="scss" scoped>

  @import "~@/styles/variables.scss";

  ::v-deep .el-submenu__title{
    width : 200px !important;
    font-weight: 500;
    font-family:'NanumSquare',san-serif;
    font-size:14px;
    vertical-align: middle;
    height: 40px;
    line-height: 40px;
    transition: all ease 1s 0s;
    background: rgba(132, 126, 126, 0.73);

  }
   ::v-deep .el-submenu__title:hover{
    //  border-bottom: 3px solid rgba(255, 255, 255, 0.897) !important;
     transition: all .20s;
  }

  ::v-deep .el-submenu__title i{
    color: rgb(237, 229, 229);
  }

</style>
