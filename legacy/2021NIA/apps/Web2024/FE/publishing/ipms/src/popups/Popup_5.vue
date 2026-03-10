<template>
  <el-dialog
    title="운용정보 등록"
    :visible.sync="dialogVisible"
    width="800px"
    :before-close="handleClose"
  >
    <div class="popupContentTable">
      <table>
        <tr>
          <th>링크IP블록</th>
          <td colSpan="3" style="text-align:left;">
            <el-select size="small" style="margin-right:10px;" />
            <el-select size="small" />
          </td>
        </tr>
        <tr>
          <th>자국 수용국</th>
          <td class="flex">
            <el-input placeholder="-" size="small">
              <el-button slot="append" icon="el-icon-search" />
            </el-input>
          </td>
          <th>대국 수용국</th>
          <td class="flex">
            <el-input placeholder="-" size="small">
              <el-button slot="append" icon="el-icon-search" />
            </el-input>
          </td>
        </tr>
        <tr>
          <th>자국 장비명</th>
          <td class="flex">
            <el-input placeholder="-" size="small" />
          </td>
          <th>대국 장비명</th>
          <td class="flex">
            <el-input placeholder="-" size="small" />
          </td>
        </tr>
        <tr>
          <th>자국 장비IP</th>
          <td class="flex">
            <el-input placeholder="-" size="small" />
          </td>
          <th>대국 장비IP</th>
          <td class="flex">
            <el-input placeholder="-" size="small" />
          </td>
        </tr>
        <tr>
          <th>자국 IF명</th>
          <td class="flex">
            <el-input placeholder="-" size="small" />
          </td>
          <th>대국 IF명</th>
          <td class="flex">
            <el-input placeholder="-" size="small" />
          </td>
        </tr>
        <tr>
          <th>SAID</th>
          <td class="flex">
            <el-input placeholder="-" size="small" />
          </td>
          <th>전용번호</th>
          <td class="flex">
            <el-input placeholder="-" size="small" />
          </td>
        </tr>
        <tr>
          <th>SAID</th>
          <td colspan="3">
            <el-input placeholder="-" size="small" />
          </td>
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
