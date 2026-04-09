<template>
  <el-dialog
    title="공지사항 상세"
    :visible.sync="dialogVisible"
    width="800px"
    :before-close="handleClose"
  >
    <div class="popupContentTable">
      <table>
        <tr>
          <th class="flex">
            <span class="tableSubject">[긴급][유선계] Secured IP대역(169.x.x.x) 조회 방법 안내...</span>
            <span><span>작성자</span>홍길동(aaaaa@aaaaa.com)</span>
          </th>
        </tr>
        <tr>
          <th class="flex">
            <span><span>팝업게시기간</span>2022-12-01 00:00:00 ~ 2022-12-01 00:00:00</span>
          </th>
        </tr>
        <tr>
          <th class="flex">
            <span><span>등록일</span>2022-12-01 00:00:00</span>
            <span><span>수정일</span>2022-12-01 00:00:00</span>
            <span><span>조회수</span>100</span>
          </th>
        </tr>
        <tr>
          <td>
            <textarea>동해물과 백두산이 마르고 닳도록 하느님이 보우하사 우리나라만세</textarea>
          </td>
        </tr>
      </table>
    </div>
    <div class="popupContentTableBottom">
      <el-button type="primary" icon="el-icon-edit-outline" size="small" round>수정</el-button>
      <el-button type="primary" icon="el-icon-delete" size="small" round>삭제</el-button>
      <el-button type="primary" icon="el-icon-close" size="small" round @click="dialogVisible=false">닫기</el-button>
    </div>
  </el-dialog>
</template>
<script>
export default {
  name: 'Popup1',
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
