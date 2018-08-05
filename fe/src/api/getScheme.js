// TODO: implement getScheme (improvement)
import axios from 'axios'
import currentLocation from './currentLocation'

const url = `${currentLocation}/scheme`
const body = value => ({
  topic: 'temp value',
  logs: JSON.stringify(value)
})

const params = {
  headers: {
    'Content-Type': 'application/json',
    Authorization: ''
  }
}

const getScheme = value => axios.get(url, body(value))

export default getScheme