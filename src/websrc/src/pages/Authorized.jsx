import Authorized from '@/utils/Authorized';
import React from 'react';
import Redirect from 'umi/redirect';
import { connect } from 'dva';
import { getAuthority } from '@/utils/authority'
import pathToRegexp from 'path-to-regexp';

const getRouteAuthority = (path, routeData) => {
  let authorities;
  routeData.forEach(route => {
    // match prefix
    if (pathToRegexp(`${route.path}(.*)`).test(path)) {
      authorities = route.authority || authorities; // get children authority recursively

      if (route.routes) {
        authorities = getRouteAuthority(path, route.routes) || authorities;
      }
    }
  });
  return authorities;
};

const AuthComponent = ({
  children,
  route = {
    routes: [],
  },
  location = {
    pathname: '',
  },
  user,
}) => {
  const { currentUser } = user;
  const { routes = [] } = route;
  const userLogin = getAuthority();
  const isLogin = userLogin && userLogin.username;
  return (
    // getAuthority()? <Authorized
    //   authority={getRouteAuthority(location.pathname, routes) || ''}
    //   noMatch={isLogin ? <Redirect to="/exception/403" /> : <Redirect to="/user/login" />}
    // >
    //   {children}
    // </Authorized>
    getAuthority() ? (
      <Authorized
       authority={getRouteAuthority(location.pathname, routes) || ''}
       noMatch={isLogin ? <Redirect to="/exception/403" /> : <Redirect to="/user/login" />}
      >
        {children}
      </Authorized>
    ) : <Redirect to='/user/login' />
  );
};

export default connect(({ user }) => ({
  user,
}))(AuthComponent);
