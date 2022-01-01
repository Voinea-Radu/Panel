error = document.getElementById("error");
if (error !== null) {
    error.hidden = true;
}

// ----- DEVELOPMENT ONLY -----
//setCookie("login_data", "", 0);

//Language sanitization
if (getCookie("lang") == null) {
    setCookie('lang', "en", 0);
}

//Login
loginCookie();

//Cancel button
const cancel = document.getElementById('cancel');
if (cancel !== null) {
    cancel.addEventListener('click', function () {
        window.location.replace("/");
    });
}

//Dashboard sanitization
dashBoard();

async function dashBoard() {
    logged = await isLoggedIn();
    if (!logged) {
        console.log(2)
        document.getElementById("entries-item").hidden = true;
        document.getElementById("complaints-item").hidden = true;
        document.getElementById("unban-item").hidden = true;
        document.getElementById("bugs-item").hidden = true;
    } else {
        user = JSON.parse(getCookie("login_data"))
        callAPI(`/api/check/staff?user=${user.username}&useCase=any`, {},
            () => {
            }, () => {
            }, () => {
                document.getElementById("entries-item").hidden = true;
            }, () => {
                document.getElementById("entries-item").hidden = false;
            })
    }
}

function getSkinURL(name) {
    return `https://cravatar.eu/helmavatar/${name}/190.png`
}

async function verifyCookie() {
    blob = await fetch("/api/login/validate", {
        method: "post", body: getCookie("login_data")
    }).then(response => response.blob());

    return JSON.parse(await blob.text());
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
        while (c.charAt(0) === ' ') {
            c = c.substring(1, c.length);
        }
        if (c.indexOf(nameEQ) === 0) {
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
    if (getCookie("login_data") !== null && getCookie("login_data") !== "" && getCookie("login_data") !== undefined) {

        try {
            if ((await verifyCookie()).code !== "200") {
                setCookie("login_data", "", 0)
            }

        } catch (error) {
            setCookie("login_data", "", 0)
        }


        //Login
        if (getCookie("login_data") !== null) {
            login = document.getElementById("login-button");

            let obj = JSON.parse(getCookie("login_data"));
            url = getSkinURL(obj.username);

            login.outerHTML = "<img class='user-icon' src='" + url + "' onclick=profile('" + obj.username + "') alt=\"profile\">";
        }
    }
}

function profile(name) {
    window.location.replace(`/profile/?user=${name}`);
}

async function isLoggedIn() {
    if (getCookie("login_data") === null || getCookie("login_data") === undefined || getCookie("login_data") === "") {
        return false;
    }
    verifier = await verifyCookie(getCookie("login_data"))
    // noinspection EqualityComparisonWithCoercionJS
    return verifier.code == 200
}

async function checkLoggedStatus() {
    loggedStatus = await isLoggedIn();
    if (!loggedStatus) {
        redirect("/401");
    }
}

function changeLanguage(language) {
    if (language !== "ro" && language !== "en") {
        return;
    }

    setCookie('lang', language, 0);

    window.location.reload();
}

function redirect(path) {
    window.location.replace(path);
}

async function callAPI(api, data, callbackEn, callbackRo, failCallbackEn, failCallbackRo) {
    var blob = await fetch(api, {
        method: 'post', headers: {
            'Accept': 'application/json', 'Content-Type': 'application/json'
        }, body: JSON.stringify(data)
    }).then(response => response.blob());

    json = await blob.text();

    try {
        obj = JSON.parse(json);

        if (obj.code !== "200") {
            error.hidden = false;
            if (getCookie("lang") === "en") {
                if (failCallbackEn === undefined) {
                    error.innerText = obj.messageEn;
                } else {
                    failCallbackEn();
                }
            } else {
                if (failCallbackRo === undefined) {
                    if (failCallbackEn === undefined) {
                        error.innerText = obj.messageRo;
                    } else {
                        failCallbackEn();
                    }
                }
                failCallbackRo();
            }
        } else {
            if (getCookie("lang") === "en") {
                callbackEn();
            } else {
                if (callbackRo === undefined || callbackRo === null) {
                    callbackEn()
                } else {
                    callbackRo();
                }
            }
        }

    } catch (error) {
        if (error !== undefined) {
            error.hidden = false;
        }
        if (getCookie("lang") === "en") {
            if (failCallbackEn === undefined) {
                error.innerText = obj.messageEn;
            } else {
                failCallbackEn();
            }
        } else {
            if (failCallbackRo === undefined) {
                if (failCallbackEn === undefined) {
                    error.innerText = obj.messageRo;
                } else {
                    failCallbackEn();
                }
            } else {
                failCallbackRo();
            }
        }
    }
}

async function applyTemplate(){
    checkLoggedStatus();

    document.getElementById('submit').addEventListener('click', function f() {
        apply();
    });
}

function apply(){
    callAPI("/api/form/apply", {
        cookie: getCookie("login_data"),
        age: document.getElementById("age").value,
        section: document.getElementById("section").value,
        english: document.getElementById("english").value,
        commands: document.getElementById("commands").value,
        why: document.getElementById("why").value,
    }, () => {
        redirect("/?message=You application was sent successfully");
    }, () => {
        redirect("/?message=Aplicatia ta a fost trimisa cu succes");
    })
}

async function applyDeatils(){
    bans = document.getElementById("bans").value;
    warns = document.getElementById("warns").value;
    kicks = document.getElementById("kicks").value;
    mutes = document.getElementById("mutes").value;
    document.getElementById("sanctions-2").innerHTML = "" +
        "                <span class='label default'>Bans: " + bans + "</span>\n" +
        "                <span class='label default'>Kicks: " + kicks + "</span>\n" +
        "                <span class='label default'>Mutes: " + mutes + "</span>\n" +
        "                <span class='label default'>Warnings: " + warns + "</span>";
}