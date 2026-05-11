import axios from 'axios'
import {ElLoading, ElMessage} from 'element-plus'
import router from '@/router'

export default (config: any) => {
    let instance = axios.create({
        baseURL: import.meta.env.VITE_API_URL,
        timeout: 600000,
        timeoutErrorMessage: '请求超时，请稍后再试'
    })

    let loadingInstance: any

    // 添加请求拦截器
    instance.interceptors.request.use(function (config: any) {
        // 在发送请求之前做些什么
        // 增加过度效果
        loadingInstance = ElLoading.service({
            lock: true,
            text: '加载中...',
            fullscreen: true,
            background: 'rgba(0, 0, 0, 0.7)'
        })
        return config
    }, function (error: any) {
        // 对请求错误做些什么
        return Promise.reject(error)
    })

    // 添加响应拦截器
    instance.interceptors.response.use(function (response: any) {
        // 对响应数据做点什么
        // 关闭过度效果
        loadingInstance.close()
        if (response.data.state == 'ERROR') {
            ElMessage.error(response.data.errorMessage)
        }
        return response
    }, function (error: any) {
        // 对响应错误做点什么
        // 关闭过度效果
        loadingInstance.close()
        try {
            if (error.response.data && error.response.data.state) {
                ElMessage.error(error.response.data.state + ": " + error.response.data.errorMessage)
            } else {
                ElMessage.error(error.response.status + ": " + error.response.statusText)
            }

        } catch (error) {
            ElMessage.error('' + error)
        }
        return Promise.reject(error)
    })

    return instance(config)
}
