'use strict';

export function splitModuleId(id) {
  const indexOfQuery = id.lastIndexOf("?");
  let moduleId = id;
  let querySuffix = ""
  if (indexOfQuery !== -1) {
    moduleId = id.substring(0, indexOfQuery)
    querySuffix = id.substring(indexOfQuery)
    // console.log(">>>>>>>" + moduleId)
    // console.log(">>>" + querySuffix)
  }
  return {
    moduleId, // anything before the last "?", or the whole module id if there is no query
    querySuffix // everything after and including "?", or an empty string if there is no query
  }
}

