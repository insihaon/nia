var config = function ($mdThemingProvider, $mdColorPalette) {

    //https://material.angularjs.org/latest/Theming/01_introduction

    //$mdThemingProvider.generateThemesOnDemand(true);

    /*
    https://material.angularjs.org/latest/Theming/03_configuring_a_theme
    RED-Theme 예제
    $mdThemingProvider.definePalette('myPalette', {
        '50': 'ffebee',
        '100': 'ffcdd2',
        '200': 'ef9a9a',
        '300': 'e57373',
        '400': 'ef5350',
        '500': 'f44336',
        '600': 'e53935',
        '700': 'd32f2f',
        '800': 'c62828',
        '900': 'b71c1c',
        'A100': 'ff8a80',
        'A200': 'ff5252',
        'A400': 'ff1744',
        'A700': 'd50000',
        'contrastDefaultColor': 'light',    // whether, by default, text (contrast)
                                            // on this palette should be dark or light

        'contrastDarkColors': ['50', '100', //hues which contrast should be 'dark' by default
            '200', '300', '400', 'A100'],
        'contrastLightColors': undefined    // could also specify this if default was 'dark'
    });
    $mdThemingProvider.theme('default').primaryPalette('myPalette')
     */

    //https://material.angularjs.org/latest/demo/input
    /*$mdThemingProvider.theme('docs-dark', 'default')
        .primaryPalette('yellow')
        .dark();
    */

    //https://www.materialpalette.com
    //red, pink, purple, deep-purple, indigo, blue, light-blue, cyan, teal, green, light-green, lime, yellow, amber, orange, deep-orange, brown, grey, blue-grey
    //$mdThemingProvider.theme('default')
        /*.primaryPalette('grey'
            //, {
            //    'default': '900', // by default use shade 400 from the pink palette for primary intentions
            //    'hue-1': '100', // use shade 100 for the <code>md-hue-1</code> class
            //    'hue-2': '600', // use shade 600 for the <code>md-hue-2</code> class
            //    'hue-3': 'A100' // use shade A100 for the <code>md-hue-3</code> class
            //}

            // <md-button md-colors="{background: 'grey-900'}">Click me</md-button>
        )
        .accentPalette('orange')
        // .backgroundPalette('grey', {
        //     'default': 'A200',
        //     'hue-1': '300',
        //     'hue-2': '600',
        //     'hue-3': '900'
        // })
        // .dark()
        */
    ;

    $mdThemingProvider.theme('altTheme')
        .primaryPalette('grey',{
            'default': '900'})
        .accentPalette('grey',{
            'default': '700'}).dark();

    $mdThemingProvider.theme('myyellow')
        .primaryPalette('yellow').dark();

    $mdThemingProvider.theme('grey')
        .primaryPalette('grey');

    //$mdThemingProvider.setDefaultTheme('altTheme');


    if (location.href.indexOf('layout.html') != -1 || location.href.indexOf('route.html') != -1) {

        //<md-button class="md-raised md-primary md-hue-1 md-mybrown-theme" ng-click="">
        $mdThemingProvider.theme('mybrown')
            .primaryPalette('brown')
            .accentPalette('brown')
            //.dark()
        ;

        $mdThemingProvider.theme('mygrey')
            .primaryPalette('grey')
            .accentPalette('grey')
            //.dark()
        ;

        $mdThemingProvider.theme('myblue')
            .primaryPalette('blue')
            .accentPalette('blue')
            //.dark()
        ;
        //$mdThemingProvider.alwaysWatchTheme(true);

        var keys = Object.keys($mdColorPalette);
        angular.forEach($mdColorPalette, function (v, k) {
            if (k == 'blue') {
                //return;
            }
            $mdThemingProvider.theme('_' + k).primaryPalette(k).accentPalette(keys[(keys.indexOf(k) + 1) % keys.length]);
        });
    }
};

export default ['$mdThemingProvider', '$mdColorPalette', config]
