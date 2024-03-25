var path = require('path')
var fs = require('fs')

function responseJson(config) {
  const param = config.headers

  if (!param.jsonfilename) {
    throw new Error(`JSON 파일 이름이 없습니다. param=${JSON.stringify(param)}`)
  }

  const dirPath = `json/${param.project}`
  // const decodeFileName = decodeURI(param.jsonfilename).replace(/^\//, '')
  const jsonFileName = param.jsonfilename
  const jsonFilePath = `${dirPath}/${jsonFileName}`

  console.log('jsonFileName:', jsonFileName)

  // console.log('config:', config)
  // console.log('param:', param)
  // console.log('decodeFileName:', decodeFileName)
  // console.log('jsonFilePath:', jsonFilePath)

  let filePath = jsonFilePath

  if (!fs.existsSync(filePath)) {
    filePath = findLikeFile(dirPath, jsonFileName)
  }

  const jsonObject = require(filePath)
  console.log('jsonObject: ', jsonObject)
  return jsonObject

  // return {
  //   status: 200,
  //   body: JSON.stringify(jsonObject),
  // }
}

function findLikeFile(dirPath, jsonFileName) {
  console.log('findLikeFile: ', dirPath, jsonFileName)
  const fileList = fs.readdirSync(path.join(__dirname, dirPath)).filter(file => file.endsWith('.json'))
  const existFileName = getLikeFileName(fileList, jsonFileName)

  // console.log('filelist: ', fileList)
  // console.log('jsonFileName: ', jsonFileName)
  console.log('existFileName: ', existFileName)

  const jsonFilePath = path.join(__dirname, dirPath, existFileName)
  // console.log(`jsonFilePath: `, jsonFilePath, existFileName)
  return jsonFilePath
}

function getLikeFileName (list, jsonFileName) {
  // 1. 리스트 정렬
  list.sort()

  // console.log('list: ', list)

  // 2. 리스트에 jsonFileName 존재 여부 확인
  let index = list.indexOf(jsonFileName)
  if (index !== -1) {
    return list[index]
  }

  // 3. 리스트에 jsonFileName 삽입
  list.push(jsonFileName)
  list.sort()

  // 4. 삽입된 jsonFileName의 인덱스 찾기 및 제거
  index = list.indexOf(jsonFileName)
  list.splice(index, 1)

  let existFileName
  if (index < 0) {
    existFileName = list[0]
  } else if (index >= list.length) {
    existFileName = list[list.length - 1]
  } else {
    existFileName = list[index]
  }

  // 5. jsonFileName 을 삽입했던 위치(삽입 후 인덱스) 반환
  return existFileName
}

module.exports = {
  responseJson
}
