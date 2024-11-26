<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="Whois 신청서 수정"
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
    <div class="popupContentTable">
      <div class="popupContentTableTitle">처리상태</div>
      <table>
        <colgroup>
          <col width="24%" />
          <col width="76%" />
        </colgroup>
        <tbody>
          <tr>
            <th>처리상태</th>
            <td>{{ resultVo.swhoisTranStatusNm }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <!-- popupContentTable END -->

    <div class="popupContentTable">
      <div class="popupContentTableTitle">기본정보</div>
      <table>
        <colgroup>
          <col width="4%" />
          <col width="18%" />
          <col width="78%" />
        </colgroup>
        <tbody>
          <tr>
            <th>h0</th>
            <th>신청서이름</th>
            <td>{{ resultVo.sapplicationname }}</td>
          </tr>
          <tr>
            <th>h1</th>
            <th>구분</th>
            <td>{{ resultVo.swhoisRequestTypeNm }}</td>
          </tr>
          <tr class="last">
            <th>h2</th>
            <th>KRNIC 회원ID</th>
            <td>{{ resultVo.skrnicmemberId }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <!-- popupContentTable END -->
    <div class="popupContentTableBottom">
      <el-button type="primary" size="small" round @click="fnSetAddr('toKT')">KT 정보대체</el-button>
      <el-button type="primary" size="small" round @click="fnSetAddr('reset')">초기화</el-button>
    </div>

    <div v-if="resultVo.dbMatchYn === 'N'" id="allocDiv" class="popupContentTable">
      <div class="popupContentTableTitle">할당 테이블 고객정보</div>
      <table>
        <colgroup>
          <col width="20%" />
          <col width="80%" />
        </colgroup>
        <tbody>
          <tr>
            <th>고객명</th>
            <td>
              <span id="custName">{{ customerName }}</span>
            </td>
          </tr>
          <tr>
            <th>주소</th>
            <td>
              <span id="custAddr">{{ customerAddr }}</span>
            </td>
          </tr>
          <tr>
            <th>우편 번호</th>
            <td>
              <span id="custZipcode">{{ customerZipcode }}</span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="popupContentTable">
      <div class="popupContentTableTitle">1. IP 주소 사용기관 (필수)</div>
      <table>
        <colgroup>
          <col width="4%" />
          <col width="18%" />
          <col width="78%" />
        </colgroup>
        <tbody>
          <tr>
            <th>1a</th>
            <th>한글 기관명(한 단어)</th>
            <td>
              <el-input v-model="userVo.sorgname" size="small" class="txt w50" maxlength="200" />
            </td>
          </tr>
          <tr>
            <th>1b</th>
            <th>한글 주소</th>
            <td>
              <el-input v-model="userVo.saddr" size="small" class="w-80" disabled maxlength="200" />
              <el-button type="primary" size="small" round @click="searchAddr()">주소검색</el-button>
            </td>
          </tr>
          <tr>
            <th>1c</th>
            <th>한글 상세 주소</th>
            <td>
              <el-input v-model="userVo.saddrDetail" size="small" class="txt w98" disabled maxlength="200" />
            </td>
          </tr>
          <tr>
            <th>1d</th>
            <th>우편 번호</th>
            <td>
              <el-input v-model="userVo.szipcode" size="small" class="txt w10" disabled />
            </td>
          </tr>
          <tr>
            <th>1e</th>
            <th>영문 기관명</th>
            <td>
              <el-input v-model="userVo.seorgname" size="small" class="txt w50" maxlength="200" />
            </td>
          </tr>
          <tr>
            <th>1f</th>
            <th>영문 주소</th>
            <td>
              <el-input v-model="userVo.seaddr" size="small" class="txt w98" disabled maxlength="200" />
            </td>
          </tr>
          <tr>
            <th>1g</th>
            <th>영문 상세 주소</th>
            <td>
              <el-input v-model="userVo.seaddrDetail" size="small" class="txt w98" disabled maxlength="200" />
            </td>
          </tr>
          <tr>
            <th>1h</th>
            <th>전화번호</th>
            <td>
              <span class="ml3">+ 82</span>
              <span class="ml5 mr5">-</span>
              <el-input v-model="userVo.dcreateDt" class="w-70" size="small" maxlength="50" placeholder="2-222-2222" />
            </td>
          </tr>
          <tr>
            <th>1i</th>
            <th>전자우편주소</th>
            <td>
              <el-input v-model="userVo.sadmEmail" class="txt w98" size="small" maxlength="100" />
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="popupContentTable">
      <div class="popupContentTableTitle">2. 네트워크 정보 (필수)</div>
      <table>
        <colgroup>
          <col width="4%" />
          <col width="18%" />
          <col width="78%" />
        </colgroup>
        <tbody>
          <tr>
            <th>2a</th>
            <th>IP 주소(ISP만 기재)</th>
            <td>
              <span>Start Address</span>
              <el-input v-model="resultVo.sfirstAddr" size="small" class="w-30" disabled />
              <span>-</span>
              <span>End Address</span>
              <el-input v-model="resultVo.slastAddr" size="small" class="w-30" disabled />
            </td>
          </tr>
          <tr>
            <th>2b</th>
            <th>네트워크 이름(한 단어)</th>
            <td>
              <el-input v-model="resultVo.snetNm" size="small" class="txt w50" disabled />
            </td>
          </tr>

          <tr>
            <th>2c</th>
            <th>네트워크분류</th>
            <td class="view">{{ resultVo.snettype }}</td>
          </tr>
          <tr>
            <th>2d</th>
            <th>인터넷서비스분류</th>
            <td class="view">{{ resultVo.sservicegb }}</td>
          </tr>
          <tr>
            <th>2e</th>
            <th>이용기관분류</th>
            <td>
              <el-select v-model="resultVo.suserorggb" size="small" class="w15">
                <el-option label="일반기업" value="CORPORATION"></el-option>
                <el-option label="공공기관" value="PUBLIC_INSTITUTION"></el-option>
                <el-option label="아파트/가정" value="APT_HOME"></el-option>
                <el-option label="대학" value="CAMPUS"></el-option>
                <el-option label="병원" value="HOSPITAL"></el-option>
                <el-option label="PC방" value="PCROOM"></el-option>
                <el-option label="기타" value="OTHERS"></el-option>
                <el-option label="INFRA" value="INFRA"></el-option>
              </el-select>
            </td>
          </tr>
          <tr>
            <th>2f</th>
            <th>서비스지역</th>
            <td>
              <el-select v-model="resultVo.scity" size="small">
                <el-option
                  v-for="item in scity"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
                <el-option label="서울" value="서울"></el-option>
                <el-option label="경기" value="경기"></el-option>
                <el-option label="인천" value="인천"></el-option>
                <el-option label="강원" value="강원"></el-option>
                <el-option label="충북" value="충북"></el-option>
                <el-option label="대전" value="대전"></el-option>
                <el-option label="충남" value="충남"></el-option>
                <el-option label="경북" value="경북"></el-option>
                <el-option label="대구" value="대구"></el-option>
                <el-option label="경남" value="경남"></el-option>
                <el-option label="울산" value="울산"></el-option>
                <el-option label="부산" value="부산"></el-option>
                <el-option label="전북" value="전북"></el-option>
                <el-option label="전남" value="전남"></el-option>
                <el-option label="광주" value="광주"></el-option>
                <el-option label="제주" value="제주"></el-option>
              </el-select>
            </td>
          </tr>
          <tr>
            <th>2g</th>
            <th>IP분류</th>
            <td>{{ resultVo.siptype }}</td>
          </tr>
          <tr class="last">
            <th>2h</th>
            <th>연결 날짜</th>
            <td>
              <el-input v-model="resultVo.dcreateDt" size="small" class="w-60" disabled />
              <span style="font-size: 12px; color: #b5b5b5">(예: 2014.01.01)</span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="popupContentTable">
      <div class="popupContentTableTitle">3. 추가사항(Comment)</div>
      <table class="tbl_data entry" summary="추가사항">
        <colgroup>
          <col width="22%" />
          <col width="78%" />
        </colgroup>
        <tbody>
          <tr>
            <th>추가사항 내용</th>
            <td>
              <el-input v-model="resultVo.ssvccommnet" size="small" type="textarea" class="w98" rows="3" maxlength="1500" />
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <!-- popupContentTable END -->

    <div class="popupContentTable">
      <div class="popupContentTableTitle">4. KRNIC 회신</div>
      <table>
        <colgroup>
          <col width="22%" />
          <col width="78%" />
        </colgroup>
        <tbody>
          <tr>
            <th>결과 메세지</th>
            <td>
              <el-input v-model="resultVo.swhoisresultMsg" size="small" type="textarea" class="view w98" rows="5" readonly />
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="popupContentTableBottom">
      <el-button v-if="resultVo.dbMatchYn === 'N'" type="primary" size="small" round @click.native="fnSaveRegWhoisSubmit()"> DB 현행화 전송 </el-button>
      <template v-if="resultVo.swhoisresultCd === '03'">
        <el-button v-if="resultVo.delyn === 'N'" type="primary" size="small" round @click.native="fnSaveRegWhoisSubmit()"> 변경 </el-button>
        <el-button v-if="resultVo.delyn === 'Y'" type="primary" size="small" round @click.native="fnDelRegWhoisSubmit()"> 삭제 </el-button>
      </template>
      <template v-else>
        <el-button v-if="resultVo.delyn === 'N'" type="primary" size="small" round @click.native="fnSaveRegWhoisSubmit()"> 변경 </el-button>
      </template>
      <el-button type="primary" size="small" icon="el-icon-close" round @click.native="close()"> 닫기 </el-button>
    </div>
    <!-- <ModalSearchKisa ref="ModalSearchKisa"></ModalSearchKisa> -->
  </el-dialog>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { onMessagePopup } from '@/utils/index'
import { apiRequestJson, ipmsJsonApis, apiRequestModel, ipmsModelApis } from '@/api/ipms'

const routeName = 'ModalRegWhois'

export default {
  name: routeName,
  components: {},
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      resultVo: {
        sapplicationname: '',
        swhoisTranStatusNm: '',
        swhoisRequestTypeNm: '',
        skrnicmemberId: '',
        sfirstAddr: '',
        slastAddr: '',
        snetNm: '',
        snettype: '',
        sservicegb: '',
        suserorggb: '',
        scity: '',
        siptype: '',
        ssvccommnet: '',
        swhoisresultMsg: '',
        srequestCd: '',
        swhoisresultCd: '',
        type: '',
        ssaid: '',
        nwhoisSeq: '',
        dcreateDt: '',
      },
      userVo: {
        ssaid: '',
        sorgname: '',
        saddr: '',
        saddrDetail: '',
        szipcode: '',
        seorgname: '',
        seaddr: '',
        seaddrDetail: '',
        sadmDpphone: '',
        sadmEmail: '',
      },
      ktInfoVo: {},
      scity: [],
      allocUserInfo: {
        customerName: '',
        customerAddr: '',
        customerZipcode: '',
      },
      formattedPhone: '',
      formattedDate: '',
      TbWhoisComplexVo_: null,
      resubmit: false,
    }
  },
  computed: {},
  mounted() {},
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1000
    },
    onOpen(model, actionMode) {
      setTimeout(() => {
        if (model.row) {
          this.fnViewDetailWhois(model.row)
        }
      }, 10)
    },
    async fnViewDetailWhois(row) {
      const target = { vue: this.$refs.content }
      try {
        this.openLoading(target)
        const { nwhoisSeq } = row
        const tbUserAuthVo = {
          nwhoisSeq: nwhoisSeq,
        }
        const res = await apiRequestModel(ipmsModelApis.viewRegWhoisNew, tbUserAuthVo)
        this.resultVo = res?.resultVo
        this.userVo = res?.userVo
        this.ktInfoVo = res?.ktInfoVo
        this.scity = res?.scity.map(item => {
          return { value: item, label: item }
        })
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    fnSetAddr(type) {
      if (this.resultVo === '' || this.resultVo === null) {
        return
      }
      if (type === 'reset') {
        const { sorgname, saddr, saddrDetail, szipcode, seorgname, seaddr, seaddrDetail, sadmDpphone, sadmEmail } = this.userVo
        this.userVo.sorgname = sorgname // 한글 기관명
        this.userVo.saddr = saddr // 한글 주소
        this.userVo.saddrDetail = saddrDetail // 한글 상세주소
        this.userVo.szipcode = szipcode // 우편번호 앞
        this.userVo.seorgname = seorgname // 영문 기관명
        this.userVo.seaddr = seaddr // 영문 주소
        this.userVo.seaddrDetail = seaddrDetail // 영문 상세주소
        this.userVo.dcreateDt = sadmDpphone // 전화번호
        this.userVo.sadmEmail = sadmEmail // 이메일
      } else if (type === 'toKT') {
        const { sorgname, sadmAddr, sadmAddrDetail, sadmZipcode, sadmEorgname, sadmEaddr, sadmEaddrDetail, sadmDpphone, sadmEmail } = this.ktInfoVo
        this.userVo.sorgname = sorgname // 한글 기관명
        this.userVo.saddr = sadmAddr // 한글 주소
        this.userVo.saddrDetail = sadmAddrDetail // 한글 상세주소
        this.userVo.szipcode = sadmZipcode // 우편번호 앞
        this.userVo.seorgname = sadmEorgname // 영문 기관명
        this.userVo.seaddr = sadmEaddr // 영문 주소
        this.userVo.seaddrDetail = sadmEaddrDetail // 영문 상세주소
        this.userVo.dcreateDt = sadmDpphone // 전화번호
        this.userVo.sadmEmail = sadmEmail // 이메일
      }
    },
  },
  async fnSaveRegWhoisSubmit(objVo) {
    const tbWhoisComplexVo = {}
    if (objVo) {
      Object.assign(tbWhoisComplexVo, objVo)
    } else {
      /* 이용기관 정보 */

      const tbWhoisUserVo = {
        ssaid: this.userVo.ssaid,
        sorgname: this.userVo.sorgname,
        saddr: this.userVo.saddr,
        saddrDetail: this.userVo.saddrDetail,
        szipcode: this.userVo.szipcode,
        seorgname: this.userVo.seorgname,
        seaddr: this.userVo.seaddr,
        seaddrDetail: this.userVo.seaddrDetail,
        sadmDpphone: this.userVo.sadmDpphone,
        sadmEmail: this.userVo.sadmEmail,
        stransKind: 'ADMIN',
      }
      /* 네트워크 정보 */
      const tbWhoisVo = {
        swhoisresultCd: '01', // 전송대기 코드
        ssaid: this.resultVo.ssaid,
        sfirstAddr: this.resultVo.sfirstAddr,
        slastAddr: this.resultVo.slastAddr,
        snetNm: this.resultVo.snetNm,
        suserorggb: this.resultVo.suserorggb,
        scity: this.resultVo.scity,
        nwhoisSeq: this.resultVo.nwhoisSeq,
        ssvccommnet: this.resultVo.ssvccommnet,
        stransKind: 'ADMIN',
      }
      Object.assign(tbWhoisComplexVo, { tbWhoisUserVo: tbWhoisUserVo })
      Object.assign(tbWhoisComplexVo, { tbWhoisVo: tbWhoisVo })
    }

    if (tbWhoisComplexVo.tbWhoisUserVo.sorgname === '' || tbWhoisComplexVo.tbWhoisUserVo.sorgname === null) {
      onMessagePopup(this, '한글기관명을 입력하세요')
      return
    }

    if (tbWhoisComplexVo.tbWhoisUserVo.saddr === '' || tbWhoisComplexVo.tbWhoisUserVo.saddr === null) {
      onMessagePopup(this, '한글주소를 입력하세요')
      return
    }

    if (tbWhoisComplexVo.tbWhoisUserVo.szipcode === '' || tbWhoisComplexVo.tbWhoisUserVo.szipcode === null) {
      onMessagePopup(this, '우편주소를 입력하세요')
      return
    }

    if (tbWhoisComplexVo.tbWhoisUserVo.seorgname === '' || tbWhoisComplexVo.tbWhoisUserVo.seorgname === null) {
      onMessagePopup(this, '영문기관명을 입력하세요')
      return
    }

    if (tbWhoisComplexVo.tbWhoisUserVo.seaddr === '' || tbWhoisComplexVo.tbWhoisUserVo.seaddr === null) {
      onMessagePopup(this, '영문주소를 입력하세요')
      return
    }

    const regEx = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/ // 영문명 한글체크
    if (regEx.test(tbWhoisComplexVo.tbWhoisUserVo.seorgname)) {
      onMessagePopup(this, '영문기관명은 한글입력이 불가합니다.')
      return
    }

    const requestType = objVo ? objVo.tbWhoisVo.srequestCd : this.resultVo.srequestCd
    const resultCode = this.resultVo.swhoisresultCd
    const type = objVo ? objVo.tbWhoisVo.type : this.resultVo.type

    switch (requestType) {
      case '01': // 신규신청서
        if (resultCode === '03') {
          // 반송
          if (type) {
            this.resubmit = true
          } else {
            tbWhoisComplexVo.tbWhoisVo.type = 'DEFAULT'
            tbWhoisComplexVo.tbWhoisVo.srequestCd = '01'
          }
        } else if (resultCode === '04') {
          // 등록완료
          tbWhoisComplexVo.tbWhoisVo.type = 'DEFAULT'
          tbWhoisComplexVo.tbWhoisVo.srequestCd = '04' // 변경신청서 코드
        } else if (['01', '02', '05'].includes(resultCode)) {
          // 전송실패
          tbWhoisComplexVo.tbWhoisVo.type = 'DEFAULT'
          tbWhoisComplexVo.tbWhoisVo.srequestCd = '01' // 신규신청서 코드
        }
        break

      case '02': // 추가신청서
        if (resultCode === '03') {
          // 반송
          if (type) {
            this.resubmit = true
          } else {
            tbWhoisComplexVo.tbWhoisVo.type = 'DEFAULT'
            tbWhoisComplexVo.tbWhoisVo.srequestCd = '02'
          }
        } else if (resultCode === '04') {
          // 등록완료
          tbWhoisComplexVo.tbWhoisVo.type = 'DEFAULT'
          tbWhoisComplexVo.tbWhoisVo.srequestCd = '04' // 변경신청서 코드
        } else if (['01', '02', '05'].includes(resultCode)) {
          // 전송실패
          tbWhoisComplexVo.tbWhoisVo.type = 'DEFAULT'
          tbWhoisComplexVo.tbWhoisVo.srequestCd = '02' // 추가신청서 코드
        }
        break

      case '04': // 변경신청서
        if (type) {
          this.resubmit = true
        } else {
          tbWhoisComplexVo.tbWhoisVo.type = 'DEFAULT'
          tbWhoisComplexVo.tbWhoisVo.srequestCd = '04' // 변경신청서 코드
        }
        break

      case '03': // 삭제신청서
        if (resultCode === '03') {
          // 반송
          tbWhoisComplexVo.tbWhoisVo.type = 'DEFAULT'
          tbWhoisComplexVo.tbWhoisVo.srequestCd = '03' // 삭제신청서 코드
        } else if (['01', '02', '05'].includes(resultCode)) {
          // 전송실패
          tbWhoisComplexVo.tbWhoisVo.type = 'DEFAULT'
          tbWhoisComplexVo.tbWhoisVo.srequestCd = '03' // 삭제신청서 코드
        }
        break

      default:
        break
    }

    if (this.resultVo.snettype === 'CUSTOMER' && tbWhoisComplexVo.tbWhoisVo.suserorggb === 'INFRA') {
      onMessagePopup(this, '네트워크 유형이 CUSTOMER인 경우엔 이용기관분류를 INFRA로 지정하실 수 없습니다.')
      return
    }

    if (this.resubmit) {
      /* 재전송 */
      this.fnWhoisReSumbit(type, tbWhoisComplexVo)
    }

    // 신규 신청서(`requestType === "01"`)이 아니거나, `resultCode`가 `"04"`(등록 완료)가 아닌 경우
    const isNewRequest = requestType === '01' && resultCode !== '04'
    const isInfraNetwork = this.resultVo.snettype === 'INFRA'
    const canSubmit = resultCode === '01' || resultCode === '02' || resultCode === '05'

    if (!isNewRequest && isInfraNetwork && !canSubmit) {
      onMessagePopup(this, '네트워크 유형이 INFRA인 경우엔 기관정보를 수정하실 수 없습니다.')
      return
    }
    const target = { vue: this.$refs.content }
    try {
      this.openLoading(target)
      const res = await apiRequestJson(ipmsJsonApis.updateWhoisComplexNew, tbWhoisComplexVo)
      if (res.tbWhoisVo.swhoisresultCd === '04') {
        onMessagePopup(this, '성공적으로 반영하였습니다.')
      } else if (res.tbWhoisVo.swhoisresultCd === '03') {
        onMessagePopup(this, '반송되었습니다.')
      } else {
        onMessagePopup(this, '반영에 실패하였습니다.')
      }
    } catch (error) {
      console.error(error)
    } finally {
      this.closeLoading(target)
    }
  },
  async fnWhoisReSumbit(type, objVo) {
    if (!type) return
    const tbWhoisComplexVo = objVo

    if (type === 'ADD_NETNAME_ERROR') {
      tbWhoisComplexVo.tbWhoisVo.type = type
      tbWhoisComplexVo.tbWhoisVo.srequestCd = '01' // 신규신청서 코드
      tbWhoisComplexVo.tbWhoisVo.swhoisresultCd = '01'
      tbWhoisComplexVo.tbWhoisVo.swhoisresultMsg = ' '

      this.TbWhoisComplexVo_ = tbWhoisComplexVo

      const tbWhoisVo = {
        type: 'ADD_COUNT',
        ssaid: this.userVo.ssaid,
        nwhoisSeq: null,
      }

      this.fnSaveRegWhoisComplexNewSubmit(tbWhoisVo) /* whois 데이터 변경 전송 */
    } else if (type === 'ADD_IPBLOCK_ERROR') {
      const tbWhois = {
        type: type,
        nwhoisSeq: tbWhoisComplexVo.tbWhoisVo.nwhoisSeq,
      }
      this.fnViewSearchKisa(tbWhois)
    } else if (type === 'NEW_IPBLOCK_ERROR') {
      const tbWhois = {
        type: type,
        nwhoisSeq: tbWhoisComplexVo.tbWhoisVo.nwhoisSeq,
      }
      this.fnViewSearchKisa(tbWhois)
    } else if (type === 'MOD_ERROR') {
      this.confirm('신규신청서로 재전송됩니다. 재전송하시겠습니까?', '신규신청서 재전송 알림', {
        cancelButtonText: '닫기',
        confirmButtonText: '재전송',
      })
        .then(async () => {
          objVo.tbWhoisVo.srequestCd = '01'
          objVo.tbWhoisVo.type = type
          this.fnSaveRegWhoisSubmit(objVo)
        })
        .catch((action) => {})
    } else if (type === 'NEW_NETNAME_ERROR') {
      this.confirm('추가신청서로 재전송됩니다. 재전송하시겠습니까?', '추가신청서 재전송 알림', {
        cancelButtonText: '닫기',
        confirmButtonText: '재전송',
      })
        .then(async () => {
          objVo.tbWhoisVo.srequestCd = '02'
          objVo.tbWhoisVo.type = type
          this.fnSaveRegWhoisSubmit(objVo)
        })
        .catch((action) => {})
    }
  },
  fnViewSearchKisa(param) {
    /* KISA IP 조회  */
    // this.$refs.ModalSearchKisa.open({ row: param })
  },
  async fnSaveRegWhoisComplexNewSubmit(param) {
    /* WHOIS 데이터 변경 전송 */
    const target = { vue: this.$refs.content }
    const tbWhoisVo = {}
    Object.assign(tbWhoisVo, param)
    try {
      this.openLoading(target)
      const res = await apiRequestJson(ipmsJsonApis.selectWhoisComplexNew, tbWhoisVo)
      if (res === null) {
        onMessagePopup(this, 'Whois 데이터 변경 전송을 실패하였습니다.')
        return
      }

      let count
      if (tbWhoisVo.type === 'ADD_COUNT') {
        count = res
        const msg = `해당 신청서는 '신규신청서'로 다시 재전송 됩니다.
          해당 신청서와 같은 SAID 면서 같은 이유로 반송 된 신청서들은
          해당 신청서를 제외한 나머지 신청서는 추가신청서로 재전송됩니다.`
        onMessagePopup(this, msg)
        const whoisObj = {
          type: tbWhoisVo.type,
          tbWhoisComplexVo: this.tbWhoisComplexVo_,
        }
        this.fnRetrySubmitWhois(whoisObj)
      }
    } catch (error) {
      console.error(error)
    } finally {
      this.closeLoading(target)
    }
  },
  async fnRetrySubmitWhois(param) {
    /* WHOIS 재전송 */
    this.confirm('재전송 하시겠습니까?', '재전송 알림', {
      cancelButtonText: '닫기',
      confirmButtonText: '재전송',
    })
      .then(async () => {
        const whoisObj = {}
        Object.assign(whoisObj, param)
        try {
          const res = await apiRequestJson(ipmsJsonApis.updateWhoisComplexNew, whoisObj)
          if (res.tbWhoisVo.swhoisresultCd === '04') {
            onMessagePopup(this, 'Whois 데이터가 재전송되었습니다.')
          } else {
            onMessagePopup(this, 'Whois 재전송이 실패되었습니다.')
          }
        } catch (error) {
          console.error(error)
        }
      })
      .catch((action) => {})
  },
  fnDelRegWhoisSubmit() {
    this.$confirm('삭제 하시겠습니까?', 'WHO IS 정보 삭제', {
      confirmButtonText: '확인',
      cancelButtonText: '취소',
    }).then(async () => {
      try {
        const tbWhoisVo = { delList: [] }
        this.selectedChecks.forEach(nWhoisSeq => {
          tbWhoisVo.delList.push({ whoisSeq: nWhoisSeq })
        })

        const res = await apiRequestJson(ipmsJsonApis.deleteTbWhoisVo, tbWhoisVo)
        if (res.tbWhoisVo.commonMsg === 'SUCCESS') {
          onMessagePopup(this, '정상적으로 삭제 하였습니다.')
          this.$emit('reload')
          this.close()
        } else {
          onMessagePopup(this, '삭제를 실패하였습니다.')
        }
      } catch (error) {
        console.log(error)
      }
    })
  },
  onClose() {
    this.resultListVo = []
  },
}
</script>
<style lang="scss" scoped></style>
