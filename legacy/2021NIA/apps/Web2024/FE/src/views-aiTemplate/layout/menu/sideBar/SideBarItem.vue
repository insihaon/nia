<template>
  <div v-if="hasOneShowingChild(item.children, item) && (!onlyOneChild.children||onlyOneChild.noShowingChildren)">
    <router-link v-if="onlyOneChild.meta" :to="resolvePath(onlyOneChild.path)">
      <el-menu-item :index="resolvePath(onlyOneChild.path)">
        <i :class="item.meta.icon" />
        <span>
          {{ item.meta.title }}
        </span>
      </el-menu-item>
    </router-link>
  </div>
  <el-submenu v-else ref="subMenu" :index="resolvePath(item.path)" popper-append-to-body>
    <template slot="title">
      <i :class="item.meta.icon" />
      <span>
        {{ item.meta.title }}
      </span>
    </template>
    <SideBarItem
      v-for="child in item.children"
      :key="child.path"
      :base-path="resolvePath(child.path)"
      :item="child"
    />
  </el-submenu>

</template>

<script>
import path from 'path'
import { isExternal } from '@/utils/validate'

const routeName = 'SideBarItem'
export default {
  name: routeName,
  components: { },
  props: {
    // route object
    item: {
      type: Object,
      required: true
    },
    basePath: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      bgColor: '#101827',
      textColor: '#bfcbd9',
      buttonDisabled: false
    }
  },
  computed: {
  },
  mounted () {
  },
  methods: {
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
        this.onlyOneChild = { ... parent, path: '' }
        return true
      }
      return false
    },
    handleButtonClick() {
      // Handle button click event
    },
    resolvePath(routePath) {
      if (isExternal(routePath)) {
        return routePath
      }
      if (isExternal(this.basePath)) {
        return this.basePath
      }
      return path.resolve(this.basePath, routePath)
    },
  }
}
</script>

<style lang="scss" scoped>
#top-inner {
  li {
    padding: 3px 15px;
  }
  li > a {
    display: block;
    font-weight: 600;
    color: black;
    font-size: 18px;
  }
  li > ul > li {
    margin-top: 5px;
    padding-left: 5px;
    a {
      font-size: 16px;
      font-weight: 600;
      color: #141414;
    }
  }
  li > ul > li > ul > li {
    a {
      font-size: 14px;
      padding-left: 5px;
      color: gray;
    }
  }
}
</style>
