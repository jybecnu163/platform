import React, { useState, useEffect } from 'react';
import { Table, message } from 'antd';
import { useParams } from 'react-router-dom';
import axios from 'axios';

const EnvironmentList = () => {
    const { appId } = useParams();
    const [envs, setEnvs] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        const fetchEnvs = async () => {
            setLoading(true);
            try {
                const res = await axios.get(`/api/environments?appId=${appId}`);
                setEnvs(res.data || []);
            } catch (e) {
                message.error('获取环境列表失败');
            }
            setLoading(false);
        };
        fetchEnvs();
    }, [appId]);

    const columns = [
        { title: '环境ID', dataIndex: 'id' },
        { title: '环境类型', dataIndex: 'envType' },
        { title: 'Nacos命名空间', dataIndex: 'nacosNamespace' },
        { title: '隔离级别', dataIndex: 'isolation' }
    ];

    return (
        <div>
            <h2>环境列表 (App #{appId})</h2>
            <Table rowKey="id" dataSource={envs} columns={columns} loading={loading} />
        </div>
    );
};

export default EnvironmentList;