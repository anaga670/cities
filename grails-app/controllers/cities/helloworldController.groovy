package cities

class helloworldController {

    def index() {
        String name = params.name ?: "World";
        //render "Hello ${name} ${new Date()}";
        [message: "Hello ${name} ${new Date()}"];
    }
}
