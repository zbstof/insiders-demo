import { SEARCH_IDLE, SEARCH_REQUESTED, SEARCH_RESPONSE } from '../../constants'
import searchInitState from '../../utils/searchInitState'

const initialState = {
  searchInput: searchInitState,
  isLoading: false
}

export default (state = initialState, action) => {
  switch (action.type) {
    case SEARCH_REQUESTED:
      return {
        ...state,
        isLoading: true
      }
    case SEARCH_IDLE:
      return {
        ...state,
        isLoading: false
      }
    case SEARCH_RESPONSE:
      return {
        ...state,
        searchInput: action.payload,
        isLoading: false
      }
    default:
      return state
  }
}
