submit = async () => {
  const username = $('username').value;
  const password = $('password').value;
  const { status, data } = await http('http://localhost:4000/user/login', 'POST', { username, password })
  switch (status) {
    case 'success':
      alert(data.msg)
      window.location.href = './index.html'
      break;
    case 'fail':
      alert(data.errMsg + "，请重新输入！")
      reset()
      break;
    default:
      break;
  }
}

reset = () => {
  $('username').value = ''
  $('password').value = ''
}