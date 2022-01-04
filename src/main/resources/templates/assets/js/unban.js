function unbanTemplate() {
    checkLoggedStatus();

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

    checkLoggedStatus();

    callPutAPI("/api/read?type=unban", {
        cookie: getCookie("login_data"),
        id: document.getElementById("id").value
    });

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
        window.location.reload();
    }, () => {
        window.location.reload();
    })
}