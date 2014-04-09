/**
Evozi - APK Downloader
http://apps.evozi.com/apk-downloader/
**/
var resetSimSettings = function() {
    setSimSettings({
        country: "USA",
        operator: "T-Mobile",
        operatorCode: "31020"
    });
};

var resetDeviceSettings = function() {
    setDeviceSettings({
        locale: "en",
        countryCode: "us",
        deviceAndSdkVersion: "mako:18"
    });
};

var checkSimSettings = function() {
    if (!localStorage.getItem("simCountry") || !localStorage.getItem("simOperator") || !localStorage.getItem("simOperatorCode")) {
        resetSimSettings();
    }
};

var checkDeviceSettings = function() {
    if (!localStorage.getItem("locale") || !localStorage.getItem("countryCode") || !localStorage.getItem("deviceAndSdkVersion")) {
        resetDeviceSettings();
    }
};

var setSimSettings = function(sim) {
    localStorage.setItem('simCountry', sim.country);
    localStorage.setItem('simOperator',  sim.operator);
    localStorage.setItem('simOperatorCode', sim.operatorCode);
};

var setDeviceSettings = function(device) {
    localStorage.setItem('locale', device.locale);
    localStorage.setItem('countryCode',  device.countryCode);
    localStorage.setItem('deviceAndSdkVersion', device.deviceAndSdkVersion);
};

var initCountryOptions = function() {
    var selectedCountry = localStorage.getItem('simCountry');
    Object.keys(codes).sort().forEach(function(country) {
        var option = new Option(/*text*/ country, /*value*/ country,
            /*default_selected*/ false, /*selected*/ country == selectedCountry);
        sltCountry.add(option);
    });

    sltCountry.onchange = function(e) {
        initOperatorOptions(this.value);
    };
    sltCountry.onchange();
};

var initOperatorOptions = function(country) {
    // Empty <select> box
    sltOperator.options.length = 0;

    var operators = codes[country];
    if (operators) {
        var selectedOperator = localStorage.getItem('simOperator');
        Object.keys(operators).sort().forEach(function(operator) {
            var option = new Option(/*text*/ operator, /*value*/ operator,
                /*default_selected*/ false, /*selected*/ operator == selectedOperator);
            sltOperator.add(option);
        });
    }
};

var saveAuth = function(email, token, deviceId) {
    localStorage.setItem('authEmail', email);
    localStorage.setItem('authToken',  token);
    localStorage.setItem('deviceId', deviceId.toLowerCase());
};

var clearAuth = function() {
    localStorage.removeItem("authEmail");
    localStorage.removeItem("authToken");
    localStorage.removeItem("deviceId");

    localStorage.removeItem("locale");
    localStorage.removeItem("countryCode");
    localStorage.removeItem("deviceAndSdkVersion");
};

/**
 * ClientLogin errors, taken from
 * https://developers.google.com/accounts/docs/AuthForInstalledApps
 */
var clientLoginErrors = {
    "BadAuthentication": "Incorrect username or password.",
    "NotVerified": "The account email address has not been verified. You need to access your Google account directly to resolve the issue before logging in here.",
    "TermsNotAgreed": "You have not yet agreed to Google's terms, acccess your Google account directly to resolve the issue before logging in using here.",
    "CaptchaRequired": "A CAPTCHA is required. (not supported, try logging in another tab)",
    "Unknown": "Unknown or unspecified error; the request contained invalid input or was malformed.",
    "AccountDeleted": "The user account has been deleted.",
    "AccountDisabled": "The user account has been disabled.",
    "ServiceDisabled": "Your access to the specified service has been disabled. (The user account may still be valid.)",
    "ServiceUnavailable": "The service is not available; try again later."
};

var login = function(email, password, deviceId) {
    var ACCOUNT_TYPE_HOSTED_OR_GOOGLE = "HOSTED_OR_GOOGLE";
    var URL_LOGIN = "https://www.google.com/accounts/ClientLogin";
    var LOGIN_SERVICE = "androidsecure";

    var params = {
        "Email": email,
        "Passwd": password,
        "service": LOGIN_SERVICE,
        "accountType": ACCOUNT_TYPE_HOSTED_OR_GOOGLE
    };

    var xhr = new XMLHttpRequest();
    xhr.open("POST", URL_LOGIN, true);

	var paramsStr = "";
    for (var key in params) {
        paramsStr += "&" + key + "=" + encodeURIComponent(params[key]);
    }

    xhr.onload = function() {
        var AUTH_TOKEN = "";
        var response = this.responseText;

        var error = response.match(/Error=(\w+)/);
        if (error) {
            var msg = clientLoginErrors[error[1]] || error[1];
            $('#error_notification').show();
            $('#error_text').text("Authentication failed, please make sure you enter a valid email and password, if you have 2 step auth set up, please use application specified password to login. "+ msg);
            return;
        }

        var match = response.match(/Auth=([a-z0-9=_\-]+)/i);
        if (match) {
            AUTH_TOKEN = match[1];
        }

        if (!AUTH_TOKEN) {
            // should never happen...
            alert("ERROR: Authentication token not available, cannot login.");
            return;
        }

        saveAuth(email, AUTH_TOKEN, deviceId);
        resetDeviceSettings();
        refreshViews();
    };

    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhr.send(paramsStr);
};

