async function http(url = '', method = 'GET', body = {}) {
  const requestBody = JSON.stringify(body)
  const haveBody = requestBody !== '{}'
  const requestOptions = {
    method,
    body: requestBody,
    credentials: 'include',
    headers: {
      'content-type': 'application/json'
    },
  }
  /* 原生fetch中GET方法不能携带body */
  if (!haveBody) delete requestOptions.body

  const res = await fetch(url, requestOptions)
  return res.json()
}