import React from 'react'
import {
  ControlLabel,
  FormControl,
  FormGroup, Glyphicon,
  HelpBlock, InputGroup
} from 'react-bootstrap'
import RenderList from '../DataRenderers/RenderList'

class Search extends React.Component {
  constructor(params) {
    super(params)
    this.handleChange = this.handleChange.bind(this)
    this.state = {
      value: ''
      // FIXME: remove this and add logic statement
    }
  }

  handleChange(e) {
    this.setState({ value: e.target.value })
    this.props.searchAsynс(e.target.value)
  }

  render() {
    return (
      <div>
        <FormGroup controlId="formBasicText">
          <ControlLabel>Search bar</ControlLabel>
          <InputGroup>
            <FormControl type="text"
                         value={this.state.value}
                         placeholder="Enter text"
                         onChange={this.handleChange} />
            <InputGroup.Addon>
              <Glyphicon glyph="search" />
            </InputGroup.Addon>
          </InputGroup>
          <FormControl.Feedback />
          <HelpBlock>
            Enter search to see, how suggestions will change
          </HelpBlock>
        </FormGroup>
        <RenderList data={this.props.searchResult} />
      </div>
    )
  }
}

export default Search
