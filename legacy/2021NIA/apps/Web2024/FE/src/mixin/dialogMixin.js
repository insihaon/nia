/*
 *  version 1.0
 *
 *  Copyright ⓒ 2017 kt corp. All rights reserved.
 *
 *  This is a proprietary software of kt corp, and you may not use this file except in
 *  compliance with license agreement with kt corp. Any redistribution or use of this
 *  software, with or without modification shall be strictly prohibited without prior written
 *  approval of kt corp, and the copyright notice above does not evidence any actual or
 *  intended publication of such software.
 */
var dialogMixin = {
  props: {
    top: { type: String, default: '' },
    width: { type: String, default: '' },
    pageTitle: { type: String, default: '' },
    isDialog: { type: Boolean, default: false },
    visible: { type: Boolean, default: false },
    fullscreen: { type: Boolean, default: false },
    center: { type: Boolean, default: false }
  },
  methods: {
    fn_closed() {
      this.$emit('update:visible', false)

      /* setTimeout(()=>{
        var elem = this.$el.childNodes[0];
        elem.style = {};

        if (!this.fullscreen) {
          elem.style.marginTop = this.top;
          elem.style.width = this.width;
        }
      }, 400); */
    },
    fn_toggleFullScreen() {
      this.$emit('update:fullscreen', !this.fullscreen)
    },
    // 화면 중앙으로 옮기기
    fn_setCenter() {
      // var elem = this.$el.childNodes[0];
      // elem.style = {};

      // if (!this.fullscreen) {
      //   elem.style.marginTop = this.top;
      //   elem.style.width = this.width;
      // }
    }
  },
  watch: {
  },
}

export default dialogMixin
