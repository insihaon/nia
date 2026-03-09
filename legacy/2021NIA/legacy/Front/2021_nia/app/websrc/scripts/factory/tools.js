import 'angular'
import * as Constants from "../class/Constants.js";
import Encrypt from '../class/Encrypt'

let global = window;

class tools {
    static name() {
        return 'tools';
    }

    static Factory(...injected) {
        tools.instance = new tools(...injected);
        return tools.instance;
    }

    constructor($injector) {
        Object.assign(this, {
            $injector,
            $http: $injector.get('$http'),
            $timeout: $injector.get('$timeout'),
            $rootScope: $injector.get('$rootScope'),
            $state: $injector.get('$state'),
            $log: $injector.get('$log'),
            $css: $injector.get('$css'),
            $mdDialog: $injector.get('$mdDialog'),
            $mdSelect: $injector.get('$mdSelect'),
            focus: $injector.get('focus'),
            $mdToast: $injector.get('$mdToast'),
            $transitions: $injector.get('$transitions'),
            $q: $injector.get('$q'),
            $mdSidenav: $injector.get('$mdSidenav'),
            $cipher: $injector.get('cipher'),
            storeService: $injector.get('storeService'),
            $compile: $injector.get('$compile'),
            // event: $injector.get('event'),
            $document: $injector.get('$document'),
        });

        this.store = this.storeService.store;
        this.localStorage = window.localStorage;
        // this.push = this.event.fireEvent;
        this.tools = this;
        this.debug = this.isDebug();


        let tools = this;

        Object.assign(global, {
            $tools: tools,
            getScope: () => angular.element($0).scope(),
            getCtrl: () => angular.element($0).controller(),
            goCtrl: () => angular.element($0).controller().constructor,
            getPath: () => angular.element($0).scope().config.path,
            getTicket: tools.getTicket,
            getTicket2: tools.getTicket2,
            getTicketAll: tools.getTicketAll,
            resetTopologyData: tools.resetTopologyData,
            simulateTicket: tools.simulateTicket,
            removeAllStoreTicket: tools.removeAllStoreTicket,
            getCurrentUserCount: tools.getCurrentUserCount,
            testTicketFilter: tools.testTicketFilter,
            restfulRequest: tools.restfulRequest,
            postRequest: tools.postRequest,
            getTT: () => tools.store.tickets,
            getCI: () => (tools.store.inspectorTicket || {}).data
        });

        Object.defineProperty(global, 'src', { get: function () { return global.getPath() }, configurable: true })
        Object.defineProperty(global, 'ctrl', { get: function () { return global.getCtrl() }, configurable: true })
        Object.defineProperty(global, 's', { get: function () { return global.getScope() }, configurable: true })
        Object.defineProperty(global, 'go', { get: function () { return global.goCtrl() }, configurable: true })



        // let ticketStorage = this.store.ticketStorage;
        // if (this.store.auth == true && !ticketStorage) {
        //     this.ticketStorage = this.store.ticketStorage = this.$injector.get('TicketStorage');
        // }

        this.stylesheets = {};
        this.store.constants = this.constants;
    }

    get ticketStorage() {
        let ticketStorage = this.store.ticketStorage;
        if (!ticketStorage) {
            if (this.store.auth == true && (!this.tools.store.hasProfile('roadshow') || this.tools.$state.current.name == 'rca.monitoring_tt')) {
                ticketStorage = this.store.ticketStorage = this.$injector.get('TicketStorage');
                this.subscribe();
            } else {
                this.onclose(false);
            }
        }

        return ticketStorage;
    }

    get filterStorage() {
        let filterStorage = this.store.filterStorage;
        if (!filterStorage) {
            filterStorage = this.store.filterStorage = this.$injector.get('FilterStorage');
        }
        return filterStorage;
    }

    get inspectorTicket() {
        let inspectorTicket = this.store.inspectorTicket;
        if (!inspectorTicket) {
            if (this.store.auth == true) {
                inspectorTicket = this.store.inspectorTicket = this.$injector.get('InspectorTicket');
                if (!this.singlePageData) {
                    inspectorTicket.load();
                }
            }
        }
        return inspectorTicket;
    }

    isDebug() {
        return this.store.debug;
    }

    isDesktopMode() {
        return (this.tools.$state.current.name == 'rca.monitoring_tt' || this.tools.$state.current.name == 'rca.control_monitoring_tt');
    };

    toString(text) {
        return text ? text.toString() : '';
    };

    fromJson(json) {
        return angular.fromJson(json);
    };

    toJson(obj, pretty) {
        return angular.toJson(obj, pretty);
    };

    init_store() {
        this.logout_data(true);
    }

    logout_data(keep) {

        let tools = this;
        tools.$mdDialog.hide();

        if (tools.store.ticketStorage) {
            angular.copy([], tools.store.tickets);
            tools.store.ticketStorage.removeAll();
            tools.store.ticketStorage = null;
        }
        if (tools.store.inspectorTicket) {
            // tools.store.tickets = [];
            tools.store.inspectorTicket.clear();
            tools.store.inspectorTicket = null;
        }

        if (!keep) {
            tools.store.auth = false;
            tools.store.user = null;
            tools.store.service_data = {};
        }

        try {
            let eventbus = tools.store.socketEventBus;
            if (eventbus /*&& eventbus.state == 1*/) {
                eventbus.sockJSConn.close();
                tools.store.socketEventBus = null;
            }
        } catch (e) {
        }

    }

    logout() {
        // location.replace(this.store.logout_url);

        let tools = this;
        tools.$http.post("logout")
            .then(response => {

                tools.logout_data();

                let page = tools.view(tools.store.login_page);
                tools.statego(page);
            });
    };

