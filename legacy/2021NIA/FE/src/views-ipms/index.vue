<template>
  <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko" lang="ko">
    <body class="main">

      <div id="skipnavi"><a href="#gnb">주메뉴 바로가기</a><a href="#content">본문 바로가기</a></div>

      <!-- wrap -->
      <div id="wrap">

        <!-- header -->
        <div id="header">
          <div class="header_above">
            <h1><a href="#none"><img src="@/assets/images/ipms/common/h1_logo.gif" alt="IPMS"></a></h1>
            <h2 class="hide">주메뉴</h2>
            <div id="gnb">
              <ul class="gnb_menu">
                <!-- GNB 1Depth Menu의 <li>에 class="on"을 하면 2Depth 이하의 Sub Menu가 보입니다.-->
                <li>
                  <a href="#none">IP 배정관리</a>
                  <div class="gnb_sub menu01">
                    <dl>
                      <dt class="nochild"><a href="#none">IP 블록관리</a></dt>
                      <dt class="nochild"><a href="#none">IP 배정</a></dt>
                      <dt class="nochild"><a href="#none">IP 선배정</a></dt>
                    </dl>
                  </div>
                </li>
                <li>
                  <a href="#none">IP 할당관리</a>
                  <div class="gnb_sub menu02">
                    <dl>
                      <dt class="nochild"><a href="#none">IP 할당</a></dt>
                      <dt>HOST 관리</dt>
                      <dd>
                        <ul>
                          <li><a href="#none">HOST 유형 관리</a></li>
                          <li><a href="#none">HOST 정보 관리</a></li>
                        </ul>
                      </dd>
                      <dt>타 서비스 관리</dt>
                      <dd>
                        <ul>
                          <li><a href="#none">HOST 정보 관리</a></li>
                          <li><a href="#none">IP 블럭 정보 관리</a></li>
                        </ul>
                      </dd>
                    </dl>
                  </div>
                </li>
                <li>
                  <a href="#none">IP 정보관리</a>
                  <div class="gnb_sub menu03">
                    <dl class="sub03">
                      <dt class="nochild"><a href="#none">IP 정보조회</a></dt>
                    </dl>
                  </div>
                </li>
                <li>
                  <a href="#none">IP 정보통계</a>
                  <div class="gnb_sub menu04">
                    <dl>
                      <dt class="nochild"><a href="#none">IP 사용율 통계 현황</a></dt>
                      <dt class="nochild"><a href="#none">DB 현행화율 현황</a></dt>
                      <dt class="nochild"><a href="#none">최적화 대상 정보 현황</a></dt>
                      <dt class="nochild"><a href="#none">조각 IP 블럭 사용율 통계 현황</a></dt>
                      <dt class="nochild"><a href="#none">IP 현행화율 분석 통계 현황</a></dt>
                      <dt class="nochild"><a href="#none">사용자(운용자) 연동정보 현황</a></dt>
                    </dl>
                  </div>
                </li>
                <li>
                  <a href="#none">운용정보관리</a>
                  <div class="gnb_sub menu05">
                    <dl>
                      <dt>조직관리</dt>
                      <dd>
                        <ul>
                          <li><a href="#none">조직 기준관리</a></li>
                          <li><a href="#none">국사 관리</a></li>
                          <li><a href="#none">가상 국사관리</a></li>
                        </ul>
                      </dd>
                      <dt>상품관리</dt>
                      <dd>
                        <ul>
                          <li><a href="#none">서비스망관리</a></li>
                          <li><a href="#none">서비스 기준관리</a></li>
                          <li><a href="#none">상품관리</a></li>
                        </ul>
                      </dd>
                      <dt>운용조직관리</dt>
                      <dd>
                        <ul>
                          <li><a href="#none">운영조직 권한관리</a></li>
                          <li><a href="#none">운용국 관리</a></li>
                          <li><a href="#none">서비스 관리</a></li>
                        </ul>
                      </dd>
                      <dt>사용자관리</dt>
                      <dd>
                        <ul>
                          <li><a href="#none">사용자 정보 관리</a></li>
                          <li><a href="#none">관리자 정보 관리</a></li>
                          <li><a href="#none">사용자 접속 현황</a></li>
                        </ul>
                      </dd>
                    </dl>
                    <dl>
                      <dt>메뉴관리</dt>
                      <dd>
                        <ul>
                          <li><a href="#none">화면 관리</a></li>
                          <li><a href="#none">메뉴 관리</a></li>
                          <li><a href="#none">메뉴 권한 관리</a></li>
                        </ul>
                      </dd>
                      <dt>기초정보</dt>
                      <dd>
                        <ul>
                          <li><a href="#none">코드관리</a></li>
                        </ul>
                      </dd>
                      <dt>알림기능</dt>
                      <dd>
                        <ul>
                          <li><a href="#none">이메일 발송 대상 관리</a></li>
                          <li><a href="#none">이메일 발송 현황</a></li>
                        </ul>
                      </dd>
                      <dt>시설기준정보관리</dt>
                      <dd>
                        <ul>
                          <li><a href="#none">상위(고정)시설 정보관리</a></li>
                        </ul>
                      </dd>
                      <dt>네트워크정보관리</dt>
                      <dd>
                        <ul>
                          <li><a href="#none">PING 정보 처리</a></li>
                          <li><a href="#none">TRACE 정보 처리</a></li>
                        </ul>
                      </dd>
                    </dl>
                    <dl>
                      <dt>사후감사정보관리</dt>
                      <dd>
                        <ul>
                          <li><a href="#none">IFOMS 시설 config 구성 정보관리</a></li>
                        </ul>
                      </dd>
                      <dt>DHCP 임계치관리</dt>
                      <dd>
                        <ul>
                          <li><a href="#none">임계치 레벨 관리</a></li>
                          <li><a href="#none">DHCP 임계 기준 관리</a></li>
                        </ul>
                      </dd>
                      <dt>최적화 기준 관리(조각IP)</dt>
                      <dd>
                        <ul>
                          <li><a href="#none">최적화 기준 정보 관리</a></li>
                        </ul>
                      </dd>
                      <dt>최적화 기준 관리</dt>
                      <dd>
                        <ul>
                          <li><a href="#none">전용 상품별 배정률 정보 관리</a></li>
                          <li><a href="#none">전용 상품별 여유 임계치 정보 관리</a></li>
                        </ul>
                      </dd>
                      <dt>WHOIS 정보공개 관리</dt>
                      <dd>
                        <ul>
                          <li><a href="#none">WHOIS 정보공개 관리</a></li>
                        </ul>
                      </dd>
                    </dl>
                    <dl>
                      <dt>A/S 관리</dt>
                      <dd>
                        <ul>
                          <li><a href="#none">A/S 관리</a></li>
                        </ul>
                      </dd>
                      <dt>연동관리</dt>
                      <dd>
                        <ul>
                          <li><a href="#none">접속정보 관리</a></li>
                          <li><a href="#none">웹서비스 인터페이스 관리</a></li>
                          <li><a href="#none">배치 인터페이스 관리</a></li>
                          <li><a href="#none">웹서비스 처리 현황</a></li>
                          <li><a href="#none">배치 처리 현황</a></li>
                          <li><a href="#none">NEOSS 시설 구성 정보 현황</a></li>
                          <li><a href="#none">불일치 데이터 현행화 현황</a></li>
                        </ul>
                      </dd>
                      <dt>티켓관리</dt>
                      <dd>
                        <ul>
                          <li><a href="#none">티켓 유형 정보관리</a></li>
                          <li><a href="#none">단계 정보 관리</a></li>
                          <li><a href="#none">상태 정보 관리</a></li>
                          <li><a href="#none">티켓 기준 정보 관리</a></li>
                          <li><a href="#none">티켓 처리 결과 현황</a></li>
                        </ul>
                      </dd>
                    </dl>
                  </div>
                </li>
              </ul>
            </div>
            <!-- //GNB END-->

            <div class="utill_group">
              <ul class="utill_menu">
                <li><strong>유재석 님</strong></li>
                <li>
                  <el-button>
                    <img src="@/assets/images/ipms/common/btn_logout_off.gif" alt="로그아웃" @click="$router.push({ path: '/login' })">
                  </el-button>
                  <!-- <a href="login.html">
                <input type="image" src="@/assets/images/ipms/common/btn_logout_off.gif" alt="로그아웃" id="" onmouseover="menuOver(this);" onmouseout="menuOut(this);" />
              </a> -->
                </li>
                <!-- <li class="last">
              <el-button @click="handleLogin">
                <img src="@/assets/images/ipms/common/btn_help_off.gif" alt="HELP" @click="handleLogin">
              </el-button>
              <div class="help_menu">
                <ul>
                  <li class="first"><a href="#none">공지사항</a></li>
                  <li><a href="#none">Q&A</a></li>
                  <li><a href="#none">게시판</a></li>
                  <li><a href="#none">이용안내</a></li>
                </ul>
              </div>
            </li> -->
                <!--
            <li>
              <input type="text" class="util_search" placeholder="IP SEARCH" title="search" id="" />
              <a href="#none">
                <input type="image" src="images/common/btn_util_search_off.gif" alt="search" id="" class="sc_btn" onmouseover="menuOver(this);" onmouseout="menuOut(this);" />
              </a>
            </li>
            -->
              </ul>
            </div><!-- utill_group END-->

          </div><!-- header_above END-->
        </div><!-- //header -->

        <!-- container -->
        <div id="container" class="main">

          <!-- search-->
          <div class="search_result">
            <fieldset>
              <legend>검색하기</legend>
              <div class="selectbox">
                <a href="#none" class="txtArea" onclick="selectShowId(1);"><span id="scTitle1">전체</span></a>
                <ul id="scList1" onclick="selectHideId(1);">
                  <li onclick="fncChangeMainTitle(1, this);">IP 주소</li>
                  <!--<li class="on" onclick="fncChangeMainTitle(1, this);">IP 주소 복수검색</li>-->
                  <li onclick="fncChangeMainTitle(1, this);">SAID</li>
                  <li class="last" onclick="fncChangeMainTitle(1, this);">전용선/회선번호</li>
                </ul>
              </div>
              <span>
                <input type="text" class="txt" placeholder="IP주소 또는 회선번호, SAID를 입력해 주세요." title="검색어 입력창">
              </span>
              <span>
                <el-button>
                  <img src="@/assets/images/ipms/content/btn_main_search_off.gif" alt="검색">
                </el-button>
              </span>
            </fieldset>
          </div>

          <div class="container_inner">
            <!-- // 공지사항 //-->
            <!-- <iframe src="main_notice.html" name="main_content" id="main_content" width="100%" scrolling="no" frameborder="no" style="height:30px;">
        </iframe> -->

            <!-- // Default Tab Menu table // -->
            <!-- <iframe src="main_content01.html" name="main_content" width="100%" height="0" scrolling="no" frameborder="no" style="min-height:1600px;">
        </iframe> -->
            <el-tabs type="card">
              <el-tab-pane label="NeOSS 오더">
                <component :is="componentLoader" />
              </el-tab-pane>
              <el-tab-pane label="i-FOMS Config 감사">i-FOMS Config 감사</el-tab-pane>
              <el-tab-pane label="신인증 IP 최적화">신인증 IP 최적화</el-tab-pane>
              <el-tab-pane label="조각 IP 최적화">조각 IP 최적화</el-tab-pane>
            </el-tabs>
          </div>
        </div>

        <!-- footer -->
        <div id="footer">
          <div class="footer_above">
            <ul class="footer_menu">
              <li><a href="#none">정보제공</a></li>
              <li><a href="#none">이용약관 법적고지</a></li>
              <li><a href="#none"><strong>개인정보취급방침</strong></a></li>
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
import NeOssOrder from '@/views-ipms/mainComponent/NeOssOrder.vue'

const routeName = 'IpmsMain'

export default {
  name: routeName,
  components: { NeOssOrder },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      activeName: 'first'

    }
  },
  computed: {
    componentLoader() {
      return NeOssOrder
    },
  },
  mounted() {},
  methods: {
  },
}
</script>
<style lang="css" scoped>
</style>
