import axios from 'axios'
import currentLocation from './currentLocation'
import { SEARCH_TIMEOUT } from '../constants'

const url = value => `${currentLocation}search?pattern=${value}`

const searchRequest = value =>
  axios.get(url(value), { timeout: SEARCH_TIMEOUT })

export default searchRequest