    injection($scope, self) {

        for (let variable in this) {
            $scope[variable] = this[variable];
        }
        $scope.debug = this.isDebug();

        angular.extend($scope, {
            safeApply: (fn) => {
                fn && fn.call(null);
                if ($scope.$$phase == '$apply' || $scope.$$phase == '$digest') {
                } else {
                    setTimeout(() => {
                        $scope.$apply();
                    }, 100);
                }
            },
            isVisible: () => {
                //<div id="{{config.viewName}}" style="display: none;"></div>
                return !!document.getElementById($scope.config.viewName);
            }
        });

        const proto = Object.getPrototypeOf(this);
        const names = Object.getOwnPropertyNames(proto);
        const includes = ['function', 'object'];
        const excludes = ['constructor', 'value', 'value2', 'view'];
        names.forEach(variable => {
            const type = typeof this[variable];
            if (excludes.filter(key => key === variable).length === 0 && includes.filter(key => key === type).length > 0) {
                $scope[variable] = this[variable];
            }
        });

        if (self && self.templateUrl) {
            let style = self.templateUrl.replace("html", "css");
            this.applyCss($scope.$parent, style);
        }
    }

    applyCss(targetScope, style) {
        if (this.stylesheets[style] == null) {
            this.stylesheets[style] = 0;
            if (this.debug) console.log('add', style);
            this.$css.add(style);//setTimeout(() => this.$css.add(style));
        }
        this.stylesheets[style]++;
        targetScope.$on('$destroy', () => {
            setTimeout(() => {
                this.stylesheets[style]--;
                if (this.stylesheets[style] <= 0) {
                    if (this.debug) console.log('remove', style);
                    delete this.stylesheets[style];
                    this.$css.remove(style);//setTimeout(() => this.$css.remove(style));
                }
            });
        });
    }

    statego(page) {
        this.$debug("state.go: " + page);
        this.$state.go(page);

        if (location.href.indexOf('/layout') === -1 && location.href.indexOf('/route') === -1) {
            this.localStorage.page = page;
        }
    };

    showAlert(title, content) {
        return this.$mdDialog.show(
            this.$mdDialog.alert()
                .multiple(true)
                .parent(angular.element(document.querySelector('#popupContainer')))
                .clickOutsideToClose(false)
                .title(title)
                .textContent(content)
                .ariaLabel('Alert')
                .ok('확인')
        );
    };

    draggableDialog(element, toolbar) {
        let isDragging = false;
        let offsetX = 0;
        let offsetY = 0;
        let left = parseInt(element.style.left) || 0;
        let top = parseInt(element.style.top) || 0;

        // 변수 추가: 마지막 애니메이션 프레임의 타임스탬프와 이전 위치
        let lastFrameTime;
        let prevLeft = left;
        let prevTop = top;

        // 변수 추가: 애니메이션 프레임 함수
        function animate() {
            const now = Date.now();
            const elapsed = now - lastFrameTime;

            // 부드러운 이동을 위해 애니메이션 프레임 간의 거리를 계산
            const deltaX = (left - prevLeft) * elapsed * 0.1;
            const deltaY = (top - prevTop) * elapsed * 0.1;

            // 새로운 위치를 계산하고 업데이트
            prevLeft += deltaX;
            prevTop += deltaY;

            // 부드러운 이동을 위해 현재 위치와 목표 위치 간의 차이를 조정
            const correctionFactor = 0.3;
            const correctedLeft = prevLeft + correctionFactor * (left - prevLeft);
            const correctedTop = prevTop + correctionFactor * (top - prevTop);

            element.style.left = correctedLeft + "px";
            element.style.top = correctedTop + "px";

            // 마지막 프레임의 시간 업데이트
            lastFrameTime = now;

            // requestAnimationFrame으로 다음 애니메이션 프레임 예약
            requestAnimationFrame(animate);
        }

        toolbar.addEventListener("mousedown", (event) => {
            isDragging = true;
            offsetX = event.clientX - left;
            offsetY = event.clientY - top;

            // 애니메이션 프레임을 시작하기 전에 마지막 프레임의 시간을 초기화
            lastFrameTime = Date.now();

            // 애니메이션 프레임 시작
            requestAnimationFrame(animate);
        });

        document.addEventListener("mousemove", (event) => {
            if (isDragging) {
                left = event.clientX - offsetX;
                top = event.clientY - offsetY;
                // element.style.left = left + "px";
                // element.style.top = top + "px";
            }
        });

        document.addEventListener("mouseup", () => {
            isDragging = false;
            // Store the last position on mouseup
            // left = parseInt(element.style.left) || 0;
            // top = parseInt(element.style.top) || 0;
            // mouseup 시 마지막 위치 저장
            prevLeft = parseInt(element.style.left) || 0;
            prevTop = parseInt(element.style.top) || 0;

            // 시작 위치 업데이트
            left = prevLeft;
            top = prevTop;
        });
    }

    showToast(content, parentId, action, delay, position = 'bottom right') {

        if (delay == null || delay == undefined) {
            delay = 5000;
        }

        var toast = this.$mdToast.simple()
            .content(content || '정보입니다.')
            .action(action /*|| '확인'*/)
            .highlightAction(true)
            .hideDelay(delay)
            .position(position)

        if (parentId)
            toast.parent(angular.element(document.getElementById(parentId)));
        else
            toast.parent(angular.element(document.querySelector('body')));

        return this.$mdToast.show(toast);
    };

    createConfirmDlg(title, content, label) {
        var confirm = this.$mdDialog.confirm()
            .multiple(true)
            .parent(angular.element(document.querySelector('#popupContainer')))
            .title(title)
            .textContent(content)
            .ariaLabel(label)
            .ok('확인')
            .cancel('취소');
        return this.$mdDialog.show(confirm);
    }


    createTicketConfirmDlg(title, textContent, label, htmlContent) {
        var confirm = this.$mdDialog.confirm()
            .multiple(true)
            .parent(angular.element(document.querySelector('#popupContainer')))
            .title(title)
            .htmlContent(
                `
            <br>
            <p>${htmlContent.first}</p>
            <p>${htmlContent.second}</p>
            <p>${htmlContent.third}</p>
            <p>${htmlContent.fourth}</p>
            `
            )
            .ariaLabel(label)
            .ok('확인')
            .cancel('취소');
        return this.$mdDialog.show(confirm);
    }

    createPromptDlg(title, content) {
        var prompt = this.$mdDialog.prompt()
            .multiple(true)
            .parent(angular.element(document.querySeleccreateConfirmDlgtor('#popupContainer')))
            .title(title)
            .textContent(content)
            .ariaLabel('비밀번호를 입력해주세요.')
            .ok('확인')
            .cancel('취소');
        return this.$mdDialog.show(prompt);
    }

