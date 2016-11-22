# cloud-coe
RabbitMQ project with Topic exchange using spring-boot

Producer and Consumer should not be run at the same time. Because they are going to be run in the same port. Whatever application runs next without stopping the first one will throw error.

##Server
Producer application will send messages as JSON

##Client
Consumer application will receive the message as JSON but handler will convert it back to Java object
