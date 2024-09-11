# HOW TO RUN locally:

Pull docker image: https://hub.docker.com/r/divertito/text-matcher and execute `docker run`.
There several option available to setup matcher: 

--matchers-amount 

--batch-size 

--words-matching 

--source-path

### example:

`docker run text-matcher:latest --matchers-amount=10 --batch-size=10000 --words-matching=James,John,Robert,Michael,William,David,Richard,Charles,Joseph,Thomas,Christopher,Daniel,Paul,Mark,Donald,George,Kenneth,Steven,Edward,Brian,Ronald,Anthony,Kevin,Jason,Matthew,Gary,Timothy,Jose,Larry,Jeffrey,Frank,Scott,Eric,Stephen,Andrew,Raymond,Gregory,Joshua,Jerry,Dennis,Walter,Patrick,Peter,Harold,Douglas,Henry,Carl,Arthur,Ryan,Roger`



### Another way to run matcher locally is to:

clone this repo -> mvn clean package -> java -jar 'pathToJar'

Same options available 