    cipher(text, callback) {
        const tools = this;
        tools.$http.post("getkey")
            .then((response) => {
                //console.log(response.data);
                tools.$cipher.setKeys(response.data);
                callback(tools.$cipher.enc(text));
            }, function (response) {
                callback(text);
            });
    }

    view(defaultView) {
        let view = null;
        let routeUrl = location.href.split('#!/')[1];
        if (routeUrl != null) {
            let paths = routeUrl.split('/');
            view = paths[paths.length - 2];
        }
        return view || defaultView || 'login';
    };

    checkbox_readonly(item, key) {
        item[key] = !item[key];
    };

    http(params, callback) {

        let tools = this;

        if (tools.isDebug()) {
            //this.$info(factory.toJson(params, 4));
            console.dir(params);
        }

        let result_callback = function (result) {
            callback(result);
        };

        let { $log, showAlert } = this;
        params = JSON.stringify(params);
        this.$http.post(params.path || 'service', params)
            .then(function (response) {
                $log.debug(response);
                let result = null;
                if (response != null && response.data != null) {
                    if (response.data.error != null) {
                        $log.error(response.data.error);
                        showAlert("", response.data.error);
                    } else if (response.data.result != null && Array.isArray(response.data.result)) {
                        result = response.data.result;
                    } else {
                        result = response.data;
                    }
                }

                // if(tools.isDebug()){
                //     console.table(result);
                // }

                result_callback(result);
            }, function (response) {
                console.log(response);
                result_callback();
            });
    };

    log(...params) {
        this.$log.log(...params);
    };

    $info(...params) {
        this.$log.info(...params);
    };

    $warn(...params) {
        this.$log.warn(...params);
    };

    $debug(...params) {
        this.$log.debug(...params);
    };

    color_convert() {
        let pub = {};

        pub.to_rgba_array = function (color) {
            return (color = color.replace('#', '')).match(new RegExp('(.{' + color.length / (color.length <= 6 ? 3 : 4) + '})', 'g')).map(function (l, i) {
                return parseInt(color.length % 2 ? l + l : l, 16)
            });
        };

        pub.to_rgba = function (color) {
            if (color == null) return null;
            let a = pub.to_rgba_array(color);
            return 'rgba(' + a[0] + ',' + a[1] + ',' + a[2] + ',' + (a[3] == null ? 1 : a[3] / 255) + ')';
        };

        pub.argb_to_rgba = function (color) {
            if (color == null) return null;
            if (color.length < 8) return pub.to_rgba(color);
            let a = pub.to_rgba_array(color);
            return 'rgba(' + (a[1] + ',' + a[2] + ',' + a[3] + ',' + a[0] / 255) + ')';
        };

        return pub;
    };

    subscribe() {
        let tools = this;
        if (tools.store.getMode("op_no_sock") == true || tools.store.socketServerStart == false || (tools.store.socketEventBus)) {
            return;
        }

        this.clearConnectTimer();
        this.tryDeviceConnect();
    }

    onChangeSession(error, message) {
        let tools = this;
        let currentUserCount = message.body.CURRENT_USER_COUNT;
        console.log('disconnect debug error: onChangeSession', message.body, error);

        tools.store.currentUserCount = currentUserCount;
        this.$rootScope.$apply();
    }

    async onChangeEvent(error, message) {

        let tools = this;
        let messageBuffer = tools.messageBuffer;

        if (message) {
            messageBuffer.push(message);
        }

        if (tools.store.tickets.length < 1) {
            return;
        }

        for (let i = 0; i < messageBuffer.length; i++) {
            let msg = messageBuffer[i];

            if (!msg) {
                debugger;
                continue;
            }

            let eventType = msg.body.event_type;
            let sequence = msg.body.sequence;

            if (sequence) {
                if (sequence <= tools.store.last_event_sequence) {
                    return;
                }

                tools.store.last_event_sequence = sequence;
            }

            switch (eventType) {
                case Constants.SocketEventType.TICKET_NEW:
                    await tools.onChangeTicket(null, msg);
                    break;
                default:
                    await tools.onChangeCableTicket(null, msg);
                    break;
            }
        }
        tools.messageBuffer = [];
    }

    async onChangeSyslog(error, message) {
        // console.trace(message);

        const tools = this;
        const syslogs = message.body.data;
        tools.store.niaSysDialogList.push(...message.body.data);
        tools.store.niaSyslogList = [];
        tools.store.niaSyslogList.push(...syslogs);
    }

    async onChangeTicket(error, message) {
        //console.trace(message);
        if (!message) {
            debugger;
        }

        let tools = this;
        let isTestMode = tools.store.getMode('op_socket_event_test');

        let eventData = message.body.data || message.body;
        let eventType = message.body.event_type;
        let sequence = message.body.sequence;
        let sendOnlyDevUser = message.body.sendOnlyDevUser;

        if (isTestMode) {
            console.log(`======> [event_type] : ${eventType}\t\t[ticket_id] : ${message.body.ticket_id ||
                Array.isArray(eventData) ? eventData[0].ticket_id : eventData.ticket_id}\t\t[ticket] : ${JSON.stringify(eventData)}`);
        }

        switch (eventType) {
            case Constants.SocketEventType.TICKET_NEW:
            case Constants.SocketEventType.TICKET_UPDATE:
            case Constants.SocketEventType.TICKET_MERGE:
                if (!tools.store.isTicketRealTimePause) {
                    await this.updateTickets(sequence, eventType, eventData, sendOnlyDevUser);
                }
                break;
            case Constants.SocketEventType.TICKET_DELETE:
                if (!tools.store.isTicketRealTimePause) {
                    await this.deleteTickets(sequence, eventType, eventData, sendOnlyDevUser);
                }
                break;
            case 'TSDN_RESERVE_RESULT':
                // 광레벨 > 예약 callback
                if (eventData.isSuccess == true) {

                    tools.$rootScope.$broadcast('onViewEvent', { type: Constants.ViewEventType.SUCCESSED_TSDN_RESERVE, data: eventData });
                } else if (eventData.isSuccess == false) {
                    tools.$rootScope.$broadcast('onViewEvent', { type: Constants.ViewEventType.FAILED_TSDN_SERVICE, data: eventData });
                }
                break;
            case 'TSDN_SERVICE_RESULT':
                if (eventData.isSuccess == true) {
                    tools.$rootScope.$broadcast('onViewEvent', { type: Constants.ViewEventType.SUCCESSED_TSDN_SERVICE, data: eventData });
                } else if (eventData.isSuccess == false) {
                    tools.$rootScope.$broadcast('onViewEvent', { type: Constants.ViewEventType.FAILED_TSDN_SERVICE, data: eventData });
                }
                // 광레벨 > 서비스 생성 callback
                break;
            /*
            case 'MAIL_RESULT' :
                Object.assign(eventData, { status: 'ACK' });
                await this.updateTickets(sequence, eventType, eventData, sendOnlyDevUser);
                break;
            */
            default:
                tools.$log.error(`Unknown Event Type=${msg.body.type}, count=${v.length}`);
        }
    }

