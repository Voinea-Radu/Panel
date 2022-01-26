async function loadNotifications() {
    notificationsParent = document.getElementById("notifications");
    notifications = await getNotifications();

    notifications.forEach(notification => {
        var el = document.createElement("div");
        el.innerText = notification.textEn;
        el.onclick = function () {
            redirect(notification.url);
        };
        el.classList.add("notification");
        notificationsParent.appendChild(el);
        notificationsParent.appendChild(document.createElement("br"));
    })

}

async function getNotifications() {

    var user = JSON.parse(getCookie("login_data"));

    var blob = await fetch("/api/getNotifications?username=" + user.username, {
        method: 'post', headers: {
            'Accept': 'application/json', 'Content-Type': 'application/json'
        }
    }).then(response => response.blob());

    json = await blob.text();

    try {
        obj = JSON.parse(json);
        return obj;
    } catch (error) {

    }
    return [];
}
