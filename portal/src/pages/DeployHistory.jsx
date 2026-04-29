import React from 'react';
import { Table, Tag } from 'antd';

const DeployHistory = () => {
  const data = [
    { id: 1, app: 'order-service', env: 'prod', version: 'v1.2.3', status: 'COMPLETED', creator: 'admin', time: '2025-01-01' }
  ];
  const columns = [
    { title: '应用', dataIndex: 'app' },
    { title: '环境', dataIndex: 'env' },
    { title: '版本', dataIndex: 'version' },
    { title: '状态', dataIndex: 'status', render: s => <Tag color="green">{s}</Tag> },
    { title: '操作人', dataIndex: 'creator' },
    { title: '时间', dataIndex: 'time' }
  ];
  return <Table rowKey="id" dataSource={data} columns={columns} />;
};

export default DeployHistory;