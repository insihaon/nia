<template>
  <el-dialog
    title="IP주소 노드 이전 등록"
    :visible.sync="dialogVisible"
    width="1000px"
    :before-close="handleClose"
  >
    <div class="popupContentTable">
      <table>
        <tr>
          <th>IP주소</th>
          <td class="flex">
            <el-select size="small" />
            <el-input size="small" />
            <el-button icon="el-icon-search" size="small" type="primary" round>조회</el-button>
          </td>
        </tr>
      </table>

      <div class="popupContentTableTitle">조회결과</div>
      <table style="margin: 10px 0">
        <tr>
          <th>선택</th>
          <th>서비스망</th>
          <th>본부</th>
          <th>노드</th>
          <th>공인/사설</th>
          <th>서비스</th>
          <th>IP블록</th>
          <th>배정상태</th>
          <th>라우팅중복개수</th>
          <th>해지</th>
        </tr>
        <tr>
          <td class="textcenter"><el-button icon="el-icon-check" type="primary" circle size="mini" /></td>
          <td class="textcenter">MOBILE</td>
          <td>DATA망</td>
          <td>DATA망(구로)</td>
          <td class="textcenter">공인</td>
          <td>POOL-M2M</td>
          <td>1.100.0.0/17</td>
          <td>서비스배정(미할당)</td>
          <td class="textcenter">0</td>
          <td />
        </tr>
        <tr>
          <td class="textcenter"><el-button icon="el-icon-check" type="primary" circle size="mini" /></td>
          <td class="textcenter">MOBILE</td>
          <td>DATA망</td>
          <td>DATA망(구로)</td>
          <td class="textcenter">공인</td>
          <td>POOL-M2M</td>
          <td>1.100.0.0/17</td>
          <td>서비스배정(미할당)</td>
          <td class="textcenter">0</td>
          <td />
        </tr>
        <tr>
          <td class="textcenter"><el-button icon="el-icon-check" type="primary" circle size="mini" /></td>
          <td class="textcenter">MOBILE</td>
          <td>DATA망</td>
          <td>DATA망(구로)</td>
          <td class="textcenter">공인</td>
          <td>POOL-M2M</td>
          <td>1.100.0.0/17</td>
          <td>서비스배정(미할당)</td>
          <td class="textcenter">0</td>
          <td />
        </tr>
        <tr>
          <td class="textcenter"><el-button icon="el-icon-check" type="primary" circle size="mini" /></td>
          <td class="textcenter">MOBILE</td>
          <td>DATA망</td>
          <td>DATA망(구로)</td>
          <td class="textcenter">공인</td>
          <td>POOL-M2M</td>
          <td>1.100.0.0/17</td>
          <td>서비스배정(미할당)</td>
          <td class="textcenter">0</td>
          <td />
        </tr>
        <tr>
          <td class="textcenter"><el-button icon="el-icon-check" type="primary" circle size="mini" /></td>
          <td class="textcenter">MOBILE</td>
          <td>DATA망</td>
          <td>DATA망(구로)</td>
          <td class="textcenter">공인</td>
          <td>POOL-M2M</td>
          <td>1.100.0.0/17</td>
          <td>서비스배정(미할당)</td>
          <td class="textcenter">0</td>
          <td />
        </tr>
      </table>
      <div class="tableListWrap">
        <div class="tableListPaging" style="justify-content: center;">
          <el-button icon="el-icon-d-arrow-left" type="text" />
          <el-button icon="el-icon-arrow-left" type="text" />
          <div class="pagingNumber">
            <span class="active">1</span>
            <span>2</span>
            <span>3</span>
            <span>4</span>
            <span>5</span>
            <span>6</span>
            <span>7</span>
            <span>8</span>
            <span>9</span>
            <span>10</span>
          </div>
          <el-button icon="el-icon-arrow-right" type="text" />
          <el-button icon="el-icon-d-arrow-right" type="text" />
        </div>
      </div>

      <div class="popupContentTableTitle">변경 전 계위정보</div>
      <table>
        <tr>
          <th>IP블록</th>
          <td>-</td>
          <th>서비스망</th>
          <td>-</td>
        </tr>
        <tr>
          <th>본부</th>
          <td>-</td>
          <th>노드</th>
          <td>-</td>
        </tr>
      </table>

      <div class="popupContentTableTitle">변경 후 계위정보</div>
      <table>
        <tr>
          <th>서비스망</th>
          <td><el-select size="small" /></td>
          <th>본부</th>
          <td><el-select size="small" /></td>
          <th>노드</th>
          <td><el-select size="small" /></td>
        </tr>
      </table>

      <div class="popupContentTableTitle">변경 사유</div>
      <table>
        <tr>
          <th>변경 사유</th>
          <td><el-select size="small" /></td>
        </tr>
        <tr>
          <th>세부 사유(기타 선택시 필수 입력)</th>
          <td><textarea style="min-height:50px;" /></td>
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
