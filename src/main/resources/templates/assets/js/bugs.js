function bugsTemplate() {
    checkLoggedStatus();

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
