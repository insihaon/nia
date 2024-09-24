<template>
  <div>
    <el-dialog
      v-if="animationVisible"
      id="ipms"
      v-el-drag-dialog
      :visible.sync="visible"
      :width="domElement.maxWidth + `px`"
      :fullscreen.sync="fullscreen"
      :modal-append-to-body="true"
      :append-to-body="true"
      :modal="modal"
      :close-on-click-modal="closeOnClickModal"
      :loading="loading"
      class="ipms-dialog"
      :class="{ [name]: true }"
    >
      <span slot="title">
        <i class="el-icon-document mr-2" style="font-size: 17px" />
        오더 노드국 관리(SON)
        <hr>
      </span>

      <div id="content" class="layer">
        <div class="content_result mt0">
          <h4>조직계위</h4>
          <table class="tbl_list mt5" summary="조회결과">
            <colgroup>
              <col width="25%" /><col width="25%" /><col width="25%" /><col width="25%" />
            </colgroup>
            <thead>
              <tr>
                <th scope="col">서비스망</th>
                <th scope="col">센터/지연본부</th>
                <th scope="col">주노드</th>
                <th scope="col">노드</th>
              </tr>
            </thead>
            <tbody>
              <tr
                class="top"
              >
                <td>{{ resultListVo[0].ssvcLineTypeNm }}</td>
                <td>{{ resultListVo[0].slvlGroupNm }}</td>
                <td>{{ resultListVo[0].slvlHighNm }}</td>
                <td>{{ resultListVo[0].slvlNm }}</td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="content_result">
          <table class="tbl_data entry" summary="조회결과">
            <colgroup>
              <col width="20%" /><col width="60%" /><col width="20%" />
            </colgroup>
            <tbody>
              <tr
                class="top"
              >
                <th class="first" scope="row">국사선택</th>

                <td>
                  <el-input v-model="ssvcObjNm" readonly size="mini" class="txt w-80">
                    <template #suffix>
                      <el-button
                        slot="trigger"
                        size="small"
                        style="font-size: larger; border: none; float: right"
                        icon="el-icon-search"
                        class="font-weight-bolder"
                        @click="fnViewSearchCenterLvlCd()"
                      />
                    </template>
                  </el-input>
                  <el-button size="mini" @click="fnValidslofficeSubmit()">추가</el-button>
                </td>
              </tr>
            </tbody>
          </table>

        </div>

        <div class="content_result">
          <h4>노드국 목록</h4>

          <table id="orgTable" class="tbl_list mt5" summary="서비스망목록">
            <caption>조직계위정보 목록</caption>
            <colgroup>
              <col width="12%" />
              <col width="12%" />
              <col width="12%" />
            </colgroup>
            <thead>
              <tr>
                <th class="first" scope="col">계위코드</th>
                <th scope="col">국사명</th>
                <th scope="col"></th>
              </tr>
            </thead>

            <tbody>
              <!-- 조회된 결과 목록이 없을 때 -->
              <template v-if="resultVo.totalCount === 0">
                <tr class="subbg last">
                  <td class="first" colspan="3">조회된 결과 목록이 존재하지 않습니다.</td>
                </tr>
              </template>

              <!-- 조회된 결과 목록이 있을 때 -->
              <template v-if="resultVo.totalCount > 0">
                <tr
                  v-for="(item, index) in resultListNodeVo"
                  :key="index"
                  :class="{'subbg': index % 2 !== 0, 'last': index === resultListNodeVo.length - 1}"
                >
                  <td class="ellipsis" :title="item.slofficecode">{{ item.slofficecode }}</td>
                  <td class="ellipsis" :title="item.slofficeNm">{{ item.slofficeNm }}</td>
                  <td>
                    <el-button size="mini" @click="fnDeleteTbLvlSubCd(item)">삭제</el-button>
                  </td>
                </tr>
              </template>
            </tbody>
          </table>
        </div>

      </div>
      <div slot="footer" class="dialog-footer">
        <el-button size="mini" class="el-icon-close" @click="close()">{{ $t('exit') }}</el-button>
      </div>
      <ModalEntireOrgSearch ref="ModalEntireOrgSearch" @selected-value="setSelectedRow" />

    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { ipmsJsonApis, apiRequestJson } from '@/api/ipms'
