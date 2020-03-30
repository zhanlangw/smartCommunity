/* eslint-disable import/no-unresolved */

import { Icon, Tooltip, Menu } from 'antd';
import React , { useEffect } from 'react';
import { formatMessage } from 'umi-plugin-react/locale';
import { connect } from 'dva';
import Avatar from './AvatarDropdown';
import HeaderSearch from '../HeaderSearch';
import router from 'umi/router';
// import SelectLang from '../SelectLang';
import NewSelectLang from '../NewSelectLang';
import styles from './index.less';
import Text from '@/components/AddFileText';
import request from "@/utils/request";
import {token} from "@/utils/utils";
import {query} from "@/services/user";

const { SubMenu } = Menu;

const GlobalHeaderRight = props => {
  const { theme, layout } = props;
  let className = styles.right;

  if (theme === 'dark' && layout === 'topmenu') {
    className = `${styles.right}  ${styles.dark}`;
  }

  return (
    <div className={className}>
      <HeaderSearch
        className={`${styles.action} ${styles.search}`}
        placeholder={formatMessage({
          id: 'component.globalHeader.search',
        })}
        // dataSource={[
        //   formatMessage({
        //     id: 'component.globalHeader.search.example1',
        //   }),
        //   formatMessage({
        //     id: 'component.globalHeader.search.example2',
        //   }),
        //   formatMessage({
        //     id: 'component.globalHeader.search.example3',
        //   }),
        // ]}
        // onSearch={value => {
        //   router.push('/globalSearch');
        // }}
        onPressEnter={value => {
          value && router.push({pathname:`/globalSearch`, query:{searchField: value}});
          let payload = {
            start: 0,
            count: 10,
            searchField: value,
          };
            // console.log("3333",props);

            props.dispatch({
            type: 'global/fetch_getGlobalSearchData',
            payload,
            callback: function() {
              props.dispatch({
                type: 'global/setSearchPage',
                payload: 1,
              })
            }
          })

        }}
      />
      {/* <Tooltip
        title={formatMessage({
          id: 'component.globalHeader.help',
        })}
      >
        <a
          target="_blank"
          href="https://pro.ant.design/docs/getting-started"
          rel="noopener noreferrer"
          className={styles.action}
        >
          <Icon type="question-circle-o" />
        </a>
      </Tooltip> */}
      <a
        target="_blank"
        rel="noopener noreferrer"
        className={styles.action}
      >
        <Text />
      </a>
      <NewSelectLang />
      <Avatar />
      {/* <SelectLang className={styles.action} /> */}
    </div>
  );
};

export default connect(({ settings }) => ({
  theme: settings.navTheme,
  layout: settings.layout,
}))(GlobalHeaderRight);
