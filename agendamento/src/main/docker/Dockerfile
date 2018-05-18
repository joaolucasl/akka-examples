FROM openjdk:8
MAINTAINER marcos.arruda@moip.com.br
VOLUME /tmp
EXPOSE 8082

ENV USER_NAME step-user
ENV APP_HOME /home/$USER_NAME/app

RUN useradd -ms /bin/bash $USER_NAME
RUN mkdir $APP_HOME

ADD wait-for-it.sh $APP_HOME/wait-for-it.sh
RUN chown $USER_NAME $APP_HOME/wait-for-it.sh
RUN chmod +x $APP_HOME/wait-for-it.sh

ADD step-a1-0.0.1.jar $APP_HOME/step.jar
RUN chown $USER_NAME $APP_HOME/step.jar

USER $USER_NAME
WORKDIR $APP_HOME
RUN bash -c 'touch step.jar'

CMD java -jar step.jar $CONNECT_BOOTSTRAP_SERVERS
