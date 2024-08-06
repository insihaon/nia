<template>
  <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko" lang="ko">
    <body class="main">
      <div id="skipnavi"><a href="#gnb">주메뉴 바로가기</a><a href="#content">본문 바로가기</a></div>
      <!-- wrap -->
      <div id="wrap" class="h-100">
        <!-- container -->
        <div id="container" class="main">
          <!-- search-->
          <div class="search_result">
            <fieldset>
              <legend>검색하기</legend>
              <el-select v-model="option" class="mr-1">
                <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
              <span>
                <el-input
                  v-model="value"
                  type="text"
                  class="txt"
                  placeholder="IP주소 또는 회선번호, SAID를 입력해 주세요.(특수문자 입력 제외)"
                  clearable
                  @keyup.enter.native="handleClickIpSearch"
                />
              </span>
              <span>
                <el-button icon="el-icon-search" round @click="handleClickIpSearch">
                  검색
                  <!-- <img src="@/assets/images/ipms/content/btn_main_search_off.gif" alt="검색" /> -->
                </el-button>
              </span>
            </fieldset>
          </div>
          <div class="container_inner">
            <Notice ref="notice" :is-dashboard="true" />
            <el-tabs type="card">
              <el-tab-pane v-for="tabItem in tabList" :key="tabItem.label">
                <span slot="label">{{ tabItem.label }}</span>
                <component :is="tabItem.component" :is-dashboard="true" />
              </el-tab-pane>
            </el-tabs>
          </div>
        </div>
      </div>
    </body>
  </html>
</template>

<script>
import { Base } from '@/min/Base.min'
import Notice from '@/views-ipms/menus/board/notice'

const routeName = 'IpmsMain'

export default {
  name: routeName,
  components: { Notice },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      activeName: 'first',
      tabList: [
        { label: 'NeOSS 오더', component: () => import('@/views-ipms/menus/ipAllocationMng/neOssOrder') },
        { label: 'i-FOMS Config 감사', component: () => import('@/views-ipms/components/IfomsConfig') },
        { label: '신인증 IP 최적화', component: () => import('@/views-ipms/components/NewCertificationOptimizationIP') },
        { label: '조각 IP 최적화', component: () => import('@/views-ipms/components/PieceIPOptimization') },
      ],
      options: [],
      option: 'CV0001',
      value: '',
    }
  },
  created () {
    this.options = this.CONSTANTS.ipms.ipInfoOptions
  },
  methods: {
    handleClickIpSearch() {
      this.$store.dispatch('ipms/setToParam', { option: this.option, value: this.value })
      this.$router.push('/ipInfoMng/ipInfoList')
    }
  },
}
</script>
<style lang="scss" scoped>
::v-deep .el-tabs__nav {
  width: 100%;
  .el-tabs__item {
    text-align: center;
    width: 25%;
    i {
      float: right;
      line-height: 36px;
      margin-right: 5px !important;
    }
  }
}
</style>
