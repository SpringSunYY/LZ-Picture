import  JSEncrypt  from 'jsencrypt'

// 必须包含完整的 PEM 格式头尾
const backPublicKey = `-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmYqTJwI7NFqGiAlS05dr
fMSOgX+xjQ4SJpC390W3+uygy2s5/BGmoqZPor5gzyHMzve47hNGuktcoYKfFbOu
fJQD4AkD1TdaIaZhuqRzOcBhWSYqUv5OmaNhxDLHJH7eYe/StOWYHXqHFB0endme
KbpDD81TtepCAWUizAELX1iWZ+Hd71tbm5JTN0+qgZicrdM4vaOVoP57lUMnhn2t
C6lSIXTlL/rzeASZ1vTlyXlnSxu8fIOkx8Lpd/TFHivFBM/TTxpEv6MhpeJhlzSu
ZQyzCHIGuC31m6RcMOtt08IApyJNOtAhafHdseCXFt53OujwMd3ABpFZ8zsomWJE
HwIDAQAB
-----END PUBLIC KEY-----`

const frontPrivateKey = `-----BEGIN RSA PRIVATE KEY-----
MIICWgIBAAKBgF5FfO7PeGcgw3eYbcPnra6zMzwW0Wx8RmTLpqi10FJ1OEWnhUgr
RhFsI05xbk+4C++pBqtpkcvIoEcOSgRNxcGGpfN7NksGtnsAPiHfdXnDs2Ce/yyp
EU3jZCQ6IHmV5tzQd40H1v6xEXV9YjkltJaVZusEXjd1/ffm2Uw+ISXZAgMBAAEC
gYAlLw96OsdpFhlDgRI6pZVyRSi+WNm+Cr7RXyqbfStwet/5SXxe3QLEOH0sob07
IINEwszoq7lgQgBGRrKXQ/3uPUXzNb0mFrJJ6BWdw1qzsxzJmCdTKxunGcpZ0qJh
SqHT5srYeV5zzQGEcwGsMNcMRr5vK4A3VHUFftDvgr41oQJBALigc7GVyi18yk3z
FTDMNdkw6t5KvkY29bc5+ixdndM6nAxupFjjdOY83X9aR6iRYHQtAaCP3iKNEQzF
K+F9siUCQQCCtwpgltHRwSlGc3ns/OXlZQpK8hNzKldTi60GXw3IJfB2uOrcKRp8
FhRkaPbAliNQy2lhb3KIdbIHgrkdBcSlAkBQqzLBxhwHkf3m3y5EGwXwNxJ4PBo1
HbBw9XEcz261K2ehevpuKDwcmT2CdpTWz2ZgfIW4fQPm/tITTVdl+ADNAkAP/4EI
o0zYxDBeDAIPLECi28PvukMBJFx9wCmUDwwrw+DzVtx/x5QTPcyPIX2ZwoZd/BdQ
sogtBWKdorUvRSZ1AkB7B190GopdTfVE/AbhpPZbrtfU9PkArcLZ0p05cYKU5Gnd
PNLEDeivZ4ubQGQ+kti6f9LEKw0w2WBsLq3CL892
-----END RSA PRIVATE KEY-----`

const frontPublicKey = `-----BEGIN PUBLIC KEY-----
MIGeMA0GCSqGSIb3DQEBAQUAA4GMADCBiAKBgF5FfO7PeGcgw3eYbcPnra6zMzwW
0Wx8RmTLpqi10FJ1OEWnhUgrRhFsI05xbk+4C++pBqtpkcvIoEcOSgRNxcGGpfN7
NksGtnsAPiHfdXnDs2Ce/yypEU3jZCQ6IHmV5tzQd40H1v6xEXV9YjkltJaVZusE
Xjd1/ffm2Uw+ISXZAgMBAAE=
-----END PUBLIC KEY-----`

export function encryptBack(txt) {
  try {
    const encryptor = new JSEncrypt()
    encryptor.setPublicKey(backPublicKey)
    return encryptor.encrypt(txt)
  } catch (error) {
    console.error('加密失败:', error)
    return null
  }
}

export function encryptFront(txt){
  try {
    const decryptor = new JSEncrypt()
    decryptor.setPublicKey(frontPublicKey)
    return decryptor.encrypt(txt)
  } catch (error) {
    console.error('加密失败:', error)
    return null
  }
}

export function decryptFront(txt){
  try {
    const decryptor = new JSEncrypt()
    decryptor.setPrivateKey(frontPrivateKey)
    return decryptor.decrypt(txt)
  } catch (error) {
    console.error('解密失败:', error)
    return null
  }
}
