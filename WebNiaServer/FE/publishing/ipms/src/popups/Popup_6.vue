<template>
  <el-dialog
    title="HOST 상세 정보"
    :visible.sync="dialogVisible"
    width="800px"
    :before-close="handleClose"
  >
    <div class="popupContentTable">
      <div class="popupContentTableTitle">공통항목</div>
      <table>
        <tr>
          <th>IP</th>
          <td>
            <el-select size="small" />
          </td>
          <th>용도</th>
          <td>
            <el-select size="small" />
          </td>
          <th>IP버전</th>
          <td>IPv4</td>
        </tr>
      </table>

      <div class="popupContentTableTitle">세부항목</div>
      <table>
        <tr>
          <th />
          <th colspan="2">기본정보</th>
          <th colspan="2">시설정보</th>
          <th colspan="2">회선정보</th>
        </tr>
        <tr>
          <td class="textcenter" rowspan="4"><el-radio /></td>
          <th class="textcenter">수용국</th>
          <td class="textcenter">영동국사</td>
          <th class="textcenter">I/F명</th>
          <td class="textcenter" />
          <th class="textcenter">서비스</th>
          <td class="textcenter" />
        </tr>
        <tr>
          <th class="textcenter">HOST유형</th>
          <td class="textcenter">NeOSS SVC-VOIP</td>
          <th class="textcenter">장비명</th>
          <td class="textcenter">(주)KT</td>
          <th class="textcenter">상품명</th>
          <td class="textcenter">PREMIUM /<br>WiFi Solo on 인터넷 Only Premium</td>
        </tr>
        <tr>
          <th class="textcenter">외부연동</th>
          <td class="textcenter">NeOSS_CODE</td>
          <th class="textcenter">모델명</th>
          <td class="textcenter">-</td>
          <th class="textcenter">SAID</th>
          <td class="textcenter">1654634165</td>
        </tr>
        <tr>
          <th class="textcenter">전용회선</th>
          <td class="textcenter" colspan="5">6549846546464</td>
        </tr>
      </table>
    </div>
    <div class="popupContentTableBottom">
      <el-button type="primary" icon="el-icon-check" size="small" round>대표아이피 지정</el-button>
      <el-button type="primary" icon="el-icon-upload" size="small" round>용도저장</el-button>
      <el-button type="primary" icon="el-icon-close" size="small" round @click="dialogVisible=false">닫기</el-button>
    </div>
  </el-dialog>
</template>
<script>
export default {
  name: 'Popup4',
  data() {
    return {
      dialogVisible: false
    }
  },
  methods: {
    handleClose() {
      this.dialogVisible = false
    },
    show() {
      this.dialogVisible = true
    },
    handleRemove(file, fileList) {
      console.log(file, fileList)
    },
    handlePreview(file) {
      console.log(file)
    },
    handleExceed(files, fileList) {
    },
    beforeRemove(file, fileList) {
    }
  }
}
</script>
<style scoped>
</style>
