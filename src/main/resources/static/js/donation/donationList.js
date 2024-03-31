/*태그별 후원 조회 함수*/
document.querySelectorAll('.tag').forEach(function(btn) {
    btn.addEventListener('click', function() {
        //모든 버튼에서 .tag-active 클래스 제거
        document.querySelectorAll('.tag').forEach(function(element) {
            element.classList.remove('tag-active');
        });

        //클릭된 버튼에만 .tag-active 클래스 추가
        btn.classList.add('tag-active');

        const keyword = btn.id; //클릭된 버튼의 id 값을 keyword로 설정

        axios.get('/support/search/tag', {
            params: {
                keyword: keyword,
                page: 0
            }
        })
        .then(function(response) {
            const supportList = response.data.content;
            document.querySelector('.donation-line').innerHTML = '';

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

                    document.querySelector('.donation-line').innerHTML += row;
                } else {
                    console.log('후원 없음');
                }
            });
        })
        .catch(function(error) {
            console.error(error);
        });
    });
});

/*donationSearch 페이지 이동*/
document.getElementById('search-btn').addEventListener('click', function() {
    var searchValue = document.querySelector('.search').value;
    window.location.href = "/support/search?search=" + searchValue;
});