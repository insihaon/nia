var path = require('path')
var fs = require('fs')
const { forEach } = require('./route/json')

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

  /*
  __body : ipms
  data: other
  */
  return jsonObject['data'] || jsonObject['__body']
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

  const existFileName = findSimilarityAlgorithm(list, jsonFileName, dirPath)
  // const existFileName = findSortAlgorithm(list, jsonFileName)

  // 5. jsonFileName 을 삽입했던 위치(삽입 후 인덱스) 반환
  return existFileName
}

/**
 *
 * 문자열의 특징:
 *   오타가 많은 경우: Damerau-Levenshtein 거리
 *   순서가 중요하지 않은 경우: LCS, Jaccard 유사도
 *   이름이나 단어의 유사성: Jaro-Winkler 거리
 *   발음이 비슷한 단어: Soundex
 * 정확도:
 *   높은 정확도가 필요한 경우: Cosine 유사도, N-gram 모델
 * 계산 비용:
 *   빠른 계산이 필요한 경우: Levenshtein 거리, Jaccard 유사도
 * 문자열의 길이:
 *   짧은 문자열: Jaccard 유사도, Jaro-Winkler 거리
 *   긴 문자열: Levenshtein 거리, LCS
 */

/**
 * LCS(Longest Common Subsequence)
 * 개념: 최장 공통 부분 수열
 * 장점: 문자열 유사도 측정: 두 문자열의 유사도를 정량적으로 측정.
 * 단점: 문자열의 길이가 길어질수록 계산 시간이 증가.
 *
 * @param {*} str1
 * @param {*} str2
 * @returns
 */
function lcsAlgorithm(str1, str2) {
  const m = str1.length
  const n = str2.length

  // 2차원 배열 생성 및 초기화
  const dp = new Array(m + 1).fill(0).map(() => new Array(n + 1).fill(0))
  const traceback = new Array(m + 1).fill(0).map(() => new Array(n + 1).fill('')) // 추적 배열

  for (let i = 1; i <= m; i++) {
    for (let j = 1; j <= n; j++) {
      if (str1[i - 1] === str2[j - 1]) {
        dp[i][j] = dp[i - 1][j - 1] + 1
        traceback[i][j] = 'diagonal' // 대각선 방향으로 왔음을 표시
      } else {
        dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1])
        traceback[i][j] = dp[i - 1][j] > dp[i][j - 1] ? 'up' : 'left' // 위쪽 또는 왼쪽 방향으로 왔음을 표시
      }
    }
  }

  // LCS 길이
  const length = dp[m][n]

  // LCS 문자열 추출
  let i = m; let j = n; let result = ''
  while (i > 0 && j > 0) {
    if (traceback[i][j] === 'diagonal') {
      result = str1[i - 1] + result
      i--
      j--
    } else if (traceback[i][j] === 'up') {
      i--
    } else {
      j--
    }
  }

  console.log({ length, subsequence: result })

  return (length / Math.max(str1.length, str2.length))
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

function findSimilarityAlgorithm(list, jsonFileName, dirPath) {
  let rate = 0
  let index = 0
  const candidateFiles = []
  const rateList = []

  // 유사도 계산
  for (let i = 0; i < list.length; i++) {
    const str1 = (list[i] + '').split('_')[1]
    const str2 = (jsonFileName + '').split('_')[1]
    const curRate = jaccardAlgorithm(str1, str2)
    // console.log(curRate.toFixed(3), list[i])

    rateList.push(curRate)

    if (rate <= curRate) {
      index = i
      rate = curRate
    }
  }

  // rateList에서 가장 높은 rate의 파일 크기가 2KB를 넘으면 해당 파일 반환
  const highestRateFile = list[index]
  const highestRateFilePath = path.join(__dirname, dirPath, highestRateFile)
  const highestRateFileSize = fs.statSync(highestRateFilePath).size

  if (highestRateFileSize > 2 * 1024) {
    return highestRateFile
  }

  // 유사도가 threshold 범위에 해당하는 파일 수집
  // 데이터가 없는 응답을 최소화 하기 위함
  // 0: 유사도가 높은 파일 반환, 값이 높을 수록 비슷한 호출 중 파일 크기가 큰 파일 반환
  const threshold = 0.1
  for (let i = 0; i < rateList.length; i++) {
    if (Math.abs(rateList[i] - rate) <= threshold) {
      candidateFiles.push(list[i])
    }
  }

  // candidateFiles에 포함되지 않은 rate 제거
  for (let i = rateList.length - 1; i >= 0; i--) {
    if (Math.abs(rateList[i] - rate) > threshold) {
      rateList.splice(i, 1) // 조건에 맞지 않는 값을 제거
    }
  }

  // 후보 파일 중 가장 파일 용량이 큰 파일 찾기
  if (candidateFiles.length > 0) {
    let largestFile = candidateFiles[0]
    let largestPath = path.join(__dirname, dirPath, candidateFiles[0])
    let largestSize = fs.statSync(largestPath).size

    const log = false

    log && console.log(''.padEnd(100, '-'))
    log && console.log(`${'유사도'.padEnd(3, ' ')} | ${'파일크기'.padStart(8, ' ')} | 파일명`)
    log && console.log(''.padEnd(100, '-'))

    for (let i = 0; i < candidateFiles.length; i++) {
      largestPath = path.join(__dirname, dirPath, candidateFiles[i])
      const currentSize = fs.statSync(largestPath).size

      log && console.log(`${rateList[i].toFixed(3).padEnd(6, ' ')} | ${currentSize.toString().padStart(12, ' ')} | ${candidateFiles[i]}`)

      if (currentSize > largestSize) {
        largestFile = candidateFiles[i]
        largestSize = currentSize
      }
    }
    log && console.log(''.padEnd(100, '-'))
    return largestFile
  }

  // 후보가 없으면 유사도가 가장 높은 파일 반환
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
