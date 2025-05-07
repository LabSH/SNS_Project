var stompClient = null;

function initChat(senderId, targetId) {
    var socket = new SockJS('/ws/chat');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('✅ 연결됨: ' + frame);

        // 자신 메시지 수신용 구독
        stompClient.subscribe('/user/queue/messages', function (message) {
            var chatMessage = JSON.parse(message.body);
            showMessage(chatMessage);
        });

        console.log("📩 구독 경로: /user/queue/messages");
    });
}

function sendMessage() {
    var content = document.getElementById("message-input").value;
    if (content && stompClient) {
        var chatMessage = {
            targetId: targetId,
            content: content
        };
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        document.getElementById("message-input").value = '';
    }
}

function showMessage(message) {
    var chatBox = document.getElementById("chat-box");
    var messageElement = document.createElement("div");



    messageElement.classList.add("message");
    if (message.senderId === senderId) {
        messageElement.classList.add("sent");
    } else {
        messageElement.classList.add("received");
    }

    // 이름 (말풍선 위)
    var senderName = (message.senderId === senderId) ? "나" : targetName;
    var messageName = document.createElement("small");
    messageName.classList.add("message-name");
    messageName.textContent = senderName;

    // 말풍선 본문
    var messageContent = document.createElement("div");
    messageContent.classList.add("bubble");
    var contentText = document.createElement("span");
    contentText.textContent = message.content;
    messageContent.appendChild(contentText);

    // 시간 (말풍선 아래)
    var messageTime = document.createElement("small");
    messageTime.classList.add((message.senderId === senderId) ? "message-time-sent" : "message-time-received");


    // 현재 시간을 가져오기
    const now = new Date();

    // 시와 분 추출
    const hours = now.getHours().toString().padStart(2, '0'); // 2자리로 포맷 (예: 03)
    const minutes = now.getMinutes().toString().padStart(2, '0'); // 2자리로 포맷 (예: 05)

    // 시:분 형식으로 출력
    const time = `${hours}시 ${minutes}분`;
    console.log(time); // 예: 14:35
    messageTime.textContent = time

    // 메시지 요소에 추가
    messageElement.appendChild(messageName);
    messageElement.appendChild(messageContent);
    messageElement.appendChild(messageTime);

    chatBox.appendChild(messageElement);

    chatBox.scrollTop = chatBox.scrollHeight;
}


function handleEnter(event) {
    if (event.key === "Enter") {
        sendMessage();
    }
}







