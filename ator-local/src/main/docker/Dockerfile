FROM openjdk:8
MAINTAINER marcos.arruda@moip.com.br
VOLUME /tmp
EXPOSE 8082

ENV USER_NAME ator-local
ENV APP_HOME /home/$USER_NAME/app
ENV APP_NAME $USER_NAME-1.0-SNAPSHOT.jar

RUN useradd -ms /bin/bash $USER_NAME
RUN mkdir $APP_HOME

ADD wait-for-it.sh $APP_HOME/wait-for-it.sh
RUN chown $USER_NAME $APP_HOME/wait-for-it.sh
RUN chmod +x $APP_HOME/wait-for-it.sh

ADD $APP_NAME $APP_HOME/$USER_NAME.jar
RUN chown $USER_NAME $APP_HOME/$USER_NAME.jar

USER $USER_NAME
WORKDIR $APP_HOME
RUN bash -c "touch $USER_NAME.jar"

CMD java -jar $USER_NAME.jar $CONNECT_BOOTSTRAP_SERVERS
