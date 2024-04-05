var path = require('path')
var fs = require('fs')

function responseJson(config) {
  console.log('url'.padStart(15), ':', config.url)

  const param = config.headers

  if (!param.jsonfilename) {
    throw new Error(`JSON 파일 이름이 없습니다. param=${JSON.stringify(param)}`)
  }

  const dirPath = `json/${param.project}`
  // const decodeFileName = decodeURI(param.jsonfilename).replace(/^\//, '')
  const jsonFileName = param.jsonfilename

  console.log('saved FileName'.padStart(15), ':', jsonFileName)

  let filePath = path.join(__dirname, dirPath, jsonFileName)
  // console.log('config:', config)
  // console.log('param:', param)
  // console.log('decodeFileName:', decodeFileName)

  if (!fs.existsSync(filePath)) {
    filePath = findLikeFile(dirPath, jsonFileName)
  }

  console.log('loaded FilePath'.padStart(15), ':', filePath)

  const jsonObject = require(filePath)
  // console.log('jsonObject: ', jsonObject)
  console.log('--------------------------------------------------------------------------------------------------------------------------------')

  // __body: response data
  // __config: 요청 정보
  return jsonObject.__body
  // return jsonObject.data
}

function findLikeFile(dirPath, jsonFileName) {
  const existFileName = getLikeFileName(dirPath, jsonFileName)

  // console.log('filelist: ', fileList)
  // console.log('jsonFileName: ', jsonFileName)
  console.log('exist FileName'.padStart(15), ':', existFileName)

  const jsonFilePath = path.join(__dirname, dirPath, existFileName)
 // console.log(`jsonFilePath: `, jsonFilePath, existFileName)
  return jsonFilePath
}

function getLikeFileName (dirPath, jsonFileName) {
  const prefix = (jsonFileName + '').split('_')[0]
  const list = fs.readdirSync(path.join(__dirname, dirPath)).filter(file => file.startsWith(prefix) && file.endsWith('.json'))

  const existFileName = findJaccardAlgorithm(list, jsonFileName)
  // const existFileName = findSortAlgorithm(list, jsonFileName)

  // 5. jsonFileName 을 삽입했던 위치(삽입 후 인덱스) 반환
  return existFileName
}

// Jaccard 유사도 - string 간 유사도 측정
function jaccardSimilarity(str1, str2) {
  const set1 = new Set(str1.split(''))
  const set2 = new Set(str2.split(''))
  const intersection = new Set([...set1].filter(x => set2.has(x)))
  const union = new Set([...set1, ...set2])
  return intersection.size / union.size
}

function findJaccardAlgorithm(list, jsonFileName) {
  let rate = 0
  let index = 0
  for (let i = 0; i < list.length; i++) {
    const curRate = jaccardSimilarity(jsonFileName, list[i])
    // console.log(curRate.toFixed(3), list[i])
    if (rate <= curRate) {
      index = i
      rate = curRate
    }
  }

  return list[index]
}

function findSortAlgorithm(list, jsonFileName) {
  const _log = console.log
  // const _log = () => {}

  // 1. 리스트 정렬
  list.sort()

  // 2. 리스트에 jsonFileName 존재 여부 확인
  let index = list.indexOf(jsonFileName)
  if (index !== -1) {
    _log('f0 :', list[index])
    return list[index]
  }

  _log('list :', list.length)

  // 3. 리스트에 jsonFileName 삽입
  list.push(jsonFileName)
  list.sort()

  _log('list + :', list.length)

  // 4. 삽입된 jsonFileName의 인덱스 찾기 및 제거
  index = list.indexOf(jsonFileName)

  if (jsonFileName === 'ZGFzaGJvYXJkL3dvcmtzdGF0ZS9zZWxlY3RDbWRHcmlkRGF0YQ_fDIwMjQtMDQtMDJ8MjAyNC0wNC0wMnwyMDI0LTA0LTAzfDQ5ODc5MA.json') {
    for (let idx = Math.max(index - 3, 0); idx < Math.min(index + 3, list.length); idx++) {
      _log(idx, list[idx])
    }
  }
  _log('index :', index)

  list.splice(index, 1)

  let existFileName
  if (index < 0) {
    existFileName = list[0]
    _log('f1 :', existFileName)
  } else if (index >= list.length) {
    existFileName = list[list.length - 1]
    _log('f2 :', existFileName)
  } else {
    existFileName = list[index]
    try {
      const prefix = (jsonFileName + '').split('_')[0]
      if (!(list[index] + '').startsWith(prefix)) {
        _log('diffrent prefix: ', prefix)
        existFileName = list[index - 1]
      }
    } catch (error) {
      console.log(error)
    }
    _log('f3 :', existFileName)
  }
}

module.exports = {
  responseJson
}
