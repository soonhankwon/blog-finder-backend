# blog-finder-backend
> 블로그 검색 서비스 프로젝트 **blog-finder**
- [Blog-Finder Notion Page](https://amusing-child-e0e.notion.site/blog-finder-backend-2ff4a56ab65a41198df5404fd3ebd44a)

## 구현 기능
> * 카카오 API를 사용한 키워드를 통한 블로그 검색 기능 
> * 최신순, 정확도순 블로그 검색 기능
> * 검색결과 Pagination
> * 인기 검색어 TOP10 과 인기 검색어 별 검색횟수 제공 기능

## TECH STACK
- Java 11
- SpringBoot 2.7.9
- Spring WebFlux
- Gradle
- MySQL 8.0.31
- JPA
- Kafka 3.1.2
- resilience4j 1.7.1
- Docker
- Swagger 3.0
- JUnit5, Mockito
- Apache Jmeter

## API
- [상세 API Notion Page](https://amusing-child-e0e.notion.site/Blog-Finder-API-34302840b73c42098c9171447ce15352)
- [Swagger UI - API](http://localhost:8080/swagger-ui/index.html)

<details>
<summary><strong> 프로젝트 실행방법 </strong></summary>
<div markdown="1">
<br>

1. Docker로 카프카 및 주키퍼 이미지를 가져온다.
````Shell
docker pull bitnami/kafka:latest
docker pull bitnami/zookeeper:latest
````
2. 터미널에서 프로젝트의 blog-finder-consumer/resources 까지 이동 및 아래의 명령어를 실행한다.
````Shell
docker-compose up
````
3. blog-finder-consumer (localhost:8081) 어플리케이션을 실행한다.
4. blog-finder-consumer/resources 에서 아래의 명령어를 실행한다.
````Shell
docker-compose start
````
- kafka 서버가 구동되지 않았다면, **docker-compose start**를 다시한번 실행한다.
5. blog-consumer-api (localhost:8080) 어플리케이션을 실행한다.

</div>
</details>

## 프로젝트 설계 내용 및 이유

### 개요
- 이 프로젝트는 **카카오 오픈 API**를 사용하여 구현한 **블로그 검색 서비스**입니다.
- 트래픽이 많고, 저장되어 있는 데이터가 많음을 고려하여 **멀티 모듈 아키텍처**를 사용하여 **모듈간 의존성을 제약**하여 설계하였습니다.
- API간 통신에 **Async & Non-blocking** 방식의 **WebClient & WebFlux**를 사용하여 서버 성능 개선
- 실시간 검색 이벤트 발생시 키워드 데이터를 **ApplicationListener & @Async**를 사용하여 consumer 모듈로 **비동기 전송**하여 검색 기능에 영향을 받지 않도록 개선
- 해당 기술 적용 간 Jmeter를 통한 **성능 및 동시성 테스트**를 통한 결과를 비교해서 적용, 검색 성능 **약20%**, 동시 접속시 성능 **약200%** 개선 & 동시성 이슈 시 데이터 정확도 검증 
- 키워드 별로 **검색된 횟수의 정확도**를 보장하기 위해서 Key-Value 구조의 **카프카 메세지 브로커**를 사용하여 검색 횟수를 저장
- 카프카를 통한 검색 키워드 데이터 수집 & API 모듈에서 **인기검색어 TOP10**을 제공하도록 설계 
- 카카오 블로그 검색 API가 외부상황에 의해 장애가 발생할 경우 **서킷브레이커**를 오픈하여 네이버 블로그 API를 통해 데이터를 제공하도록 구현

### Entity Modeling

#### Keyword Table

> - ID : 인조키 (PK)
> - WORD :  단어 (Index)
> - COUNT : 검색횟수 (Index)

#### Data Type
> - VARCHAR(255) : 키워드의 길이는 구글 기준(90자)임으로 충분하다고 예상
> - BIGINT : 대용량 트래픽이 예상되는 검색횟수에 사용

### 멀티 모듈 구성
> - blog-finder-api : 비동기 방식으로 오픈 API에서 데이터를 받아서 제공 & consumer 모듈로 실시간 데이터 전송
> - blog-finder-core : keyword **도메인** 모듈
> - blog-finder-consumer : kafka message broker를 기반으로한 **데이터 수집 모듈** & DB로 실시간 데이터를 저장
> - API -> CORE <- CONSUMER
> - CORE는 **도메인**과 핵심 비즈니스로직 담당. 따라서 CORE는 도메인에만 집중할 수 있도록 구성
> - 도메인 자체의 서비스 로직에 집중할 수 있어 테스트 용이 및 도메인을 사용하는 타 모듈에서 중복 코드를 제거할 수 있도록 개선
> - MySQL DB는 http://localhost:3306/{DB} 사용하도록 구축

### 카카오 API의 키워드로 블로그 검색
> - **카카오 API** 키워드 블로그 검색 사용 [카카오 API 링크](https://developers.kakao.com/docs/latest/ko/daum-search/dev-guide#search-blog)
> - 카카오 API 장애시 네이버 블로그 검색 API 사용 [네이버 API 링크](https://developers.naver.com/docs/serviceapi/search/blog/blog.md) 

### 검색결과 Sorting & Pagination
<details>
<summary><strong> 카카오 API의 sort 와 page 사용, size 기본값 10 </strong></summary>
<div markdown="1">
  
- sort : accuracy (정확도순)
  
- sort : recency (최신순)
  
- size : 한 번에 보여줄 검색결과 수 (default : 10)
  
- page : 페이지네이션

````Java
WebClient.builder()
                .baseUrl(apiReqValueStorage.getKakaoUrl())
                .build().get()
                .uri(builder -> builder.path(apiReqValueStorage.getKakaoPath())
                        .queryParam("query", query)
                        .queryParam("sort", sortType.getValue())
                        .queryParam("page", apiReqValueStorage.getKakaoPagination())
                        .build())
```` 
  
</div>
</details>

<details>
<summary><strong> 네이버 API의 sort 와 display 사용 </strong></summary>
<div markdown="1">
  
- sort : sim (정확도순)

- sort : date (최신순)

- display : 10 (한 번에 표시할 검색 결과 개수)

````Java
 WebClient.builder()
                .baseUrl(apiReqValueStorage.getNaverUrl())
                .build().get()
                .uri(uriBuilder -> uriBuilder.path(apiReqValueStorage.getNaverPath())
                        .queryParam("query", query)
                        .queryParam("display", apiReqValueStorage.getNaverDisplay())
                        .queryParam("sort", sortType.getValue())
                        .build())
```` 
</div>
</details>

### 인기검색어 TOP10 조회
> - blog-finder-consumer 를 통해 수집 & 저장된 데이터를 blog-finder-api 에서 요청하도록 구현
> - JPA를 사용하여 검색횟수를 내림차순으로 정렬 후 TOP10 키워드 & 검색횟수 리스트 조회
> - word, count **인덱스** 적용으로 쿼리 최적화
````Java
List<Keyword> findTop10ByOrderByCountDesc();
````
## 핵심 문제해결 전략 및 분석

### 대량 트래픽 및 데이터 핸들링 이슈
> * 대량의 트래픽 및 이에 따른 데이터가 많음을 염두한다.
#### 오픈 API사용시 Spring WebFlux를 사용한 비동기식 API 모듈 구성
> - 블로그 검색 서비스의 특성상 한 자원에 동시에 접근해도 무방하다고 판단 **비동기식 module-api** 구축
> - **WebFlux**에서 제공하는 리액티브 프로그래밍을 사용하여, 이를 **WebClient**에서 **Async & Non-Blocking**으로 요청 처리, **Mono**로 응답을 받도록 구현
> - 서버에서 수행되는 작업 중 하나가 완료되기를 기다리는 동안 다른 작업을 수행할 수 있어 서버의 성능과 처리량을 향상
> - 따라서 대규모 트래픽과 데이터 핸들링의 안정성을 개선 

#### 이벤트 기반 + 비동기처리로 데이터 수집 모듈인 blog-finder-consumer로 실시간 데이터 전송
> - 빠른 이벤트 처리를 위해 컨트롤러 단에서 이벤트가 발생하도록 테스트 후 적용 (데이터 정합성 보다는 **검색 기능**에 초점)
> - **ApplicationEventPublisher**를 사용하여 SearchEvent를 Pub
> - 데이터 정확성을 위해 TransactionalEventListener 사용 & **@Async**를 사용하여 비동기 처리 전송 
> - 이벤트 기반 비동기처리로 오픈 API에서 받는 응답 작업에 영향이 없도록 결합도 및 성능 개선
<details>
<summary><strong> CODE </strong></summary>
<div markdown="1">
 
````Java  
public Mono<List<SearchResultDto>> apiSearchAccuracy(@RequestParam("query") String query, @RequestParam("sortType") String sortType) {
        applicationEventPublisher.publishEvent(new SearchEvent(this, new Keyword(query)));
        return keywordSearchServiceRouter.searchByKakao(query, sortType);
    }
````

</div>
</details>

### 검색어 데이터 수집시 동시성 이슈
#### 카프카 메세지 브로커를 사용한 키워드 별 검색 횟수의 정확도 보장
> - blog-finder-api 에서 보내는 실시간 검색어 데이터를 blog-finder-consumer 의 **카프카 메세지 브로커**가 받도록 설계
> - 후보군으로 같은 Key-Value 구조의 Redis를 생각하였으나, 대량의 트래픽과 데이터에 적합하지 않다고 생각(캐싱 비용)
> - 카프카를 사용함으로써 검색어를 **고유한 파티션 키**로 사용하여 해싱을 통해 동일한 레코드들이 동일한 파티션에 도착하는 것을 보장
> - 메세지를 consume 하면서 DB에 키워드와 검색횟수를 저장 및 업데이트 하도록 구현
<details>
<summary><strong> CODE </strong></summary>
<div markdown="1">
 
````Java  
@Async
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onApplicationEvent(SearchEvent event) {
        log.info("Search Event On!!!!");
        String keyword = event.getKeyword().getWord();
        String topic = "keyword";
        kafkaTemplate.send(topic, keyword, keyword);
        log.info("Kafka Producer sent data : " + keyword);
    }
````

</div>
</details>

### 테스트를 통한 성능 개선 및 버그 픽스
#### WebFlux 와 @Async를 적용한 Event & Listener 비동기기술 적용 시 테스트를 통한 버그 픽스
> - WebFlux & Client 의 **Async & Non-Blocking** 방식으로 이벤트 Pub 시 Mono의 데이터 방출 방식과 트랜잭션의 타이밍이 맞지않아 중복 이벤트가 발생하는 문제 발생
> - Jmeter 테스트를 진행하면서, 중복 이벤트 발생 케이스인 EventListener, 검색 비즈니스 로직 메서드 안에서 이벤트 Pub 시 두 가지 케이스 캐치
> - EventListener -> TransactionalEventListener
> - 비즈니스 로직 안에서 이벤트 Pub -> 컨트롤러 단에서 이벤트 Pub 으로 버그 픽스

#### Jmeter를 사용하여 **성능 및 동시성 테스트** 진행 성능 개선 및 데이터 정확도 검증
> - 성능 테스트를 통하여 점진적으로 성능 개선 
> - 기존 방식 대비 Latency avg **약 20%** , TPS **약 21.3%** 개선
- [Jmeter 성능 테스트 노션 페이지](https://amusing-child-e0e.notion.site/edbefac60ab0436fba290a915b8abeb4)
> - 동시성 테스트를 통한 다중 사용자 접속시 성능 개선 & 검증
> - 기존 방식 대비 Latency avg **약 252%** 개선, TPS **약 187%** 개선
- [Jmeter 동시성 테스트 노션 페이지](https://amusing-child-e0e.notion.site/87c1948dce754c979541e0873c7bbaa6)
> - DB에서 카운트를 확인하여 동시성 발생시 데이터 정확도 검증 & 에러율 0%

### 카카오API 장애 발생 시 네이버 블로그 검색API 데이터 제공
#### resilience4j를 사용한 서킷브레이커로 API 전환 및 장애 회복
> - 카카오 API 장애 발생 시 네이버 API로 전환하도록 하여 **검색 기능 회복** 
> - blog-finder-api 와 blog-finder-consumer 사이의 의존성은 없지만, API에서 CONSUMER로 데이터를 실시간 전송하고 있는 구조
> - 따라서, 데이터 수집의 정확성에 영향을 받게 될 가능성 또한 있다.
> - 호스팅할 경우 서버가 가지고 있는 스레드가 API서버와 통신하는 데 몰리게될 것 임으로 모든 모듈로의 **장애 전파** 가능성
> - **resilience4j**를 사용한 **서킷브레이커** 의 슬라이딩 윈도우를 사용 
> - 슬라이딩 윈도우의 체크 구간을 이동하면서 **에러가 60% 이상** 일어나면 서킷브레이커가 오픈하도록 설정.
> - 10분동안 네이버 API로 전환하도록 fallbackMethod를 구현, 10분이 지나면 **half-open** 상태로 전환
> - half-open 상태에서 기존 카카오 API로 요청을 보내고 API가 정상 응답을 하면 다시 카카오 API로 전환하여 장애를 회복하도록 구축
