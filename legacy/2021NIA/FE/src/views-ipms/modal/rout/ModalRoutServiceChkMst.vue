<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="서비스 변경 작업"
    :visible.sync="visible"
    :width="domElement.maxWidth + `px`"
    :fullscreen.sync="fullscreen"
    :modal-append-to-body="true"
    :append-to-body="true"
    :close-on-click-modal="closeOnClickModal"
    :loading="loading"
    class="ipms-dialog"
    :class="{ [name]: true }"
  >
    <div class="popupContentTable">
      <div class="popupContentTableTitle">조회결과</div>
      <table>
        <colgroup>
          <col width="5%" />
          <col width="6%" />
          <col width="6%" />
          <col width="4%" />
          <col width="10%" />
          <col width="10%" />
          <col width="4%" />
          <col width="8%" />
          <col width="11%" />
          <col width="11%" />
          <col width="5%" />
          <col width="6%" />
          <col width="11%" />
          <col width="11%" />
        </colgroup>
        <thead>
          <tr>
            <th colspan="5">계위</th>
            <th colspan="3">IPMS</th>
            <th colspan="3">실제 라우팅 장비</th>
            <th rowspan="2">분할/병합<br />건수</th>
            <th rowspan="2">장비수집일자</th>
            <th rowspan="2">서비스</th>
          </tr>
          <tr>
            <th>서비스망</th>
            <th>본부</th>
            <th>노드</th>
            <th>공인<br />/사설</th>
            <th>서비스</th>

            <th>IP블록</th>
            <th>회선</th>
            <th>IP블록상태</th>

            <th>IP블록</th>
            <th>Nexthop</th>
            <th>사용여부</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="tbRoutChkMstVos.length === 0">
            <td class="first" colspan="14">조회된 결과 목록이 존재하지 않습니다.</td>
          </tr>
          <tr v-for="(item, index) in tbRoutChkMstVos" :key="index">
            <td v-if="shouldRenderRowspan(index)" :rowspan="getRowSpan(item, index)" class="first ellipsis merge" :title="item.ssvcLineTypeNm">
              {{ item.ssvcLineTypeNm }}
            </td>
            <td v-if="shouldRenderRowspan(index)" :rowspan="getRowSpan(item, index)" class="ellipsis merge pripPipPrefixPop" :title="item.ssvcGroupNm">
              {{ item.ssvcGroupNm }}
            </td>
            <td v-if="shouldRenderRowspan(index)" :rowspan="getRowSpan(item, index)" class="ellipsis merge pripPipPrefixPop" :title="item.ssvcObjNm">
              {{ item.ssvcObjNm }}
            </td>
            <td v-if="shouldRenderRowspan(index)" :rowspan="getRowSpan(item, index)" class="ellipsis merge pripPipPrefixPop" :title="item.sipCreateTypeNm">
              {{ item.sipCreateTypeNm }}
            </td>
            <td v-if="shouldRenderRowspan(index)" :rowspan="getRowSpan(item, index)" class="ellipsis merge pripPipPrefixPop" :title="item.sassignTypeNm">
              {{ item.sassignTypeNm }}
            </td>
            <td
              v-if="shouldRenderRowspan(index)"
              class="ellipsis merge pripPipPrefixPop dataAssignSeqPop"
              :rowspan="getRowSpan(item, index)"
            >
              {{ item.pipmsIpPrefix }}
            </td>
            <td v-if="shouldRenderRowspan(index)" :rowspan="getRowSpan(item, index)" class="ellipsis merge pripPipPrefixPop" :title="item.nipAllocMstCnt">
              {{ item.nipAllocMstCnt }}
            </td>
            <td v-if="shouldRenderRowspan(index)" :rowspan="getRowSpan(item, index)" class="ellipsis merge pripPipPrefixPop" :title="item.sassignLevelNm">
              {{ item.sassignLevelNm }}
            </td>

            <td class="ellipsis merge pripAssignSeqPop dataPipPrefixPop">
              {{ item.proutingIpPrefix }}
            </td>
            <td class="ellipsis merge pripAssignSeqPop" :title="item.sipNexthop">
              {{ item.sipNexthop }}
            </td>
            <td class="ellipsis merge pripAssignSeqPop" :title="item.sroutingUseYn">
              {{ item.sroutingUseYn }}
            </td>
            <td v-if="shouldRenderRowspan(index)" :rowspan="getRowSpan(item, index)" class="ellipsis merge" :title="item.ntargetCnt">
              {{ item.ntargetCnt }}
            </td>
            <td v-if="shouldRenderRowspan(index)" :rowspan="getRowSpan(item, index)" class="ellipsis merge" :title="item.scollect_dt">
              {{ item.scollect_dt }}
            </td>
            <td v-if="shouldRenderRowspan(index)" :rowspan="getRowSpan(item, index)" class="ellipsis merge">
              <el-select
                v-model="item['selectedService']"
                name="sassignTypeCdPop"
              >
                <el-option value="" label="전체" />
                <el-option v-for="type in sassignTypeCds" :key="type.value" :value="type.value" :label="type.label" />
              </el-select>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="popupContentTableBottom">
      <el-button type="primary" size="small" round @click="fnUpdateServiceMst">서비스변경</el-button>
      <el-button type="primary" size="small" icon="el-icon-close" round @click.native="close()">{{ $t('exit') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { onMessagePopup } from '@/utils'

import { ipmsModelApis, ipmsJsonApis, apiRequestModel, apiRequestJson } from '@/api/ipms'

const routeName = 'ModalRoutServiceChkMst'

export default {
  name: routeName,
  components: { },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      tbRoutChkMstVo: {},
      tbRoutChkMstVos: [],
      sassignTypeCds: [],
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1500
    },
    onOpen(model, actionMode) {
      if (model.tbRoutChkMstVo) {
        this.tbRoutChkMstVo = model.tbRoutChkMstVo
        this.fnViewPopRoutServiceChkMst(model.tbRoutChkMstVo)
      } else {
        this.tbRoutChkMstVo = {}
        this.tbRoutChkMstVos = []
        this.sassignTypeCds = []
      }
    },
    onClose() {
    },
    async fnViewPopRoutServiceChkMst(tbRoutChkMstVo) {
      try {
        const res = await apiRequestModel(ipmsModelApis.viewPopRoutServiceChkMst, tbRoutChkMstVo)
        const tempTbRoutChkMstVos = res.result.resultListVo.tbRoutChkMstVos
        tempTbRoutChkMstVos.forEach(row => {
          row['selectedService'] = ''
        })
        this.tbRoutChkMstVos = tempTbRoutChkMstVos ?? []

        const tempSassignTypeCds = res.result.sassignTypeCds
        this.sassignTypeCds = tempSassignTypeCds.map(v => { return { label: v.name, value: v.code } })
      } catch (error) {
        this.error(error)
      }
    },
    fnUpdateServiceMst() {
      this.confirm('IP블록상태가 [할당/할당예약]일 경우 해지 후 서비스 변경이 진행됩니다. 서비스 변경을  하시겠습니까?', '확인', {
          confirmButtonText: '확인',
          cancelButtonText: '취소',
          type: 'warning',
        }).then(async() => {
          const { ssvcLineTypeCd, sipCreateTypeCd, menuType } = this.tbRoutChkMstVo
          const chkListStr = []
          this.tbRoutChkMstVos.forEach(row => {
            chkListStr.push(`sassignTypeCd_${row.scollectDtOrigin}_${row.nlvlMstSeq}_${row.proutingIpPrefix}_${row.selectedService}`)
          })
          const param = { chkListStr, ssvcLineTypeCd, sipCreateTypeCd, menuType }
          try {
            const res = await apiRequestJson(ipmsJsonApis.updateServiceMst, param)
            if (res.commonMsg === 'SUCCESS') {
              onMessagePopup(this, '서비스 변경이 완료되었습니다.')
              this.tbRoutChkMstVo = {}
              this.tbRoutChkMstVos = []
              this.sassignTypeCds = []
              this.$emit('reload')
              this.close()
            } else {
              onMessagePopup(this, res.commonMsg)
            }
          } catch (error) {
            this.error(error)
          }
        })
    },
    shouldRenderRowspan(index) {
      if (index === 0) return true

      const preRow = this.tbRoutChkMstVos[index - 1]
      const curRow = this.tbRoutChkMstVos[index]
      if (index + 1 === this.tbRoutChkMstVos.length) { // last row
        if (preRow.pipmsIpPrefix !== curRow.pipmsIpPrefix) {
          return true
        }
        return false
      } else {
        const nextRow = this.tbRoutChkMstVos[index + 1]
        if (preRow.pipmsIpPrefix !== curRow.pipmsIpPrefix && curRow.pipmsIpPrefix === nextRow.pipmsIpPrefix) {
          return true
        }
        return false
      }
    },
    getRowSpan(row, index) {
      if (index === 0) {
        return row.ntargetCnt
      } else {
        const preRow = this.tbRoutChkMstVos[index - 1]
        const curRow = this.tbRoutChkMstVos[index]
        if (preRow.pipmsIpPrefix !== curRow.pipmsIpPrefix) {
          return curRow.ntargetCnt
        } else {
          return 1
        }
      }
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
