<template>
  <div>

    <!-- 상단 검색 -->
    <div class="search-wrap">
      <select>
        <option>전체</option>
        <option>IP</option>
        <option>회선번호</option>
        <option>SAID</option>
      </select>
      <input type="text" placeholder="IP주소 또는 회선번호, SAID를 입력해 주세요 (특수문자 입력 제외)">
      <button>검색</button>
    </div>

    <!-- 공지사항 -->
    <div class="notice-wrap">
      <div class="notice-title">
        <span>공지사항</span>
        <div class="notice-link-btn" @click="gotoNotice" />
      </div>
      <div class="notice-content">
        <el-carousel
          height="34px"
          direction="vertical"
          :autoplay="true"
          :interval="3000"
          indicator-position="none"
          arrow="always"
        >
          <el-carousel-item v-for="(item, index) in 4" :key="item">
            <div class="notice-content-item">
              <span>[ 긴급 ]</span>
              <span>2024-07-08</span>
              <span>IPMS 시스템 관련공지입니다. {{ index }}</span>
            </div>
          </el-carousel-item>
        </el-carousel>
      </div>
    </div>

    <!-- 탭 -->
    <div class="content-tab-wrap">
      <div v-for="(item, index) in tabs" :key="index" :class="{'active': selectionTab == index }" @click="selectionTab=index">{{ item }}</div>
    </div>

    <div v-show="selectionTab == 0" class="tab-content">
      <table>
        <tr>
          <th>노드국</th>
          <th>수용국</th>
          <th>오더번호</th>
          <th>SA ID</th>
          <th>전용회선번호</th>
          <th>접수일</th>
          <th>희망일</th>
          <th>고객명</th>
          <th>상품명</th>
          <th>이용내역</th>
        </tr>
        <tr>
          <td class="acc textcenter">노드국</td>
          <td class="acc textcenter">수용국</td>
          <td class="acc textcenter">오더번호</td>
          <td class="acc textcenter">SA ID</td>
          <td class="acc textcenter">전용회선번호</td>
          <td>접수일</td>
          <td>희망일</td>
          <td>고객명</td>
          <td>상품명</td>
          <td class="textcenter"><div class="btype1">사업용</div></td>
        </tr>
        <tr>
          <td class="acc textcenter">노드국</td>
          <td class="acc textcenter">수용국</td>
          <td class="acc textcenter">오더번호</td>
          <td class="acc textcenter">SA ID</td>
          <td class="acc textcenter">전용회선번호</td>
          <td>접수일</td>
          <td>희망일</td>
          <td>고객명</td>
          <td>상품명</td>
          <td class="textcenter"><div class="btype2">일반용</div></td>
        </tr>
        <tr>
          <td class="acc textcenter">노드국</td>
          <td class="acc textcenter">수용국</td>
          <td class="acc textcenter">오더번호</td>
          <td class="acc textcenter">SA ID</td>
          <td class="acc textcenter">전용회선번호</td>
          <td>접수일</td>
          <td>희망일</td>
          <td>고객명</td>
          <td>상품명</td>
          <td class="textcenter"><div class="btype2">일반용</div></td>
        </tr>
        <tr>
          <td class="acc textcenter">노드국</td>
          <td class="acc textcenter">수용국</td>
          <td class="acc textcenter">오더번호</td>
          <td class="acc textcenter">SA ID</td>
          <td class="acc textcenter">전용회선번호</td>
          <td>접수일</td>
          <td>희망일</td>
          <td>고객명</td>
          <td>상품명</td>
          <td class="textcenter"><div class="btype1">사업용</div></td>
        </tr>
        <tr>
          <td class="acc textcenter">노드국</td>
          <td class="acc textcenter">수용국</td>
          <td class="acc textcenter">오더번호</td>
          <td class="acc textcenter">SA ID</td>
          <td class="acc textcenter">전용회선번호</td>
          <td>접수일</td>
          <td>희망일</td>
          <td>고객명</td>
          <td>상품명</td>
          <td class="textcenter"><div class="btype2">일반용</div></td>
        </tr>
        <tr>
          <td class="acc textcenter">노드국</td>
          <td class="acc textcenter">수용국</td>
          <td class="acc textcenter">오더번호</td>
          <td class="acc textcenter">SA ID</td>
          <td class="acc textcenter">전용회선번호</td>
          <td>접수일</td>
          <td>희망일</td>
          <td>고객명</td>
          <td>상품명</td>
          <td class="textcenter"><div class="btype2">일반용</div></td>
        </tr>
      </table>
    </div>
    <div v-show="selectionTab == 1" class="tab-content">
      i-FOMS Config 감사
    </div>
    <div v-show="selectionTab == 2" class="tab-content">
      신인증 IP 최적화
    </div>
    <div v-show="selectionTab == 3" class="tab-content">
      조각 IP 최적화
    </div>
  </div>
