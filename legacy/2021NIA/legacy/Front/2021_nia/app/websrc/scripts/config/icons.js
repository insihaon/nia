var config = function($mdIconProvider) {
    $mdIconProvider
        .iconSet('communication', 'img/icons/sets/communication-icons.svg', 24)
        .icon('menu', 'img/icons/menu.svg', 24)
        .icon('more_vert', 'img/icons/more_vert.svg', 24);

};

export default ['$mdIconProvider', config]