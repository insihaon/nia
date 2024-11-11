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
          <col width="24%" /><col width="76%" />
        </colgroup>
        <tbody>
          <tr>
            <th>처리상태</th>
            <td>{{ resultVo.swhoisTranStatusNm }}</td>
          </tr>
        </tbody>
      </table>
    </div><!-- popupContentTable END -->

    <div class="popupContentTable">
      <div class="popupContentTableTitle">기본정보</div>
      <table>
        <colgroup>
          <col width="4%" /><col width="18%" /><col width="78%" />
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
    </div><!-- popupContentTable END -->
    <div class="popupContentTableBottom">
      <el-button type="primary" size="small" round @click="fnSetAddr('toKT')">KT 정보대체</el-button>
      <el-button type="primary" size="small" round @click="fnSetAddr('reset')">초기화</el-button>
    </div>

    <div v-if="resultVo.dbMatchYn === 'N'" id="allocDiv" class="popupContentTable">
      <div class="popupContentTableTitle">할당 테이블 고객정보</div>
      <table>
        <colgroup>
          <col width="20%" /><col width="80%" />
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
          <col width="4%" /><col width="18%" /><col width="78%" />
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
          <col width="4%" /><col width="18%" /><col width="78%" />
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
              <el-select v-model="resultVo.scity" size="small" class="w15">
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
              <span style="font-size:12px;color:#b5b5b5">(예: 2014.01.01)</span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="popupContentTable">
      <div class="popupContentTableTitle">3. 추가사항(Comment)</div>
      <table class="tbl_data entry" summary="추가사항">
        <colgroup>
          <col width="22%" /><col width="78%" />
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
    </div><!-- popupContentTable END -->

    <div class="popupContentTable">
      <div class="popupContentTableTitle">4. KRNIC 회신</div>
      <table>
        <colgroup>
          <col width="22%" /><col width="78%" />
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
      <el-button v-if="resultVo.dbMatchYn === 'N'" type="primary" size="small" round @click.native="fnSaveRegWhoisSubmit()">
        DB 현행화 전송
      </el-button>
      <template v-if="resultVo.swhoisresultCd === '03'">
        <el-button v-if="resultVo.delyn === 'N'" type="primary" size="small" round @click.native="fnSaveRegWhoisSubmit()">
          변경
        </el-button>
        <el-button v-if="resultVo.delyn === 'Y'" type="primary" size="small" round @click.native="fnSaveRegWhoisSubmit()">
          삭제
        </el-button>
      </template>
      <template v-else>
        <el-button v-if="resultVo.delyn === 'N'" type="primary" size="small" round @click.native="fnSaveRegWhoisSubmit()">
          변경
        </el-button>
      </template>
      <el-button type="primary" size="small" icon="el-icon-close" round @click.native="close()">
        닫기
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { apiRequestJson, ipmsJsonApis, apiRequestModel, ipmsModelApis } from '@/api/ipms'