    async onChangeCableTicket(error, message) {

        let tools = this;

        let eventData = message.body.data;
        let eventType = message.body.event_type;

        switch (eventType) {
            case 'INSPECTOR_NEW':
            case 'INSPECTOR_UPDATE':
                if (!tools.store.isTicketRealTimePause) {
                    tools.updateCableTickets(eventType, eventData);
                }
                break;
            default:
                tools.$log.error(`Unknown Event Type=${message.body.type}`);
        }
    }

    async updateTickets(sequence, eventType, newTickets, sendOnlyDevUser = false) {
        let tools = this;

        if (tools.store.isTicketRealTimePause) return;

        if (eventType == Constants.SocketEventType.TICKET_NEW) {
            /* EngineToUI */
            await loadTicket(newTickets);
        } else {
            /* T-SDN 자가구성 */
            if (Array.isArray(newTickets)) {
                newTickets.forEach((ticket, index) => {
                    updateTicketStore(ticket);
                })
            } else {
                updateTicketStore(newTickets);
            }
        }

        function updateTicketStore(newTicket) {
            let ticket = tools.store.niaList.find(v => v.ticket_id == newTicket.ticket_id);
            if (ticket) {
                Object.assign(ticket, newTicket);
            } else {
                tools.store.niaList.push(newTicket);
            }
            tools.fireTicketChangedEvent();
        }

        function loadTicket(newTicket) {
            try {
                tools.http({
                    service: tools.constants.Service.rca,
                    action: "SELECT_NIA_TICKET_LIST",
                    TICKET_ID: newTicket[0].ticket_id
                }, (result) => {
                    if (!result || result.length < 1) {
                        return false;
                    }
                    tools.store.niaList.splice(0, 0, result[0]);
                    tools.fireTicketChangedEvent(result);

                    var type = tools.getTicketType(tools.store.niaList.find((list) => list.ticket_id == newTicket[0].ticket_id).root_cause_type);

                    if (tools.store.sound) {
                        let audio = document.getElementById(type.audio);
                        if (audio) {
                            audio.play().catch(function (e) { });
                        }
                    }
                });
            } catch (e) {
                console.error(e);
            }
        }
    }

    fireTicketChangedEvent(tickets) {
        let tools = this;
        tools.$rootScope.$broadcast('onViewEvent', { type: Constants.ViewEventType.CHANGED_NIA_TICKET, data: tickets });
    }

    deleteTickets(sequence, eventType, newTickets, sendOnlyDevUser = false) {
        let tools = this;

        let delete_ticket_id = newTickets[0].ticket_id;
        let ticket = tools.store.niaList.find(v => v.ticket_id == delete_ticket_id);

        if (!ticket) {
            tools.$log.error(`Not Found Ticket ${delete_ticket_id}`);
            return;
        } else {
            tools.store.niaList.remove(ticket);
            console.log(`Success Delete Ticket ${delete_ticket_id}`);
        }
    }

    updateCableTickets(eventType, eventData) {

        let tools = this;

        tools.store.inspectorTicket.upsert(eventData, true);

        tools.$rootScope.$apply();
    }

    lazyLoadTicket(delay = 300, scope) {
        let tools = this;

        function clearTimer() {
            if (tools.delayTimer) {
                clearTimeout(tools.delayTimer);
                tools.delayTimer = null;
            }
        }

        if (delay) {

            clearTimer();

            this.delayTimer = setTimeout(function () {
                tools.loadTicket();
                clearTimer();
            }, delay);
        } else {
            tools.loadTicket();
        }
    }

    loadTicket() {
        let tools = this;
        let store = this.store;

    }

    reloadTickets() {
        let tools = this;

    }

    getTicket(id) {
        let tools = this || $tools;
        id = id || tools.store.ticketAllSearch;
        return tools.store.tickets.find(t => t.ticket_id == id);
    }

    getTicketAll() {

        let tools = this || $tools;

        let value = {}
        angular.forEach(tools.store.tickets, (v, k) => {
            value[v.ticket_id] = v;
        });
        // console.log(value);`
        console.table(value);
    }

    resetTopologyData() {
        let tools = this || $tools;

        let tickets = tools.store.niaList;
        tickets.splice(0);
        tools.$rootScope.$broadcast('broadcastReloadTopology', 'reset');
    }

    simulateTicket() {
        let tools = this || $tools;
        let tickets = tools.store.niaList;
        let clone = angular.copy(tickets, []);

        tickets.splice(0);
        tools.$rootScope.$broadcast('broadcastReloadTopology', 'reset');
        tools.fireTicketChangedEvent();
        tools.shiftTicket(clone, 2)
    }

    shiftTicket(clone, delay = 10) {
        if (clone.length === 0) return;

        setTimeout(() => {
            let tools = this || $tools;
            tools.store.niaList.push(clone.shift())
            tools.fireTicketChangedEvent(tools.store.niaList);
            tools.shiftTicket(clone)
        }, 1000 * delay)
    }

