<template>
  <el-dialog
    title="사용자 권한 등록"
    :visible.sync="dialogVisible"
    width="800px"
    :before-close="handleClose"
  >
    <div class="popupContentTable">
      <table>
        <tr>
          <th>운용자</th>
          <th>
            <el-input value="홍길동" size="small" />
          </th>
        </tr>
      </table>

      <div class="popupContentTableTitle">권한상세</div>
      <table>
        <tr>
          <th>사용자명</th>
          <th>권한등급</th>
          <th>서비스망</th>
          <th>본부</th>
          <th>노드</th>
        </tr>
        <tr>
          <td class="textcenter"><span>홍길동</span></td>
          <td class="textcenter"><span>시스템관리자</span></td>
          <td class="textcenter"><span>-</span></td>
          <td class="textcenter"><span>-</span></td>
          <td class="textcenter"><span>-</span></td>
        </tr>
      </table>

      <div class="popupContentTableTitle">권한입력</div>
      <table>
        <tr>
          <th>권한등급</th>
          <td>
            <el-select size="small" />
          </td>
          <th>계위</th>
          <td class="flex">
            <el-select size="small" />
            <el-select size="small" />
            <el-select size="small" />
          </td>
        </tr>
      </table>
      <div class="popupContentTableBottom">
        <el-button type="primary" icon="el-icon-plus" size="mini" round>추가</el-button>
      </div>

      <div class="popupContentTableTitle">등록예정 권한 정보</div>
      <table>
        <tr>
          <th>사용자명</th>
          <th>권한등급</th>
          <th>서비스망</th>
          <th>본부</th>
          <th>노드</th>
          <th>삭제</th>
        </tr>
        <tr>
          <td class="textcenter">홍길동</td>
          <td class="textcenter">시스템관리자</td>
          <td class="textcenter">PREMIUM</td>
          <td class="textcenter">미디어운용센터</td>
          <td class="textcenter">미디어계획팀</td>
          <td class="textcenter"><el-button class="el-icon-delete" type="danger" size="mini" circle /></td>
        </tr>
      </table>

    </div>
    <div class="popupContentTableBottom">
      <el-button type="primary" icon="el-icon-check" size="small" round>등록</el-button>
      <el-button type="primary" icon="el-icon-close" size="small" round @click="dialogVisible=false">닫기</el-button>
    </div>
  </el-dialog>
</template>
<script>
export default {
  name: 'Popup2',
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
