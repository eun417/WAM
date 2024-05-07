document.addEventListener('DOMContentLoaded', function() {
    loadSupportList(0);
});

//전체 페이지 수
let totalPages = 0

/*후원 검색 목록 조회*/
function loadSupportList(pageNo) {
    const { search } = getQueryParams();
    document.querySelector('.search').value = search;   //검색창에 검색어 설정

    axios.get('/support/search-keyword', {
        params: {
            keyword: search,
            pageNo: pageNo
        }
    })
    .then(function(response) {
        const searchList = response.data.content;
        totalPages = response.data.totalPages;

        //DocumentFragment 생성
        const fragment = document.createDocumentFragment();

        if (searchList.length === 0) {
            noResultDiv(fragment);
        } else {
            searchList.forEach(function(support) {
                //graph의 width 설정(목표금액의 몇 퍼센트 모였는지 보여줌)
                const progress = (support.supportAmount / support.goalAmount) * 100;
                const graphWidth = progress > 100 ? '100%' : progress + '%';
                const graph = '<div class="graph" style="width: ' + graphWidth + ';"></div>';

                let row = `<div class="donation-box">
                                <div class="donation-img-box">
                                        <div class="donation-status ${support.supportStatus === 'START' ? 'start' :
                                                                       support.supportStatus === 'ENDING_SOON' ? 'ending-soon' :
                                                                       support.supportStatus === 'END' ? 'end' : ''}">
                                            ${support.supportStatus === 'START' ? '후원 시작' :
                                              support.supportStatus === 'ENDING_SOON' ? '종료 임박' :
                                              support.supportStatus === 'END' ? '후원 종료' : ''}
                                        </div>
                                    <div class="donation-img"><img src="${support.firstImg}" class="first-pic" alt="animal"></div>
                                </div>
                                <a href="/support/detail/${support.supportId}" class="donation-title title-hover">${support.title}</a>
                                <div class="donation-nickname gray-text">${support.nickname}</div>
                                <div class="donation-amount">
                                    <div class="percent">
                                        ${graph}
                                        <div class="graph-bottom"></div>
                                    </div>
                                    <p class="support-amount"><span>${support.supportAmount}</span>원</p>
                                </div>
                            </div>`;
                let div = document.createElement('div');
                div.innerHTML = row;

                fragment.appendChild(div.firstChild);
            });
        }

        //DOM에 fragment 추가
        const donationLine = document.querySelector('.donation-line');
        donationLine.appendChild(fragment);
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