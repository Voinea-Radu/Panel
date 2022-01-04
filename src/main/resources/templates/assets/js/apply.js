async function applyTemplate() {
    checkLoggedStatus();

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
    checkLoggedStatus();

    callPutAPI("/api/read?type=apply", {
        cookie: getCookie("login_data"),
        id: document.getElementById("id").value
    });

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

    if (status === "OPEN") {
        var user = JSON.parse(getCookie("login_data"));

        callAPI(`/api/check/staff?user=${user.username}&useCase=unban`, {}, () => {
            document.getElementById("approve").hidden = false;
            document.getElementById("deny").hidden = false;

            document.getElementById('approve').addEventListener('click', function f() {
                approveApplication();
            });

            document.getElementById('deny').addEventListener('click', function f() {
                denyApplication();
            });
        })

    }
}

async function approveApplication() {
    callAPI("/api/update/form/apply", {
        cookie: getCookie("login_data"), decision: "APPROVED", id: document.getElementById("id").value
    }, () => {
        window.location.reload();
    }, () => {
        window.location.reload();
    });
}

async function denyApplication() {
    callAPI("/api/update/form/apply", {
        cookie: getCookie("login_data"), decision: "DENIED", id: document.getElementById("id").value
    }, () => {
        window.location.reload();
    }, () => {
        window.location.reload();
    })
}