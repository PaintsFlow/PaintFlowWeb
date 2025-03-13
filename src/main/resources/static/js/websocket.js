var alarmSocket = new SockJS('/ws');
var alarmStompClient = Stomp.over(alarmSocket);

alarmStompClient.connect({}, function (frame) {
    console.log("Connected to WebSocket: " + frame);

    // WebSocket 메시지 구독 (센서 데이터)
    alarmStompClient.subscribe('/topic/sensorData', function (message) {
        var sensorData = JSON.parse(message.body);
        displayLiveAlarm(sensorData); // 🔥 실시간 알람 표시
    });
});

// 실시간 알람 표시 함수
function displayLiveAlarm(data) {
    var alarmContainer = document.getElementById("liveAlarmContainer");
    
    var alarmDiv = document.createElement("div");
    alarmDiv.classList.add("live-alarm-item");
    
    alarmDiv.innerHTML = `
        <strong>${data.sensorName}</strong> (${data.timestamp})<br>
        값 1: ${data.value1}, 값 2: ${data.value2}, 상태: <strong>${data.status}</strong>
    `;

    // HIGH 상태일 경우 강조
    if (data.status === "HIGH") {
        alarmDiv.style.backgroundColor = "red";
        alarmDiv.style.color = "white";
    } else {
        alarmDiv.style.backgroundColor = "lightgray";
    }

    // 새로운 알람을 맨 위에 추가 (최신 데이터 우선)
    alarmContainer.prepend(alarmDiv);

    // 10초 후 자동 삭제 (선택 사항)
    setTimeout(() => {
        alarmDiv.remove();
    }, 10000);
}
