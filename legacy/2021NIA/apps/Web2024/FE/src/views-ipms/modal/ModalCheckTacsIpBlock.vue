<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="IP블럭 중복체크 조회 결과"
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
    <div class="popupContentTable">
      <div class="popupContentTableTitle">IP블럭 <span style="color: red;">중복체크</span> 조회 결과</div>
      <fragment v-loading="viewLoading">
        <table>
          <colgroup>
            <col width="15%" /><col width="35%" /><col width="15%" /><col width="35%" />
          </colgroup>
          <tbody>
            <tr>
              <th>계위</th>
              <td colspan="3">{{ ipRow.ssvcLineTypeNm }} - {{ ipRow.ssvcGroupNm }} - {{ ipRow.ssvcObjNm }}</td>
            </tr>
            <tr>
              <th>서비스</th>
              <td>{{ ipRow.sassignTypeNm }}</td>
              <th scope="row">공인/사설</th>
              <td>{{ ipRow.sipCreateTypeNm }}</td>
            </tr>
            <tr>
              <th>대상 IP블럭</th>
              <td>{{ ipRow.pipPrefix }}</td>
              <th scope="row">할당가능여부</th>
              <td>
                <span v-if="ipRow.typeFlag === 'Y'" style="color: blue; font-weight: bold;">할당가능</span>
                <span v-else style="color: red; font-weight: bold;">할당불가능</span>
              </td>
            </tr>
            <tr class="last">
              <th>참고내용</th>
              <td colspan="3" style="color: green; font-weight: bold;">할당가능여부는 조회명령어 결과의 조합 기준이며, 할당불가능시에도 운용자의 판단(책임)에 따라 할당이 가능합니다.</td>
            </tr>
          </tbody>
        </table>
        <el-tabs>
          <template v-if="tacsResultList.length > 0">
            <el-tab-pane v-for="(item, index) in tacsResultList" :key="index" :label="'접속장비' + (index + 1)">
              <table class="tbl_data" summary="TACS 정보">
                <colgroup>
                  <col width="15%" />
                  <col width="35%" />
                  <col width="15%" />
                  <col width="35%" />
                </colgroup>
                <tbody>
                  <tr class="top">
                    <th>장비IP / 장비타입</th>
                    <td>{{ item.targetIp }} / {{ item.targetType }}</td>
                    <th scope="row">접속결과</th>
                    <td>{{ item.responseMsg }}</td>
                  </tr>
                  <tr>
                    <th>IP블럭 중복 여부</th>
                    <td colspan="3">
                      <span v-if="item.flag === 'Y'" style="color: blue; font-weight: bold;">중복아님</span>
                      <span v-else style="color: red; font-weight: bold;">중복</span>
                    </td>
                  </tr>
                  <tr v-if="item.responseCd !== '0'">
                    <th>조회명령어</th>
                    <td colspan="3">-</td>
                  </tr>
                  <tr v-if="item.responseCd !== '0'" class="last">
                    <th>조회명령어결과</th>
                    <td colspan="3">
                      <textarea readonly class="w98" rows="8">조회 결과 없음</textarea>
                    </td>
                  </tr>
                  <template v-for="(result, resultIndex) in item.responseList">
                    <tr :key="resultIndex">
                      <th>조회명령어{{ resultIndex + 1 }}</th>
                      <td colspan="3">{{ result.cmd }}</td>
                    </tr>
                    <tr :key="resultIndex">
                      <th>IP블럭 중복 여부{{ resultIndex + 1 }}</th>
                      <td colspan="3">
                        <span v-if="result.flag === 'Y'" style="color: blue; font-weight: bold;">중복아님</span>
                        <span v-else style="color: red; font-weight: bold;">중복</span>
                      </td>
                    </tr>
                    <tr :key="resultIndex" :class="{ 'last': resultIndex === item.responseList.length - 1 }">
                      <th>조회명령어결과 {{ resultIndex + 1 }}</th>
                      <td colspan="3">
                        <textarea v-model="result.result" readonly class="w98" rows="5" />
                      </td>
                    </tr>
                  </template>
                </tbody>
              </table>
            </el-tab-pane>
          </template>
        </el-tabs>
      </fragment>
    </div>
    <div class="popupContentTableBottom">
      <el-button
        v-if="['ALLOC', 'NEOSS', 'AUTO_ALLOC'].includes(typeFlag)"
        size="small"
        icon="el-icon-thumb"
        style="background: #2b5890"
        type="primary"
        round
        @click.native="handleClickAllockConfirm()"
      >
        할당
      </el-button>
      <!-- <el-button size="mini" @click.native="close()">
        취소
      </el-button> -->
      <el-button icon="el-icon-close" size="small" type="primary" round @click.native="close()">{{ $t('exit') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { onMessagePopup } from '@/utils/index'
import { ipmsModelApis, apiRequestModel } from '@/api/ipms'

const routeName = 'ModalCheckTacsIpBlock'

export default {
  name: routeName,
  components: { },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      viewLoading: false,
      typeFlag: '',
      ipRow: {
        ssvcLineTypeNm: '',
        ssvcGroupNm: '',
        ssvcObjNm: '',
        sassignTypeNm: '',
        sipCreateTypeNm: '',
        pipPrefix: '',
        typeFlag: ''
      },
      tacsTypeFlag: 'N',
      tacsResultList: [],
      currentTabIndex: 0
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1000
    },
    onOpen(model, actionMode) {
      if (model.typeFlag) {
        this.typeFlag = model.typeFlag
      }
      if (model.row) {
        this.fnViewCheckTacsIpBlock({ nipAssignMstSeq: model.row.nipAssignMstSeq, typeFlag: model.typeFlag })
        this._merge(this.ipRow, model.row)
      }
    },
    onClose() {
      this.tacsTypeFlag = 'N'
    },
    async fnViewCheckTacsIpBlock(param) {
      // viewCheckTacsIpBlock
      try {
        this.viewLoading = true
        const res = await apiRequestModel(ipmsModelApis.viewCheckTacsIpBlock, param)
        if (res.result.data) {
          const result = res.result.data
          // if (result.commonMsg === 'SUCCESS') {
            this.tacsResultList = result
            this.tacsTypeFlag = result.typeFlag
          // } else {
          //   onMessagePopup(this, result.commonMsg)
          //   this.close()
          // }
        }
      } catch (error) {
        this.error(error)
      } finally {
        this.viewLoading = false
      }
    },
    handleClickAllockConfirm() {
      if (this.typeFlag !== 'Y') {
        this.confirm('중복체크결과가 할당불가능입니다. 그래도 할당진행 하시겠습니까?', '알림', {
          confirmButtonText: '확인',
          cancelButtonText: '취소',
          type: 'warning',
        }).then(() => {
          this.fnAlloc()
        })
      } else {
        this.fnAlloc()
      }
    },
    fnAlloc() {
      // 할당처리
      if (this.typeFlag === 'ALLOC') {
        const result = this.$parent.$parent.fnInsertConfirmBtnClick()
        if (result) {
          this.tacsResultList = []
          this.close()
        }
      } else if (this.typeFlag === 'NEOSS') {
        // fnAllocBtnClick()
      }
    }
  },
}
</script>
<style lang="scss" scoped>
::v-deep .el-tabs__nav {
  .el-tabs__item.is-active {
    color: red;
  font-weight: bold;
  }
  .el-tabs__item {
    &:hover {
      color: red;
    }
  }
}
</style>
