error = document.getElementById("error");
if (error !== null) {
    error.hidden = true;
}

// ----- DEVELOPMENT ONLY -----
setCookie("login_data", "", 0);

if (getCookie("lang") == null) {
    setCookie('lang', "en", 0);
}

loginCookie()
setSiteLanguage()

const cancel = document.getElementById('cancel');

if (cancel !== null) {
    cancel.addEventListener('click', function () {
        window.location.replace("/");
    });
}

function getSkinURL(name) {
    return `https://cravatar.eu/helmavatar/${name}/190.png`
}

function verifyCookie() {
    return fetch("/api/login/validate", {
        method: "post",
        body: getCookie("login_data")
    }).then(response => response.blob());
}

function setCookie(name, value, days) {
    var expires = "";
    if (days) {
        var date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        expires = "; expires=" + date.toUTCString();
    }
    document.cookie = name + "=" + (value || "") + expires + "; path=/";
}

function getCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1, c.length);
        }
        if (c.indexOf(nameEQ) == 0) {
            output = c.substring(nameEQ.length, c.length);
            if (output === "") {
                return null;
            }
            return output;
        }
    }
    return null;
}

async function loginCookie() {
    if (getCookie("login_data") !== null) {

        blob = await verifyCookie();
        json = await blob.text();
        obj = {};

        try {
            obj = JSON.parse(json);

            if (obj.response !== "200 OK") {
                setCookie("login_data", "", 0)
            }

            if (obj.password !== "" && obj.password !== undefined) {
                obj.response = undefined;
                setCookie("login_data", JSON.stringify(obj), 30)
            } else {
                setCookie("login_data", "", 0)
            }
        } catch (error) {
            setCookie("login_data", "", 0)
        }


        //Login
        if (getCookie("login_data") !== null) {
            login = document.getElementById("login-button");

            url = getSkinURL(obj.username);

            login.outerHTML = "<img src=" + url + " style='width: min(20%,50px)'>";
        }
    }
}

function loginTemplate() {
    document.getElementById('username').addEventListener("keydown", function (e) {
        if (e.key === "Enter") {
            login();
        }
    });
    document.getElementById('password').addEventListener("keydown", function (e) {
        if (e.key === "Enter") {
            login();
        }
    });
    document.getElementById('login-submit').addEventListener('click', function () {
        login()
    });
}

async function login() {
    const usernameField = document.getElementById('username');
    const passwordField = document.getElementById('password');

    loginData = {};
    loginData.username = usernameField.value;
    loginData.password = passwordField.value;

    var blob = await fetch('/api/login/v2', {
        method: 'post',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(loginData)
    }).then(response => response.blob());

    json = await blob.text();

    try {
        obj = JSON.parse(json);

        if (obj.response !== "200 OK") {
            setCookie("login_data", "", 0);
            error.hidden = false;
            error.innerText = obj.response;
        }

        if (obj.password !== "" && obj.password !== undefined) {
            obj.response = undefined;
            setCookie("login_data", JSON.stringify(obj), 30)
            window.location.replace("/");
        } else {
            setCookie("login_data", "", 0);
            error.hidden = false;
            error.innerText = obj.response;
        }
    } catch (error) {
        setCookie("login_data", "", 0);
        error.hidden = false;
        error.innerText = obj.response;
    }
}

function complainsTemplate() {
    document.getElementById("target").addEventListener("keydown", function (e) {
        if (e.key === "Enter") {
            submit();
        }
    });
    document.getElementById("description").addEventListener("keydown", function (e) {
        if (e.key === "Enter") {
            submit();
        }
    });
    document.getElementById("proof").addEventListener("keydown", function (e) {
        if (e.key === "Enter") {
            submit();
        }
    });
    document.getElementById("section").addEventListener("keydown", function (e) {
        if (e.key === "Enter") {
            submit();
        }
    });
    document.getElementById("date_and_time").addEventListener("keydown", function (e) {
        if (e.key === "Enter") {
            submit();
        }
    });

    document.getElementById('complain-submit').addEventListener('click', function f() {
        submit()
    });
}

async function submit() {

    complainData = {};

    complainData.cookie = getCookie("login_data");
    complainData.target = document.getElementById("target").value;
    complainData.section = document.getElementById("section").value;
    complainData.dateAndTime = document.getElementById("date_and_time").value;
    complainData.description = document.getElementById("description").value;
    complainData.proof = document.getElementById("proof").value;

    var blob = await fetch('/api/form/complain', {
        method: 'post',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(complainData)
    }).then(response => response.blob());

    json = await blob.text();

    try {
        obj = JSON.parse(json);

        if (obj.response !== "200 OK") {
            error.hidden = false;
            error.innerText = obj.response;
        } else {
            window.location.replace("/?message=Complain sent successfully");
        }

    } catch (error) {
        error.hidden = false;
        error.innerText = obj.response;
    }
}

function changeLanguage(language) {
    if (language !== "ro" && language !== "en") {
        return;
    }

    setCookie('lang', language, 0);

    window.location.reload();
}

function setSiteLanguage() {
    if (getCookie("lang") === "en") {
        return;
    }

    var donors = document.getElementById("donors");
    if (donors !== null) {
        donors.innerText = "Donatori";
    }

    var registered = document.getElementById("registered");
    if (registered !== null) {
        registered.innerText = "Jucatori Inregistrati";
    }

    var online = document.getElementById("online");
    if (online !== null) {
        online.innerText = "Jucatori online";
    }

    var home = document.getElementById("home");
    if (home !== null) {
        home.innerText = "Home";
    }

    var staff = document.getElementById("staff");
    if (staff !== null) {
        staff.innerText = "Staff";
    }

    var complaints = document.getElementById("complaints");
    if (complaints !== null) {
        complaints.innerText = "Reclamatii";
    }

    var unban = document.getElementById("unban");
    if (unban !== null) {
        unban.innerText = "Cereri Unban";
    }

    var bugs = document.getElementById("bugs");
    if (bugs !== null) {
        bugs.innerText = "bugs";
    }

    var rules = document.getElementById("rules");
    if (rules !== null) {
        rules.innerText = "Regulament";
    }

    var entries = document.getElementById("entries");
    if (entries !== null) {
        entries.innerText = "Activitate";
    }

    var login = document.getElementById("login-button");
    if (login !== null) {
        login.innerText = "Logheaza-te";
    }


}