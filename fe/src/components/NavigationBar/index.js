import React from 'react'
import { Nav, Navbar, NavItem } from 'react-bootstrap'

const NavigationBar = () => {
  return (
    <Navbar inverse collapseOnSelect>
      <Navbar.Header>
        <Navbar.Brand>
          <a href="/about">ZBS</a>
        </Navbar.Brand>
        <Navbar.Toggle />
      </Navbar.Header>
      <Nav>
        <NavItem eventKey={1} href="/">
          Intro
        </NavItem>
      </Nav>
    </Navbar>
  )
}

export default NavigationBar
