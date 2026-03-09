import AppMainCtrl from '../controller/appMainCtrl';
import AppSubCtrl from '../controller/appSubCtrl';
import LoginCtrl from '../../pages/login/controller.js';

import MainPageCtrl from '../../pages/main/controller';
import MonitoringTTCtrl from '../../pages/main/monitoringTT/controller.js';
import RcaTicketCtrl from '../../pages/main/monitoringTT/rcaTicket/controller.js';
import SyslogCtrl from '../../pages/main/monitoringTT/syslog/controller.js';
import SyslogDialogCtrl from '../../pages/main/monitoringTT/syslog_dialog/controller.js';
import TicketAckDialogCtrl from '../../pages/main/monitoringTT/template_dialog/ticket_ack_template/controller.js';
import TicketFinDialogCtrl from '../../pages/main/monitoringTT/template_dialog/ticket_fin_template/controller.js';

import Map3dCtrl from '../../pages/main/monitoringTT/map3d/controller.js';

import AIProcessCtrl from '../../pages/main/aiProcess/controller.js';
import SelfConfigurationCtrl from '../../pages/main/aiProcess/selfConfiguration/controller.js';
import SelfHealingCtrl from '../../pages/main/aiProcess/selfHealing/controller.js';
import AiHistoryCtrl from '../../pages/main/aiProcess/aiHistory/controller.js';

import AIProcessDialogCtrl from '../../pages/main/aiProcess_template/controller.js';
import SelfConfigurationDialogCtrl from '../../pages/main/aiProcess_template/selfConfiguration_template/controller.js';
import SelfHealingDialogCtrl from '../../pages/main/aiProcess_template/selfHealing_template/controller.js';
import AiHistoryDialogCtrl from '../../pages/main/aiProcess_template/aiHistory_template/controller.js';
import ConfigTestActionDialogCtrl from '../../pages/main/aiProcess_dialog_template/configTestAction/controller.js';
import BypassRouteListDialogCtrl from '../../pages/main/aiProcess_dialog_template/bypassRouteList/controller.js';

import FinProcessDialogCtrl from '../../pages/main/aiProcess_dialog_template/fin_process_dialog/controller.js';

import OptimalRouteCountCtrl from '../../pages/main/optimalRouteCount/controller.js';
import OptimalRouteSettingCtrl from '../../pages/main/optimalRouteSetting/controller.js';

import ByAgencyStatisticsSkillCtrl from '../../pages/main/byAgencyStatisticsSkill/controller.js';
import ByCountryStatisticsSkillCtrl from '../../pages/main/byCountryStatisticsSkill/controller.js';
import ByApplicationStatisticsSkillCtrl from '../../pages/main/byApplicationStatisticsSkill/controller.js';

import UnverifiedTrafficListCtrl from '../../pages/main/unverified_traffic/unverified_traffic_list/controller.js';
import UnverifiedTrafficDetailCtrl from '../../pages/main/unverified_traffic/unverified_traffic_detail/controller.js';

import ApplicationAddCtrl from '../../pages/main/unverified_traffic/application_add/controller.js';
import TrafficAgencyAddCtrl from '../../pages/main/unverified_traffic/agency_add/controller.js';

import AlarmManagementCtrl from '../../pages/main/alarm_management/controller.js';
import SyslogRuleManagementCtrl from '../../pages/main/syslog_rule_management/controller.js';

import EquipByPortCtrl from '../../pages/main/equip_by_port/controller.js';
import EquipAmountUsedCtrl from '../../pages/main/equip_amount_used/controller.js';

import SOPEditListCtrl from '../../pages/main/aiProcess_dialog_template/SOP/SOPEditList/controller.js';
import SOPEditCtrl from '../../pages/main/aiProcess_dialog_template/SOP/SOPEdit/controller.js';

import SOPCtrl from '../../pages/main/SOP/SOP/controller.js';
import SOPDetailCtrl from '../../pages/main/SOP/SOPDetail/controller.js';
import syslogSOPDetailCtrl from '../../pages/main/SOP/syslogSOPDetail/controller.js';

