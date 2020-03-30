import { routerRedux } from 'dva/router';
import { stringify } from 'qs';
import { user_login } from '@/services/user';
import { setAuthority } from '@/utils/authority';
import { reloadAuthorized } from '@/utils/Authorized';
import { message } from 'antd';
import router from 'umi/router';

export default {
  namespace: 'login',

  state: {
    autoLogin: false,
    login_state: null,
  },

  effects: {
    *login ({ payload }, { call, put, select }) {
      const response = yield call(user_login, payload);
      const autoLogin = yield select(state => state.login.autoLogin);
      try {
        if (response.status === 0) {
          setAuthority(JSON.stringify(response.value));
          yield put({
            type: 'changeLoginStatus',
            payload: response
          });
          if (autoLogin) {
            localStorage.setItem('user', JSON.stringify(payload));
          } else {
            localStorage.removeItem('user');
          }
          reloadAuthorized();
          router.push('/');
        } else {
          message.error(response.message);
          yield put({
            type: 'changeLoginStatus',
            payload: null
          });
          localStorage.removeItem('manager_authority');
        }
      } catch (error) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *logout (_, { put }) {
      localStorage.removeItem('manager_authority');
      yield put({
        type: 'changeLoginStatus',
        payload: null
      });
      reloadAuthorized();
      router.push('/user/login');
    },
  },
  reducers: {
    changeAutoLogin (state, { payload }) {
      return {
        ...state,
        autoLogin: payload.autoLogin,
      };
    },
    changeLoginStatus (state, { payload }) {
      return {
        ...state,
        login_state: payload
      };
    },
  },
};