import _ from 'lodash'
import ModalEntireOrgSearch from '@/views-ipms/modal/search/ModalEntireOrgSearch.vue'

const routeName = 'ModalInsertLvlSonMgmt'

export default {
  name: routeName,
  components: { ModalEntireOrgSearch },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      resultVo: null,
      resultListVo: [],
      resultListNodeVo: [],
      ssvcObjNm: '',
      ssvcObjCd: ''
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
      this.domElement.maxWidth = 600
    },
    onOpen(model, actionMode) {
      this.resultVo = this._cloneDeep(model.row)
      this.resultListVo = this._cloneDeep(model.row.data)
      this.resultListNodeVo = this._cloneDeep(model.row.data)

      this.ssvcObjNm = ''
    },
    fnViewSearchCenterLvlCd() {
      this.$refs.ModalEntireOrgSearch.open({ viewTitle: '노드' })
    },
    setSelectedRow(row) {
      this.ssvcObjNm = row?.slvlNm
      this.ssvcObjCd = row?.slvlCd
    },
    async fnDeleteTbLvlSubCd(item) { /* 노드국 목록 삭제 */
      let res
      try {
        const TbLvlSubCdVo = {
          ssvcLineType: item.ssvcLineTypeCd,
          slvlCd: item.slvlCd,
          ssvcLineTypeCd: item.ssvcLineTypeCd
        }
        res = await apiRequestJson(ipmsJsonApis.deleteTbLvlSubCd, TbLvlSubCdVo)
        if (res.commonMsg === 'SUCCESS') {
          this.$message('정상처리 되었습니다.')
          this.$emit('reload')
          const index = this.resultListNodeVo.indexOf(item)
            if (index > -1) {
              this.resultListNodeVo.splice(index, 1)
          }
        } else {
          return
        }
      } catch (error) {
        console.error(error)
      }
    },
    async fnValidslofficeSubmit() {
      if (this.ssvcObjNm === '' || this.ssvcObjNm === null) {
        this.$message('등록 할 국사 정보가 없습니다.')
        return
      }
      let res
      try {
        const TbLvlSubCdVo = {
          slofficecode: this.ssvcObjCd,
          ssvcLineTypeCd: this.resultListVo[0].ssvcLineTypeCd
        }
        res = await apiRequestJson(ipmsJsonApis.selectSloffice, TbLvlSubCdVo)
        if (res.commonMsg === 'SUCCESS') {
          this.fnInsertSonSubmit(this.resultListVo[0])
        } else {
          this.$message.error('이미 등록된 국사정보입니다.')
        }
      } catch (error) {
        console.error(error)
      }
    },
    async fnInsertSonSubmit(resultListVo) {
      let res
      try {
        const TbLvlSubCdVo = {
          slofficecode: this.ssvcObjCd,
          smodifyId: this.$store.state.user.info.Uid,
          ssvcLineTypeCd: resultListVo.ssvcLineTypeCd,
          slvlCd: resultListVo.slvlCd,
        }
        res = await apiRequestJson(ipmsJsonApis.insertTbLvlSubCd, TbLvlSubCdVo)
        if (res.commonMsg === 'SUCCESS') {
         this.$message('국사 등록이 정상처리 되었습니다.')
         this.resultListNodeVo.push({
          slofficecode: this.ssvcObjCd,
          slofficeNm: this.ssvcObjNm
         })
         this.ssvcObjCd = ''
         this.ssvcObjNm = ''
        } else {
          this.$message.error(`${res.commonMsg}`)
        }
      } catch (error) {
        console.error(error)
      }
    },
    onClose() {
      this.$emit('reload')
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