    // for Test
    testTicketFilter(data, num = '1') {
        let tools = this || $tools;
        let filter = tools.$injector.get('$filter')(`rcaTicketFilter${num}`);
        data = data || tools.store.tickets;

        if (Array.isArray(data)) {
            return filter(data, tools, true);
        } else if (typeof data === 'object') {
            return filter([data], tools, true);
        } else if (typeof data == 'number') {
            return filter([tools.store.tickets.find(ticket => ticket.ticket_id == data)], tools, true);
        }
    }

    removeAllStoreTicket() {
        angular.copy([], tools.store.tickets);
    }

    getCurrentUserCount() {
        return tools.store.currentUserCount;
    }

    get messageBuffer() {
        let tools = this;
        if (!tools._messageBuffer) {
            tools._messageBuffer = [];
        }

        return tools._messageBuffer;
    }

    set messageBuffer(buffer) {
        let tools = this;
        tools._messageBuffer = buffer;
    }

    onopen(isTest) {
        let { store, $mdDialog } = this;
        let tools = this;
        if (isTest) {
            return;
        }
        store.socketEventBus.registerHandler(Constants.SocketEventBusAddress.BROADCAST_UNKNOWN, function (error, message) {
            tools.resetTopologyData();
        });

        store.socketEventBus.registerHandler(Constants.SocketEventBusAddress.ADDR_OUT_SESSION, this.onChangeSession.bind(tools));
        store.socketEventBus.registerHandler(Constants.SocketEventBusAddress.BROADCAST_MESSAGE, this.onChangeCableTicket.bind(tools));
        store.socketEventBus.registerHandler(Constants.SocketEventBusAddress.BROADCAST_TICKET, this.onChangeTicket.bind(tools));
        store.socketEventBus.registerHandler(Constants.SocketEventBusAddress.BROADCAST_SYSLOG, this.onChangeSyslog.bind(tools));
        store.socketEventBus.registerHandler(Constants.SocketEventBusAddress.BROADCAST_HEATBEAT, function (error, message) {
            let msg = `HeatBeat Check ${message.body.data}`;
            if (tools.debug) {
                tools.log(msg);
            }
        });
        store.socketEventBus.registerHandler(Constants.SocketEventBusAddress.BROADCAST_UPDATE_STATUS, function (error, message) {
            let msg = `Update Status Check ${message.body.date} [ ${message.body.result} ]`;
            if (tools.debug) {
                tools.log(msg);
            }
        });
        store.socketEventBus.registerHandler('out', function (error, message) {
            tools.showAlert('관리자 Message', message.body.data);
        });
    }

    onclose(retry = true) {
        let tools = this;
        tools.clearConnectTimer();
        if (retry && tools.store.auth) {
            tools.tryDeviceConnect();
        }
    }

    clearConnectTimer() {
        let tools = this;
        if (tools.connectTimer) {
            clearTimeout(this.connectTimer);
            tools.connectTimer = null;
        }
    }

    tryDeviceConnect(retry, toast) {
        let tools = this;
        // if(tools.store.auth != true) {
        //     recusiveConnnectTry();
        //     return;
        // }

        retry = (retry || 0) + 1;
        tools.log(`try connection server, retry=${retry}`);

        tools.store.socketEventBus = new EventBus(`//${tools.store.socketServerHost}:${tools.store.socketServerPort}/eventbus`);
        tools.store.socketEventBus.onopen = () => {
            tools.showToast('서버와 연결되었습니다.');
            tools.$rootScope.$broadcast('onViewEvent', { type: Constants.ViewEventType.SOCKET_OPENED });

            tools.clearConnectTimer();
            tools.onopen();
            tools.store.socketEventBus.onclose = tools.onclose.bind(tools);
            retry = 0;
        }

        function recusiveConnnectTry(retry) {
            tools.connectTimer = setTimeout(function () {
                if (tools.connectTimer) {
                    tools.tryDeviceConnect(retry, toast);
                } else {
                    tools.onclose(true);
                }
            }, 10 * 1000);
        }

        tools.store.socketEventBus.onclose = () => {
            if (!toast) {
                toast = tools.showToast('서버와 접속이 끊어졌습니다. 접속을 시도합니다.', null, null, 0);
            }

            try {
                let eventbus = tools.store.socketEventBus;
                if (eventbus /*&& eventbus.state == 1*/) {
                    eventbus.sockJSConn.close();
                    tools.store.socketEventBus = null;
                }
            } catch (e) {
            }
            recusiveConnnectTry(retry);
        }
    }

    elementBlink(element = $(), repeatCount) {
        let { $timeout } = this;
        var count = typeof (repeatCount) == 'number' ? repeatCount : 1;
        for (var i = 0; i < count; i++) {
            $timeout(() => {
                element.hide();
                $timeout(() => {
                    element.show();
                }, 100);
            }, i * 300);
        }
    };

    getTicketType(code) {
        var obj = null;
        angular.forEach(Constants.TicketType, (v, k) => {
            if (code == v.code) {
                obj = v;
                return false;
            }
        });
        return obj || Constants.TicketType.Etc;
    }

    getTicketStatus(code) {
        var obj = null;
        angular.forEach(Constants.TicketStatus, (v, k) => {
            if (code == v.code) {
                obj = v;
                return false;
            }
        });
        return obj;
    }

    getDefaultStartDate() {
        let filterStorage = tools.filterStorage || this.filterStorage;
        return filterStorage.loadFilter().then((options) => {
            return new Date().setHours(0, 0, 0, 0) - new Date(1000 * 60 * 60 * 24 * options.maxDays);
        });
    }

    downloadServerFile(url, newFileName = null) {
        var path = 'http://' + window.location.host + '/' + url;
        var a = document.createElement('A');
        a.href = encodeURI(path);
        if (newFileName) { a.download = newFileName; }
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
    }

    // EXPORT_EXCEL 서비스 콜백 함수/ 혹은 콜백 함수 내에서 호출
    fnExportExcelSuccess(result, newFileName = null) {
        if (!result || !result.excelUrl) {
            this.$log.info('조회된 데이터가 없습니다.');
            this.showAlert('', '조회된 데이터가 없습니다.');
            return;
        }
        else {
            let excelUrl = 'excel/' + result.excelUrl;
            this.downloadServerFile(excelUrl, newFileName);
        }
    }

