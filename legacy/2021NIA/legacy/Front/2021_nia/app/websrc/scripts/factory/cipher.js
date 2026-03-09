import Encrypt from '../class/Encrypt'

var cipher = function () {
    let factory = {};
    factory.keys = null;

    factory.setKeys = function (keys) {
        factory.keys = keys;
    };

    factory.enc = function (text) {
        let data = text;
        let keys = factory.keys;
        if (keys != null) {
            var encrypt = new Encrypt(keys, text);
            data = angular.toJson({key: encrypt.rsakey, value: encrypt.data_base64()});
        }
        return data;
    };

    return factory;
};

export default [cipher]