</template>

<script>
export default {
  name: 'Login',
  data() {
    return {
      tabs: ['NeOSS 오더', 'i-FOMS Config 감사', '신인증 IP 최적화', '조각 IP 최적화'],
      selectionTab: 0
    }
  },
  methods: {
    goto(pageName) {
      this.$router.push({ name: pageName })
    },
    gotoNotice() {
      alert('공지사항')
    }
  }
}
</script>
<style scoped lang="scss">
// 상단 검색
div.search-wrap{
  display: flex;
  align-items: center;
  justify-content: center;
  gap:10px;
  // 검색어 구분 select
  >select{
    background-color:#252531;
    height:50px;
    border:none;
    border-radius: 25px;
    color:#FFFFFF;
    font-size:20px;
    padding: 0 20px 0 30px;
    min-width: 170px;
    -moz-appearance:none; /* Firefox */
    -webkit-appearance:none; /* Safari and Chrome */
    appearance:none;
    background-image: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAAH6ji2bAAAACXBIWXMAAAsTAAALEwEAmpwYAAAFHGlUWHRYTUw6Y29tLmFkb2JlLnhtcAAAAAAAPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS42LWMxNDUgNzkuMTYzNDk5LCAyMDE4LzA4LzEzLTE2OjQwOjIyICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtbG5zOmRjPSJodHRwOi8vcHVybC5vcmcvZGMvZWxlbWVudHMvMS4xLyIgeG1sbnM6cGhvdG9zaG9wPSJodHRwOi8vbnMuYWRvYmUuY29tL3Bob3Rvc2hvcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RFdnQ9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZUV2ZW50IyIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ0MgMjAxOSAoV2luZG93cykiIHhtcDpDcmVhdGVEYXRlPSIyMDI0LTA5LTI3VDEwOjM4OjIyKzA5OjAwIiB4bXA6TW9kaWZ5RGF0ZT0iMjAyNC0wOS0yN1QxMDozODo0MSswOTowMCIgeG1wOk1ldGFkYXRhRGF0ZT0iMjAyNC0wOS0yN1QxMDozODo0MSswOTowMCIgZGM6Zm9ybWF0PSJpbWFnZS9wbmciIHBob3Rvc2hvcDpDb2xvck1vZGU9IjMiIHBob3Rvc2hvcDpJQ0NQcm9maWxlPSJzUkdCIElFQzYxOTY2LTIuMSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDoyZGQ3ZWIxZC00ZWQwLTA1NDgtYmFmMC0xYmZmODdhOTI4ZTkiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6MmRkN2ViMWQtNGVkMC0wNTQ4LWJhZjAtMWJmZjg3YTkyOGU5IiB4bXBNTTpPcmlnaW5hbERvY3VtZW50SUQ9InhtcC5kaWQ6MmRkN2ViMWQtNGVkMC0wNTQ4LWJhZjAtMWJmZjg3YTkyOGU5Ij4gPHhtcE1NOkhpc3Rvcnk+IDxyZGY6U2VxPiA8cmRmOmxpIHN0RXZ0OmFjdGlvbj0iY3JlYXRlZCIgc3RFdnQ6aW5zdGFuY2VJRD0ieG1wLmlpZDoyZGQ3ZWIxZC00ZWQwLTA1NDgtYmFmMC0xYmZmODdhOTI4ZTkiIHN0RXZ0OndoZW49IjIwMjQtMDktMjdUMTA6Mzg6MjIrMDk6MDAiIHN0RXZ0OnNvZnR3YXJlQWdlbnQ9IkFkb2JlIFBob3Rvc2hvcCBDQyAyMDE5IChXaW5kb3dzKSIvPiA8L3JkZjpTZXE+IDwveG1wTU06SGlzdG9yeT4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz7MEhh8AAABAUlEQVQ4jc2TsWrDMBBAn4wXZy80ECj4awyF/kQDgaxZM/obDAbnJwKBfFC37vWoDtIZWT4lIm2ht+n0/O6kk421FokSwLQDwN6EO9OiNO0wAtWUmXGaaQQOQKeSxSLjq4TrBrgWcSJZXU2qdXybI1AlmAaoS78QKPygAWqgm4xBCLQD+od6zAbVm/wRWJp2eAc+gXOC+bLH7aoATsAT8KZBwCosrcETFPcYwjMoBgVexxD8xT1mG3Mju3JuyPveAR/A5UHPK7Cxx20vHfbAM+4/0AaeigY3whfvWMy5ws36nlhENW7UnWxod3hLnBTdEsbitRePuKOpIon//2x+XfgNdjpYziDJk5MAAAAASUVORK5CYII=");
    background-repeat: no-repeat;
    background-position-x: calc( 100% - 15px);
    background-position-y: 15px;
    border:1px solid #252531;
    box-sizing: border-box;
    outline: none !important;
    &:hover,
    &:focus,
    &:active{
      border:1px solid #2d80f7;
    }
  }
  // 검색어 input
  >input{
    background-color:#252531;
    height:50px;
    border-radius: 25px;
    color:#FFFFFF;
    font-size:20px;
    text-align: center;
    min-width:500px;
    box-sizing: border-box;
    transition: all 0.25s;
    border:1px solid #252531;
    outline: none !important;
    &::placeholder{
      color:#00839b;
      font-size:16px;
      font-weight: 400;
    }
    &:hover,
    &:focus{
      border:1px solid #2d80f7;
    }
  }
  // 검색버튼
  >button{
    background-color:#2d80f7;
    height:50px;
    border:none;
    color:#FFFFFF;
    font-size:20px;
    border-radius: 25px;
    width:130px;
    cursor:pointer;
    outline: none;
    transition: all 0.25s;
    background-image: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABIAAAASCAYAAAEhyb7BAAAACXBIWXMAAAsTAAALEwEAmpwYAAAH5WlUWHRYTUw6Y29tLmFkb2JlLnhtcAAAAAAAPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS42LWMxNDUgNzkuMTYzNDk5LCAyMDE4LzA4LzEzLTE2OjQwOjIyICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtbG5zOnhtcE1NPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvbW0vIiB4bWxuczpzdEV2dD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL3NUeXBlL1Jlc291cmNlRXZlbnQjIiB4bWxuczpwaG90b3Nob3A9Imh0dHA6Ly9ucy5hZG9iZS5jb20vcGhvdG9zaG9wLzEuMC8iIHhtbG5zOmRjPSJodHRwOi8vcHVybC5vcmcvZGMvZWxlbWVudHMvMS4xLyIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ0MgMjAxOSAoV2luZG93cykiIHhtcDpDcmVhdGVEYXRlPSIyMDI0LTA5LTI3VDExOjM3OjUwKzA5OjAwIiB4bXA6TWV0YWRhdGFEYXRlPSIyMDI0LTA5LTI3VDExOjM3OjUwKzA5OjAwIiB4bXA6TW9kaWZ5RGF0ZT0iMjAyNC0wOS0yN1QxMTozNzo1MCswOTowMCIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDplYjQxNDliOS1hZDc4LWU0NDQtOWVkNi1hYmRmYTkzNDI4YTgiIHhtcE1NOkRvY3VtZW50SUQ9ImFkb2JlOmRvY2lkOnBob3Rvc2hvcDo5Nzc2ZjU0Yi04YTZiLWJjNGEtODc2Yy0yMmZjNjM0Mjc0NDYiIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDoxNTQyMzMzNi1mNWI2LWM5NDMtYWNlMy1kY2Y4NmQwYzYxMzAiIHBob3Rvc2hvcDpDb2xvck1vZGU9IjMiIGRjOmZvcm1hdD0iaW1hZ2UvcG5nIj4gPHhtcE1NOkhpc3Rvcnk+IDxyZGY6U2VxPiA8cmRmOmxpIHN0RXZ0OmFjdGlvbj0iY3JlYXRlZCIgc3RFdnQ6aW5zdGFuY2VJRD0ieG1wLmlpZDoxNTQyMzMzNi1mNWI2LWM5NDMtYWNlMy1kY2Y4NmQwYzYxMzAiIHN0RXZ0OndoZW49IjIwMjQtMDktMjdUMTE6Mzc6NTArMDk6MDAiIHN0RXZ0OnNvZnR3YXJlQWdlbnQ9IkFkb2JlIFBob3Rvc2hvcCBDQyAyMDE5IChXaW5kb3dzKSIvPiA8cmRmOmxpIHN0RXZ0OmFjdGlvbj0ic2F2ZWQiIHN0RXZ0Omluc3RhbmNlSUQ9InhtcC5paWQ6ZWI0MTQ5YjktYWQ3OC1lNDQ0LTllZDYtYWJkZmE5MzQyOGE4IiBzdEV2dDp3aGVuPSIyMDI0LTA5LTI3VDExOjM3OjUwKzA5OjAwIiBzdEV2dDpzb2Z0d2FyZUFnZW50PSJBZG9iZSBQaG90b3Nob3AgQ0MgMjAxOSAoV2luZG93cykiIHN0RXZ0OmNoYW5nZWQ9Ii8iLz4gPC9yZGY6U2VxPiA8L3htcE1NOkhpc3Rvcnk+IDxwaG90b3Nob3A6RG9jdW1lbnRBbmNlc3RvcnM+IDxyZGY6QmFnPiA8cmRmOmxpPjU4NkZCNjBGMzUzNTg5RTBFRTk5ODI3N0RCRTlFMzFEPC9yZGY6bGk+IDxyZGY6bGk+NkFBMUQ1REJCQURGQUJDNzVCNDIyMUMyNzQzNTMxNTE8L3JkZjpsaT4gPHJkZjpsaT5BNTI1MDg2MzkxNzhFQUMxODRCOTNBQTBDM0JFQkNBMDwvcmRmOmxpPiA8cmRmOmxpPkJGODgyRTZFOTNGNUE4MzE1RDY4QjEyNUU3QTQ5RkRCPC9yZGY6bGk+IDxyZGY6bGk+QzEzQTc3QUUyNzZDQkIyM0NDODk2NUIxNDU3RTVCNjI8L3JkZjpsaT4gPHJkZjpsaT5hZG9iZTpkb2NpZDpwaG90b3Nob3A6ZjYzNzNmYmUtZjI2Ny1lNjRmLTkwYzktMDE1Y2VkNDkzNTA5PC9yZGY6bGk+IDxyZGY6bGk+eG1wLmRpZDozOTkzM2IxNC03NWYzLTM3NDItYmM0Ny0zMDliZTMwZjlmMjY8L3JkZjpsaT4gPHJkZjpsaT54bXAuZGlkOmRkM2FiMzk0LTI4YTYtMjA0My05MzgxLTJmMzE1MzMyNzRjYjwvcmRmOmxpPiA8L3JkZjpCYWc+IDwvcGhvdG9zaG9wOkRvY3VtZW50QW5jZXN0b3JzPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PtkC8qwAAAGmSURBVDiNnZI/SFZhFMZ/3+UzCkwkIiwEIYQCt5pSCgrByMXRpULn/tDg5FzQ4lAuKrQ1ReAQQjUGTkFDohAkif0BcakgKtSfg+fa8fal0oED9573eZ/zPOe8qKhfVbY/VGoqEaJO5OrQn2N1srykvi2ZUOfUYbdiu6h6RaUOdAG1aHM49wToKYBHQB8g0FRFnMukXUnGmDqVvZga5egDWoAnhKwHiSWzkz2gtqqD6vn47w7gpRJwRv0cxadV1jpwG+gFToSW9rBZB9bL4a420HNQnc2aRhqAdggvgM4G9ndETd0Aikr9GnAUGCs1tQT1HbVXfaH+Vhdzu28x8Vfh7DpwIBg/le12k7MIrJSgArgJ3AN+ATPAG2ANOBV7PAk8BEaB73/RxUZ+qDPqoX+MrMy74fFq9Qz1i/p4D4Kct9R19WyuF0Ab8HqvvaSYj1GcrlqbjvEf36eil+o79VjVWqGOh/f7anODyzV1SP2prqn9VUwBbAA3gCZgAXjO1uvOOQ8cAQaAD8AzYBm4kK39T3arS+Hio3q5+qb3G7NAB3AReA/UNwFUGHYdgYQYMwAAAABJRU5ErkJggg==");
    background-repeat: no-repeat;
    background-position: 20px 50%;
    text-indent: 10px;
    &:hover,
    &:focus{
      filter: brightness(1.25);
    }
    &:active{
      filter: brightness(0.75);
    }
  }
}

