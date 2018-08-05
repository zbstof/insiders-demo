import React from 'react'
import uuidv4 from 'uuid'
import { Image, ListGroup, ListGroupItem } from 'react-bootstrap'
import isNil from 'lodash/isNil'

// FIXME: when data will change to actual, update parsing
const RenderList = props => {
  let list = props.data
  if (isNil(list.categories) || isNil(list.items)) {
    return <p />
  }
  if (list.categories.length < 1 && list.items.length < 1)
    return (
      <p>
        Your search has no results. Probably, we will make this page looks
        better
      </p>
    )

  if (list.items.length < 1) return <p>Looks like, there is no items</p>

  return (
    <ListGroup id={'render-list'}>
      {list.categories.map(e => (
        <ListGroupItem bsStyle="info" href="" key={uuidv4()}>
          {`${e.name} (${e.hits})`}
        </ListGroupItem>
      ))}
      {list.items.map(e => (
        <ListGroupItem href="" key={uuidv4()}>
          <Image
            src={'http://retrogamer.biz/wp-content/uploads/2015/09/navody.png'}
            width={16}
            height={16}
          />
          {` ${e.name}`}
        </ListGroupItem>
      ))}
    </ListGroup>
  )
}

export default RenderList
