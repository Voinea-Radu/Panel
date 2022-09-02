async function complainsTemplate() {
    await checkLoggedStatus();

    document.getElementById('complain-submit').addEventListener('click', function f() {
        complain()
    });
}

function complain() {
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

async function complaintsDetails() {
    await checkLoggedStatus();
    document.getElementById("logged-in-required").style.visibility = "visible";

    var creator = document.getElementById("user").value;
    var target = document.getElementById("target").value;
    var user = JSON.parse(getCookie("login_data"));

    if (user.username.toLowerCase() === creator.toLowerCase()) {
        callPutAPI("/api/read?type=complain-user", {
            cookie: getCookie("login_data"),
            id: document.getElementById("id").value
        });
    } else if (user.username.toLowerCase() === target.toLowerCase()) {
        callPutAPI("/api/read?type=complain-target", {
            cookie: getCookie("login_data"),
            id: document.getElementById("id").value
        });
    } else {
        document.getElementById("logged-in-required").style.visibility = "hidden";
    }

    if (status === "OPEN_AWAITING_TARGET_RESPONSE") {
        if (target.toLowerCase() === user.username.toLowerCase()) {
            document.getElementById("complain-submit").hidden = false;
            document.getElementById("cancel").hidden = false;
            document.getElementById("target-response").hidden = false;
            document.getElementById("target-response-label").hidden = false;
            document.getElementById("target-response").readOnly = false;

            document.getElementById('complain-submit').addEventListener('click', function f() {
                complainRespond()
            });
        } else {
            document.getElementById("target-response-db").hidden = false;
            document.getElementById("target-response-db-label").hidden = false;
        }
        return;
    }
    await callAPI2(`/api/check/staff?user=${user.username}&useCase=complain`, {}, () => {
        document.getElementById("logged-in-required").style.visibility = "visible";

        if (status === "OPEN_AWAITING_STAFF_APPROVAL") {
            document.getElementById("approve").hidden = false;
            document.getElementById("deny").hidden = false;

            document.getElementById('approve').addEventListener('click', function f() {
                approveComplain();
            });

            document.getElementById('deny').addEventListener('click', function f() {
                denyComplain();
            });
        }
    }, () => {
        if (document.getElementById("logged-in-required").style.visibility === "hidden") {
            redirect("/401");
        }
    });
    if (status === "CLOSED") {
        document.getElementById("target-response").readOnly = true;

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
        lang: getCookie("lang"),
        decision: "APPROVED",
        id: status = document.getElementById("id").value
    }, () => {
        window.location.reload();
    }, () => {
        window.location.reload();
    });
}

async function denyComplain() {
    callAPI("/api/update/form/complain", {
        cookie: getCookie("login_data"),
        lang: getCookie("lang"),
        decision: "DENIED",
        id: status = document.getElementById("id").value
    }, () => {
        window.location.reload();
    }, () => {
        window.location.reload();
    })
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