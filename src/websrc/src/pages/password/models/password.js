import {
    updPassword
} from '../services';
import { message } from 'antd';
import router from 'umi/router';

export default {
    namespace: 'password',
    state: [],
    reducers: {
    },
    effects: {
        *fetch_updPassword({ payload }, { call, put }) {
            const response = yield call(updPassword, payload);
            try {
                if (response.status === 0) {
                    message.success('修改密码成功，请重新登录');
                    localStorage.removeItem('manager_authority') //删除key值
                    router.push('/user/login') //返回登录页面
                } else {
                    message.error(response.message)
                }
            } catch (e) {
                console.log(e)
                message.error('请求失败，请稍后再试！');
            }
        },
    },
};

