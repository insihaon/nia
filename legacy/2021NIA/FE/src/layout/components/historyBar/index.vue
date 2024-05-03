<template>
  <div id="tags-view-container" :class="{'opend': historybar.opened}" :style="{'right': isMobile ? '-180px': '-170px'}">
    <div id="title-container">
      <div><i class="el-icon-reading pr-1" />방문 기록</div>
      <div><i class="el-icon-close" @click="closeHistoryBar()" /></div>
    </div>
    <router-link
      v-for="tag in visitedViews"
      id="tags-view-item"
      ref="div"
      :key="tag.path"
      :class="isActive(tag)?'active':''"
      :to="{ path: tag.path, query: tag.query, fullPath: tag.fullPath }"
      tag="div"
      @click.middle.native="!isAffix(tag) ? closeSelectedTag(tag):''"
      @contextmenu.prevent.native="openMenu(tag,$event)"
    >
      <!-- @contextmenu.prevent.native="openMenu(tag,$event)" -->
      <div class="title" :class="{'long-title': getTitleLen(tag.title) > 10}">
        {{ $t(tag.meta.i18n) || tag.meta.tagTitle || tag.title }}
      </div>
      <div class="d-flex">
        <!-- <i v-if="!isAffix(tag)" class="el-icon-collection-tag" @click.prevent.stop="closeSelectedTag(tag)" /> -->
        <span v-if="!isAffix(tag)" class="el-icon-close" @click.prevent.stop="closeSelectedTag(tag)" />
      </div>
    </router-link>
    <ul v-show="visible" :style="{left:left+'px',top:top+'px'}" class="contextmenu">
      <li @click="refreshSelectedTag(selectedTag)">Refresh</li>
      <li @click="openNewTab(selectedTag)">Open New Tab</li>
      <li v-if="!isAffix(selectedTag)" @click="closeSelectedTag(selectedTag)">Close</li>
      <li @click="closeOthersTags">Close Others</li>
      <li @click="closeAllTags(selectedTag)">Close All</li>
    </ul>
  </div>
</template>

<script>
import { Base } from '@/min/Base.min'
import { AppOptions } from '@/class/appOptions'
import { mapState } from 'vuex'
import path from 'path'

const routeName = 'HistoryBar'

