import searchRequest from '../../api/searchRequest'
import searchMockData from '../../utils/searchMockData'
import { SEARCH_REQUESTED, SEARCH_RESPONSE, SEARCH_IDLE } from '../../constants'

export const searchAsync = value => {
  if (value.length < 2) {
    return dispatch => {
      dispatch({ type: SEARCH_IDLE })
    }
  }

  return dispatch => {
    dispatch({ type: SEARCH_REQUESTED })

    return searchRequest(value)
      .then(resp => {
        return dispatch({ type: SEARCH_RESPONSE, payload: resp })
      })
      .catch(err => {
        return dispatch({ type: SEARCH_RESPONSE, payload: searchMockData })
      })
  }
}
