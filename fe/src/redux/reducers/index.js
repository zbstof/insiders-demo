import { combineReducers } from 'redux'
import counter from './counterReducer'
import search from './searchReducer'

export default combineReducers({
  counter, search
})
