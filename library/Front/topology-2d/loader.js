(function() {
    var i, pair, url;

    var href = window.location.href, start, end, paramsString, pairs,
        pageParams = {};
    if (href.indexOf('?') > 0) {
        start = href.indexOf('?') + 1;
        end = href.indexOf('#') > 0 ? href.indexOf('#') : href.length;
        paramsString = href.substring(start, end);
        pairs = paramsString.split(/[&;]/);
        for (i = 0; i < pairs.length; ++i) {
            pair = pairs[i].split('=');
            if (pair[0]) {
                pageParams[decodeURIComponent(pair[0])] =
                    decodeURIComponent(pair[1]);
            }
        }
    }

    window.pageParams = pageParams;

    var mode;
    if ('mode' in pageParams) {
        mode = pageParams.mode.toLowerCase();
    }

    var scripts = [];
    if (mode === undefined) {
        const src = './index_dev.js'
        scripts.push(
            src
        );
        setJsPath(src);
    } else {
        const src = `index_${mode}.js`
        scripts.push(
            src
        );
        setJsPath(src);
    }

    function setJsPath(src) {
        window.src = `${location.origin}/${src}`;
        console.log(window.src);
    }
    
    document.write('<section>');
    for(i = 0; i < scripts.length; i++) {
	    url = scripts[i];
	    document.write('<script type="text/javascript" src="' + url + '"></script>');
    }    
    document.write('</section>');
})();
