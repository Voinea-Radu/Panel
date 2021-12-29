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

async function loginTemplate() {
    loggedStatus = await isLoggedIn();
    if (loggedStatus) {
        redirect("/401");
    }

    /*
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
    */

    document.getElementById('login-submit').addEventListener('click', function () {
        login()
    });
}

async function login() {
    callAPI("/api/login/v2", {
        username: document.getElementById('username').value, password: document.getElementById('password').value
    }, () => {
        cookie = {
            username: document.getElementById('username').value, password: obj.data
        }

        setCookie("login_data", JSON.stringify(cookie), 30)
        window.location.replace("/");
    });
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

function complainsTemplate() {
    checkLoggedStatus();

    /*
    document.getElementById("target").addEventListener("keydown", function (e) {
        if (e.key === "Enter") {
            complain();
        }
    });
    document.getElementById("description").addEventListener("keydown", function (e) {
        if (e.key === "Enter") {
            complain();
        }
    });
    document.getElementById("proof").addEventListener("keydown", function (e) {
        if (e.key === "Enter") {
            complain();
        }
    });
    document.getElementById("section").addEventListener("keydown", function (e) {
        if (e.key === "Enter") {
            complain();
        }
    });
    document.getElementById("date_and_time").addEventListener("keydown", function (e) {
        if (e.key === "Enter") {
            complain();
        }
    });
    */

    document.getElementById('complain-submit').addEventListener('click', function f() {
        complain()
    });
}

async function complain() {

    callAPI("/api/form/complain", {
        cookie: getCookie("login_data"),
        target: document.getElementById("target").value,
        section: document.getElementById("section").value,
        dateAndTime: document.getElementById("date_and_time").value,
        description: document.getElementById("description").value,
        proof: document.getElementById("proof").value
    }, () => {
        redirect("/?message=Complain sent successfully");
    }, () => {
        redirect("/?message=Reclamatia trimisa cu succes");
    })
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

async function complaintsDetails() {

    var status = document.getElementById("status").value;

    if (status === "OPEN_AWAITING_TARGET_RESPONSE") {
        var target = document.getElementById("target").value;
        var by = document.getElementById("user").value;
        // noinspection JSDuplicatedDeclaration
        var user = JSON.parse(getCookie("login_data"))

        if (user === null) {
            redirect("/unauthorised");
            return;
        }

        if (target !== user.username) {
            document.getElementById("complain-submit").hidden = true;
            document.getElementById("cancel").hidden = true;
            document.getElementById("target-response").readOnly = true;

            if (by !== user.username) {
                console.log(2)
                redirect("/unauthorised");
                return;
            }
        }


        document.getElementById("target-response").addEventListener("keydown", function (e) {
            if (e.key === "Enter") {
                complainRespond();
            }
        });

        document.getElementById('complain-submit').addEventListener('click', function f() {
            complainRespond()
        });


    } else {
        document.getElementById("target-response").hidden = true;
        document.getElementById("target-response-label").hidden = true;
        document.getElementById("complain-submit").hidden = true;
        document.getElementById("cancel").hidden = true;

        document.getElementById("target-response-db").hidden = false;
        document.getElementById("target-response-db-label").hidden = false;

    }
    if (status === "OPEN_AWAITING_STAFF_APPROVAL") {
        // noinspection JSDuplicatedDeclaration
        var user = JSON.parse(getCookie("login_data"));

        callAPI(`/api/check/staff?user=${user.username}&useCase=complain`, {}, () => {
            document.getElementById("approve").hidden = false;
            document.getElementById("deny").hidden = false;

            document.getElementById('approve').addEventListener('click', function f() {
                approveComplain();
            });

            document.getElementById('deny').addEventListener('click', function f() {
                denyComplain();
            });
        })

    }
    if (status === "CLOSED") {
        document.getElementById("complain-submit").hidden = true;
        document.getElementById("cancel").hidden = true;
        document.getElementById("target-response").readOnly = true;
        document.getElementById("target-response-db").hidden = true;
        document.getElementById("target-response-db-label").hidden = true;
        let approved = document.getElementById("approved");
        if (approved !== null) {
            approved.hidden = false;
        }
        let denied = document.getElementById("denied");
        if (denied !== null) {
            denied.hidden = false;
        }

    }

}

async function approveComplain() {
    callAPI("/api/update/form/complain", {
        cookie: getCookie("login_data"), decision: "APPROVED", id: status = document.getElementById("id").value
    }, () => {
        console.log("sent")
        window.location.reload();
    }, () => {
        console.log("trimis")
        window.location.reload();
    });
}

async function denyComplain() {
    callAPI("/api/update/form/complain", {
        cookie: getCookie("login_data"), decision: "DENY", id: status = document.getElementById("id").value
    }, () => {
        console.log("sent")
        window.location.reload();
    }, () => {
        console.log("trimis")
        window.location.reload();
    })
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
            }
            failCallbackRo();
        }
    }
}

