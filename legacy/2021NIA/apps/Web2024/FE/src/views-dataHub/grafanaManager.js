export const grafanaSettings = {
    refreshTime: '30s'

}

export function makeGrafanaVariable(isMulti, variableName, modelData) {
    if (isMulti) {
        if (modelData.length > 0) {
            let variable = ''
            modelData.forEach((data) => {
                variable += `&var-${variableName}=` + data
            })
            return variable
        } else {
            return ''
        }
    } else {
        return `&var-${variableName}=` + modelData
    }
}

export function hangleEncoding(text) {
    // 직접적으로 함수를 써도 되는데 현재 따로 사용되는 곳이 없어서 혹시 나중에 필요할까 해서 기록용으로 남김
    return encodeURIComponent(text)
}

export const grafanaDashboardUrlMap = {
    // 애플리케이션 현황용
    sparkAndLogStash: {
        heapMemory: '/d-solo/Ebqos8mIz/apache-spark-sample?orgId=1&theme=light&panelId=8',
        JVMThreads: '/d-solo/Ebqos8mIz/apache-spark-sample?orgId=1&theme=light&panelId=4',
        memoryPool: '/d-solo/Ebqos8mIz/apache-spark-sample?orgId=1&theme=light&panelId=2',
        driverGc: '/d-solo/Ebqos8mIz/apache-spark-sample?orgId=1&theme=light&panelId=6',

        pipelineStatus: '/d-solo/logstash-development/logstash-on-kubernetes-dashboard-sample?orgId=1&panelId=6775',
        eventsProcessTimes: '/d-solo/logstash-development/logstash-on-kubernetes-dashboard-sample?orgId=1&panelId=2641'
    },

    logStash: {
        pipelineStatus: '/d-solo/logstash-development/logstash-on-kubernetes-dashboard-sample?orgId=1&panelId=6775',
        totalEventsInOut: '/d-solo/logstash-development/logstash-on-kubernetes-dashboard-sample?orgId=1&panelId=6897',
        eventsProcessTimes: '/d-solo/logstash-development/logstash-on-kubernetes-dashboard-sample?orgId=1&panelId=2641'
    },

    mongoDB: {
        allocatedStorageToCollectionsDB: '/d-solo/SsEeTs97k/opstree-mongodb-dashboard-sample?orgId=1&theme=light&panelId=37',
        allocatedStorageToIndexsDB: '/d-solo/SsEeTs97k/opstree-mongodb-dashboard-sample?orgId=1&theme=light&panelId=38',

        cpuBasic: '/d-solo/SsEeTs97k/opstree-mongodb-dashboard-sample?orgId=1&theme=light&panelId=4',
        memoryUtilization: '/d-solo/SsEeTs97k/opstree-mongodb-dashboard-sample?orgId=1&theme=light&panelId=6',
        networkIO: '/d-solo/SsEeTs97k/opstree-mongodb-dashboard-sample?orgId=1&theme=light&panelId=12',
        diskIO: '/d-solo/SsEeTs97k/opstree-mongodb-dashboard-sample?orgId=1&theme=light&panelId=10',

        mongoDBUptime: '/d-solo/SsEeTs97k/opstree-mongodb-dashboard-sample?orgId=1&theme=light&panelId=18',
        currentConnections: '/d-solo/SsEeTs97k/opstree-mongodb-dashboard-sample?orgId=1&theme=light&panelId=20',
        totalCreatedConnection: '/d-solo/SsEeTs97k/opstree-mongodb-dashboard-sample?orgId=1&theme=light&panelId=22',
        availableConnection: '/d-solo/SsEeTs97k/opstree-mongodb-dashboard-sample?orgId=1&theme=light&panelId=21',
    },

    spark: {
        heapMemory: '/d-solo/Ebqos8mIz/apache-spark-sample?orgId=1&theme=light&panelId=8',
        JVMThreads: '/d-solo/Ebqos8mIz/apache-spark-sample?orgId=1&theme=light&panelId=4',
        memoryPool: '/d-solo/Ebqos8mIz/apache-spark-sample?orgId=1&theme=light&panelId=2',
        driverGc: '/d-solo/Ebqos8mIz/apache-spark-sample?orgId=1&theme=light&panelId=6'
    },

    kafka: {
        messagesInPerTopic: '/d-solo/i8HLvrkiz/kafka-dashboard-sample?orgId=1&panelId=38',
        bytesInPerTopic: '/d-solo/i8HLvrkiz/kafka-dashboard-sample?orgId=1&panelId=42',
        kafkaLogSizeByTopic: '/d-solo/i8HLvrkiz/kafka-dashboard-sample?orgId=1&panelId=22',
        bytesInOutPerTopic: '/d-solo/i8HLvrkiz/kafka-dashboard?orgId=1&panelId=58'
    },

    nodeExporter: {
        // 노드 사용현황 통계
        nodeTotal: '/d-solo/Vqx5K9zIz/nodeexporterdashboard?orgId=1&panelId=18',

        // CPU
        nodeCpu: '/d-solo/Vqx5K9zIz/nodeexporterdashboard?orgId=1&panelId=4', // 노드별 CPU 사용률
        currentTotalCpu: '/d-solo/Vqx5K9zIz/nodeexporterdashboard?orgId=1&panelId=12', // 모든 노드의 CPU 사용량
        realTimeCpu: '/d-solo/Vqx5K9zIz/nodeexporterdashboard?orgId=1&panelId=30', // 실시간 CPU 정보

        // DISK
        nodeDisk: '/d-solo/Vqx5K9zIz/nodeexporterdashboard?orgId=1&panelId=8',
        currentTotalDisk: '/d-solo/Vqx5K9zIz/nodeexporterdashboard?orgId=1&panelId=14',
        realTimeDisk: '/d-solo/Vqx5K9zIz/nodeexporterdashboard?orgId=1&panelId=20',

        // Memory
        nodeMemory: '/d-solo/Vqx5K9zIz/nodeexporterdashboard?orgId=1&panelId=6',
        currentTotalMemory: '/d-solo/Vqx5K9zIz/nodeexporterdashboard?orgId=1&panelId=16',
        realTimeMemory: '/d-solo/Vqx5K9zIz/nodeexporterdashboard?orgId=1&panelId=28'
    }
}
