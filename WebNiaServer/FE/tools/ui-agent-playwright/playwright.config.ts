import { defineConfig, devices } from '@playwright/test'

// Vue 앱의 기본 포트는 4000 (vue.config.js 참고)
const UI_BASE_URL = process.env.UI_BASE_URL || 'http://localhost:4000'

export default defineConfig({
  testDir: './tests',
  fullyParallel: true,
  forbidOnly: !!process.env.CI,
  retries: process.env.CI ? 2 : 0,
  workers: process.env.CI ? 1 : undefined,
  reporter: 'html',
  use: {
    baseURL: UI_BASE_URL,
    trace: 'on-first-retry',
    screenshot: 'only-on-failure',
  },
  projects: [
    {
      name: 'chromium',
      use: { ...devices['Desktop Chrome'] },
    },
  ],
  // webServer 설정 제거: 서버는 수동으로 실행 (npm run dev 또는 npm run dev:all)
  // Playwright는 이미 실행 중인 서버에 연결합니다
  // webServer: {
  //   command: 'npm run dev',
  //   url: UI_BASE_URL,
  //   reuseExistingServer: true,
  //   timeout: 120000,
  // },
})
