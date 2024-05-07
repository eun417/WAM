//좌표 체계 정의
const epsg5186 = "+proj=tmerc +lat_0=38 +lon_0=127 +k=1 +x_0=200000 +y_0=600000 +ellps=GRS80 +units=m +no_defs";
const epsg4326 = "+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs";

//HTTP 요청&응답 함수
async function loadDataAndAddMarkers(apiUrl) {
    let previousOverlay = null; //이전에 열렸던 overlay

    try {
        const mapContainer = document.getElementById('map'), //지도를 표시할 div
           mapOption = {
              center: new kakao.maps.LatLng(36, 127.8), //지도의 중심좌표
              level: 13 //지도의 확대 레벨
           };

        //지도를 표시할 div와 지도 옵션으로 지도를 생성
        const map = new kakao.maps.Map(mapContainer, mapOption);

        const response = await axios.get(apiUrl);
        const mapDataList = response.data;

       //마커 클러스터러 생성
       let clusterer = new kakao.maps.MarkerClusterer({
           map: map,    //마커들을 클러스터로 관리하고 표시할 지도 객체
           averageCenter: true, //클러스터에 포함된 마커들의 평균 위치를 클러스터 마커 위치로 설정
           minLevel: 10 //클러스터 할 최소 지도 레벨
       });

        //마커들을 저장할 변수 생성(마커 클러스터러 관련)
        let markers = [];

        mapDataList.forEach(async (mapData) => {
            //좌표 변환 (EPSG:5186 -> WGS84)
            const p1 = proj4(epsg5186, epsg4326, [mapData.coordinates[0], mapData.coordinates[1]]);

            //마커 생성
            const marker = new kakao.maps.Marker({
                position: new kakao.maps.LatLng(p1[1], p1[0]), // 서식지(위도, 경도)
                title: mapData.speciesName, // 동물 이름
                map: map
            });

            //생성된 마커를 마커 저장하는 변수에 넣음(마커 클러스터러 관련)
            markers.push(marker);

            const content = document.createElement('div');
            content.innerHTML = `<div class="animal-detail">
                                      <div class="close" title="닫기">
                                        <span class="material-symbols-outlined">close</span>
                                      </div>
                                      <div class="name">${mapData.speciesName}</div>
                                      <div class="info">
                                          <div>조사년도: ${mapData.year}</div>
                                          <div>조사지역: ${mapData.areaName}</div>
                                          <div>조사 시작일자: ${mapData.beginDate}</div>
                                          <div>조사 종료일자: ${mapData.endDate}</div>
                                      </div>
                                  </div>`;

            //마커 클릭 시 정보창 열기
            kakao.maps.event.addListener(marker, 'click', function() {
                //이전 overlay가 열려 있으면 닫기
                if (previousOverlay) {
                    previousOverlay.setMap(null);
                    previousOverlay = null;
                }

                //Overlay 객체 생성
                const overlay = new kakao.maps.CustomOverlay({
                    content: content,
                    map: map,
                    position: marker.getPosition(),
                    yAnchor: 1
//                    zIndex: 3
                });

                //현재 overlay를 이전 overlay로 설정
                previousOverlay = overlay;

                //overlay 열기
                overlay.setMap(map);

                //닫기 버튼 클릭 이벤트
                const closeBtn = content.querySelector('.close');
                closeBtn.addEventListener('click', function() {
                    overlay.setMap(null);
                    previousOverlay = null;
                });
            });
        });

        //클러스터러에 마커들을 추가
        clusterer.addMarkers(markers);
    } catch (error) {
        console.error(error);
    }
}