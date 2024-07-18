<template>
  <el-header style="height: 50px;padding: 0px" class="wrap">
    <div id="header">
      <div class="header_above">
        <h1 style="cursor: pointer;">
          <img src="@/assets/images/ipms/common/h1_logo.gif" alt="IPMS" @click="$router.push({ path: '/ipms/index' })" />
        </h1>
        <div id="gnb">
          <ul class="gnb_menu">
            <li v-for="route in permission_routes" v-if="!route.hidden && route.meta" :key="route.path" @mouseover="(e)=> onMouseOver(e, route.menuIdx)" @mouseout="(e)=> onMouseOut(e, route.menuIdx)">
              <span @mouseover="(e)=> onMouseOver(e, route.menuIdx)">
                {{ route.meta.title }}
              </span>
              <div v-if="route.children" class="gnb_sub" :class="{['menu'+route.menuIdx]:true }" @mouseover="(e)=> onMouseOver(e, route.menuIdx)" @mouseout="(e)=> onMouseOut(e, route.menuIdx)">
                <dl v-for="child in route.children" v-if="child.meta" :key="child.path" :class="{'no-child': !child.children}">
                  <router-link v-if="child.meta && !child.children" :to="resolvePath(child.path, route.path)">
                    {{ child.meta.title }}
                  </router-link>
                  <template v-else>
                    <dt>{{ child.meta.title }}</dt>
                    <dd>
                      <ul>
                        <li v-for="child2 in child.children" :key="child2.path">
                          <router-link v-if="child2.meta && !child2.children" :to="resolvePath(child2.path, child.path)">
                            {{ child2.meta.title }}
                          </router-link>
                        </li>
                      </ul>
                    </dd>
                  </template>
                </dl>
              </div>
            </li>
          </ul>
        </div>
        <ul class="utill_menu">
          <li>
            <strong>{{ $store.state.user.name || '' }}</strong>
          </li>
          <li>
            <el-button style="border: none">
              <img src="@/assets/images/ipms/common/btn_logout_off.gif" alt="로그아웃" @click="$router.push({ path: '/login' })" />
            </el-button>
          </li>
        </ul>
      </div>
    </div>
  </el-header>
</template>

<script>
import { Base } from '@/min/Base.min'
import { mdiHistory } from '@mdi/js'
import { mapState, mapGetters } from 'vuex'
import path from 'path'
import { isExternal } from '@/utils/validate'

const routeName = 'NavBar'
export default {
  name: routeName,
  src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
  components: {},
  extends: Base,
  data() {
    return {}
  },
  computed: {
    ...mapGetters(['permission_routes']),
  },
  created() {
    // const header = document.querySelector('#gnb')
    // header?.addEventListener('mouseover', (event) => {
    //   console.log('a')
    // })
  },
  mounted() {
  },
  methods: {
    onMouseOver(event, menuIdx) {
      document.querySelector('#gnb').children[0].children[menuIdx].classList.add('on')
    },
    onMouseOut(event, menuIdx) {
      document.querySelector('#gnb').children[0].children[menuIdx].classList.remove('on')
    },
    resolvePath(routePath, basePath) {
      if (isExternal(routePath)) {
        return routePath
      }
      if (isExternal(basePath)) {
        return basePath
      }
      return path.resolve(basePath, routePath)
    },
  },
}
</script>

<style lang="scss" scoped></style>
