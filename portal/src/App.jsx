import React from 'react';
import { Routes, Route, Link, Navigate } from 'react-router-dom';
import { Layout, Menu } from 'antd';
import { AppstoreOutlined, CloudServerOutlined, HistoryOutlined } from '@ant-design/icons';
import AppList from './pages/AppList';
import InstanceView from './pages/InstanceView';
import DeployHistory from './pages/DeployHistory';
import './App.css';
import EnvironmentList from './pages/EnvironmentList'; // 新增导入
import DeployForm from './pages/DeployForm';

const { Header, Sider, Content } = Layout;

function App() {
  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Header style={{ color: 'white', fontSize: 20, fontWeight: 'bold' }}>
        统一运维管理平台
      </Header>
      <Layout>
        <Sider width={200}>
          <Menu theme="dark" mode="inline" defaultSelectedKeys={['apps']}>
            <Menu.Item key="apps" icon={<AppstoreOutlined />}>
              <Link to="/apps">应用列表</Link>
            </Menu.Item>
            <Menu.Item key="instances" icon={<CloudServerOutlined />}>
              <Link to="/instances">机器实例</Link>
            </Menu.Item>
            <Menu.Item key="history" icon={<HistoryOutlined />}>
              <Link to="/history">发布历史</Link>
            </Menu.Item>
          </Menu>
        </Sider>
        <Content style={{ padding: 24, background: '#f5f5f5' }}>
          <Routes>
            <Route path="/" element={<Navigate to="/apps" />} />
            <Route path="/apps" element={<AppList />} />
            <Route path="/instances" element={<InstanceView />} />
            <Route path="/history" element={<DeployHistory />} />
            {/* 新增环境详情路由 */}
            <Route path="/apps/:appId/environments" element={<EnvironmentList />} />
            // 在 Routes 内部添加
            <Route path="/apps/:appId/deploy" element={<DeployForm />} />
          </Routes>
        </Content>
      </Layout>
    </Layout>
  );
}

export default App;