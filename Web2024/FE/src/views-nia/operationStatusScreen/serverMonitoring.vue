<template>
  <div :class="{ [name]: true }" style="background-color: #1f335d;">
    <div class="all-container w-100">
      <div class="sub-container">
        <div class="title">서버 자원 사용량 <i class="el-icon-refresh" @click="reloadData('resource')" /></div>
        <div class="d-flex">
          <div v-for="(item, index) in resource" :key="index" class="frame-container" :class="item.class">
            <div class="frame-title">{{ item.title }}</div>
            <iframe
              id="resource"
              scrolling="yes"
              :src="item.url"
              width="100%"
              height="300px"
              frameBorder="0"
            />
          </div>
        </div>
      </div>

      <div class="sub-container">
        <div class="title">파일 이상 여부(사이즈 한정)를 시각화 및 실시간 (3시간 범위) <i class="el-icon-refresh" @click="reloadData('file3Hours')" /></div>
        <div class="d-flex">
          <div v-for="(item, index) in file3Hours1" :key="index" class="frame-container" :class="item.class">
            <div class="frame-title">{{ item.title }}</div>
            <iframe
              id="file3Hours"
              scrolling="yes"
              :src="item.url"
              width="100%"
              height="150px"
              frameBorder="0"
            />
          </div>
        </div>
        <div class="d-flex">
          <div v-for="(item, index) in file3Hours2" :key="index" class="frame-container" :class="item.class">
            <div class="frame-title">{{ item.title }}</div>
            <iframe
              id="file3Hours"
              scrolling="yes"
              :src="item.url"
              width="100%"
              height="150px"
              frameBorder="0"
            />
          </div>
        </div>
      </div>

      <div class="sub-container">
        <div class="title">파일 이상 여부(사이즈 한정)를 시각화 및 실시간 (하루 주기, 7일 범위) <i class="el-icon-refresh" @click="reloadData('fileDays')" /></div>
        <div class="d-flex">
          <div v-for="(item, index) in fileDays" :key="index" class="frame-container" :class="item.class">
            <div class="frame-title">{{ item.title }}</div>
            <iframe
              id="fileDays"
              scrolling="yes"
              :src="item.url"
              width="100%"
              height="150px"
              frameBorder="0"
            />
          </div>
        </div>
      </div>

      <div class="sub-container">
        <div class="title">파일 데이터 Value 시각화 및 실시간  <i class="el-icon-refresh" @click="reloadData('fileData')" /></div>
        <div class="d-flex">
          <div v-for="(item, index) in fileData1" :key="index" class="frame-container" :class="item.class">
            <div class="frame-title">{{ item.title }}</div>
            <iframe
              id="fileData"
              scrolling="yes"
              :src="item.url"
              width="100%"
              height="200px"
              frameBorder="0"
            />
          </div>
        </div>
        <div class="d-flex">
          <div v-for="(item, index) in fileData2" :key="index" class="frame-container" :class="item.class">
            <div class="frame-title">{{ item.title }}</div>
            <iframe
              id="fileData"
              scrolling="yes"
              :src="item.url"
              width="100%"
              height="200px"
              frameBorder="0"
            />
          </div>
        </div>

        <div class="sub-container">
          <div class="title">파일 이상 여부(file size 기준) <i class="el-icon-refresh" @click="reloadData('fileWrong')" /></div>
          <div class="d-flex">
            <div v-for="(item, index) in fileWrong1" :key="index" class="frame-container" :class="item.class">
              <div class="frame-title">{{ item.title }}</div>
              <iframe
                id="fileWrong"
                scrolling="yes"
                :src="item.url"
                width="100%"
                height="150px"
                frameBorder="0"
              />
            </div>
          </div>
          <div class="d-flex">
            <div v-for="(item, index) in fileWrong2" :key="index" class="frame-container" :class="item.class">
              <div class="frame-title">{{ item.title }}</div>
              <iframe
                id="fileWrong"
                scrolling="yes"
                :src="item.url"
                width="100%"
                height="150px"
                frameBorder="0"
              />
            </div>
          </div>
          <div class="d-flex">
            <div v-for="(item, index) in fileWrong3" :key="index" class="frame-container" :class="item.class">
              <div class="frame-title">{{ item.title }}</div>
              <iframe
                id="fileWrong"
                scrolling="yes"
                :src="item.url"
                width="100%"
                height="150px"
                frameBorder="0"
              />
            </div>
          </div>
        </div>

        <div class="sub-container">
          <div class="title">수집 데이터 <i class="el-icon-refresh" @click="reloadData('collect')" /></div>
          <div v-for="(item, index) in collect" :key="index" class="frame-container" :class="item.class">
            <div class="frame-title">{{ item.title }}</div>
            <iframe
              id="collect"
              scrolling="yes"
              :src="item.url"
              width="100%"
              height="220px"
              frameBorder="0"
            />
          </div>
        </div>

      </div>
    </div>
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompInquiryPannel from '@/views-nia/components/CompInquiryPannel'

const routeName = 'ServerMonitoring'

