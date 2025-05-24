import JSEncrypt from 'jsencrypt/bin/jsencrypt.min'

// 密钥对生成 http://web.chacuo.net/netrsakeypair

const publicKey = 'MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1QR130Hdr3+vwt+MxDLc\n' +
    'ri+4Qtb5rsQSd5aHX7R6h369PuXGw8eiX3p2VnUy9tNQAH0u4SFZOx5lk+jwjvCZ\n' +
    'tV0weTB4OAUImZ6mE0ODILhvBiPvOeAcNHu6v5C0QaOjG2PYEWgu7g47u5lkJpIk\n' +
    'QuxW9MDl1RIhFUbbJJQ78k9jJZSjpr8xpsKX/gALgOS47vzA/emV7hQ5aCtMZSxB\n' +
    '7Fy/QNOSoyOYhbHoNs4VJ2yHyZTJqdZo4RLd0rGfwkk/FDUN94OSh/xo8zt/+wZ1\n' +
    '1ngfIrhKssbwReU62lf3d/dNUfvlJiT6d0X+BW7iHNh/Nvk37Ai9bW/BtxAIEmCj\n' +
    '0QIDAQAB'

const privateKey = 'MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDVBHXfQd2vf6/C\n' +
    '34zEMtyuL7hC1vmuxBJ3lodftHqHfr0+5cbDx6JfenZWdTL201AAfS7hIVk7HmWT\n' +
    '6PCO8Jm1XTB5MHg4BQiZnqYTQ4MguG8GI+854Bw0e7q/kLRBo6MbY9gRaC7uDju7\n' +
    'mWQmkiRC7Fb0wOXVEiEVRtsklDvyT2MllKOmvzGmwpf+AAuA5Lju/MD96ZXuFDlo\n' +
    'K0xlLEHsXL9A05KjI5iFseg2zhUnbIfJlMmp1mjhEt3SsZ/CST8UNQ33g5KH/Gjz\n' +
    'O3/7BnXWeB8iuEqyxvBF5TraV/d3901R++UmJPp3Rf4FbuIc2H82+TfsCL1tb8G3\n' +
    'EAgSYKPRAgMBAAECggEAIgtGc1npfiTgCDsGGk0uq8AAqN4GpYG8mdQ66nbDpmgB\n' +
    'As5iR+Pd9Xhy9Tph3MTE4m8fUOethR9EiLEP0ShuyGeOWa//ZqSXTUXnsOh3SoFv\n' +
    'bQHNN+izWUf0/qy3wk3OwY/hdla6OV2Y+lB3ixqMJQ30j8VMdhMhqm7akywFsKHo\n' +
    'xilc5hpaWWhc6ojGU3ILqjd+BTARbEG/CEzL6FBYZOZOjpdXMDT1IFyH/12xvart\n' +
    'fJrxCWL+P7DHvlo6+BCVnKWWZai3kS/J/2FHLXMlIoYRVsqcXUnjbXBg/LVi3EFf\n' +
    'dLp2+Lxd7GcnZM4gkommfBQO/urpS5vN+Vu2B3ZciQKBgQDsuSOuFKY8IUwwyQxY\n' +
    'uWkSwyr9Luem/ArA5CMJDbh1lXm3MmU2ePJuIn3snsEs/dzUgOEW4fMd8mrJ5Edf\n' +
    'CKbd4fC5T1pKKDovSCvV6xQAaehxFHrqUQSO6kSozjrCwDodFyHHQyDERQw2LXbf\n' +
    'iwFAy4eStkf4fExXXC//c4p0ywKBgQDmXSM1ntNKMUTHb3jup8w63d8nK8kEycca\n' +
    '513aHk1PXTl4RQktfh7Zy9Iow6laqWw7YrJ8K6hAQlGqqJxf2zpQ1uAYWo6JyCO3\n' +
    'wfTD2s2vNjkQC4Dni7HXIiVG/ERSpJI6MxThoja9lUj8+vsOVZALlouf9KSoChc0\n' +
    'dSIKZoOSUwKBgQCpTeCO6IXcC5SKb8A81J6ppJxiZ+GWND0cqA3Gs+Fxd26N76Uj\n' +
    'yGzbCCA303Ml0orWETbrPr66dF6b9oB1H6L4nleksXRKBWtX1a5EWNT9VsG/3n3q\n' +
    'h/dSn4prkNPF5eWqWJj4ArK0rQ2G0g1q4+gJh4d3Dpd6yKtw76YEdYZY8QKBgQCH\n' +
    'LyLpZYICqPT+wEjMbIXqCafZudUmTS7dd+bYiC3AJCoEjSa8nqWGFBkJOQEaJhBQ\n' +
    'rTOmsIv04bZBGt9hVSpcpCwvvnylGGD1hgmYIb/QUVadNtL3jj/Xyr7rcEC76r2x\n' +
    'D6byXhoXmRKS+sn3eHimhDr9rDgwMHO7of1O8yfE5wKBgBCxBovHiZhFD3ExSPBn\n' +
    '3hE0kHZlNBmNMByX1J8jG526ZMje3Z8wP5jCNRR7HQduXBGcwfjF4UFE8O5JBMCs\n' +
    'SQWaA2gbaIbC3CAP8naZS3rDbMF1iSUWYL6bFHfi8jJSjlU5tE1oXBKOooUHnA5F\n' +
    'KRmnmEP5m6gaAUGKfzxaK0j6'

// 加密
export function encrypt(txt) {
  const encryptor = new JSEncrypt()
  encryptor.setPublicKey(publicKey) // 设置公钥
  return encryptor.encrypt(txt) // 对数据进行加密
}

// 解密
export function decrypt(txt) {
  const encryptor = new JSEncrypt()
  encryptor.setPrivateKey(privateKey) // 设置私钥
  return encryptor.decrypt(txt) // 对数据进行解密
}

