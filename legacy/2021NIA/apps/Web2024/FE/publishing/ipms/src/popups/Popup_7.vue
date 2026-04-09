<template>
  <el-dialog
    title="IP블록분할"
    :visible.sync="dialogVisible"
    width="800px"
    :before-close="handleClose"
  >
    <div class="popupContentTable">
      <div class="popupContentTableTitle">IP블록 기본 정보</div>
      <table>
        <tr>
          <th>계위</th>
          <td>KORNET - 강남/서부NW운용본부 - 양재국사</td>
          <th>용도</th>
          <td />
        </tr>
        <tr>
          <th>IP버전</th>
          <td>IPv4</td>
          <th>IP주소</th>
          <td>192.0.0.0</td>
        </tr>
        <tr>
          <th>시작IP</th>
          <td>192.0.0.0</td>
          <th>끝IP</th>
          <td>192.0.0.254</td>
        </tr>
        <tr>
          <th>총IP수</th>
          <td colspan="3">254</td>
        </tr>
      </table>

      <div class="popupContentTableTitle">분할 정보</div>
      <table>
        <tr>
          <th>분할단위</th>
          <td class="flex">
            <el-input placeholder="-" size="small">
              <template slot="append">BitMask</template>
            </el-input>
            <el-button type="primary" size="mini" round>분할</el-button>
          </td>
        </tr>
        <tr>
          <th>비고</th>
          <td colspan="3">
            <textarea />
          </td>
        </tr>
      </table>

      <div class="popupContentTableTitle">세부항목</div>
      <table>
        <tr>
          <th><el-checkbox /></th>
          <th>선번장블록</th>
          <th>시작IP</th>
          <th>끝IP</th>
          <th>Bitmask</th>
          <th>블록단위</th>
          <th>분할</th>
        </tr>
        <tr>
          <td class="textcenter"><el-checkbox /></td>
          <td class="textcenter">13.0.0.0</td>
          <td class="textcenter">13.0.0.255</td>
          <td class="textcenter">13.0.0.0</td>
          <td class="textcenter">25</td>
          <td class="textcenter"><el-input size="small" value="27" /></td>
          <td class="textcenter"><el-button icon="el-icon-scissors" size="mini" type="danger" circle /></td>
        </tr>
        <tr>
          <td class="textcenter"><el-checkbox /></td>
          <td class="textcenter">13.0.0.0</td>
          <td class="textcenter">13.0.0.255</td>
          <td class="textcenter">13.0.0.0</td>
          <td class="textcenter">25</td>
          <td class="textcenter"><el-input size="small" value="27" /></td>
          <td class="textcenter"><el-button icon="el-icon-scissors" size="mini" type="danger" circle /></td>
        </tr>
      </table>
    </div>
    <div class="popupContentTableBottom">
      <el-button type="primary" icon="el-icon-plus" size="small" round>병합</el-button>
      <el-button type="primary" icon="el-icon-check" size="small" round>분할확정</el-button>
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
