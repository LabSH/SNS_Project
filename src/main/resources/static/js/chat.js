// let stompClient = null;
//
// console.log("chart.js 진입!");
//
// function connectWebSocket() {
//     if (stompClient && stompClient.connected) {
//         console.log("✅ 이미 WebSocket이 연결됨!");
//         return;
//     }
//
//     let socket = new SockJS('/ws-chat');  // Spring Boot WebSocket 엔드포인트
//     stompClient = Stomp.over(socket);
//
//     stompClient.connect({}, function (frame) {
//         if (!frame) {
//             console.error("❌ WebSocket 연결 실패!");
//             return;
//         }
//
//         console.log('✅ WebSocket 연결 성공: ' + frame);
//
//         // 구독: 특정 사용자에게만 오는 메시지를 받음
//         let targetId = document.getElementById("targetId").value; // 대상 사용자 ID
//         stompClient.subscribe('/user/' + targetId + '/queue/messages', function (message) {
//             showMessage(JSON.parse(message.body));
//         });
//     });
// }
//
// // 메시지 전송
// function sendMessage() {
//     let messageContent = document.getElementById("chatInput").value.trim();
//     let senderId = document.getElementById("senderId").value;
//     let targetId = document.getElementById("targetId").value;
//
//     if (!stompClient || !stompClient.connected) {
//         console.error("❌ WebSocket이 연결되지 않음. 메시지를 보낼 수 없습니다.");
//         return;
//     }
//
//     if (messageContent) {
//         let chatMessage = {
//             targetId: targetId,
//             senderId: senderId,
//             content: messageContent
//         };
//         stompClient.send("/app/chat", {}, JSON.stringify(chatMessage));  // /app/chat 경로로 메시지 전송
//         document.getElementById("chatInput").value = ""; // 입력창 초기화
//     }
// }
//
// function showMessage(message) {
//     let chatBox = document.querySelector(".chat-box_ch");
//
//     if (!chatBox) {
//         console.error("❌ chat-box_ch 요소를 찾을 수 없습니다!");
//         return;
//     }
//
//     console.log("받은 메시지:", message);  // 메시지 확인 로그
//
//     let messageElement = document.createElement("p");
//
//     // 보낸 사람과 받은 사람 구분
//     if (message.senderId === document.getElementById("senderId").value) {
//         messageElement.innerHTML = `<strong>나:</strong> ${message.content}`;
//         messageElement.style.textAlign = "right";  // 오른쪽 정렬
//     } else {
//         messageElement.innerHTML = `<strong>${message.senderId}:</strong> ${message.content}`;
//         messageElement.style.textAlign = "left";  // 왼쪽 정렬
//     }
//
//     chatBox.appendChild(messageElement);
//
//     // 스크롤 자동으로 아래로 이동
//     chatBox.scrollTop = chatBox.scrollHeight;
// }
//
// // 모달창 열기 & WebSocket 연결
// function openModal(memberName) {
//     console.log(document.getElementById("chatMemberName"));
//     document.getElementById("chatMemberName").textContent = memberName;
//     document.getElementById("targetId").value = document.getElementById('target').value.trim();
//
//     document.getElementById("chatModal").style.display = "block";
//     if (!stompClient || !stompClient.connected) {
//         connectWebSocket();
//     }
// }
//
// // 모달창 닫기
// function closeModal() {
//     document.getElementById("chatModal").style.display = "none";
//
//     if (stompClient) {
//         let targetId = document.getElementById("targetId").value;
//         stompClient.unsubscribe('/user/' + targetId + '/queue/messages'); // 구독 해제
//     }
// }

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

    var senderInfo = document.createElement("strong");
    senderInfo.textContent = message.senderId + ": ";

    var messageContent = document.createElement("span");
    messageContent.textContent = message.content;

    messageElement.appendChild(senderInfo);
    messageElement.appendChild(messageContent);
    chatBox.appendChild(messageElement);

    chatBox.scrollTop = chatBox.scrollHeight;
}

function handleEnter(event) {
    if (event.key === "Enter") {
        sendMessage();
    }
}







