package cities

class CityController {

    static scaffold = City

    def cityService

    def gettile() {
        render cityService.gettile(params)
    }

}
