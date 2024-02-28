import { AppOptions } from '@/class/appOptions'
const { debug, isProd } = AppOptions.instance

export const filePath = __filename.replace(/\\/g, '/')

// export function apiDataListTest(params = {}) {
//   return http({
//     url: '/selectList',
//     method: 'post',
//     filePath: filePath,
//     sqlId: 'SELECT_API_LIST',
//     data: params
//   })
// }