const prefixUrl = 'http://incodej-lab.iptime.org:3000/d-solo'
export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { CompInquiryPannel },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      tempSrc: 'http://incodej-lab.iptime.org:3000/d-solo/XB7JYac7z/koren_data_all?orgId=1&panelId=24',
      resource: [
        { title: 'CPU 사용량', url: prefixUrl + '/LBlvJ7p7z/server_status?orgId=1&refresh=1m&panelId=14', class: 'w-50' },
        { title: 'Memory 사용량', url: prefixUrl + '/LBlvJ7p7z/server_status?orgId=1&refresh=1m&panelId=12', class: 'w-50' }
      ],
      file3Hours1: [
        { title: '장비', url: prefixUrl + '/88QEOB2nz/file_size?orgId=1&refresh=1m&panelId=22', class: 'w-50' },
        { title: '경로', url: prefixUrl + '/LBlvJ7p7z/server_status?orgId=1&refresh=1m&panelId=12', class: 'w-50' },
      ],
      file3Hours2: [
        { title: 'ipSdnSflow', url: prefixUrl + '/LBlvJ7p7z/server_status?orgId=1&refresh=1m&panelId=12', class: 'w-2/6' },
        { title: 'ipSdnSyslog', url: prefixUrl + '/LBlvJ7p7z/server_status?orgId=1&refresh=1m&panelId=12', class: 'w-2/6' },
        { title: 'ipSdnSyslogAlarm', url: prefixUrl + '/LBlvJ7p7z/server_status?orgId=1&refresh=1m&panelId=12', class: 'w-2/6' },
      ],
      fileDays: [
        { title: 'link', url: prefixUrl + '/1edhLePnk/file_size_week?orgId=1&refresh=1h&panelId=18', class: 'w-2/6' },
        { title: 'node', url: prefixUrl + '/1edhLePnk/file_size_week?orgId=1&refresh=1h&panelId=19', class: 'w-2/6' },
        { title: 'interface', url: prefixUrl + '/1edhLePnk/file_size_week?orgId=1&refresh=1h&panelId=20', class: 'w-2/6' },
      ],
      fileData1: [
        { title: '장비', url: prefixUrl + '/XB7JYac7z/koren_data_all?orgId=1&panelId=24', class: 'w-50' },
        { title: '경로', url: prefixUrl + '/XB7JYac7z/koren_data_all?orgId=1&panelId=20', class: 'w-50' },
      ],
      fileData2: [
        { title: 'ipSdnSflow', url: prefixUrl + '/XB7JYac7z/koren_data_all?orgId=1&panelId=25', class: 'w-2/6' },
        { title: 'ipSdnSyslog', url: prefixUrl + '/XB7JYac7z/koren_data_all?orgId=1&panelId=21', class: 'w-2/6' },
        { title: 'ipSdnSyslogAlarm', url: prefixUrl + '/XB7JYac7z/koren_data_all?orgId=1&panelId=26', class: 'w-2/6' },
      ],
      fileWrong1: [
        { title: 'IP 시설 정보', url: prefixUrl + '/1edhLePnk/file_size_week?orgId=1&panelId=1', class: 'w-2/6' },
        { title: '', url: prefixUrl + '/1edhLePnk/file_size_week?orgId=1&panelId=5', class: 'w-2/6' },
        { title: '', url: prefixUrl + '/1edhLePnk/file_size_week?orgId=1&panelId=7', class: 'w-2/6' },
      ],
      fileWrong2: [
        { title: '전송 시설 정보', url: prefixUrl + '/1edhLePnk/file_size_week?orgId=1&panelId=2' },
        { title: '', url: prefixUrl + '/1edhLePnk/file_size_week?orgId=1&panelId=3' },
        { title: '', url: prefixUrl + '/1edhLePnk/file_size_week?orgId=1&panelId=4' },
        { title: '', url: prefixUrl + '/1edhLePnk/file_size_week?orgId=1&panelId=8' },
        { title: '', url: prefixUrl + '/1edhLePnk/file_size_week?orgId=1&panelId=9' },
        { title: '', url: prefixUrl + '/1edhLePnk/file_size_week?orgId=1&panelId=10' },
      ],
      fileWrong3: [
        { title: 'Sflow 트래픽', url: prefixUrl + '/88QEOB2nz/file_size?orgId=1&panelId=12', class: 'w-2/6' },
        { title: '광세기', url: prefixUrl + '/88QEOB2nz/file_size?orgId=1&panelId=6', class: 'w-2/6' },
        { title: 'IP 트래픽', url: prefixUrl + '/88QEOB2nz/file_size?orgId=1&panelId=11', class: 'w-2/6' }
      ],
      collect: [
        { title: '광세기', url: prefixUrl + '/XB7JYac7z/koren_data_all?orgId=1&panelId=14', class: 'w-100' },
        { title: '트래픽', url: prefixUrl + '/XB7JYac7z/koren_data_all?orgId=1&panelId=16', class: 'w-100' },
        { title: 'Sflow', url: prefixUrl + '/XB7JYac7z/koren_data_all?orgId=1&panelId=18', class: 'w-100' },
      ]
    }
  },

  computed: {
  },
  mounted() {

  },
  methods: {
    reloadData(type) {
      const elements = document.querySelectorAll(`#${type}`)
      if (elements && elements.length > 0) {
        elements.forEach(el => {
          el.src = el?.src
        })
      }
    }
  },
}
</script>
<style lang="scss" scoped>
.sub-container {
  width: 100%;
  color: #eee;
  .title {
    font-size: 16px;
    font-weight: 600;
    padding-top: 10px;
    padding-left: 20px;
  }
  .frame-container {
    margin: 5px 10px 10px 10px;
    border-radius: 15px;
    padding: 15px;
    background-color: #1c2b40;
  }
  .frame-title {
    color: #fff;
    font-size: 15px;
    padding-bottom: 10px;
    padding-left: 20px;
  }
}
</style>
