import {KEY} from "../constants";
import isNil from "lodash/isNil";

export const clearLocalStorage = () => {
  localStorage.removeItem(KEY)
}
