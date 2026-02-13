import { test, expect } from '@playwright/test'
import * as path from 'path'
import * as fs from 'fs'

// Vue 앱의 기본 포트는 4000 (vue.config.js 참고)
const UI_BASE_URL = process.env.UI_BASE_URL || 'http://localhost:4000'
const TEST_ARTIFACTS_DIR = path.join(__dirname, '../test-artifacts')

// 로그인 정보
const LOGIN_USERNAME = process.env.LOGIN_USERNAME || 'dimmby'
const LOGIN_PASSWORD = process.env.LOGIN_PASSWORD || 'dimmby'

// test-artifacts 디렉토리 생성
if (!fs.existsSync(TEST_ARTIFACTS_DIR)) {
  fs.mkdirSync(TEST_ARTIFACTS_DIR, { recursive: true })
}

/**
 * 로그인 처리 헬퍼 함수
 */
async function login(page: any) {
  // 로그인 페이지인지 확인
  const isLoginPage = await page.locator('input[name="username"]').isVisible().catch(() => false)

  if (isLoginPage) {
    console.log('[Login] 로그인 페이지 감지, 로그인 진행...')

    // 사용자명 입력
    await page.fill('input[name="username"]', LOGIN_USERNAME)
    await page.waitForTimeout(300)

    // 비밀번호 입력
    await page.fill('input[name="password"]', LOGIN_PASSWORD)
    await page.waitForTimeout(300)

    // 로그인 버튼 클릭
    await page.click('button:has-text("Login")')

    // 로그인 완료 대기 (URL이 /login이 아닌 페이지로 변경되거나, 특정 요소가 나타날 때까지)
    try {
      // 방법 1: URL 변경 대기 (로그인 성공 시 리다이렉트)
      await page.waitForURL((url) => !url.pathname.includes('/login'), { timeout: 10000 })
      console.log('[Login] 로그인 성공 - 메인 페이지로 이동')
    } catch (error) {
      // 방법 2: 로그인 폼이 사라질 때까지 대기
      await page.waitForSelector('input[name="username"]', { state: 'hidden', timeout: 10000 }).catch(() => {
        console.log('[Login] 로그인 폼이 사라지지 않음, 계속 진행...')
      })
    }

    // 페이지 로드 대기
    await page.waitForLoadState('networkidle')
    await page.waitForTimeout(1000) // 추가 안정화 대기
  } else {
    console.log('[Login] 로그인 페이지가 아님, 계속 진행...')
  }
}

test.describe('UI Evidence Collection Test', () => {
  test('should collect evidence from Vue app', async ({ page }) => {
    // 세션 ID 생성
    const sessionId = `test_${Date.now()}`

    // Vue 앱 접속 (세션 ID를 쿼리 파라미터로 전달)
    await page.goto(`${UI_BASE_URL}/?ag_session=${sessionId}`)

    // 페이지 로드 대기
    await page.waitForLoadState('networkidle')

    // 로그인 처리
    await login(page)

    // 로그인 후 스크린샷 저장
    const afterLoginScreenshotPath = path.join(TEST_ARTIFACTS_DIR, `${sessionId}_after_login.png`)
    await page.screenshot({ path: afterLoginScreenshotPath, fullPage: true })
    console.log(`Screenshot saved: ${afterLoginScreenshotPath}`)

    // window.__emitEvidence가 있는지 확인하고 호출
    const emitEvidenceExists = await page.evaluate(() => {
      return typeof window.__emitEvidence === 'function'
    })

    if (emitEvidenceExists) {
      // after_first_render 이벤트 전송
      await page.evaluate((stepName) => {
        if (window.__emitEvidence) {
          window.__emitEvidence(stepName)
        }
      }, 'after_first_render')

      // ACK 대기 (선택사항)
      await page.waitForTimeout(500)
    }

    // 스크린샷 저장
    const screenshotPath = path.join(TEST_ARTIFACTS_DIR, `${sessionId}_initial.png`)
    await page.screenshot({ path: screenshotPath, fullPage: true })
    console.log(`Screenshot saved: ${screenshotPath}`)

    // 추가 테스트 단계 예시
    // 예: 특정 버튼 클릭 후 증거 수집
    try {
      // 예시: 특정 요소가 있는지 확인
      const bodyVisible = await page.locator('body').isVisible()
      expect(bodyVisible).toBeTruthy()

      // 추가 증거 수집 (예시)
      if (emitEvidenceExists) {
        await page.evaluate((stepName) => {
          if (window.__emitEvidence) {
            window.__emitEvidence(stepName)
          }
        }, 'after_body_check')

        await page.waitForTimeout(500)
      }

      // 최종 스크린샷
      const finalScreenshotPath = path.join(TEST_ARTIFACTS_DIR, `${sessionId}_final.png`)
      await page.screenshot({ path: finalScreenshotPath, fullPage: true })
      console.log(`Final screenshot saved: ${finalScreenshotPath}`)
    } catch (error) {
      console.error('Test error:', error)
      // 에러 발생 시에도 스크린샷 저장
      const errorScreenshotPath = path.join(TEST_ARTIFACTS_DIR, `${sessionId}_error.png`)
      await page.screenshot({ path: errorScreenshotPath, fullPage: true })
      throw error
    }
  })

  test('should handle custom session ID', async ({ page }) => {
    const customSessionId = `custom_${Date.now()}`
    await page.goto(`${UI_BASE_URL}/?ag_session=${customSessionId}`)
    await page.waitForLoadState('networkidle')

    // 로그인 처리
    await login(page)

    // 세션 ID가 올바르게 설정되었는지 확인
    const url = page.url()
    expect(url).toContain(`ag_session=${customSessionId}`)

    // 스크린샷 저장
    const screenshotPath = path.join(TEST_ARTIFACTS_DIR, `${customSessionId}_custom.png`)
    await page.screenshot({ path: screenshotPath, fullPage: true })
  })
})