    humanReadableFileSize(size) {
        var units = ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
        var i = 0;
        while (size >= 1024) {
            size /= 1024;
            ++i;
        }
        return size.toFixed(1) + ' ' + units[i];
    }

    getDefaultStartDateTime() {
        let tools = this;
        let maxDays = tools.filterStorage.filter.maxDays;
        return new Date().setHours(0, 0, 0, 0) - new Date(1000 * 60 * 60 * 24 * maxDays);
    }

    applyDateFilter() {
        let tools = this;
        let defaultStart = tools.getDefaultStartDateTime();
        if (tools.store.ticketSearchDate.start != '' && tools.store.ticketSearchDate.start < defaultStart) {
            tools.store.ticketSearchDate.start = defaultStart;
        }
        tools.store.ticketSearchDate.start = new Date(tools.store.ticketSearchDate.start).getTime() || defaultStart;
        tools.store.ticketSearchDate.end = new Date(tools.store.ticketSearchDate.end).getTime() || '';

        angular.copy(tools.store.ticketSearchDate, tools.store.filterStorage.filter.date);
        tools.store.filterStorage.saveFilter(tools.store.filterStorage.filter);

        tools.loadTicket();
        tools.showToast('설정 되었습니다');
    }

    /** 월보드용 티켓 elSize 세팅 */
    setTicketElsize(type, ticket) {
        let tools = this;
        switch (type) {
            case tools.constants.RcaTicketType.TICKET.code:
                case1();
                break;
            case tools.constants.RcaTicketType.INSPECTOR.code:
                case2();
                break;
        }

        function case1() {
            let elSize = (ticket.tc_list ? tools.constants.TicketEleStatus.OPEN.size :
                (tools.store.ticketExpandMode ? tools.constants.TicketEleStatus.EXPAND.size : tools.constants.TicketEleStatus.NORMAL.size));
            Object.assign(ticket, { 'ticket_el_size': elSize });
        }
        function case2() {
            Object.assign(ticket, { 'ticket_el_size': tools.constants.TicketEleStatus.EXPAND.size });
        }

    }

    /** 티켓 전표지속시간 */
    calculateDuration(ticket) {
        let tools = this;
        if (['INIT', 'ACK'].includes(ticket.status)) {
            let resultTime = new Date().getTime() - new Date(ticket.ticket_generation_time).getTime();
            return tools.store.formatSecToTime(resultTime > 0 ? resultTime : 0);
        } else if (['AUTO_FIN', 'FIN'].includes(ticket.status)) {
            let resultTime = new Date(ticket.handling_time).getTime() - new Date(ticket.ticket_generation_time).getTime();
            return tools.store.formatSecToTime(resultTime > 0 ? resultTime : 0);
        } else {
            return '0분';
        }
    };

    /** 케이블 티켓 전표지속시간 */
    calculateDuration2(ticket) {
        let tools = this;
        if (['INIT', 'ACK'].includes(ticket.status)) {
            let resultTime = new Date().getTime() - new Date(ticket.ticket_generation_time || ticket.update_time).getTime();
            return tools.store.formatSecToTime(resultTime > 0 ? resultTime : 0);
        } else if (['AUTO_FIN', 'FIN'].includes(ticket.status)) {
            let resultTime = new Date(ticket.clear_time).getTime() - new Date(ticket.ticket_generation_time || ticket.update_time).getTime();
            return tools.store.formatSecToTime(resultTime > 0 ? resultTime : 0);
        } else {
            return '0분';
        }
    }

    /**
     * 티켓 인지/마감 룰
     * @param options 작업 선택 옵션 (결과값 타입)
     * - VISIBLE : 버튼 활성 여부(bool)
     * - MODIFIED : 수정창 여부(bool)
     * - CODE : Processing 코드값(string)
     * @param type 작업 타입 (ACK or FIN)
     * @param ticket 티켓(or Inspector) 오브젝트
     */
    ticketProcessingRule(options, type, ticket) {
        if (!ticket || !ticket.status || ticket.ticket_type == 'SUB_ALM') { return false; }

        let tools = this;
        const TicketStatus = tools.constants.TicketStatus;
        const TicketProcessing = tools.constants.TicketProcessing;
        const OPTIONS = {
            VISIBLE: 'VISIBLE',
            MODIFIED: 'MODIFIED',
            CODE: 'CODE'
        };

        const INIT = TicketStatus.INIT.code;
        const ACK = TicketStatus.ACK.code;
        const FIN = TicketStatus.FIN.code;
        const AUTO_FIN = TicketStatus.AUTO_FIN.code;

        switch (options + type) {
            case OPTIONS.VISIBLE + TicketProcessing.ACK.code:           /* 인지버튼 활성 여부 */
                return case1(ticket.status);
            case OPTIONS.VISIBLE + TicketProcessing.FIN.code:           /* 마감버튼 활성 여부 */
                return case2(ticket.status);
            case OPTIONS.MODIFIED + TicketProcessing.ACK.code:          /* 인지버튼의 수정창 여부 */
                return case3(ticket.status);
            case OPTIONS.MODIFIED + TicketProcessing.FIN.code:          /* 마감버튼의 수정창 여부 */
                return case4(ticket.status);
            case OPTIONS.CODE + TicketProcessing.ACK.code:              /* 인지버튼 processing 코드 */
                return case5(ticket.status);
            case OPTIONS.CODE + TicketProcessing.FIN.code:              /* 마감버튼 processing 코드 */
                return case6(ticket.status);
            case OPTIONS.CODE + TicketProcessing.INSPECTOR_ACK.code:    /* 선로장애 인지버튼 processing 코드 */
                return case7(ticket.status);
            case OPTIONS.CODE + TicketProcessing.INSPECTOR_FIN.code:    /* 선로장애 마감버튼 processing 코드 */
                return case8(ticket.status);
            default:
                console.error('option 파라미터 입력값 오류');
                return false;
        }

        function case1(status) {
            return [INIT, ACK].includes(status);
        }
        function case2(status) {
            return [ACK, FIN, AUTO_FIN].includes(status);
        }
        function case3(status) {
            return status == ACK;
        }
        function case4(status) {
            return status == FIN;
        }
        function case5(status) {
            if (status == ACK) { return tools.constants.TicketProcessing.ACK_MODIFIED.code; }
            else if (status == INIT) { return tools.constants.TicketProcessing.ACK.code; }
        }
        function case6(status) {
            if (status == FIN) { return tools.constants.TicketProcessing.FIN_MODIFIED.code; }
            else if ([ACK, AUTO_FIN].includes(status)) { return tools.constants.TicketProcessing.FIN.code; }
        }
        function case7(status) {
            if (status == ACK) { return tools.constants.TicketProcessing.INSPECTOR_ACK_MODIFIED.code; }
            else if (status == INIT) { return tools.constants.TicketProcessing.INSPECTOR_ACK.code; }
        }
        function case8(status) {
            if (status == FIN) { return tools.constants.TicketProcessing.INSPECTOR_FIN_MODIFIED.code; }
            else if ([ACK, AUTO_FIN].includes(status)) { return tools.constants.TicketProcessing.INSPECTOR_FIN.code; }
        }
    }

