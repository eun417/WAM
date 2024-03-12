/*QnA 목록 조회*/
//현재 페이지 번호를 저장할 변수
var currentPage = 0;

//페이지 로드 시 QnA 목록을 테이블에 표시하는 함수
function loadQnaList(page) {
    axios.get(`/qna/page=${page}`)
        .then(function(response) {
            //서버로부터 받은 데이터를 테이블에 추가
            var qnaList = response.data.content;
            var tableBody = document.querySelector('#qnaTableBody');
            tableBody.innerHTML = ''; //테이블 내용 초기화

            qnaList.forEach(function(qna) {
                var row = `<tr>
                            <td>${qna.qnaId}</td>
                            <td>${qna.title}</td>
                            <td>${qna.nickname}</td>
                            <td>${qna.createDate}</td>
                            <td>${qna.viewCount}</td>
                            <td><button class="answer-btn btn-s bc1" style="cursor: default;">${qna.qnaCheck === 'CHECKING' ? '확인 중' : '답변 완료'}</button></td>
                        </tr>`;
                tableBody.innerHTML += row; // 행 추가
            });

            //페이지 내비게이션 업데이트
            renderPagination('Qna', response.data.totalPages, page);
        })
        .catch(function(error) {
            console.error(error);
        });
}

//페이지 로드 시 첫 번째 페이지 QnA 목록을 불러옴
loadQnaList(currentPage);