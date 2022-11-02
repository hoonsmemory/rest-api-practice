# rest-api-practice
REST API

아키텍쳐
* Client-Server
* Stateless
* Cache 
* Uniform Interface 
* Layered System
* Code-On-Demand (optional) 

Uniform Interface 
Self-descriptive message 
* 메시지 스스로 메시지에 대한 설명이 가능해야 한다.
* 서버가 변해서 메시지가 변해도 클라이언트는 그 메시지를 보고 해석이 가능하다.
* 확장 가능한 커뮤니케이션

HATEOAS
* 하이퍼미디어(링크)를 통해 애플리케이션 상태 변화가 가능해야 한다.
* 링크 정보를 동적으로 바꿀 수 있다. (Versioning 할 필요 없이!) 


Self-descriptive message 해결 방법 
* 방법 1: 미디어 타입을 정의하고 IANA에 등록하고 그 미디어 타입을 리소스 리턴할 때 Content-Type으로 사용한다. 
* 방법 2: profile 링크 헤더를 추가한다 . ( 발표 영상 41 분 50초 ) 
    * 브라우저들이 아직 스팩 지원을 잘 안해
    * 대안으로 HAL 의 링크 데이터에 p rofile 링크 추가 

HATEOAS 해결 방법 
* 방법1: 데이터에 링크 제공
    * 링크를 어떻게 정의할 것인가? —> HAL
* 방법2: 링크 헤더나 Location을 제공 
