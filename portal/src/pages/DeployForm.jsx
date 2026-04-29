import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Form, Select, Input, Button, message, Card, Space, Radio, AutoComplete } from 'antd';
import axios from 'axios';

const { Option } = Select;

const DeployForm = () => {
    const { appId } = useParams();
    const navigate = useNavigate();
    const [form] = Form.useForm();
    const [envs, setEnvs] = useState([]);
    const [versions, setVersions] = useState([]);   // 新增：历史版本列表
    const [app, setApp] = useState(null);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        axios.get(`/api/applications/${appId}`).then(res => setApp(res.data)).catch(() => message.error('获取应用信息失败'));
        axios.get(`/api/environments?appId=${appId}`).then(res => setEnvs(res.data)).catch(() => message.error('获取环境列表失败'));
        // 新增：拉取可发布的历史版本
        axios.get(`/api/applications/${appId}/versions`).then(res => setVersions(res.data || [])).catch(() => message.error('获取版本列表失败'));
    }, [appId]);

    const onFinish = (values) => {
        setLoading(true);
        axios.post('/api/deployments', {
            appId: Number(appId),
            envId: values.envId,
            version: values.version,
            packageUrl: values.packageUrl || '',
            strategyType: values.strategyType || 'BATCH',
            batchConfig: values.strategyType === 'BATCH' ? values.batchConfig : null,
        })
            .then(() => {
                message.success('发布任务已创建');
                navigate(`/apps/${appId}/environments`);
            })
            .catch(() => message.error('创建发布任务失败'))
            .finally(() => setLoading(false));
    };

    return (
        <Card title={`发布管理 - ${app?.name || ''}`} style={{ maxWidth: 600, margin: '0 auto' }}>
            <Form form={form} layout="vertical" onFinish={onFinish}>
                <Form.Item name="envId" label="目标环境" rules={[{ required: true }]}>
                    <Select placeholder="选择环境">
                        {envs.map(env => (
                            <Option key={env.id} value={env.id}>{env.envType} - {env.nacosNamespace}</Option>
                        ))}
                    </Select>
                </Form.Item>

                {/* 版本选择改为下拉框，支持过滤和手动输入 */}
                <Form.Item name="version" label="目标版本" rules={[{ required: true, message: '请选择或输入版本' }]}>
                    <AutoComplete
                        placeholder="选择历史版本或输入新版本号"
                        options={versions.map(v => ({ value: v }))}
                        filterOption={(inputValue, option) =>
                            option.value.toLowerCase().includes(inputValue.toLowerCase())
                        }
                        allowClear
                    />
                </Form.Item>

                <Form.Item name="packageUrl" label="制品包地址">
                    <Input placeholder="可选，若不填则使用默认仓库路径" />
                </Form.Item>

                <Form.Item name="strategyType" label="发布策略" initialValue="BATCH">
                    <Radio.Group>
                        <Radio.Button value="BATCH">分批灰度</Radio.Button>
                        <Radio.Button value="ROLLING">滚动更新</Radio.Button>
                    </Radio.Group>
                </Form.Item>

                <Form.Item noStyle shouldUpdate={(prev, cur) => prev.strategyType !== cur.strategyType}>
                    {({ getFieldValue }) =>
                        getFieldValue('strategyType') === 'BATCH' ? (
                            <Form.Item name="batchConfig" label="分批配置" initialValue="10%,30%,100%">
                                <Select>
                                    <Option value="10%,30%,100%">10% → 30% → 100%</Option>
                                    <Option value="20%,100%">20% → 100%</Option>
                                    <Option value="100%">全量</Option>
                                </Select>
                            </Form.Item>
                        ) : null
                    }
                </Form.Item>

                <Form.Item>
                    <Space>
                        <Button type="primary" htmlType="submit" loading={loading}>创建发布任务</Button>
                        <Button onClick={() => navigate(-1)}>取消</Button>
                    </Space>
                </Form.Item>
            </Form>
        </Card>
    );
};

export default DeployForm;