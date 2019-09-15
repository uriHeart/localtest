# Argo
Argo

AWS RDS Connection Info  
Name: argo-db  
Host: argo-db.cnkxzmhdcjyk.ap-northeast-2.rds.amazonaws.com  
User: argo  
Password: argo2020  
Database: argo  

Cassandra Connection Info  
Host: 54.180.151.82  
User: argo  
Password: argo2020  
Keyspace: argo  
  
AWS 접속 URL: https://aitify.signin.aws.amazon.com/console  
초기 계정 비밀번호: argo2020


카산드라 로컬 환경 
 - download : http://www.apache.org/dyn/closer.lua/cassandra/3.11.4/apache-cassandra-3.11.4-bin.tar.gz
 - cassandra yaml 오버라이드 : git repository 에 있는 cassandra.yaml 파일 오버라이드
 - 사용자 권한 설정 변경
    1. bin 디렉토리에서 cqlsh -u cassandra -p cassandra
    2. CREATE USER argo WITH PASSWORD 'argo2020' SUPERUSER;
    3. exit
    4. cqlsh -u argo -p argo2020
    5. drop user cassandra;
    6. CREATE KEYSPACE argo WITH REPLICATION = {'class' : 'SimpleStrategy', 'replication_factor': 3};
