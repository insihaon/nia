import CustomChip from '../directive/customChip';
import BgPane from '../directive/bgPane';
import BgSplitter from '../directive/bgSplitter';
import FocusOn from '../directive/focusOn';
import NgEnter from '../directive/ngEnter';

import TopMenu from '../../pages/directive/top-menu';
import ProgressPopup from '../../pages/directive/progress-popup';
import SelectOnClick from '../directive/selectOnClick';

import OpenTwist from '../directive/openTwist';
import NgSglClick from '../directive/ngSglClick';
import NgRightClick from '../directive/ngRightClick';
import UiGridAutoResize from '../directive/uiGridAutoResize';
import Range from '../directive/range';
import ClearBtn from '../directive/clearBtn';
import Draggable from '../directive/draggable';
import NumbersOnly from '../directive/numbersOnly';
import ShowOverText from '../directive/showOverText';
import MdSelectClose from '../directive/mdSelectClose';
import * as MdMenu from '../directive/mdContextMenu.js';
import MdSelectAutoHide from '../directive/mdSelectAutoHide';
import MdColumnSize from '../directive/mdColumnSize';

import * as  RcaMdList from '../directive/rcaMdList';

import RcaCircularProgress from '../directive/rcaCircularProgress';

import ProcessingList from '../directive/processingList';
import ProcessingComboboxGroup from '../directive/processingComboboxGroup';


function directives() {
    this.directive('customChip', CustomChip);
    this.directive('bgSplitter', BgSplitter);
    this.directive('bgPane', BgPane);
    this.directive('focusOn', FocusOn);
    this.directive('ngEnter', NgEnter);

    this.directive('selectOnClick', SelectOnClick);
    this.directive('topMenu', TopMenu);
    this.directive('progressPopup', ProgressPopup);

    this.directive('readOnly', readOnly);
    function readOnly() {
        var directive = {
            restrict: 'A',
            link: link
        };

        return directive;

        function link(scope, element) {
            element.find("input")[0].setAttribute("readonly","true");
            element.find("input").bind('click', function(){
                element.find("button")[0].click();
            });
        }
    }

    this.directive('openTwist', OpenTwist);
    this.directive('ngSglClick', NgSglClick);
    this.directive('ngRightClick', NgRightClick);
    this.directive('uiGridAutoResize', UiGridAutoResize);
    this.directive('range', Range);
    this.directive('clearBtn', ClearBtn);
    this.directive('draggable', Draggable);
    this.directive('numbersOnly', NumbersOnly);
    this.directive('showOverText', ShowOverText);
    this.directive('mdSelectClose', MdSelectClose);
    this.directive('mdContextMenu', MdMenu.mdContextMenu);
    this.directive('mdContextMenuTrigger', MdMenu.mdContextMenuTrigger);
    this.directive('mdSelectAutoHide', MdSelectAutoHide);
    this.directive('mdColumnSize', MdColumnSize);

    this.directive('rcaMdList', RcaMdList.rcaMdList);
    this.directive('rmlColumn', RcaMdList.rmlColumn);

    /* rca 공통 디렉티브 */
    this.directive('rcaCircularProgress', RcaCircularProgress);

    /* 티켓 처리(processing) */
    this.directive('processingList', ProcessingList);
    this.directive('processingComboboxGroup', ProcessingComboboxGroup);

}

export {directives}
