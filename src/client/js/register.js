submit = async () => {
  const username = $('username').value;
  const password = $('password').value;
  const idCard = $('idCard').value;

  const { status, data } = await http('http://localhost:4000/user/register', 'POST', { username, password, idCard })
  switch (status) {
    case 'success':
      alert(data.msg)
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
  $('idCard').value = ''
}