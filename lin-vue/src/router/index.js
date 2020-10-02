import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: { title: 'Dashboard', icon: 'dashboard' }
    }]
  },

  // 系统管理
  {
    path: '/system',
    component: Layout,
    redirect: '/system/userIndex',
    alwaysShow: true, // will always show the root menu
    name: 'system',
    meta: {
      title: '系统管理',
      icon: 'lock',
      roles: ['admin', 'editor'] // you can set roles in root nav
    },
    children: [
      {
        path: 'userIndex',
        component: () => import('../views/system/userIndex/component/mainCon/index.vue'),
        name: 'userIndex',
        meta: {
          title: '用户管理',
          roles: ['admin', 'editor'] // or you can only set roles in sub nav
        }
      },
      {
        path: 'passwordManagement',
        component: () => import('../views/system/passwordManagement'),
        name: 'passwordManagement',
        meta: {
          title: '密码管理',
          roles: ['admin', 'editor'] // or you can only set roles in sub nav
        }
      },
      {
        path: 'role',
        component: () => import('../views/system/role'),
        name: 'role',
        meta: {
          title: '角色管理',
          roles: ['admin', 'editor'] // or you can only set roles in sub nav
        }
      },
      {
        path: 'systemMenu',
        component: () => import('../views/system/systemMenu'),
        name: 'systemMenu',
        meta: {
          title: '系统菜单',
          roles: ['admin', 'editor'] // or you can only set roles in sub nav
        }
      },
      {
        path: 'department',
        component: () => import('../views/system/department'),
        name: 'department',
        meta: {
          title: '部门管理',
          roles: ['admin', 'editor'] // or you can only set roles in sub nav
        }
      }
    ]
  },

  // 系统工具
  {
    path: '/tool',
    component: Layout,
    redirect: '/tool/plan',
    alwaysShow: true, // will always show the root menu
    name: 'tool',
    meta: {
      title: '系统工具',
      icon: 'lock',
      roles: ['admin', 'editor'] // you can set roles in root nav
    },
    children: [
      {
        path: 'plan',
        component: () => import('../views/tool/plan'),
        name: 'plan',
        meta: {
          title: '计划任务',
          roles: ['admin', 'editor'] // or you can only set roles in sub nav
        }
      },
    ]
  },
  // 系统监控
  {
    path: '/monitoring',
    component: Layout,
    redirect: '/monitoring/notes',
    alwaysShow: true, // will always show the root menu
    name: 'monitoring',
    meta: {
      title: '系统监控',
      icon: 'lock',
      roles: ['admin', 'editor'] // you can set roles in root nav
    },
    children: [
      {
        path: 'notes',
        component: () => import('../views/monitoring/notes'),
        name: 'notes',
        meta: {
          title: '在线用户',
          roles: ['admin', 'editor'] // or you can only set roles in sub nav
        }
      },
      {
        path: 'online',
        component: () => import('../views/monitoring/online'),
        name: 'online',
        meta: {
          title: '系统日志',
          roles: ['admin', 'editor'] // or you can only set roles in sub nav
        }
      },
      {
        path: 'runMonitoring',
        component: () => import('../views/monitoring/runMonitoring'),
        name: 'runMonitoring',
        meta: {
          title: '运行监控',
          roles: ['admin', 'editor'] // or you can only set roles in sub nav
        }
      },
    ]
  },
  // 基础管理
  {
    path: '/basis',
    component: Layout,
    redirect: '/basis/basisData',
    alwaysShow: true, // will always show the root menu
    name: 'basis',
    meta: {
      title: '基础管理',
      icon: 'lock',
      roles: ['admin', 'editor'] // you can set roles in root nav
    },
    children: [
      {
        path: 'basisData',
        component: () => import('../views/basis/basisData'),
        name: 'basisData',
        meta: {
          title: '数据字典',
          roles: ['admin', 'editor'] // or you can only set roles in sub nav
        }
      },
      {
        path: 'fileManagement',
        component: () => import('../views/basis/fileManagement'),
        name: 'fileManagement',
        meta: {
          title: '文件管理',
          roles: ['admin', 'editor'] // or you can only set roles in sub nav
        }
      },
    ]
  },
  /*{
    path: '/example',
    component: Layout,
    redirect: '/example/table',
    name: 'Example',
    meta: { title: 'Example', icon: 'el-icon-s-help' },
    children: [
      {
        path: 'table',
        name: 'Table',
        component: () => import('@/views/table/index'),
        meta: { title: 'Table', icon: 'table' }
      },
      {
        path: 'tree',
        name: 'Tree',
        component: () => import('@/views/tree/index'),
        meta: { title: 'Tree', icon: 'tree' }
      }
    ]
  },

  {
    path: '/form',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'Form',
        component: () => import('@/views/form/index'),
        meta: { title: 'Form', icon: 'form' }
      }
    ]
  },

  {
    path: '/nested',
    component: Layout,
    redirect: '/nested/menu1',
    name: 'Nested',
    meta: {
      title: 'Nested',
      icon: 'nested'
    },
    children: [
      {
        path: 'menu1',
        component: () => import('@/views/nested/menu1/index'), // Parent router-view
        name: 'Menu1',
        meta: { title: 'Menu1' },
        children: [
          {
            path: 'menu1-1',
            component: () => import('@/views/nested/menu1/menu1-1'),
            name: 'Menu1-1',
            meta: { title: 'Menu1-1' }
          },
          {
            path: 'menu1-2',
            component: () => import('@/views/nested/menu1/menu1-2'),
            name: 'Menu1-2',
            meta: { title: 'Menu1-2' },
            children: [
              {
                path: 'menu1-2-1',
                component: () => import('@/views/nested/menu1/menu1-2/menu1-2-1'),
                name: 'Menu1-2-1',
                meta: { title: 'Menu1-2-1' }
              },
              {
                path: 'menu1-2-2',
                component: () => import('@/views/nested/menu1/menu1-2/menu1-2-2'),
                name: 'Menu1-2-2',
                meta: { title: 'Menu1-2-2' }
              }
            ]
          },
          {
            path: 'menu1-3',
            component: () => import('@/views/nested/menu1/menu1-3'),
            name: 'Menu1-3',
            meta: { title: 'Menu1-3' }
          }
        ]
      },
      {
        path: 'menu2',
        component: () => import('@/views/nested/menu2/index'),
        name: 'Menu2',
        meta: { title: 'menu2' }
      }
    ]
  },

  {
    path: 'external-link',
    component: Layout,
    children: [
      {
        path: 'https://panjiachen.github.io/vue-element-admin-site/#/',
        meta: { title: 'External Link', icon: 'link' }
      }
    ]
  },*/

  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