const routeName = 'ModalRegWhois'

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
        dcreateDt: ''

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
      TbWhoisComplexVo_Sub: null
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
      setTimeout(() => {
        if (model.row) {
         this.fnViewDetailWhois(model.row)
        }
      }, 10)
    },
    async fnViewDetailWhois(row) {
      const target = ({ vue: this.$refs.content })
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
          this.scity = res?.scity
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
        // this.sAftOrgName = this.resultVo.sAftOrgName
        // this.sAftOrgAddr = this.resultVo.sAftOrgAddr
        // this.sAftOrgAddrDetail = this.resultVo.sAftOrgAddrDetail
        // this.sAftZipCode = this.resultVo.sAftZipCode
        // this.sAftEOrgName = this.resultVo.sAftEOrgName
        // this.sAftEOrgAddr = this.resultVo.sAftEOrgAddr
        // this.sAftEOrgAddrDetail = this.resultVo.sAftEOrgAddrDetail
      } else if (type === 'toKT') {
        this.sAftOrgName = this.ktInfoVo.sorgname
        this.sAftOrgAddr = this.ktInfoVo.sadmAddr
        this.sAftOrgAddrDetail = this.ktInfoVo.sadmAddrDetail
        this.sAftEOrgName = this.ktInfoVo.sadmEorgname
        this.sAftEOrgAddr = this.ktInfoVo.sadmEaddr
        this.sAftEOrgAddrDetail = this.ktInfoVo.sadmEaddrDetail
      }
    },
    async fnSaveRegWhoisSubmit() {
      let resubmit = false
      const tbWhoisComplexVo = {
        /* 이용기관 정보 */
        tbWhoisUserVo: {
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
        },
        /* 네트워크 정보 */
        tbWhoisVo: {
          swhoisresultCd: '01', // 전송대기 코드
          ssaid: this.resultVo.ssaid,
          sfirstAddr: this.resultVo.sfirstAddr,
          slastAddr: this.resultVo.slastAddr,
          snetNm: this.resultVo.snetNm,
          suserorggb: this.resultVo.suserorggb,
          scity: this.resultVo.scity,
          nwhoisSeq: this.resultVo.nwhoisSeq,
          ssvccommnet: this.resultVo.ssvccommnet,
          type: this.resultVo.type,
          srequestCd: this.resultVo.srequestCd,
          stransKind: 'ADMINs',
        }
      }

      if (tbWhoisComplexVo.tbWhoisUserVo.sorgname === '' || tbWhoisComplexVo.tbWhoisUserVo.sorgname === null) {
        this.$message('한글기관명을 입력하세요')
        return
      }

       if (tbWhoisComplexVo.tbWhoisUserVo.saddr === '' || tbWhoisComplexVo.tbWhoisUserVo.saddr === null) {
        this.$message('한글주소를 입력하세요')
        return
      }

       if (tbWhoisComplexVo.tbWhoisUserVo.szipcode === '' || tbWhoisComplexVo.tbWhoisUserVo.szipcode === null) {
        this.$message('우편주소를 입력하세요')
        return
      }

       if (tbWhoisComplexVo.tbWhoisUserVo.seorgname === '' || tbWhoisComplexVo.tbWhoisUserVo.seorgname === null) {
        this.$message('영문기관명을 입력하세요')
        return
      }

       if (tbWhoisComplexVo.tbWhoisUserVo.seaddr === '' || tbWhoisComplexVo.tbWhoisUserVo.seaddr === null) {
        this.$message('영문주소를 입력하세요')
        return
      }

      const regEx = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/ // 영문명 한글체크
      if (regEx.test(tbWhoisComplexVo.tbWhoisUserVo.seorgname)) {
        this.$message('영문기관명은 한글입력이 불가합니다.')
        return
      }

      // this.resultVo.srequestCd, this.resultVo.swhoisresultCd, this.resultVo.type
      const requestType = this.resultVo.srequestCd
      const resultCode = this.resultVo.swhoisresultCd
      const type = this.resultVo.type

      switch (requestType) {
        case '01': // 신규신청서
          if (resultCode === '03') { // 반송
            if (type) {
              resubmit = true
            } else {
              tbWhoisComplexVo.tbWhoisVo.type = 'DEFAULT'
              tbWhoisComplexVo.tbWhoisVo.srequestCd = '01'
            }
          } else if (resultCode === '04') { // 등록완료
            tbWhoisComplexVo.tbWhoisVo.type = 'DEFAULT'
            tbWhoisComplexVo.tbWhoisVo.srequestCd = '04' // 변경신청서 코드
          } else if (['01', '02', '05'].includes(resultCode)) { // 전송실패
            tbWhoisComplexVo.tbWhoisVo.type = 'DEFAULT'
            tbWhoisComplexVo.tbWhoisVo.srequestCd = '01' // 신규신청서 코드
          }
          break

        case '02': // 추가신청서
          if (resultCode === '03') { // 반송
            if (type) {
              resubmit = true
            } else {
              tbWhoisComplexVo.tbWhoisVo.type = 'DEFAULT'
              tbWhoisComplexVo.tbWhoisVo.srequestCd = '02'
            }
          } else if (resultCode === '04') { // 등록완료
            tbWhoisComplexVo.tbWhoisVo.type = 'DEFAULT'
            tbWhoisComplexVo.tbWhoisVo.srequestCd = '04' // 변경신청서 코드
          } else if (['01', '02', '05'].includes(resultCode)) { // 전송실패
            tbWhoisComplexVo.tbWhoisVo.type = 'DEFAULT'
            tbWhoisComplexVo.tbWhoisVo.srequestCd = '02' // 추가신청서 코드
          }
          break

        case '04': // 변경신청서
          if (type) {
            resubmit = true
          } else {
            tbWhoisComplexVo.tbWhoisVo.type = 'DEFAULT'
            tbWhoisComplexVo.tbWhoisVo.srequestCd = '04' // 변경신청서 코드
          }
          break

        case '03': // 삭제신청서
          if (resultCode === '03') { // 반송
            tbWhoisComplexVo.tbWhoisVo.type = 'DEFAULT'
            tbWhoisComplexVo.tbWhoisVo.srequestCd = '03' // 삭제신청서 코드
          } else if (['01', '02', '05'].includes(resultCode)) { // 전송실패
            tbWhoisComplexVo.tbWhoisVo.type = 'DEFAULT'
            tbWhoisComplexVo.tbWhoisVo.srequestCd = '03' // 삭제신청서 코드
          }
          break

        default:
          break
      }

      if (this.resultVo.snettype === 'CUSTOMER' && tbWhoisComplexVo.tbWhoisVo.suserorggb === 'INFRA') {
       this.$message('네트워크 유형이 CUSTOMER인 경우엔 이용기관분류를 INFRA로 지정하실 수 없습니다.')
       return
      }

      if (resubmit) {
        this.fnWhoisReSumbit(type, tbWhoisComplexVo)
      } else {
        if (requestType !== '01' || resultCode === '04') {
          if (this.resultVo.snettype === 'INFRA') {
            if (resultCode !== '01' || resultCode !== '02' || resultCode !== '05') {
            this.$message('네트워크 유형이 INFRA인 경우엔 기관정보를 수정하실 수 없습니다.')
              return
            }
          }
        }
      }

      try {
        const res = await apiRequestJson(ipmsJsonApis.updateWhoisComplexNew, tbWhoisComplexVo)
        if (res.tbWhoisVo.swhoisresultCd === '04') {
          this.$message('성공적으로 반영하였습니다.')
        } else if (res.tbWhoisVo.swhoisresultCd === '03') {
          this.$message('반송되었습니다.')
        } else {
          this.$message('반영에 실패하였습니다.')
        }
      } catch (error) {
        console.error(error)
      }
    },
