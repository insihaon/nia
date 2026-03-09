import BaseController from 'scripts/controller/baseController'

class ServerMonitorCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdPanel, $sce) {
        $scope.config = tools.store.viewType.serverMonitor;
        super($injector, $scope, tools, $http, $timeout, $mdPanel, $sce);
        {
            $scope.resourceDataUrl = {
                'CPU': 'http://116.89.191.40:3000/d-solo/LBlvJ7p7z/server_status?orgId=1&refresh=1m&panelId=14',
                'MEMORY': 'http://116.89.191.40:3000/d-solo/LBlvJ7p7z/server_status?orgId=1&refresh=1m&panelId=12'
            };

            $scope.fileDataUrl = {
                'IP1': 'http://116.89.191.40:3000/d-solo/1edhLePnk/file_size_week?orgId=1&panelId=1',
                'IP2': 'http://116.89.191.40:3000/d-solo/1edhLePnk/file_size_week?orgId=1&panelId=5',
                'IP3': 'http://116.89.191.40:3000/d-solo/1edhLePnk/file_size_week?orgId=1&panelId=7',
                '전송1': 'http://116.89.191.40:3000/d-solo/1edhLePnk/file_size_week?orgId=1&panelId=2',
                '전송2': 'http://116.89.191.40:3000/d-solo/1edhLePnk/file_size_week?orgId=1&panelId=3',
                '전송3': 'http://116.89.191.40:3000/d-solo/1edhLePnk/file_size_week?orgId=1&panelId=4',
                '전송4': 'http://116.89.191.40:3000/d-solo/1edhLePnk/file_size_week?orgId=1&panelId=8',
                '전송5': 'http://116.89.191.40:3000/d-solo/1edhLePnk/file_size_week?orgId=1&panelId=9',
                '전송6': 'http://116.89.191.40:3000/d-solo/1edhLePnk/file_size_week?orgId=1&panelId=10',
                'Sflow': 'http://116.89.191.40:3000/d-solo/88QEOB2nz/file_size?orgId=1&panelId=12',
                '광세기': 'http://116.89.191.40:3000/d-solo/88QEOB2nz/file_size?orgId=1&panelId=6',
                '트래픽': 'http://116.89.191.40:3000/d-solo/88QEOB2nz/file_size?orgId=1&panelId=11'
            };

            $scope.fileData3HoursUrl = {
                '장비': 'http://116.89.191.40:3000/d-solo/88QEOB2nz/file_size?orgId=1&refresh=1m&panelId=22',
                '경로': 'http://116.89.191.40:3000/d-solo/88QEOB2nz/file_size?orgId=1&refresh=1m&panelId=21',
                'ipSdnSflow': 'http://116.89.191.40:3000/d-solo/88QEOB2nz/file_size?orgId=1&refresh=1m&panelId=32',
                'ipSdnSyslog': 'http://116.89.191.40:3000/d-solo/88QEOB2nz/file_size?orgId=1&refresh=1m&panelId=34',
                'ipSdnSyslogAlarm': 'http://116.89.191.40:3000/d-solo/88QEOB2nz/file_size?orgId=1&refresh=1m&panelId=36',
            };

            $scope.fileData1DayUrl = {
                'link': 'http://116.89.191.40:3000/d-solo/1edhLePnk/file_size_week?orgId=1&refresh=1h&panelId=18',
                'node': 'http://116.89.191.40:3000/d-solo/1edhLePnk/file_size_week?orgId=1&refresh=1h&panelId=19',
                'interface': 'http://116.89.191.40:3000/d-solo/1edhLePnk/file_size_week?orgId=1&refresh=1h&panelId=20'
            };

            $scope.collectDataUrl = {
                '광세기': 'http://116.89.191.40:3000/d-solo/XB7JYac7z/koren_data_all?orgId=1&panelId=14',
                '트래픽': 'http://116.89.191.40:3000/d-solo/XB7JYac7z/koren_data_all?orgId=1&panelId=16',
                'Sflow': 'http://116.89.191.40:3000/d-solo/XB7JYac7z/koren_data_all?orgId=1&panelId=18'
            };

            $scope.dataValueUrl = {
                '경로': 'http://116.89.191.40:3000/d-solo/XB7JYac7z/koren_data_all?orgId=1&panelId=20',
                '장비': 'http://116.89.191.40:3000/d-solo/XB7JYac7z/koren_data_all?orgId=1&panelId=24',
                'ipSdnSyslog': 'http://116.89.191.40:3000/d-solo/XB7JYac7z/koren_data_all?orgId=1&panelId=21',
                'ipSdnSyslogAlarm': 'http://116.89.191.40:3000/d-solo/XB7JYac7z/koren_data_all?orgId=1&panelId=26',
                'ipSdnSflow': 'http://116.89.191.40:3000/d-solo/XB7JYac7z/koren_data_all?orgId=1&panelId=25',
            }

            $scope.reloadData = (type) => {
                const iframe = {
                    resource: ['iframe1', 'iframe2'],
                    file: ['iframe3', 'iframe4', 'iframe5', 'iframe7', 'iframe8', 'iframe9', 'iframe10', 'iframe11', 'iframe12', 'iframe13', 'iframe14', 'iframe15'],
                    file3Hour: ['iframe101', 'iframe102', 'iframe103', 'iframe104', 'iframe105'],
                    file1Day: ['iframe201', 'iframe202', 'iframe203'],
                    collect: ['iframe6'],
                    dataValue: ['iframe301', 'iframe302', 'iframe303', 'iframe304', 'iframe305'],
                };

                const elements = iframe[type];
                elements && elements.forEach(id => {
                    const element = document.getElementById(id);
                    if (element) {
                        element.src = element.src;
                    }
                });
            }

            $scope.getResourceDataUrl = (type = 'CPU') => {
                let { resourceDataUrl } = $scope;
                const url = resourceDataUrl[type] || '';
                return $sce.trustAsResourceUrl(url)
            }


            $scope.getFileDataUrl = (type = 'Sflow') => {
                let { fileDataUrl } = $scope;
                const url = fileDataUrl[type] || '';
                return $sce.trustAsResourceUrl(url)
            }

            $scope.getFileData3HoursUrl = (type) => {
                let { fileData3HoursUrl } = $scope;
                const url = fileData3HoursUrl[type] || '';
                return $sce.trustAsResourceUrl(url)
            }

            $scope.getFileData1DayUrl = (type) => {
                let { fileData1DayUrl } = $scope;
                const url = fileData1DayUrl[type] || '';
                return $sce.trustAsResourceUrl(url)
            }
            
            $scope.getCollectDataUrl = () => {
                let { collectDataUrl, dataType } = $scope;
                const url = collectDataUrl[dataType] || '';
                return $sce.trustAsResourceUrl(url)
            }

            $scope.getDataValueUrl = (type) => {
                let { dataValueUrl } = $scope;
                const url = dataValueUrl[type] || '';
                return $sce.trustAsResourceUrl(url)
            }
           
        }
    }
}

export default ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdPanel', '$sce', ServerMonitorCtrl];
