const { defineConfig } = require('cypress')

module.exports = defineConfig({
  component: {
    devServer: {
      framework: 'vue-cli',
      bundler: 'webpack',
    },
  },
  e2e: {
    baseUrl: 'http://localhost:4000',
    // 자동 테스트 코드 생성 설정
    experimentalStudio: true,
    failOnStatusCode: true,
    // 크롬 동일 출처 정책 보안 비활성화
    chromeWebSecurity: false,
    setupNodeEvents(on, config) {
      // implement node event listeners here
    },
  },
});
