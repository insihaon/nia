import '../cipher/tea-block.js'
import '../cipher/base64.js'
import '../cipher/utf8.js'
import '../cipher/rsa/rsa.js'
import '../cipher/rsa/jsbn.js'
import '../cipher/rsa/prng4.js'
import '../cipher/rsa/rng.js'

class Encrypt {
    constructor(keys, data) {
        function generateUUID() {
            var d = new Date().getTime();
            //'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'
            var uuid = 'xxxxxxxxxxxxxxxx'.replace(/[xy]/g,function(c) {
                var r = (d + Math.random()*16)%16 | 0;
                d = Math.floor(d/16);
                return (c=='x' ? r : (r&0x7|0x8)).toString(16);
            });
            return uuid.toUpperCase();
        }

        function generateKey() {
            return generateUUID();
        }

        this._key = generateKey();
        this._keys = keys;
        this._data = data;
    }

    get key() {
        return this._key;
    }

    get rsakey() {
        let rsa = new RSAKey();
        rsa.setPublic(this._keys.m, this._keys.e);
        return rsa.encrypt(this._key);
    }

    get data() {
        return Tea.encrypt(this._data, this._key);
    }

    data_base64() {
        return Base64.encode(this.data);
    }

    decrypt(ciphertext, key) {
        return Tea.decrypt(ciphertext, key);
    }
}

export default Encrypt;
