import React from 'react'
import { ListGroup } from 'react-bootstrap'
import isNil from 'lodash/isNil'
import { categoryListItem, regularListItem } from './Item'
import { onClick } from '../../localStorage'

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
      {/* FIXME: need to clear send params */}
      {list.categories.map(e => categoryListItem(e, onClick(e)))}
      {list.items.map(e => regularListItem(e, onClick(e)))}
    </ListGroup>
  )
}

export default RenderList
