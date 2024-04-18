let pageNo = 0;

/*페이지 로드 시 함수 실행*/
document.addEventListener('DOMContentLoaded', function() {
    loadList(0);
});


//무한 스크롤
window.addEventListener('scroll', function() {
    const scrollHeight = document.documentElement.scrollHeight; //문서의 총 높이
    const scrollTop = window.pageYOffset || document.documentElement.scrollTop; //현재 스크롤 위치
    const clientHeight = document.documentElement.clientHeight; //현재 보이는 화면의 높이

    //스크롤이 페이지 하단에 도달했는지 확인
    if (scrollTop + clientHeight >= scrollHeight - 10) {
        //현재 스크롤 위치 + 화면 높이 = 현재 화면의 맨 아래 위치

        //페이지 번호 증가
        pageNo++;

        //추가 콘텐츠 로드
        loadList(pageNo);
    }
});


/*후원 목록 조회*/
function loadList(pageNo) {
    axios.get('/support', {
        params: {
            pageNo: pageNo
        }
    })
    .then(function(response) {
        var supportList = response.data.content;

        //DocumentFragment 생성
        var fragment = document.createDocumentFragment();

        supportList.forEach(function(support) {
            //graph의 width 설정(목표금액의 몇 퍼센트 모였는지 보여줌)
            const progress = (support.supportAmount / support.goalAmount) * 100;
            const graphWidth = progress > 100 ? '100%' : progress + '%';
            const graph = '<div class="graph" style="width: ' + graphWidth + ';"></div>';

            var row = `<div class="donation-box">
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
            var div = document.createElement('div');
            div.innerHTML = row;

            fragment.appendChild(div.firstChild);
        });

        //DOM에 fragment 추가
        var donationLine = document.querySelector('.donation-line');
        donationLine.appendChild(fragment);

        //페이지 내비게이션 업데이트
        //renderPagination('Support', response.data.totalPages, pageNo);
    })
    .catch(function(error) {
        console.error(error);
    });
}


//태그 버튼 클릭 이벤트 리스너
document.querySelectorAll('.tag').forEach(function(btn) {
    btn.addEventListener('click', function() {
        //모든 버튼에서 .tag-active 클래스 제거
        document.querySelectorAll('.tag').forEach(function(element) {
            element.classList.remove('tag-active');
        });

        //클릭된 버튼에만 tag-active 클래스 추가
        btn.classList.add('tag-active');

        const keyword = btn.id; //클릭된 버튼의 id 값을 keyword로 설정
        loadSupportByTag(keyword, 0);  //데이터 조회
    });
});

/*태그별 후원 조회 함수*/
function loadSupportByTag(keyword, pageNo) {
    axios.get('/support/search-tag', {
        params: {
            keyword: keyword,
            pageNo: pageNo
        }
    })
    .then(function(response) {
        const supportList = response.data.content;
        document.querySelector('.donation-line').innerHTML = '';

        //DocumentFragment 생성
        var fragment = document.createDocumentFragment();

        supportList.forEach(function(supportTag) {
            if(supportTag) {
                //graph의 width 설정(목표금액의 몇 퍼센트 모였는지 보여줌)
                const progress = (supportTag.supportAmount / supportTag.goalAmount) * 100;
                const graphWidth = progress > 100 ? '100%' : progress + '%';
                const graph = '<div class="graph" style="width: ' + graphWidth + ';"></div>';

                var row = `<div class="donation-box">
                                <div class="donation-img-box">
                                    <div class="donation-status start">
                                        ${supportTag.supportStatus === 'START' ? '후원 시작' :
                                        supportTag.supportStatus === 'SUPPORTING' ? '후원중' :
                                        supportTag.supportStatus === 'ENDING_SOON' ? '종료 임박' :
                                        supportTag.supportStatus === 'END' ? '후원 종료' : ''}
                                    </div>
                                    <div class="donation-img"><img src="${supportTag.firstImg}" class="first-pic" alt="animal"></div>
                                </div>
                                <a href="/support/detail/${supportTag.supportId}" class="donation-title title-hover">${supportTag.title}</a>
                                <div class="donation-nickname gray-text">${supportTag.nickname}</div>
                                <div class="donation-amount">
                                    <div class="percent">
                                        ${graph}
                                        <div class="graph-bottom"></div>
                                    </div>
                                    <p class="support-amount"><span>${supportTag.supportAmount}</span>원</p>
                                </div>
                            </div>`;
                var div = document.createElement('div');
                div.innerHTML = row;

                fragment.appendChild(div.firstChild);
            } else {
                console.log('후원 없음');
            }

            //DOM에 fragment 추가
            var donationLine = document.querySelector('.donation-line');
            donationLine.appendChild(fragment);

            //페이지 내비게이션 업데이트
            //renderPagination('Support', response.data.totalPages, pageNo);
        });
    })
    .catch(function(error) {
        console.error(error);
    });
}