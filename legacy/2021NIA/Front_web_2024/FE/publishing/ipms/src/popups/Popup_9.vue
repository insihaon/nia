<template>
  <el-dialog
    title="IP블록생성"
    :visible.sync="dialogVisible"
    width="800px"
    :before-close="handleClose"
  >
    <div class="popupContentTable">
      <div class="popupContentTableTitle">IP블록생성</div>
      <table>
        <tr>
          <th>공인/사설</th>
          <td><el-select size="small" /></td>
          <th>생성차수</th>
          <td><el-input size="small" /></td>
        </tr>
        <tr>
          <th>서비스망</th>
          <td><el-select size="small" /></td>
          <th>작업일시</th>
          <td><el-select size="small" /></td>
        </tr>
        <tr>
          <th>비고</th>
          <td colspan="3"><textarea style="min-height:50px;" /></td>
        </tr>
      </table>

      <table style="margin: 10px 0">
        <tr>
          <th>IP주소</th>
          <td class="flex">
            <el-input size="small" />
            <el-button size="small" type="primary" round>추가</el-button>
          </td>
        </tr>
      </table>

      <table>
        <tr>
          <th>순번</th>
          <th>IP블록</th>
          <th>시작 IP</th>
          <th>끝 IP</th>
          <th>단위블록수</th>
          <th>총IP수</th>
          <th>삭제</th>
        </tr>
        <tr>
          <td class="textcenter">-</td>
          <td class="textcenter">-</td>
          <td class="textcenter">-</td>
          <td class="textcenter">-</td>
          <td class="textcenter">-</td>
          <td class="textcenter">-</td>
          <td class="textcenter"><el-button type="danger" size="mini" icon="el-icon-delete" circle /></td>
        </tr>
      </table>

      <div class="popupContentTableBottom">
        <el-button type="primary" icon="el-icon-refresh" size="mini" round>초기화</el-button>
        <el-button type="primary" icon="el-icon-check" size="mini" round>등록</el-button>
      </div>

      <div class="popupContentTableTitle">IP블록 처리 결과</div>
      <div class="popupContentBox">
        -
      </div>

    </div>
    <div class="popupContentTableBottom">
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