//        async fnWhoisReSumbit(type, objVo) {
//       if (!type) return
//       const tbWhoisComplexVo = objVo

//       if(type == "ADD_NETNAME_ERROR") {

//       tbWhoisComplexVo.tbWhoisVo.type = type;
//       tbWhoisComplexVo.tbWhoisVo.srequestCd = "01"; 				// 신규신청서 코드
//       tbWhoisComplexVo.tbWhoisVo.swhoisresultCd = "01";
//       tbWhoisComplexVo.tbWhoisVo.swhoisresultMsg = " ";

// 		  this.TbWhoisComplexVo_Sub = tbWhoisComplexVo;

//       const tbWhoisVo = {
//         type : "ADD_COUNT",
//         ssaid : this.userVo.ssaid,
//         nwhoisSeq : null
//       }

//       const res = await apiRequestJson(ipmsJsonApis.selectWhoisComplexNew, tbWhoisVo)
//       //"opermgmt/whoismgmt/selectWhoisComplexNew.json"

//       if(res === null){
//         this.$message('Whois 데이터 변경 전송을 실패하였습니다.')
//         return
//       }

//       if (res) {

//         const whoisObj ={
//           type : tbWhoisVo.type,
//           tbWhoisComplexVo : this.TbWhoisComplexVo_Sub,
//         }

//       }

// 	} else if(type == "ADD_IPBLOCK_ERROR") {

// 		var url = baseContext + "opermgmt/whoismgmt/viewSearchKisa.ajax";
// 		var tbWhois = new Object();
// 		tbWhois.type = type;
// 		tbWhois.nwhoisSeq = tbWhoisComplexVo.tbWhoisVo.nwhoisSeq;
// 		var param = JSON.stringify(tbWhois);
// 		doAjaxSubmit(url, param, "html", "json", "fnViewSearchKisaCallback");

// 	} else if(type == "NEW_IPBLOCK_ERROR") {

// 		var url = baseContext + "opermgmt/whoismgmt/viewSearchKisa.ajax";
// 		var tbWhois = new Object();
// 		tbWhois.type = type;
// 		tbWhois.nwhoisSeq = tbWhoisComplexVo.tbWhoisVo.nwhoisSeq;
// 		var param = JSON.stringify(tbWhois);
// 		doAjaxSubmit(url, param, "html", "json", "fnViewSearchKisaCallback");

// 	} else if (type == "MOD_ERROR") {
// 		confirmDialog = createLayerConfirm("confirmDialog", 'auto', 400, "신규신청서로 재전송됩니다. 재전송하시겠습니까?", function() {
// 			var url = baseContext + "opermgmt/whoismgmt/updateWhoisComplexNew.json";

// 			obj.tbWhoisVo.srequestCd = "01";
// 			obj.tbWhoisVo.type = type;
// 			var param = JSON.stringify(obj);

// 			doAjaxSubmit(url, param, "json", "json", "fnUpdateRegWhoisSubmitCallback");

// 		});
// 	} else if (type == "NEW_NETNAME_ERROR") {
// 		confirmDialog = createLayerConfirm("confirmDialog", 'auto', 400, "추가신청서로 재전송됩니다. 재전송하시겠습니까?", function() {
// 			var url = baseContext + "opermgmt/whoismgmt/updateWhoisComplexNew.json";

// 			obj.tbWhoisVo.srequestCd = "02";
// 			obj.tbWhoisVo.type = type;
// 			var param = JSON.stringify(obj);

// 			doAjaxSubmit(url, param, "json", "json", "fnUpdateRegWhoisSubmitCallback");

// 		});
// 	}

// }

    onClose() { this.resultListVo = [] },
  },
}
</script>
<style lang="scss" scoped>
</style>