async function complainRespond() {

    callAPI("/api/form/complain-target-responde", {
        id: document.getElementById("id").value,
        cookie: getCookie("login_data"),
        targetResponse: document.getElementById("target-response").value
    }, () => {
        window.location.replace("/?message=Complain response sent successfully");
    }, () => {
        window.location.replace("/?message=Raspunsul reclamatiei a fost trimis cu succes");
    })
}

function unbanTemplate() {
    checkLoggedStatus();

    /*
    document.getElementById("staff-user").addEventListener("keydown", function (e) {
        if (e.key === "Enter") {
            unban();
        }
    });
    document.getElementById("reason").addEventListener("keydown", function (e) {
        if (e.key === "Enter") {
            unban();
        }
    });
    document.getElementById("date_and_time").addEventListener("keydown", function (e) {
        if (e.key === "Enter") {
            unban();
        }
    });
    document.getElementById("ban").addEventListener("keydown", function (e) {
        if (e.key === "Enter") {
            unban();
        }
    });
    document.getElementById("argument").addEventListener("keydown", function (e) {
        if (e.key === "Enter") {
            unban();
        }
    });
    */

    document.getElementById('submit').addEventListener('click', function f() {
        unban();
    });
}

async function unban() {

    callAPI("/api/form/unban", {
        cookie: getCookie("login_data"),
        staff: document.getElementById("staff-user").value,
        reason: document.getElementById("reason").value,
        dateAndTime: document.getElementById("date_and_time").value,
        ban: document.getElementById("ban").value,
        argument: document.getElementById("argument").value
    }, () => {
        redirect("/?message=Unban request sent successfully");
    }, () => {
        redirect("/?message=Cererea unban trimisa cu succes");
    })
}

async function unbanDetails() {

    var status = document.getElementById("status").value;

    if (status === "OPEN") {
        var user = JSON.parse(getCookie("login_data"));

        callAPI(`/api/check/staff?user=${user.username}&useCase=unban`, {}, () => {
            document.getElementById("approve").hidden = false;
            document.getElementById("deny").hidden = false;

            document.getElementById('approve').addEventListener('click', function f() {
                approveUnban();
            });

            document.getElementById('deny').addEventListener('click', function f() {
                denyUnban();
            });
        })

    }
}

async function approveUnban() {
    callAPI("/api/update/form/unban", {
        cookie: getCookie("login_data"), decision: "APPROVED", id: status = document.getElementById("id").value
    }, () => {
        window.location.reload();
    }, () => {
        window.location.reload();
    });
}

async function denyUnban() {
    callAPI("/api/update/form/unban", {
        cookie: getCookie("login_data"), decision: "DENIED", id: document.getElementById("id").value
    }, () => {
        console.log("sent")
        window.location.reload();
    }, () => {
        console.log("trimis")
        window.location.reload();
    })
}

function bugsTemplate() {
    checkLoggedStatus();

    /*
    document.getElementById("section").addEventListener("keydown", function (e) {
        if (e.key === "Enter") {
            bug();
        }
    });
    document.getElementById("description").addEventListener("keydown", function (e) {
        if (e.key === "Enter") {
            bug();
        }
    });
    */

    document.getElementById('submit').addEventListener('click', function f() {
        bug();
    });
}

async function bug() {
    callAPI("/api/form/bugs", {
        cookie: getCookie("login_data"),
        section: document.getElementById("section").value,
        description: document.getElementById("description").value,
    }, () => {
        redirect("/?message=Bug report sent successfully");
    }, () => {
        redirect("/?message=Bug-ul a fost raportat cu succes");
    })
}

async function bugDetails() {

    var status = document.getElementById("status").value;

    if (status === "OPEN") {
        var user = JSON.parse(getCookie("login_data"));

        callAPI(`/api/check/staff?user=${user.username}&useCase=bug`, {}, () => {
            document.getElementById("close").hidden = false;

            document.getElementById('close').addEventListener('click', function f() {
                closeBug();
            });
        })

    }
}

async function closeBug() {
    callAPI("/api/update/form/bug", {
        cookie: getCookie("login_data"), id: document.getElementById("id").value
    }, () => {
        window.location.reload();
    }, () => {
        window.location.reload();
    });
}

function entriesTemplate() {
    callAPI(`/api/check/staff?user=${user.username}&useCase=any`, {},
        () => {
        }, () => {
        }, () => {
            redirect("/401")
        }, () => {
            redirect("/401")
        })
}

function profileTemplate() {
    callAPI(`/api/check/staff?user=${user.username}&useCase=any`, {},
        () => {
        }, () => {
        }, () => {
            document.getElementById("sanctions-row").hidden = true;
        }, () => {
            document.getElementById("sanctions-row").hidden = false;
        })
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

}