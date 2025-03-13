let currentPage = 1;
const rowsPerPage = 10; // 한 페이지당 표시할 데이터 개수

window.showPage = function(pageId, clickedElement) {
    console.log(`showPage 실행됨: ${pageId}`);

    document.querySelectorAll(".page").forEach(page => {
        page.style.display = "none";
    });

    let page = document.getElementById(pageId);
    if (page) {
        page.style.display = "block";
    }

    document.querySelectorAll(".menu li").forEach(menuItem => {
        menuItem.classList.remove("active");
    });

    if (clickedElement) {
        clickedElement.classList.add("active");
    }
};

window.loadFilteredData = function () {
    const process = document.getElementById("processSelect").value;
    const startDate = document.getElementById("startDate").value;
    const endDate = document.getElementById("endDate").value;

    if (!process || !startDate || !endDate) {
        alert("모든 값을 입력해야 합니다.");
        return;
    }

    // ISO 8601 포맷으로 변환
    const startFormatted = startDate + "T00:00:00";
    const endFormatted = endDate + "T23:59:59";

    console.log(`Fetching data from: /api/${process}/${startFormatted}/${endFormatted}`);

    fetch(`/api/${process}/${startFormatted}/${endFormatted}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`서버 오류: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log("받은 데이터:", data);
            updateProcessTitle(process);
            displayDataInTable(data);
        })
        .catch(error => {
            console.error("데이터 로드 실패:", error);
            document.getElementById("result").innerHTML = `<p style="color: red;">데이터를 불러올 수 없습니다. (${error.message})</p>`;
        });
};

// 알람 데이터 조회
window.loadAlarmData = function () {
    const process = document.getElementById("alarmProcessSelect").value;
    const startDate = document.getElementById("alarmStartDate").value;
    const endDate = document.getElementById("alarmEndDate").value;

    if (!process || !startDate || !endDate) {
        alert("모든 값을 입력해야 합니다.");
        return;
    }

    const startFormatted = startDate + "T00:00:00";
    const endFormatted = endDate + "T23:59:59";

    console.log(`Fetching alarm data from: /api/alarm/${process}/${startFormatted}/${endFormatted}`);

    fetch(`/api/alarm/${process}/${startFormatted}/${endFormatted}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`서버 오류: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log("받은 알람 데이터:", data);
            displayAlarmDataInTable(data);
        })
        .catch(error => {
            console.error("알람 데이터 로드 실패:", error);
            document.getElementById("alarmResult").innerHTML = `<p style="color: red;">알람 데이터를 불러올 수 없습니다. (${error.message})</p>`;
        });
};

function displayAlarmDataInTable(data) {
    const tableHeader = document.getElementById("alarmTableHeader");
    const tableBody = document.getElementById("alarmTableBody");

    tableHeader.innerHTML = "<th>시간</th><th>센서</th><th>데이터</th><th>메시지</th>";
    tableBody.innerHTML = "";

    if (!data || data.length === 0) {
        tableBody.innerHTML = "<tr><td colspan='4'>조회된 데이터가 없습니다.</td></tr>";
        return;
    }

    data.forEach(row => {
        let tr = document.createElement("tr");
        tr.innerHTML = `<td>${row.time}</td><td>${row.sensor}</td><td>${row.data}</td><td>${row.message}</td>`;
        tableBody.appendChild(tr);
    });
}

function updateProcessTitle(process) {
    const titleMap = {
        "electroDeposition": "하도 전착",
        "dry": "건조 환경",
        "paint": "도장 공정"
    };
    document.getElementById("selectedProcessTitle").textContent = titleMap[process] || "데이터 표시";
}

function displayDataInTable(data) {
    const tableHeader = document.getElementById("tableHeader");
    const tableBody = document.getElementById("tableBody");
    const paginationDiv = document.getElementById("pagination");

    tableHeader.innerHTML = "";
    tableBody.innerHTML = "";
    paginationDiv.innerHTML = "";

    if (!data || data.length === 0) {
        tableBody.innerHTML = "<tr><td colspan='100%'>조회된 데이터가 없습니다.</td></tr>";
        return;
    }

    // 테이블 헤더 생성 (한글 변환)
    const headers = Object.keys(data[0]);
    const headerNames = {
        "time": "시간",
        "waterLevel": "수위",
        "viscosity": "점도",
        "ph": "PH",
        "current": "전류",
        "voltage": "전압",
        "temperature": "온도",
        "humidity": "습도",
        "paintAmount": "페인트유량",
        "pressure": "스프레이건압력"
    };

    headers.forEach(header => {
        let th = document.createElement("th");
        th.textContent = headerNames[header] || header;
        tableHeader.appendChild(th);
    });

    // 총 페이지 수 계산
    const totalPages = Math.ceil(data.length / rowsPerPage);
    updateTable(data, currentPage, totalPages);
}

function updateTable(data, page, totalPages) {
    const tableBody = document.getElementById("tableBody");
    const paginationDiv = document.getElementById("pagination");
    tableBody.innerHTML = "";
    paginationDiv.innerHTML = "";

    let start = (page - 1) * rowsPerPage;
    let end = start + rowsPerPage;
    let paginatedItems = data.slice(start, end);

    paginatedItems.forEach(row => {
        let tr = document.createElement("tr");
        Object.values(row).forEach(value => {
            let td = document.createElement("td");
            td.textContent = value || "-";
            tr.appendChild(td);
        });
        tableBody.appendChild(tr);
    });

    // 페이지네이션 버튼
    let prevButton = document.createElement("button");
    prevButton.textContent = "◀";
    prevButton.disabled = (page === 1);
    prevButton.classList.add("page-btn");
    prevButton.addEventListener("click", () => {
        if (currentPage > 1) {
            currentPage--;
            updateTable(data, currentPage, totalPages);
        }
    });
    paginationDiv.appendChild(prevButton);

    let pageIndicator = document.createElement("span");
    pageIndicator.textContent = ` ${page} / ${totalPages} `;
    paginationDiv.appendChild(pageIndicator);

    let nextButton = document.createElement("button");
    nextButton.textContent = "▶";
    nextButton.disabled = (page === totalPages);
    nextButton.classList.add("page-btn");
    nextButton.addEventListener("click", () => {
        if (currentPage < totalPages) {
            currentPage++;
            updateTable(data, currentPage, totalPages);
        }
    });
    paginationDiv.appendChild(nextButton);
}

document.addEventListener("DOMContentLoaded", function() {
    console.log("DOMContentLoaded 실행됨");
});

function downloadData() {
    const process = document.getElementById("processSelect").value;
    const startDate = document.getElementById("startDate").value;
    const endDate = document.getElementById("endDate").value;

    if (!process || !startDate || !endDate) {
        alert("모든 값을 입력해야 합니다.");
        return;
    }

    // ISO 형식 변환
    const startFormatted = new Date(startDate).toISOString().split('.')[0]; // 초까지만 사용
    const endFormatted = new Date(endDate).toISOString().split('.')[0];

    // 다운로드 요청
    window.location.href = `/api/download/${process}/${startFormatted}/${endFormatted}`;
}
