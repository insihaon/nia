<template>
  <div>
    <el-dialog
      v-if="animationVisible"
      id="ipms"
      v-el-drag-dialog
      :visible.sync="visible"
      :width="domElement.maxWidth + `px`"
      :fullscreen.sync="fullscreen"
      title="대체 키워드 추가"
      :modal-append-to-body="true"
      :append-to-body="true"
      :modal="modal"
      :close-on-click-modal="closeOnClickModal"
      :loading="loading"
      class="ipms-dialog"
      :class="{ [name]: true }"
    >

      <div class="popupContentTable">

        <table>
          <tbody>
            <tr>
              <th>기관 구분</th>
              <td>
                <div>
                  <el-select
                    v-model="suserorggb"
                    size="small"
                  >
                    <el-option
                      v-for="item in suserorggbOp"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                </div>
              </td>
              <th>
                <label>기관명</label>
              </th>
              <td>
                <div>
                  <el-input v-model="sorgname"></el-input>
                </div>
              </td>
            </tr>
          </tbody>
        </table>

      </div>
      <div class="popupContentTableBottom">
        <el-button type="primary" size="small" class="el-icon-check" @click="btnSaveSearchAddr()">저장</el-button>
        <el-button type="primary" size="small" class="el-icon-close" @click="close()">{{ $t('exit') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { apiRequestModel, ipmsModelApis, ipmsJsonApis, apiRequestJson } from '@/api/ipms'
import { onMessagePopup } from '@/utils/index'

const routeName = 'ModalInsertKeyword'

export default {

  name: routeName,
  components: { },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      searchAddrVo: {},
      suserorggb: '',
      sorgname: '',
      suserorggbOp: [
        { label: 'CORPORATION', value: 'CORPORATION' },
        { label: 'PUBLIC_INSTITUTION', value: 'PUBLIC_INSTITUTION' },
        { label: 'APT_HOME', value: 'APT_HOME' },
        { label: 'CAMPUS', value: 'CAMPUS' },
        { label: 'HOSPITAL', value: 'HOSPITAL' },
        { label: 'PCROOM', value: 'PCROOM' },
        { label: 'OTHERS', value: 'OTHERS' },
      ],
    }
  },
  computed: {
  },
  mounted() {
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1000
    },
    onOpen(model, actionMode) {
    },
    async btnSaveSearchAddr() { /* 이용기관 저장 */
      const target = { vue: this.$refs.content }
      try {
         this.openLoading(target)
        const tbWhoisKeywordVo = {
          sorgname: this.sorgname,
          suserorggb: this.suserorggb

        }
        const res = await apiRequestJson(ipmsJsonApis.insertWhoisKeyword, tbWhoisKeywordVo)
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, '정상적으로 등록되었습니다.')
          this.$emit('reload')
          this.close()
        } else {
          onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        console.log(error)
      } finally {
      this.closeLoading(target)
    }
    },
    onClose() {
    }
  },
}
</script>
<style lang="scss" scoped>
.ModalSearchZipCode{
  .el-input {
    width: 100%;
  }
}

</style>
