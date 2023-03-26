
Cypress.Commands.add('login', (username, password) => {
    cy.visit('/login')
    cy.get('[data-test="userInput"]').type(username)
    cy.get('[data-test="passwordInput"]').type(password)
    cy.get('[data-test="button"]').click()
  })
  