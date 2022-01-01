async function loginTemplate() {
    loggedStatus = await isLoggedIn();
    if (loggedStatus) {
        redirect("/401");
    }

    document.getElementById('login-submit').addEventListener('click', function () {
        login();
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
