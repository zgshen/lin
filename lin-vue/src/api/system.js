import request from '@/utils/request'
import qs from 'qs'
/*用户管理*/


// 获取登录签名
export function getSign(params) {
  return request({
    url: '/getSign',
    method: 'post',
    params
  })
}
export function captcha(){
  return request({
    url:'/captcha',
    method:'POST'
  })
}
/*用户列表*/
export function getUserList(params) {
  return request({
    url: '/sys/user/list',
    method: 'get',
    params
  })
}

/*查询用户*/
export function getUserInfo(data) {
  return request({
    url: '/sys/user/queryUser',
    method: 'post',
    data: qs.stringify(data),
  })
}


/*新增用户*/
export function addNewUser(params, data) {
  let str = '?'
  if (params.roleIds.length > 0) {
    params.roleIds.forEach(v => {
      str += 'roleIds=' + v + '&'
    })
    str = str.substring(0, str.length - 1)
  } else {
    str += 'roleIds='
  }
  return request({
    url: '/sys/user/save' + str,
    method: 'post',
    data: qs.stringify(data)
  })
}

/*修改用户信息  */
export function updateUser(params, data) {
  let str = '?'
  if (params.roleIds.length > 0) {
    params.roleIds.forEach(v => {
      str += 'roleIds=' + v + '&'
    })
    str = str.substring(0, str.length - 1)
  } else {
    str += 'roleIds='
  }
  return request({
    url: '/sys/user/update' + str,
    method: 'post',
    data: qs.stringify(data)
  })
}

/*用户修改用户密码*/
export function updatePwd_user(params, data) {
  return request({
    url: '/sys/user/resetPwd',
    method: 'post',
    params,
    data
  })
}
/*无登录修改密码*/
export function updatePwd_noAuth(params, data) {
  return request({
    url: '/sys/user/resetPwdUnAuth',
    method: 'post',
    params,
    data
  })
}
/*管理员修改密码*/
export function updatePwd_admin(params, data) {
  return request({
    url: '/sys/user/adminResetPwd',
    method: 'post',
    params,
    data
  })
}

/*删除多个用户*/
export function deleteUsers(params) {
  return request({
    transformRequest: [function (data) {
      data = qs.stringify(data)
      return data.replace(/\%5B(.+?)\%5D/g,'%5B%5D')
    }],
    url: '/sys/user/batchRemove',
    method: 'post',
    data: params
  })
}


/*菜单管理*/

/*菜单列表*/
export function getMenuList(params) {
  return request({
    url: '/sys/menu/list',
    method: 'get',
    params
  })
}

/*菜单树*/
export function getMenuTree() {
  return request({
    url: '/sys/menu/tree',
    method: 'get',
  })
}

/*添加菜单*/
export function addMenu(data) {
  return request({
    url: '/sys/menu/save',
    method: 'post',
    data: qs.stringify(data)
  })
}

/*编辑菜单*/
export function editMenu(data) {
  return request({
    url: '/sys/menu/update',
    method: 'post',
    data: qs.stringify(data)
  })
}

/*菜单详细*/
export function getMenuDetail(params) {
  return request({
    url: '/sys/menu/getMenu',
    method: 'get',
    params
  })
}

/*删除菜单*/
export function deleteMenu(params) {
  return request({
    url: '/sys/menu/remove',
    method: 'post',
    params
  })
}


/*部门*/

// 部门列表
export function departmentList(params) {
  return request({
    url: '/system/sysDept/deptTree',
    method: 'get',
    params
  })
}

// 部门添加
export function departmentSave(params) {
  return request({
    url: '/system/sysDept/save',
    method: 'post',
    params
  })
}

// 部门修改
export function departmentUpdate(params) {
  return request({
    url: '/system/sysDept/update',
    method: 'post',
    params
  })
}

// 部门删除
export function departmentRemove(params) {
  return request({
    url: '/system/sysDept/remove',
    method: 'post',
    params
  })
}

/*获取部门树结构*/
export function getUserTree() {
  return request({
    url: '/system/sysDept/deptTree',
    method: 'get'
  })
}

/*查询部门父节点 */
export function getgetParent(params) {
  return request({
    url: '/system/sysDept/getParent',
    method: 'get',
    params
  })
}


/*角色管理*/

/*角色列表*/
export function getRoleList(params) {
  return request({
    url: '/sys/role/list',
    method: 'get',
    params
  })
}

/*获取角色信息*/

export function getRoleInfo(data) {
  return request({
    url: '/sys/role/editRole',
    method: 'post',
    data: qs.stringify(data)
  })
}

/*添加角色*/
export function addRole(params, data) {
  let str = '?'
  if (params.length > 0) {
    params.forEach(v => {
      str += 'menuIds=' + v + '&'
    })
    str = str.substring(0, str.length - 1)
  } else {
    str += 'menuIds='
  }
  return request({
    url: '/sys/role/save' + str,
    method: 'post',
    data: data
  })
}

/*修改角色*/
export function editRole(params, data) {
  let str = '?'
  if (params.length > 0) {
    params.forEach(v => {
      str += 'menuIds=' + v + '&'
    })
    str = str.substring(0, str.length - 1)
  } else {
    str += 'menuIds='
  }
  return request({
    url: '/sys/role/update' + str,
    method: 'post',
    data: data
  })
}

/*删除多个角色*/
export function deleteRole(params) {
  return request({
    transformRequest: [function (data) {
      data = qs.stringify(data)
      return data.replace(/\%5B(.+?)\%5D/g,'%5B%5D')
    }],
    url: '/sys/role/batchRemove',
    method: 'post',
    data:params,
  })
}

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~个人设置接口~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// 更新头像
export function getUploadImg(params) {
  let formData = new FormData()
  formData.append('avatar_file', params.avatar_file)
  return request({
    url: '/sys/user/uploadImg',
    method: 'post',
    data: formData,
    // headers: {
    //     'Content-Type': 'x-www-form-urlencoded',
    // },
  })
}

// 保存用户信息
export function getUpdatePeronal(params) {
  return request({
    url: '/sys/user/updatePersonal',
    method: 'post',
    params,
  })
}

// 用户修改密码
export function resetUserPwd(params) {
  return request({
    url: '/sys/user/resetPwd',
    method: 'post',
    params,
  })
}


// 用户修改密码
export function queryCurrentUser(params) {
  return request({
    url: '/sys/user/queryCurrentUser',
    method: 'get',
    params,
  })
}

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~密码策略设置接口~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// 查询当前系统密码策略
export function getPasswordStrategy(params) {
  return request({
    url: '/system/passwordStrategy/getPasswordStrategy',
    method: 'post',
    params,
  })
}

// 查询当前系统密码策略
export function passwordStrategySave(params) {
  return request({
    url: '/system/passwordStrategy/save',
    method: 'post',
    params,
  })
}

