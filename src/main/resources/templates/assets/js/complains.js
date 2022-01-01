function complainsTemplate() {
    checkLoggedStatus();

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