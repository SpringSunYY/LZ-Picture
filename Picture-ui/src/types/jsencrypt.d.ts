declare module 'jsencrypt' {
  class JSEncrypt {
    constructor(options?: { default_key_size?: string });
    setPublicKey(publicKey: string): void;
    setPrivateKey(privateKey: string): void;
    encrypt(plaintext: string): string | null;
    decrypt(ciphertext: string): string | null;
  }
  export = JSEncrypt;
}
