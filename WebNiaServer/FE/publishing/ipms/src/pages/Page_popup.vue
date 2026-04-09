<template>
  <div>

    <!-- 페이지 타이틀 -->
    <div class="page-title-wrap">
      <div class="page-title">
        <span>팝업샘플</span>
      </div>
      <div class="page-loc">
        <i class="el-icon-s-home" />
        <span>팝업</span>
        <span>샘플</span>
      </div>
    </div>

    <el-button type="primary" dialog-visible="false" @click="showPopup(1)">공지사항상세</el-button>
    <el-button type="primary" @click="showPopup(2)">사용자권한등록</el-button>
    <el-button type="primary" @click="showPopup(3)">수용국검색</el-button>
    <el-button type="primary" @click="showPopup(4)">업로드관리등록</el-button>
    <el-button type="primary" @click="showPopup(5)">운영정보등록</el-button>
    <el-button type="primary" @click="showPopup(6)">host상세정보</el-button>
    <el-button type="primary" @click="showPopup(7)">ip분할</el-button>
    <el-button type="primary" @click="showPopup(8)">ip블록관리상세</el-button>
    <el-button type="primary" @click="showPopup(9)">ip블록생성</el-button>
    <el-button type="primary" @click="showPopup(10)">ip블록중복체크조회</el-button>
    <el-button type="primary" @click="showPopup(11)">ip주소노드이전등록</el-button>

    <PopUp1 ref="popup1" />
    <PopUp2 ref="popup2" />
    <PopUp3 ref="popup3" />
    <PopUp4 ref="popup4" />
    <PopUp5 ref="popup5" />
    <PopUp6 ref="popup6" />
    <PopUp7 ref="popup7" />
    <PopUp8 ref="popup8" />
    <PopUp9 ref="popup9" />
    <PopUp10 ref="popup10" />
    <PopUp11 ref="popup11" />
  </div>
</template>

<script>
import PopUp1 from '../popups/Popup_1.vue'
import PopUp2 from '../popups/Popup_2.vue'
import PopUp3 from '../popups/Popup_3.vue'
import PopUp4 from '../popups/Popup_4.vue'
import PopUp5 from '../popups/Popup_5.vue'
import PopUp6 from '../popups/Popup_6.vue'
import PopUp7 from '../popups/Popup_7.vue'
import PopUp8 from '../popups/Popup_8.vue'
import PopUp9 from '../popups/Popup_9.vue'
import PopUp10 from '../popups/Popup_10.vue'
import PopUp11 from '../popups/Popup_11.vue'
export default {
  name: 'Login',
  components: {
    PopUp1,
    PopUp2,
    PopUp3,
    PopUp4,
    PopUp5,
    PopUp6,
    PopUp7,
    PopUp8,
    PopUp9,
    PopUp10,
    PopUp11
  },
  data() {
    return {
    }
  },
  methods: {
    showPopup(no) {
      this.$refs['popup' + no].show()
    }
  }
}
</script>
<style scoped lang="scss">

</style>
