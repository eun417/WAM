/*후원 목록 조회*/
//현재 페이지 번호를 저장할 변수
var currentPage = 0;

//페이지 로드 시 Support 목록을 테이블에 표시하는 함수
function loadSupportList(page) {
    axios.get(`/admin/support/page=${page}`)
        .then(function(response) {
            //서버로부터 받은 데이터를 테이블에 추가
            var supportList = response.data.content;
            var tableBody = document.querySelector('#supportTableBody');
            tableBody.innerHTML = ''; //테이블 내용 초기화

            supportList.forEach(function(support) {
                var row = `<tr>
                            <td>${support.supportId}</td>
                            <td>${support.title}</td>
                            <td>${support.nickname}</td>
                            <td>${support.goalAmount}</td>
                            <td>${support.supportAmount}</td>
                            <td>${support.createDate}</td>
                        </tr>`;
                tableBody.innerHTML += row; // 행 추가
            });

            //페이지 내비게이션 업데이트
            renderPagination('Support', response.data.totalPages, page);
        })
        .catch(function(error) {
            console.error(error);
        });
}

//페이지 로드 시 첫 번째 페이지 Support 목록을 불러옴
loadSupportList(currentPage);