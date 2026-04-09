let instance = null

export class Device {
  static get instance() {
    return new Device()
  }

  #data = {}
  constructor() {
    if (instance) {
      return instance
    }
    instance = window.deviceInfo =/*  this */
    this.self = this
    this.#load()
    this.#defineProp()
  }

  get data() {
    return this.#data
  }

  #defineProp = function() {
    for (const key in this.#data) {
      Object.defineProperty(this, key, {
        get: function() { return this.#data[key] },
        set: function(newValue) { this.update({ [key]: newValue }) }
      })
    }
  }

  #load = function() {
    /* test cases
        alert(
            'browserInfo result: OS: ' + browserInfo.os +' '+ browserInfo.osVersion + '\n'+
                'Browser: ' + browserInfo.browser +' '+ browserInfo.browserVersion + '\n' +
                'Mobile: ' + browserInfo.mobile + '\n' +
                'Cookies: ' + browserInfo.cookies + '\n' +
                'Screen Size: ' + browserInfo.screen
        );
    */
    var unknown = 'Unknown'
    let width, height

    // screen
    var screenSize = ''
    if (screen.width) {
      width = (screen.width) ? screen.width : ''
      height = (screen.height) ? screen.height : ''
      screenSize += '' + width + ' x ' + height
    }

    // browser
    var nVer = navigator.appVersion
    var nAgt = navigator.userAgent
    var browser = navigator.appName
    var version = '' + parseFloat(navigator.appVersion)
    var majorVersion = parseInt(navigator.appVersion, 10)
    var nameOffset, verOffset, ix

    if ((verOffset = nAgt.indexOf('Opera')) !== -1) {
      // Opera
      browser = 'Opera'
      version = nAgt.substring(verOffset + 6)
      if ((verOffset = nAgt.indexOf('Version')) !== -1) {
        version = nAgt.substring(verOffset + 8)
      }
    }
    if ((verOffset = nAgt.indexOf('OPR')) !== -1) {
      // Opera Next
      browser = 'Opera'
      version = nAgt.substring(verOffset + 4)
    } else if ((verOffset = nAgt.indexOf('Edge')) !== -1) {
      // Legacy Edge
      browser = 'Microsoft Legacy Edge'
      version = nAgt.substring(verOffset + 5)
    } else if ((verOffset = nAgt.indexOf('Edg')) !== -1) {
      // Edge (Chromium)
      browser = 'Microsoft Edge'
      version = nAgt.substring(verOffset + 4)
    } else if ((verOffset = nAgt.indexOf('MSIE')) !== -1) {
      // MSIE
      browser = 'Microsoft Internet Explorer'
      version = nAgt.substring(verOffset + 5)
    } else if ((verOffset = nAgt.indexOf('Chrome')) !== -1) {
      // Chrome
      browser = 'Chrome'
      version = nAgt.substring(verOffset + 7)
    } else if ((verOffset = nAgt.indexOf('Safari')) !== -1) {
      // Safari
      browser = 'Safari'
      version = nAgt.substring(verOffset + 7)
      if ((verOffset = nAgt.indexOf('Version')) !== -1) {
        version = nAgt.substring(verOffset + 8)
      }

      // Chrome on iPad identifies itself as Safari. Actual results do not match what Google claims
      //  at: https://developers.google.com/chrome/mobile/docs/user-agent?hl=ja
      //  No mention of chrome in the user agent string. However it does mention CriOS, which presumably
      //  can be keyed on to detect it.
      if (nAgt.indexOf('CriOS') !== -1) {
        // Chrome on iPad spoofing Safari...correct it.
        browser = 'Chrome'
        // Don't believe there is a way to grab the accurate version number, so leaving that for now.
      }
    } else if ((verOffset = nAgt.indexOf('Firefox')) !== -1) {
      // Firefox
      browser = 'Firefox'
      version = nAgt.substring(verOffset + 8)
    } else if (nAgt.indexOf('Trident/') !== -1) {
      // MSIE 11+
      browser = 'Microsoft Internet Explorer'
      version = nAgt.substring(nAgt.indexOf('rv:') + 3)
    } else if ((nameOffset = nAgt.lastIndexOf(' ') + 1) < (verOffset = nAgt.lastIndexOf('/'))) {
      // Other browsers
      browser = nAgt.substring(nameOffset, verOffset)
      version = nAgt.substring(verOffset + 1)
      if (browser.toLowerCase() === browser.toUpperCase()) {
        browser = navigator.appName
      }
    }
    // trim the version string
    if ((ix = version.indexOf(';')) !== -1) version = version.substring(0, ix)
    if ((ix = version.indexOf(' ')) !== -1) version = version.substring(0, ix)
    if ((ix = version.indexOf(')')) !== -1) version = version.substring(0, ix)

