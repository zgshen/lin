
密码模式测试，获取 jwt token：  
http://localhost:8081/auth/oauth/token?grant_type=password&username=user_1&password=123456&client_id=client_1&client_secret=123456  
返回  
```json
{
    "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MTQ2MjU0MDgsInVzZXJfbmFtZSI6InVzZXJfMSIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiI4NWY5MzU5ZC05MjcyLTRiZjctYWJhYS1iYWYyODQwZGIyYTEiLCJjbGllbnRfaWQiOiJjbGllbnRfMSIsInNjb3BlIjpbImFsbCJdfQ.ZGEHLWnFTCpWoTIQxXtPYwvVPNbuQUF7RuPvWFK6XyQ",
    "token_type": "bearer",
    "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJ1c2VyXzEiLCJzY29wZSI6WyJhbGwiXSwiYXRpIjoiODVmOTM1OWQtOTI3Mi00YmY3LWFiYWEtYmFmMjg0MGRiMmExIiwiZXhwIjoxNjE0ODc3NDA4LCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwianRpIjoiOWM0YjJjMTktOTQxNS00OWI3LThhYzktOGJlMDQ4MDNlZmU5IiwiY2xpZW50X2lkIjoiY2xpZW50XzEifQ.S3Htis_fOLuQU1c8UNQkk_SBzZDqKR8v3KXDiY0qI4s",
    "expires_in": 6942,
    "scope": "all",
    "jti": "85f9359d-9272-4bf7-abaa-baf2840db2a1"
}
```

验证 jwt token ：
http://localhost:8081/auth/oauth/check_token?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MTQ2MjU0MDgsInVzZXJfbmFtZSI6InVzZXJfMSIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiI4NWY5MzU5ZC05MjcyLTRiZjctYWJhYS1iYWYyODQwZGIyYTEiLCJjbGllbnRfaWQiOiJjbGllbnRfMSIsInNjb3BlIjpbImFsbCJdfQ.ZGEHLWnFTCpWoTIQxXtPYwvVPNbuQUF7RuPvWFK6XyQ  
返回  
```json
{
    "user_name": "user_1",
    "scope": [
        "all"
    ],
    "active": true,
    "exp": 1614625408,
    "authorities": [
        "ROLE_USER"
    ],
    "jti": "85f9359d-9272-4bf7-abaa-baf2840db2a1",
    "client_id": "client_1"
}
```

