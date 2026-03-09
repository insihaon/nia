/** Some utility functions used by the application */

export const setProp = (obj, key, val) => { obj[key] = val; return obj; };
export const pushToArr = (array, item) => { array.push(item); return array; };
export const uniqReduce = (arr, item) => arr.indexOf(item) !== -1 ? arr : pushToArr(arr, item);
export const flattenReduce = (arr, item) => arr.concat(item);
let guidChar = (c) => c !== 'x' && c !== 'y' ? '-' : Math.floor(Math.random()*16).toString(16).toUpperCase();
export const guid = () => "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".split("").map(guidChar).join("");

function padLeft(nr, n, str){
    return Array(n-String(nr).length+1).join(str||'0')+nr;
}
//or as a Number prototype method:

if (typeof Number.prototype.padLeft == "undefined") {
    Number.prototype.padLeft = function (n, str) {
        return Array(n - String(this).length + 1).join(str || '0') + this;
    }
}

if (typeof Number.prototype.randomInt == "undefined") {
    Number.prototype.randomInt = function (min, max) {
        min = Math.ceil(min);
        max = Math.floor(max);
        return Math.floor(Math.random() * (max - min + 1)) + min;
    }
}

if (typeof Array.prototype.clearAll == "undefined") {
    Array.prototype.clearAll = function() {
        if (this.length > 0) {
            this.splice(0, this.length);
        }
    }
}

if (typeof Array.prototype.clone == "undefined") {
    Array.prototype.clone = function(source) {
        this.clearAll();
        this.concat(JSON.parse(JSON.stringify(source)));
    }
}

if (typeof Array.prototype.copy == "undefined") {
    Array.prototype.copy = function(source) {
        this.clearAll();
        this.push(...source);
    }
}

if (typeof Array.prototype.unique == "undefined") {
    Array.prototype.unique = function() {
        return Array.from(new Set(this));
    }
}

// 배열의 동일 여부
Array.prototype.equals = function(arr) {
    if(this.length !== arr.length) return false;

    for(let i in arr) {
        let t = this[i],
            a = arr[i];

        if(t instanceof Array && a instanceof Array) {
            if(!t.equals(a)) return false;
        } else if(this[i] !== arr[i]) return false;
    }
    return true;
}

/**
 * arrayToTree
 * @param arr   [example]
 * [{
		"level1" : "장치고장",
		"level2" : "HW불량",
		"level3" : "신호인터페이스부"
	},
    {
		"level1" : "장치고장",
		"level2" : "HW불량",
		"level3" : "클럭부"
	},
    {
		"level1" : "장치고장",
		"level2" : "HW불량",
		"level3" : "제어부"
	}]
 */
Array.treeConverter = function (data, options = {}) {
    if(!Array.isArray(data)) { console.error('data is not Array'); return; }

    let result = [];
    data.forEach(item => {
        let levels = Object.keys(item);
        let nodes = Object.assign({}, levels.reduce((r, p) => (r[p] = null, r), {}));

        levels.forEach((lvl, idx) => {
            let arr;
            if(idx == 0) { arr = result }
            else {
                let parent = nodes[levels[idx - 1]];
                if(parent.children == false) { parent.children = []; }
                arr = parent.children;
            }

            let node = arr.find(val => val.label == item[lvl]);
            if(!node) {
                node = makeNode(item[lvl], lvl);
                arr.push(node);
            }
            nodes[lvl] = node;
        })
    });
    return result;

    function makeNode(label, level) {
        return Object.assign({
            label: label,
            level: level,
            selected: false,
            children: false
        }, options);
    }
}


