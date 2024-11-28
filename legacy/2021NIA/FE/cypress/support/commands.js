// ***********************************************
// This example commands.js shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
const tokenPath = 'cypress/fixtures/ipms/token.json'
Cypress.Commands.add('visitLocal', () => {
    cy.visit('http://localhost:4000')
    cy.viewport(1800, 900)
    cy.setToken()
    cy.visit('/')
})
Cypress.Commands.add('login', () => { // 코드 전부 지우고 제거
    cy.get(':nth-child(1) > .el-form-item__content > input').type('10150810');
    cy.get(':nth-child(2) > .el-form-item__content > input').type('1');
    cy.get('button').click();
})
Cypress.Commands.add('visitPath', (path) => {
    cy.visitLocal()
    cy.wait(200)
    cy.visit(`/#/${path}`)
    cy.wait(200)
})

Cypress.Commands.add('saveToken', () => {
    cy.window().then((window) => {
        const authToken = window.localStorage.getItem('X-AUTH-TOKEN')
        const authInfo = window.localStorage.getItem('X-AUTH-INFO')
        const data = {
            authToken: authToken,
            authInfo: authInfo,
        }
        cy.task('saveDataToFile', { filePath: 'cypress/fixtures/ipms/token.json', data }, { log: false })
            .then((message) => {
                // cy.log(message) // Log success message
            })
    })
})
Cypress.Commands.add('setToken', () => {
    cy.window().then((window) => {
        cy.task('loadDataFromFile', tokenPath).then((data) => {
            window.localStorage.setItem('X-AUTH-TOKEN', data.authToken)
            window.localStorage.setItem('X-AUTH-INFO', data.authInfo)
        })
    })
})
Cypress.Commands.add('setOptions', () => {
    cy.window().then((window) => {
        cy.task('loadDataFromFile', 'cypress/fixtures/ipms/options.json').then((data) => {
            window.localStorage.setItem('options', data.options)
        })
    })
})
//
// -- This is a parent command --
// Cypress.Commands.add('login', (email, password) => { ... })
//
//
// -- This is a child command --
// Cypress.Commands.add('drag', { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add('dismiss', { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This will overwrite an existing command --
// Cypress.Commands.overwrite('visit', (originalFn, url, options) => { ... })