    majorVersion = parseInt('' + version, 10)
    if (isNaN(majorVersion)) {
      version = '' + parseFloat(navigator.appVersion)
      majorVersion = parseInt(navigator.appVersion, 10)
    }

    // mobile version
    var mobile = /Mobile|mini|Fennec|Android|iP(ad|od|hone)/.test(nVer)

    // cookie
    var cookieEnabled = !!(navigator.cookieEnabled)

    if (typeof navigator.cookieEnabled === 'undefined' && !cookieEnabled) {
      document.cookie = 'testcookie'
      cookieEnabled = (document.cookie.indexOf('testcookie') !== -1)
    }

    // system
    var os = unknown
    var clientStrings = [
      { s: 'Windows 10', r: /(Windows 10.0|Windows NT 10.0)/ },
      { s: 'Windows 8.1', r: /(Windows 8.1|Windows NT 6.3)/ },
      { s: 'Windows 8', r: /(Windows 8|Windows NT 6.2)/ },
      { s: 'Windows 7', r: /(Windows 7|Windows NT 6.1)/ },
      { s: 'Windows Vista', r: /Windows NT 6.0/ },
      { s: 'Windows Server 2003', r: /Windows NT 5.2/ },
      { s: 'Windows XP', r: /(Windows NT 5.1|Windows XP)/ },
      { s: 'Windows 2000', r: /(Windows NT 5.0|Windows 2000)/ },
      { s: 'Windows ME', r: /(Win 9x 4.90|Windows ME)/ },
      { s: 'Windows 98', r: /(Windows 98|Win98)/ },
      { s: 'Windows 95', r: /(Windows 95|Win95|Windows_95)/ },
      { s: 'Windows NT 4.0', r: /(Windows NT 4.0|WinNT4.0|WinNT|Windows NT)/ },
      { s: 'Windows CE', r: /Windows CE/ },
      { s: 'Windows 3.11', r: /Win16/ },
      { s: 'Android', r: /Android/ },
      { s: 'Open BSD', r: /OpenBSD/ },
      { s: 'Sun OS', r: /SunOS/ },
      { s: 'Chrome OS', r: /CrOS/ },
      { s: 'Linux', r: /(Linux|X11(?!.*CrOS))/ },
      { s: 'iOS', r: /(iPhone|iPad|iPod)/ },
      { s: 'Mac OS X', r: /Mac OS X/ },
      { s: 'Mac OS', r: /(Mac OS|MacPPC|MacIntel|Mac_PowerPC|Macintosh)/ },
      { s: 'QNX', r: /QNX/ },
      { s: 'UNIX', r: /UNIX/ },
      { s: 'BeOS', r: /BeOS/ },
      { s: 'OS/2', r: /OS\/2/ },
      { s: 'Search Bot', r: /(nuhk|Googlebot|Yammybot|Openbot|Slurp|MSNBot|Ask Jeeves\/Teoma|ia_archiver)/ }
    ]
    for (var id in clientStrings) {
      var cs = clientStrings[id]
      if (cs.r.test(nAgt)) {
        os = cs.s
        break
      }
    }

    var osVersion = unknown

    if (/Windows/.test(os)) {
      osVersion = /Windows (.*)/.exec(os)[1]
      os = 'Windows'
    }

    switch (os) {
      case 'Mac OS':
      case 'Mac OS X':
        osVersion = /Mac OS X (10[\.\_\d]+)/.exec(nAgt)[1]
        break

      case 'Android':
        osVersion = /(?:Android|Mac OS|Mac OS X|MacPPC|MacIntel|Mac_PowerPC|Macintosh) ([\.\_\d]+)/.exec(nAgt)[1]
        break

      case 'iOS':
        osVersion = /OS (\d+)_(\d+)_?(\d+)?/.exec(nVer)
        osVersion = osVersion[1] + '.' + osVersion[2] + '.' + (osVersion[3] | 0)
        break
    }

    const browserInfo = {
      screen: screenSize,
      browser: browser,
      browserVersion: version,
      mobile: mobile,
      os: os,
      osVersion: osVersion,
      cookies: cookieEnabled
    }
    Object.assign(this.#data, browserInfo)
  }
}
