# Insider demo

Repo for demo

# Run locally:

- Run `docker run -p 9200:9200 docker.elastic.co/elasticsearch/elasticsearch:6.3.2`
- Run play app in IDEA OR run `sbt run`
- When compilation is triggered, initial data will be uploaded in ES, and boostings will be applied
        
- Run to search (not typo):

        curl -X GET \
          'http://localhost:9000/search?pattern=ipone'
          
# Run front-end

- `cd fe`
- `npm i`
- `npm run start`
