/*페이지 내비게이션을 생성하는 함수*/
function generatePagination(totalPages, currentPage) {
    var pagination = '';
    var maxVisiblePages = 10; // 표시할 최대 페이지 번호 수
    var halfVisiblePages = Math.floor(maxVisiblePages / 2);
    var startPage = Math.max(0, currentPage - halfVisiblePages);
    var endPage = Math.min(totalPages - 1, startPage + maxVisiblePages - 1);

    //이전 버튼 추가
    if (currentPage > 0) {
        pagination += '<li><a onclick="loadList(0)">&laquo;</a></li> '; // 맨 앞 페이지로 이동
    }

    //페이지 번호 추가
    for (var i = startPage; i <= endPage; i++) {
        if (i === currentPage) {
            pagination += '<li><a class="w3-green">' + (i + 1) + '</a></li> ';
        } else {
            pagination += '<li><a onclick="loadList(' + i + ')">' + (i + 1) + '</a></li> ';
        }
    }

    //다음 버튼 추가
    if (currentPage < totalPages - 1) {
        pagination += '<li><a onclick="loadList(' + (totalPages - 1) + ')">&raquo;</a></li>'; // 맨 뒤 페이지로 이동
    }

    return pagination;
}

/*페이지 내비게이션을 HTML에 추가하는 함수*/
function renderPagination(totalPages, currentPage) {
    var paginationContainer = document.getElementById('pagination');

    //전체 페이지 수가 0 이하인 경우 페이지 내비게이션을 생성하지 않음
    if (totalPages <= 0) {
        paginationContainer.innerHTML = '';
        return;
    }

    //페이지 내비게이션을 생성
    var paginationHTML = generatePagination(totalPages, currentPage);

    //페이지 내비게이션을 HTML에 추가
    paginationContainer.innerHTML = paginationHTML;
}