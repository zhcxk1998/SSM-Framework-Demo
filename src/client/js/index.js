let loginInfo;

window.onload = async () => {
  const { data } = await fetchLoginList()
  if (data.errCode === 401) {
    alert(data.errMsg)
    window.location.href = './login.html'
    return;
  }
  
  loginInfo = data;
  setLoginInfo(1)
  setPaginator()
  handlePaginatorClick(0)
}

fetchLoginList = async () => {
  return await http('http://localhost:4000/index/login_list', 'GET')
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
    const infoNode = document.createElement('div');
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
  const paginatorContainer = $('paginator-container');
  Array.from({ length: length }).map((_, index) => {
    const pageNode = document.createElement('div');
    pageNode.setAttribute('class', 'paginator-wrap')
    pageNode.setAttribute('onclick', `handlePaginatorClick(${index})`)
    pageNode.innerHTML = `
        ${index + 1}
    `
    paginatorContainer.appendChild(pageNode);
  })
}

handlePaginatorClick = (page) => {
  const pageList = $('paginator-container').children;
  const currentPage = pageList[page];
  /* 将所有分页样式还原 */
  Array.from(pageList).map(page => {
    page.style.background = '#fff'
    page.style.color = '#333'
    page.style.border = '#333 2px solid'
  })
  currentPage.style.background = '#333'
  currentPage.style.color = '#fff'
  currentPage.style.border = '#fff 2px solid'

  setLoginInfo(page + 1)
}