var refreshViews = function() {
    txtAuthEmail.value = inpEmail.value = localStorage.getItem("authEmail");
    txtDeviceId.value = inpDeviceId.value = localStorage.getItem("deviceId");
    inpCountryCode.value = localStorage.getItem("countryCode");
    inpLocale.value = localStorage.getItem("locale");
     inpDeviceAndSdkVersion.value = localStorage.getItem("deviceAndSdkVersion");

    if (typeof localStorage.authToken == "undefined") {
        formLogin.style.display = "block";
        formInfo.style.display = "none";
        formDevice.style.display = "none";
    } else {
        formInfo.style.display = "block";
        formLogin.style.display = "none";
        formDevice.style.display = "block";

        checkSimSettings();
        initCountryOptions();
    }
};

var formInfo = document.getElementById("info_form");
var formDevice = document.getElementById("device_form");
var formLogin = document.getElementById("login_form");

var txtAuthEmail = document.getElementById("auth_email");
var txtDeviceId = document.getElementById("device_id");

var inpEmail = document.getElementById("user_email");
var inpPassword = document.getElementById("user_password");
var inpDeviceId = document.getElementById("user_device_id");

var sltCountry = document.getElementById("slt_country");
var sltOperator = document.getElementById("slt_operator");
var btnDefault = document.getElementById("btn_default");
var btnDeviceDefault = document.getElementById("btn_device_default");

var inpLocale = document.getElementById("device_locale");
var inpCountryCode = document.getElementById("country_code");
var inpDeviceAndSdkVersion = document.getElementById("deviceAndSdkVersion");

var btnsAdv = document.getElementsByClassName("btn-advanced-settings");
function toggleAdvCb(e) {
    e.preventDefault();
    formInfo.classList.toggle("hide-advanced");
    formInfo.classList.toggle("show-advanced");
}
for (var i=0; i<btnsAdv.length; i++) {
    btnsAdv[i].addEventListener("click", toggleAdvCb);
}

var btnsDevice = document.getElementsByClassName("btn-device-settings");

for (var i=0; i<btnsDevice.length; i++) {
        btnsDevice[i].addEventListener("click", function (e) {
        e.preventDefault();
        formDevice.classList.toggle("hide-device");
        formDevice.classList.toggle("show-device");
    });
}

btnDefault.onclick = function(e) {
    e.preventDefault();
    if (confirm('Reset to default sim operator?')) {
        resetSimSettings();
        initCountryOptions();
    }
};

btnDeviceDefault.onclick = function(e) {
    e.preventDefault();
    if (confirm('Reset to default device info?')) {
        resetDeviceSettings();
        refreshViews();
    }
};

var btnSave = document.getElementById("btn_save");
btnSave.onclick = function(e) {
    e.preventDefault();
    var country = sltCountry.value;
    var operator = sltOperator.value;
    var operatorCode = codes[country][operator];
    setSimSettings({
        country: country,
        operator: operator,
        operatorCode: operatorCode
    });
    alert('Successfully Saved!');
};

var btnDeviceSave = document.getElementById("btn_device_save");
btnDeviceSave.onclick = function(e) {
    e.preventDefault();
    var locale = inpLocale.value;
    var countryCode = inpCountryCode.value;
    var deviceAndSdkVersion = inpDeviceAndSdkVersion.value;
    
    setDeviceSettings({
        locale: locale,
        countryCode: countryCode,
        deviceAndSdkVersion: deviceAndSdkVersion
    });
    alert('Device Info Successfully Saved!');
};


var btnLogin = document.getElementById("btn_login");

btnLogin.onclick = function(e) {
    e.preventDefault();

    var email = inpEmail.value;
    var password = inpPassword.value;
    var deviceId = inpDeviceId.value;

    var match = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.exec(email);
    if (!match) {
        $('#error_notification').show();
        $('#error_text').text("Please enter valid email!");
        inpEmail.focus();
        return;
    }

    if (password.length === 0) {
        $('#error_notification').show();
        $('#error_text').text("Please enter a password!");
        inpPassword.focus();
        return;
    }

    if (!/^[0-9a-f]{16}$/i.test(deviceId)) {
        $('#error_notification').show();
        $('#error_text').text("Android Device ID (GSF ID) must be 16 characters long and only contains characters from 0-9, A-F");
        inpDeviceId.focus();
        return;
    }

    login(email, password, deviceId);
};

var btnLogout = document.getElementById("btn_logout");
btnLogout.onclick = function(e) {
    e.preventDefault();

    if (confirm('Are you sure you want to log out?')) {
        clearAuth();
        refreshViews();
    }
};

var _gaq = _gaq || [];
_gaq.push(["_setAccount", "UA-33205486-2"]);
_gaq.push(['_trackPageview']);

(function() {
  var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
  ga.src = 'https://ssl.google-analytics.com/ga.js';
  var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
})();

refreshViews();

/* test if still logged in */
if (localStorage.getItem("authToken") !== null) {
    chrome.extension.getBackgroundPage().hasValidSession(function(isValid) {
        if (!isValid) {
            refreshViews();
        }
    });
}
      
$('#user_email').click(function () { 
   hide_notification();
   $('#user_email_help').show();
   $('#user_password_help').hide();
   $('#user_device_id_help').hide();
});

$('#user_password').click(function () { 
   hide_notification();
   $('#user_email_help').hide();
   $('#user_password_help').show();
   $('#user_device_id_help').hide();
});

$('#user_device_id').click(function () {
   hide_notification();
   $('#user_email_help').hide();
   $('#user_password_help').hide();
   $('#user_device_id_help').show();
});

function hide_notification(){
   $('#error_notification').fadeOut(250);
}