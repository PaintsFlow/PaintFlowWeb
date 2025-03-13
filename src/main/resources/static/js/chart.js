// 차트 저장 객체
const charts = {};

// 차트 생성 함수
function createChart(ctx, label, color) {
  return new Chart(ctx, {
    type: 'line',
    data: {
      labels: [], // x축 라벨 (시간)
      datasets: [{
        label: label,
        lineTension: 0.3,
        backgroundColor: color + "20", // 투명도 추가 (ex: "rgba(2,117,216,0.2)")
        borderColor: color,
        pointRadius: 3,
        pointBackgroundColor: color,
        pointBorderColor: "rgba(255,255,255,0.8)",
        pointHoverRadius: 5,
        pointHoverBackgroundColor: color,
        pointHitRadius: 50,
        pointBorderWidth: 2,
        data: [] // 실제 데이터
      }]
    },
    options: {
      animation: false, // 실시간 성능 최적화
      responsive: true,
      scales: {
        x: {
          type: 'time',
          time: { unit: 'second' },
          grid: { display: false },
          title: { display: true, text: '시간' }
        },
        y: {
          beginAtZero: false,
          title: { display: true, text: label }
        }
      },
      legend: { display: false }
    }
  });
}

// 차트 초기화 함수
function initializeCharts() {
  console.log("차트 초기화 시작!");

  const canvasElements = {
    "waterLevel": document.getElementById('waterLevelChart'),
    "viscosity": document.getElementById('viscosityChart'),
    "ph": document.getElementById('phChart'),
    "current": document.getElementById('currentChart'),
    "voltage": document.getElementById('voltageChart'),
    "temperature": document.getElementById('temperatureChart'),
    "humidity": document.getElementById('humidityChart'),
    "paintAmount": document.getElementById('paintAmountChart'),
    "pressure": document.getElementById('pressureChart')
  };

  Object.keys(canvasElements).forEach(key => {
    const canvasElement = canvasElements[key];
    if (canvasElement) {
      const ctx = canvasElement.getContext('2d');
      charts[key] = createChart(ctx, key, getColorForChart(key));
      console.log(`차트 ${key} 생성 완료!`);
    } else {
      console.error(`${key} 차트 canvas 요소를 찾을 수 없음.`);
    }
  });
}

// 차트에 사용할 색상을 반환하는 함수
function getColorForChart(chartName) {
  const colors = {
    "waterLevel": 'rgba(2,117,216,1)',
    "viscosity": 'rgba(231, 76, 60, 1)',
    "ph": 'rgba(39, 174, 96, 1)',
    "current": 'rgba(155, 89, 182, 1)',
    "voltage": 'rgba(243, 156, 18, 1)',
    "temperature": 'rgba(255, 99, 132, 1)',
    "humidity": 'rgba(54, 162, 235, 1)',
    "paintAmount": 'rgba(255, 206, 86, 1)',
    "pressure": 'rgba(75, 192, 192, 1)'
  };
  return colors[chartName] || 'rgba(0, 0, 0, 1)'; // 기본값 검정색
}

// 실시간 데이터 업데이트 함수
function updateCharts(newData) {
  const time = new Date(newData.timestamp); // 타임스탬프 파싱
  console.log("수신된 데이터:", newData);

  Object.keys(charts).forEach(key => {
    if (newData[key] !== undefined) { // 값이 존재하는 경우에만 업데이트
      const chart = charts[key];
      chart.data.datasets[0].data.push({ x: time, y: newData[key] });

      // 최근 20개 데이터 유지 (오래된 데이터 삭제)
      if (chart.data.datasets[0].data.length > 20) {
        chart.data.datasets[0].data.shift();
      }

      chart.update(); // 차트 업데이트
    }
  });
}

// WebSocket 연결 시도
function connectWebSocket() {
	var chartSocket = new SockJS('/ws');
	var chartStompClient = Stomp.over(chartSocket);

  chartStompClient.connect({}, function (frame) {
    console.log('WebSocket 연결됨:', frame);

    chartStompClient.subscribe('/topic/sensorDataUpdate', function (message) {
      try {
        console.log('WebSocket으로 수신한 원본 메시지:', message);  // 원본 메시지 출력

        const data = JSON.parse(message.body);
        console.log('WebSocket으로 수신한 파싱된 데이터:', data);

        // 차트 업데이트
        updateCharts(data);
      } catch (error) {
        console.error('WebSocket 데이터 수신 및 파싱 오류:', error);
      }
    });
  }, function (error) {
    console.error('WebSocket 연결 실패:', error);
    setTimeout(connectWebSocket, 5000); // 연결 실패 시 5초 후 재시도
  });
}

document.addEventListener("DOMContentLoaded", function () {
  console.log("chart.js 로드됨!");
  initializeCharts();
  connectWebSocket();
});

// WebSocket 연결 상태 및 메시지 전송 확인용 로그
socket.onopen = function() {
  console.log("WebSocket 연결이 열렸습니다.");
};

socket.onclose = function() {
  console.log("WebSocket 연결이 종료되었습니다.");
};

socket.onerror = function(error) {
  console.error("WebSocket 오류 발생:", error);
};