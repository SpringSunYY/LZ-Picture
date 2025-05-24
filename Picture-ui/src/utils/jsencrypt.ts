// src/utils/encrypt.ts
import JSEncrypt from 'jsencrypt'

// 必须包含完整的 PEM 格式头尾
const publicKey = `MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmYqTJwI7NFqGiAlS05dr
fMSOgX+xjQ4SJpC390W3+uygy2s5/BGmoqZPor5gzyHMzve47hNGuktcoYKfFbOu
fJQD4AkD1TdaIaZhuqRzOcBhWSYqUv5OmaNhxDLHJH7eYe/StOWYHXqHFB0endme
KbpDD81TtepCAWUizAELX1iWZ+Hd71tbm5JTN0+qgZicrdM4vaOVoP57lUMnhn2t
C6lSIXTlL/rzeASZ1vTlyXlnSxu8fIOkx8Lpd/TFHivFBM/TTxpEv6MhpeJhlzSu
ZQyzCHIGuC31m6RcMOtt08IApyJNOtAhafHdseCXFt53OujwMd3ABpFZ8zsomWJE
HwIDAQAB`

const privateKey = `-----BEGIN RSA PRIVATE KEY-----
MIIBVAIBADANBgkqhkiGw0BAQEFAASCAT4wggE6AgEAAkEAqhHyZfSsYourNxaY
7Nt+PrgrxkiA50efORdI5U5lsW79MmFnusUA355oaSXcLhu5xxB38SMSyP2KvuKN
PuH3owIDAQABAkAfoiLyL+Z4lf4Myxk6xUDgLaWGximj20CUf+5BKKnlrK+Ed8gA
kM0HqoTt2UZwA5E2MzS4EI2gjfQhz5X28uqxAiEA3wNFxfrCZlSZHb0gn2zDpWow
cSxQAgiCstxGUoOqlW8CIQDDOerGKH5OmCJ4Z21v+F25WaHYPxCFMvwxpcw99Ecv
DQIgIdhDTIqD2jfYjPTY8Jj3EDGPbH2HHuffvflECt3Ek60CIQCFRlCkHpi7hthh
YhovyloRYsM+IS9h/0BzlEAuO0ktMQIgSPT3aFAgJYwKpqRYKlLDVcflZFCKY7u3
UP8iWi1Qw0Y=
-----END RSA PRIVATE KEY-----`

export function encrypt(txt: string): string | null {
  try {
    const encryptor = new JSEncrypt()
    encryptor.setPublicKey(publicKey)
    return encryptor.encrypt(txt)
  } catch (error) {
    console.error('加密失败:', error)
    return null
  }
}

export function decrypt(txt: string): string | null {
  try {
    const decryptor = new JSEncrypt()
    decryptor.setPrivateKey(privateKey)
    return decryptor.decrypt(txt)
  } catch (error) {
    console.error('解密失败:', error)
    return null
  }
}
