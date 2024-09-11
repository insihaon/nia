<template>
  <div>
    <el-dialog
      v-if="animationVisible"
      id="ipms"
      v-el-drag-dialog
      :visible.sync="visible"
      :width="domElement.maxWidth + `px`"
      :fullscreen.sync="fullscreen"
      :modal-append-to-body="false"
      :append-to-body="true"
      :modal="modal"
      :close-on-click-modal="closeOnClickModal"
      :loading="loading"
      class="ipms-dialog"
      :class="{ [name]: true }"
    >
      <span slot="title">
        <i class="el-icon-document mr-2" style="font-size: 17px" />
        {{ isTitle }}
        <hr>
      </span>

      <div id="content" class="layer">
        <div class="content_result mt0">
          <table class="tbl_data entry">
            <colgroup>
              <col width="30%" /><col width="70%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th class="first" scope="row">계위코드</th>
                <td> {{ resultVo.slvlCd }} </td>
              </tr>
              <tr>
                <th class="first" scope="row">계위명</th>
                <td><el-input v-model="resultVo.slvlNm" type="text" size="mini" class="txt w-100" /></td>
              </tr>
              <tr>
                <th class="first" scope="row">계위구분</th>
                <td>
                  <el-select v-model="resultVo.sorgOfficeFlagYn" class="w-100" size="mini">
                    <el-option
                      v-for="item in [
                        { label : '국사' , value: 'N'},
                        { label : '조직' , value: 'Y'}
                      ]"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                </td>
              </tr>
              <tr>
                <th class="first" scope="row">외부연동코드</th>
                <td><el-input type="text" size="mini" class="txt w-100" value="수작업데이터" disabled="disabled" /></td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">비고</th>
                <td><el-input v-model="resultVo.scomment" size="mini" type="text" class="txt w-100" name="scomment" /></td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button v-if="isViewType === 'edit'" size="mini" @click="fnApproveIpAssignApy()">
          {{ $t('저장') }}
        </el-button>
        <el-button v-else size="mini" @click="fnInsertSubmit()">
          {{ $t('등록') }}
        </el-button>
        <el-button size="mini" class="el-icon-close" @click="close()">{{ $t('exit') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { ipmsJsonApis, apiRequestJson } from '@/api/ipms'
import { mapState } from 'vuex'
import _ from 'lodash'

const routeName = 'ModalTbLvlCd'

export default {

  name: routeName,
  components: { },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      resultVo: {
        slvlCd: '',
        slvlNm: '',
        sorgOfficeFlagYn: '',
        scomment: '',
      },
      isViewType: ''
    }
  },
  computed: {
    ...mapState({
      adminYn: state => state.ipms.adminYn,
      suserId: state => state.user.info.Uid,
      suserGradeCd: state => state.ipms.suserGradeCd,
    }),
    isTitle() {
      return this.isViewType === 'edit' ? '가상 국사 조직 정보 수정' : '가상 국사 조직 정보 등록'
    },
  },
  mounted() {

  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 600
    },
    onOpen(model, actionMode) {
      this.isViewType = model.type
      if (this.isViewType === 'edit') {
        this.resultVo = _.cloneDeep(model.row)
      } else {
        this.resultVo = {}
      }
    },
    async fnApproveIpAssignApy() {
      if (this.resultVo.slvlNm === '') {
        this.$message('파라미터 오류, 계위명을 입력 하세요.')
        return
      }

      if (this.resultVo.sorgOfficeFlagYn === '') {
        this.$message('파라미터 오류, 계위구분을 입력 하세요.')
        return
      }

      let res
      try {
        const tbLvlCdVo = {
          slvlCd: this.resultVo.slvlCd,
          smodifyId: this.$store.state.user.info.Uid,
          slvlNm: this.resultVo.slvlNm,
          sorgOfficeFlagYn: this.resultVo.sorgOfficeFlagYn,
          scomment: this.resultVo.scomment,
        }
         res = await apiRequestJson(ipmsJsonApis.updateTbLvlCdVo, tbLvlCdVo)
        if (res.commonMsg === 'SUCCESS') {
          this.$message('계위를 정상적으로 수정하였습니다.')
          this.$emit('reload')
          this.close()
        }
      } catch (error) {
        this.$message.error({ message: `${res.commonMsg}` })
        console.error(error)
      }
    },
    async fnInsertSubmit() {
      if (this.resultVo.slvlNm === '') {
        this.$message('파라미터 오류, 계위명을 입력 하세요.')
        return
      }

      if (this.resultVo.sorgOfficeFlagYn === '') {
        this.$message('파라미터 오류, 계위구분을 입력 하세요.')
        return
      }

      let res
      try {
        const tbLvlCdVo = {
          smodifyId: this.$store.state.user.info.Uid,
          screateId: this.$store.state.user.info.Uid,
          slvlNm: this.resultVo.slvlNm,
          sexLinkUseTypeCd: 'CE0006',
          sorgOfficeFlagYn: this.resultVo.sorgOfficeFlagYn,
          scomment: this.resultVo.scomment,
        }
         res = await apiRequestJson(ipmsJsonApis.insertTbLvlCdVo, tbLvlCdVo)
        if (res.commonMsg === 'SUCCESS') {
          this.$message('계위를 정상적으로 등록하였습니다.')
          this.$emit('reload')
          this.close()
        }
      } catch (error) {
        this.$message.error({ message: `${res.commonMsg}` })
        console.error(error)
      }
    },
    onClose() {
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
