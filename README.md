# SPRING PLUS

## RDS, S3 적용

### 1. RDS 적용

- 로컬 컴퓨터에서 적용 성공

![RDS 연결 성공](https://github.com/user-attachments/assets/41a80994-b582-4b7f-a1f6-d279583ffc8e)

### 2. S3 적용

- S3 적용 화면

![이미지 저장 결과 1](https://github.com/user-attachments/assets/90bd5d69-a9c6-4591-bdf3-320cdbe11463)

![이미지 저장 결과 2](https://github.com/user-attachments/assets/abdbf842-3c11-4dbf-ac84-3980e17fc079)

## 검색 속도 개선

### 1. 인덱스 적용 전

|           항목            |   속도   |              설명               |
|:-----------------------:|:------:|:-----------------------------:|
|     JPQL      | 451 ms |     검색 후 모든 컬럼을 조회하도록 했다.     |
| JPQL +  DTO  | 331 ms |  검색 후 DTO 에 필요한 열만 조회되도록 했다.  |
|        Query DSL        | 376 ms | Query DSL 을 이용하여 검색했다. (QDTO) |
|       Cache 최초 검색       | 624 ms |  Redis Cache 를 이용해 최초 검색 했다.  |
|       Cache 2회 검색       | 33 ms  |          Redis Cache 를 이용해 최초 검색 했다.   |

- 최초 검색 시 JPQL + DTO 가 제일 빨랐다.
- Cache 가 두 번 검색 될 시에는 가장 짧았다.

### 2. 인덱스 적용 후

|           항목            |   속도   |              설명               |
|:-----------------------:|:------:|:-----------------------------:|
|     JPQL      | 75 ms  |     검색 후 모든 컬럼을 조회하도록 했다.     |
| JPQL +  DTO  |  8 ms  |  검색 후 DTO 에 필요한 열만 조회되도록 했다.  |
|        Query DSL        | 76 ms  | Query DSL 을 이용하여 검색했다. (QDTO) |
|       Cache 최초 검색       | 430 ms |  Redis Cache 를 이용해 최초 검색 했다.  |
|       Cache 2회 검색       | 29 ms  |          Redis Cache 를 이용해 최초 검색 했다.   |

- 최초 검색 시에는 JPQL + DTO 가 성능이 압도적으로 좋았다.
- Cache 가 두 번 검색 되어도 JPQL + DTO 가 짧은 걸 알 수 있다.

=> 단순 조회시에는 인덱스 적용 + JPQL + DTO 가 좋다는 걸 알았다.
