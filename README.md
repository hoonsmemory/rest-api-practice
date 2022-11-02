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
* 방법 2: profile 링크 헤더를 추가한다.
    * 브라우저들이 아직 스팩 지원을 잘 안한다.
    * 대안으로 HAL 의 링크 데이터에 p rofile 링크 추가 

HATEOAS 해결 방법 
* 방법1: 데이터에 링크 제공
    * 링크를 어떻게 정의할 것인가? —> HAL
* 방법2: 링크 헤더나 Location을 제공


<br/><br/>
RESTful Web Service 설계 시 고려해야할 사항
1. 개발자 입장이 아닌 해당 API 명령어를 사용하는 소비자 입장에서 간단 명료하고 직관적으로 설계해야 한다. 
2. HTTP Method, Header 등 HTTP의 장점을 살려서 구현해야 한다.
3. 성공 혹은 실패에 따라 Response Status를 남겨야 한다. ex) /users/10 GET —> 만약 데이터가 없다면 404
4. 보안…
5. 리소스는 복수형을 사용하는 것이 좋다 /users … 만약 단수로 사용하고 싶다면 /users/1
