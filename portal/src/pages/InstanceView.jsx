import React from 'react';
import { Table } from 'antd';

const InstanceView = () => {
  const data = [
    { id: 1, ip: '10.0.1.10', app: 'order-service', env: 'prod', status: 'ONLINE', version: 'v1.2.3' }
  ];
  const columns = [
    { title: 'IP', dataIndex: 'ip' },
    { title: '应用', dataIndex: 'app' },
    { title: '环境', dataIndex: 'env' },
    { title: '状态', dataIndex: 'status' },
    { title: '版本', dataIndex: 'version' }
  ];
  return <Table rowKey="id" dataSource={data} columns={columns} />;
};

export default InstanceView;