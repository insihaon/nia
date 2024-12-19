function toSimpleString(arr, regex) {
  // 1. 정규 표현식 객체 생성
  const re = new RegExp(regex)

  // 2. 배열 필터링
  // const filteredArr = arr.filter(element => {
  // 	// 3. 각 요소를 정규 표현식으로 테스트
  // 	return re.test(element);
  // });

  const filteredArr = arr

  // 4. 필터링된 배열의 각 요소에서 매칭된 부분만 추출
  const matchingElements = filteredArr.map((element) => {
    // 4.1. exec() 메서드를 사용하여 매칭 결과 객체 얻기
    const match = re.exec(element)

    // 4.2. 매칭 결과 객체에서 그룹 값 추출
    return match?.at(0) || ''
  })

  // 5. 추출된 값들을 배열로 반환
  return matchingElements.join('|')
}
window.toSimpleString = toSimpleString

/**
 * todo 나중에 변경하도록 하자 2024.08.06
 * 현재방식 : Object values 추출 > toSimpleString > base64 Encoding
 * 문제점   : input 일부를 바꾸면 output 이 일부가 아닌 전체 변경된다.
 *            이로인해 입력파라미터를 기준으로 비슷한 파일을 찾아도 입력과 무관한 데이터인 경우가 있다
 * 변경방식 : Object 중 한글,영문,숫자에 해당하는 key n글자 value n 글자를 정규표현식 추출 (convertTextV2)
 */

function convertTextV2(obj, maxKeyLength = 6, maxValueLength = 10) {
  // 정규 표현식
  const regex = /^[가-힣a-zA-Z0-9]$/

  let result = ''

  // 객체의 모든 key-value 쌍 순회
  for (const key in obj) {
    // eslint-disable-next-line no-prototype-builtins
    if (obj.hasOwnProperty(key)) {
      // key와 value 각각 검사하여 조건에 맞는 문자만 추출
      const extractedKey = key
        .split('')
        .filter((char) => regex.test(char))
        .slice(0, maxKeyLength)
        .join('')
      const extractedValue = (obj[key] || '')
        .toString()
        .split('')
        .filter((char) => regex.test(char))
        .slice(0, maxValueLength)
        .join('')

      // 추출된 문자열 연결
      result += extractedKey + extractedValue
    }
  }

  return result
}
window.convertTextV2 = convertTextV2

// // 예시 객체
// console.log(convertTextV2({
//   'name': '홍길동',
//   'age': 30,
//   'address': ['서울시', '부산시', '광주시'],
//   'abc123456789': 'def4567890',
//   '123abc': '456def'
// }))
// 'name홍길동age30addre서울시부산시광abc12def4567123ab456def'

function convertTextV1(data) {
  let json = data
  if (typeof data === 'object') {
    // console.log(JSON.stringify(data))

    const values = Object.values(data)
    const regex = /^(?:\S{1,10})/
    json = toSimpleString(values, regex)
  }
  const bytes = new TextEncoder().encode(json)
  const base64 = btoa(String.fromCharCode(...bytes))
  const urlSafe = base64.replace(/=/g, '').replace(/\+/g, '-').replace(/\//g, '_')
  return urlSafe
}
window.convertTextV1 = convertTextV1

function filterNonNullValues(obj) {
  return Object.fromEntries(Object.entries(obj).filter(([key, value]) => value !== undefined && value !== null && value !== ''))
}

function getJsonfileName2(url, config) {
  let param = Object.assign({}, typeof config.data === 'string' ? JSON.parse(config.data) : config.data)

  delete param.encrypt
  delete param._t

  param = filterNonNullValues(param)

  const convertText = convertTextV2
  const param_encoding = convertText(JSON.parse(JSON.stringify(param)))
  const param_encoding_min = param_encoding.replace(/[^0-9]/g, '')

  let url_encoding = url.replace(/^https?:\/\/[^/]+/, '')
  url_encoding = url_encoding.replace(/^(\/selectList|\/selectOne|\/modify|\/)/, '')
  url_encoding = [...url_encoding].filter(char => /^[가-힣a-zA-Z0-9]$/.test(char)).join('')
  const filename = `${url_encoding}_${param_encoding}`.slice(0, 200) + '.json'

  // const filename_v1 = `${convertTextV1(url_encoding)}_${param_encoding}`.substring(0, 200) + '.json'
  // console.log(`filename_v1=${filename_v1}`)

  // console.log(
  // 	`getJsonfileName2({url:'${url}', param: JSON.parse('${JSON.stringify( param )}')})\n`,
  // 	`=> out filename=${filename}`
  //   );

  return filename
}
window.getJsonfileName2 = getJsonfileName2

export { getJsonfileName2 }
