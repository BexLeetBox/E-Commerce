describe('App', () => {
  beforeEach(() => {
    cy.visit('/')
  })

  it('should render the logo', () => {
    cy.get('#logo').should('be.visible')
  })

  it('should navigate to the home page on click of Home link', () => {
    cy.get('a').contains('Home').click()
    cy.url().should('include', '/')
  })

  it('should navigate to the cart page on click of My cart link when user is logged in', () => {
    // Set isLoggedIn flag in localStorage
    localStorage.setItem('isLoggedIn', true)
    
    cy.get('a').contains('My cart').click()
    cy.url().should('include', '/cart')
  })



  it('should not navigate to the sell page on click of Sell link when user is not logged in', () => {
    // Clear isLoggedIn flag in localStorage
    localStorage.removeItem('isLoggedIn')
    
    cy.get('a').contains('Sell').click()
    cy.url().should('not.include', '/sell')
  })


  it('should not navigate to the my listings page on click of My listings link when user is not logged in', () => {
    // Clear isLoggedIn flag in localStorage
    localStorage.removeItem('isLoggedIn')
    
    cy.get('a').contains('My listings').click()
    cy.url().should('not.include', '/mylisting')
  })

  it('should navigate to the about page on click of About us link', () => {
    cy.get('a').contains('About us').click()
    cy.url().should('include', '/about')
  })

  it('should navigate to the login page on click of Login button when user is not logged in', () => {
    // Clear isLoggedIn flag in localStorage
    localStorage.removeItem('isLoggedIn')
    
    cy.get('#loginButton').click()
    cy.url().should('include', '/login')
  })

})
