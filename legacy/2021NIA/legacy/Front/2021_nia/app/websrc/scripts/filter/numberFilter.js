if (typeof Number.prototype.formatNumber == "undefined") {
    // 숫자 타입에서 쓸 수 있도록 format() 함수 추가
    Number.prototype.formatNumber = function () {
        if (this == 0) return 0;

        var reg = /(^[+-]?\d+)(\d{3})/;
        var n = (this + '');

        while (reg.test(n)) n = n.replace(reg, '$1' + ',' + '$2');

        return n;
    };
}

if (typeof String.prototype.formatNumber == "undefined") {
    // 문자열 타입에서 쓸 수 있도록 format() 함수 추가
    String.prototype.formatNumber = function () {
        var num = parseFloat(this);
        if (isNaN(num)) return "0";

        return num.formatNumber();
    };
}

export function CommaFilter() {
    return function(input) {
        let string = (input || "").toString();
        return string.formatNumber();
    }
}

