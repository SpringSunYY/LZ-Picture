import axios from 'axios'
import {ElLoading, ElMessage} from 'element-plus'
import {saveAs} from 'file-saver'
import {getToken} from '@/utils/auth'
import errorCode from '@/utils/errorCode'
import {blobValidate} from '@/utils/ruoyi'

const baseURL = import.meta.env.VITE_APP_BASE_API
let downloadLoadingInstance;

export default {
    name(name, isDelete = true) {
        var url = baseURL + "/common/download?fileName=" + encodeURIComponent(name) + "&delete=" + isDelete
        axios({
            method: 'get',
            url: url,
            responseType: 'blob',
            headers: {'Authorization': 'Bearer ' + getToken()}
        }).then((res) => {
            const isBlob = blobValidate(res.data);
            if (isBlob) {
                const blob = new Blob([res.data])
                this.saveAs(blob, decodeURIComponent(res.headers['download-filename']))
            } else {
                this.printErrMsg(res.data);
            }
        })
    },
    downloadNetwork(url) {
        url = baseURL + "/common/download/network?url=" + encodeURIComponent(url);
        return new Promise((resolve, reject) => {
            axios({
                method: 'get',
                url: url,
                responseType: 'blob',
                headers: {'Authorization': 'Bearer ' + getToken()}
            }).then(async (res) => { // 添加 async
                const isBlob = blobValidate(res.data);
                if (isBlob) {
                    const blob = new Blob([res.data]);

                    // 转换为 Base64
                    try {
                        const base64String = await blobToBase64(blob);
                        console.log('Base64:', base64String.substring(0, 50) + '...'); // 打印前50字符
                        resolve(base64String); // 返回完整 Base64 字符串
                    } catch (err) {
                        reject(err);
                        this.printErrMsg(new Blob([JSON.stringify({msg: 'Base64转换失败'})]));
                    }
                } else {
                    this.printErrMsg(res.data);
                    reject(new Error('Invalid blob'));
                }
            }).catch(reject);
        });
    },
    resource(resource) {
        var url = baseURL + "/common/download/resource?resource=" + encodeURIComponent(resource);
        axios({
            method: 'get',
            url: url,
            responseType: 'blob',
            headers: {'Authorization': 'Bearer ' + getToken()}
        }).then((res) => {
            const isBlob = blobValidate(res.data);
            if (isBlob) {
                const blob = new Blob([res.data])
                this.saveAs(blob, decodeURIComponent(res.headers['download-filename']))
            } else {
                this.printErrMsg(res.data);
            }
        })
    },
    downloadStatisticsPicture(isDelete, type, commonKey, statisticsKey, stages, number, fileName) {
        var url = baseURL + "/picture/statisticsInfo/download/hot" +
            "?isDelete=" + encodeURIComponent(isDelete) +
            "&type=" + encodeURIComponent(type) +
            "&commonKey=" + encodeURIComponent(commonKey) +
            "&statisticsKey=" + encodeURIComponent(statisticsKey) +
            "&stages=" + encodeURIComponent(stages) +
            "&number=" + encodeURIComponent(number);
        axios({
            method: 'get',
            url: url,
            responseType: 'blob',
            headers: {'Authorization': 'Bearer ' + getToken()}
        }).then((res) => {
            const isBlob = blobValidate(res.data)
            if (isBlob) {
                const blob = new Blob([res.data])
                this.saveAs(blob, fileName)
            } else {
                this.printErrMsg(res.data);
            }
        })
    },
    zip(url, name) {
        var url = baseURL + url
        downloadLoadingInstance = ElLoading.service({text: "正在下载数据，请稍候", background: "rgba(0, 0, 0, 0.7)",})
        axios({
            method: 'get',
            url: url,
            responseType: 'blob',
            headers: {'Authorization': 'Bearer ' + getToken()}
        }).then((res) => {
            const isBlob = blobValidate(res.data);
            if (isBlob) {
                const blob = new Blob([res.data], {type: 'application/zip'})
                this.saveAs(blob, name)
            } else {
                this.printErrMsg(res.data);
            }
            downloadLoadingInstance.close();
        }).catch((r) => {
            console.error(r)
            ElMessage.error('下载文件出现错误，请联系管理员！')
            downloadLoadingInstance.close();
        })
    },
    saveAs(text, name, opts) {
        saveAs(text, name, opts);
    },
    async printErrMsg(data) {
        const resText = await data.text();
        const rspObj = JSON.parse(resText);
        const errMsg = errorCode[rspObj.code] || rspObj.msg || errorCode['default']
        ElMessage.error(errMsg);
    }
}


const blobToBase64 = (blob) => {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.onloadend = () => resolve(reader.result); // 包含 "data:image/jpg;base64," 前缀
        reader.onerror = reject;
        reader.readAsDataURL(blob);
    });
};
