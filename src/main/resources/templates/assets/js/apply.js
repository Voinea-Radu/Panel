async function applyTemplate() {
    await checkLoggedStatus();

    document.getElementById('submit').addEventListener('click', function f() {
        apply();
    });
}

function apply() {
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

async function applyDetails() {
    await checkLoggedStatus();

    bans = document.getElementById("bans").value;
    warns = document.getElementById("warns").value;
    kicks = document.getElementById("kicks").value;
    mutes = document.getElementById("mutes").value;
    document.getElementById("sanctions").innerHTML = "" +
        "                <span class='label default apply-sanction'>Bans: " + bans + "</span>\n" +
        "                <span class='label default apply-sanction'>Kicks: " + kicks + "</span>\n" +
        "                <span class='label default apply-sanction'>Mutes: " + mutes + "</span>\n" +
        "                <span class='label default apply-sanction'>Warnings: " + warns + "</span>";

    var status = document.getElementById("status").value;
    var creator = document.getElementById("user").value;
    var user = JSON.parse(getCookie("login_data"));

    if (user.username !== creator) {
        document.getElementById("logged-in-required").style.visibility = "hidden";
    } else {
        callPutAPI("/api/read?type=apply", {
            cookie: getCookie("login_data"),
            id: document.getElementById("id").value
        });
        return;
    }

    callAPI2(`/api/check/staff?user=${user.username}&useCase=apply`, {}, () => {
        document.getElementById("logged-in-required").style.visibility = "visible";

        if (status === "OPEN") {
            document.getElementById("approve").hidden = false;
            document.getElementById("deny").hidden = false;

            document.getElementById('approve').addEventListener('click', function f() {
                approveApplication();
            });

            document.getElementById('deny').addEventListener('click', function f() {
                denyApplication();
            });
        }
    }, () => {
        redirect("/401");
    })
}

async function approveApplication() {
    callAPI("/api/update/form/apply", {
        cookie: getCookie("login_data"),
        lang: getCookie("lang"),
        decision: "APPROVED",
        id: document.getElementById("id").value
    }, () => {
        window.location.reload();
    }, () => {
        window.location.reload();
    });
}

async function denyApplication() {
    callAPI("/api/update/form/apply", {
        cookie: getCookie("login_data"),
        lang: getCookie("lang"),
        decision: "DENIED",
        id: document.getElementById("id").value
    }, () => {
        window.location.reload();
    }, () => {
        window.location.reload();
    })
}