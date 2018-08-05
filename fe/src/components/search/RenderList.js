import React from 'react'
import uuidv4 from 'uuid'
import { ListGroup, ListGroupItem } from 'react-bootstrap'
import isNil from 'lodash/isNil'

// FIXME: when data will change to actual, update parsing
const RenderList = props => {
  let list = props.data
  if (isNil(list.categories) || isNil(list.items)) {
    return <b>sorry no results</b>
  }
  return (
    <ListGroup id={'render-list'}>
      {list.categories.map(e => (
        <ListGroupItem bsStyle="info" href="" key={uuidv4()}>
          {`${e.name} (${e.hits})`}
          </ListGroupItem>
      ))}
      {list.items.map(e => (
        <ListGroupItem href="" key={uuidv4()}>
          {`${e.name}`}
          </ListGroupItem>
      ))}
    </ListGroup>
  )
}

export default RenderList
