import React from 'react'
import uuidv4 from 'uuid'
import { Image, ListGroup, ListGroupItem } from 'react-bootstrap'
import isNil from 'lodash/isNil'
import getImage from '../../api/getImage'

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
        <ListGroupItem bsStyle="info" href="/" key={uuidv4()} onClick={onClick}>
          {`${e.name} (${e.hits})`}
        </ListGroupItem>
      ))}
      {list.items.map(e => (
        <ListGroupItem href="/" key={uuidv4()} onClick={onClick}>
          <Image src={getImage} width={16} height={16} />
          {` ${e.name}`}
        </ListGroupItem>
      ))}
    </ListGroup>
  )
}

const onClick = e => {
  // TODO: add hit counter
  e.preventDefault()
  let storage = []
  const KEY = 'insider4'
  if (JSON.parse(localStorage.getItem(KEY)) !== null) {
    storage = JSON.parse(localStorage.getItem(KEY))
  }
  if (!isNil(e) && !isNil(e.target) && !isNil(e.target.innerText)) {
    storage.push(e.target.innerText)
  }
  localStorage.setItem(KEY, JSON.stringify(storage))
}

export default RenderList
