var path = require('path')
var fs = require('fs')

function responseJson(config) {
  console.log('url'.padStart(17), ':', config.url)

  const param = config.headers

  if (!param.jsonfilename) {
    throw new Error(`JSON 파일 이름이 없습니다. param=${JSON.stringify(param)}`)
  }

  const dirPath = `json/${param.project}`
  // const decodeFileName = decodeURI(param.jsonfilename).replace(/^\//, '')
  const jsonFileName = param.jsonfilename

  console.log('REQ FileName'.padStart(17), ':', jsonFileName)

  let filePath = path.join(__dirname, dirPath, jsonFileName)
  // console.log('config:', config)
  // console.log('param:', param)
  // console.log('decodeFileName:', decodeFileName)

  if (!fs.existsSync(filePath)) {
    filePath = findSimilarFile(dirPath, jsonFileName)
  }

  console.log('RES FilePath'.padStart(17), ':', filePath)

  const jsonObject = require(filePath)
  // console.log('jsonObject: ', jsonObject)
  console.log('--------------------------------------------------------------------------------------------------------------------------------')

  // __body: response data
  // __config: 요청 정보
  return jsonObject.__body
  // return jsonObject.data
}

function findSimilarFile(dirPath, jsonFileName) {
  const existFileName = getSimilarFileName(dirPath, jsonFileName)

  // console.log('filelist: ', fileList)
  // console.log('jsonFileName: ', jsonFileName)
  console.log('Similar FileName'.padStart(17), ':', existFileName)

  const jsonFilePath = path.join(__dirname, dirPath, existFileName)
 // console.log(`jsonFilePath: `, jsonFilePath, existFileName)
  return jsonFilePath
}

function getSimilarFileName (dirPath, jsonFileName) {
  const prefix = (jsonFileName + '').split('_')[0]
  const list = fs.readdirSync(path.join(__dirname, dirPath)).filter(file => file.startsWith(prefix) && file.endsWith('.json'))

  const existFileName = findSimilarityAlgorithm(list, jsonFileName)
  // const existFileName = findSortAlgorithm(list, jsonFileName)

  // 5. jsonFileName 을 삽입했던 위치(삽입 후 인덱스) 반환
  return existFileName
}

/**
 * Levenshtein 거리 (Edit Distance)
 * 개념: 두 문자열을 서로 변환하기 위해 필요한 최소한의 편집 횟수 (삽입, 삭제, 변경)를 측정합니다. 값이 작을수록 유사도가 높습니다.
 * 장점: 간단하고 구현하기 쉽습니다.
 * 단점: 문자열의 길이가 길어지면 계산 비용이 증가할 수 있습니다.
 */
function levenshteinAlgorithm(str1, str2) {
  const m = str1.length
  const n = str2.length
  const dp = Array(m + 1)
    .fill(0)
    .map(() => Array(n + 1).fill(0))

  for (let i = 0; i <= m; i++) {
    dp[i][0] = i
  }

  for (let j = 0; j <= n; j++) {
    dp[0][j] = j
  }

  for (let i = 1; i <= m; i++) {
    for (let j = 1; j <= n; j++) {
      const cost = str1[i - 1] === str2[j - 1] ? 0 : 1
      dp[i][j] = Math.min(
        dp[i - 1][j] + 1, // 삭제
        dp[i][j - 1] + 1, // 삽입
        dp[i - 1][j - 1] + cost // 변경
      )
    }
  }

  const distance = dp[m][n]
  return 1 - (distance / Math.max(str1.length, str2.length))
}

/**
 * Jaccard 유사도 - string 간 유사도 측정
 * 개념: 두 집합의 교집합 크기를 합집합 크기로 나눈 값입니다. 문자열을 n-gram으로 분할하여 집합으로 취급하고 계산합니다.
 * 장점: 문자열의 순서에 영향을 받지 않습니다.
 * 단점: 짧은 문자열이나 중복되는 단어가 많은 경우 정확도가 떨어질 수 있습니다.
 */
function jaccardAlgorithm(str1, str2) {
  const set1 = new Set(str1.split(''))
  const set2 = new Set(str2.split(''))
  const intersection = new Set([...set1].filter(x => set2.has(x)))
  const union = new Set([...set1, ...set2])
  return intersection.size / union.size
}

function findSimilarityAlgorithm(list, jsonFileName) {
  let rate = 0
  let index = 0
  for (let i = 0; i < list.length; i++) {
    const str1 = (list[i] + '').split('_')[1]
    const str2 = (jsonFileName + '').split('_')[1]
    const curRate = jaccardAlgorithm(str1, str2)
    // const curRate = levenshteinAlgorithm(str1, str2)
    // console.log(curRate.toFixed(3), list[i])
    if (rate <= curRate) {
      index = i
      rate = curRate
    }
  }
  // console.log(jsonFileName, list[index])

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
