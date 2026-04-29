import React, { useState, useEffect } from 'react';
import { Table, Button, Space, message } from 'antd';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const AppList = () => {
  const [apps, setApps] = useState([]);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const fetchApps = async () => {
    setLoading(true);
    try {
      const res = await axios.get('/api/applications');
      setApps(res.data || []);
    } catch (e) {
      message.error('获取应用列表失败');
    }
    setLoading(false);
  };

  useEffect(() => { fetchApps(); }, []);

  const columns = [
    { title: '系统名称', dataIndex: 'name', key: 'name' },
    { title: '唯一标识', dataIndex: 'code', key: 'code' },
    { title: '业务线', dataIndex: 'bizLine', key: 'bizLine' },
    { title: '负责人', dataIndex: 'owner', key: 'owner' },
    {
      title: '操作', key: 'action', render: (_, record) => (
          <Space>
            <Button
                size="small"
                onClick={() => navigate(`/apps/${record.id}/environments`)}
            >
              环境
            </Button>
            <Button size="small" type="primary" onClick={() => navigate(`/apps/${record.id}/deploy`)}>
              发布
            </Button>
          </Space>
      )
    }
  ];

  return <Table rowKey="id" dataSource={apps} columns={columns} loading={loading} />;
};

export default AppList;