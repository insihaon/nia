<template>
  <div class="profile-container">
    <div class="list">
      <el-tag
        v-for="item in profileList"
        :key="item.name"
        closable
        effect="plain"
        :type="ipms.curProfileByVue[propName] === item.name ? 'danger': 'primary'"
        @click="handleClickApply(item)"
        @close="handleClickRemove(item.name)"
      >
        {{ item.name }}
      </el-tag>
    </div>
    <div class="ml-1">
      <el-button type="info" icon="el-icon-document-add" size="mini" round plain @click="handleClickSave">프로파일 저장</el-button>
    </div>
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
import Eventbus from '@/utils/event-bus'
import { EventType } from '@/min/types'
import { onMessagePopup } from '@/utils/index'

const routeName = 'SearchConditionSaver'

export default {
  name: routeName,
  extends: Base,
  props: {
    propName: {
      type: String,
      default: ''
    },
    parameter: {
      type: Object,
      default() { return {} }
    },
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      profileList: []
    }
  },
  mounted () {
    this.initialization()
  },
  methods: {
    initialization() {
      const name = this.propName
      if (name) {
        this.$set(this, 'profileList', JSON.parse(window.localStorage['savedSearchCondition'] || '{}')[name] ?? [])
      }
    },
    async handleClickSave() {
      let savedProfile
      const name = this.propName
      if (name) {
        savedProfile = JSON.parse(window.localStorage['savedSearchCondition'] || '{}')
      }
      const currentViewProfile = savedProfile[name] ? savedProfile[name] : []
      if (this.ipms.curProfileByVue[this.propName]) {
        await this.confirm(`현재 '${this.ipms.curProfileByVue[this.propName]}' 프로파일이 적용 되어있습니다. 변경된 조건을 해당 프로파일에 덮어 씌우시겠습니까?`, '알림', {
          cancelButtonText: '취소',
          confirmButtonText: '확인',
          dangerouslyUseHTMLString: true
        }).then(async () => {
          await this.setCoverProfile(currentViewProfile)
        }).catch(action => {
          // if(action === 'cancel') {
          //   await this.setCoverProfile(currentViewProfile)
          // }
        })
      } else {
        await this.setAddProfile(currentViewProfile)
      }
      /* 저장 공통 */
      savedProfile[name] = currentViewProfile
      window.localStorage['savedSearchCondition'] = JSON.stringify(savedProfile)
      this.initialization()
    },
    setCoverProfile(currentViewProfile) {
      currentViewProfile.forEach(profileItem => {
        if (profileItem.name === this.ipms.curProfileByVue[this.propName]) {
          Object.assign(profileItem, { parameter: this.parameter })
        }
      })
    },
    async setAddProfile(currentViewProfile) {
      if (currentViewProfile.length >= 3) {
        onMessagePopup(this, '3개 이상 저장할 수 없습니다.')
        return
      }
      const result = await this.prompt('저장할 프로파일명을 입력하세요.', '현재 검색조건 저장')
      if (result.action === 'confirm') {
        if (result.value === null || result.value === '' || result.value.trim().length === 0) {
          onMessagePopup(this, '저장할 프로파일명이 빈 값입니다.')
          return
        }
        if (currentViewProfile.findIndex(d => d.name === result.value) !== -1) {
          onMessagePopup(this, '동일한 프로파일명이 존재합니다.')
          return
        }
        currentViewProfile.push({ name: result.value, parameter: this.parameter })
      }
    },
    handleClickApply(profile) {
      this.$store.dispatch('ipms/setCurProfileByVue', { key: this.propName, profileName: profile.name })
      const parameter = this._cloneDeep(profile.parameter)
      Eventbus.$emit(EventType.setSavedParameter, parameter)
    },
    async handleClickRemove(profileName) {
      await this.confirm(`프로파일 '${profileName}'을 삭제하시겠습니까?`, '알림', {
        cancelButtonText: '취소',
        confirmButtonText: '확인',
        dangerouslyUseHTMLString: true
      }).then(() => {
        let savedProfile
        const name = this.propName
        if (name) {
          savedProfile = JSON.parse(window.localStorage['savedSearchCondition'] || '{}')
        }
        if (savedProfile[name]) {
          const removeIdx = savedProfile[name].findIndex(v => v.name === profileName)
          if (removeIdx !== -1) {
            savedProfile[name].splice(removeIdx, 1)
          }
          window.localStorage['savedSearchCondition'] = JSON.stringify(savedProfile)
          this.initialization()
        }
      }).catch(action => {
      })
    }
  },
}
</script>
<style lang="scss" scoped>
.el-tag {
  cursor: pointer;
}
div.profile-container {
  width: 100%;display: flex;justify-content: space-between;
}
div.profile-container>div.list {
  display: flex;gap:10px;
}
</style>
