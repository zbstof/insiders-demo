import React from 'react'
import uuidv4 from 'uuid'
import { Badge, ListGroup, ListGroupItem } from 'react-bootstrap'

// FIXME: when data will change to actual, update parsing
const RenderList = props => {
  let list = props.data
    return (
      <ListGroup>
        {list.values.map(e => (
          <ListGroupItem href="" key={uuidv4()}>
            <Badge>{'category'}</Badge>  {e.id} | {e.val}
          </ListGroupItem>
        ))}
      </ListGroup>
    )
}

export default RenderList
