
(function (global) {

    class Hook {

        constructor() {

            this.timer = null
            this.mouseDown = false
            this.mouseMove = false
            this.self = this
            this.runTimers = {}

            let { self } = this

            window.onmousedown = function (e) {
                self.mouseDown = true
            }
    
            window.onmouseup = function (e) {
                self.mouseDown = false
            }
    
            window.onmousemove = function (e) {
                self.mouseMove = true
                clearTimeout(self.timer)
                self.timer = setTimeout(() => {
                    self.mouseMove = false
                }, 200)
            }
        }

        isIdle() {
            let {mouseDown, mouseMove} = this
            return !mouseDown && !mouseMove
        }

        run(fn, name, interval = 200) {
            let { self, runTimers } = this
            let timer = runTimers[name] 

            clearTimeout(timer)
            if (!self.isIdle()) {
                runTimers[name] = setTimeout(self.run.bind(self, fn, name, interval), interval);
                return
            }

            delete runTimers[name] 
            fn()
        }
    }

    (function example() {
        let hook = new Hook
        hook.run(() => {
            console.log('Hook run Job', this)
            console.log('\n\n')
        }, 'Job')
    })()

})(typeof exports !== 'undefined' ? exports : this);