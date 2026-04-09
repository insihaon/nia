import Vue from 'vue'
import VueI18n from 'vue-i18n'
import en from './locales/en.json'
import ko from './locales/ko.json'
import { AppOptions } from '@/class/appOptions'

Vue.use(VueI18n)

export const i18n = new VueI18n({
  locale: AppOptions.instance.language,
  fallbackLocale: AppOptions.instance.language,
  messages: { en, ko }
})

export function $t(key) {
  console.log(i18n.t(key))
  return i18n.t(key) ?? key
}
