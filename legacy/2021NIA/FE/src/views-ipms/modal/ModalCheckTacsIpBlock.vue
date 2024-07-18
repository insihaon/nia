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
        IP블럭 중복체크 조회 결과
        <hr>
      </span>
      <div id="content" class="layer w-100 h-100">
        <div id="tacsView" class="content_result">
          <h3 class="mb-2">IP블럭 <span>중복체크</span> 조회 결과</h3>
          <table class="tbl_data" summary="IP 할당 정보">
            <caption>IP 할당 정보</caption>
            <colgroup>
              <col width="15%" /><col width="35%" /><col width="15%" /><col width="35%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th class="first" scope="row">계위</th>
                <td colspan="3">{{ ipRow.ssvcLineTypeNm }} - {{ ipRow.ssvcGroupNm }} - {{ ipRow.ssvcObjNm }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">서비스</th>
                <td>{{ ipRow.sassignTypeNm }}</td>
                <th scope="row">공인/사설</th>
                <td>{{ ipRow.sipCreateTypeNm }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">대상 IP블럭</th>
                <td>{{ ipRow.pipPrefix }}</td>
                <th scope="row">할당가능여부</th>
                <td>
                  <span v-if="resultListVo.typeFlag === 'Y'" style="color: blue; font-weight: bold;">할당가능</span>
                  <span v-else style="color: red; font-weight: bold;">할당불가능</span>
                </td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">참고내용</th>
                <td colspan="3" style="color: green; font-weight: bold;">할당가능여부는 조회명령어 결과의 조합 기준이며, 할당불가능시에도 운용자의 판단(책임)에 따라 할당이 가능합니다.</td>
              </tr>
            </tbody>
          </table>
          <el-tabs type="card" class="mt-2">
            <el-tab-pane v-for="(item, index) in resultListVo.tacsResponse" :key="index" :label="'접속장비' + (index + 1)">
              <table class="tbl_data" summary="TACS 정보">
                <colgroup>
                  <col width="15%" />
                  <col width="35%" />
                  <col width="15%" />
                  <col width="35%" />
                </colgroup>
                <tbody>
                  <tr class="top">
                    <th class="first" scope="row">장비IP / 장비타입</th>
                    <td class="ellipsis" :title="item.targetIp + ' / ' + item.targetType">{{ item.targetIp }} / {{ item.targetType }}</td>
                    <th scope="row">접속결과</th>
                    <td>{{ item.responseMsg }}</td>
                  </tr>
                  <tr>
                    <th class="first" scope="row">IP블럭 중복 여부</th>
                    <td colspan="3">
                      <span v-if="item.flag === 'Y'" style="color: blue; font-weight: bold;">중복아님</span>
                      <span v-else style="color: red; font-weight: bold;">중복</span>
                    </td>
                  </tr>
                  <tr v-if="item.responseCd !== '0'">
                    <th class="first" scope="row">조회명령어</th>
                    <td colspan="3">-</td>
                  </tr>
                  <tr v-if="item.responseCd !== '0'" class="last">
                    <th class="first" scope="row">조회명령어결과</th>
                    <td colspan="3">
                      <textarea readonly class="w98" rows="8">조회 결과 없음</textarea>
                    </td>
                  </tr>
                  <template v-for="(result, resultIndex) in item.responseList">
                    <tr :key="resultIndex">
                      <th class="first" scope="row">조회명령어{{ resultIndex + 1 }}</th>
                      <td colspan="3">{{ result.cmd }}</td>
                    </tr>
                    <tr :key="resultIndex">
                      <th class="first" scope="row">IP블럭 중복 여부{{ resultIndex + 1 }}</th>
                      <td colspan="3">
                        <span v-if="result.flag === 'Y'" style="color: blue; font-weight: bold;">중복아님</span>
                        <span v-else style="color: red; font-weight: bold;">중복</span>
                      </td>
                    </tr>
                    <tr :key="resultIndex" :class="{ 'last': resultIndex === item.responseList.length - 1 }">
                      <th class="first" scope="row">조회명령어결과 {{ resultIndex + 1 }}</th>
                      <td colspan="3">
                        <textarea v-model="result.result" readonly class="w98" rows="8" />
                      </td>
                    </tr>
                  </template>
                </tbody>
              </table>
            </el-tab-pane>
          </el-tabs>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button
          v-if="ipRow.typeFlag === 'ALLOC' || ipRow.typeFlag === 'NEOSS'"
          size="mini"
          icon="el-icon-thumb"
          style="background: #2b5890"
          type="primary"
          @click.native="handleClickAllockConfirm()"
        >
          할당
        </el-button>
        <!-- <el-button size="mini" @click.native="close()">
          취소
        </el-button> -->
        <el-button size="mini" class="el-icon-close" @click.native="close()">
          {{ $t('exit') }}
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { onMessagePopup } from '@/utils/index'

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
      ipRow: {
        ssvcLineTypeNm: '',
        ssvcGroupNm: '',
        ssvcObjNm: '',
        sassignTypeNm: '',
        sipCreateTypeNm: '',
        pipPrefix: '',
        typeFlag: ''
      },
      resultListVo: {
        typeFlag: 'N',
        tacsResponse: [
        {
          flag: 'N',
          responseCd: '99999',
          value: 99999,
          responseList: null,
          responseMsg: '알수 없는 에러',
          targetIp: '172.29.24.148',
          targetType: 'MOBILE-JUNIPER-QFX10008',
        },
        {
          flag: 'N',
          responseCd: '99999',
          value: 99999,
          responseList: null,
          responseMsg: '알수 없는 에러',
          targetIp: '172.21.120.241',
          targetType: 'MOBILE-JUNIPER_MX960',
        }
        ]
      },
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
      if (model.tacsResponse) {
        this.tacsResponse = model.tacsResponse
      }
      if (model.row) {
        this._merge(this.ipRow, model.row)
      }
    },
    onClose() {
    },
    handleClickAllockConfirm() {
      if (this.resultListVo.typeFlag !== 'Y') {
        this.confirm('중복체크결과가 할당불가능입니다. 그래도 할당진행 하시겠습니까?', '알림', {
          confirmButtonText: '확인',
          cancelButtonText: '취소',
          type: 'warning',
        }).then(async () => {
          const { typeFlag } = this.ipRow
         /*
          할당처리
          if (typeFlag === 'ALLOC') {}
          else if(typeFlag === 'NEOSS') {}
          */
        })
      } else {
        /*
        할당처리
        if (typeFlag === 'ALLOC') {}
        else if(typeFlag === 'NEOSS') {}
        */
      }
    },
  },
}
</script>
<style lang="scss" scoped>
</style>
