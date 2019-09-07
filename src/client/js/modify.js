submit = async () => {
  const username = $('username').value;
  const prePassword = $('prePassword').value;
  const newPassword = $('newPassword').value;
  const idCard = $('idCard').value;
  const { status, data } = await http('http://localhost:4000/user/modify', 'POST', { username, prePassword, newPassword, idCard })
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
  $('prePassword').value = ''
  $('newPassword').value = ''
  $('idCard').value = ''
}