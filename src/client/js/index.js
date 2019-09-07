let loginInfo;

window.onload = async () => {
  const { data } = await fetchLoginList()
  loginInfo = data;
  setLoginInfo(1)
  setPaginator()
}

fetchLoginList = async () => {
  return await http('http://localhost:4000/user/login_list', 'GET')
}

setLoginInfo = (page) => {
  const infoContainer = $('login-info-container');
  /* 清空之前记录节点 */
  while (infoContainer.hasChildNodes()) {
    infoContainer.removeChild(infoContainer.firstChild)
  }

  const start = (page - 1) * 5;
  const end = page * 5;

  loginInfo.slice(start, end).map((item) => {
    var infoNode = document.createElement('div');
    const { id, userId, loginTime } = item;
    const loginTimeString = new Date(parseInt(loginTime)).toLocaleString();
    infoNode.setAttribute('class', 'login-info-wrap')
    infoNode.innerHTML = `
      <div class="login-info-item">
        ${id}
      </div>
      <div class="login-info-item">
      ${userId}
      </div>
      <div class="login-info-item">
      ${loginTimeString}
      </div>
    `
    infoContainer.appendChild(infoNode);
  });
}

setPaginator = () => {
  const length = Math.ceil(loginInfo.length / 5)
  var paginatorContainer = $('paginator-container');
  Array.from({ length: length }).map((_, index) => {
    var pageNode = document.createElement('div');
    pageNode.setAttribute('class', 'paginator-wrap')
    pageNode.setAttribute('onclick', `handlePaginatorClick(${index})`)
    pageNode.innerHTML = `
        ${index + 1}
    `
    paginatorContainer.appendChild(pageNode);
  })
}

handlePaginatorClick = (page) => {
  setLoginInfo(page + 1)
}