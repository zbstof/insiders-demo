import React from 'react'
import { Nav, Navbar, NavItem } from 'react-bootstrap'
import { clearLocalStorage } from '../../localStorage'

const NavigationBar = () => {
  return (
    <Navbar inverse collapseOnSelect>
      <Navbar.Header>
        <Navbar.Brand>
          <a href="/about">ZBS</a>
        </Navbar.Brand>
      </Navbar.Header>
      <Nav>
        <NavItem eventKey={1} href="/">
          Intro
        </NavItem>
        <NavItem eventKey={2} onClick={clearLocalStorage}>
          clear storage
        </NavItem>
      </Nav>
    </Navbar>
  )
}

export default NavigationBar
