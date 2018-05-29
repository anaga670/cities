package cities

import com.vividsolutions.jts.geom.Point

class City {

    String name;
    String country;
    Integer population;
    Boolean capital;
    Double latitude;
    Double longitude;
    Point location;



    static constraints = {

        name()
        country()
        population min: 1
        capital()
        latitude()
        longitude()
        location()
    }
}
