async function bugsTemplate() {
    await checkLoggedStatus();

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
    await checkLoggedStatus();

    var status = document.getElementById("status").value;
    var creator = document.getElementById("user").value;
    var user = JSON.parse(getCookie("login_data"));

    if (user.username !== creator) {
        document.getElementById("logged-in-required").style.visibility = "hidden";
    } else {
        callPutAPI("/api/read?type=bug", {
            cookie: getCookie("login_data"),
            id: document.getElementById("id").value
        });
        return;
    }

    callAPI2(`/api/check/staff?user=${user.username}&useCase=bug`, {}, () => {
        document.getElementById("logged-in-required").style.visibility = "visible";
        if (status === "OPEN") {
            document.getElementById("close").hidden = false;

            document.getElementById('close').addEventListener('click', function f() {
                closeBug();
            });
        }
    }, () => {
        redirect("/401");
    })
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
