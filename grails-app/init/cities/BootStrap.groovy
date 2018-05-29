package cities

import org.geotools.factory.Hints


class BootStrap {

    CityService cityService

    def init = { servletContext ->
        Hints.putSystemDefault( Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE )
        File file = new File("cities.csv")
        cityService.loadCsv(file)
    }
    def destroy = {
    }
}
