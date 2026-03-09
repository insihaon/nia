## 🚀 실행 순서

### 1단계: Elasticsearch 실행
```bash
cd chatbot/elasticsearch
docker-compose -f docker-compose-light.yml up --build
```

### 2단계: 데이터 초기화 (최초 한 번만)
```bash
cd chatbot/1.chatbot
python data_initializer.py
```

### 3단계: 챗봇 서비스 실행
```bash
python main.py
```