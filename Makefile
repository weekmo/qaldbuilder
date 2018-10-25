default: build push

build:
	mvn clean deploy -U -Dmaven.test.skip=true 
	
push: 	
	git add .
	git commit -m "update"
	git push