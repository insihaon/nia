<template>
  <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko" lang="ko">
    <body class="main">
      <div id="skipnavi"><a href="#gnb">주메뉴 바로가기</a><a href="#content">본문 바로가기</a></div>

      <!-- wrap -->
      <div id="wrap">
        <!-- header -->

        <!-- //header -->

        <!-- container -->
        <div id="container" class="main">
          <!-- search-->
          <div class="search_result">
            <fieldset>
              <legend>검색하기</legend>
              <el-select v-model="value">
                <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
              <span>
                <input type="text" class="txt" placeholder="IP주소 또는 회선번호, SAID를 입력해 주세요." title="검색어 입력창" />
              </span>
              <span>
                <el-button>
                  <img src="@/assets/images/ipms/content/btn_main_search_off.gif" alt="검색" />
                </el-button>
              </span>
            </fieldset>
          </div>
          <div class="container_inner">
            <!-- // 공지사항 //-->
            <!-- <iframe src="main_notice.html" name="main_content" id="main_content" width="100%" scrolling="no" frameborder="no" style="height:30px;">
        </iframe> -->
            <Notice :is-dashboard="true" />
            <!-- // Default Tab Menu table // -->
            <!-- <iframe src="main_content01.html" name="main_content" width="100%" height="0" scrolling="no" frameborder="no" style="min-height:1600px;">
        </iframe> -->
            <el-tabs type="card">
              <el-tab-pane v-for="tabItem in tabList" :key="tabItem.label">
                <span slot="label">{{ tabItem.label }}<i class="el-icon-plus"></i></span>
                <component :is="tabItem.component" :is-dashboard="true" />
              </el-tab-pane>
            </el-tabs>
          </div>
        </div>
        <!-- footer -->
        <div id="footer">
          <div class="footer_above">
            <ul class="footer_menu">
              <li><a href="#none">정보제공</a></li>
              <li><a href="#none">이용약관 법적고지</a></li>
              <li>
                <a href="#none"><strong>개인정보취급방침</strong></a>
              </li>
              <li><a href="#none">이메일 무단 수집거부</a></li>
              <li><a href="#none">상호접속협정</a></li>
              <li class="last"><a href="#none">문의/연락처</a></li>
            </ul>
            <span>Copyright(c) 2014 kt IPMS. all rights reserved.</span>
          </div>
        </div>
        <!-- //footer -->
      </div>
      <!-- //wrap -->
      <div id="return_top"><a href="#skipnavi">페이지 맨 위로 이동</a></div>
    </body>
  </html>
</template>

<script>
import { Base } from '@/min/Base.min'
import Notice from '@/views-ipms/menus/board/Notice'

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
        { label: 'NeOSS 오더', component: () => import('@/views-ipms/menus/ipAllocationMng/NeOssOrder') },
        { label: 'i-FOMS Config 감사', component: () => import('@/views-ipms/components/IfomsConfig') },
        { label: '신인증 IP 최적화', component: () => import('@/views-ipms/components/NewCertificationOptimizationIP') },
        { label: '조각 IP 최적화', component: () => import('@/views-ipms/components/PieceIPOptimization') },
      ],
      options: [
        {
          value: 'IPv4',
          label: 'IPv4',
        },
        {
          value: 'IPv6',
          label: 'IPv6',
        },
        {
          value: 'SAID',
          label: 'SAID',
        },
        {
          value: '전용번호',
          label: '전용번호',
        },
      ],
      value: 'IPv4',
    }
  },
  computed: {},
  mounted() {},
  methods: {},
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
