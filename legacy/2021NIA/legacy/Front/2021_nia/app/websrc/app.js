import 'angular'
import 'angular-aria'
import 'angular-animate'
import 'angular-material'
import 'angular-messages'
import 'angular-ui-router'
import 'castillo-io/angular-css'
import 'angular-bind-html-compile'
import 'ng-material-datetimepicker'
import moment from "moment"

import './scripts/services/svg-assets-cache'
import './scripts/services/miscellaneous'
import './scripts/services/calendar'

import 'angular-material/angular-material.css!'
import 'ng-material-datetimepicker/css/material-datetimepicker.css!'
import 'fullcalendar/dist/fullcalendar.css!'

import './pages/directive/top-menu.css!'
import './css/login.css!'
import './css/fonts/roboto.css!'
import './css/app.css!'
import './css/ticket.css!'
import './css/map3d.css!'
import './css/newTicket.css!'

import './css/mddialog.css!'
import './css/ticketProcessing.css!'
import './css/alarmList.css!'
import './css/aiProcess.css!'

import './css/detailDialog.css!'

import './lib/jquery-ui/jquery-ui.js'
import './lib/jquery-ui/jquery-ui.css!'

import './lib/dataTables/md-data-table.css!'
import './lib/dataTables/icon.css!'

import './lib/dataTables/jquery.dataTables.min.js'
import './lib/dataTables/md-data-table.js'

// angular-vs-repeat (Virture Scroll)
import './lib/angular-vs-repeat/angular-vs-repeat.js'
import './lib/mdPickers/mdPickers.min.js'

// angular-meditor
import './lib/angular-meditor-master/meditor.js'

window.moment = moment;

import {run} from './scripts/registers/run';
import {config} from './scripts/registers/config';
import {filters} from './scripts/registers/filters';
import {controllers} from './scripts/registers/controllers';
import {directives} from './scripts/registers/directives';
import {factory} from './scripts/registers/factory';
import Store from 'scripts/class/SingletonStore'
import 'angular-sanitize'

var app = angular.module(Store.app, [
	'ngMaterial', 'material.svgAssetsCache', 'ngMaterialDatePicker', 'ngMessages', 'mdPickers',
	'ngAnimate', 'ngAria', 'ui.router', 'angularCSS', 'angular-bind-html-compile', 'ui.calendar',
	'md.data.table','ngSanitize',
    'vs-repeat','angular-meditor']);

Store.module = app;

[run, config, filters, controllers, directives, factory].forEach((fn)=>fn.call(app));

angular.element(document).ready(function () {
    angular.bootstrap(document, [Store.app], {strictDi: true});
});