    /** 티켓 처리, 상태변경 */
    ticketProcessing(ticket, event, work) {
        let { $mdDialog, $mdSelect } = this;
        let tools = this;
        let processing = tools.constants.TicketProcessing[work] || {};
        let page = tools.store.viewType[processing.view];

        if (!ticket || !page) {
            console.error('파라미터 입력값 오류');
            return;
        }

        return $mdDialog.show({
            locals: {
                param: { ticket: (Array.isArray(ticket) ? ticket : [ticket]), processing: processing }
            },
            controller: page.controller,
            templateUrl: page.path,
            parent: angular.element(document.body),
            targetEvent: event,
            clickOutsideToClose: true,
            bindToController: true,
            autoWrap: false,
            fullscreen: false, // Only for -xs, -sm breakpoints.
            onRemoving: function () {
                $mdSelect.hide();
            }
        });
    };

    showPage(page) {
        // tools.statego(tools.store.viewType.window_map3d.type);
        // tools.showPage(tools.store.viewType.window_map3d.type);
        // tools.showPage(tools.store.viewType.influence_table_template.type);

        this.$debug("showPage: " + page);
        this.$state.go(page);
    }

    encryptUrl(data = null) {
        let encrypt = new Encrypt(null, JSON.stringify(data));
        let encryptText = encrypt.key + encrypt.data;
        let url = location.origin + "/?s=" + encryptText;
        return url;
    }

    openNewTab(data = null) {
        if (!this.debug && !data) {
            return;
        }

        // var tools = $tools;tools.openNewTab();

        // if (!data) {
        //     let {tools} = this;
        //     let ticketId = '1923989';
        //     data = {
        //         view: tools.store.viewType.monitoring_tt.type,
        //         TICKET_ID: ticketId,
        //         MAP_TYPE: tools.constants.MapType.LAYERED,
        //     };
        // }

        if (!data) {
            let { tools } = this;
            let ticketId = '1939811';
            data = {
                view: tools.store.viewType.monitoring_tt.type,
                TICKET_ID: ticketId,
                MAP_TYPE: tools.constants.MapType.CABLE,
                INDEX: 0
            };
        }

        let url = this.encryptUrl(data);
        var tab = window.open(url, '_blank');

        if (this.debug) {
            console.log(`data=`, data);
            console.log(`url=`, url);
        }
        tab.focus();
    }

    copyToUrl(data = null) {
        /*exam
        var tools = $tools;
        tools.copyToUrl();
        tools.copyToUrl({view: tools.store.viewType.window_map3d.type});
        tools.copyToUrl({view: tools.store.viewType.control_monitoring_tt_influencecircuit_table.type});
        */

        if (!this.debug && !data) {
            return;
        }

        /* 토폴로지 */
        if (!data) {
            let { tools } = this;
            let ticketId = '1923989';
            data = {
                view: tools.store.viewType.monitoring_tt.type,
                TICKET_ID: ticketId,
                MAP_TYPE: tools.constants.MapType.LAYERED,
                INDEX: 0
            };
        }

        /* 영향회선 */
        // if (!data) {
        //     let {tools} = this;
        //     let ticketId = '1923989';
        //     data = {
        //         view: tools.store.viewType.influence_table_template.type,
        //         TICKET_ID: ticketId
        //     };
        // }

        let url = this.encryptUrl(data);
        this.copyToClipboard(url);
        if (this.debug) {
            console.log(`data=`, data);
            console.log(`url=`, url);
        }
    }

    copyToClipboard(value) {
        let $temp_input = $("<input>");
        $("body").append($temp_input);
        $temp_input.val(value).select();
        document.execCommand("copy");
        $temp_input.remove();
    }

    decryptView(encryptParams) {
        this.$debug("encryptParams:", encryptParams);
        let encrypt = new Encrypt();
        let key = encryptParams.substring(0, 16);
        let encryptText = encryptParams.substring(16, encryptParams.length);

        let data = null;
        try {
            let decrypt = encrypt.decrypt(encryptText, key);
            data = JSON.parse(decrypt);
            this.singlePage = true;
            this.singlePageData = data;
        } catch (e) {
            this.$debug(e);
        }
        return data == null ? null : data.view;
    }

    /*getCookie(name) {
        var value = document.cookie.match('(^|;) ?' + name + '=([^;]*)(;|$)');
        return value ? value[2] : null;
    };

    setCookie(name, value, day) {
        var date = new Date();
        date.setTime(date.getTime() + (day || 1) * 60 * 60 * 24 * 1000);
        document.cookie = name + '=' + value + ';expires=' + date.toUTCString() + ';path=/';
    };*/

    convertNiaNodeId(val = '') {
        // if(val.includes('-')) {
        //     return val.substr(0, val.indexOf('-'));
        // }
        // return val
        return val.replace(/(\d+\.\d+\.\d+\.\d+)-\w+/gi, '$1')
    }

    /** For Check */
    getStorageSize(storage = localStorage) {
        var _lsTotal = 0,
            _xLen, _x;
        for (_x in storage) {
            if (!storage.hasOwnProperty(_x)) {
                continue;
            }
            _xLen = ((storage[_x].length + _x.length) * 2);
            _lsTotal += _xLen;
            console.log(_x.substr(0, 50) + " = " + (_xLen / 1024).toFixed(2) + " KB")
        };
        console.log("Total = " + (_lsTotal / 1024).toFixed(2) + " KB");
    }

