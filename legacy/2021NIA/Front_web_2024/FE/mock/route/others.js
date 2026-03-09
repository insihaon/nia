const { responseJson } = require('../response')

module.exports = [
  {
    url: '/selectList\.*',
    type: 'post',
    response: responseJson
  },
  {
    url: '/selectOne\.*',
    type: 'post',
    response: responseJson
  },
  {
    url: '/modify\.*',
    type: 'post',
    response: responseJson
  },
  {
    url: '/\.*',
    type: 'get',
    response: responseJson
  },
  {
    url: '/\.*',
    type: 'post',
    response: responseJson
  },
  {
    url: '/\.*',
    type: 'delete',
    response: responseJson
  }
]
