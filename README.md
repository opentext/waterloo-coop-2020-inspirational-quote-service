# waterloo-coop-2020-inspirational-quote-service
This is a demo project to showcase the use of spring-boot framework. 
The application fetches a live quote from an external API then displays the message on a web server.

InspirationalQuoteServiceApplication:
The main application for starting the program.
Implements @EnableCaching(proxyTargetClass = true) notation to enable caching and @EnableScheduling to enable scheduling for automatic cache flush.

Controller:
Mvc controller to handle mapping to the "\quote" route.
Returns a POJO Quote after routing. The routing utilizes two services, "LocalQuoteRepository" and "RemoteQuoteRepository" with @Autowired annotation for dependency injection.

QuoteRepository:
An interface to be used for implementing services with fetchJSON() function to fetch a JSON file.

RemoteQuoteRepository:"
A service, which implements QuoteRepository to fetch live response from the "theysaidso" API as a JSONObject. 
The JSONObject is cached with @Cacheable annotation and lives until midnight, then automatically flushed with @Scheduled and @CacheEvict. 
The fetchJSON() function has @Qualifier to be recognized by @Autowired in Controller.

LocalQuoteRepository:
A service, which implements QuoteRepository to load local JSON from the resources.
The fetchJSON() function has @Qualifier to be recognized by @Autowired in Controller.

Quote:
A POJO to store the necessary information to be displayed by the webserver.


## How to run ##
1. after pull from this branch run `docker compose up`
2. run `mvn spring-boot:run` or run the project from IDE
3. open `localhost:9000/` to check the graylog
4. create an **TCP** input and check the logging