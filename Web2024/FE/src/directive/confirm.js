import Vue from 'vue'
import axios from 'axios'

/** *******************************************************
 * 사용법
 #main.is
 import '@/directive/confirm.js'

 #vue
 <el-button
                v-confirm="{
                  message: '삭제하시겠습니까?',
                  callback: handleDeleteComment
                }"
                type="text"
                icon="el-icon-delete"
                style="color:red;"
                circle
              />
  * 하지만 vue extend 를 이용하여 구현하는게 더 편해서 지금은 사용않마
  * directive 가 필요한 부분에 참고하도록 소스 남겨놓음
********************************************************/

var VueConfirm = Vue.directive('confirm', {
  inserted(el, binding) {
    // if no reload and callback not give, throw error
    if (typeof binding.modifiers.reload === 'undefined' &&
      typeof binding.value.callback === 'undefined') {
      // eslint-disable-next-line no-throw-literal
      throw 'A callback should be defined if reload is turned off.'
    }

    el.addEventListener('click', (event) => {
      var confirm = Vue.prototype.$confirm(binding.value.message)
      confirm.then(ok).catch(() => { /* nothing */ })

      function ok() {
        var data = {}

        // check for data
        if (binding.value.data != null && binding.value.data !== '') {
          data = binding.value.data
        }

        if (binding.value.link) {
          axios.post(binding.value.link, data).then(response => {
            if (binding.modifiers.reload && binding.modifiers.reload === true) {
              location.reload()
            } else {
              if (isFunction(binding.value.callback)) {
                binding.value.callback(response)
              }
            }
          })
        } else {
          if (isFunction(binding.value.callback)) {
            binding.value.callback()
          }
        }
      }
    })
  }
})

function isFunction(functionToCheck) {
  return functionToCheck && {}.toString.call(functionToCheck) === '[object Function]'
}

export default VueConfirm
