<template>
  <el-dialog
    title="업로드"
    :visible.sync="dialogVisible"
    width="800px"
    :before-close="handleClose"
  >
    <div class="popupContentTable">
      <ul class="popupContentTableDes">
        <li>양식 다운로드 및 업로드</li>
        <li>
          주의사항
          <ul>
            <li>※ 양식 다운로드 : <span>계위 선택 및 수용국까지 선택하셔야합니다.</span></li>
            <li>※ Upload : <span>계위 선택을 하셔야하며 선택한 계위로 엑셀 데이터가 등록됩니다.<br>업로드 성공 또는 실패시 경고창이 나타납니다. 그 전까지 새로고침 혹은 페이지를 벗어나지 마세요.</span></li>
          </ul>
        </li>
      </ul>
      <table>
        <tr>
          <th>계위</th>
          <td class="flex">
            <el-select size="small" />
            <el-select size="small" />
            <el-select size="small" />
          </td>
        </tr>
        <tr>
          <th>수용국</th>
          <td class="flex">
            <el-select size="small" />
          </td>
        </tr>
        <tr>
          <th>파일업로드</th>
          <td class="flex">
            <el-upload
              action="https://www.typicode.com/posts/"
              :on-preview="handlePreview"
              :on-remove="handleRemove"
              :before-remove="beforeRemove"
              multiple
              :limit="3"
              :on-exceed="handleExceed"
              :file-list="fileList"
            >
              <el-button size="small" type="primary">파일선택</el-button>
              <div slot="tip" class="el-upload__tip">선택된 파일 없음</div>
            </el-upload>
          </td>
        </tr>
      </table>
    </div>
    <div class="popupContentTableBottom">
      <el-button type="primary" icon="el-icon-download" size="small" round>텍스트 양식 다운로드</el-button>
      <el-button type="primary" icon="el-icon-download" size="small" round>엑셀 양식 다운로드</el-button>
      <el-button type="primary" icon="el-icon-upload" size="small" round>업로드</el-button>
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