    objectToParam(obj) {
        var str = [];
        for (var p in obj)
            if (obj.hasOwnProperty(p)) {
                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
            }
        return str.join("&");
    }

    /** RESTful API for IP-SDN Request */
    /* ex) $tools.sdnRequest('POST', 'ipsdn/opt/daydata', {sourcedate:'2022-10-12'}).then(result => {debugger;}) */
    async sdnRequest(method = 'GET', path, parameter) {
        if (!path) return
        let { tools } = this;
        return new Promise(async (resolve, reject) => {
            const param = parameter ? `?${tools.objectToParam(parameter)}` : ''
            try {
                await tools.http({
                    service: tools.constants.Service.rca,
                    action: "REQUEST_IPSDN_API",
                    METHOD: method,
                    PATH: path,
                    PARAM: param
                }, result => resolve(result));
            } catch (error) {
                reject();
            }
        })
    };

    /** RESTful API Request */
    restfulRequest(method, host, port, service, param, callback = (res => { })) {
        let { tools } = this;
        if (method == 'help') {
            // return "$tools.restfulRequest('POST','jsonplaceholder.typicode.com','80','posts','', res => {debugger})";
            return "$tools.restfulRequest('POST','127.0.0.1','8080','tsdn/selfconfig','?result=success&ticket_id=55', res => {})";
        }
        /* For T-SDN INPUT TEST */
        if (method == 'tsdn') {
            method = 'POST';
            host = '127.0.0.1';
            port = '8080';
            service = 'tsdn/selfconfig';
            param = '?result=' + (host == false ? 'fail' : 'success') + '&ticket_id=55';
        }
        tools.http({
            service: tools.constants.Service.rca,
            action: Constants.Action.REQUEST_RESTFUL,
            METHOD: method,
            HOST: host,
            PORT: port,
            SERVICE: service,
            PARAM: param
        }, callback);
    }
    /** RESTful API Request */
    restfulHttpsRequest(method, host, port, service, param, callback = (res => { })) {
        let { tools } = this;
        if (method == 'help') {
            // return "$tools.restfulRequest('POST','jsonplaceholder.typicode.com','80','posts','', res => {debugger})";
            return "$tools.restfulHttpsRequest('POST','127.0.0.1','8080','tsdn/selfconfig', {}, res => {})";
        }

        tools.http({
            service: tools.constants.Service.rca,
            action: Constants.Action.REQUEST_HTTPS_RESTFUL,
            METHOD: method,
            HOST: host,
            PORT: port,
            SERVICE: service,
            PARAM: param
        }, callback);
    }
    /** POST Request */
    postRequest(url, param, callback = (res => { })) {
        let { tools } = this;
        if (url == 'help') {
            return "$tools.postRequest('http://jsonplaceholder.typicode.com:80/posts','', res => {debugger})";
        }
        tools.http({
            service: tools.constants.Service.rca,
            action: Constants.Action.REQUEST_POST,
            URL: url,
            PARAM: param
        }, callback);
    }

    /** RESTful API Request */
    simulatorRequest(service, param, callback = (res => { })) {
        let { tools } = this;
        if (service == 'help') {
            // return "$tools.restfulRequest('POST','jsonplaceholder.typicode.com','80','posts','', res => {debugger})";
            return "$tools.simulatorRequest('restconf/operations/koren-tsdn-nbi-service:reserve-e2e-service', {}, res => {})";
        }

        tools.http({
            service: tools.constants.Service.rca,
            action: Constants.Action.REQUEST_SIMULATOR,
            SERVICE: service,
            PARAM: param
        }, callback);
    }

    addTicket() {
        let { tools } = this;
        tools.store.niaList.push(
            {
                "ticket_type": "ATT2",
                "root_cause_sysnamez": "seoul-7712",
                "is_backbone": false,
                "child_count": 1,
                "root_cause_sysnamea": "gwangju-7712",
                "ticket_id": "1134930",
                "root_cause_type": "TrafficFail",
                "ticket_rca_result_orig_dtl_code": "이상 트래픽(링크)",
                "ip_addra": "116.89.169.41",
                "is_organ_link": false,
                "ticket_update_time": 1669820833000,
                "fault_time": 1669819969000,
                "is_organ_system": false,
                "root_cause_portz": "ce4/1",
                "ticket_generation_time": 1669820534705,
                "total_related_alarm_cnt": 1,
                "root_cause_porta": "ce1/1",
                "root_cause_domain": "TRAFFIC",
                "ticket_rca_result_dtl_code": "이상 트래픽(링크)",
                "status": "INIT",
                "$$hashKey": "object:422"
            }
        )
    }

    get constants() {
        return {
            ServicePath: Constants.ServicePath,
            Service: Constants.Service,
            Action: Constants.Action,
            EquipType: Constants.EquipType,
            TicketType: Constants.TicketType,
            TicketStatus: Constants.TicketStatus,
            TicketProcessing: Constants.TicketProcessing,
            CableTicketStatus: Constants.CableTicketStatus,
            TicketResultCode: Constants.TicketResultCode,
            TicketEleStatus: Constants.TicketEleStatus,
            SocketEventBusAddress: Constants.SocketEventBusAddress,
            SocketEventType: Constants.SocketEventType,
            ViewEventType: Constants.ViewEventType,
            Properties: Constants.Properties,
            TopologyLoadType: Constants.TopologyLoadType,
            AdditionalTicketStatus: Constants.AdditionalTicketStatus,
            UserGrant: Constants.UserGrant,
            TicketOrg: Constants.TicketOrg,
            LinkAlarmPrediction: Constants.LinkAlarmPrediction,
            IndexedDB: Constants.IndexedDB,
            MapType: Constants.MapType,
            MonitoringType: Constants.MonitoringType,
            RcaTicketType: Constants.RcaTicketType,
            LteEquipType: Constants.LteEquipType,
            Message: Constants.Message,
            SimulatorService: Constants.SimulatorService,
        };
    }
}
;

export default ['$injector', tools.Factory]


