package cities

import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.GeometryFactory
import com.vividsolutions.jts.geom.PrecisionModel
import grails.gorm.transactions.Transactional

import geoscript.geom.Bounds
import geoscript.render.Map as GeoScriptMap
import geoscript.workspace.Directory
import geoscript.workspace.PostGIS
import static geoscript.style.Symbolizers.*
import geoscript.layer.Layer

import javax.imageio.ImageIO
import java.awt.Color
import java.awt.image.BufferedImage
import java.nio.Buffer

@Transactional
class CityService {

    def messageSource

    def gettile(parems) {
        println parems
        def coords = parems['BBOX'].toString().split(',')*.toDouble()
        PostGIS postgis = new PostGIS("cities-dev", user: "postgres")
        Layer cities = postgis['cities']
        cities.style = shape(type: "star", size: 4, color: "#ff00ff")
        def map = new GeoScriptMap(
                width: 256,
                height: 256,
                proj: "EPSG:4326",
                bounds: new Bounds(coords[1], coords[0], coords[3], coords[2], "EPSG:4326"),
                layers: [postgis["world_adm0"], postgis["statesp020"], cities]
        )
        def image = map.renderToImage()
        def gtd = image.createGraphics()
        gtd.color = Color.RED
        gtd.drawRect(0,0,254,254)
        gtd.dispose()
        def ostream = new ByteArrayOutputStream()
        ImageIO.write(image, "png", ostream)
        map.close()
        postgis.close()
        return [contentType: "image/png", file: ostream.toByteArray()]
    }

    def loadCsv(File csvfile) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326)
        csvfile.eachLine {line ->
            String[] tokens = line.split(",")
            City city = new City(name:tokens[0], country:tokens[1], population:tokens[2].toInteger(),
                    capital:tokens[3]=="Y", longitude:tokens[4].toDouble(), latitude:tokens[5].toDouble())
            city.location = geometryFactory.createPoint(new Coordinate(city.longitude, city.latitude))
            if (!city.save()) {
                city.errors.allErrors.each {
                    println (messageSource.getMessage(it, Locale.default))
                }
            }
        }

    }
}
