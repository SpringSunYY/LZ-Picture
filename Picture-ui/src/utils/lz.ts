/**
 * 参数处理
 * @param params 参数
 */
function tansParams({params}: { params: any }): string {
    let result = '';
    for (const propName in params) {
        const value = params[propName];
        const part = encodeURIComponent(propName) + "=";

        if (value !== null && value !== "" && typeof value !== "undefined") {
            if (typeof value === 'object') {
                for (const key in value) {
                    if (value[key] !== null && value[key] !== "" && typeof value[key] !== 'undefined') {
                        const param = `${propName}[${key}]`;
                        result += encodeURIComponent(param) + "=" + encodeURIComponent(value[key]) + "&";
                    }
                }
            } else {
                result += part + encodeURIComponent(value) + "&";
            }
        }
    }
    return result;
}

export default {tansParams};
