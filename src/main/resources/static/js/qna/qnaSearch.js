/*페이지 로드 시 함수 실행*/
document.addEventListener('DOMContentLoaded', function() {
    loadList(0);
});


/*QnA 검색 결과 조회*/
function loadList(pageNo) {
    const { search } = getQueryParams();
    document.querySelector('.search').value = search;   //검색창에 검색어 설정

    axios.get('/qna/search-keyword', {
        params: {
            search: search,
            pageNo: pageNo
        }
    }).then(function(response) {
        if (response.data.content.length === 0) {
            //검색 결과가 없는 경우
            alert('검색 결과가 없습니다.');
        }

        var searchList = response.data.content;
        var tableBody = document.querySelector('#qnaTableBody');

        //DocumentFragment 생성
        var fragment = new DocumentFragment();
        tableBody.innerHTML = ''; //테이블 내용 초기화

        searchList.forEach(function(qna) {
            var row = `<tr>
                        <td>${qna.qnaId}</td>
                        <td><a href="/qna/detail/${qna.qnaId}" class="title-hover">${qna.title}</a></td>
                        <td>${qna.nickname}</td>
                        <td>${qna.createDate}</td>
                        <td>${qna.viewCount}</td>
                        <td><button class="answer-btn btn bs1 ${qna.qnaCheck === 'CHECKING' ? 'bc3' : 'bc7'}" style="cursor: default;">${qna.qnaCheck === 'CHECKING' ? '확인 중' : '답변 완료'}</button></td>
                    </tr>`;
            var tr = document.createElement('tr');
            tr.innerHTML = row;

            fragment.appendChild(tr);
        });

        //테이블 내용을 한 번에 추가
        tableBody.innerHTML = '';
        tableBody.appendChild(fragment);

        //페이지 내비게이션 업데이트
        renderPagination(response.data.totalPages, pageNo);
    })
    .catch(function(error) {
        console.error(error);
    });
}

//URL에서 매개변수 추출 함수
function getQueryParams() {
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const search = urlParams.get('q');
    console.log(search);
    return { search };
}