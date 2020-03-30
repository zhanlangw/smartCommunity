// use localStorage to store the authority info, which might be sent from server in actual project.
export function getAuthority() {
  // return localStorage.getItem('antd-pro-authority') || ['admin', 'user'];
  // preview.pro.ant.design only do not use in your production.
  // preview.pro.ant.design 专用环境变量，请不要在你的项目中使用它。

  return JSON.parse(localStorage.getItem('manager_authority'));
}
export function setAuthority(authority) {
  // const proAuthority = typeof authority === 'string' ? [authority] : authority;
  return localStorage.setItem('manager_authority', authority);
}
