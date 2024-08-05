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
        WHOIS 정보
        <hr>
      </span>
      <div id="content" class="layer">
        <div id="whoisView" class="content_result">
          <h3>KISA WHOIS / <span>WHOIS 정보</span></h3>

          <h4 class="mt10">IP 주소 정보</h4>
          <table class="tbl_data" summary="IP 주소 정보">
            <caption>IP 주소 정보</caption>
            <colgroup>
              <col width="15%" />
              <col width="35%" />
              <col width="15%" />
              <col width="35%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th class="first" scope="row">IP 주소</th>
                <td>{{ whoisVo.query || '-' }}</td>
                <th scope="row">연결 ISP명</th>
                <td>{{ whoisVo.korean.isp.netinfo.orgName || '-' }}</td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">등록일</th>
                <td colspan="3">{{ whoisVo.korean.isp.netinfo.regDate || '-' }}</td>
              </tr>
            </tbody>
          </table>

          <div class="tabmenu mt10">
            <ul class="tab">
              <li class="first" :class="{ on: tabIndex === 0 }">
                <a href="#none" @click.prevent="tabIndex = 0">USER</a>
              </li>
              <li :class="{ on: tabIndex === 1 }">
                <a href="#none" @click.prevent="tabIndex = 1">PI</a>
              </li>
              <li class="last" :class="{ on: tabIndex === 2 }">
                <a href="#none" @click.prevent="tabIndex = 2">ISP</a>
              </li>
            </ul>
          </div>

          <table v-show="tabIndex === 0" class="tbl_data" summary="기관 정보">
            <caption>기관 정보</caption>
            <colgroup>
              <col width="15%" />
              <col width="35%" />
              <col width="15%" />
              <col width="35%" />
            </colgroup>
            <tbody>
              <tr>
                <td class="first" scope="row" colspan="4">
                  <h4>기관정보 (USER)</h4>
                </td>
              </tr>
              <tr class="top">
                <th class="first" scope="row">네트워크이름</th>
                <td>{{ whoisVo.korean.user.netinfo.netName || '-' }}</td>
                <th scope="row">한글 기관명</th>
                <td>{{ whoisVo.korean.user.netinfo.orgName || '-' }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">한글 주소</th>
                <td colspan="3">{{ whoisVo.korean.user.netinfo.addr || '-' }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">영문 기관명</th>
                <td colspan="3">{{ whoisVo.english.user.netinfo.orgName || '-' }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">영문주소</th>
                <td colspan="3">{{ whoisVo.english.user.netinfo.addr || '-' }}</td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">기관고유번호</th>
                <td>{{ whoisVo.korean.user.netinfo.orgID || '-' }}</td>
                <th scope="row">우편번호</th>
                <td>{{ whoisVo.korean.user.netinfo.zipCode || '-' }}</td>
              </tr>
            </tbody>
          </table>

          <table v-show="tabIndex === 1" class="tbl_data" summary="기관 정보">
            <caption>기관 정보</caption>
            <colgroup>
              <col width="15%" />
              <col width="35%" />
              <col width="15%" />
              <col width="35%" />
            </colgroup>
            <tbody>
              <tr>
                <td class="first" scope="row" colspan="4">
                  <h4>기관정보 (PI)</h4>
                </td>
              </tr>
              <tr class="top">
                <th class="first" scope="row">네트워크이름</th>
                <td>{{ whoisVo.korean.pi.netinfo.netName || '-' }}</td>
                <th scope="row">한글 기관명</th>
                <td>{{ whoisVo.korean.pi.netinfo.orgName || '-' }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">한글 주소</th>
                <td colspan="3">{{ whoisVo.korean.pi.netinfo.addr || '-' }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">영문 기관명</th>
                <td colspan="3">{{ whoisVo.english.pi.netinfo.orgName || '-' }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">영문주소</th>
                <td colspan="3">{{ whoisVo.english.pi.netinfo.addr || '-' }}</td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">기관고유번호</th>
                <td>{{ whoisVo.korean.pi.netinfo.orgID || '-' }}</td>
                <th scope="row">우편번호</th>
                <td>{{ whoisVo.korean.pi.netinfo.zipCode || '-' }}</td>
              </tr>
            </tbody>
          </table>

          <table v-show="tabIndex === 2" class="tbl_data" summary="기관 정보">
            <caption>기관 정보</caption>
            <colgroup>
              <col width="15%" />
              <col width="35%" />
              <col width="15%" />
              <col width="35%" />
            </colgroup>
            <tbody>
              <tr>
                <td class="first" scope="row" colspan="4">
                  <h4>기관정보 (ISP)</h4>
                </td>
              </tr>
              <tr class="top">
                <th class="first" scope="row">네트워크이름</th>
                <td>{{ whoisVo.korean.isp.netinfo.netName || '-' }}</td>
                <th scope="row">한글 기관명</th>
                <td>{{ whoisVo.korean.isp.netinfo.orgName || '-' }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">한글 주소</th>
                <td colspan="3">{{ whoisVo.korean.isp.netinfo.addr || '-' }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">영문 기관명</th>
                <td colspan="3">{{ whoisVo.english.isp.netinfo.orgName || '-' }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">영문주소</th>
                <td colspan="3">{{ whoisVo.english.isp.netinfo.addr || '-' }}</td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">기관고유번호</th>
                <td>{{ whoisVo.korean.isp.netinfo.orgID || '-' }}</td>
                <th scope="row">우편번호</th>
                <td>{{ whoisVo.korean.isp.netinfo.zipCode || '-' }}</td>
              </tr>
            </tbody>
          </table>

          <table v-show="tabIndex === 0" class="tbl_data mt10" summary="관리자 정보">
            <caption>관리자 정보</caption>
            <colgroup>
              <col width="15%" />
              <col width="35%" />
              <col width="15%" />
              <col width="35%" />
            </colgroup>
            <tbody>
              <tr>
                <td class="first" scope="row" colspan="4">
                  <h4>관리자정보 (USER)</h4>
                </td>
              </tr>
              <tr class="top">
                <th class="first" scope="row">한글 기관명</th>
                <td colspan="3">{{ whoisVo.korean.user.techContact.orgName || '-' }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">한글 주소</th>
                <td colspan="3">{{ whoisVo.korean.user.techContact.addr || '-' }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">영문 기관명</th>
                <td colspan="3">{{ whoisVo.english.user.techContact.orgName || '-' }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">영문주소</th>
                <td colspan="3">{{ whoisVo.english.user.techContact.addr || '-' }}</td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">전자우편</th>
                <td class="mail">
                  <a href="#none">{{ whoisVo.korean.user.techContact.email || '-' }}</a>
                </td>
                <th scope="row">우편번호</th>
                <td>{{ whoisVo.korean.user.techContact.zipCode || '-' }}</td>
              </tr>
            </tbody>
          </table>

          <table v-show="tabIndex === 1" class="tbl_data mt10" summary="관리자 정보">
            <caption>관리자 정보</caption>
            <colgroup>
              <col width="15%" />
              <col width="35%" />
              <col width="15%" />
              <col width="35%" />
            </colgroup>
            <tbody>
              <tr>
                <td class="first" scope="row" colspan="4">
                  <h4>관리자정보 (PI)</h4>
                </td>
              </tr>
              <tr class="top">
                <th class="first" scope="row">한글 기관명</th>
                <td colspan="3">{{ whoisVo.korean.pi.techContact.orgName || '-' }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">한글 주소</th>
                <td colspan="3">{{ whoisVo.korean.pi.techContact.addr || '-' }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">영문 기관명</th>
                <td colspan="3">{{ whoisVo.english.pi.techContact.orgName || '-' }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">영문주소</th>
                <td colspan="3">{{ whoisVo.english.pi.techContact.addr || '-' }}</td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">전자우편</th>
                <td class="mail">
                  <a href="#none">{{ whoisVo.korean.pi.techContact.email || '-' }}</a>
                </td>
                <th scope="row">우편번호</th>
                <td>{{ whoisVo.korean.pi.techContact.zipCode || '-' }}</td>
              </tr>
            </tbody>
          </table>

          <table v-show="tabIndex === 2" class="tbl_data mt10" summary="관리자 정보">
            <caption>관리자 정보</caption>
            <colgroup>
              <col width="15%" />
              <col width="35%" />
              <col width="15%" />
              <col width="35%" />
            </colgroup>
            <tbody>
              <tr>
                <td class="first" scope="row" colspan="4">
                  <h4>관리자정보 (ISP)</h4>
                </td>
              </tr>
              <tr class="top">
                <th class="first" scope="row">한글 기관명</th>
                <td colspan="3">{{ whoisVo.korean.isp.techContact.orgName || '-' }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">한글 주소</th>
                <td colspan="3">{{ whoisVo.korean.isp.techContact.addr || '-' }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">영문 기관명</th>
                <td colspan="3">{{ whoisVo.english.isp.techContact.orgName || '-' }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">영문주소</th>
                <td colspan="3">{{ whoisVo.english.isp.techContact.addr || '-' }}</td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">전자우편</th>
                <td class="mail">
                  <a href="#none">{{ whoisVo.korean.isp.techContact.email || '-' }}</a>
                </td>
                <th scope="row">우편번호</th>
                <td>{{ whoisVo.korean.isp.techContact.zipCode || '-' }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button size="mini" class="el-icon-close" @click.native="close()">{{ $t('exit') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'

const routeName = 'ModelWhoInfoDetail'

export default {
  name: routeName,
  components: { },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      tabIndex: 0,
      whoisVo: {
        query: null,
        korean: {
          isp: {
            netinfo: {
              orgName: null,
              regDate: null,
              netName: null,
              addr: null,
              orgID: null,
              zipCode: null
            },
            techContact: {
              orgName: null,
              addr: null,
              email: null,
              zipCode: null
            }
          },
          user: {
            netinfo: {
              netName: null,
              orgName: null,
              addr: null,
              orgID: null,
              zipCode: null
            },
            techContact: {
              orgName: null,
              addr: null,
              email: null,
              zipCode: null
            }
          },
          pi: {
            netinfo: {
              netName: null,
              orgName: null,
              addr: null,
              orgID: null,
              zipCode: null
            },
            techContact: {
              orgName: null,
              addr: null,
              email: null,
              zipCode: null
            }
          }
        },
        english: {
          isp: {
            netinfo: {
              orgName: null,
              addr: null
            },
            techContact: {
              orgName: null,
              addr: null
            }
          },
          user: {
            netinfo: {
              orgName: null,
              addr: null
            },
            techContact: {
              orgName: null,
              addr: null
            }
          },
          pi: {
            netinfo: {
              orgName: null,
              addr: null
            },
            techContact: {
              orgName: null,
              addr: null
            }
          }
        }
      }
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1200
    },
    onOpen(model, actionMode) {
      this.selectedRow = model?.response ?? null
      if (model?.response) {
        this.whoisVo = model?.response?.result.resultVo
      }
    },
    onClose() {
    },
  },
}
</script>
<style lang="scss" scoped>
</style>
