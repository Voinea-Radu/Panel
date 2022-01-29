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
    if (logged) {
        document.getElementById("complaints-item").hidden = false;
        document.getElementById("unban-item").hidden = false;
        document.getElementById("bugs-item").hidden = false;
        user = JSON.parse(getCookie("login_data"));

        callAPI(`/api/check/staff?user=${user.username}&useCase=any`, {},
            () => {
                document.getElementById("entries-item").hidden = false;
            }, () => {
                document.getElementById("entries-item").hidden = false;
            }, () => {

            }, () => {

            });
    }
}

function getSkinURL(name) {
    return `https://cravatar.eu/helmavatar/${name}/190.png`
}

async function verifyCookie() {
    /*
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            alert(this.responseText);
        }
    };
    xhttp.open("POST", "/api/login/validate", true);
    xhttp.setRequestHeader("Content-type", "application/json");
    xhttp.send(getCookie("login_data"));

    console.log(1);
    wait(5000);
    console.log(2);
    console.log(xhttp.responseText);
    */

    blob = await fetch("/api/login/validate", {
        method: "post",
        body: getCookie("login_data")
    }).then(response => response.blob());

    return JSON.parse(await blob.text());
}

function setCookie(name, value) {
    window.localStorage.setItem(name, value);
}

function getCookie(name) {
    return window.localStorage.getItem(name);
}

async function loginCookie() {
    if (getCookie("login_data") !== null && getCookie("login_data") !== "" && getCookie("login_data") !== undefined) {

        try {
            if ((await verifyCookie()).code !== "200") {
                console.log("Invalid login data - Success")
                setCookie("login_data", "", 0)
            }

        } catch (error) {
            console.log("Invalid login data - Error")
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
    verifier = await verifyCookie(getCookie("login_data"));
    // noinspection EqualityComparisonWithCoercionJS
    return verifier.code == 200;
}

async function checkLoggedStatus() {
    loggedStatus = await isLoggedIn();
    if (!loggedStatus) {
        console.log("Not logged in");
        redirect("/401");
        return;
    }
    body = document.getElementById("logged-in-required");
    if (body !== undefined && body !== null) {
        body.style.visibility = "visible";
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

async function callPutAPI(api, data) {
    await fetch(api, {
        method: 'post',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });
}

async function callAPI(api, data, callbackEn, callbackRo, failCallbackEn, failCallbackRo) {
    var blob = await fetch(api, {
        method: 'post',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }).then(response => response.blob());

    json = await blob.text();

    try {
        obj = JSON.parse(json);

        if (obj.code !== "200") {
            if (getCookie("lang") === "en") {
                if (failCallbackEn === undefined) {
                    error.hidden = false;
                    error.innerText = obj.messageEn;
                } else {
                    failCallbackEn();
                }
            } else {
                if (failCallbackRo === undefined) {
                    if (failCallbackEn === undefined) {
                        error.hidden = false;
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

async function callAPI2(api, data, callback, failCallback) {
    await callAPI(api, data, callback, callback, failCallback, failCallback);
}


function wait(ms) {
    var start = Date.now(),
        now = start;
    while (now - start < ms) {
        now = Date.now();
    }
}