const { responseJson } = require('../response')

module.exports = [
  {
    url: '/json\.*',
    type: 'get',
    response: responseJson
  },
  {
    url: '/json\.*',
    type: 'post',
    response: responseJson
  },
  {
    url: '/json\.*',
    type: 'delete',
    response: responseJson
  }
]
