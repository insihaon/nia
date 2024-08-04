/* text input 체크
 * example : text 속성에 onkeyup='checkInput(this,'IPonly')'
 *             형태로 사용
 */
export function checkInput(event, type) {
    var inputValue = event.currentTarget.value.toString()
    var regexp
    var regexpchg

    if (type === 'IPonly') {
        regexp = /[^a-f0-9:.]/i
        regexpchg = /[^a-f0-9:.]/gi
    } else if (type === 'IP') {
        regexp = /[^a-f0-9:./]/i
        regexpchg = /[^a-f0-9:./]/gi
    } else if (type === 'NUM') {
        regexp = /[^0-9]/i
        regexpchg = /[^0-9]/gi
    } else if (type === 'ENG') {
        regexp = /[^a-z]/i
        regexpchg = /[^a-z]/gi
    } else if (type === 'ENGNUM') {
        regexp = /[^a-z0-9]/i
        regexpchg = /[^a-z0-9:.]/gi
    }
    if (regexp.test(inputValue)) {
        event.currentTarget.blur()
        event.currentTarget.value = inputValue.replace(regexpchg, '')
        event.currentTarget.focus()
    }
}
