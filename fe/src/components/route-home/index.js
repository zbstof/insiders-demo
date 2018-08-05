import React from 'react'
import Search from '../search'
import { Col, PageHeader, Row } from 'react-bootstrap'
import { bindActionCreators } from 'redux'
import { searchAsync } from '../../redux/actions'
import { connect } from 'react-redux'

const SweetHome = incomeProps => {
  return (
    <React.Fragment>
      <Row>
        <Col>
          {/*<PageHeader>Make Search</PageHeader>*/}
          <Search
            searchAsynс={incomeProps.searchAsync}
            searchResult={incomeProps.searchResults}
          />
        </Col>
      </Row>
    </React.Fragment>
  )
}

const mapStateToProps = ({ search }) => {
  return {
    searchResults: search.searchInput
  }
}

const mapDispatchToProps = dispatch =>
  bindActionCreators(
    {
      searchAsync
    },
    dispatch
  )

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(SweetHome)