// 공지사항 타이틀
div.notice-wrap{
  border-top: 1px solid #0d465e;
  border-bottom: 1px solid #0d465e;
  padding: 20px;
  margin-top:35px;
  margin-bottom:35px;
  box-sizing: border-box;
  display: flex;
  align-items: center;
  gap:15px;
  >div.notice-title{
    display: flex;
    justify-content: flex-start;
    align-items: center;
    gap:10px;
    padding-right:15px;
    border-right: 1px solid #133040;
    &::before{
      content:"";
      display: inline-block;
      width:24px;
      height:26px;
      background-image: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAaCAYAAAHauKxlAAAACXBIWXMAAAsTAAALEwEAmpwYAAAH5WlUWHRYTUw6Y29tLmFkb2JlLnhtcAAAAAAAPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS42LWMxNDUgNzkuMTYzNDk5LCAyMDE4LzA4LzEzLTE2OjQwOjIyICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtbG5zOnhtcE1NPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvbW0vIiB4bWxuczpzdEV2dD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL3NUeXBlL1Jlc291cmNlRXZlbnQjIiB4bWxuczpwaG90b3Nob3A9Imh0dHA6Ly9ucy5hZG9iZS5jb20vcGhvdG9zaG9wLzEuMC8iIHhtbG5zOmRjPSJodHRwOi8vcHVybC5vcmcvZGMvZWxlbWVudHMvMS4xLyIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ0MgMjAxOSAoV2luZG93cykiIHhtcDpDcmVhdGVEYXRlPSIyMDI0LTA5LTI3VDExOjQ0OjI5KzA5OjAwIiB4bXA6TWV0YWRhdGFEYXRlPSIyMDI0LTA5LTI3VDExOjQ0OjI5KzA5OjAwIiB4bXA6TW9kaWZ5RGF0ZT0iMjAyNC0wOS0yN1QxMTo0NDoyOSswOTowMCIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDo0YjZlYmU3Yy1jNTc4LTYwNDctODQyYS02NTdjYzYwY2M1NmQiIHhtcE1NOkRvY3VtZW50SUQ9ImFkb2JlOmRvY2lkOnBob3Rvc2hvcDoxYTE4OWUxNS1lZDg0LWI3NDctYjhlNS04ZDhkN2IwZTAwMzIiIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDozZTgxZTFlMS1mZTNjLTE4NGQtODc0Yi1mNDJlMjViODdiN2IiIHBob3Rvc2hvcDpDb2xvck1vZGU9IjMiIGRjOmZvcm1hdD0iaW1hZ2UvcG5nIj4gPHhtcE1NOkhpc3Rvcnk+IDxyZGY6U2VxPiA8cmRmOmxpIHN0RXZ0OmFjdGlvbj0iY3JlYXRlZCIgc3RFdnQ6aW5zdGFuY2VJRD0ieG1wLmlpZDozZTgxZTFlMS1mZTNjLTE4NGQtODc0Yi1mNDJlMjViODdiN2IiIHN0RXZ0OndoZW49IjIwMjQtMDktMjdUMTE6NDQ6MjkrMDk6MDAiIHN0RXZ0OnNvZnR3YXJlQWdlbnQ9IkFkb2JlIFBob3Rvc2hvcCBDQyAyMDE5IChXaW5kb3dzKSIvPiA8cmRmOmxpIHN0RXZ0OmFjdGlvbj0ic2F2ZWQiIHN0RXZ0Omluc3RhbmNlSUQ9InhtcC5paWQ6NGI2ZWJlN2MtYzU3OC02MDQ3LTg0MmEtNjU3Y2M2MGNjNTZkIiBzdEV2dDp3aGVuPSIyMDI0LTA5LTI3VDExOjQ0OjI5KzA5OjAwIiBzdEV2dDpzb2Z0d2FyZUFnZW50PSJBZG9iZSBQaG90b3Nob3AgQ0MgMjAxOSAoV2luZG93cykiIHN0RXZ0OmNoYW5nZWQ9Ii8iLz4gPC9yZGY6U2VxPiA8L3htcE1NOkhpc3Rvcnk+IDxwaG90b3Nob3A6RG9jdW1lbnRBbmNlc3RvcnM+IDxyZGY6QmFnPiA8cmRmOmxpPjU4NkZCNjBGMzUzNTg5RTBFRTk5ODI3N0RCRTlFMzFEPC9yZGY6bGk+IDxyZGY6bGk+NkFBMUQ1REJCQURGQUJDNzVCNDIyMUMyNzQzNTMxNTE8L3JkZjpsaT4gPHJkZjpsaT5BNTI1MDg2MzkxNzhFQUMxODRCOTNBQTBDM0JFQkNBMDwvcmRmOmxpPiA8cmRmOmxpPkJGODgyRTZFOTNGNUE4MzE1RDY4QjEyNUU3QTQ5RkRCPC9yZGY6bGk+IDxyZGY6bGk+QzEzQTc3QUUyNzZDQkIyM0NDODk2NUIxNDU3RTVCNjI8L3JkZjpsaT4gPHJkZjpsaT5hZG9iZTpkb2NpZDpwaG90b3Nob3A6ZjYzNzNmYmUtZjI2Ny1lNjRmLTkwYzktMDE1Y2VkNDkzNTA5PC9yZGY6bGk+IDxyZGY6bGk+eG1wLmRpZDozOTkzM2IxNC03NWYzLTM3NDItYmM0Ny0zMDliZTMwZjlmMjY8L3JkZjpsaT4gPHJkZjpsaT54bXAuZGlkOmRkM2FiMzk0LTI4YTYtMjA0My05MzgxLTJmMzE1MzMyNzRjYjwvcmRmOmxpPiA8L3JkZjpCYWc+IDwvcGhvdG9zaG9wOkRvY3VtZW50QW5jZXN0b3JzPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/Pg5yYfUAAAEMSURBVEiJ3VVtasJAEH0jiwkEeqaiFxAFsfepPUt/eYUQcp4NEgwYKZ3+aDa4JiM7urbggyHsMm/mzUcSYmY4EAB2FxNIoHOOAdCfBpzZKMcL5nLWy01/+bL7BABsOzWeiaFExZ7GIIZcPADO8/xSVSt6i3JFAoCWmachztZaf2whuFpdFEKPoiganO2BBHWXHl/DpKqqwfJJRkRH0gyBiO6soV6sR59elr+VFIoWvxvLaZp+4Xpr3x2JkyTZa7I0oY7q5dPi9vdHgQ90jY1sLYCt+hujwU2Lp8X/JqiXG9Srt+DzGJ5jBidrbfTAXczBX5iZmcuy/M6y7GCMmcdOzMYYC+A1dmCH6IF/AAgZqnzwHXTYAAAAAElFTkSuQmCC');
      background-repeat: no-repeat;
    }
    >span{
      font-size:18px;
      color:#FFFFFF;
    }
    div.notice-link-btn{
      width:18px;
      height:18px;
      cursor:pointer;
      transition:all 0.25s;
      background-image: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABIAAAASCAYAAAEhyb7BAAAACXBIWXMAAAsTAAALEwEAmpwYAAAH5WlUWHRYTUw6Y29tLmFkb2JlLnhtcAAAAAAAPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS42LWMxNDUgNzkuMTYzNDk5LCAyMDE4LzA4LzEzLTE2OjQwOjIyICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtbG5zOnhtcE1NPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvbW0vIiB4bWxuczpzdEV2dD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL3NUeXBlL1Jlc291cmNlRXZlbnQjIiB4bWxuczpwaG90b3Nob3A9Imh0dHA6Ly9ucy5hZG9iZS5jb20vcGhvdG9zaG9wLzEuMC8iIHhtbG5zOmRjPSJodHRwOi8vcHVybC5vcmcvZGMvZWxlbWVudHMvMS4xLyIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ0MgMjAxOSAoV2luZG93cykiIHhtcDpDcmVhdGVEYXRlPSIyMDI0LTA5LTI3VDExOjQ5OjI3KzA5OjAwIiB4bXA6TWV0YWRhdGFEYXRlPSIyMDI0LTA5LTI3VDExOjQ5OjI3KzA5OjAwIiB4bXA6TW9kaWZ5RGF0ZT0iMjAyNC0wOS0yN1QxMTo0OToyNyswOTowMCIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpmYjgwMTkyNi0xMGZiLTQwNDktYTliNy1hNzg2NDUzNTlhMGIiIHhtcE1NOkRvY3VtZW50SUQ9ImFkb2JlOmRvY2lkOnBob3Rvc2hvcDoxNWRmNmNiMi0yNzhkLWQ3NDYtOWYyOC00ZTA3NzYzZjY5OTYiIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDphMTgwNjc2Ny1mMjBjLTVlNDAtYjk1ZS1lNWVmYjYyY2E2OTMiIHBob3Rvc2hvcDpDb2xvck1vZGU9IjMiIGRjOmZvcm1hdD0iaW1hZ2UvcG5nIj4gPHhtcE1NOkhpc3Rvcnk+IDxyZGY6U2VxPiA8cmRmOmxpIHN0RXZ0OmFjdGlvbj0iY3JlYXRlZCIgc3RFdnQ6aW5zdGFuY2VJRD0ieG1wLmlpZDphMTgwNjc2Ny1mMjBjLTVlNDAtYjk1ZS1lNWVmYjYyY2E2OTMiIHN0RXZ0OndoZW49IjIwMjQtMDktMjdUMTE6NDk6MjcrMDk6MDAiIHN0RXZ0OnNvZnR3YXJlQWdlbnQ9IkFkb2JlIFBob3Rvc2hvcCBDQyAyMDE5IChXaW5kb3dzKSIvPiA8cmRmOmxpIHN0RXZ0OmFjdGlvbj0ic2F2ZWQiIHN0RXZ0Omluc3RhbmNlSUQ9InhtcC5paWQ6ZmI4MDE5MjYtMTBmYi00MDQ5LWE5YjctYTc4NjQ1MzU5YTBiIiBzdEV2dDp3aGVuPSIyMDI0LTA5LTI3VDExOjQ5OjI3KzA5OjAwIiBzdEV2dDpzb2Z0d2FyZUFnZW50PSJBZG9iZSBQaG90b3Nob3AgQ0MgMjAxOSAoV2luZG93cykiIHN0RXZ0OmNoYW5nZWQ9Ii8iLz4gPC9yZGY6U2VxPiA8L3htcE1NOkhpc3Rvcnk+IDxwaG90b3Nob3A6RG9jdW1lbnRBbmNlc3RvcnM+IDxyZGY6QmFnPiA8cmRmOmxpPjU4NkZCNjBGMzUzNTg5RTBFRTk5ODI3N0RCRTlFMzFEPC9yZGY6bGk+IDxyZGY6bGk+NkFBMUQ1REJCQURGQUJDNzVCNDIyMUMyNzQzNTMxNTE8L3JkZjpsaT4gPHJkZjpsaT5BNTI1MDg2MzkxNzhFQUMxODRCOTNBQTBDM0JFQkNBMDwvcmRmOmxpPiA8cmRmOmxpPkJGODgyRTZFOTNGNUE4MzE1RDY4QjEyNUU3QTQ5RkRCPC9yZGY6bGk+IDxyZGY6bGk+QzEzQTc3QUUyNzZDQkIyM0NDODk2NUIxNDU3RTVCNjI8L3JkZjpsaT4gPHJkZjpsaT5hZG9iZTpkb2NpZDpwaG90b3Nob3A6ZjYzNzNmYmUtZjI2Ny1lNjRmLTkwYzktMDE1Y2VkNDkzNTA5PC9yZGY6bGk+IDxyZGY6bGk+eG1wLmRpZDozOTkzM2IxNC03NWYzLTM3NDItYmM0Ny0zMDliZTMwZjlmMjY8L3JkZjpsaT4gPHJkZjpsaT54bXAuZGlkOmRkM2FiMzk0LTI4YTYtMjA0My05MzgxLTJmMzE1MzMyNzRjYjwvcmRmOmxpPiA8L3JkZjpCYWc+IDwvcGhvdG9zaG9wOkRvY3VtZW50QW5jZXN0b3JzPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PqNpPWcAAAFkSURBVDiNjdO/TlRBFAbw373sGi1NKO1NAIkgja9h4lJY8gIQim2X0s7ORG0tZF/AgpZECwoTtzV2NvYqbDIUcwbGy92FL5ncmTPf+c6/uU1KyebRXxgPZLzBfpNSUtA8mfyBcYt1vL66bkLhMyY4wKgNv0N8wS6aIgKr2GvD5Ri/sV3cRviE3WIQGum/rOrwOniANbzHVilsgilmcf6Ae9hGGujHq/gmNIV0ju895PNlOY2r/emicCcYyG07KvUnuUvHcf4WhKeCXTCK7wouMMTjLqlgHoS53JJZ20NqglBC6yPdQB0uLSLdGOZSUk9Du1jXPxWqXsIL/IvU6vUy7mdy4UPXhX8tzrXQpvyCNsKhrGncr+CjPIs2BPf6hBZhIP8K89gPI8s5fmAH7xa9pRqP8Mv1wOF52Kc4K9Fuw0/sd2wHkVVTp93FGu7fIv6wa6iF3spTehZrGc6Cf4U7Pba74BLOsmhBWQUchwAAAABJRU5ErkJggg==');
      &:hover{
        filter:brightness(1.5);
      }
      &:active{
        filter:brightness(0.75);
      }
    }
  }
  >div.notice-content{
    width:600px;
    div.notice-content-item{
      position:relative;
      display: flex;
      align-items: center;
      justify-content: flex-start;
      gap:13px;
      background-color:#313340;
      line-height:34px;
      padding: 0 25px;
      border-radius: 17px;
      font-size:16px;
      cursor:pointer;
      transition: all 0.25s;
      margin-left:10px;
      &::before{
        content:"";
        position:absolute;
        display: inline-block;
        width:10px;
        height:10px;
        background-color:#313340;
        left:0;
        transform: translateX(-3px) rotate(45deg);
        transition: all 0.25s;
      }
      >span:nth-child(1){
        font-weight: 600;
        color:#FFFFFF;
      }
      >span:nth-child(2){
        font-weight: 200;
        color:#00839b;
        transition: all 0.25s;
      }
      >span:nth-child(3){
        font-weight: 400;
        color:#ffffff;
      }
      &:hover{
        background-color:#2d80f7;
        >span:nth-child(2){
          font-weight: 200;
          color:rgba(255,255,255,0.75);
        }
        &::before{
          background-color:#2d80f7;
        }
      }
      &:active{
        opacity: 0.5;
      }
    }
  }
}