import UserSettingCtrl from '../../pages/main/userSetting/controller.js';
import AdministratorDialogCtrl from '../../pages/main/administrator/controller.js';
import DateSettingCtrl from '../../pages/main/dateSetting/controller.js';

import NodeListCtrl from '../../pages/management/node/node_list/controller.js';
import NodeAddCtrl from '../../pages/management/node/node_add/controller.js';
import NodeDetailCtrl from '../../pages/management/node/node_detail/controller.js';
import SyslogRuleDetailCtrl from '../../pages/main/monitoringTT/syslog_dialog/syslog_rule_detail/controller.js';
import SyslogDetailCtrl from '../../pages/main/monitoringTT/syslog_dialog/syslog_detail/controller.js';
import SyslogConfigurationDialogCtrl from '../../pages/main/monitoringTT/syslog_dialog/syslogConfiguration_template/controller.js';
import SelfProcessEmailDialogCtrl from '../../pages/main/monitoringTT/selfProcess/selfProcess_email_dialog/controller.js';
import PortListCtrl from '../../pages/management/node/port_list/controller.js';
import PortDetailCtrl from '../../pages/management/node/port_detail/controller.js';
import LinkListCtrl from '../../pages/management/link/link_list/controller.js';
import LinkAddCtrl from '../../pages/management/link/link_add/controller.js';
import LinkDetailCtrl from '../../pages/management/link/link_detail/controller.js';
import AgencyListCtrl from '../../pages/management/agency/agency_list/controller.js';
import AgencyAddCtrl from '../../pages/management/agency/agency_add/controller.js';
import AgencyDetailCtrl from '../../pages/management/agency/agency_detail/controller.js';
import ProFileListCtrl from '../../pages/management/proFile/proFile_list/controller.js';
import RecoveryCtrl from '../../pages/management/proFile/proFile_recovery/controller.js';
import RecoveryDetailCtrl from '../../pages/management/proFile/proFile_recovery_detail/controller.js';

import AiMonitorCtrl from '../../pages/main/monitoringTT/aiMonitor/controller.js';
import ServerMonitorCtrl from '../../pages/main/monitoringTT/serverMonitor/controller.js';
import DashboardCtrl from '../../pages/main/monitoringTT/dashboard/controller.js';
import SelfProcessCtrl from '../../pages/main/monitoringTT/selfProcess/controller.js';
import SelfProcessDialogCtrl from '../../pages/main/monitoringTT/selfProcess/selfProcess_dialog/controller.js';



