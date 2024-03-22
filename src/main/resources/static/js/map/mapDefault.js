//마커 클릭 시 정보창을 열기 위한 InfoWindow 객체 생성
const infoWindow = new naver.maps.InfoWindow();

//좌표 체계 정의
const epsg5186 = "+proj=tmerc +lat_0=38 +lon_0=127 +k=1 +x_0=200000 +y_0=600000 +ellps=GRS80 +units=m +no_defs";
const epsg4326 = "+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs";

//HTTP 요청&응답 함수
async function loadDataAndAddMarkers(apiUrl) {
    try {
        var map = new naver.maps.Map('map', {
            center: new naver.maps.LatLng(36, 127.8), //대한민국 지도 전체적으로 보임
            zoom: 7
        });

        const response = await axios.get(apiUrl);
        const mapDataList = response.data;

        mapDataList.forEach(async (mapData) => {
            //좌표 변환 (EPSG:5186 -> WGS84)
            var p1 = proj4(epsg5186, epsg4326, [mapData.coordinates[0], mapData.coordinates[1]]);

            //마커 생성 및 추가
            var marker = new naver.maps.Marker({
                position: new naver.maps.LatLng(p1[1], p1[0]),    //서식지(위도, 경도)
                title: mapData.speciesName, //동물 이름
                map: map,
                animation: naver.maps.Animation.DROP
            });

            //마커 클릭 시 정보창 열기
            naver.maps.Event.addListener(marker, 'click', function() {
                //현재 정보창이 열려있는지 확인
                if (!infoWindow.getMap()) {
                    //정보창이 열려있지 않다면 열기
                    infoWindow.setContent(`<div class="animal-detail" style="padding:15px;">
                                               <div>이름: ${mapData.speciesName}</div>
                                               <div>조사년도: ${mapData.year}</div>
                                               <div>조사지역: ${mapData.areaName}</div>
                                               <div>조사 시작일자: ${mapData.beginDate}</div>
                                               <div>조사 종료일자: ${mapData.endDate}</div>
                                           </div>`);
                    infoWindow.open(map, marker);

                    //동적 클래스 추가
                    $('.animal-detail').parent().parent().addClass('animal-detail-parent-parent');
                } else {
                    //정보창이 열려있다면 닫기
                    infoWindow.close();
                }
            });
        });
    } catch (error) {
        console.error(error);
    }
}