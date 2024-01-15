import CryptoJS from 'crypto-js'

// 상태 암호화
export function encryptState(state, secretKey) {
  const encryptedState = CryptoJS.AES.encrypt(JSON.stringify(state), secretKey).toString()
  return encryptedState
}

// 상태 복호화
export function decryptState(encryptedState, secretKey) {
  if (!encryptedState) return null
  const bytes = CryptoJS.AES.decrypt(encryptedState, secretKey)
  const decryptedState = bytes.toString(CryptoJS.enc.Utf8)
  try {
    return JSON.parse(decryptedState)
  } catch (e) {
    // 만약 JSON 파싱이 실패하면 복호화된 문자열 그대로 반환
    return decryptedState
  }
}

  // TEST
  // const enc = encryptState({ a: 1 })
  // const dec = decryptState(enc)
