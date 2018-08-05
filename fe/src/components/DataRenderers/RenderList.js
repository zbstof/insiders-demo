import React from 'react'
import { ListGroup } from 'react-bootstrap'
import isNil from 'lodash/isNil'
import { categoryListItem, regularListItem } from './Item'
import { KEY } from '../../constants'

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
      {list.categories.map(e => categoryListItem(e, onClick))}
      {list.items.map(e => regularListItem(e, onClick))}
    </ListGroup>
  )
}

const onClick = e => {
  e.preventDefault()
  let storage = []
  let inner = e.target.innerText
  if (JSON.parse(localStorage.getItem(KEY)) !== null) {
    storage = JSON.parse(localStorage.getItem(KEY))
  }
  if (!isNil(e) && !isNil(e.target) && !isNil(inner)) {
    let trigger = false
    storage.map(e => {
      if (e.name === inner) {
        e.click++
        trigger = true
      }
    })
    if (storage.length === 0 || !trigger) {
      storage.push({ name: inner, click: 1, views: 0 })
    }
  }
  localStorage.setItem(KEY, JSON.stringify(storage))
}

export default RenderList
