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
setSiteLanguage();

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
        complainTitle.innerText = "Creeaza o reclamatie";
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

    var joinDate = document.getElementById("join-date");
    if (joinDate !== null) {
        joinDate.innerText = "Data Alaturari";
    }

    var playingHours = document.getElementById("playing-hours");
    if (playingHours !== null) {
        playingHours.innerText = "Ore Jucate";
    }

    var unbanTitle = document.getElementById("unban-title");
    if (unbanTitle !== null) {
        unbanTitle.innerText = "Cerere Unban";
    }

    var reasonLabel = document.getElementById("reason-label");
    if (reasonLabel !== null) {
        reasonLabel.innerText = "Motiv";
    }

    var photoLabel = document.getElementById("photo-label");
    if (photoLabel !== null) {
        photoLabel.innerText = "Fotografie cu banul";
    }

    var bugTitle = document.getElementById("bug-title");
    if (bugTitle !== null) {
        bugTitle.innerText = "Raporteaza un bug";
    }

    var submit = document.getElementById("submit");
    if (submit !== null) {
        submit.innerText = "Trimite";
    }

    var argument = document.getElementById("argument");
    if (argument !== null) {
        argument.innerText = "De ce ar trebui sa primesti unban?";
    }

    var rulesBody = document.getElementById("rules-body");
    if (rulesBody !== null) {
        rulesBody.innerHTML = "" +
            "                    <ul>\n" +
            "                        <li> Nu aveti voie sa dezvaluiti informatii confidentiale </li>\n" +
            "                        <li> Membrii Staff sunt obligati sa raspunda la intrebarile jucatorilor chiar daca acestia incalca regulametul (De exemplu, daca un player spameaza o intrebare, trebuie sa le raspunzi dupa ce sunt sanctionati) </li>\n" +
            "                        <li> Membrii Staff nu au voie sa ramana AFK pe sectiuni, datoria lor este sa fie atenti la ceea ce se intampla pe sectiune. </li>\n" +
            "                        <li> Membrii Staff cu functia de Moderator -> HManager nu au voie sa participe la top </li>\n" +
            "                        <li> Membrii Staff nu sunt responsabili de itemele pierdute prin orice metoda, dar pot sanctiona in functie de situatie. Spre exemplu: Constructii in aproprierea protectiei, Trap tpa + kill, /ah scam, etc. (Majoritatea acestor situatii vor fi sanctionate cu WARN) </li>\n" +
            "                        <li> Acest regulament ar trebui sa fie considerat ca un ghid, din acest motiv este scris peste tot \"Mute intre Xmin-Ymin\". Datoria ta este sa fii responsabil / constient si sa actionezi corespunzator in orice situatie (Aminteste-ti, nu esti un robot, nu esti sanctionat daca dai mute cu +/- 5-10min) </li>\n" +
            "                        <li> In cazul in care nu sunteti sigur cum sa actionati intr-o situatie nespecificata in tabel, va rugam sa intrebati Managerii, HManagerii sau Ownerii. </li>\n" +
            "                        <li> Punem accent pe flexebilitate / transparenta / diplomatie. </li>\n" +
            "                        <li> Membrii Staff sunt nevoiti sa respecte si sa ajute jucatorii. </li>\n" +
            "                        <li> Membrii Staff nu au voie sa abuzeze de puterea lor => Incalcand automat regulile de mai sus. </li>\n" +
            "\n" +
            "                    </ul>";
    }

    var rulesTable = document.getElementById("rules-table");
    if (rulesTable !== null) {
        rulesTable.innerHTML = "<table>\n" +
            "                        <tr>\n" +
            "                            <th style=\"text-align: center\">Incalcare</th>\n" +
            "                            <th style=\"text-align: center\">Descriere</th>\n" +
            "                            <th style=\"text-align: center\">Sanctiune</th>\n" +
            "                        </tr>\n" +
            "                        <tr>\n" +
            "                            <td>Spam</td>\n" +
            "                            <td>\n" +
            "                                Orice tip de spam:\n" +
            "                                <ul>\n" +
            "                                    <li>Spam normal</li>\n" +
            "                                    <li>Extensie de cuvant</li>\n" +
            "                                    <li>Spam /msg</li>\n" +
            "                                    <li>Caps-lock</li>\n" +
            "                                    <li>Scrieti mai multe mesaje consecutive (Suficiente pentru a umple chatul)</li>\n" +
            "                                </ul>\n" +
            "                            </td>\n" +
            "                            <td>Mute 10-15 minute</td>\n" +
            "                        </tr>\n" +
            "                        <tr>\n" +
            "                            <td>\n" +
            "                                <ul>\n" +
            "                                    <li>Lipsa de respect</li>\n" +
            "                                    <li>Toxicitate</li>\n" +
            "                                    <li>Limbaj Indecent</li>\n" +
            "                                </ul>\n" +
            "                            </td>\n" +
            "                            <td>\n" +
            "                                Orice tip de mesaj care face referire la\n" +
            "                                <ul>\n" +
            "                                    <li>Ranire</li>\n" +
            "                                    <li>Limbaj Indecent</li>\n" +
            "                                    <li>Toxicitate</li>\n" +
            "                                </ul>\n" +
            "                            </td>\n" +
            "                            <td>Mute 15-30 minute</td>\n" +
            "                        </tr>\n" +
            "                        <tr>\n" +
            "                            <td>\n" +
            "                                <ul>\n" +
            "                                    <li>Informatii false</li>\n" +
            "                                    <li>Trolling in chat</li>\n" +
            "                                </ul>\n" +
            "                            </td>\n" +
            "                            <td>Orice text care deruteaza jucatorii</td>\n" +
            "                            <td>Warn</td>\n" +
            "                        </tr>\n" +
            "                        <tr>\n" +
            "                            <td>\n" +
            "                                <ul>\n" +
            "                                    <li> Reclama directa </li>\n" +
            "                                    <li>Reclama indirecta</li>\n" +
            "                                </ul>\n" +
            "                            </td>\n" +
            "                            <td>Orice mesaj care mentioneaza servere / conturi / informatii personale</td>\n" +
            "                            <td>\n" +
            "                                <ul>\n" +
            "                                    <li>I abatere: Ban 14d</li>\n" +
            "                                    <li>II abatere: Ban perm.</li>\n" +
            "                                </ul>\n" +
            "                            </td>\n" +
            "                        </tr>\n" +
            "                        <tr>\n" +
            "                            <td>Orice link suspect</td>\n" +
            "                            <td>Orice tip de link care pare suspect si care nu provine de pe un site web securizat</td>\n" +
            "                            <td>Mute 30min-3h</td>\n" +
            "                        </tr>\n" +
            "                        <tr>\n" +
            "                            <td>Rasism</td>\n" +
            "                            <td>Orice mijloc de a promova rasismul si hartuirea etc</td>\n" +
            "                            <td>Mute 1h</td>\n" +
            "                        </tr>\n" +
            "                        <tr>\n" +
            "                            <td>Hacking</td>\n" +
            "                            <td>Jucatorul foloseste metode externe in avantajul sau</td>\n" +
            "                            <td>Ban Permanent</td>\n" +
            "                        </tr>\n" +
            "                        <tr>\n" +
            "                            <td>Dezvaluirea informatiilor personale</td>\n" +
            "                            <td>Orice informatie dezvaluita de un jucator (daca este corect sau nu)</td>\n" +
            "                            <td>Ban 14d</td>\n" +
            "                        </tr>\n" +
            "                        <tr>\n" +
            "                            <td>Bug Abuse</td>\n" +
            "                            <td>Orice tip de abuz (indiferent daca jucatorul este constient sau nu)</td>\n" +
            "                            <td>Ban 7d - Ban Permanent</td>\n" +
            "                        </tr>\n" +
            "                        <tr>\n" +
            "                            <td>Folosirea unui VPN</td>\n" +
            "                            <td>Jucatorul foloseste VPN pentru a evita sanctiunile</td>\n" +
            "                            <td>Ban Permanent</td>\n" +
            "                        </tr>\n" +
            "                        <tr>\n" +
            "                            <td>Folosirea dovezilor falsificate</td>\n" +
            "                            <td>Orice modalitate prin care dovezile sunt falsificate pentru a face rau altora</td>\n" +
            "                            <td>Ban 30d</td>\n" +
            "                        </tr>\n" +
            "                        <tr>\n" +
            "                            <td>AFK</td>\n" +
            "                            <td>A sta AFK pe sectiuni in care economia nu se bazeaza pe a fi AFK</td>\n" +
            "                            <td>\n" +
            "                                <ul>\n" +
            "                                    <li>I - Ban 7d</li>\n" +
            "                                    <li>II - Ban 14d</li>\n" +
            "                                    <li>III - Ban perm</li>\n" +
            "                                </ul>\n" +
            "                            </td>\n" +
            "                        </tr>\n" +
            "                        <tr>\n" +
            "                            <td>Abuz Helpop</td>\n" +
            "                            <td>Orice tip de spam sau injuraturi pe Helpop</td>\n" +
            "                            <td>Warn</td>\n" +
            "                        </tr>\n" +
            "                        <tr>\n" +
            "                            <td>Nume indecent</td>\n" +
            "                            <td>Atunci cand un jucator foloseste un nume indecent / inadecvat</td>\n" +
            "                            <td>Ban Permanent</td>\n" +
            "                        </tr>\n" +
            "                        <tr>\n" +
            "                            <td>Nume de iteme false</td>\n" +
            "                            <td>Pentru orice obiect numit care poate induce in eroare jucatorii (Un topor numit „Event”)</td>\n" +
            "                            <td>Warn</td>\n" +
            "                        </tr>\n" +
            "\n" +
            "                    </table>";
    }


}

function unbanTemplate() {
    checkLoggedStatus();

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
