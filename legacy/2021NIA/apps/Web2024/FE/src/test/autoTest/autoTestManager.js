let instance = null

export class AutoTestManager {
    static get instance() {
        return new AutoTestManager()
    }

    constructor() {
        if (instance) {
            return instance
        }

        instance = this
        this.autoTestFunctionMap = autoTestFunctionMap
        this.componentTestPage = null
        this.testComponentfilePath = null
        this.testComponent = null
    }

    async runAutoTest(testComponentfilePath, componentTestPage, testComponent) {
        this.testComponentfilePath = testComponentfilePath
        this.testComponent = testComponent
        this.componentTestPage = componentTestPage
        await autoTestFunctionMap[testComponentfilePath].call(this)
    }

    static autoTestManagerReset() {
        instance = null
    }
}

function functionTest(functionName, param) {
    try {
        console.log(functionName + '의 테스트를 시작합니다.')
        param = param || ''
        AutoTestManager.instance.testComponent[functionName].call(param)
        console.log(functionName + '의 테스트를 종료합니다.')
    } catch (e) {
        console.error(
            AutoTestManager.instance.testComponentfilePath + '의 함수를 테스트 하는 도중 에러가 발생했습니다. \n' +
            '함수명 : ' + functionName
        )
    }
}

function setProp(propName, propValue) {
    try {
        if (!Object.hasOwnProperty.call(AutoTestManager.instance.componentTestPage.currentComponentConfig.testProps, propName)) {
            throw new Error('해당 PropName은 해당 컴포넌트에 없습니다. propName: ' + propName)
        }

        AutoTestManager.instance.componentTestPage.changeTestProps({ objectKey: propName, dataValue: propValue })
    } catch (e) {
        console.error(
            AutoTestManager.instance.testComponentfilePath + '의 Prop를 테스트 하는 도중 에러가 발생했습니다. \n' +
            'PropName : ' + propName + '\n' +
            'PropValue : ' + propValue + '\n' +
            '에러사유 : ' + e
        )
    }
}

const autoTestFunctionMap = {
    'src_views-dataHub_components_CompTemplate': () => {
        functionTest('refreshData')

        setProp('title', '123')
        setProp('xynx', '123')
        setProp('isSearch', false)
    }
}