function controllers() {
    this.controller('AppMainCtrl', AppMainCtrl);
    this.controller('AppSubCtrl', AppSubCtrl);
    this.controller('LoginCtrl', LoginCtrl);

    this.controller('MainPageCtrl', MainPageCtrl);
    this.controller('SyslogCtrl', SyslogCtrl);
    this.controller('SyslogDialogCtrl', SyslogDialogCtrl);
    this.controller('MonitoringTTCtrl', MonitoringTTCtrl);
    this.controller('RcaTicketCtrl', RcaTicketCtrl);
    this.controller('TicketAckDialogCtrl', TicketAckDialogCtrl);
    this.controller('TicketFinDialogCtrl', TicketFinDialogCtrl);

    this.controller('Map3dCtrl', Map3dCtrl);

    this.controller('AIProcessCtrl', AIProcessCtrl);
    this.controller('SelfConfigurationCtrl', SelfConfigurationCtrl);
    this.controller('SelfHealingCtrl', SelfHealingCtrl);
    this.controller('AiHistoryCtrl', AiHistoryCtrl);
    this.controller('ConfigTestActionDialogCtrl', ConfigTestActionDialogCtrl);
    this.controller('BypassRouteListDialogCtrl', BypassRouteListDialogCtrl); 
    this.controller('FinProcessDialogCtrl', FinProcessDialogCtrl);

    this.controller('AIProcessDialogCtrl', AIProcessDialogCtrl);
    this.controller('SelfConfigurationDialogCtrl', SelfConfigurationDialogCtrl);
    this.controller('SelfHealingDialogCtrl', SelfHealingDialogCtrl);
    this.controller('AiHistoryDialogCtrl', AiHistoryDialogCtrl);

    this.controller('OptimalRouteCountCtrl', OptimalRouteCountCtrl);
    this.controller('OptimalRouteSettingCtrl', OptimalRouteSettingCtrl);

    this.controller('ByAgencyStatisticsSkillCtrl', ByAgencyStatisticsSkillCtrl);
    this.controller('ByCountryStatisticsSkillCtrl', ByCountryStatisticsSkillCtrl);
    this.controller('ByApplicationStatisticsSkillCtrl', ByApplicationStatisticsSkillCtrl);

    this.controller('UnverifiedTrafficListCtrl', UnverifiedTrafficListCtrl);
    this.controller('UnverifiedTrafficDetailCtrl', UnverifiedTrafficDetailCtrl);
    this.controller('ApplicationAddCtrl', ApplicationAddCtrl);
    this.controller('TrafficAgencyAddCtrl', TrafficAgencyAddCtrl);

    this.controller('AlarmManagementCtrl', AlarmManagementCtrl);
    this.controller('SyslogRuleManagementCtrl', SyslogRuleManagementCtrl);

    this.controller('EquipByPortCtrl', EquipByPortCtrl);
    this.controller('EquipAmountUsedCtrl', EquipAmountUsedCtrl);

    this.controller('SOPEditListCtrl', SOPEditListCtrl);
    this.controller('SOPEditCtrl', SOPEditCtrl);

    this.controller('SOPCtrl', SOPCtrl);
    this.controller('SOPDetailCtrl', SOPDetailCtrl);
    this.controller('syslogSOPDetailCtrl', syslogSOPDetailCtrl);

    this.controller('SelfProcessEmailDialogCtrl', SelfProcessEmailDialogCtrl);
    this.controller('UserSettingCtrl', UserSettingCtrl);
    this.controller('AdministratorDialogCtrl', AdministratorDialogCtrl);
    this.controller('DateSettingCtrl', DateSettingCtrl);

    this.controller('NodeListCtrl', NodeListCtrl);
    this.controller('NodeAddCtrl', NodeAddCtrl);
    this.controller('NodeDetailCtrl', NodeDetailCtrl);
    this.controller('SyslogDetailCtrl', SyslogDetailCtrl);
    this.controller('SyslogConfigurationDialogCtrl', SyslogConfigurationDialogCtrl);
    this.controller('SyslogRuleDetailCtrl', SyslogRuleDetailCtrl);
    this.controller('PortListCtrl', PortListCtrl);
    this.controller('PortDetailCtrl', PortDetailCtrl);
    this.controller('LinkListCtrl', LinkListCtrl);
    this.controller('LinkAddCtrl', LinkAddCtrl);
    this.controller('LinkDetailCtrl', LinkDetailCtrl);
    this.controller('AgencyListCtrl', AgencyListCtrl);
    this.controller('AgencyAddCtrl', AgencyAddCtrl);
    this.controller('AgencyDetailCtrl', AgencyDetailCtrl);
    this.controller('ProFileListCtrl', ProFileListCtrl);
    this.controller('RecoveryCtrl', RecoveryCtrl);
    this.controller('RecoveryDetailCtrl', RecoveryDetailCtrl);

    this.controller('AiMonitorCtrl', AiMonitorCtrl);
    this.controller('ServerMonitorCtrl', ServerMonitorCtrl);
    this.controller('DashboardCtrl', DashboardCtrl);
    this.controller('SelfProcessCtrl', SelfProcessCtrl);
    this.controller('SelfProcessDialogCtrl', SelfProcessDialogCtrl);
}

export { controllers }
