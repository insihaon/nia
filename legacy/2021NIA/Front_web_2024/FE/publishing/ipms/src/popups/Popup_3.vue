<template>
  <el-dialog
    title="수용국 검색"
    :visible.sync="dialogVisible"
    width="800px"
    :before-close="handleClose"
  >
    <div class="popupContentTable">
      <table>
        <tr>
          <th>운용자</th>
          <td>
            <el-input value="홍길동" size="small" />
          </td>
          <td>
            <el-button type="primary" icon="el-icon-search" size="mini" round>조회</el-button>
          </td>
        </tr>
      </table>

      <div class="popupContentTableTitle">운용팀 조회 결과</div>
      <table>
        <tr>
          <th>선택</th>
          <th>수용국ID</th>
          <th>수용국명</th>
        </tr>
        <tr>
          <td class="textcenter"><el-radio /></td>
          <td class="textcenter">w00001</td>
          <td class="textcenter">ACCESS(강남)</td>
        </tr>
        <tr>
          <td class="textcenter"><el-radio /></td>
          <td class="textcenter">w00001</td>
          <td class="textcenter">ACCESS(강남)</td>
        </tr>
        <tr>
          <td class="textcenter"><el-radio /></td>
          <td class="textcenter">w00001</td>
          <td class="textcenter">ACCESS(강남)</td>
        </tr>
        <tr>
          <td class="textcenter"><el-radio /></td>
          <td class="textcenter">w00001</td>
          <td class="textcenter">ACCESS(강남)</td>
        </tr>
        <tr>
          <td class="textcenter"><el-radio /></td>
          <td class="textcenter">w00001</td>
          <td class="textcenter">ACCESS(강남)</td>
        </tr>
      </table>
    </div>
    <div class="popupContentTableBottom">
      <el-button type="primary" icon="el-icon-check" size="small" round>선택</el-button>
      <el-button type="primary" icon="el-icon-close" size="small" round @click="dialogVisible=false">닫기</el-button>
    </div>
  </el-dialog>
</template>
<script>
export default {
  name: 'Popup3',
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
    }
  }
}
</script>
<style scoped>
</style>
