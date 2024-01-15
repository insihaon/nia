<template>
  <html>
    <body>

      <div class="mainWrap">

        <div class="contentWrap">
          <div class="slideBottom slide1" />
          <div class="slideBottom slide2" />
          <div class="slideBottom slide3" />

          <div class="mainVisualWrap">
            <img src="@/assets/images/datahub/main_center.png">
            <div><span style="font-weight: bold;">{{ '데이터허브' }} </span><span>관리시스템</span></div>
            <div>DATA HUB MANAGEMENT SYSTEM</div>
          </div>

          <div class="mainCenterBoxWrap">
            <div class="menuItem">
              <div class="menuTitleWrap">
                <div><span>{{ dataHubRoute[1].meta.title }}</span></div>
                <div>{{ dataHubRoute[1].path.substring(1) }}</div>
              </div>
              <div class="quickBtn">{{ handlePageText }}</div>
              <div class="itemIcon">
                <img src="@/assets/images/datahub/menu_item_icon_front_01.png">
              </div>
              <div class="menuBtnWrap">
                <template v-for="menu in dataHubRoute">
                  <template v-for="childMenu in menu.children">
                    <div v-show="menu.path === '/apiManagement'" v-if="hasPermission(childMenu)" :key="childMenu.path">
                      <router-link :to="{name : childMenu.name}">
                        <div>
                          {{ childMenu.meta.title }}
                        </div>
                      </router-link>
                    </div>
                  </template>
                </template>
              </div>
            </div>

            <div class="menuItem">
              <div class="menuTitleWrap">
                <div><span>{{ dataHubRoute[2].meta.title }}</span></div>
                <div>{{ dataHubRoute[2].path.substring(1) }}</div>
              </div>
              <div class="quickBtn">{{ handlePageText }}</div>
              <div class="itemIcon">
                <img src="@/assets/images/datahub/menu_item_icon_front_02.png">
              </div>
              <div class="menuBtnWrap">
                <template v-for="menu in dataHubRoute">
                  <template v-for="childMenu in menu.children">
                    <div v-show="menu.path === '/metaData'" v-if="hasPermission(childMenu)" :key="childMenu.path">
                      <router-link :to="{name : childMenu.name}">
                        <div>
                          {{ childMenu.meta.title }}
                        </div>
                      </router-link>
                    </div>
                  </template>
                </template>
              </div>
            </div>

            <div class="menuItem">
              <div class="menuTitleWrap">
                <div><span>{{ dataHubRoute[3].meta.title }}</span></div>
                <div>{{ dataHubRoute[3].path.substring(1) }}</div>
              </div>
              <div class="quickBtn">{{ handlePageText }}</div>
              <div class="itemIcon">
                <img src="@/assets/images/datahub/menu_item_icon_front_03.png">
              </div>
              <div class="menuBtnWrap">
                <template v-for="menu in dataHubRoute">
                  <div v-for="childMenu in menu.children" v-show="menu.path === '/dashBoard'" :key="childMenu.path">
                    <router-link :to="{name : childMenu.name}">
                      <div>
                        {{ childMenu.meta.title }}
                      </div>
                    </router-link>
                  </div>
                </template>
              </div>
            </div>

            <div class="menuItem">
              <div class="menuTitleWrap">
                <div><span>{{ dataHubRoute[4].meta.title }}</span></div>
                <div>{{ dataHubRoute[4].path.substring(1) }}</div>
              </div>
              <div class="quickBtn">{{ handlePageText }}</div>
              <div class="itemIcon">
                <img src="@/assets/images/datahub/menu_item_icon_front_04.png">
              </div>
              <div class="menuBtnWrap">
                <template v-for="menu in dataHubRoute">
                  <div v-for="childMenu in menu.children" v-show="menu.path === '/manager'" :key="childMenu.path">
                    <router-link :to="{name : childMenu.name}">
                      <div>
                        {{ childMenu.meta.title }}
                      </div>
                    </router-link>
                  </div>
                </template>
              </div>
            </div>

          </div>
        </div>

        <div class="bottomWrap">
          <div class="bottomLogo" />
          <div class="bottomCopyright">
            <span>(주)케이티 대표이사 김영섭 경기도 성남시 분당구 불정로 90 (정자동) 사업자등록번호 : 102-81-42945 통신판매업신고 : 2002-경기성남-0048</span>
            <span>고객센터 : [모바일] 휴대폰+114(무료), 1588-0010(유료) [인터넷/TV/전화] 유선전화(국번없이) 100, 휴대폰(지역번호)+100</span>
            <span>Copyrightⓒ2018 KT corp. All rights reserved.</span>
          </div>
          <div class="bottomRightLogo">
            <span style="font-weight: bold;">{{ '데이터허브' }} </span>
            <span>관리시스템</span>
          </div>
        </div>

      </div>
    </body>
  </html>
</template>

<script>
import { AppOptions } from '@/class/appOptions'
import { Base } from '@/min/Base.min'
export const _ = { AppOptions }
import { dataHubRoute } from '@/router/dataHub/index'
import { mapState } from 'vuex'
import { findIntersection } from '@/utils'

const routeName = 'datahubMain'

export default {
  name: routeName,
  components: {

  },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      AppOptions: AppOptions,
      handlePageText: '바로가기'
}
  },
  computed: {
      ...mapState({
          roles: state => state.user.roles,
      }),
      dataHubRoute() {
        return dataHubRoute
      },
  },
  mounted() {
  },
  methods: {
    hasPermission(item) {
      const menuRoles = item?.meta?.grant ?? ['ROLE_ADMIN', 'ROLE_USER']
      const myRoles = this.roles ?? ['ROLE_USER']

      const intersection = findIntersection(menuRoles, myRoles)
      return intersection.length > 0
    },
  }
}

</script>
<style lang="css" scoped>
@import "~@/assets/css/style_main.css";

</style>
