let keyword = "";
let tagBool = false;    //loadTagList 조회 X

//태그 버튼 클릭 이벤트 리스너
document.querySelectorAll('.tag-name').forEach(function(btn) {
    btn.addEventListener('click', function() {
        //모든 버튼에서 .tag-name-active 클래스 제거
        document.querySelectorAll('.tag-name').forEach(function(element) {
            element.classList.remove('tag-name-active');
        });

        //클릭된 버튼에만 tag-name-active 클래스 추가
        btn.classList.add('tag-name-active');
        document.querySelector('.inputBox').style.display = 'none';

        keyword = btn.id; //클릭된 버튼의 id 값을 keyword로 설정

        tagBool = true; //loadTagList 조회 O
        document.querySelector('.donation-line').innerHTML = '';    //요소 초기화
        loadTagList(0);   //첫 페이지 목록 조회
        pageNo = 1;
        scrollList("loadTagList");
    });
});

/*태그별 후원 조회 함수*/
function loadTagList(pageNo) {
    axios.get('/support/search-tag', {
        params: {
            keyword: keyword,
            pageNo: pageNo
        }
    })
    .then(function(response) {
        const supportList = response.data.content;
        totalPages = response.data.totalPages;

        //DocumentFragment 생성
        const fragment = document.createDocumentFragment();

        supportList.forEach(function(supportTag) {
            if(supportTag) {
                //graph의 width 설정(목표금액의 몇 퍼센트 모였는지 보여줌)
                const progress = (supportTag.supportAmount / supportTag.goalAmount) * 100;
                const graphWidth = progress > 100 ? '100%' : progress + '%';
                const graph = '<div class="graph" style="width: ' + graphWidth + ';"></div>';

                let row = `<div class="donation-box">
                                <div class="donation-img-box">
                                    <div class="donation-status ${supportTag.supportStatus === 'ENDING_SOON' ? 'ending-soon' :
                                                                   supportTag.supportStatus === 'END' ? 'end' : ''}">
                                        ${supportTag.supportStatus === 'ENDING_SOON' ? '종료 임박' :
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
                let div = document.createElement('div');
                div.innerHTML = row;

                fragment.appendChild(div.firstChild);
            } else {
                console.log('후원 없음');
            }

            //DOM에 fragment 추가
            const donationLine = document.querySelector('.donation-line');
            donationLine.appendChild(fragment);
        });

        isLoading = false; //데이터 요청 완료
    })
    .catch(function(error) {
        console.error(error);
        isLoading = false;
    });
}