div.content-tab-wrap{
  background-color:#17232e;
  display: flex;
  border-radius: 25px;
  overflow: hidden;
  >div{
    width:25%;
    text-align: center;
    line-height:50px;
    font-size: 18px;
    border-radius: 25px;
    color:#00839b;
    font-weight: 400;
    cursor:pointer;
    transition: all 0.25s;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    &:hover{
      color:#FFFFFF;
    }
  }
  >div.active{
    background-color:#2d80f7;
    color:#FFFFFF;
    font-weight: 600;
    cursor:pointer;
  }
}

.tab-content{
  margin-top:40px;

  >table{
    border-collapse: collapse;
    width:100%;
    tr{
      >th{
        background-color:rgba(49,51,64,0.5);
        font-size:12px;
        color:#00839b;
      }
      >td{
        font-size:14px;
        color:#00839b;
        font-weight: 500;
        transition: all 0.25s;
        >.btype1,
        >.btype2{
          display: inline-block;
          color:#FFFFFF;
          font-size:13px;
          border:1px solid #ffffff;
          line-height:20px;
          border-radius: 12px;
          padding: 0 10px;
          box-sizing: border-box;
        }
        >.btype1{
          border-color:#0d4f5f;
        }
        >.btype2{
          border-color:#275091;
        }
      }
      >td.acc{
        color:#FFFFFF;
      }
      >td.textcenter{
        text-align: center;
      }
      >th,>td{
        border:1px solid #134d65;
        padding: 10px 5px;
      }
      &:hover>td{
        background-color:rgba(49,51,64,0.4);
      }
      &:nth-child(even){
        background-color:rgba(49,51,64,0.25);
      }
    }
  }
}

</style>
