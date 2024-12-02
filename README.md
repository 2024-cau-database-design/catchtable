# catchtable

-- 웨이팅 특정 식당의 대기순위표 조회 예시
curl -X GET "http://localhost:8080/waiting/dynamic-rank?restaurantId=1"

-- 웨이팅 상태 변경 예시
curl -X POST http://localhost:8080/waiting/update-status \             
-d "waitingId=1" \
-d "newStatusId=2"

-- 웨이팅 등록 예시
curl -X POST "http://localhost:8080/waiting" \                         
     -d "customerId=3" \
     -d "guestCount=1" \
     -d "restaurantId=1"
