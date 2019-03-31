var stompClient = null;
var uid = uuidv4();

function connect(event) {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);
    if (event) {
        event.preventDefault();
    }
}

function onConnected() {
    stompClient.subscribe('/topic/' + place + '/' + secret + '/' + uid, onMessage);
}

function onError(error) {
    onMessage(error);
}

function sendMessage(destination, value) {
    if (value && stompClient) {
        stompClient.send(destination, {}, JSON.stringify(value));
    }
}

function onMessage(m) {
    console.log(m);

    if (m.headers.authSuccess === "1") {
        sendMessage("/ctrl/table/load", {mode: "default"});
        return;
    }

    if (m.headers.authSuccess === "0") {
        stompClient.disconnect();
        alert(place + " permission denied");
        return;
    }

    if (m.headers.type === "TableLoadResponse") {
        var loadData = JSON.parse(m.body);
        doTableLoad(loadData);
        return;
    }
}

function uuidv4() {
    return ([1e7] + -1e3 + -4e3 + -8e3 + -1e11).replace(/[018]/g, c =>
        (c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> c / 4).toString(16)
    )
}
