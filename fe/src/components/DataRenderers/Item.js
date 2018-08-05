import { Image, ListGroupItem } from 'react-bootstrap'
import uuidv4 from 'uuid'
import getImage from '../../api/getImage'
import React from 'react'

export const regularListItem = (event, clickHandler) => (
  <ListGroupItem href="/" key={uuidv4()} onClick={clickHandler}>
    <Image src={getImage} width={16} height={16} />
    {` ${event.name}`}
  </ListGroupItem>
)

export const categoryListItem = (event, clickHandler) => (
  <ListGroupItem bsStyle="info" href="/" key={uuidv4()} onClick={clickHandler}>
    {`${event.name} (${event.hits})`}
  </ListGroupItem>
)
