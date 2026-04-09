<template>
  <div>
    <transition :name="animation">
      <el-dialog
        v-if="animationVisible"
        v-el-drag-dialog
        :visible.sync="visible"
        :width="domElement.maxWidth + `px`"
        :fullscreen.sync="fullscreen"
        :modal-append-to-body="false"
        :append-to-body="true"
        :modal="modal"
        :close-on-click-modal="closeOnClickModal"
        :loading="loading"
        class="datahub-dialog"
        :class="{ [name]: true }"
      >
        <span slot="title">
          <i class="el-icon-document mr-2" style="font-size: 17px;" />
          신규 API 데이터 요청 확인
          <hr>
        </span>

        <table class="basic">

          <th>API명</th>
          <td class="disable">
            <el-input
              v-model="dataSetName"
              :placeholder="sendMessage.name"
            />
          </td>
          <tr>
            <th>파라미터 항목
              <!-- <el-tooltip :content="jsonDesc" effect="dark" placement="right">
                <i class="el-icon-question mr-2" style="font-size: 15px;" />
              </el-tooltip> -->
            </th>
            <td>
              <el-select
                v-model="selectedItem"
                collapse-tags
                :style="{ width: '100%' }"
                multiple
                @handleSelectedAll="changeSelected"
              >
                <el-option
                  label="전체"
                  value="ALL"
                  style="{ width: '100%' }"
                />
                <el-option
                  v-for="(option, i) in dataSetValue.list"
                  :key="i"
                  :label="option.column_nm"
                  :value="option.column_nm"
                  :style="{ width: '100%' }"
                />
              </el-select>
            </td>
          </tr>
          <tr>
            <th>용도 및 요청 설명
            </th>
            <td>
              <el-input
                v-model="dataSetDesc"
                type="textarea"
                :rows="4"
                :placeholder="sendMessage.usage"
              />
            </td>
          </tr>
          <tr>
            <th>연동 방식</th>
            <td>
              <el-select
                v-model="execModeCd"
                :collapse-tags="isViewport('<', 'sm')"
                :style="{ width: '100%' }"
                :placeholder="sendMessage.execMode"
              >
                <el-option
                  v-for="(option, i) in execMode"
                  :key="i"
                  :label="option.label"
                  :value="option.value"
                  :style="{ width: '100%' }"
                />
              </el-select>
            </td>
          </tr>
          <tr v-if="execModeCd === CONSTANTS.apiAlarm.batch.code">
            <th>연동 주기</th>
            <td>
              <el-input
                v-model="fixedDelay"
                :placeholder="sendMessage.fixedDelay"
              />
            </td>
          </tr>
        </table>

        <el-row class="my-1 check">{{ checkMessage }}</el-row>

        <div slot="footer" class="dialog-footer">
          <el-button size="medium" plain type="primary" class="aam-button" @click.native="onSubmit()">
            {{ $t('confirm') }}
          </el-button>
          <el-button size="medium" plain type="info" class="aam-button" @click.native="onClose()">
            {{ $t('cancel') }}
          </el-button>
        </div>
      </el-dialog>
    </transition>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import _ from 'lodash'
import { param } from '@/utils'
import { apiInsertDataSetReqProc, apiInsertApiAuthProc } from '@/api/dataHub'
import Eventbus from '@/utils/event-bus'

const routeName = 'ModalRequestData'

export default {
  name: routeName,
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      type: true,
      title: '',
      dataSetValue: {},
      checkMessage: '내용을 입력하고 확인을 선택하면 생성요청 작업을 수행합니다.',
      dataSetName: '',
      dataSetDesc: '',
      fixedDelay: '',
      selectedItem: [],
      execMode: [{ label: 'On-demand', value: 'O' }, { label: 'Batch', value: 'B' }],
      execModeCd: '',
      sendMessage: {
        name: 'API 데이터명을 입력하세요',
        usage: '용도 및 요청설명을 입력하세요',
        execMode: '연동방식을 선택해주세요',
        fixedDelay: '연동주기를 설정하세요(단위 : second, default : 300s)'
    },
    jsonDesc: 'value -> 문자열은 ""(큰따옴표), 숫자는 null로 표시해주세요.',
    reqDesc: 'API 데이터에 대한 용도와 JSON입력칸에 대한 설명을 입력하세요.',
    selectColumnList: []
    }
  },
  computed: {

  },
  watch: {
    'selectedItem'(n, o) {
      this.changeSelected(n, o)
    }
  },
  mounted() {

  },
  methods: {
  changeSelected(newValue, oldValue) {
      if (newValue.includes('ALL') && !oldValue.includes('ALL')) {
        const otherItems = this.dataSetValue.list.map(item => item.column_nm)
        this.selectedItem = ['ALL', ...otherItems]
      } else if (!newValue.includes('ALL') && oldValue.includes('ALL')) {
        this.selectedItem = []
      }
      this.selectColumnList = { list: this.dataSetValue.list }
    },
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
    },
    onOpen(model, actionMode) {
      this.dataSetValue = { list: model.modalEditInfo }
    },
    onClose() {
      this.dataSetName = ''
      this.dataSetDesc = ''
      this.selectedItem = []
      this.dataSetValue.list = []
      this.execModeCd = ''
      this.fixedDelay = ''
      this.close()
    },
    async onSubmit() {
      const paramiteList = this.selectedItem.map(String).join(', ')
      const dataSetInfo = {
        dataset_desc: this.dataSetDesc,
        dataset_request_column: paramiteList,
        dataset_nm: this.dataSetName,
        exec_mode_cd: this.execModeCd,
        fixed_delay: this.fixedDelay
       }
      try {
        if (dataSetInfo.dataset_nm !== '' & dataSetInfo.dataset_desc !== '') {
          const result = await apiInsertDataSetReqProc({
            info: dataSetInfo,
            values: this.selectColumnList
          })

          if (result.success) {
            this.$message.success('데이터가 요청되었습니다.')
            this.selectedItem = []
            this.dataSetValue.list = []
            this.execModeCd = ''
            this.fixedDelay = ''
            this.close()
            this.$emit('clearData', this.selectedItem)
            Eventbus.$emit('requestDataSetData')
          }
        } else {
          this.$message('API 데이터 명과 설명은 필수 입력 칸입니다.')
        }
      } catch (error) {
        this.$message.error({ message: `데이터 요청에 실패했습니다.` })
        console.log(error)
      }
    },
  },
}
</script>

<style lang="scss" scope>

.ModalRequestData {
    .disable-area{
      // background-color: #ccc;
      color: #999;
      pointer-events: none;
    }
}

</style>