export default {
  name: routeName,
  components: { },
  extends: Base,
  data() {
    return {
      visible: false,
      top: 0,
      left: 0,
      selectedTag: {},
      affixTags: [],
      AppOptions: AppOptions
    }
  },
  computed: {
    visitedViews() {
      const visitedViews = [...this.$store.state.tagsView.visitedViews]
      const excludeAffix = visitedViews.filter(v => !v.meta.affix)

      return visitedViews.filter(v => v.meta.affix).concat(excludeAffix.reverse())
    },
    routes() {
      return this.$store.state.permission.routes
    },
    ...mapState({
      historybar: state => state.app.historybar,
    }),
  },
  watch: {
    $route() {
      this.addTags()
      this.moveToCurrentTag()
    },
    visible(value) {
      if (value) {
        document.body.addEventListener('click', this.closeMenu)
      } else {
        document.body.removeEventListener('click', this.closeMenu)
      }
    }
  },
  mounted() {
    this.initTags()
    this.addTags()
    const THIS = this
    const historyBar = document.querySelector('#tags-view-container')
    historyBar?.addEventListener('mouseover', function(event) {
      !THIS.historybar.opened && THIS.toggleHistoryBar()
    })
    historyBar?.addEventListener('mouseout', function(event) {
      THIS.historybar.opened && THIS.toggleHistoryBar()
    })
  },
  methods: {
    toggleHistoryBar() {
      this.$store.dispatch('app/toggleHistoryBar')
    },
    getTitleLen(title) {
      const tempTitle = this._cloneDeep(title)
      return tempTitle.replace(/ /g, '').length
    },
    isActive(route) {
      return route.path === this.$route.path
    },
    isAffix(tag) {
      return tag.meta && tag.meta.affix
    },
    filterAffixTags(routes, basePath = '/') {
      let tags = []
      routes.forEach(route => {
        if (route.meta && route.meta.affix) {
          const tagPath = path.resolve(basePath, route.path)
          tags.push({
            fullPath: tagPath,
            path: tagPath,
            name: route.name,
            meta: { ...route.meta }
          })
        }
        if (route.children) {
          const tempTags = this.filterAffixTags(route.children, route.path)
          if (tempTags.length >= 1) {
            tags = [...tags, ...tempTags]
          }
        }
      })
      return tags
    },
    initTags() {
      const affixTags = this.affixTags = this.filterAffixTags(this.routes)
      for (const tag of affixTags) {
        // Must have tag name
        if (tag.name) {
          this.$store.dispatch('tagsView/addVisitedView', tag)
        }
      }
    },
    addTags() {
      const { name } = this.$route
      if (name) {
        this.$store.dispatch('tagsView/addView', this.$route)
      }
      return false
    },
    moveToCurrentTag() {
      const tags = this.$refs.tag
      if (!tags) {
        return
      }
      this.$nextTick(() => {
        for (const tag of tags) {
          if (tag.to.path === this.$route.path) {
            this.$refs.scrollPane.moveToTarget(tag)
            // when query is different then update
            if (tag.to.fullPath !== this.$route.fullPath) {
              this.$store.dispatch('tagsView/updateVisitedView', this.$route)
            }
            break
          }
        }
      })
    },
    refreshSelectedTag(view) {
      this.$store.dispatch('tagsView/delCachedView', view).then(() => {
        const { fullPath } = view
        this.$nextTick(() => {
          this.$router.replace({
            path: '/redirect' + fullPath
          })
        })
      })
    },
    openNewTab(view) {
      const url = `${window.location.origin}/#${view.fullPath}`
      const win = window.open(url, '_blank')
      win.focus()
    },
    closeSelectedTag(view) {
      this.$store.dispatch('tagsView/delView', view).then(({ visitedViews }) => {
        if (this.isActive(view)) {
          this.toLastView(visitedViews, view)
        }
      })
    },
    closeOthersTags() {
      this.$router.push(this.selectedTag)
      this.$store.dispatch('tagsView/delOthersViews', this.selectedTag).then(() => {
        this.moveToCurrentTag()
      })
    },
    closeAllTags(view) {
      this.$store.dispatch('tagsView/delAllViews').then(({ visitedViews }) => {
        if (this.affixTags.some(tag => tag.path === view.path)) {
          return
        }
        this.toLastView(visitedViews, view)
      })
    },
    toLastView(visitedViews, view) {
      const latestView = visitedViews.slice(-1)[0]
      if (latestView) {
        this.$router.push(latestView.fullPath)
      } else {
        // now the default is to redirect to the home page if there is no tags-view,
        // you can adjust it according to your needs.
        if (view.name === 'Dashboard') {
          // to reload home page
          this.$router.replace({ path: '/redirect' + view.fullPath })
        } else {
          this.$router.push('/')
        }
      }
    },
    openMenu(tag, e) {
      const menuMinWidth = 105
      const offsetLeft = this.$el.getBoundingClientRect().left // container margin left
      const offsetWidth = this.$el.offsetWidth // container width
      const maxLeft = offsetWidth - menuMinWidth // left boundary
      const left = e.clientX - offsetLeft + 15 // 15: margin right

      if (left > maxLeft) {
        this.left = maxLeft
      } else {
        this.left = left
      }

      this.top = e.clientY
      this.visible = true
      this.selectedTag = tag
    },
    closeMenu() {
      this.visible = false
    },
    closeHistoryBar() {
      this.$store.dispatch('app/closeHistoryBar')
    },
    handleScroll() {
      this.closeMenu()
    }
  }
}
</script>

<style lang="scss" scoped>
@import "~@/styles/variables.scss";

#tags-view-container.opend {
  right: 0px !important;
  #title-container {
    display: flex;
  }
  #tags-view-item {
    display: flex !important;
  }
}

#tags-view-container {
  top: 0;
  right: -170px;
  width: 180px;
  padding: 5px;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  position: absolute;
  transition: 0.3s;
  background: #fff;
  border-bottom: 1px solid #d8dce5;
  box-shadow: 0 1px 3px 0 rgba(16, 12, 12, 0.12), 0 0 3px 0 rgba(0, 0, 0, .04);
  #title-container {
    display: none;
    width: 100%;
    font-size: 12px;
    font-weight: 600;
    padding: 0px 5px;
    justify-content: space-between;
    i {
      border-radius: 5px;
      padding: 2px;
      &:hover {
        color: #fff;
        background-color: black;
      }
    }
  }
  #tags-view-item {
    width: 100%;
    display: none !important;
    transition: all 0.3s;
    align-items: center;
    justify-content: space-between;
    cursor: pointer;
    height: 26px;
    line-height: 26px;
    font-weight: 600;
    background-color: #cbd5e1;
    font-size: 12px;
    padding: 0px 15px;
    border-radius: 6px;
    margin-top: 5px;
    .title {
      display: block;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
    }
    span {
      border-radius: 50%;
      &:hover {
        color: black;
        background-color: #fff;
      }
    }
    &:hover {
      color: #fff;
      background: #64748b;
      height: 35px;
      .long-title {
        line-height: 14px;
        white-space: normal;
        transition: all 0.3s;
      }
    }
    &.active {
      color: #fff;
      background-color: #1e293b;
      justify-content: space-between;
      white-space: nowrap;
      display: flex;
      overflow: hidden;
      text-overflow: ellipsis;
    }
  }
}
</style>

