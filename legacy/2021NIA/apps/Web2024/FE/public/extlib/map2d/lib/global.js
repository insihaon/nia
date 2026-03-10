if (typeof Math.randomInt == "undefined") {
    Math.randomInt = function (min, max) {
        return Math.floor(Math.random() * (max - min + 1)) + min;
    };
}

if (typeof Array.prototype.remove == "undefined") {
    Array.prototype.remove = function (value, fnEquals) {
        if (!fnEquals) {
            fnEquals =
                !!value && Array.isArray(value)
                    ? (item, array) => array.includes(item)
                    : (item, value) => item === value;
        }

        for (var i = 0, idx = 0; i < this.length; i++, idx++) {
            if (fnEquals(this[i], value, idx)) {
                this.splice(i, 1);
                i--;
            }
        }
        return this;
    };
}

if (typeof d3.selection.prototype.moveToFront == "undefined") {
    d3.selection.prototype.moveToFront = function () {
        return this.each(function () {
            if (this.parentNode != null) {
                this.parentNode.appendChild(this);
            }
        });
    };
}

if (typeof d3.selection.prototype.moveToBack == "undefined") {
    d3.selection.prototype.moveToBack = function () {
        return this.each(function () {
            if (this.parentNode != null) {
                var refChild = this.parentNode.firstChild;
                if (refChild) {
                    this.parentNode.insertBefore(this, refChild);
                }
            }
        });
    };
}

if (typeof Array.prototype.deduplication == "undefined") {
    Array.prototype.deduplication = function (key) {
        let keys = []
        let arr = this.reduce(function (acc, cur, index) {        // 중복제거
            if(key) {
                if (cur[key] && !keys.includes(cur[key])) {
                    acc.push(cur);
                    keys.push(cur[key]);
                }
            } else if (acc.indexOf(cur) < 0) {
                acc.push(cur);
            }
            return acc
        }, []);
        return arr
    }
}