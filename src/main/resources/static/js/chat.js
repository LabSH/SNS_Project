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
console.log("chat.js 진입!");

function initChat(senderId, targetId) {
    var socket = new SockJS('/ws/chat'); // Spring WebSocket 엔드포인트
    stompClient = Stomp.over(socket);


    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);

        // 자신의 구독 경로 설정 (senderId로 고유 식별)
        stompClient.subscribe('/user/' + senderId + '/queue/messages', function(message) {
            var chatMessage = JSON.parse(message.body);
            console.log('수신된 메시지: ', chatMessage);
            showMessage(chatMessage); // 서버에서 온 메시지를 화면에 표시
        });
        console.log("✅ " + senderId + "번 사용자가 '/user/" + senderId + "/queue/messages' 경로를 구독");

        console.log("✅ 채팅 구독 완료:", senderId);
    });
}

function sendMessage() {
    var messageContent = document.getElementById("message-input").value;

    if (messageContent && stompClient) {
        var chatMessage = {
            senderId: senderId,
            targetId: targetId,
            content: messageContent,
        };

        console.log("📩 메시지 전송:", chatMessage);
        console.log("메시지를 '/user/" + targetId + "/queue/messages' 경로로 전송");

        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        document.getElementById("message-input").value = '';
    }
}

function showMessage(message) {
    console.log("📩 showMessage 호출됨");
    console.log('받은 메시지의 보낸 사람 ID:', message.senderId); // 메시지를 보낸 사람 ID
    console.log('현재 채팅을 보내는 사람 ID:', senderId); // 현재 채팅을 보내고 있는 사람 ID
    console.log('메시지 내용:', message.content); // 메시지 내용


    var chatBox = document.getElementById("chat-box");

    if (!chatBox) {
        console.error("❌ chat-box 요소를 찾을 수 없습니다!");
        return;
    }

    var messageElement = document.createElement("div");

    // 📌 메시지가 보낸 사람이면 'sent', 받는 사람이면 'received' 클래스 추가
    messageElement.classList.add("message");
    if (message.senderId === senderId) {
        messageElement.classList.add("sent");
    } else {
        messageElement.classList.add("received");
    }

    // 📌 보낸 사람 ID + 메시지 내용 추가
    var senderInfo = document.createElement("strong");
    senderInfo.textContent = message.senderId + ": ";

    var messageContent = document.createElement("span");
    messageContent.textContent = message.content;

    messageElement.appendChild(senderInfo);
    messageElement.appendChild(messageContent);

    // 📌 메시지를 채팅 박스에 추가
    chatBox.appendChild(messageElement);

    // 📌 스크롤 자동 이동
    chatBox.scrollTop = chatBox.scrollHeight;
}

function handleEnter(event) {
    if (event.key === "Enter") {
        sendMessage();
    }
}





