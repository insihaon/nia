var config = function(ivhTreeviewOptionsProvider) {
    ivhTreeviewOptionsProvider.set({
        defaultSelectedState: false,
        validate: true,
        // Twisties can be images, custom html, or plain text
        //twistieCollapsedTpl: '<span style="color:red;">▶</span>',
        twistieCollapsedTpl: '<span style="">▷</span>',
        twistieExpandedTpl: '<open-twist></open-twist>',
        twistieLeafTpl: '&nbsp;&nbsp;'
        //twistieLeafTpl: '&#9679;'
    });
};

export default ['ivhTreeviewOptionsProvider', config]
