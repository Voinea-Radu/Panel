error = document.getElementById("error");
if (error !== null) {
    error.hidden = true;
}

// ----- DEVELOPMENT ONLY -----
//setCookie("login_data", "", 0);

if (getCookie("lang") == null) {
    setCookie('lang', "en", 0);
}

loginCookie();
setSiteLanguage();

const cancel = document.getElementById('cancel');
if (cancel !== null) {
    cancel.addEventListener('click', function () {
        window.location.replace("/");
    });
}

function getSkinURL(name) {
    return `https://cravatar.eu/helmavatar/${name}/190.png`
}

async function verifyCookie() {
    blob = await fetch("/api/login/validate", {
        method: "post",
        body: getCookie("login_data")
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
    if (getCookie("login_data") !== null) {

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

            obj = JSON.parse(getCookie("login_data"));
            url = getSkinURL(obj.username);

            login.outerHTML = "<img class='user-icon' src='" + url + "' onclick=profile('" + obj.username + "')>";
        }
    }
}

function profile(name) {
    window.location.replace(`/profile/?user=${name}`);
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
    callAPI("/api/login/v2", {
        username: document.getElementById('username').value,
        password: document.getElementById('password').value
    }, () => {
        cookie = {
            username: document.getElementById('username').value,
            password: obj.data
        }

        setCookie("login_data", JSON.stringify(cookie), 30)
        window.location.replace("/");
    });

    //TODO remove
    /*
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

        if (obj.code !== "200") {
            setCookie("login_data", "", 0);
            error.hidden = false;
            if (getCookie("lang") === "en") {
                error.innerText = obj.messageEn;
            } else {
                error.innerText = obj.messageRo;
            }
        }

        if (obj.data !== "" && obj.data !== undefined) {
            cookie = {}
            cookie.username = loginData.username;
            cookie.password = obj.data;
            setCookie("login_data", JSON.stringify(cookie), 30)
            window.location.replace("/");
        } else {
            setCookie("login_data", "", 0);
            error.hidden = false;
            if (getCookie("lang") === "en") {
                error.innerText = obj.messageEn;
            } else {
                error.innerText = obj.messageRo;
            }
        }
    } catch (error) {
        setCookie("login_data", "", 0);
        error.hidden = false;
        if (getCookie("lang") === "en") {
            error.innerText = obj.messageEn;
        } else {
            error.innerText = obj.messageRo;
        }
    }
     */
}

function complainsTemplate() {
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

    //TODO remove
    /*
    data = {};

    data.cookie = getCookie("login_data");
    data.target = document.getElementById("target").value;
    data.section = document.getElementById("section").value;
    data.dateAndTime = document.getElementById("date_and_time").value;
    data.description = document.getElementById("description").value;
    data.proof = document.getElementById("proof").value;

    var blob = await fetch('/api/form/complain', {
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
            error.hidden = false;
            if (getCookie("lang") === "en") {
                error.innerText = obj.messageEn;
            } else {
                error.innerText = obj.messageRo;
            }
        } else {
            if (getCookie("lang") === "en") {
                redirect("/?message=Complain sent successfully");
            } else {
                redirect("/?message=Reclamatia trimisa cu succes");
            }
        }

    } catch (error) {
        error.hidden = false;
        if (getCookie("lang") === "en") {
            error.innerText = obj.messageEn;
        } else {
            error.innerText = obj.messageRo;
        }
    }

     */
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
        var user = JSON.parse(getCookie("login_data"));

        callAPI(`/api/check/staff?user=${user.username}&useCase=complain`, {}, ()=>{
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
        cookie: getCookie("login_data"),
        decision: "APPROVED",
        id: status = document.getElementById("id").value
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
        cookie: getCookie("login_data"),
        decision: "DENY",
        id: status = document.getElementById("id").value
    }, () => {
        console.log("sent")
        window.location.reload();
    }, () => {
        console.log("trimis")
        window.location.reload();
    })
}

async function callAPI(api, data, callbackEn, callbackRo) {
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
            error.hidden = false;
            if (getCookie("lang") === "en") {
                error.innerText = obj.messageEn;
            } else {
                error.innerText = obj.messageRo;
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
        error.hidden = false;
        if (getCookie("lang") === "en") {
            error.innerText = obj.messageEn;
        } else {
            error.innerText = obj.messageRo;
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

    //TODO remove
    /*
    data = {}

    data.id = document.getElementById("id").value;
    data.cookie = getCookie("login_data");
    data.targetResponse = document.getElementById("target-response").value;


    var blob = await fetch('', {
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
            error.hidden = false;
            if (getCookie("lang") === "en") {
                error.innerText = obj.messageEn;
            } else {
                error.innerText = obj.messageRo;
            }
        } else {
            if (getCookie("lang") === "en") {
                window.location.replace("/?message=Complain response sent successfully");
            } else {
                window.location.replace("/?message=Raspunsul reclamatiei a fost trimis cu succes");
            }
        }

    } catch (error) {
        error.hidden = false;
        if (getCookie("lang") === "en") {
            error.innerText = obj.messageEn;
        } else {
            error.innerText = obj.messageRo;
        }
    }

     */

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

    var complainTitle = document.getElementById("complain-title");
    if (complainTitle !== null) {
        complainTitle.innerText = "Creaza o reclamatie";
    }

    var target = document.getElementById("target-label");
    if (target !== null) {
        target.innerText = "Reclamatul";
    }

    var section = document.getElementById("section-label");
    if (section !== null) {
        section.innerText = "Sectiune";
    }

    var date = document.getElementById("date_and_time-label");
    if (date !== null) {
        date.innerText = "Data si Ora";
    }

    var description = document.getElementById("description-label");
    if (description !== null) {
        description.innerText = "Descriere";
    }

    var proof = document.getElementById("proof-label");
    if (proof !== null) {
        proof.innerText = "Dovada";
    }

    var complainSubmit = document.getElementById("complain-submit");
    if (complainSubmit !== null) {
        complainSubmit.innerText = "Trimite";
    }

    var complainCancel = document.getElementById("complain-cancel");
    if (complainCancel !== null) {
        complainCancel.innerText = "Anuleaza";
    }

    var loginTitle = document.getElementById("login-title");
    if (loginTitle !== null) {
        loginTitle.innerText = "Logheaza-te";
    }

    var username = document.getElementById("username-label");
    if (username !== null) {
        username.innerText = "Nume de utilizator";
    }

    var password = document.getElementById("password-label");
    if (password !== null) {
        password.innerText = "Parola";
    }

    var loginSubmit = document.getElementById("login-submit");
    if (loginSubmit !== null) {
        loginSubmit.innerText = "Logheaza-te";
    }

    var cancel = document.getElementById("cancel");
    if (cancel !== null) {
        cancel.innerText = "Anuleaza";
    }


}