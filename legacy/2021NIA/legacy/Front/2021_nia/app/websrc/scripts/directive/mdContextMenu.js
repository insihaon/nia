// <editor-fold desc="[사용예]">
/*
// https://codepen.io/wosevision/pen/RGyvqk
<div layout layout-fill ng-app="md-context-menu">
	<md-content layout layout-fill>
		<md-menu context-menu>
			<md-whiteframe
				md-context-menu-trigger="$openContextMenu($event)"
				class="md-whiteframe-3dp"
				ng-click=""
				layout
				layout-padding
				layout-align="center center">
				<span><strong>Right click</strong> inside!</span>
			</md-whiteframe>
					<md-menu-content>
							<md-menu-item>
									<md-button ng-click="process(item, $event)">
										Copy
									</md-button>
							</md-menu-item>
							<md-menu-item>
									<md-button ng-click="process(item, $event)">
										Paste
									</md-button>
							</md-menu-item>
					</md-menu-content>
		</md-menu>
	</md-content>
</div>
*/
// <!--</editor-fold desc="[사용예]">
export const mdContextMenu = function () {
    return {
        restrict: 'A',
        require: 'mdMenu',
        link: function ($scope, $element, $attrs, mdMenuCtrl) {
            let prev = {x: 0, y: 0};
            $scope.$openContextMenu = function ($event) {
                mdMenuCtrl.offsets = () => {
                    var mouse = {
                        x: $event.clientX,
                        y: $event.clientY
                    }
                    var offsets = {
                        left: mouse.x - prev.x,
                        top: mouse.y - prev.y
                    }
                    prev = mouse;
                    return offsets;
                }
                mdMenuCtrl.open($event);
            };
        }
    }
}

export const mdContextMenuTrigger = ['$parse', $parse => {
    return (scope, element, attrs) => {
        const fn = $parse(attrs.contextMenuTrigger);
        element.bind('contextmenu', $event => {
            scope.$apply(() => {
                event.preventDefault();
                fn(scope, {$event});
            });
        });
    };
}]
